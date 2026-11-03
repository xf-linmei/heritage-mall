<%--
  前台公共页头：Logo + 搜索 + 非遗分类导航 + 用户区
  分类列表来自 application 域（由 InitListener 启动时加载）
  本文件使用静态包含（<%@ include %>），所以这里定义的 ctx 变量在包含它的页面中也能用
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<nav class="navbar navbar-expand-lg jj-navbar">
    <div class="container">
        <a class="navbar-brand jj-logo" href="${ctx}/index.action">匠<span>集</span></a>

        <!-- 商品搜索：表单提交到商品列表 Action，Struts2 会自动把 keyword 注入 Action -->
        <form class="jj-search" action="${ctx}/product_list.action" method="get">
            <input class="form-control jj-search-input" type="text" name="keyword"
                   placeholder="搜索文创名称…" value="${param.keyword}">
            <button class="jj-btn" type="submit">搜索</button>
        </form>

        <ul class="navbar-nav me-auto">
            <li class="nav-item"><a class="nav-link" href="${ctx}/index.action">首页</a></li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="cateDrop" data-bs-toggle="dropdown">非遗分类</a>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="${ctx}/product_list.action">全部文创</a></li>
                    <%-- OGNL/EL 两种方式都演示：这里用 JSTL 读取 application 域中的分类列表 --%>
                    <c:forEach items="${applicationScope.categoryList}" var="c">
                        <li>
                            <a class="dropdown-item" href="${ctx}/product_list.action?categoryId=${c.categoryId}">
                                    ${c.categoryName}
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </li>
            <li class="nav-item"><a class="nav-link" href="${ctx}/cart_list.action">购物车</a></li>
            <li class="nav-item"><a class="nav-link" href="${ctx}/order_list.action">我的订单</a></li>
            <li class="nav-item"><a class="nav-link" href="${ctx}/collect_list.action">我的收藏</a></li>
        </ul>

        <div class="jj-user">
            <%-- 用 Struts2 标签 + OGNL 读取 session 中的登录用户 --%>
            <s:if test="#session.loginUser == null">
                <a href="${ctx}/user_toLogin.action">登录</a>
                <a href="${ctx}/user_toRegister.action">注册</a>
            </s:if>
            <s:else>
                你好，<s:property value="#session.loginUser.realName"/>
                <a href="${ctx}/user_profile.action">个人中心</a>
                <%-- isAdmin() 方法在 OGNL 中按属性 admin 访问 --%>
                <s:if test="#session.loginUser.admin">
                    <a href="${ctx}/admin/product_list.action" style="color:#8c2f26;">后台管理</a>
                </s:if>
                <a href="${ctx}/user_logout.action">退出</a>
            </s:else>
        </div>
    </div>
</nav>
