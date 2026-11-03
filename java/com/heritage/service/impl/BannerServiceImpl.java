package com.heritage.service.impl;

import com.heritage.common.Constant;
import com.heritage.dao.BannerDao;
import com.heritage.entity.Banner;
import com.heritage.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页轮播业务实现类
 */
@Service("bannerService")
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerDao bannerDao;

    @Override
    public List<Banner> listAll() {
        return bannerDao.selectAll();
    }

    @Override
    public List<Banner> listEnabled() {
        return bannerDao.selectEnabled();
    }

    @Override
    public Banner getById(Integer bannerId) {
        return bannerDao.selectById(bannerId);
    }

    @Override
    public int add(Banner banner) {
        if (banner.getSortOrder() == null) {
            banner.setSortOrder(0);
        }
        if (banner.getStatus() == null) {
            banner.setStatus(Constant.STATUS_ON);
        }
        return bannerDao.insert(banner);
    }

    @Override
    public int update(Banner banner) {
        return bannerDao.update(banner);
    }

    @Override
    public int delete(Integer bannerId) {
        return bannerDao.delete(bannerId);
    }

    @Override
    public int updateStatus(Integer bannerId, Integer status) {
        return bannerDao.updateStatus(bannerId, status);
    }
}
