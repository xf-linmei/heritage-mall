package com.heritage.common;

import com.heritage.entity.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Map;

/**
 * Action 基类：所有前台/后台 Action 都继承它
 *
 * <p>封装了三件几乎每个 Action 都要做的事：</p>
 * <ol>
 *   <li>从 session 中取登录用户（Struts2 用 ActionContext 操作 session，不依赖 Servlet API）；</li>
 *   <li>把用户放入/移除 session；</li>
 *   <li>校验登录用户数据归属（防止越权操作别人的购物车、订单、地址）。</li>
 * </ol>
 */
public class BaseAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    /** 获取 session Map（等价于 Servlet 的 HttpSession.setAttribute） */
    protected Map<String, Object> getSession() {
        return ActionContext.getContext().getSession();
    }

    /** 获取当前登录用户对象，未登录返回 null */
    protected User getLoginUser() {
        return (User) getSession().get(Constant.SESSION_USER);
    }

    /** 获取当前登录用户ID，未登录返回 null */
    protected Integer getLoginUserId() {
        User user = getLoginUser();
        return user == null ? null : user.getUserId();
    }

    /** 登录成功后把用户放入 session */
    protected void setLoginUser(User user) {
        getSession().put(Constant.SESSION_USER, user);
    }

    /** 退出登录：移除 session 中的用户 */
    protected void removeLoginUser() {
        getSession().remove(Constant.SESSION_USER);
    }

    /** 判断当前登录用户是否是管理员 */
    protected boolean isAdmin() {
        User user = getLoginUser();
        return user != null && Constant.ROLE_ADMIN.equals(user.getRole());
    }

    /**
     * 存入一条"一次性提示信息"（可跨重定向显示）
     *
     * <p>背景：本项目写操作都用 redirectAction 跳转（防止刷新重复提交），
     * 而 addActionError() 的信息在重定向后会丢失，所以把提示暂存在 session 中，
     * 页面显示后由 JSP 把它移除。</p>
     */
    protected void setFlashMsg(String msg) {
        getSession().put(Constant.SESSION_FLASH_MSG, msg);
    }
}
