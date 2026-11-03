package com.heritage.entity;

import java.util.Date;

/**
 * 非遗分类实体类，对应数据库 category 表
 * 例如：剪纸、扎染、木雕、银饰
 */
public class Category {

    private Integer categoryId;    // 分类ID，主键自增
    private String categoryName;   // 分类名称
    private String categoryDesc;   // 分类简介（非遗文化背景）
    private Integer sortOrder;     // 排序值，越小越靠前
    private Date createTime;       // 创建时间

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
