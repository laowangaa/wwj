package cn.cyberict.ncha.business.commons.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;
import java.util.Map;


public class UserHelper implements Serializable {


    
    private String id;
    
    private String username;
    
    private String name;
    
    private String password;

    
    private String loginChannel;

    
    private String instType;
    
    private String instId;
    
    private String instName;

    
    private String divisionId;
    
    private String divisionName;
    
    private String departmentId;
    
    private String departmentName;

    
    private String telNo;
    
    private String userType;

    
    private String roleType;


    
    private String province;

    
    private String instProvince;
    
    private JSONObject func;

    
    public UserHelper(String token, RedisTemplate redisTemplate) {
        this.getUserInfo(token, redisTemplate);

    }

    public UserHelper() {

    }

    
    public void getUserInfo(String token, RedisTemplate redisTemplate) {
        Object sToken = null;
        DataType type = redisTemplate.type(token);

        if ("string".equals(type.code())) {
            
            sToken = redisTemplate.opsForValue().get(token);
            this.id = (String) sToken;
            this.username = (String) sToken;
            this.name = (String) sToken;
        }
        if ("hash".equals(type.code())) {
            sToken = redisTemplate.opsForHash().entries(token);
            Map<String, Object> map = (Map) sToken;
            this.id = map.get("userId") != null ? ((String) map.get("userId")).replaceAll("\"", "") : null;
            this.username = map.get("username") != null ? ((String) map.get("username")).replaceAll("\"", "") : null;
            this.name = map.get("name") != null ? ((String) map.get("name")).replaceAll("\"", "") : null;
            this.instId = map.get("instId") != null ? ((String) map.get("instId")).replaceAll("\"", "") : null;
            this.instName = map.get("instName") != null ? ((String) map.get("instName")).replaceAll("\"", "") : null;
            this.telNo = map.get("telNo") != null ? ((String) map.get("telNo")).replaceAll("\"", "") : null;
            this.userType = map.get("userType") != null ? ((String) map.get("userType")).replaceAll("\"", "") : null;
            try {
                this.instProvince = map.get("instProvince") != null ? ((String) map.get("instProvince")).replaceAll("\"", "") : null ;
            } catch (NullPointerException e) {
                this.instProvince = "";
            }
            this.divisionId = map.get("divisionId") != null ? ((String) map.get("divisionId")).replaceAll("\"", "") : null;
            this.divisionName = map.get("divisionName") != null ? ((String) map.get("divisionName")).replaceAll("\"", "") : null;
            this.departmentId = map.get("departmentId") != null ? ((String) map.get("departmentId")).replaceAll("\"", "") : null;
            this.departmentName = map.get("departmentName") != null ? ((String) map.get("departmentName")).replaceAll("\"", "") : null;

            JSONObject jsonObject;
            if (!(map.get("func") instanceof java.util.HashMap)) {
                String funcStr = (String) map.get("func");
                jsonObject = JSONObject.parseObject(funcStr);
                this.func = jsonObject;
            } else {
                this.func = new JSONObject();
            }

        }


    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLoginChannel() {
        return loginChannel;
    }

    public void setLoginChannel(String loginChannel) {
        this.loginChannel = loginChannel;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getInstProvince() {
        return instProvince;
    }

    public void setInstProvince(String instProvince) {
        this.instProvince = instProvince;
    }

    public JSONObject getFunc() {
        return func;
    }

    public void setFunc(JSONObject func) {
        this.func = func;
    }
}
