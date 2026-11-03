package com.heritage.action.admin;

import com.heritage.common.BaseAction;
import com.heritage.entity.Category;
import com.heritage.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 后台非遗分类管理 Action
 *
 * <p>删除前由 CategoryService 做业务校验：分类下存在商品时抛异常，
 * 这里捕获后重新加载列表并返回 input，在列表页显示错误提示（struts.xml 已配 input 结果）。</p>
 */
@Controller("com.heritage.action.admin.CategoryAction")
@Scope("prototype")
public class CategoryAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CategoryService categoryService;

    private Integer categoryId;
    private Category category;
    private List<Category> categoryList;

    /** 分类列表 */
    public String list() {
        categoryList = categoryService.listAll();
        return SUCCESS;
    }

    /** 新增分类 */
    public String add() {
        if (category == null || !StringUtils.hasText(category.getCategoryName())) {
            setFlashMsg("请填写分类名称");
            return SUCCESS;
        }
        categoryService.add(category);
        setFlashMsg("分类新增成功");
        return SUCCESS;
    }

    /** 修改分类 */
    public String edit() {
        if (category == null || category.getCategoryId() == null) {
            setFlashMsg("参数错误");
            return SUCCESS;
        }
        categoryService.update(category);
        setFlashMsg("分类修改成功");
        return SUCCESS;
    }

    /**
     * 删除分类
     * 校验失败时返回 input（列表页），并把错误信息显示在页面上
     */
    public String delete() {
        try {
            categoryService.delete(categoryId);
            setFlashMsg("分类已删除");
            return SUCCESS;
        } catch (RuntimeException e) {
            // 重新加载列表数据，否则页面没有数据可显示
            categoryList = categoryService.listAll();
            addActionError(e.getMessage());
            return INPUT;
        }
    }

    // ==================== getter / setter ====================
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }
}
