<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>订单管理 · 匠集后台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/admin_head.jsp" %>

<div class="jj-admin-title">订单管理</div>

<div class="jj-admin-box">
    <!-- 状态筛选 + 关键字搜索 -->
    <div class="mb-3">
        <a class="jj-btn-outline jj-btn-sm ${empty param.status ? '' : 'jj-btn'}"
           href="${ctx}/admin/order_list.action">全部</a>
        <a class="jj-btn-outline jj-btn-sm ${param.status == 0 ? 'jj-btn' : ''}"
           href="${ctx}/admin/order_list.action?status=0">待付款</a>
        <a class="jj-btn-outline jj-btn-sm ${param.status == 1 ? 'jj-btn' : ''}"
           href="${ctx}/admin/order_list.action?status=1">待发货</a>
        <a class="jj-btn-outline jj-btn-sm ${param.status == 2 ? 'jj-btn' : ''}"
           href="${ctx}/admin/order_list.action?status=2">待收货</a>
        <a class="jj-btn-outline jj-btn-sm ${param.status == 3 ? 'jj-btn' : ''}"
           href="${ctx}/admin/order_list.action?status=3">已完成</a>
        <a class="jj-btn-outline jj-btn-sm ${param.status == 4 ? 'jj-btn' : ''}"
           href="${ctx}/admin/order_list.action?status=4">已取消</a>

        <form class="d-inline-flex ms-3" action="${ctx}/admin/order_list.action" method="get">
            <input type="hidden" name="status" value="${param.status}">
            <input type="text" name="keyword" class="form-control form-control-sm" style="width:180px;"
                   placeholder="订单号 / 收货人" value="${param.keyword}">
            <button class="jj-btn jj-btn-sm ms-1">搜索</button>
        </form>
    </div>

    <table class="table table-bordered jj-table">
        <thead>
        <tr>
            <th style="width:60px;">ID</th>
            <th style="width:190px;">订单号</th>
            <th style="width:110px;">下单用户</th>
            <th style="width:100px;">金额</th>
            <th style="width:90px;">状态</th>
            <th>收货信息</th>
            <th style="width:150px;">下单时间</th>
            <th style="width:130px;">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="o">
            <tr>
                <td>${o.orderId}</td>
                <td>${o.orderNo}</td>
                <td>${o.userName}</td>
                <td class="jj-price">￥${o.totalAmount}</td>
                <td style="color:#8c2f26;">${o.statusName}</td>
                <td class="jj-muted">${o.receiver} ${o.phone} / ${o.addressDetail}</td>
                <td><fmt:formatDate value="${o.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>
                    <a href="${ctx}/admin/order_detail.action?orderId=${o.orderId}">订单明细</a>
                    <c:if test="${o.status == 1}">
                        <br>
                        <a href="${ctx}/admin/order_deliver.action?orderId=${o.orderId}" class="jj-btn jj-btn-sm mt-1"
                           onclick="return confirm('确认已发货？');">发货</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty page.list}">
            <tr>
                <td colspan="8" class="text-center jj-muted py-4">没有符合条件的订单</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <!-- 分页 -->
    <c:if test="${page.totalPage > 1}">
        <div class="jj-page mt-3">
            <c:forEach begin="1" end="${page.totalPage}" var="i">
                <c:url var="u" value="/admin/order_list.action">
                    <c:param name="pageNo" value="${i}"/>
                    <c:param name="status" value="${param.status}"/>
                    <c:param name="keyword" value="${param.keyword}"/>
                </c:url>
                <c:choose>
                    <c:when test="${i == page.pageNo}"><span class="current">${i}</span></c:when>
                    <c:otherwise><a href="${u}">${i}</a></c:otherwise>
                </c:choose>
            </c:forEach>
            <span class="jj-muted ms-2">共 ${page.totalCount} 条</span>
        </div>
    </c:if>
</div>

<%@ include file="/WEB-INF/jsp/common/admin_foot.jsp" %>
</body>
</html>
