package cn.cyberict.ncha.business.commons.DocumentUtils;

import com.aspose.words.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Component
public class AsposeWordsUtil {


    public void saveWordToPdf(String sourceFilePath, String targetFilePath) throws Exception {
        Optional.ofNullable(sourceFilePath).filter(s -> !s.equals("")).orElseThrow(() -> new NullPointerException("原文件地址不能为空"));
        Optional.ofNullable(targetFilePath).filter(s -> !s.equals("")).orElseThrow(() -> new NullPointerException("目标文件路径不能为空"));
        Document document = this.DocumentBuilder(sourceFilePath);
        FileOutputStream fileOutputStream = new FileOutputStream(targetFilePath);
        this.saveWordToPdf(document, fileOutputStream);
    }

    public ByteArrayOutputStream saveWordToPdfByteArray(String sourceFilePath) throws Exception {
        Optional.ofNullable(sourceFilePath).filter(s -> !s.equals("")).orElseThrow(() -> new NullPointerException("原文件地址不能为空"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = this.DocumentBuilder(sourceFilePath);
        this.saveWordToPdf(document, byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    public static void saveWordToPdf(Document document, OutputStream outputStream) throws Exception {
        Optional.ofNullable(document).orElseThrow(() -> new NullPointerException("document对象不能为空"));
        Optional.ofNullable(outputStream).orElseThrow(() -> new NullPointerException("输出流不能为空"));
        document.save(outputStream, SaveFormat.PDF);
    }

    public static void replaceElementsForWord(Document document, Map<String, String> elementMap) throws Exception {
        Optional.ofNullable(document).orElseThrow(() -> new NullPointerException("document对象不能为空"));
        Optional.ofNullable(elementMap).orElseThrow(() -> new NullPointerException("elementsMap不能为空"));
        for (Map.Entry<String, String> entry : elementMap.entrySet()) {

            if (entry.getValue() == null) {
                entry.setValue(" ");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            FindReplaceOptions options = new FindReplaceOptions();
            options.setMatchCase(false);
            options.setFindWholeWordsOnly(false);
            document.getRange().replace(key, value, options);
        }
    }

    public static Document DocumentBuilder(String documentFilePath) throws Exception {
        if (documentFilePath != null && !documentFilePath.equals("")) {
            return DocumentBuilder(new FileInputStream(documentFilePath));
        } else {
            FileInputStream fileInputStream = null;
            return DocumentBuilder(fileInputStream);
        }
    }

    
    public static void appendDocForDocx(String outPutPath, List<String> targetPath) throws Exception {
        if (targetPath == null || targetPath.size() == 0) {
            throw new NullPointerException("文档路径不能为空");
        }
        if (outPutPath == null || outPutPath.equals("")) {
            throw new NullPointerException("输出路径不能为空");
        }
        appendDocForDocx(new FileOutputStream(outPutPath), targetPath);
    }


    
    public static void appendDocForDocx(OutputStream outPutPath, List<String> targetPath) throws Exception {
        if (targetPath == null || targetPath.size() == 0) {
            throw new NullPointerException("文档路径不能为空");
        }
        if (outPutPath == null) {
            throw new NullPointerException("输出路径不能为空");
        }
        Document outDoc = DocumentBuilder("");
        outDoc.removeAllChildren();
        for (String s : targetPath) {
            if (!Files.exists(Paths.get(s))) {
                continue;
            }
            Document document = DocumentBuilder(s);
            outDoc.appendDocument(document, ImportFormatMode.KEEP_SOURCE_FORMATTING);
        }
        outDoc.save(outPutPath, SaveFormat.DOCX);
    }

    
    public static void appendDocForDocx(String outPutPath, Set<Document> targetPath) throws Exception {
        if (targetPath == null || targetPath.size() == 0) {
            throw new NullPointerException("文档路径不能为空");
        }
        if (outPutPath == null) {
            throw new NullPointerException("输出路径不能为空");
        }
        Document outDoc = DocumentBuilder("");
        outDoc.removeAllChildren();
        for (Document document : targetPath) {
            outDoc.appendDocument(document, ImportFormatMode.KEEP_SOURCE_FORMATTING);
        }
        outDoc.save(outPutPath, SaveFormat.DOCX);
    }

    
    public static void appendDocForPDF(String outPutPath, List<String> targetPath) throws Exception {
        if (targetPath == null || targetPath.size() == 0) {
            throw new NullPointerException("文档路径不能为空");
        }
        if (outPutPath == null || outPutPath.equals("")) {
            throw new NullPointerException("输出路径不能为空");
        }
        appendDocForPDF(new FileOutputStream(outPutPath), targetPath);
    }

    
    public static void appendDocForPDF(OutputStream outPutPath, List<String> targetPath) throws Exception {
        if (targetPath == null || targetPath.size() == 0) {
            throw new NullPointerException("文档路径不能为空");
        }
        if (outPutPath == null) {
            throw new NullPointerException("输出路径不能为空");
        }
        Document outDoc = DocumentBuilder("");
        outDoc.removeAllChildren();
        for (String s : targetPath) {
            if (!Files.exists(Paths.get(s))) {
                continue;
            }
            Document document = DocumentBuilder(s);
            outDoc.appendDocument(document, ImportFormatMode.KEEP_SOURCE_FORMATTING);
        }
        outDoc.save(outPutPath, SaveFormat.PDF);
    }

    
    public static void replaceElementByBookMark(Document document, Map<String, Object> elementMap) throws Exception {
        DocumentBuilder documentBuilder = new DocumentBuilder(document);
        BookmarkCollection bookmarks = document.getRange().getBookmarks();
        for (Map.Entry<String, Object> entry : elementMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Bookmark bookmark = bookmarks.get(key);
            if (bookmark == null) {
                continue;
            }
            String bookMarkName = bookmark.getName();
            if (!documentBuilder.moveToBookmark(bookMarkName)) {
                continue;
            }
            if (value instanceof String) {
                documentBuilder.write((String) value);
            }
            if (value instanceof byte[]) {
                documentBuilder.insertImage((byte[]) value);
            }
            if (value instanceof InputStream) {
                documentBuilder.insertImage((InputStream) value);
            }
        }
    }


    public static Document saveWordToHtml(String path, Document document) throws Exception {
        if (path == null || path.equals("")) {
            return null;
        }
        if (document == null) {
            return null;
        }
        document.save(path, SaveFormat.HTML);
        return document;
    }


    public static Document DocumentBuilder(InputStream inputStream) throws Exception {
        InputStream is = Document.class.getResourceAsStream("/com.aspose.words.lic_2999.xml");
        License aposeLic = new License();
        aposeLic.setLicense(is);
        is.close();
        if (inputStream != null) {
            Document document = new Document(inputStream);
            inputStream.close();
            return document;
        } else {
            Document document = new Document();
            return document;
        }
    }

    public static Document saveWordToHtml(OutputStream outputStream, Document document) throws Exception {
        if (outputStream == null) {
            return null;
        }
        if (document == null) {
            return null;
        }



        document.save(outputStream, SaveFormat.HTML);

        return document;

    }
}
