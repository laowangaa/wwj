package cn.cyberict.ncha.business.shell.commons.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class PaginationUtils {
    
    public static PageResult initResult(List list, Integer currentPage, Integer pageSize, Integer count){
        Pagination pagination = new Pagination();
        pagination.setCurrentPage(currentPage);
        pagination.setPageSize(pageSize);
        pagination.setTotalNum(count);
        pagination.setTotalPage(0 == pageSize ? 1 : (count - 1) / pageSize + 1);
        PageResult result = new PageResult();
        result.setPagination(pagination);
        result.setData(list);
        return result;
    }

    
    public static void initPaginationParam(Map<String, Object> param, JSONObject jsonObject) {
        JSONObject pagination = jsonObject.getJSONObject("pagination");
        if (pagination == null) {
            pagination = jsonObject.getJSONObject("pageNation");
            if (pagination == null) {
                pagination = new JSONObject();
            }
        }
        JSONObject sort = jsonObject.getObject("sort", JSONObject.class);
        if (sort != null) {
            Set<String> set = sort.keySet();
            for (String key : set) {
                param.put("sort", key);
                if ("1".equals(sort.get(key))) {
                    param.put("dir", "asc");
                } else {
                    param.put("dir", "desc");
                }

            }
        }else {
            param.put("sort",null);
        }
        Object currentPage = pagination.get("currentPage");
        if (currentPage == null) {
            currentPage = pagination.get("currentPageNumber");
        }
        param.put("currentPage", currentPage);
        param.put("pageSize", pagination.get("pageSize"));
    }

    
    public static void initPageQueryParam(Map<String, Object> param, Integer currentPage, Integer pageSize) {
        int top = currentPage * pageSize;
        int minNum = currentPage * pageSize - pageSize;
        param.put("top", top);
        param.put("minNum", minNum);
        param.put("startNum", minNum);
        param.put("pageSize", pageSize);
    }
    public static String initSortByJsonObject(JSONObject jsonObject){
        JSONObject sortObj = jsonObject.getJSONObject("sort");
        return initSortBySortObject(sortObj);
    }
    public static String initSortBySortObject(JSONObject sortObject) {
        StringBuffer orderBy = new StringBuffer();
        Set<Map.Entry<String, Object>> entrySet = sortObject.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            orderBy.setLength(0);
            orderBy.append(entry.getKey());
            orderBy.append(1 == (Integer)entry.getValue() ? " asc" : " desc");
        }
        return orderBy.toString();
    }

}
