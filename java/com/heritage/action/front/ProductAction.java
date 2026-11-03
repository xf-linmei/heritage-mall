package com.heritage.action.front;

import com.heritage.common.BaseAction;
import com.heritage.common.Constant;
import com.heritage.common.Page;
import com.heritage.entity.Category;
import com.heritage.entity.Product;
import com.heritage.service.CategoryService;
import com.heritage.service.CollectService;
import com.heritage.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

/**
 * 前台商品 Action
 *
 * <p>接收条件参数（分类、关键字、价格区间、排序、页码）并分页查询；
 * 详情页负责展示主图、详情图、非遗介绍，并判断是否已收藏。</p>
 *
 * <p>参数由 Struts2 自动注入：表单/URL 中的 categoryId、keyword 会自动调用对应 setter。</p>
 */
@Controller("com.heritage.action.front.ProductAction")
@Scope("prototype")
public class ProductAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CollectService collectService;

    // ==================== 接收参数（自动注入）====================
    private Integer categoryId;      // 分类筛选
    private String keyword;          // 名称模糊搜索
    private BigDecimal minPrice;     // 最低价
    private BigDecimal maxPrice;     // 最高价
    private String sort;             // 排序方式
    private Integer pageNo;          // 页码
    private Integer productId;       // 商品ID

    // ==================== 页面数据 ====================
    private Page<Product> page;      // 分页商品
    private List<Category> categoryList;
    private Product product;         // 商品详情
    private boolean collected;       // 当前用户是否已收藏

    /**
     * 商品列表：多条件 + 分页
     */
    public String list() {
        int pageNum = (pageNo == null || pageNo < 1) ? 1 : pageNo;
        // 前台只展示上架商品
        page = productService.listByCondition(categoryId, keyword, minPrice, maxPrice,
                Constant.STATUS_ON, sort, pageNum, Constant.FRONT_PAGE_SIZE);
        categoryList = categoryService.listAll();
        return SUCCESS;
    }

    /**
     * 商品详情
     */
    public String detail() {
        product = productService.getById(productId);
        if (product == null || product.getStatus() == null
                || product.getStatus() != Constant.STATUS_ON) {
            addActionError("该文创商品不存在或已下架");
            return ERROR;
        }
        categoryList = categoryService.listAll();
        // 已登录用户才判断收藏状态
        if (getLoginUserId() != null) {
            collected = collectService.isCollected(getLoginUserId(), productId);
        }
        return SUCCESS;
    }

    // ==================== getter / setter ====================
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Page<Product> getPage() {
        return page;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public Product getProduct() {
        return product;
    }

    public boolean isCollected() {
        return collected;
    }
}
