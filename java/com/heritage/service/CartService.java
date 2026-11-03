package com.heritage.service;

import com.heritage.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车业务接口
 */
public interface CartService {

    /** 查询某用户的全部购物车项（已关联商品信息） */
    List<Cart> listCart(Integer userId);

    /** 只查询勾选了的商品（结算页与下单时用） */
    List<Cart> listCheckedCart(Integer userId);

    /**
     * 加入购物车
     *
     * @return 影响行数；0 表示加入失败（商品不存在/已下架/库存不足）
     */
    int addToCart(Integer userId, Integer productId, Integer quantity);

    /** 修改数量 */
    int updateQuantity(Integer userId, Integer cartId, Integer quantity);

    /** 勾选 / 取消勾选 */
    int updateChecked(Integer userId, Integer cartId, Integer checked);

    /** 删除购物项 */
    int delete(Integer userId, Integer cartId);

    /** 已勾选商品的总金额（购物车自动计算总额用） */
    BigDecimal getCheckedTotal(Integer userId);

    /** 购物车商品件数（页头角标用） */
    int countCart(Integer userId);
}
