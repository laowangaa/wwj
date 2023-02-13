package cn.cyberict.ncha.business.commons.utils;

public class FieldUtil {

    
    public static String toGetter(String fieldname) {

        if (fieldname == null || fieldname.length() == 0) {
            return null;
        }

        
        if (fieldname.length() > 2) {
            String second = fieldname.substring(1, 2);
            if (second.equals(second.toUpperCase())) {
                return new StringBuffer("get").append(fieldname).toString();
            }
        }

        
        fieldname = new StringBuffer("get").append(fieldname.substring(0, 1).toUpperCase())
                .append(fieldname.substring(1)).toString();

        return  fieldname;
    }

    public static String toSetter(String fieldname) {

        if (fieldname == null || fieldname.length() == 0) {
            return null;
        }

        
        if (fieldname.length() > 2) {
            String second = fieldname.substring(1, 2);
            if (second.equals(second.toUpperCase())) {
                return new StringBuffer("set").append(fieldname).toString();
            }
        }

        
        fieldname = new StringBuffer("set").append(fieldname.substring(0, 1).toUpperCase())
                .append(fieldname.substring(1)).toString();

        return  fieldname;
    }
}
