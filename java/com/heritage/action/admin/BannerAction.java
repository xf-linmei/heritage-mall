package com.heritage.action.admin;

import com.heritage.common.BaseAction;
import com.heritage.common.UploadUtil;
import com.heritage.entity.Banner;
import com.heritage.service.BannerService;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 后台轮播 Banner 管理 Action
 *
 * <p>文件上传三件套（Struts2 约定）：</p>
 * <ul>
 *   <li>{@code File image}            —— 上传的临时文件，属性名必须与表单 file 标签的 name 一致</li>
 *   <li>{@code String imageFileName}  —— 原始文件名（固定写法：属性名 + FileName）</li>
 *   <li>{@code String imageContentType} —— 文件类型（固定写法：属性名 + ContentType）</li>
 * </ul>
 */
@Controller("com.heritage.action.admin.BannerAction")
@Scope("prototype")
public class BannerAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private BannerService bannerService;

    // ==================== 接收参数 ====================
    private Integer bannerId;
    private Integer status;
    private Banner banner;

    // ==================== 上传文件 ====================
    private File image;
    private String imageFileName;
    private String imageContentType;

    // ==================== 页面数据 ====================
    private List<Banner> bannerList;

    /** 轮播列表 */
    public String list() {
        bannerList = bannerService.listAll();
        return SUCCESS;
    }

    /** 跳转新增页 */
    public String toAdd() {
        return SUCCESS;
    }

    /** 新增轮播（含图片上传） */
    public String add() {
        try {
            String imagePath = saveImage();
            if (imagePath == null) {
                setFlashMsg("请上传轮播图片（jpg / png，5MB 以内）");
                return SUCCESS;
            }
            banner.setImageUrl(imagePath);
            bannerService.add(banner);
            setFlashMsg("轮播新增成功");
        } catch (IOException e) {
            setFlashMsg("上传失败：" + e.getMessage());
        }
        return SUCCESS;
    }

    /** 跳转编辑页 */
    public String toEdit() {
        banner = bannerService.getById(bannerId);
        return SUCCESS;
    }

    /** 修改轮播（不上传新图时保留原图） */
    public String edit() {
        try {
            String imagePath = saveImage();
            if (imagePath != null) {
                // 上传了新图：删除旧图后替换
                Banner old = bannerService.getById(banner.getBannerId());
                String webRoot = ServletActionContext.getServletContext().getRealPath("/");
                UploadUtil.deleteImage(old.getImageUrl(), webRoot);
                banner.setImageUrl(imagePath);
            }
            bannerService.update(banner);
            setFlashMsg("轮播修改成功");
        } catch (IOException e) {
            setFlashMsg("上传失败：" + e.getMessage());
        }
        return SUCCESS;
    }

    /** 删除轮播（连同磁盘上的图片一起删除） */
    public String delete() {
        Banner old = bannerService.getById(bannerId);
        if (old != null) {
            String webRoot = ServletActionContext.getServletContext().getRealPath("/");
            UploadUtil.deleteImage(old.getImageUrl(), webRoot);
            bannerService.delete(bannerId);
            setFlashMsg("轮播已删除");
        }
        return SUCCESS;
    }

    /** 启用 / 禁用 */
    public String updateStatus() {
        bannerService.updateStatus(bannerId, status);
        setFlashMsg("状态已更新");
        return SUCCESS;
    }

    /**
     * 保存上传的图片到 upload/banner 目录
     *
     * @return 保存后的访问路径；没有选择文件时返回 null
     */
    private String saveImage() throws IOException {
        if (image == null) {
            return null;
        }
        String webRoot = ServletActionContext.getServletContext().getRealPath("/");
        return UploadUtil.saveImage(image, imageFileName, webRoot, UploadUtil.FOLDER_BANNER);
    }

    // ==================== getter / setter ====================
    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }
}
