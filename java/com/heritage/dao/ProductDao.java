package com.heritage.dao;

import com.heritage.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 文创商品 DAO
 *
 * <p>selectByCondition / countByCondition 使用 MyBatis 动态 SQL，
 * 支持：分类筛选、名称模糊搜索、价格区间筛选、上架状态、排序、分页。</p>
 */
public interface ProductDao {

    /** 按主键查询商品 */
    Product selectById(@Param("productId") Integer productId);

    /**
     * 多条件分页查询商品
     *
     * @param categoryId 分类ID（可为 null）
     * @param keyword    商品名称关键字（可为 null，模糊匹配）
     * @param minPrice   最低价（可为 null）
     * @param maxPrice   最高价（可为 null）
     * @param status     上架状态（前台传 1，后台传 null 表示全部）
     * @param sort       排序方式：default / hot / new / price_asc / price_desc
     */
    List<Product> selectByCondition(@Param("categoryId") Integer categoryId,
                                    @Param("keyword") String keyword,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    @Param("status") Integer status,
                                    @Param("sort") String sort,
                                    @Param("startIndex") Integer startIndex,
                                    @Param("pageSize") Integer pageSize);

    /** 多条件查询的总记录数（与 selectByCondition 条件必须一致） */
    int countByCondition(@Param("categoryId") Integer categoryId,
                         @Param("keyword") String keyword,
                         @Param("minPrice") BigDecimal minPrice,
                         @Param("maxPrice") BigDecimal maxPrice,
                         @Param("status") Integer status);

    /** 首页热门推荐（销量最高的前 N 件上架商品） */
    List<Product> selectHot(@Param("limit") int limit);

    /** 首页新品推荐（最新上架的前 N 件商品） */
    List<Product> selectNew(@Param("limit") int limit);

    /** 新增商品（自增主键回填到 product.productId） */
    int insert(Product product);

    /** 修改商品 */
    int update(Product product);

    /** 删除商品 */
    int delete(@Param("productId") Integer productId);

    /** 上架 / 下架 */
    int updateStatus(@Param("productId") Integer productId, @Param("status") Integer status);

    /**
     * 扣减库存（下单事务中调用）
     * SQL 带 AND stock >= #{quantity} 条件：库存不足时影响行数为 0，
     * Service 检测到 0 就抛异常，事务回滚，绝对不会出现超卖。
     */
    int deductStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);

    /** 回补库存（取消订单时调用） */
    int restoreStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
}
