package com.heritage.common;

/**
 * 系统常量类
 * 把散落在各处的"魔法值"（session 键、角色、状态码、分页大小、上传限制）集中管理，
 * 便于维护，也方便在作业报告里说明设计。
 */
public class Constant {

    private Constant() {
    }

    // ==================== Session 键 ====================
    /** 登录成功后，用户对象存入 session 的键名 */
    public static final String SESSION_USER = "loginUser";
    /** 一次性提示信息（跨重定向显示）存入 session 的键名 */
    public static final String SESSION_FLASH_MSG = "flashMsg";
    /** 非遗分类列表存入 application 的键名（全站导航共用，避免每个页面重复查库） */
    public static final String APP_CATEGORY_LIST = "categoryList";

    // ==================== 角色与状态 ====================
    /** 角色：管理员 */
    public static final String ROLE_ADMIN = "ADMIN";
    /** 角色：普通用户 */
    public static final String ROLE_USER = "USER";
    /** 通用状态：启用 */
    public static final int STATUS_ON = 1;
    /** 通用状态：禁用/下架 */
    public static final int STATUS_OFF = 0;

    // ==================== 订单状态（0待付款 1待发货 2待收货 3已完成 4已取消）====================
    public static final int ORDER_WAIT_PAY = 0;
    public static final int ORDER_WAIT_SEND = 1;
    public static final int ORDER_WAIT_RECEIVE = 2;
    public static final int ORDER_FINISHED = 3;
    public static final int ORDER_CANCELED = 4;

    /**
     * 把订单状态码转换成中文，供 JSP 页面直接显示
     */
    public static String getOrderStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case ORDER_WAIT_PAY:
                return "待付款";
            case ORDER_WAIT_SEND:
                return "待发货";
            case ORDER_WAIT_RECEIVE:
                return "待收货";
            case ORDER_FINISHED:
                return "已完成";
            case ORDER_CANCELED:
                return "已取消";
            default:
                return "未知";
        }
    }

    // ==================== 商品标记 ====================
    /** 热门推荐 */
    public static final int PRODUCT_HOT = 1;
    /** 新品推荐 */
    public static final int PRODUCT_NEW = 1;

    // ==================== 分页 ====================
    /** 前台商品列表每页条数 */
    public static final int FRONT_PAGE_SIZE = 8;
    /** 后台管理列表每页条数 */
    public static final int ADMIN_PAGE_SIZE = 10;

    // ==================== 文件上传 ====================
    /** 图片保存到 webapp 下的目录名：webapp/upload */
    public static final String UPLOAD_DIR = "upload";
    /** 单个文件大小上限：5MB */
    public static final long MAX_UPLOAD_SIZE = 5 * 1024 * 1024;
    /** 允许的图片后缀 */
    public static final String[] ALLOWED_IMAGE_EXT = {".jpg", ".jpeg", ".png"};
}
