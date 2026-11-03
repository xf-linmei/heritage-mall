<%--
  后台公共布局：顶部栏 + 左侧菜单，并打开主内容区
  每个后台页面：开头 include 本文件，结尾 include admin_foot.jsp
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="uri" value="${pageContext.request.requestURI}"/>

<div class="jj-admin-top">
    <div class="logo">匠集 · 后台管理系统</div>
    <div>
        管理员：<s:property value="#session.loginUser.username"/>
        <a href="${ctx}/index.action">返回前台</a>
        <a href="${ctx}/user_logout.action">退出登录</a>
    </div>
</div>

<div class="jj-admin-body">
    <div class="jj-admin-side">
        <a href="${ctx}/admin/product_list.action">文创商品管理</a>
        <a href="${ctx}/admin/category_list.action">非遗分类管理</a>
        <a href="${ctx}/admin/banner_list.action">轮播 Banner 管理</a>
        <a href="${ctx}/admin/order_list.action">订单管理</a>
        <a href="${ctx}/admin/user_list.action">用户管理</a>
    </div>
    <div class="jj-admin-main">
        <%-- 公共提示（含删除分类时的业务校验错误）--%>
        <%@ include file="/WEB-INF/jsp/common/msg.jsp" %>
