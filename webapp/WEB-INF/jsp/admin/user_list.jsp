<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户管理 · 匠集后台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/admin_head.jsp" %>

<div class="jj-admin-title">注册用户管理</div>

<div class="jj-admin-box">
    <table class="table table-bordered jj-table">
        <thead>
        <tr>
            <th style="width:60px;">ID</th>
            <th style="width:130px;">账号</th>
            <th style="width:110px;">姓名</th>
            <th style="width:130px;">手机号</th>
            <th>邮箱</th>
            <th style="width:90px;">角色</th>
            <th style="width:90px;">状态</th>
            <th style="width:150px;">注册时间</th>
            <th style="width:100px;">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${userList}" var="u">
            <tr>
                <td>${u.userId}</td>
                <td>${u.username}</td>
                <td>${u.realName}</td>
                <td>${u.phone}</td>
                <td class="jj-muted">${u.email}</td>
                <td>
                    <c:choose>
                        <c:when test="${u.role == 'ADMIN'}"><span style="color:#8c2f26;">管理员</span></c:when>
                        <c:otherwise>普通用户</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${u.status == 1}"><span style="color:#2e7d32;">正常</span></c:when>
                        <c:otherwise><span class="jj-muted">已禁用</span></c:otherwise>
                    </c:choose>
                </td>
                <td><fmt:formatDate value="${u.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>
                    <%-- 不允许禁用当前登录的管理员自己（Action 里也做了校验） --%>
                    <%-- 角色为 ADMIN 的用户不允许禁用，避免把管理员锁在门外 --%>
                    <c:if test="${u.role != 'ADMIN'}">
                        <a href="${ctx}/admin/user_status.action?userId=${u.userId}&status=${u.status == 1 ? 0 : 1}"
                           onclick="return confirm('确定执行该操作吗？');">
                                ${u.status == 1 ? '禁用账号' : '启用账号'}
                        </a>
                    </c:if>
                    <c:if test="${u.role == 'ADMIN'}"><span class="jj-muted">不可操作</span></c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty userList}">
            <tr>
                <td colspan="9" class="text-center jj-muted py-4">暂无用户</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jsp/common/admin_foot.jsp" %>
</body>
</html>
