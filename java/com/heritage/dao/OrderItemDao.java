package com.heritage.dao;

import com.heritage.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单明细 DAO
 */
public interface OrderItemDao {

    /** 新增一条订单明细（下单事务中，一个商品一条） */
    int insert(OrderItem item);

    /** 查询某订单的全部明细 */
    List<OrderItem> selectByOrder(@Param("orderId") Integer orderId);

    /** 删除某订单的全部明细（整单取消时用） */
    int deleteByOrder(@Param("orderId") Integer orderId);
}
