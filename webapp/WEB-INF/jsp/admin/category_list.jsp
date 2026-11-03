<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>分类管理 · 匠集后台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/admin_head.jsp" %>

<div class="jj-admin-title">非遗分类管理</div>

<div class="jj-admin-box">
    <table class="table table-bordered jj-table">
        <thead>
        <tr>
            <th style="width:60px;">ID</th>
            <th style="width:180px;">分类名称</th>
            <th>分类简介</th>
            <th style="width:80px;">排序</th>
            <th style="width:260px;">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${categoryList}" var="c">
            <tr>
                <td>${c.categoryId}</td>
                <td>${c.categoryName}</td>
                <td class="jj-muted">${c.categoryDesc}</td>
                <td>${c.sortOrder}</td>
                <td>
                    <!-- 行内编辑：直接提交表单修改 -->
                    <form action="${ctx}/admin/category_edit.action" method="post" class="d-flex">
                        <input type="hidden" name="category.categoryId" value="${c.categoryId}">
                        <input type="text" name="category.categoryName" value="${c.categoryName}"
                               class="form-control form-control-sm me-1" style="width:110px;">
                        <input type="number" name="category.sortOrder" value="${c.sortOrder}"
                               class="form-control form-control-sm me-1" style="width:60px;">
                        <button class="jj-btn-outline jj-btn-sm">保存</button>
                    </form>
                    <div class="mt-1">
                        <a href="${ctx}/admin/category_delete.action?categoryId=${c.categoryId}"
                           onclick="return confirm('确定删除该分类吗？');" style="color:#a94442;">删除分类</a>
                        <span class="jj-muted ms-2">（分类下存在商品时禁止删除）</span>
                    </div>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty categoryList}">
            <tr>
                <td colspan="5" class="text-center jj-muted py-4">暂无分类，请在下方添加</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<div class="jj-admin-box mt-3">
    <div class="jj-admin-title" style="font-size:16px;">新增非遗分类</div>
    <form action="${ctx}/admin/category_add.action" method="post" class="row g-2">
        <div class="col-md-3">
            <input type="text" name="category.categoryName" class="form-control" placeholder="分类名称，如：剪纸" required>
        </div>
        <div class="col-md-4">
            <input type="text" name="category.categoryDesc" class="form-control" placeholder="分类简介（非遗背景）">
        </div>
        <div class="col-md-2">
            <input type="number" name="category.sortOrder" class="form-control" placeholder="排序值" value="0">
        </div>
        <div class="col-md-2">
            <button class="jj-btn w-100">添加分类</button>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/jsp/common/admin_foot.jsp" %>
</body>
</html>
