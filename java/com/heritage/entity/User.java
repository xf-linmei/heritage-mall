package com.heritage.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类，对应数据库 user 表
 * 前台注册用户与后台管理员共用一张表，用 role 字段区分（USER / ADMIN）
 * 存入 session，因此实现 Serializable 接口
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;     // 用户ID，主键自增
    private String username;    // 登录账号
    private String password;    // 登录密码（MD5 32位密文）
    private String realName;    // 真实姓名/昵称
    private String phone;       // 手机号
    private String email;       // 邮箱
    private String role;        // 角色：USER / ADMIN
    private Integer status;     // 状态：1启用 0禁用
    private Date createTime;    // 注册时间

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    /** 是否是管理员（JSP 中可直接用 ${loginUser.admin} 判断） */
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}
