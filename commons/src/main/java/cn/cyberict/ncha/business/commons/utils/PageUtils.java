package cn.cyberict.ncha.business.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PageUtils {
    public static Map jsonToMap(String jsonStr){
        
        Map map = new HashMap();
        JSONObject dataObject = JSON.parseObject(jsonStr);
        String condition = dataObject.getString("condition");
        String pagination = dataObject.getString("pagination");
        String sort = dataObject.getString("sort");

        JSONObject conditionObject = JSON.parseObject(condition);
        JSONObject paginationObject = JSON.parseObject(pagination);
        JSONObject sortObject = JSON.parseObject(sort);
        if (!sortObject.containsKey("dir")) {
            sortObject.put("dir", "");
        }
        if (sortObject.get("dir").equals("ascending")||sortObject.get("dir").equals("asc")) {
            sortObject.put("dir", "asc");
        } else {
            sortObject.put("dir", "desc");
        }

        map.putAll(conditionObject);
        map.putAll(paginationObject);
        map.putAll(sortObject);

        return map;
    }
}
