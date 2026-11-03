<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>商品管理 · 匠集后台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/admin_head.jsp" %>

<div class="d-flex justify-content-between align-items-center">
    <div class="jj-admin-title">文创商品管理</div>
    <a class="jj-btn" href="${ctx}/admin/product_toAdd.action">+ 新增商品</a>
</div>

<div class="jj-admin-box">
    <!-- 筛选：分类 + 名称关键字 -->
    <form class="row g-2 mb-3" action="${ctx}/admin/product_list.action" method="get">
        <div class="col-md-3">
            <select name="categoryId" class="form-select">
                <option value="">全部分类</option>
                <c:forEach items="${categoryList}" var="c">
                    <option value="${c.categoryId}" ${param.categoryId == c.categoryId ? 'selected' : ''}>
                            ${c.categoryName}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-3">
            <input type="text" name="keyword" class="form-control" placeholder="商品名称关键字" value="${param.keyword}">
        </div>
        <div class="col-md-2">
            <button class="jj-btn w-100">搜索</button>
        </div>
    </form>

    <table class="table table-bordered jj-table">
        <thead>
        <tr>
            <th style="width:60px;">ID</th>
            <th style="width:120px;">主图</th>
            <th>商品名称</th>
            <th style="width:90px;">分类</th>
            <th style="width:90px;">价格</th>
            <th style="width:70px;">库存</th>
            <th style="width:70px;">销量</th>
            <th style="width:80px;">状态</th>
            <th style="width:150px;">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="p">
            <tr>
                <td>${p.productId}</td>
                <td><img src="${ctx}${p.mainImage}" class="jj-thumb-admin"></td>
                <td>
                    ${p.productName}
                    <c:if test="${p.isHot == 1}"><span class="badge" style="background:#8c2f26;">热</span></c:if>
                    <c:if test="${p.isNew == 1}"><span class="badge" style="background:#c8a45c;">新</span></c:if>
                </td>
                <td>${p.categoryName}</td>
                <td class="jj-price">￥${p.price}</td>
                <td>${p.stock}</td>
                <td>${p.sales}</td>
                <td>
                    <c:choose>
                        <c:when test="${p.status == 1}"><span style="color:#2e7d32;">在售</span></c:when>
                        <c:otherwise><span class="jj-muted">已下架</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="${ctx}/admin/product_toEdit.action?productId=${p.productId}">编辑</a>
                    &nbsp;
                    <a href="${ctx}/admin/product_status.action?productId=${p.productId}&status=${p.status == 1 ? 0 : 1}">
                        ${p.status == 1 ? '下架' : '上架'}
                    </a>
                    &nbsp;
                    <a href="${ctx}/admin/product_delete.action?productId=${p.productId}"
                       onclick="return confirm('确定删除该商品吗？');" style="color:#a94442;">删除</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty page.list}">
            <tr>
                <td colspan="9" class="text-center jj-muted py-4">没有找到商品</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <!-- 分页 -->
    <c:if test="${page.totalPage > 1}">
        <div class="jj-page mt-3">
            <c:forEach begin="1" end="${page.totalPage}" var="i">
                <c:url var="u" value="/admin/product_list.action">
                    <c:param name="pageNo" value="${i}"/>
                    <c:param name="categoryId" value="${param.categoryId}"/>
                    <c:param name="keyword" value="${param.keyword}"/>
                </c:url>
                <c:choose>
                    <c:when test="${i == page.pageNo}"><span class="current">${i}</span></c:when>
                    <c:otherwise><a href="${u}">${i}</a></c:otherwise>
                </c:choose>
            </c:forEach>
            <span class="jj-muted ms-2">共 ${page.totalCount} 件</span>
        </div>
    </c:if>
</div>

<%@ include file="/WEB-INF/jsp/common/admin_foot.jsp" %>
</body>
</html>
