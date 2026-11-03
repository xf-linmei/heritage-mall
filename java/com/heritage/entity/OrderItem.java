package com.heritage.entity;

import java.math.BigDecimal;

/**
 * 订单明细实体类，对应数据库 order_item 表
 *
 * <p>一条明细 = 订单中的一个商品。下单时把商品名称、主图、单价"快照"下来，
 * 以后商品价格调整，历史订单金额保持不变。</p>
 */
public class OrderItem {

    private Integer itemId;        // 明细ID，主键自增
    private Integer orderId;       // 所属订单ID
    private Integer productId;     // 商品ID
    private String productName;    // 商品名称（快照）
    private String productImage;   // 商品主图（快照）
    private BigDecimal price;      // 下单时单价（快照）
    private Integer quantity;      // 购买数量
    private BigDecimal subtotal;   // 小计 = price × quantity

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
