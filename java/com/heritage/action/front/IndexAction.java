package com.heritage.action.front;

import com.heritage.common.BaseAction;
import com.heritage.entity.Banner;
import com.heritage.entity.Category;
import com.heritage.entity.Product;
import com.heritage.service.BannerService;
import com.heritage.service.CategoryService;
import com.heritage.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 前台首页 Action
 *
 * <p>负责装配首页需要的四块数据：</p>
 * <ol>
 *   <li>启用的轮播 banner（后台可管理）；</li>
 *   <li>非遗分类导航；</li>
 *   <li>热门文创推荐（按销量）；</li>
 *   <li>新品推荐（按上架时间）。</li>
 * </ol>
 *
 * <p>属性写 getter 即可被值栈访问，JSP 用 Struts2 标签或 EL 直接取值。</p>
 */
@Controller("com.heritage.action.front.IndexAction")
@Scope("prototype")
public class IndexAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private BannerService bannerService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    // ==================== 页面数据（值栈属性，需提供 getter）====================
    private List<Banner> bannerList;
    private List<Category> categoryList;
    private List<Product> hotList;
    private List<Product> newList;

    public String index() {
        bannerList = bannerService.listEnabled();          // 只展示启用的轮播
        categoryList = categoryService.listAll();          // 分类导航
        hotList = productService.listHot(8);               // 热门文创 8 件
        newList = productService.listNew(8);               // 新品推荐 8 件
        return SUCCESS;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public List<Product> getHotList() {
        return hotList;
    }

    public List<Product> getNewList() {
        return newList;
    }
}
