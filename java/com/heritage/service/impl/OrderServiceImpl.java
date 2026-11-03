package com.heritage.service.impl;

import com.heritage.common.Constant;
import com.heritage.common.Page;
import com.heritage.dao.AddressDao;
import com.heritage.dao.CartDao;
import com.heritage.dao.OrderItemDao;
import com.heritage.dao.OrdersDao;
import com.heritage.dao.ProductDao;
import com.heritage.entity.Address;
import com.heritage.entity.Cart;
import com.heritage.entity.OrderItem;
import com.heritage.entity.Orders;
import com.heritage.entity.Product;
import com.heritage.service.CartService;
import com.heritage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 订单业务实现类 —— 本项目事务控制的核心类
 *
 * <p>createOrder() 方法被 applicationContext.xml 中的 AOP 切面
 * 按 {@code <tx:method name="createOrder*" propagation="REQUIRED" rollback-for="Exception"/>}
 * 织入事务：方法里任何一步抛出 RuntimeException，前面已经写入数据库的订单主表与明细<b>全部回滚</b>，
 * 商品库存也<b>不会被扣掉</b>。</p>
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private CartService cartService;

    @Override
    public List<Cart> getCheckedCart(Integer userId) {
        return cartService.listCheckedCart(userId);
    }

    @Override
    public BigDecimal getCheckedTotal(Integer userId) {
        return cartService.getCheckedTotal(userId);
    }

    @Override
    public Integer createOrder(Integer userId, Integer addressId, String remark) {
        // ===== 1. 取出购物车中勾选的商品 =====
        List<Cart> cartList = cartService.listCheckedCart(userId);
        if (cartList == null || cartList.isEmpty()) {
            throw new RuntimeException("请先在购物车中勾选要结算的文创商品");
        }

        // ===== 2. 校验收货地址 =====
        Address address = addressDao.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("请选择有效的收货地址");
        }

        // ===== 3. 校验商品状态并计算总金额 =====
        BigDecimal totalAmount = new BigDecimal("0");
        for (Cart cart : cartList) {
            Product product = cart.getProduct();
            if (product == null || product.getStatus() == null
                    || product.getStatus() != Constant.STATUS_ON) {
                throw new RuntimeException("商品【" + productName(cart) + "】已下架，请从购物车中移除");
            }
            if (product.getStock() == null || product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("商品【" + productName(cart) + "】库存不足，仅剩 "
                        + (product.getStock() == null ? 0 : product.getStock()) + " 件");
            }
            // 小计 = 单价 × 数量
            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(cart.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
        }

        // ===== 4. 插入订单主表（自增主键回填到 order.orderId，供明细表使用）=====
        Orders order = new Orders();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        order.setStatus(Constant.ORDER_WAIT_PAY);
        // 收货信息"快照"进订单，防止用户以后改地址导致历史订单信息变化
        order.setReceiver(address.getReceiver());
        order.setPhone(address.getPhone());
        order.setAddressDetail(address.getAddressDetail());
        order.setRemark(remark);

        int rows = ordersDao.insert(order);
        if (rows < 1) {
            throw new RuntimeException("订单创建失败，请稍后再试");
        }

        // ===== 5. 循环处理：写明细 + 扣库存 + 清购物车 =====
        for (Cart cart : cartList) {
            Product product = cart.getProduct();
            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(cart.getQuantity()));

            // 5.1 订单明细（把商品名称、主图、单价快照下来）
            OrderItem item = new OrderItem();
            item.setOrderId(order.getOrderId());
            item.setProductId(product.getProductId());
            item.setProductName(product.getProductName());
            item.setProductImage(product.getMainImage());
            item.setPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());
            item.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
            rows = orderItemDao.insert(item);
            if (rows < 1) {
                throw new RuntimeException("订单明细写入失败，事务回滚");
            }

            // 5.2 扣减库存：SQL 自带 stock >= quantity 条件，
            //     库存不足时返回 0，这里抛异常让整个订单事务回滚（防超卖的关键）
            rows = productDao.deductStock(product.getProductId(), cart.getQuantity());
            if (rows < 1) {
                throw new RuntimeException("商品【" + product.getProductName() + "】库存不足，下单失败，事务回滚");
            }

            // 5.3 下单成功后从购物车移出该商品
            cartDao.deleteByUserAndProduct(userId, product.getProductId());
        }

        return order.getOrderId();
    }

    @Override
    public List<Orders> listMyOrders(Integer userId) {
        return ordersDao.selectByUser(userId);
    }

    @Override
    public Orders getOrderDetail(Integer orderId) {
        Orders order = ordersDao.selectById(orderId);
        if (order != null) {
            // 明细单独查出来放进订单对象，JSP 用 order.itemList 遍历
            order.setItemList(orderItemDao.selectByOrder(orderId));
        }
        return order;
    }

    @Override
    public int payOrder(Integer orderId) {
        return ordersDao.payOrder(orderId);
    }

    @Override
    public int confirmReceive(Integer orderId) {
        return ordersDao.finishOrder(orderId);
    }

    @Override
    public int cancelOrder(Integer orderId) {
        // 先把待付款改成已取消；SQL 带 status=0 条件，只有待付款能取消
        int rows = ordersDao.cancelOrder(orderId);
        if (rows > 0) {
            // 取消订单要把库存加回去（销量相应减少），保证数据一致
            List<OrderItem> itemList = orderItemDao.selectByOrder(orderId);
            for (OrderItem item : itemList) {
                productDao.restoreStock(item.getProductId(), item.getQuantity());
            }
        }
        return rows;
    }

    @Override
    public int deliverOrder(Integer orderId) {
        return ordersDao.deliverOrder(orderId);
    }

    @Override
    public Page<Orders> listAdminOrders(Integer status, String keyword, int pageNo, int pageSize) {
        Page<Orders> page = new Page<Orders>(pageNo, pageSize);
        int totalCount = ordersDao.countByCondition(status, keyword);
        page.setTotalCount(totalCount);
        if (totalCount > 0) {
            page.setList(ordersDao.selectByCondition(status, keyword,
                    page.getStartIndex(), page.getPageSize()));
        }
        return page;
    }

    /** 生成订单号：JJ + yyyyMMddHHmmss + 4位随机数，保证基本不重复 */
    private String generateOrderNo() {
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int rand = new Random().nextInt(9000) + 1000;
        return "JJ" + time + rand;
    }

    /** 安全取商品名（避免 NPE 影响错误提示） */
    private String productName(Cart cart) {
        return cart.getProduct() == null ? "该商品" : cart.getProduct().getProductName();
    }
}
