package com.heritage.entity;

import java.util.Date;

/**
 * 商品收藏实体类，对应数据库 collect 表
 *
 * <p>product 是非数据库字段：我的收藏页面要展示商品名称、主图、价格，
 * 由 CollectMapper 通过 association 关联查询填充。</p>
 */
public class Collect {

    private Integer collectId;   // 收藏ID，主键自增
    private Integer userId;      // 用户ID
    private Integer productId;   // 商品ID
    private Date createTime;     // 收藏时间

    /** 关联的商品对象（非数据库字段） */
    private Product product;

    public Integer getCollectId() {
        return collectId;
    }

    public void setCollectId(Integer collectId) {
        this.collectId = collectId;
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
}
