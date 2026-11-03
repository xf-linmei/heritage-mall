package com.heritage.service.impl;

import com.heritage.common.Constant;
import com.heritage.dao.CartDao;
import com.heritage.dao.ProductDao;
import com.heritage.entity.Cart;
import com.heritage.entity.Product;
import com.heritage.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车业务实现类
 *
 * <p>加入购物车时要做三重校验：商品是否存在、是否已上架、库存是否足够。
 * 同一商品重复加入时不新增记录，而是在原记录上累加数量（依赖 cart 表 (user_id, product_id) 唯一键）。</p>
 */
@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List<Cart> listCart(Integer userId) {
        return cartDao.selectByUser(userId);
    }

    @Override
    public List<Cart> listCheckedCart(Integer userId) {
        List<Cart> all = listCart(userId);
        List<Cart> checkedList = new ArrayList<Cart>();
        if (all == null) {
            return checkedList;
        }
        for (Cart cart : all) {
            // 只保留勾选的、且商品仍然存在且未下架的记录
            if (cart.getChecked() != null && cart.getChecked() == 1
                    && cart.getProduct() != null
                    && cart.getProduct().getStatus() != null
                    && cart.getProduct().getStatus() == Constant.STATUS_ON) {
                checkedList.add(cart);
            }
        }
        return checkedList;
    }

    @Override
    public int addToCart(Integer userId, Integer productId, Integer quantity) {
        Product product = productDao.selectById(productId);
        if (product == null || product.getStatus() == null || product.getStatus() != Constant.STATUS_ON) {
            return 0;                                   // 商品不存在或已下架
        }
        if (quantity == null || quantity < 1) {
            quantity = 1;
        }
        // 购物车中是否已有该商品
        Cart exist = cartDao.selectByUserAndProduct(userId, productId);
        if (exist != null) {
            int newQuantity = exist.getQuantity() + quantity;
            // 数量不能超过库存
            if (product.getStock() < newQuantity) {
                newQuantity = product.getStock();
            }
            if (newQuantity < 1) {
                return 0;
            }
            return cartDao.updateQuantity(exist.getCartId(), newQuantity);
        }
        // 首次加入：数量也不能超过库存
        if (product.getStock() < quantity) {
            quantity = product.getStock();
        }
        if (quantity < 1) {
            return 0;                                   // 已售罄
        }
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setChecked(1);
        return cartDao.insert(cart);
    }

    @Override
    public int updateQuantity(Integer userId, Integer cartId, Integer quantity) {
        if (cartId == null || quantity == null || quantity < 1) {
            return 0;
        }
        // 越权保护：确认这条购物车记录属于当前用户
        Cart cart = findOwnCart(userId, cartId);
        if (cart == null) {
            return 0;
        }
        // 不能超过库存
        if (cart.getProduct() != null && cart.getProduct().getStock() < quantity) {
            quantity = cart.getProduct().getStock();
        }
        return cartDao.updateQuantity(cartId, quantity);
    }

    @Override
    public int updateChecked(Integer userId, Integer cartId, Integer checked) {
        if (cartId == null) {
            return 0;
        }
        Cart cart = findOwnCart(userId, cartId);
        if (cart == null) {
            return 0;
        }
        return cartDao.updateChecked(cartId, (checked != null && checked == 1) ? 1 : 0);
    }

    @Override
    public int delete(Integer userId, Integer cartId) {
        if (cartId == null) {
            return 0;
        }
        Cart cart = findOwnCart(userId, cartId);
        if (cart == null) {
            return 0;
        }
        return cartDao.delete(cartId);
    }

    @Override
    public BigDecimal getCheckedTotal(Integer userId) {
        BigDecimal total = new BigDecimal("0");
        List<Cart> checkedList = listCheckedCart(userId);
        for (Cart cart : checkedList) {
            total = total.add(cart.getSubtotal());     // subtotal = 单价 × 数量
        }
        return total;
    }

    @Override
    public int countCart(Integer userId) {
        List<Cart> list = listCart(userId);
        return list == null ? 0 : list.size();
    }

    /** 从当前用户的购物车中查找指定记录，保证不能操作别人的购物车 */
    private Cart findOwnCart(Integer userId, Integer cartId) {
        List<Cart> list = listCart(userId);
        if (list == null) {
            return null;
        }
        for (Cart cart : list) {
            if (cart.getCartId() != null && cart.getCartId().equals(cartId)) {
                return cart;
            }
        }
        return null;
    }
}
