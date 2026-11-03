package com.heritage.service.impl;

import com.heritage.dao.CategoryDao;
import com.heritage.entity.Category;
import com.heritage.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 非遗分类业务实现类
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> listAll() {
        return categoryDao.selectAll();
    }

    @Override
    public Category getById(Integer categoryId) {
        return categoryDao.selectById(categoryId);
    }

    @Override
    public int add(Category category) {
        // 没填排序值时默认给 0，避免数据库里 NULL 影响排序
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        return categoryDao.insert(category);
    }

    @Override
    public int update(Category category) {
        return categoryDao.update(category);
    }

    @Override
    public int delete(Integer categoryId) {
        // 业务校验：分类下还有商品时禁止删除（作业要求）
        int productCount = categoryDao.countProduct(categoryId);
        if (productCount > 0) {
            // 抛运行时异常 → Spring 事务回滚，Action 捕获后转成页面错误提示
            throw new RuntimeException("该分类下还有 " + productCount + " 件文创商品，不能删除！请先转移或删除这些商品。");
        }
        return categoryDao.delete(categoryId);
    }
}
