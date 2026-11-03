package com.heritage.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * 文件上传工具类
 *
 * <p>职责（对应作业要求：限制 jpg/png、限制大小、自动重命名、保存到 webapp/upload 目录）：</p>
 * <ol>
 *   <li>校验文件大小不能超过 5MB；</li>
 *   <li>校验后缀必须是 .jpg / .jpeg / .png；</li>
 *   <li>用 UUID 重新生成文件名，避免中文名、重名覆盖导致的问题；</li>
 *   <li>把 Struts2 生成的临时文件 copy 到 webapp/upload/{folder}/ 目录；</li>
 *   <li>返回数据库要保存的访问路径（如 /upload/product/xxx.jpg）。</li>
 * </ol>
 *
 * <p>注意：数据库里保存的是<b>不含项目名的相对路径</b>，
 * JSP 展示时统一拼 ${pageContext.request.contextPath}，
 * 这样即使更换部署的项目名，图片也不会裂。</p>
 */
public class UploadUtil {

    /** 轮播图存放子目录 */
    public static final String FOLDER_BANNER = "banner";
    /** 商品图存放子目录 */
    public static final String FOLDER_PRODUCT = "product";

    private UploadUtil() {
    }

    /**
     * 保存上传的图片
     *
     * @param tempFile    Struts2 上传组件自动存放的临时文件（Action 中的 File 属性）
     * @param fileName    原始文件名（Action 中的 xxxFileName 属性）
     * @param webRootPath webapp 的真实物理路径，由 ServletContext.getRealPath("/") 得到
     * @param folder      子目录名：banner / product
     * @return 保存后的访问路径，如 /upload/product/uuid.jpg
     * @throws IOException 文件为空、超过大小限制、后缀不合法或写入失败时抛出，由 Action 转成错误提示
     */
    public static String saveImage(File tempFile, String fileName, String webRootPath, String folder)
            throws IOException {
        if (tempFile == null || !tempFile.exists()) {
            throw new IOException("请选择要上传的图片");
        }
        if (tempFile.length() > Constant.MAX_UPLOAD_SIZE) {
            throw new IOException("图片大小不能超过 " + (Constant.MAX_UPLOAD_SIZE / 1024 / 1024) + "MB");
        }

        // 取后缀并校验：toLowerCase 兼容大写后缀，找不到点则后缀为空串
        String ext = "";
        if (fileName != null && fileName.lastIndexOf('.') > -1) {
            ext = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
        }
        if (!Arrays.asList(Constant.ALLOWED_IMAGE_EXT).contains(ext)) {
            throw new IOException("图片格式不支持，只能上传 jpg / jpeg / png");
        }

        // 目标目录：webapp/upload/{folder}
        File dir = new File(webRootPath, Constant.UPLOAD_DIR + File.separator + folder);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("创建上传目录失败：" + dir.getAbsolutePath());
        }

        // UUID 重命名，避免中文文件名乱码与同名覆盖
        String newName = UUID.randomUUID().toString().replace("-", "") + ext;
        File dest = new File(dir, newName);
        FileUtils.copyFile(tempFile, dest);

        // 返回相对访问路径（JSP 里会拼上项目名）
        return "/" + Constant.UPLOAD_DIR + "/" + folder + "/" + newName;
    }

    /**
     * 删除磁盘上的旧图片（删除商品或替换图片时调用，避免留下垃圾文件）
     *
     * @param imageUrl    数据库中的路径，如 /upload/product/xxx.jpg
     * @param webRootPath webapp 的真实物理路径
     */
    public static void deleteImage(String imageUrl, String webRootPath) {
        if (imageUrl == null || imageUrl.trim().length() == 0 || webRootPath == null) {
            return;
        }
        // 把 "项目名/upload/..." 或 "/upload/..." 统一处理成相对 webapp 的路径
        String relative = imageUrl;
        int idx = relative.indexOf("/" + Constant.UPLOAD_DIR + "/");
        if (idx > -1) {
            relative = relative.substring(idx + 1);
        }
        File file = new File(webRootPath, relative);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }
}
