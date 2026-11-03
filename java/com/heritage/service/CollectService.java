package com.heritage.service;

import com.heritage.entity.Collect;

import java.util.List;

/**
 * 商品收藏业务接口
 */
public interface CollectService {

    /** 查询某用户收藏的全部商品（已关联商品信息） */
    List<Collect> listCollect(Integer userId);

    /** 添加收藏（已收藏过则不再重复添加） */
    int add(Integer userId, Integer productId);

    /** 取消收藏 */
    int remove(Integer userId, Integer productId);

    /** 判断是否已收藏（详情页显示"已收藏/收藏"） */
    boolean isCollected(Integer userId, Integer productId);

    /** 收藏数量 */
    int countCollect(Integer userId);
}
