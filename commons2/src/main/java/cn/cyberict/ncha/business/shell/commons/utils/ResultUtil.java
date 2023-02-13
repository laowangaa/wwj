package cn.cyberict.ncha.business.shell.commons.utils;


public class ResultUtil<T> implements Cloneable {

    public ResultUtil(Integer code, String msg, T data, String flag) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.flag = flag;
    }


    public ResultUtil(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    
    public static final Integer SUCCESS_STATE = 200;
    
    public static final Integer SUCCESS_ERROR = 201;
    
    public static final Integer PARAMETE_ERROR = 400;
    
    public static final Integer NO_PERMISSION = 401;
    
    public static final Integer NO_TOKEN = 403;
    
    public static final Integer RESOURCE_NOTEXIST = 404;
    
    public static final Integer UNKNOWN_ERROR = 500;
    
    public static final Integer SERVICE_STOP = 503;
    
    public static final Integer VERIFICATIONCODE_ERROR = 504;
    
    public static final Integer PASSWORD_ERROR = 402;
    
    private Integer code;
    
    private String msg;
    
    private T data;

    private String uuid;

    
    private String flag = "0";

    public static <T> ResultUtil<T> success(T data) {
        return new ResultUtil<>(SUCCESS_STATE, "请求成功", data);
    }

    public static ResultUtil success() {
        return new ResultUtil<>(SUCCESS_STATE, "请求成功", null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultUtil(Integer code) {
        this.code = code;
    }

    public ResultUtil() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public ResultUtil clone() {
        ResultUtil resultUtil = null;
        try {
            resultUtil = (ResultUtil) super.clone();
        } catch (CloneNotSupportedException e) {
            resultUtil = new ResultUtil();
        } finally {
            return resultUtil;
        }
    }
}
