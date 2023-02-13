package cn.cyberict.ncha.business.commons.utils;

public class CharUtil {
    public static final char UNDERLINE = '_';

    
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (i != 0 && Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
            }
            sb.append(Character.toLowerCase(c));  
        }
        return sb.toString();
    }

}
