package cn.cyberict.ncha.business.commons.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class ListTreeUtil {
    
    public JSONArray listToTree(JSONArray arr, String id, String pid, String submenu) {
        JSONArray r = new JSONArray();
        JSONObject hash = new JSONObject();
        
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = (JSONObject) arr.get(i);
            hash.put(json.getString(id), json);
        }
        
        for (int j = 0; j < arr.size(); j++) {
            
            JSONObject aVal = (JSONObject) arr.get(j);
            
            JSONObject hashVP = (JSONObject) hash.get(aVal.get(pid));
            Map map = new HashMap();
            
            if (hashVP != null) {
                
                if (hashVP.get(submenu) != null) {
                    JSONArray ch = (JSONArray) hashVP.get(submenu);
                    map.put("attr", aVal.getString("attr"));
                    map.put("type", aVal.getString("type1"));
                    aVal.put("handle", map);
                    ch.add(aVal);
                    hashVP.put(submenu, ch);
                } else {
                    JSONArray ch = new JSONArray();
                    map.put("attr", aVal.getString("attr"));
                    map.put("type", aVal.getString("type1"));
                    aVal.put("handle", map);
                    ch.add(aVal);
                    hashVP.put(submenu, ch);
                }
            } else {
                map.put("attr", aVal.getString("attr"));
                map.put("type", aVal.getString("type1"));
                aVal.put("handle", map);

                r.add(aVal);
            }
        }
        return r;
    }
}
