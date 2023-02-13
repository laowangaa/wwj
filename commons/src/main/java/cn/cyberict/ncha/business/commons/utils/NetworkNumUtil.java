package cn.cyberict.ncha.business.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class NetworkNumUtil {

    public synchronized static String getNetworkNum(StringRedisTemplate stringRedisTemplate) {
        return getNetworkNum(stringRedisTemplate, "networkNum", "", "", 2, 6);
    }

    public synchronized static String getNetworkNum(StringRedisTemplate stringRedisTemplate, String type, String midString) {
        return getNetworkNum(stringRedisTemplate, type, "", midString, 2, 6);
    }

    public synchronized static String getCrioNetworkNum(StringRedisTemplate stringRedisTemplate, String instCode) {
        return getNetworkNum(stringRedisTemplate, "crioNetworkNum", instCode, "", 0, 8);
    }

    
    public static String getNetworkNum(StringRedisTemplate stringRedisTemplate, String key, String startString, String endString, Integer dateStartIndex, Integer dateEndIndex) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String networkNumAhead = getDateString(dateStartIndex, dateEndIndex);
        StringBuffer sb = new StringBuffer(startString).append(networkNumAhead).append(StringUtils.isEmpty(endString) ? "" : endString);
        long currentNum = operations.increment(key + sb.toString());
        sb.append(String.format("%03d", currentNum));
        return sb.toString();
    }

    
    public static String getDateString(Integer dateStartIndex, Integer dateEndIndex) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String networkNumAhead = df.format(new Date()).substring(dateStartIndex, dateEndIndex);
        return networkNumAhead;
    }


    
    public static String getNetworkNum(StringRedisTemplate stringRedisTemplate, String key) {
        if (!stringRedisTemplate.hasKey(key) && stringRedisTemplate.opsForValue().setIfAbsent(key, "0")) {
            
            Long ttl = Duration.between(LocalTime.now(), LocalTime.MAX).getSeconds();
            stringRedisTemplate.expire(key, ttl, TimeUnit.SECONDS);
        }
        
        Long value = stringRedisTemplate.opsForValue().increment(key, 1);
        String format = String.format("%04d", value);
        return format;
    }
}
