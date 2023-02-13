package cn.cyberict.ncha.business.commons.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;


public class QueryParams {

    private JSONObject condition;

    private Pagination pagination;

    private Sort sort;

    public JSONObject getCondition() {
        return condition;
    }

    public void setCondition(JSONObject condition) {
        this.condition = condition;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public String getSortText() {
        if (this.sort == null) {
            return "";
        } else {
            String column = this.sort.getColumn();
            String dir = this.sort.getDir();
            if (StringUtils.isEmpty(column)) {
                return "";
            } else {
                if (StringUtils.isEmpty(dir)) {
                    return column;
                } else {
                    return column + " " + dir;
                }
            }
        }

    }
}
