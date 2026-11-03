package com.heritage.service.impl;

import com.heritage.common.Constant;
import com.heritage.common.Page;
import com.heritage.dao.ProductDao;
import com.heritage.entity.Product;
import com.heritage.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 文创商品业务实现类
 *
 * <p>分页逻辑封装在这里（Page 对象），Action 和 JSP 只管用，不关心 LIMIT 怎么算。</p>
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getById(Integer productId) {
        return productDao.selectById(productId);
    }

    @Override
    public Page<Product> listByCondition(Integer categoryId, String keyword, BigDecimal minPrice,
                                         BigDecimal maxPrice, Integer status, String sort,
                                         int pageNo, int pageSize) {
        Page<Product> page = new Page<Product>(pageNo, pageSize);
        // 先查总记录数
        int totalCount = productDao.countByCondition(categoryId, keyword, minPrice, maxPrice, status);
        page.setTotalCount(totalCount);
        // 再查当前页数据（没有记录时不必再查一次数据库）
        if (totalCount > 0) {
            List<Product> list = productDao.selectByCondition(categoryId, keyword, minPrice, maxPrice,
                    status, sort, page.getStartIndex(), page.getPageSize());
            page.setList(list);
        }
        return page;
    }

    @Override
    public List<Product> listHot(int limit) {
        return productDao.selectHot(limit);
    }

    @Override
    public List<Product> listNew(int limit) {
        return productDao.selectNew(limit);
    }

    @Override
    public int add(Product product) {
        if (product.getStock() == null) {
            product.setStock(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(Constant.STATUS_ON);
        }
        if (product.getIsHot() == null) {
            product.setIsHot(0);
        }
        if (product.getIsNew() == null) {
            product.setIsNew(0);
        }
        return productDao.insert(product);
    }

    @Override
    public int update(Product product) {
        return productDao.update(product);
    }

    @Override
    public int delete(Integer productId) {
        return productDao.delete(productId);
    }

    @Override
    public int updateStatus(Integer productId, Integer status) {
        return productDao.updateStatus(productId, status);
    }
}
