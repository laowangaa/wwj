package cn.cyberict.ncha.business.shell.commons.utils;


import org.springframework.data.domain.Page;

import java.util.List;

public class PageResult<T> {
    Pagination pagination;
    List<T> data;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public PageResult() {

    }

    public PageResult(Page page) {
        this.pagination.setCurrentPage(page.getNumber());
        this.pagination.setPageSize(page.getTotalPages());
        this.pagination.setTotalNum((int) page.getTotalElements());
        this.data = page.getContent();
    }
}
