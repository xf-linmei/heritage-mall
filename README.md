# heritage-mall
面向非遗传承人的文创电商商城，赋能传统手工艺品线上展销，包含用户端与管理后台
## 环境要求

| 软件 | 版本要求 | 说明 |
|---|---|---|
| JDK | **8 或 11** | 本项目用 `javax.servlet` API，JDK 17+ 可能导致 JSP 编译失败 |
| Tomcat | **9.x** | ⚠️ 不能用 Tomcat 10/11（它们改为 `jakarta.*`，与 Struts2 2.5 不兼容） |
| MySQL | 5.7 / 8.0 | 需能本地连接 |
| Maven | 3.6+ | 打包用 |

技术栈：Struts2 2.5.33 + Spring 5.3.39 + MyBatis 3.5.13 + Druid + Bootstrap 5，传统 WAR 部署（非 SpringBoot）。

---

## 一、导入数据库

```bash
# 1. 建库（字符集必须是 utf8mb4）
mysql -uroot -p -e "CREATE DATABASE IF NOT EXISTS heritage DEFAULT CHARACTER SET utf8mb4;"

# 2. 导入表结构与预置数据（9 张表 + 12 件商品 + 17 张演示图路径）
mysql -uroot -p --default-character-set=utf8mb4 heritage < src/main/resources/sql/heritage.sql
```

导入成功会输出：`匠集数据库初始化完成：9 张表 + 预置数据`

---

## 二、修改数据库连接

编辑 `src/main/resources/jdbc.properties`，改成你自己的账号密码：

```properties
jdbc.url=jdbc:mysql://localhost:3306/heritage?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
jdbc.username=root
jdbc.password=你的MySQL密码
```

---

## 三、打包

```bash
mvn clean package -DskipTests
```

成功后生成：`target/heritage.war`

---

## 四、部署到 Tomcat 9

### 方式 A：直接丢 war（最简单）
把 `target/heritage.war` 复制到 Tomcat 的 `webapps/` 目录，然后运行 `bin/startup.bat`，Tomcat 会自动解压部署。

### 方式 B：IDEA 部署
1. Run/Debug Configurations → `+` → **Tomcat Server → Local**（选 Tomcat 9，JRE 选 JDK 8/11）；
2. Deployment → `+` → **Artifact** → 选 `heritage:war exploded`；
3. Application context 填 `/heritage`；
4. 启动（若改过代码先 **Build → Rebuild Project**）。

---

## 五、访问

```
前台首页：http://localhost:8080/heritage/
后台管理：http://localhost:8080/heritage/admin/index.action（登录后可见入口）
```

| 角色 | 账号 | 密码 |
|---|---|---|
| 管理员 | `admin` | `123456` |
| 普通用户 | `test` | `123456` |

---

## 六、常见问题

| 现象 | 原因与解决 |
|---|---|
| 部署时报 `ConflictingBeanDefinitionException` | 前后台同名 Action 的 Bean 冲突，本项目已用全限定类名作 Bean 名，若自行新增 Action 请同样显式命名 |
| 页面没样式（Bootstrap 生效但自定义样式失效） | 静态资源目录**不能叫 `static`**（Struts2 将其作为保留前缀拦截，返回 404），本项目已改用 `assets/` |
| 图片不显示 | `webapp/upload/` 需存在且有图片；后台上传的图片也存这里，部署目录需有写权限 |
| `Access denied for user 'root'` | `jdbc.properties` 的账号密码与本机 MySQL 不一致 |
| IDEA 报找不到已删除的测试文件 | 编译缓存残留，**Build → Rebuild Project** |
| 页面空白、显示 `${...}` | `web.xml` 需为 Servlet 4.0 且开启 EL（本项目已配置 `el-ignored=false`） |

---

## 七、功能验收路线

注册新用户 → 浏览/筛选商品 → 详情加购 + 收藏 → 购物车勾选结算 → 下单（库存不足会回滚）→ 模拟支付 → 后台 `admin` 发货 → 前台确认收货 → 后台新增分类/商品（试上传 jpg）→ 删除有商品的分类（应被拒绝）。
