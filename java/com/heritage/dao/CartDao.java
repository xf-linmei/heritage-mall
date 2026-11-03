package com.heritage.dao;

import com.heritage.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车 DAO
 */
public interface CartDao {

    /** 查询某用户的购物车（关联商品信息，按加入时间倒序） */
    List<Cart> selectByUser(@Param("userId") Integer userId);

    /** 查询某用户在购物车中的某个商品（用于判断重复加入） */
    Cart selectByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /** 新增购物项 */
    int insert(Cart cart);

    /** 修改数量 */
    int updateQuantity(@Param("cartId") Integer cartId, @Param("quantity") Integer quantity);

    /** 勾选 / 取消勾选 */
    int updateChecked(@Param("cartId") Integer cartId, @Param("checked") Integer checked);

    /** 删除购物项 */
    int delete(@Param("cartId") Integer cartId);

    /** 清空某用户购物车 */
    int deleteByUser(@Param("userId") Integer userId);

    /** 下单成功后，从购物车中移除已购买的该商品 */
    int deleteByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);
}
