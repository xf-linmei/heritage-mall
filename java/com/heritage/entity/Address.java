package com.heritage.entity;

import java.util.Date;

/**
 * 收货地址实体类，对应数据库 address 表
 * 一个用户可以有多个地址，最多一个默认地址（is_default = 1）
 */
public class Address {

    private Integer addressId;      // 地址ID，主键自增
    private Integer userId;         // 所属用户ID
    private String receiver;        // 收货人姓名
    private String phone;           // 收货人手机号
    private String addressDetail;   // 详细收货地址
    private Integer isDefault;      // 是否默认地址：1默认 0非默认
    private Date createTime;        // 创建时间

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
