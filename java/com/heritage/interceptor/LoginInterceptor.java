package com.heritage.interceptor;

import com.heritage.common.Constant;
import com.heritage.entity.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.Map;

/**
 * 登录拦截器
 *
 * <p>作用：未登录用户访问"个人中心 / 购物车 / 结算 / 订单 / 地址 / 收藏"等页面时，
 * 直接跳转到登录页（struts.xml 中名字为 login 的全局结果）。</p>
 *
 * <p>注册方式：在 struts.xml 的 base 包中声明，并组装进 loginStack / adminStack，
 * 需要登录的 Action 通过 {@code <interceptor-ref name="loginStack"/>} 引用。</p>
 */
public class LoginInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        // 通过 ActionContext 取 session，不依赖 Servlet API
        Map<String, Object> session = invocation.getInvocationContext().getSession();
        User user = (User) session.get(Constant.SESSION_USER);

        if (user == null) {
            return "login";                       // 未登录 → 跳登录页
        }
        if (user.getStatus() == null || user.getStatus() != Constant.STATUS_ON) {
            // 账号在登录期间被管理员禁用：踢下线
            session.remove(Constant.SESSION_USER);
            return "login";
        }
        // 已登录且账号正常：放行，继续执行后续拦截器与 Action
        return invocation.invoke();
    }
}
