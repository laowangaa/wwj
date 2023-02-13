package cn.cyberict.ncha.business.commons.utils;

import com.github.pagehelper.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageUtil {

    
    public static String httpImageToBase(String path) {
        if (StringUtil.isEmpty(path)) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (path.endsWith("jpg")) {
            stringBuffer.append("data:image/jpg;base64,");
        } else if (path.endsWith("png")) {
            stringBuffer.append("data:image/png;base64,");
        } else if (path.endsWith("jepg")) {
            stringBuffer.append("data:image/jepg;base64,");
        } else if (path.endsWith("gif")) {
            stringBuffer.append("data:image/gif;base64,");
        }
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        byte[] buffer = null;
        try {
            
            URL url = new URL(path);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            inputStream = conn.getInputStream();
            outputStream = new ByteArrayOutputStream();
            
            buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            buffer = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        String encode = new BASE64Encoder().encode(buffer);
        stringBuffer.append(encode);
        return stringBuffer.toString();
    }


    
    public static String localImageToBase(String path) {
        StringBuffer stringBuffer = new StringBuffer();
        if (path.endsWith("jpg")) {
            stringBuffer.append("data:image/jpg;base64,");
        } else if (path.endsWith("png")) {
            stringBuffer.append("data:image/png;base64,");
        } else if (path.endsWith("jepg")) {
            stringBuffer.append("data:image/jepg;base64,");
        } else if (path.endsWith("gif")) {
            stringBuffer.append("data:image/gif;base64,");
        }
        if (StringUtil.isEmpty(path)) {
            return null;
        }
        
        FileInputStream fis = null;
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(path);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String encode = Base64.encodeBase64String(buffer);
            stringBuffer.append(encode);
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
