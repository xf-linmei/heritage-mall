<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>轮播管理 · 匠集后台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/admin_head.jsp" %>

<div class="d-flex justify-content-between align-items-center">
    <div class="jj-admin-title">轮播 Banner 管理</div>
    <a class="jj-btn" href="${ctx}/admin/banner_toAdd.action">+ 新增轮播</a>
</div>

<div class="jj-admin-box">
    <table class="table table-bordered jj-table">
        <thead>
        <tr>
            <th style="width:60px;">ID</th>
            <th style="width:160px;">轮播图片</th>
            <th>标题</th>
            <th>跳转链接</th>
            <th style="width:70px;">排序</th>
            <th style="width:80px;">状态</th>
            <th style="width:160px;">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${bannerList}" var="b">
            <tr>
                <td>${b.bannerId}</td>
                <td><img src="${ctx}${b.imageUrl}" class="jj-thumb-admin"></td>
                <td>${b.title}</td>
                <td class="jj-muted">${b.linkUrl}</td>
                <td>${b.sortOrder}</td>
                <td>
                    <c:choose>
                        <c:when test="${b.status == 1}"><span style="color:#2e7d32;">启用</span></c:when>
                        <c:otherwise><span class="jj-muted">禁用</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="${ctx}/admin/banner_toEdit.action?bannerId=${b.bannerId}">编辑</a>
                    &nbsp;
                    <a href="${ctx}/admin/banner_status.action?bannerId=${b.bannerId}&status=${b.status == 1 ? 0 : 1}">
                        ${b.status == 1 ? '禁用' : '启用'}
                    </a>
                    &nbsp;
                    <a href="${ctx}/admin/banner_delete.action?bannerId=${b.bannerId}"
                       onclick="return confirm('确定删除该轮播吗？');" style="color:#a94442;">删除</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty bannerList}">
            <tr>
                <td colspan="7" class="text-center jj-muted py-4">暂无轮播，点击右上角新增</td>
            </tr>
        </c:if>
        </tbody>
    </table>
    <div class="jj-muted">提示：图片限制 jpg/png，大小不超过 5MB；排序值越小越靠前。</div>
</div>

<%@ include file="/WEB-INF/jsp/common/admin_foot.jsp" %>
</body>
</html>
