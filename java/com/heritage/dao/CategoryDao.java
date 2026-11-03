package com.heritage.dao;

import com.heritage.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 非遗分类 DAO
 */
public interface CategoryDao {

    /** 查询全部分类（按 sort_order 升序，前台导航与后台管理共用） */
    List<Category> selectAll();

    /** 按主键查询分类 */
    Category selectById(@Param("categoryId") Integer categoryId);

    /** 新增分类 */
    int insert(Category category);

    /** 修改分类 */
    int update(Category category);

    /** 删除分类（Service 层会先校验：分类下有商品时禁止删除） */
    int delete(@Param("categoryId") Integer categoryId);

    /** 统计某个分类下的商品数量（删除校验用） */
    int countProduct(@Param("categoryId") Integer categoryId);
}
