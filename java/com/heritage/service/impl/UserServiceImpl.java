package com.heritage.service.impl;

import com.heritage.common.Constant;
import com.heritage.dao.UserDao;
import com.heritage.entity.User;
import com.heritage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用户业务实现类
 *
 * <p>密码使用 Spring 提供的 DigestUtils 做 MD5 摘要后再比对，
 * 与数据库初始化脚本里的 MySQL MD5() 结果一致（都是对 UTF-8 字节做 MD5），
 * 数据库中不存明文密码。</p>
 *
 * <p>被 applicationContext.xml 中的 AOP 切面织入声明式事务。</p>
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        User user = userDao.selectByUsername(username.trim());
        if (user == null) {
            return null;                       // 账号不存在
        }
        String inputMd5 = md5(password);
        if (!inputMd5.equals(user.getPassword())) {
            return null;                       // 密码错误
        }
        if (user.getStatus() == null || user.getStatus() != Constant.STATUS_ON) {
            return null;                       // 账号已被管理员禁用
        }
        return user;
    }

    @Override
    public boolean register(User user) {
        if (user == null || !StringUtils.hasText(user.getUsername())
                || !StringUtils.hasText(user.getPassword())) {
            return false;
        }
        // 账号重名校验
        if (userDao.selectByUsername(user.getUsername().trim()) != null) {
            return false;
        }
        user.setUsername(user.getUsername().trim());
        user.setPassword(md5(user.getPassword()));   // 明文转 MD5 后再入库
        user.setRole(Constant.ROLE_USER);            // 前台注册一律是普通用户
        user.setStatus(Constant.STATUS_ON);          // 默认启用
        return userDao.insert(user) > 0;
    }

    @Override
    public List<User> listAll() {
        return userDao.selectAll();
    }

    @Override
    public boolean updateProfile(User user) {
        if (user == null || user.getUserId() == null) {
            return false;
        }
        return userDao.update(user) > 0;
    }

    @Override
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword) {
        if (userId == null || !StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            return false;
        }
        User user = userDao.selectById(userId);
        if (user == null) {
            return false;
        }
        // 旧密码必须正确才允许修改
        if (!md5(oldPassword).equals(user.getPassword())) {
            return false;
        }
        return userDao.updatePassword(userId, md5(newPassword)) > 0;
    }

    @Override
    public int updateStatus(Integer userId, Integer status) {
        return userDao.updateStatus(userId, status);
    }

    /** MD5 摘要工具方法 */
    private String md5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }
}
