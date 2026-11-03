package com.heritage.service;

import com.heritage.entity.User;

import java.util.List;

/**
 * 用户业务接口
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @return 登录成功返回用户对象（含 userId、role），失败返回 null
     */
    User login(String username, String password);

    /**
     * 用户注册（注册的角色固定为普通用户 USER）
     *
     * @return true 注册成功；false 账号已存在或参数不合法
     */
    boolean register(User user);

    /** 查询全部用户（后台用户管理） */
    List<User> listAll();

    /** 修改个人信息（真实姓名、手机号、邮箱） */
    boolean updateProfile(User user);

    /**
     * 修改密码（必须先验证旧密码）
     *
     * @return true 修改成功；false 旧密码不正确或用户不存在
     */
    boolean updatePassword(Integer userId, String oldPassword, String newPassword);

    /** 启用 / 禁用账号（后台） */
    int updateStatus(Integer userId, Integer status);
}
