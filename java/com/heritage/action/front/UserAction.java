package com.heritage.action.front;

import com.heritage.common.BaseAction;
import com.heritage.entity.User;
import com.heritage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

/**
 * 前台用户 Action：注册、登录、退出、个人资料、修改密码
 *
 * <p>页面表单字段（username、password…）由 Struts2 自动注入到同名属性，
 * 不需要 request.getParameter()。</p>
 */
@Controller("com.heritage.action.front.UserAction")
@Scope("prototype")
public class UserAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserService userService;

    // ==================== 接收参数 ====================
    private String username;
    private String password;
    private String confirmPassword;
    private String realName;
    private String phone;
    private String email;
    private String oldPassword;
    private String newPassword;

    /** 跳转登录页 */
    public String toLogin() {
        return SUCCESS;
    }

    /** 跳转注册页 */
    public String toRegister() {
        return SUCCESS;
    }

    /**
     * 登录：校验账号密码 → 成功后把用户对象放入 session
     */
    public String login() {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            addActionError("请输入账号和密码");
            return INPUT;
        }
        User user = userService.login(username.trim(), password);
        if (user == null) {
            addActionError("账号或密码错误，或该账号已被禁用");
            return INPUT;
        }
        setLoginUser(user);          // 登录成功：存入 session（键 loginUser）
        return SUCCESS;              // → 重定向到首页
    }

    /**
     * 注册：参数校验 → 调用 Service（内部做 MD5 加密与账号查重）
     */
    public String register() {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            addActionError("账号和密码不能为空");
            return INPUT;
        }
        if (password.length() < 6 || password.length() > 20) {
            addActionError("密码长度需为 6 ~ 20 位");
            return INPUT;
        }
        if (!password.equals(confirmPassword)) {
            addActionError("两次输入的密码不一致");
            return INPUT;
        }
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password);          // Service 内部会转成 MD5 再入库
        user.setRealName(realName);
        user.setPhone(phone);
        user.setEmail(email);
        if (!userService.register(user)) {
            addActionError("该账号已被注册，请换一个账号");
            return INPUT;
        }
        addActionMessage("注册成功，请登录");
        return SUCCESS;                      // → 登录页（转发，保留提示信息）
    }

    /** 退出登录：移除 session 中的用户 */
    public String logout() {
        removeLoginUser();
        return SUCCESS;
    }

    /** 个人中心：把 session 中的用户信息回显到表单 */
    public String profile() {
        User user = getLoginUser();
        if (user != null) {
            username = user.getUsername();
            realName = user.getRealName();
            phone = user.getPhone();
            email = user.getEmail();
        }
        return SUCCESS;
    }

    /** 修改个人信息 */
    public String updateProfile() {
        User user = new User();
        user.setUserId(getLoginUserId());
        user.setRealName(realName);
        user.setPhone(phone);
        user.setEmail(email);
        if (!userService.updateProfile(user)) {
            addActionError("修改失败，请稍后再试");
            return INPUT;
        }
        // 同步刷新 session 中的用户信息，页头昵称才能立刻更新
        User sessionUser = getLoginUser();
        sessionUser.setRealName(realName);
        sessionUser.setPhone(phone);
        sessionUser.setEmail(email);
        setLoginUser(sessionUser);
        setFlashMsg("个人信息修改成功");
        return SUCCESS;
    }

    /** 跳转修改密码页 */
    public String toPassword() {
        return SUCCESS;
    }

    /** 修改密码：必须验证原密码 */
    public String updatePassword() {
        if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            addActionError("请填写原密码与新密码");
            return INPUT;
        }
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            addActionError("新密码长度需为 6 ~ 20 位");
            return INPUT;
        }
        if (!newPassword.equals(confirmPassword)) {
            addActionError("两次输入的新密码不一致");
            return INPUT;
        }
        if (!userService.updatePassword(getLoginUserId(), oldPassword, newPassword)) {
            addActionError("原密码不正确");
            return INPUT;
        }
        addActionMessage("密码修改成功，下次登录请使用新密码");
        return SUCCESS;
    }

    // ==================== getter / setter ====================
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
