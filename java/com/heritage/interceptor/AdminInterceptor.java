package com.heritage.interceptor;

import com.heritage.common.Constant;
import com.heritage.entity.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.Map;

/**
 * 管理员拦截器（后台权限校验）
 *
 * <p>作用：非管理员（普通用户 USER）访问 /admin/** 下的任何 Action 时，
 * 返回 noPermission，由 struts.xml 全局结果转发到"无权限访问"页面。</p>
 *
 * <p>注意：adminStack 的顺序是 adminInterceptor → loginInterceptor → defaultStack，
 * 所以未登录时会先被本拦截器挡住（返回 login），再进入登录拦截器，逻辑不会冲突。</p>
 */
public class AdminInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Map<String, Object> session = invocation.getInvocationContext().getSession();
        User user = (User) session.get(Constant.SESSION_USER);

        if (user == null) {
            return "login";                       // 未登录
        }
        if (!Constant.ROLE_ADMIN.equals(user.getRole())) {
            return "noPermission";                // 已登录但不是管理员
        }
        return invocation.invoke();
    }
}
