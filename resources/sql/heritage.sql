-- =====================================================================
-- 匠集 · 非遗文创电商系统 数据库初始化脚本
-- MySQL 8.x / 数据库名：heritage
-- 使用方法（任选其一）：
--   1) 命令行： mysql -uroot -p < heritage.sql
--   2) Navicat / IDEA 数据库控制台：直接全选执行本文件
-- 说明：脚本会先 DROP 掉同名库再重建，重复执行可达到"重置数据"的效果
-- =====================================================================

DROP DATABASE IF EXISTS heritage;
CREATE DATABASE heritage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE heritage;

-- ---------------------------------------------------------------------
-- 表1：user 用户表（前台注册用户 + 后台管理员共用一张表，用 role 区分）
-- ---------------------------------------------------------------------
CREATE TABLE user (
    user_id     INT          NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键自增',
    username    VARCHAR(50)  NOT NULL                COMMENT '登录账号',
    password    VARCHAR(64)  NOT NULL                COMMENT '登录密码（MD5 32位密文）',
    real_name   VARCHAR(50)  DEFAULT NULL            COMMENT '真实姓名/昵称',
    phone       VARCHAR(20)  DEFAULT NULL            COMMENT '手机号',
    email       VARCHAR(100) DEFAULT NULL            COMMENT '电子邮箱',
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色：USER普通用户 / ADMIN管理员',
    status      INT          NOT NULL DEFAULT 1      COMMENT '账号状态：1启用 0禁用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

-- ---------------------------------------------------------------------
-- 表2：category 非遗分类表（剪纸 / 扎染 / 木雕 / 银饰）
-- ---------------------------------------------------------------------
CREATE TABLE category (
    category_id   INT          NOT NULL AUTO_INCREMENT COMMENT '分类ID，主键自增',
    category_name VARCHAR(50)  NOT NULL                COMMENT '分类名称，如：剪纸',
    category_desc VARCHAR(200) DEFAULT NULL            COMMENT '分类简介（非遗文化背景）',
    sort_order    INT          NOT NULL DEFAULT 0      COMMENT '排序值，越小越靠前',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (category_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '非遗分类表';

-- ---------------------------------------------------------------------
-- 表3：banner 首页轮播图表（后台可管理：上传图片、排序、启用禁用）
-- ---------------------------------------------------------------------
CREATE TABLE banner (
    banner_id   INT          NOT NULL AUTO_INCREMENT COMMENT '轮播ID，主键自增',
    title       VARCHAR(100) NOT NULL                COMMENT '轮播标题',
    image_url   VARCHAR(200) NOT NULL                COMMENT '轮播图片地址，如 /upload/demo/banner1.png',
    link_url    VARCHAR(200) DEFAULT NULL            COMMENT '点击跳转链接（可为空）',
    sort_order  INT          NOT NULL DEFAULT 0      COMMENT '排序值，越小越靠前',
    status      INT          NOT NULL DEFAULT 1      COMMENT '状态：1启用 0禁用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (banner_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '首页轮播图表';

-- ---------------------------------------------------------------------
-- 表4：product 文创商品表
-- ---------------------------------------------------------------------
CREATE TABLE product (
    product_id    INT            NOT NULL AUTO_INCREMENT COMMENT '商品ID，主键自增',
    category_id   INT            NOT NULL                COMMENT '所属分类ID，外键 category.category_id',
    product_name  VARCHAR(100)   NOT NULL                COMMENT '商品名称',
    product_desc  TEXT                                   COMMENT '非遗文化介绍（商品详情的文化说明）',
    main_image    VARCHAR(200)   DEFAULT NULL            COMMENT '商品主图地址',
    detail_images VARCHAR(1000)  DEFAULT NULL            COMMENT '详情图地址集合，多个用英文逗号分隔',
    price         DECIMAL(10, 2) NOT NULL                COMMENT '商品价格',
    stock         INT            NOT NULL DEFAULT 0      COMMENT '库存数量',
    sales         INT            NOT NULL DEFAULT 0      COMMENT '累计销量',
    status        INT            NOT NULL DEFAULT 1      COMMENT '上架状态：1上架 0下架',
    is_hot        INT            NOT NULL DEFAULT 0      COMMENT '是否热门推荐：1是 0否',
    is_new        INT            NOT NULL DEFAULT 0      COMMENT '是否新品推荐：1是 0否',
    create_time   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (product_id),
    KEY fk_product_category (category_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '文创商品表';

-- ---------------------------------------------------------------------
-- 表5：cart 购物车表（同一用户对同一商品只保留一条记录，重复加入则累加数量）
-- ---------------------------------------------------------------------
CREATE TABLE cart (
    cart_id     INT      NOT NULL AUTO_INCREMENT COMMENT '购物车ID，主键自增',
    user_id     INT      NOT NULL                COMMENT '用户ID，外键 user.user_id',
    product_id  INT      NOT NULL                COMMENT '商品ID，外键 product.product_id',
    quantity    INT      NOT NULL DEFAULT 1      COMMENT '购买数量',
    checked     INT      NOT NULL DEFAULT 1      COMMENT '是否勾选结算：1勾选 0未勾选',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    PRIMARY KEY (cart_id),
    UNIQUE KEY uk_user_product (user_id, product_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '购物车表';

-- ---------------------------------------------------------------------
-- 表6：address 收货地址表（一个用户可有多条地址，最多一条默认地址）
-- ---------------------------------------------------------------------
CREATE TABLE address (
    address_id     INT          NOT NULL AUTO_INCREMENT COMMENT '地址ID，主键自增',
    user_id        INT          NOT NULL                COMMENT '用户ID，外键 user.user_id',
    receiver       VARCHAR(50)  NOT NULL                COMMENT '收货人姓名',
    phone          VARCHAR(20)  NOT NULL                COMMENT '收货人手机号',
    address_detail VARCHAR(200) NOT NULL                COMMENT '详细收货地址',
    is_default     INT          NOT NULL DEFAULT 0      COMMENT '是否默认地址：1默认 0非默认',
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (address_id),
    KEY fk_address_user (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '收货地址表';

-- ---------------------------------------------------------------------
-- 表7：orders 订单主表（status：0待付款 1待发货 2待收货 3已完成 4已取消）
-- 说明：下单时把收货信息（收货人/电话/地址）快照进订单，避免用户改地址影响历史订单
-- ---------------------------------------------------------------------
CREATE TABLE orders (
    order_id      INT            NOT NULL AUTO_INCREMENT COMMENT '订单ID，主键自增',
    order_no      VARCHAR(64)    NOT NULL                COMMENT '订单编号，全局唯一（时间戳+随机数）',
    user_id       INT            NOT NULL                COMMENT '下单用户ID，外键 user.user_id',
    total_amount  DECIMAL(10, 2) NOT NULL                COMMENT '订单总金额',
    status        INT            NOT NULL DEFAULT 0      COMMENT '订单状态：0待付款 1待发货 2待收货 3已完成 4已取消',
    receiver      VARCHAR(50)    NOT NULL                COMMENT '收货人（下单时快照）',
    phone         VARCHAR(20)    NOT NULL                COMMENT '收货电话（下单时快照）',
    address_detail VARCHAR(200)  NOT NULL                COMMENT '收货地址（下单时快照）',
    remark        VARCHAR(200)   DEFAULT NULL            COMMENT '买家留言',
    create_time   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    pay_time      DATETIME       DEFAULT NULL            COMMENT '支付时间（模拟支付）',
    send_time     DATETIME       DEFAULT NULL            COMMENT '发货时间',
    finish_time   DATETIME       DEFAULT NULL            COMMENT '确认收货时间',
    PRIMARY KEY (order_id),
    UNIQUE KEY uk_order_no (order_no),
    KEY fk_order_user (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单主表';

-- ---------------------------------------------------------------------
-- 表8：order_item 订单明细表（下单事务中：主表1条 + 明细N条 + 扣库存N次）
-- ---------------------------------------------------------------------
CREATE TABLE order_item (
    item_id       INT            NOT NULL AUTO_INCREMENT COMMENT '明细ID，主键自增',
    order_id      INT            NOT NULL                COMMENT '订单ID，外键 orders.order_id',
    product_id    INT            NOT NULL                COMMENT '商品ID，外键 product.product_id',
    product_name  VARCHAR(100)   NOT NULL                COMMENT '商品名称（下单时快照）',
    product_image VARCHAR(200)   DEFAULT NULL            COMMENT '商品主图（下单时快照）',
    price         DECIMAL(10, 2) NOT NULL                COMMENT '下单时单价（快照）',
    quantity      INT            NOT NULL                COMMENT '购买数量',
    subtotal      DECIMAL(10, 2) NOT NULL                COMMENT '小计金额 = price * quantity',
    PRIMARY KEY (item_id),
    KEY fk_item_order (order_id),
    KEY fk_item_product (product_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单明细表';

-- ---------------------------------------------------------------------
-- 表9：collect 我的收藏表（用户与商品的多对多关联）
-- ---------------------------------------------------------------------
CREATE TABLE collect (
    collect_id  INT      NOT NULL AUTO_INCREMENT COMMENT '收藏ID，主键自增',
    user_id     INT      NOT NULL                COMMENT '用户ID，外键 user.user_id',
    product_id  INT      NOT NULL                COMMENT '商品ID，外键 product.product_id',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (collect_id),
    UNIQUE KEY uk_user_product (user_id, product_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '商品收藏表';

-- =====================================================================
-- 预置测试数据
-- =====================================================================

-- -------- 用户数据 --------
-- 管理员：admin / 123456（密码用 MySQL MD5() 生成，与 Java 端 DigestUtils.md5DigestAsHex 一致）
INSERT INTO user (username, password, real_name, phone, email, role, status) VALUES
('admin', MD5('123456'), '系统管理员', '13800000000', 'admin@jiangji.com', 'ADMIN', 1);
-- 普通测试用户：test / 123456
INSERT INTO user (username, password, real_name, phone, email, role, status) VALUES
('test',  MD5('123456'), '张小匠',     '13900000001', 'test@jiangji.com',  'USER',  1);

-- -------- 非遗分类（剪纸 / 扎染 / 木雕 / 银饰） --------
INSERT INTO category (category_name, category_desc, sort_order) VALUES
('剪纸', '一把剪刀一张红纸，剪出千年吉祥纹样，国家级非物质文化遗产。', 1),
('扎染', '以线结之，而后染色；染后解结，花纹天成，白族千年手艺。', 2),
('木雕', '以刀代笔，以木为纸，东阳木雕与徽州木雕皆源于此。', 3),
('银饰', '苗族银饰锻制技艺，一锤一錾皆手作，盛装之时满身银光。', 4);

-- -------- 首页轮播（2 条，图片放在 webapp/upload/demo 下，保证不裂图） --------
INSERT INTO banner (title, image_url, link_url, sort_order, status) VALUES
('匠心传承 · 非遗文创年礼季', '/upload/demo/banner1.png', '/product_list.action', 1, 1),
('一件文创 · 一段千年技艺',   '/upload/demo/banner2.png', '/product_list.action', 2, 1);

-- -------- 文创商品（12 件：每个分类 3 件） --------
-- 剪纸类
INSERT INTO product (category_id, product_name, product_desc, main_image, detail_images, price, stock, sales, status, is_hot, is_new) VALUES
(1, '非遗剪纸·福字窗花套装', '手工剪制的福字窗花，采用传统折叠剪法，一套六张，红纸为安徽泾县宣纸，可贴窗可装裱。',
 '/upload/demo/p1.png', '/upload/demo/d1.png,/upload/demo/d2.png', 39.00, 200, 128, 1, 1, 0),
(1, '非遗剪纸·十二生肖书签', '十二生肖造型的剪纸书签，过塑防水处理，双面可见，随书附赠非遗技艺介绍卡。',
 '/upload/demo/p2.png', '/upload/demo/d1.png,/upload/demo/d3.png', 25.00, 300, 86, 1, 0, 1),
(1, '非遗剪纸·牡丹团花摆件', '牡丹团花剪纸装裱于实木圆框中，可作桌面摆件或墙面挂饰，礼盒包装。',
 '/upload/demo/p3.png', '/upload/demo/d2.png,/upload/demo/d3.png', 128.00, 60, 34, 1, 1, 0);

-- 扎染类
INSERT INTO product (category_id, product_name, product_desc, main_image, detail_images, price, stock, sales, status, is_hot, is_new) VALUES
(2, '白族扎染·纯棉方巾', '大理周城白族扎染，板蓝根植物染色，一巾一花纹，绝无重复。',
 '/upload/demo/p4.png', '/upload/demo/d1.png,/upload/demo/d2.png', 68.00, 150, 210, 1, 1, 1),
(2, '非遗扎染·亚麻桌布', '手工扎染亚麻桌布，尺寸 140×140cm，植物染，色牢度高，可机洗。',
 '/upload/demo/p5.png', '/upload/demo/d2.png,/upload/demo/d3.png', 158.00, 80, 46, 1, 0, 0),
(2, '非遗扎染·渐变帆布袋', '扎染渐变帆布单肩包，内衬加厚，可装 14 寸笔记本，每一只花纹都独一无二。',
 '/upload/demo/p6.png', '/upload/demo/d1.png,/upload/demo/d3.png', 89.00, 120, 175, 1, 1, 1);

-- 木雕类
INSERT INTO product (category_id, product_name, product_desc, main_image, detail_images, price, stock, sales, status, is_hot, is_new) VALUES
(3, '东阳木雕·松鹤延年挂屏', '东阳木雕浅浮雕工艺，樟木材质，松鹤延年寓意长寿，附收藏证书。',
 '/upload/demo/p7.png', '/upload/demo/d2.png,/upload/demo/d3.png', 468.00, 25, 18, 1, 1, 0),
(3, '非遗木雕·黄杨木书签', '黄杨木手工雕刻书签，线条细腻，木质温润，配流苏与锦盒。',
 '/upload/demo/p8.png', '/upload/demo/d1.png,/upload/demo/d2.png', 58.00, 260, 132, 1, 0, 1),
(3, '非遗木雕·檀木镇纸', '檀木雕花镇纸一对，压纸不伤纸，底部加绒防滑，书房文房雅器。',
 '/upload/demo/p9.png', '/upload/demo/d3.png,/upload/demo/d1.png', 168.00, 70, 52, 1, 0, 0);

-- 银饰类
INSERT INTO product (category_id, product_name, product_desc, main_image, detail_images, price, stock, sales, status, is_hot, is_new) VALUES
(4, '苗族银饰·錾刻花纹手镯', '苗族银饰锻制技艺，足银 999，手工錾刻蝴蝶妈妈纹样，开口可微调。',
 '/upload/demo/p10.png', '/upload/demo/d1.png,/upload/demo/d2.png', 699.00, 30, 41, 1, 1, 1),
(4, '非遗银饰·蝴蝶妈妈胸针', '蝴蝶妈妈是苗族创世图腾，纯银掐丝工艺，可作胸针或帽饰。',
 '/upload/demo/p11.png', '/upload/demo/d2.png,/upload/demo/d3.png', 259.00, 90, 76, 1, 0, 0),
(4, '非遗银饰·手工银锁项链', '传统长命锁造型，纯银打造，可刻字，周岁礼与生日礼首选。',
 '/upload/demo/p12.png', '/upload/demo/d3.png,/upload/demo/d1.png', 529.00, 45, 29, 1, 0, 1);

-- -------- 测试用户的收货地址（两条，第一条为默认） --------
INSERT INTO address (user_id, receiver, phone, address_detail, is_default) VALUES
(2, '张小匠', '13900000001', '江苏省南京市鼓楼区中山北路 200 号 3 栋 501', 1),
(2, '张小匠', '13900000002', '江苏省南京市江宁区将军大道 66 号 匠集文创园 A 座', 0);

-- -------- 测试用户的购物车（2 件）与收藏（2 件） --------
INSERT INTO cart (user_id, product_id, quantity, checked) VALUES
(2, 1, 2, 1),
(2, 6, 1, 1);

INSERT INTO collect (user_id, product_id) VALUES
(2, 3),
(2, 10);

-- =====================================================================
-- 初始化完成
-- =====================================================================
SELECT '匠集数据库初始化完成：9 张表 + 预置数据' AS result;
