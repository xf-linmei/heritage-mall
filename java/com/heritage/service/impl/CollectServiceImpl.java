package com.heritage.service.impl;

import com.heritage.dao.CollectDao;
import com.heritage.entity.Collect;
import com.heritage.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品收藏业务实现类
 */
@Service("collectService")
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectDao collectDao;

    @Override
    public List<Collect> listCollect(Integer userId) {
        return collectDao.selectByUser(userId);
    }

    @Override
    public int add(Integer userId, Integer productId) {
        if (userId == null || productId == null) {
            return 0;
        }
        // 已收藏过就直接返回，避免 collect 表唯一键冲突
        if (collectDao.selectByUserAndProduct(userId, productId) != null) {
            return 1;
        }
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setProductId(productId);
        return collectDao.insert(collect);
    }

    @Override
    public int remove(Integer userId, Integer productId) {
        return collectDao.deleteByUserAndProduct(userId, productId);
    }

    @Override
    public boolean isCollected(Integer userId, Integer productId) {
        if (userId == null || productId == null) {
            return false;
        }
        return collectDao.selectByUserAndProduct(userId, productId) != null;
    }

    @Override
    public int countCollect(Integer userId) {
        return collectDao.countByUser(userId);
    }
}
