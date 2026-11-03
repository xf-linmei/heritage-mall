package com.heritage.service;

import com.heritage.entity.Banner;

import java.util.List;

/**
 * 首页轮播业务接口
 */
public interface BannerService {

    /** 后台：查询全部轮播 */
    List<Banner> listAll();

    /** 前台首页：只查询启用状态的轮播 */
    List<Banner> listEnabled();

    /** 按主键查询 */
    Banner getById(Integer bannerId);

    /** 新增轮播（图片路径由 Action 完成上传后传入） */
    int add(Banner banner);

    /** 修改轮播 */
    int update(Banner banner);

    /** 删除轮播 */
    int delete(Integer bannerId);

    /** 启用 / 禁用 */
    int updateStatus(Integer bannerId, Integer status);
}
