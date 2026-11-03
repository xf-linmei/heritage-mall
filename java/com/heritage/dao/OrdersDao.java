package com.heritage.dao;

import com.heritage.entity.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单主表 DAO
 *
 * <p>状态流转：0待付款 →（支付）→ 1待发货 →（发货）→ 2待收货 →（确认收货）→ 3已完成
 * 待付款状态下可取消 → 4已取消</p>
 */
public interface OrdersDao {

    /** 新增订单主表（自增主键回填到 orders.orderId，供明细表使用） */
    int insert(Orders orders);

    /** 按主键查询订单（关联查询下单账号） */
    Orders selectById(@Param("orderId") Integer orderId);

    /** 按订单编号查询 */
    Orders selectByOrderNo(@Param("orderNo") String orderNo);

    /** 我的订单：查询某用户的全部订单（按下单时间倒序） */
    List<Orders> selectByUser(@Param("userId") Integer userId);

    /** 后台：按状态 + 订单号/收货人关键字分页查询 */
    List<Orders> selectByCondition(@Param("status") Integer status,
                                   @Param("keyword") String keyword,
                                   @Param("startIndex") Integer startIndex,
                                   @Param("pageSize") Integer pageSize);

    /** 后台分页查询的总记录数 */
    int countByCondition(@Param("status") Integer status, @Param("keyword") String keyword);

    /** 模拟支付：待付款 → 待发货，同时写入支付时间 */
    int payOrder(@Param("orderId") Integer orderId);

    /** 后台发货：待发货 → 待收货，同时写入发货时间 */
    int deliverOrder(@Param("orderId") Integer orderId);

    /** 确认收货：待收货 → 已完成，同时写入完成时间 */
    int finishOrder(@Param("orderId") Integer orderId);

    /** 取消订单：仅待付款状态可取消 */
    int cancelOrder(@Param("orderId") Integer orderId);

    /** 通用状态修改（备用） */
    int updateStatus(@Param("orderId") Integer orderId, @Param("status") Integer status);
}
