package cn.cyberict.ncha.business.commons.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TokenUtils {
    public static Map<String, Object> getRedisInfoFromRequest(StringRedisTemplate stringRedisTemplate, String token) {
        String[] strArr = token.split(" ");
        return getRedisInfo(stringRedisTemplate, strArr[1]);
    }

    public static Map<String, Object> getRedisInfo(StringRedisTemplate stringRedisTemplate, String token) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String s = operations.get(token);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(s);
        Map map = jsonObject;
        return map;
    }

    public synchronized static String getNetworkNum(StringRedisTemplate stringRedisTemplate) {
        return getNetworkNum(stringRedisTemplate, "networkNum", "", 6, "");
    }

    public synchronized static String getNetworkNum(StringRedisTemplate stringRedisTemplate, String type, String midString) {
        return getNetworkNum(stringRedisTemplate, type, "", 6, midString);
    }

    public synchronized static String getCrioNetworkNum(StringRedisTemplate stringRedisTemplate, String instCode) {
        return getNetworkNum(stringRedisTemplate, "crioNetworkNum", instCode, 8, "");
    }

    public static String getNetworkNum(StringRedisTemplate stringRedisTemplate, String key, String before, Integer dateLength, String midString) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String networkNumAhead = df.format(new Date()).substring(8 - dateLength, 8);
        StringBuffer sb = new StringBuffer(before).append(networkNumAhead).append(StringUtils.isEmpty(midString) ? "" : midString);
        long currentNum = operations.increment(key + sb.toString());
        sb.append(String.format("%03d", currentNum));
        return sb.toString();
    }

    
    public static String getCustomNetworkNum(StringRedisTemplate stringRedisTemplate, String key, String prefix, String between, String suffix, String serialNumberFormat) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        StringBuffer sb = new StringBuffer(prefix).append(between).append(suffix);
        long currentNum = operations.increment(key + sb.toString());
        sb.append(String.format(serialNumberFormat, currentNum));
        return sb.toString();
    }
}
