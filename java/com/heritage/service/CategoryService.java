package com.heritage.service;

import com.heritage.entity.Category;

import java.util.List;

/**
 * 非遗分类业务接口
 */
public interface CategoryService {

    /** 查询全部分类（按排序值升序） */
    List<Category> listAll();

    /** 按主键查询分类 */
    Category getById(Integer categoryId);

    /** 新增分类 */
    int add(Category category);

    /** 修改分类 */
    int update(Category category);

    /**
     * 删除分类
     *
     * @throws RuntimeException 分类下仍存在商品时抛出，因为一抛异常触发事务回滚，
     *                          也不会出现"商品没有分类"的脏数据
     */
    int delete(Integer categoryId);
}
