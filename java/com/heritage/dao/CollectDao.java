package com.heritage.dao;

import com.heritage.entity.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品收藏 DAO
 */
public interface CollectDao {

    /** 查询某用户收藏的全部商品（关联商品信息，按收藏时间倒序） */
    List<Collect> selectByUser(@Param("userId") Integer userId);

    /** 判断是否已收藏过该商品（避免重复收藏） */
    Collect selectByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /** 新增收藏 */
    int insert(Collect collect);

    /** 按主键取消收藏 */
    int delete(@Param("collectId") Integer collectId);

    /** 按用户+商品取消收藏 */
    int deleteByUserAndProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /** 统计某用户的收藏数量 */
    int countByUser(@Param("userId") Integer userId);
}
