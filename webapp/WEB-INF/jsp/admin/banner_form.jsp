<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty banner ? '新增' : '编辑'}轮播 · 匠集后台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/admin_head.jsp" %>

<div class="jj-admin-title">${empty banner ? '新增' : '编辑'}轮播</div>

<div class="jj-admin-box" style="max-width:720px;">
    <%-- 表单必须 enctype="multipart/form-data"，否则 Struts2 无法接收上传文件 --%>
    <form action="${ctx}/admin/${empty banner ? 'banner_add' : 'banner_edit'}.action"
          method="post" enctype="multipart/form-data">
        <c:if test="${not empty banner}">
            <input type="hidden" name="banner.bannerId" value="${banner.bannerId}">
        </c:if>

        <div class="mb-3 row">
            <label class="col-sm-3 col-form-label jj-form-label">轮播标题</label>
            <div class="col-sm-9">
                <input type="text" name="banner.title" class="form-control" value="${banner.title}"
                       placeholder="如：匠心传承 · 非遗文创年礼季" required>
            </div>
        </div>

        <div class="mb-3 row">
            <label class="col-sm-3 col-form-label jj-form-label">跳转链接</label>
            <div class="col-sm-9">
                <input type="text" name="banner.linkUrl" class="form-control" value="${banner.linkUrl}"
                       placeholder="如：/product_list.action">
                <div class="jj-muted mt-1">填写站内相对路径，留空表示不跳转。</div>
            </div>
        </div>

        <div class="mb-3 row">
            <label class="col-sm-3 col-form-label jj-form-label">排序值</label>
            <div class="col-sm-9">
                <input type="number" name="banner.sortOrder" class="form-control" style="width:140px;"
                       value="${empty banner ? 0 : banner.sortOrder}">
            </div>
        </div>

        <c:if test="${empty banner}">
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label jj-form-label">状态</label>
                <div class="col-sm-9">
                    <select name="banner.status" class="form-select" style="width:140px;">
                        <option value="1">启用</option>
                        <option value="0">禁用</option>
                    </select>
                </div>
            </div>
        </c:if>

        <div class="mb-3 row">
            <label class="col-sm-3 col-form-label jj-form-label">轮播图片</label>
            <div class="col-sm-9">
                <%-- name="image" 必须与 Action 中的 File image 属性名一致 --%>
                <input type="file" name="image" class="form-control" accept=".jpg,.jpeg,.png">
                <div class="jj-muted mt-1">仅支持 jpg / png，大小不超过 5MB。</div>
                <c:if test="${not empty banner.imageUrl}">
                    <div class="mt-2">
                        <div class="jj-muted">当前图片：</div>
                        <img src="${ctx}${banner.imageUrl}" class="jj-thumb-admin" style="width:240px;height:90px;">
                        <div class="jj-muted">不选择新文件时保持原图不变。</div>
                    </div>
                </c:if>
            </div>
        </div>

        <button type="submit" class="jj-btn">保存</button>
        <a class="jj-btn-outline ms-2" href="${ctx}/admin/banner_list.action">返回列表</a>
    </form>
</div>

<%@ include file="/WEB-INF/jsp/common/admin_foot.jsp" %>
</body>
</html>
