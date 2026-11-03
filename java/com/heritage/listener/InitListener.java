package com.heritage.listener;

import com.heritage.common.Constant;
import com.heritage.entity.Category;
import com.heritage.service.CategoryService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * 系统初始化监听器
 *
 * <p>作用：Web 应用启动时把"非遗分类列表"查出来放入 application 域，
 * 全站导航（前台页头、后台页头）都能直接用 ${applicationScope.categoryList} 读取，
 * 不必在每个 Action 里重复查询数据库。</p>
 *
 * <p>顺序：web.xml 中它排在 ContextLoaderListener 之后，
 * 所以能通过 WebApplicationContextUtils 直接拿到 Spring 容器里的 Service。</p>
 */
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            WebApplicationContext context =
                    WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
            if (context != null) {
                CategoryService categoryService = context.getBean("categoryService", CategoryService.class);
                List<Category> categoryList = categoryService.listAll();
                sce.getServletContext().setAttribute(Constant.APP_CATEGORY_LIST, categoryList);
                System.out.println("【匠集】系统初始化完成，已加载非遗分类 " + categoryList.size() + " 条");
            }
        } catch (Exception e) {
            // 数据库连不上时也要让应用启动成功，页面报错信息会更清晰
            System.err.println("【匠集】初始化分类数据失败：" + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 无需处理
    }
}
