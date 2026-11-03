package com.heritage.service;

import com.heritage.common.Page;
import com.heritage.entity.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * 文创商品业务接口
 */
public interface ProductService {

    /** 按主键查询商品（含分类名称） */
    Product getById(Integer productId);

    /**
     * 多条件分页查询
     *
     * @param categoryId 分类ID，可为 null
     * @param keyword    商品名称关键字，可为 null
     * @param minPrice   最低价，可为 null
     * @param maxPrice   最高价，可为 null
     * @param status     上架状态，前台传 1；后台传 null 表示全部
     * @param sort       排序：default / hot / new / price_asc / price_desc
     * @return 分页对象，包含当前页数据与总记录数
     */
    Page<Product> listByCondition(Integer categoryId, String keyword, BigDecimal minPrice,
                                  BigDecimal maxPrice, Integer status, String sort,
                                  int pageNo, int pageSize);

    /** 首页热门推荐 */
    List<Product> listHot(int limit);

    /** 首页新品推荐 */
    List<Product> listNew(int limit);

    /** 新增商品 */
    int add(Product product);

    /** 修改商品 */
    int update(Product product);

    /** 删除商品 */
    int delete(Integer productId);

    /** 上架 / 下架 */
    int updateStatus(Integer productId, Integer status);
}
