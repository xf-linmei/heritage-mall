package com.heritage.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文创商品实体类，对应数据库 product 表
 *
 * <p>说明：detail_images 字段在数据库中用英文逗号分隔存多张详情图地址，
 * 这里额外提供 getDetailImageList() 把字符串切成 List，方便 JSP 用 c:forEach 遍历。</p>
 */
public class Product {

    private Integer productId;     // 商品ID，主键自增
    private Integer categoryId;    // 所属分类ID
    private String productName;    // 商品名称
    private String productDesc;    // 非遗文化介绍
    private String mainImage;      // 商品主图地址
    private String detailImages;   // 详情图地址（多个用英文逗号分隔）
    private BigDecimal price;      // 商品价格
    private Integer stock;         // 库存
    private Integer sales;         // 销量
    private Integer status;        // 上架状态：1上架 0下架
    private Integer isHot;         // 是否热门：1是 0否
    private Integer isNew;         // 是否新品：1是 0否
    private Date createTime;       // 创建时间

    // ==================== 非数据库字段（页面展示用）====================
    /** 所属分类名称（列表页/详情页展示，由 Mapper 关联查询填充） */
    private String categoryName;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String detailImages) {
        this.detailImages = detailImages;
    }

    /**
     * 把逗号分隔的详情图字符串切成 List，供 JSP 循环渲染
     */
    public List<String> getDetailImageList() {
        List<String> list = new ArrayList<String>();
        if (detailImages != null && detailImages.trim().length() > 0) {
            String[] arr = detailImages.split(",");
            for (String s : arr) {
                if (s.trim().length() > 0) {
                    list.add(s.trim());
                }
            }
        }
        return list;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
