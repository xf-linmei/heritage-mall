package com.heritage.service;

import com.heritage.entity.Address;

import java.util.List;

/**
 * 收货地址业务接口
 */
public interface AddressService {

    /** 查询某用户的全部地址（默认地址排最前） */
    List<Address> listAddress(Integer userId);

    /** 按主键查询地址 */
    Address getById(Integer addressId);

    /** 新增地址（第一条地址自动成为默认地址） */
    int add(Address address);

    /** 修改地址 */
    int update(Address address);

    /** 删除地址（校验归属，防止删别人的地址） */
    int delete(Integer userId, Integer addressId);

    /** 设置默认地址：先取消其它默认，再设置新的，保证唯一 */
    int setDefault(Integer userId, Integer addressId);
}
