package com.heritage.service.impl;

import com.heritage.common.Constant;
import com.heritage.dao.AddressDao;
import com.heritage.entity.Address;
import com.heritage.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址业务实现类
 */
@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> listAddress(Integer userId) {
        return addressDao.selectByUser(userId);
    }

    @Override
    public Address getById(Integer addressId) {
        return addressDao.selectById(addressId);
    }

    @Override
    public int add(Address address) {
        if (address == null || address.getUserId() == null) {
            return 0;
        }
        List<Address> existList = addressDao.selectByUser(address.getUserId());
        // 第一条地址自动成为默认地址，避免结算时没有默认可用
        if (existList == null || existList.isEmpty()) {
            address.setIsDefault(1);
        } else if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            // 用户勾选了默认，则先把其它地址取消默认
            addressDao.clearDefault(address.getUserId());
        } else {
            address.setIsDefault(0);
        }
        return addressDao.insert(address);
    }

    @Override
    public int update(Address address) {
        if (address == null || address.getUserId() == null) {
            return 0;
        }
        // 归属校验：只能修改自己的地址
        Address old = addressDao.selectById(address.getAddressId());
        if (old == null || !old.getUserId().equals(address.getUserId())) {
            return 0;
        }
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            addressDao.clearDefault(address.getUserId());
        } else {
            address.setIsDefault(0);
        }
        return addressDao.update(address);
    }

    @Override
    public int delete(Integer userId, Integer addressId) {
        Address address = addressDao.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return 0;                                   // 地址不存在或不属于当前用户
        }
        return addressDao.delete(addressId);
    }

    @Override
    public int setDefault(Integer userId, Integer addressId) {
        Address address = addressDao.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return 0;
        }
        // 两步：全部取消默认 → 设置目标为默认
        addressDao.clearDefault(userId);
        return addressDao.setDefault(addressId, userId);
    }
}
