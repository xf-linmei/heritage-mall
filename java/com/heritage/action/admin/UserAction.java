package com.heritage.action.admin;

import com.heritage.common.BaseAction;
import com.heritage.common.Constant;
import com.heritage.entity.User;
import com.heritage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 后台用户管理 Action：查看全部用户、禁用 / 启用账号
 */
@Controller("com.heritage.action.admin.UserAction")
@Scope("prototype")
public class UserAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserService userService;

    private Integer userId;
    private Integer status;
    private List<User> userList;

    /** 用户列表 */
    public String list() {
        userList = userService.listAll();
        return SUCCESS;
    }

    /**
     * 禁用 / 启用账号
     * 安全考虑：不允许禁用当前登录的管理员自己，否则会被踢出后台
     */
    public String updateStatus() {
        if (userId != null && userId.equals(getLoginUserId())) {
            setFlashMsg("不能禁用当前登录的账号");
            return SUCCESS;
        }
        userService.updateStatus(userId, status);
        setFlashMsg(status != null && status == Constant.STATUS_ON ? "账号已启用" : "账号已禁用");
        return SUCCESS;
    }

    // ==================== getter / setter ====================
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<User> getUserList() {
        return userList;
    }
}
