<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty product ? '新增' : '编辑'}商品 · 匠集后台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/admin_head.jsp" %>

<div class="jj-admin-title">${empty product ? '新增' : '编辑'}文创商品</div>

<div class="jj-admin-box">
    <%-- 上传必须 enctype="multipart/form-data" --%>
    <form action="${ctx}/admin/${empty product ? 'product_add' : 'product_edit'}.action"
          method="post" enctype="multipart/form-data">
        <c:if test="${not empty product}">
            <input type="hidden" name="product.productId" value="${product.productId}">
        </c:if>

        <div class="row">
            <div class="col-md-7">
                <div class="mb-3 row">
                    <label class="col-sm-3 col-form-label jj-form-label">商品名称</label>
                    <div class="col-sm-9">
                        <input type="text" name="product.productName" class="form-control"
                               value="${product.productName}" placeholder="如：非遗剪纸·福字窗花套装" required>
                    </div>
                </div>

                <div class="mb-3 row">
                    <label class="col-sm-3 col-form-label jj-form-label">非遗分类</label>
                    <div class="col-sm-9">
                        <select name="product.categoryId" class="form-select">
                            <c:forEach items="${categoryList}" var="c">
                                <option value="${c.categoryId}" ${product.categoryId == c.categoryId ? 'selected' : ''}>
                                        ${c.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="mb-3 row">
                    <label class="col-sm-3 col-form-label jj-form-label">价格（元）</label>
                    <div class="col-sm-4">
                        <input type="text" name="product.price" class="form-control" value="${product.price}" required>
                    </div>
                    <label class="col-sm-2 col-form-label jj-form-label">库存</label>
                    <div class="col-sm-3">
                        <input type="number" name="product.stock" class="form-control" value="${empty product ? 0 : product.stock}">
                    </div>
                </div>

                <div class="mb-3 row">
                    <label class="col-sm-3 col-form-label jj-form-label">上架状态</label>
                    <div class="col-sm-4">
                        <select name="product.status" class="form-select">
                            <option value="1" ${product.status == 1 ? 'selected' : ''}>上架销售</option>
                            <option value="0" ${product.status == 0 ? 'selected' : ''}>下架</option>
                        </select>
                    </div>
                    <label class="col-sm-2 col-form-label jj-form-label">标记</label>
                    <div class="col-sm-3">
                        <select name="product.isHot" class="form-select mb-1">
                            <option value="0" ${product.isHot == 0 ? 'selected' : ''}>不热门</option>
                            <option value="1" ${product.isHot == 1 ? 'selected' : ''}>热门推荐</option>
                        </select>
                        <select name="product.isNew" class="form-select">
                            <option value="0" ${product.isNew == 0 ? 'selected' : ''}>非新品</option>
                            <option value="1" ${product.isNew == 1 ? 'selected' : ''}>新品推荐</option>
                        </select>
                    </div>
                </div>

                <div class="mb-3 row">
                    <label class="col-sm-3 col-form-label jj-form-label">非遗文化介绍</label>
                    <div class="col-sm-9">
                        <textarea name="product.productDesc" class="form-control" rows="6"
                                  placeholder="介绍该文创的工艺、传承与文化寓意">${product.productDesc}</textarea>
                    </div>
                </div>
            </div>

            <div class="col-md-5">
                <div class="mb-3">
                    <label class="jj-form-label">商品主图</label>
                    <%-- name="image" 对应 Action 的 File image --%>
                    <input type="file" name="image" class="form-control" accept=".jpg,.jpeg,.png">
                    <c:if test="${not empty product.mainImage}">
                        <img src="${ctx}${product.mainImage}" class="jj-thumb-admin mt-2" style="width:160px;height:100px;">
                    </c:if>
                </div>
                <div class="mb-3">
                    <label class="jj-form-label">商品详情图（可多选）</label>
                    <%-- name="detailImage" 对应 Action 的 List<File> detailImage --%>
                    <input type="file" name="detailImage" class="form-control" accept=".jpg,.jpeg,.png" multiple>
                    <div class="jj-muted mt-1">支持一次选择多张 jpg / png，单张不超过 5MB。</div>
                    <c:if test="${not empty product.detailImageList}">
                        <div class="jj-muted mt-2">当前详情图：</div>
                        <c:forEach items="${product.detailImageList}" var="img">
                            <img src="${ctx}${img}" class="jj-thumb-admin" style="width:70px;height:70px;">
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>

        <button type="submit" class="jj-btn">保存商品</button>
        <a class="jj-btn-outline ms-2" href="${ctx}/admin/product_list.action">返回列表</a>
    </form>
</div>

<%@ include file="/WEB-INF/jsp/common/admin_foot.jsp" %>
</body>
</html>
