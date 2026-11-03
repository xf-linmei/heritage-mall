package com.heritage.action.admin;

import com.heritage.common.BaseAction;
import com.heritage.common.Constant;
import com.heritage.common.Page;
import com.heritage.entity.Orders;
import com.heritage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 后台订单管理 Action：全部订单（按状态/关键字筛选 + 分页）、订单明细、发货
 */
@Controller("com.heritage.action.admin.OrderAction")
@Scope("prototype")
public class OrderAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private OrderService orderService;

    // ==================== 接收参数 ====================
    private Integer orderId;
    private Integer status;     // 订单状态筛选：0待付款 1待发货 2待收货 3已完成 4已取消
    private String keyword;     // 订单号 / 收货人
    private Integer pageNo;

    // ==================== 页面数据 ====================
    private Page<Orders> page;
    private Orders order;

    /** 订单列表 */
    public String list() {
        int pageNum = (pageNo == null || pageNo < 1) ? 1 : pageNo;
        page = orderService.listAdminOrders(status, keyword, pageNum, Constant.ADMIN_PAGE_SIZE);
        return SUCCESS;
    }

    /** 订单详情（含明细） */
    public String detail() {
        order = orderService.getOrderDetail(orderId);
        if (order == null) {
            addActionError("订单不存在");
            return ERROR;
        }
        return SUCCESS;
    }

    /** 发货：待发货 → 待收货 */
    public String deliver() {
        int rows = orderService.deliverOrder(orderId);
        setFlashMsg(rows > 0 ? "发货成功" : "发货失败：只有待发货的订单可以发货");
        return SUCCESS;
    }

    // ==================== getter / setter ====================
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Page<Orders> getPage() {
        return page;
    }

    public Orders getOrder() {
        return order;
    }
}
