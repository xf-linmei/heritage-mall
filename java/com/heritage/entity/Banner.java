package com.heritage.entity;

import java.util.Date;

/**
 * 首页轮播图实体类，对应数据库 banner 表
 * 后台可管理：上传图片、设置排序、启用/禁用
 */
public class Banner {

    private Integer bannerId;   // 轮播ID，主键自增
    private String title;       // 轮播标题
    private String imageUrl;    // 轮播图片地址（/heritage/upload/xxx.jpg）
    private String linkUrl;     // 点击跳转链接
    private Integer sortOrder;  // 排序值，越小越靠前
    private Integer status;     // 状态：1启用 0禁用
    private Date createTime;    // 创建时间

    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
