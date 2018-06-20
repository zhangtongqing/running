package com.peipao.framework.page;


import com.github.pagehelper.PageInfo;
import com.peipao.framework.constant.WebConstants;

/**
 * 方法名称：MyPageInfo
 * 功能描述：分页封装实体
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/10/20 9:57
 * 修订记录：
 */
public class MyPageInfo {
    private int pageindex;
    private int pagesize;
    private int maxpage;
    private int hasNext;
    private Long total;
    private Object data;

    public MyPageInfo(PageInfo p) {
        this.pageindex = p.getPageNum();
        this.pagesize = p.getPageSize();
        this.maxpage = p.getPages();
        this.total = p.getTotal();
        if(p.isHasNextPage() == true) {
            this.hasNext = WebConstants.Boolean.TRUE.ordinal();
        } else {
            this.hasNext = WebConstants.Boolean.FALSE.ordinal();
        }
        this.data = p.getList();
    }

    public int getPageindex() {
        return pageindex;
    }

    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getMaxpage() {
        return maxpage;
    }

    public void setMaxpage(int maxpage) {
        this.maxpage = maxpage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public int getHasNext() {
        return hasNext;
    }

    public void setHasNext(int hasNext) {
        this.hasNext = hasNext;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
