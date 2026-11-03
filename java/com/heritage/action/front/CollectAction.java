package com.heritage.action.front;

import com.heritage.common.BaseAction;
import com.heritage.entity.Collect;
import com.heritage.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 我的收藏 Action：收藏列表、加入收藏、取消收藏
 */
@Controller("com.heritage.action.front.CollectAction")
@Scope("prototype")
public class CollectAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CollectService collectService;

    private Integer productId;
    private Integer collectId;
    private List<Collect> collectList;

    /** 收藏列表 */
    public String list() {
        collectList = collectService.listCollect(getLoginUserId());
        return SUCCESS;
    }

    /** 加入收藏（struts.xml 中配置：成功后重定向回商品详情页） */
    public String add() {
        int rows = collectService.add(getLoginUserId(), productId);
        setFlashMsg(rows > 0 ? "已加入我的收藏" : "收藏失败");
        return SUCCESS;
    }

    /** 取消收藏 */
    public String delete() {
        if (collectId != null) {
            // 从收藏列表页传来的是 collectId，这里按用户+商品删除更安全
            collectService.remove(getLoginUserId(), productId);
        }
        setFlashMsg("已取消收藏");
        return SUCCESS;
    }

    // ==================== getter / setter ====================
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCollectId() {
        return collectId;
    }

    public void setCollectId(Integer collectId) {
        this.collectId = collectId;
    }

    public List<Collect> getCollectList() {
        return collectList;
    }
}
