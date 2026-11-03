package com.heritage.dao;

import com.heritage.entity.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收货地址 DAO
 */
public interface AddressDao {

    /** 查询某用户的全部地址（默认地址排最前） */
    List<Address> selectByUser(@Param("userId") Integer userId);

    /** 按主键查询地址 */
    Address selectById(@Param("addressId") Integer addressId);

    /** 新增地址 */
    int insert(Address address);

    /** 修改地址 */
    int update(Address address);

    /** 删除地址 */
    int delete(@Param("addressId") Integer addressId);

    /** 先把该用户的所有地址取消默认，保证设置默认地址时全库只有一个默认 */
    int clearDefault(@Param("userId") Integer userId);

    /** 把指定地址设为默认 */
    int setDefault(@Param("addressId") Integer addressId, @Param("userId") Integer userId);
}
