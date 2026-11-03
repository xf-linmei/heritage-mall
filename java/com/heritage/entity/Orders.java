package com.heritage.entity;

import com.heritage.common.Constant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单主表实体类，对应数据库 orders 表
 *
 * <p>下单时把收货人/电话/地址"快照"进订单表（receiver、phone、address_detail），
 * 这样用户以后修改地址，历史订单信息也不会跟着变。</p>
 *
 * <p>status：0待付款 1待发货 2待收货 3已完成 4已取消</p>
 */
public class Orders {

    private Integer orderId;        // 订单ID，主键自增
    private String orderNo;         // 订单编号（唯一）
    private Integer userId;         // 下单用户ID
    private BigDecimal totalAmount; // 订单总金额
    private Integer status;         // 订单状态
    private String receiver;        // 收货人（快照）
    private String phone;           // 收货电话（快照）
    private String addressDetail;   // 收货地址（快照）
    private String remark;          // 买家留言
    private Date createTime;        // 下单时间
    private Date payTime;           // 支付时间
    private Date sendTime;          // 发货时间
    private Date finishTime;        // 确认收货时间

    // ==================== 非数据库字段 ====================
    /** 下单用户账号（后台订单列表展示用） */
    private String userName;
    /** 订单明细列表（订单详情展示用） */
    private List<OrderItem> itemList;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
    }

    /** 订单状态的中文名称（JSP 中 ${order.statusName} 直接显示） */
    public String getStatusName() {
        return Constant.getOrderStatusName(status);
    }
}
