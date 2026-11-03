package com.heritage.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车实体类，对应数据库 cart 表
 *
 * <p>product 是非数据库字段：购物车列表需要展示商品名称、主图、单价、库存，
 * 由 CartMapper 通过 association 关联查询 product 表填充。</p>
 */
public class Cart {

    private Integer cartId;      // 购物车ID，主键自增
    private Integer userId;      // 用户ID
    private Integer productId;   // 商品ID
    private Integer quantity;    // 购买数量
    private Integer checked;     // 是否勾选结算：1勾选 0未勾选
    private Date createTime;     // 加入时间

    /** 关联的商品对象（非数据库字段） */
    private Product product;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * 小计金额 = 商品单价 × 数量（页面自动计算总金额时用到）
     */
    public BigDecimal getSubtotal() {
        if (product == null || product.getPrice() == null || quantity == null) {
            return new BigDecimal("0");
        }
        return product.getPrice().multiply(new BigDecimal(quantity));
    }
}
