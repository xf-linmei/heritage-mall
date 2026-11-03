package com.heritage.dao;

import com.heritage.entity.Banner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页轮播图 DAO
 */
public interface BannerDao {

    /** 后台：查询全部轮播（按 sort_order 升序、ID 降序） */
    List<Banner> selectAll();

    /** 前台首页：只查启用的轮播 */
    List<Banner> selectEnabled();

    /** 按主键查询 */
    Banner selectById(@Param("bannerId") Integer bannerId);

    /** 新增轮播 */
    int insert(Banner banner);

    /** 修改轮播 */
    int update(Banner banner);

    /** 删除轮播 */
    int delete(@Param("bannerId") Integer bannerId);

    /** 启用 / 禁用 */
    int updateStatus(@Param("bannerId") Integer bannerId, @Param("status") Integer status);
}
