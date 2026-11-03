<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>订单明细 · 匠集后台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/admin_head.jsp" %>

<div class="jj-admin-title">订单明细</div>

<div class="jj-admin-box">
    <div class="row mb-3">
        <div class="col-md-6">
            <p>订单编号：<b>${order.orderNo}</b></p>
            <p>下单用户：${order.userName}（ID：${order.userId}）</p>
            <p>订单状态：<span style="color:#8c2f26;font-size:16px;">${order.statusName}</span></p>
            <p>下单时间：<fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            <p>支付时间：<fmt:formatDate value="${order.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            <p>发货时间：<fmt:formatDate value="${order.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            <p>完成时间：<fmt:formatDate value="${order.finishTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
        </div>
        <div class="col-md-6">
            <p>收货人：${order.receiver}</p>
            <p>联系电话：${order.phone}</p>
            <p>收货地址：${order.addressDetail}</p>
            <p>买家留言：${empty order.remark ? '无' : order.remark}</p>
        </div>
    </div>

    <table class="table table-bordered jj-table">
        <thead>
        <tr>
            <th>文创商品</th>
            <th style="width:110px;">单价</th>
            <th style="width:80px;">数量</th>
            <th style="width:110px;">小计</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${order.itemList}" var="item">
            <tr>
                <td>
                    <div class="d-flex align-items-center">
                        <img src="${ctx}${item.productImage}" class="jj-thumb-admin" style="width:50px;height:50px;">
                        <span class="ms-2">${item.productName}</span>
                    </div>
                </td>
                <td>￥${item.price}</td>
                <td>${item.quantity}</td>
                <td class="jj-price">￥${item.subtotal}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="text-end mb-3">
        订单总金额：<span class="jj-price" style="font-size:24px;">￥${order.totalAmount}</span>
    </div>
    <div>
        <c:if test="${order.status == 1}">
            <a class="jj-btn" href="${ctx}/admin/order_deliver.action?orderId=${order.orderId}"
               onclick="return confirm('确认已发货？');">确认发货</a>
        </c:if>
        <a class="jj-btn-outline ms-2" href="${ctx}/admin/order_list.action">返回订单列表</a>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/common/admin_foot.jsp" %>
</body>
</html>
