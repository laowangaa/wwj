package cn.cyberict.ncha.business.commons.utils;

import java.util.HashMap;
import java.util.Map;


public class UserToMapUtil {

    public static Map getUserInfo(UserHelper userHelper) {
        Map map = new HashMap();
        map.put("id", userHelper.getId());
        map.put("name", userHelper.getName());
        map.put("departmentId", userHelper.getDepartmentId());
        map.put("departmentName", userHelper.getDepartmentName());
        map.put("divisionId", userHelper.getDivisionId());
        map.put("divisionName", userHelper.getDivisionName());
        map.put("instType", userHelper.getInstType());
        map.put("instId", userHelper.getInstId());
        map.put("instName", userHelper.getInstName());
        map.put("loginChannel", userHelper.getLoginChannel());
        map.put("password", userHelper.getPassword());
        map.put("telNo", userHelper.getTelNo());
        map.put("username", userHelper.getUsername());
        map.put("userType", userHelper.getUserType());
        map.put("roleType", userHelper.getRoleType());
        map.put("province", userHelper.getProvince());
        return map;
    }

}
