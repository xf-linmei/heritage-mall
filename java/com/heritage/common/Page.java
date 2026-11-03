package com.heritage.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用分页封装类（泛型）
 *
 * <p>用法：Service 先查总数 count，再查当前页数据 list，
 * 然后把二者塞进 Page，JSP 用 page.list 遍历、用 page.totalPage 渲染页码。</p>
 *
 * <p>SQL 层面使用 MySQL 的 LIMIT #{startIndex}, #{pageSize} 实现物理分页。</p>
 *
 * @param <T> 当前页数据的类型
 */
public class Page<T> {

    /** 当前页码（从 1 开始） */
    private int pageNo = 1;
    /** 每页条数 */
    private int pageSize;
    /** 总记录数 */
    private int totalCount;
    /** 当前页数据 */
    private List<T> list = new ArrayList<T>();

    public Page() {
    }

    public Page(int pageNo, int pageSize) {
        // 页码小于 1 时强制修正为第 1 页，避免 LIMIT 出现负数
        this.pageNo = pageNo < 1 ? 1 : pageNo;
        this.pageSize = pageSize < 1 ? 10 : pageSize;
    }

    /** 总页数 */
    public int getTotalPage() {
        if (pageSize == 0) {
            return 0;
        }
        return (totalCount + pageSize - 1) / pageSize;
    }

    /** SQL 中 LIMIT 的起始下标 */
    public int getStartIndex() {
        return (pageNo - 1) * pageSize;
    }

    /** 是否有上一页 */
    public boolean isHasPrev() {
        return pageNo > 1;
    }

    /** 是否有下一页 */
    public boolean isHasNext() {
        return pageNo < getTotalPage();
    }

    public int getPrev() {
        return isHasPrev() ? pageNo - 1 : 1;
    }

    public int getNext() {
        return isHasNext() ? pageNo + 1 : getTotalPage();
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo < 1 ? 1 : pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? 10 : pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
