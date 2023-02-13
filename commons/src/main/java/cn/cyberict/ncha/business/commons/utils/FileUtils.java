package cn.cyberict.ncha.business.commons.utils;

import com.github.pagehelper.util.StringUtil;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;





@Slf4j
public class FileUtils {

    
    public static String generatePath(String uploadFolder, Chunk chunk) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder).append("/").append(chunk.getIdentifier());
        
        if (!Files.isWritable(Paths.get(sb.toString()))) {
            Files.createDirectories(Paths.get(sb.toString()));
        }
        Path filePath = Paths.get(sb.append("/").append(chunk.getFilename())
                .append("-")
                .append(chunk.getChunkNumber()).toString());
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
        }
        return filePath.toString();
    }

    
    public static void merge(String targetFile, String folder, String filename) {
        try {
            if (Files.notExists(Paths.get(targetFile))) {
                Files.createFile(Paths.get(targetFile));
            }
            Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void addToMerge(String targetFile, Path path) {
        try {
            if (Files.notExists(Paths.get(targetFile))) {
                Files.createFile(Paths.get(targetFile));
            }
            
            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
            
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
    public static String moveDataFile2Reject(String oldFilePath, String newFilePath) {
        File newFile = new File(newFilePath);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        File file = new File(oldFilePath);
        String fileName = file.getName();
        String filePath = newFilePath + file.getName();
        file.renameTo(new File(filePath));
        return filePath;
    }

    
    public static void copyDir(String oldFilePath, String newFilePath) throws IOException {
        File file = new File(oldFilePath);
        String[] filePath = file.list();

        if (!(new File(newFilePath)).exists()) {
            (new File(newFilePath)).mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldFilePath + file.separator + filePath[i])).isDirectory()) {
                copyDir(oldFilePath + file.separator + filePath[i], newFilePath + file.separator + filePath[i]);
            }

            if (new File(oldFilePath + file.separator + filePath[i]).isFile()) {
                copyFile(oldFilePath + file.separator + filePath[i], newFilePath + file.separator + filePath[i]);
            }

        }
    }

    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[2097152];
        int readByte = 0;
        while ((readByte = in.read(buffer)) != -1) {
            out.write(buffer, 0, readByte);
        }
        in.close();
        out.close();
    }


    
    public static void fileDownLoad(String path, String fileName, HttpServletResponse response, HttpServletRequest request) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        try {
            String userAgent = request.getHeader("User-Agent");
            
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            } else {
                if (!fileName.endsWith("pdf")) {
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                }
            }
            if (fileName.endsWith("pdf")) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "utf-8"));
            } else {
                response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
            }


            response.setCharacterEncoding("UTF-8");
            byte[] bytes = null;
            if (StringUtil.isNotEmpty(path) && path.contains("http")) {
                bytes = HttpUtils.getHttp(path);
            } else {
                bytes = HttpUtils.getLocal(path);
            }

            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                System.out.println("关闭流出现异常");
                e.printStackTrace();
            }
        }
    }

    
    public static File transferToFile(MultipartFile multipartFile) {

        File file = null;
        try {
            String fileName = multipartFile.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            file = File.createTempFile("temp", prefix);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    
    public static MultipartFile fileToMultipartFile(File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = null;





        return multipartFile;
    }

    
    public static File base64ToFile(String base64, String fileName) {
        if (base64 == null || "".equals(base64)) {
            return null;
        }
        base64 = base64.substring(base64.indexOf(",") + 1);
        byte[] buff = Base64.decode(base64);
        File file = new File(fileName);
        FileOutputStream fout = null;
        try {

            fout = new FileOutputStream(file);
            fout.write(buff);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    
    public static String docxToStr(MultipartFile multipartFile){
        String str = "";
        String fileName = multipartFile.getOriginalFilename();
        if (fileName.endsWith(".doc")) {
           try {

               ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(multipartFile.getBytes());
               HWPFDocument doc = new HWPFDocument(byteArrayInputStream);
               str = doc.getDocumentText();
               doc.close();
               byteArrayInputStream.close();
           }
           catch (Exception e ) {
               e.printStackTrace();
           }
        }else if (fileName.endsWith(".docx")) {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(multipartFile.getBytes());
                XWPFDocument xdoc = new XWPFDocument(byteArrayInputStream);
                XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                str = extractor.getText();
                extractor.close();
                byteArrayInputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return str;
    }

}
