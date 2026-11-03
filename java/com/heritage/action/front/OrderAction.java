package com.heritage.action.front;

import com.heritage.common.BaseAction;
import com.heritage.entity.Address;
import com.heritage.entity.Cart;
import com.heritage.entity.Orders;
import com.heritage.service.AddressService;
import com.heritage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

/**
 * 前台订单 Action：结算页、下单（事务）、模拟支付、我的订单、订单详情、确认收货、取消订单
 *
 * <p><b>重点</b>：create() 本身不写任何 SQL，它只调用 OrderService.createOrder()，
 * 由 Spring 声明式事务保证"订单主表 + 明细 + 扣库存"同成功同失败。</p>
 */
@Controller("com.heritage.action.front.OrderAction")
@Scope("prototype")
public class OrderAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressService addressService;

    // ==================== 接收参数 ====================
    private Integer addressId;      // 选择的收货地址
    private String remark;          // 买家留言
    private Integer orderId;

    // ==================== 页面数据 ====================
    private List<Cart> checkedCartList;   // 结算时勾选的商品
    private List<Address> addressList;    // 收货地址列表
    private BigDecimal total;             // 应付金额
    private Orders order;                 // 订单详情
    private List<Orders> orderList;       // 我的订单

    /** 结算页：展示勾选的商品、收货地址与应付金额 */
    public String checkout() {
        checkedCartList = orderService.getCheckedCart(getLoginUserId());
        total = orderService.getCheckedTotal(getLoginUserId());
        addressList = addressService.listAddress(getLoginUserId());
        return SUCCESS;   // 没勾选商品时页面会给出提示
    }

    /**
     * 下单：调用 Service（内部开启事务）
     * 成功/失败信息都通过一次性提示带回列表页
     */
    public String create() {
        try {
            orderService.createOrder(getLoginUserId(), addressId, remark);
            setFlashMsg("下单成功！请在订单列表中完成支付");
        } catch (RuntimeException e) {
            // 订单创建失败时事务已回滚，库存不会被扣
            setFlashMsg("下单失败：" + e.getMessage());
        }
        return SUCCESS;
    }

    /** 模拟支付：待付款 → 待发货 */
    public String pay() {
        int rows = orderService.payOrder(orderId);
        setFlashMsg(rows > 0 ? "支付成功，商家即将发货" : "支付失败：只有待付款的订单可以支付");
        return SUCCESS;
    }

    /** 我的订单 */
    public String list() {
        orderList = orderService.listMyOrders(getLoginUserId());
        return SUCCESS;
    }

    /** 订单详情（带明细列表），并做归属校验，防止查看别人的订单 */
    public String detail() {
        order = orderService.getOrderDetail(orderId);
        if (order == null || !order.getUserId().equals(getLoginUserId())) {
            addActionError("订单不存在或无权查看");
            return ERROR;
        }
        return SUCCESS;
    }

    /** 确认收货：待收货 → 已完成 */
    public String confirmReceive() {
        Orders temp = orderService.getOrderDetail(orderId);
        if (temp == null || !temp.getUserId().equals(getLoginUserId())) {
            setFlashMsg("操作失败：订单不存在或无权操作");
            return SUCCESS;
        }
        int rows = orderService.confirmReceive(orderId);
        setFlashMsg(rows > 0 ? "已确认收货，订单完成" : "操作失败：只在待收货状态可确认");
        return SUCCESS;
    }

    /** 取消订单：仅待付款可取消，Service 内会回补库存 */
    public String cancel() {
        Orders temp = orderService.getOrderDetail(orderId);
        if (temp == null || !temp.getUserId().equals(getLoginUserId())) {
            setFlashMsg("操作失败：订单不存在或无权操作");
            return SUCCESS;
        }
        int rows = orderService.cancelOrder(orderId);
        setFlashMsg(rows > 0 ? "订单已取消，库存已回补" : "取消失败：只有待付款的订单可以取消");
        return SUCCESS;
    }

    // ==================== getter / setter ====================
    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<Cart> getCheckedCartList() {
        return checkedCartList;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Orders getOrder() {
        return order;
    }

    public List<Orders> getOrderList() {
        return orderList;
    }
}
