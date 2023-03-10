package cn.cyberict.ncha.business.commons.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.UUID;

public abstract class CommonControllerAspect {

    private static final ObjectMapper jsontoperson = new ObjectMapper();

    public static String TOPIC = "system_log";


    
    public abstract void log();

    @Around("log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        String url = request.getRequestURL().toString();
        String uuid = UUID.randomUUID().toString();
        Signature signature = joinPoint.getSignature();

        String header = request.getHeader("Authorization");
        String flag = request.getHeader("flag");
        Object[] objects = joinPoint.getArgs();
        if ("1".equals(flag)) {
            int index = this.getRequestBodyIndex(joinPoint);
            if (index > -1) {
                objects[index] = DesUtil.decrypt(objects[index].toString());
            }
        }
        String reqParam = this.processInputArg(objects);
        Object result = joinPoint.proceed(objects);
        ResultUtil resultUtil = null;
        if (result instanceof ResultUtil) {
            resultUtil = ((ResultUtil) result).clone();
            resultUtil.setUuid(uuid);
            if (this.getEncodeFlag() && "1".equals(flag) || "0".equals(flag)) {
                resultUtil.setData(DesUtil.encrypt(jsontoperson.writeValueAsString(resultUtil.getData())));
                resultUtil.setFlag("1");
            }
        }
        String respParam = postHandle(result);
        this.getLogger().warn("???????????????{}???URL:???{}???,????????????:???{}???,token??????{}???,????????????:???{}?????????????????????{}???", signature.getName(), url, reqParam, header, respParam, uuid);
        this.saveLogs(signature.getName(), url, reqParam, header, respParam, uuid);
        return resultUtil == null ? result : resultUtil;
    }

    
    private Integer getRequestBodyIndex(ProceedingJoinPoint joinPoint) throws Exception {
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        Annotation[][] parameterAnnotations = currentMethod.getParameterAnnotations();
        int requestBodyIndex = 0;
        boolean flag = false;
        for (Annotation[] annotations : parameterAnnotations) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(RequestBody.class)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                break;
            }
            requestBodyIndex++;
        }
        return flag ? requestBodyIndex : -1;
    }

    
    private String postHandle(Object retVal) throws Exception {
        if (null == retVal) {
            return "";
        }
        return jsontoperson.writeValueAsString(retVal);
    }


    
    private String processInputArg(Object[] args) {
        StringBuffer param = new StringBuffer(" ");
        int i = 1;
        for (Object arg : args) {
            param.append("param");
            param.append(i);
            param.append("=   ");
            param.append(arg);
            param.append("    ");
            i++;
        }
        return param.toString();
    }

    public abstract boolean getEncodeFlag();

    public abstract Logger getLogger();

    public void saveLogs(String method, String url, String reqParam, String header, String respParam, String uuid) {

    }
}
