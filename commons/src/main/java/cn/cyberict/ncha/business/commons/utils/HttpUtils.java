package cn.cyberict.ncha.business.commons.utils;


import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpUtils {


    
    public static byte[] getHttp(String path) throws IOException {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = p.matcher(path);
        if (matcher.find()) {
            String fileUrl = path.substring(0, path.lastIndexOf("."));
            String str[] = fileUrl.split("/");
            for (int i = 0; i < str.length; i++) {
                matcher = p.matcher(str[i]);
                if (matcher.find()) {
                    path = path.replaceFirst(str[i], URLEncoder.encode(str[i], "UTF-8"));
                }
            }
        }
        URL url = new URL(path);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.connect();
        InputStream inputStream = httpConn.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int b;
        while ((b = inputStream.read(bytes)) != -1) {
            byteArrayOutputStream.write(bytes, 0, b);
        }
        inputStream.close();
        httpConn.disconnect();
        return byteArrayOutputStream.toByteArray();
    }


    
    public static byte[] getLocal(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        InputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int b;
        while ((b = inputStream.read(bytes)) != -1) {
            byteArrayOutputStream.write(bytes, 0, b);
        }
        inputStream.close();
        return byteArrayOutputStream.toByteArray();
    }


    
    public static String postHttp(String stringUrl, Map<String, String> param) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        URL url = new URL(stringUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setUseCaches(false);
        httpConn.setConnectTimeout(30000);
        httpConn.setReadTimeout(30000);
        
        if (param.size() > 0) {
            StringBuilder paramBuilder = new StringBuilder();
            DataOutputStream outputStream = new DataOutputStream(httpConn.getOutputStream());
            for (Map.Entry<String, String> entry : param.entrySet()) {
                String value = entry.getValue();
                String key = entry.getKey();
                paramBuilder.append("&");
                paramBuilder.append(key);
                paramBuilder.append("=");
                paramBuilder.append(value);
            }
            outputStream.writeBytes(paramBuilder.substring(1));
            outputStream.flush();
            outputStream.close();
        }
        InputStream inputStream = httpConn.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        
        bufferedReader.close();
        httpConn.disconnect();
        return stringBuilder.toString();
    }
}
