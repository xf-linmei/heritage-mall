package com.heritage.action.admin;

import com.heritage.common.BaseAction;
import com.heritage.common.Constant;
import com.heritage.common.Page;
import com.heritage.common.UploadUtil;
import com.heritage.entity.Category;
import com.heritage.entity.Product;
import com.heritage.service.CategoryService;
import com.heritage.service.ProductService;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台文创商品管理 Action
 *
 * <p>上传支持两种：</p>
 * <ul>
 *   <li>主图：单个 File（属性名 image）；</li>
 *   <li>详情图：多文件 List<File>（属性名 detailImage，JSP 中多个同名 file 输入框）。</li>
 * </ul>
 */
@Controller("com.heritage.action.admin.ProductAction")
@Scope("prototype")
public class ProductAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    // ==================== 接收参数 ====================
    private Integer productId;
    private Integer status;
    private Integer categoryId;
    private String keyword;
    private Integer pageNo;
    private Product product;

    // ==================== 上传文件 ====================
    private File image;                          // 主图
    private String imageFileName;
    private String imageContentType;
    private List<File> detailImage;              // 多张详情图
    private List<String> detailImageFileName;
    private List<String> detailImageContentType;

    // ==================== 页面数据 ====================
    private Page<Product> page;
    private List<Category> categoryList;

    /** 商品列表：分页 + 分类筛选 + 名称搜索 */
    public String list() {
        int pageNum = (pageNo == null || pageNo < 1) ? 1 : pageNo;
        // 后台传 status=null，表示上架和下架的都要看
        page = productService.listByCondition(categoryId, keyword, null, null, null,
                "default", pageNum, Constant.ADMIN_PAGE_SIZE);
        categoryList = categoryService.listAll();
        return SUCCESS;
    }

    /** 跳转新增页（需要分类下拉框数据） */
    public String toAdd() {
        categoryList = categoryService.listAll();
        return SUCCESS;
    }

    /** 新增商品（上传主图与详情图） */
    public String add() {
        categoryList = categoryService.listAll();
        if (product == null || !StringUtils.hasText(product.getProductName())
                || product.getPrice() == null || product.getCategoryId() == null) {
            addActionError("请填写商品名称、分类与价格");
            return INPUT;
        }
        try {
            String webRoot = ServletActionContext.getServletContext().getRealPath("/");
            // 主图
            if (image != null) {
                product.setMainImage(UploadUtil.saveImage(image, imageFileName, webRoot,
                        UploadUtil.FOLDER_PRODUCT));
            }
            // 详情图（多张，用逗号拼接成字符串存库）
            String detailPaths = saveDetailImages(webRoot);
            if (detailPaths != null) {
                product.setDetailImages(detailPaths);
            }
            productService.add(product);
        } catch (IOException e) {
            addActionError("图片上传失败：" + e.getMessage());
            return INPUT;
        }
        return SUCCESS;     // 重定向到列表页
    }

    /** 跳转编辑页 */
    public String toEdit() {
        product = productService.getById(productId);
        categoryList = categoryService.listAll();
        return SUCCESS;
    }

    /** 修改商品（上传了新图才替换旧图，并删除磁盘旧文件） */
    public String edit() {
        categoryList = categoryService.listAll();
        if (product == null || product.getProductId() == null) {
            addActionError("参数错误");
            return INPUT;
        }
        try {
            String webRoot = ServletActionContext.getServletContext().getRealPath("/");
            Product old = productService.getById(product.getProductId());
            if (image != null) {
                UploadUtil.deleteImage(old.getMainImage(), webRoot);
                product.setMainImage(UploadUtil.saveImage(image, imageFileName, webRoot,
                        UploadUtil.FOLDER_PRODUCT));
            }
            String detailPaths = saveDetailImages(webRoot);
            if (detailPaths != null) {
                // 重新上传了详情图，则删除原来的全部详情图
                deleteDetailImages(old.getDetailImages(), webRoot);
                product.setDetailImages(detailPaths);
            }
            productService.update(product);
        } catch (IOException e) {
            addActionError("图片上传失败：" + e.getMessage());
            return INPUT;
        }
        return SUCCESS;
    }

    /** 删除商品（连同图片文件一起删除） */
    public String delete() {
        Product old = productService.getById(productId);
        if (old != null) {
            String webRoot = ServletActionContext.getServletContext().getRealPath("/");
            UploadUtil.deleteImage(old.getMainImage(), webRoot);
            deleteDetailImages(old.getDetailImages(), webRoot);
            productService.delete(productId);
            setFlashMsg("商品已删除");
        }
        return SUCCESS;
    }

    /** 上架 / 下架 */
    public String updateStatus() {
        productService.updateStatus(productId, status);
        setFlashMsg(status != null && status == 1 ? "商品已上架" : "商品已下架");
        return SUCCESS;
    }

    /**
     * 保存多张详情图，返回用英文逗号拼接的路径字符串
     *
     * @return 没有上传详情图时返回 null
     */
    private String saveDetailImages(String webRoot) throws IOException {
        if (detailImage == null || detailImage.isEmpty()) {
            return null;
        }
        List<String> paths = new ArrayList<String>();
        for (int i = 0; i < detailImage.size(); i++) {
            File file = detailImage.get(i);
            String name = (detailImageFileName != null && detailImageFileName.size() > i)
                    ? detailImageFileName.get(i) : "detail.jpg";
            paths.add(UploadUtil.saveImage(file, name, webRoot, UploadUtil.FOLDER_PRODUCT));
        }
        // 手动拼接，避免依赖额外工具类
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(paths.get(i));
        }
        return sb.toString();
    }

    /** 删除商品原有的全部详情图文件 */
    private void deleteDetailImages(String detailImages, String webRoot) {
        if (!StringUtils.hasText(detailImages)) {
            return;
        }
        String[] arr = detailImages.split(",");
        for (String path : arr) {
            UploadUtil.deleteImage(path, webRoot);
        }
    }

    // ==================== getter / setter ====================
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Page<Product> getPage() {
        return page;
    }

    public List<Category> getCategoryList() {
        return categoryList;
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

    public void setDetailImage(List<File> detailImage) {
        this.detailImage = detailImage;
    }

    public void setDetailImageFileName(List<String> detailImageFileName) {
        this.detailImageFileName = detailImageFileName;
    }

    public void setDetailImageContentType(List<String> detailImageContentType) {
        this.detailImageContentType = detailImageContentType;
    }
}
