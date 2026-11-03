package com.heritage.action.front;

import com.heritage.common.BaseAction;
import com.heritage.entity.Address;
import com.heritage.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 收货地址 Action：新增、编辑、删除、设置默认地址
 */
@Controller("com.heritage.action.front.AddressAction")
@Scope("prototype")
public class AddressAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private AddressService addressService;

    private Integer addressId;
    private Address address;
    private List<Address> addressList;

    /** 地址列表 */
    public String list() {
        addressList = addressService.listAddress(getLoginUserId());
        return SUCCESS;
    }

    /** 新增地址 */
    public String add() {
        if (address == null || !StringUtils.hasText(address.getReceiver())
                || !StringUtils.hasText(address.getPhone())
                || !StringUtils.hasText(address.getAddressDetail())) {
            setFlashMsg("请填写完整的收货信息");
            return SUCCESS;
        }
        address.setUserId(getLoginUserId());        // 归属当前登录用户，防止越权
        int rows = addressService.add(address);
        setFlashMsg(rows > 0 ? "收货地址添加成功" : "添加失败");
        return SUCCESS;
    }

    /** 编辑地址 */
    public String update() {
        if (address == null || address.getAddressId() == null) {
            setFlashMsg("参数错误");
            return SUCCESS;
        }
        address.setUserId(getLoginUserId());        // Service 里会校验归属
        int rows = addressService.update(address);
        setFlashMsg(rows > 0 ? "收货地址修改成功" : "修改失败，地址不存在或无权修改");
        return SUCCESS;
    }

    /** 删除地址 */
    public String delete() {
        int rows = addressService.delete(getLoginUserId(), addressId);
        setFlashMsg(rows > 0 ? "收货地址已删除" : "删除失败");
        return SUCCESS;
    }

    /** 设置为默认地址 */
    public String setDefault() {
        int rows = addressService.setDefault(getLoginUserId(), addressId);
        setFlashMsg(rows > 0 ? "已设为默认地址" : "设置失败");
        return SUCCESS;
    }

    // ==================== getter / setter ====================
    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Address> getAddressList() {
        return addressList;
    }
}
