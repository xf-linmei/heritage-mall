package com.heritage.service;

import com.heritage.common.Page;
import com.heritage.entity.Cart;
import com.heritage.entity.Orders;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单业务接口
 *
 * <p>订单状态流转：0待付款 →（支付）→ 1待发货 →（后台发货）→ 2待收货 →（确认收货）→ 3已完成
 * 待付款状态下用户可取消 → 4已取消</p>
 */
public interface OrderService {

    /** 查询购物车中已勾选的商品（结算页展示用） */
    List<Cart> getCheckedCart(Integer userId);

    /** 已勾选商品的总金额 */
    BigDecimal getCheckedTotal(Integer userId);

    /**
     * 下单（Spring 声明式事务的典型场景）
     *
     * <p>一个事务内完成：插入订单主表 → 插入订单明细 → 扣减商品库存 → 清空已购购物车项。
     * 任一步失败抛出异常事务回滚，保证不会出现"有订单没明细"或"库存被扣但订单没生成"的脏数据。</p>
     *
     * @throws RuntimeException 购物车为空、地址无效、商品下架、库存不足等情况抛出
     */
    Integer createOrder(Integer userId, Integer addressId, String remark);

    /** 我的订单列表 */
    List<Orders> listMyOrders(Integer userId);

    /** 订单详情（含明细列表） */
    Orders getOrderDetail(Integer orderId);

    /** 模拟支付：待付款 → 待发货，返回影响行数（0 表示状态不对） */
    int payOrder(Integer orderId);

    /** 确认收货：待收货 → 已完成 */
    int confirmReceive(Integer orderId);

    /** 取消订单（仅待付款可取消），并回补库存 */
    int cancelOrder(Integer orderId);

    /** 后台：待发货 → 待收货 */
    int deliverOrder(Integer orderId);

    /** 后台：按订单状态 + 关键字分页查询订单 */
    Page<Orders> listAdminOrders(Integer status, String keyword, int pageNo, int pageSize);
}
