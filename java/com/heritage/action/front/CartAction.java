package com.heritage.action.front;

import com.heritage.common.BaseAction;
import com.heritage.entity.Cart;
import com.heritage.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车 Action：加入、修改数量、删除、勾选、列表与自动算总额
 */
@Controller("com.heritage.action.front.CartAction")
@Scope("prototype")
public class CartAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CartService cartService;

    // ==================== 接收参数 ====================
    private Integer productId;
    private Integer quantity;
    private Integer cartId;
    private Integer checked;

    // ==================== 页面数据 ====================
    private List<Cart> cartList;
    private BigDecimal total;          // 已勾选商品总金额

    /** 购物车列表 */
    public String list() {
        cartList = cartService.listCart(getLoginUserId());
        total = cartService.getCheckedTotal(getLoginUserId());
        return SUCCESS;
    }

    /** 加入购物车 */
    public String add() {
        int qty = (quantity == null || quantity < 1) ? 1 : quantity;
        int rows = cartService.addToCart(getLoginUserId(), productId, qty);
        // 重定向会丢掉 addActionError，所以用一次性提示
        setFlashMsg(rows > 0 ? "已加入购物车" : "加入失败：商品不存在、已下架或库存不足");
        return SUCCESS;
    }

    /** 修改数量 */
    public String updateQuantity() {
        int rows = cartService.updateQuantity(getLoginUserId(), cartId, quantity);
        setFlashMsg(rows > 0 ? "数量已更新" : "修改失败：数量超出库存");
        return SUCCESS;
    }

    /** 删除购物项 */
    public String delete() {
        cartService.delete(getLoginUserId(), cartId);
        setFlashMsg("已从购物车移除");
        return SUCCESS;
    }

    /** 勾选 / 取消勾选（总金额随之变化） */
    public String check() {
        cartService.updateChecked(getLoginUserId(), cartId, checked);
        return SUCCESS;
    }

    // ==================== getter / setter ====================
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
