package com.heritage.dao;

import com.heritage.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 DAO（Mapper 接口）
 * 只有接口没有实现类：由 applicationContext.xml 中的 MapperScannerConfigurer
 * 自动生成代理实现并注册成 Spring Bean，Service 中直接 @Autowired 注入即可。
 */
public interface UserDao {

    /** 按主键查询用户 */
    User selectById(@Param("userId") Integer userId);

    /** 按登录账号查询用户（登录校验、注册查重） */
    User selectByUsername(@Param("username") String username);

    /** 新增用户（自增主键回填到 user.userId） */
    int insert(User user);

    /** 修改个人信息（真实姓名、手机号、邮箱） */
    int update(User user);

    /** 修改密码 */
    int updatePassword(@Param("userId") Integer userId, @Param("password") String password);

    /** 启用 / 禁用账号（后台用户管理） */
    int updateStatus(@Param("userId") Integer userId, @Param("status") Integer status);

    /** 查询全部用户（后台用户管理，按注册时间倒序） */
    List<User> selectAll();
}
