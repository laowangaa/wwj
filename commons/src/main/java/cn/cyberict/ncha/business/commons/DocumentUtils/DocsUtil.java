package cn.cyberict.ncha.business.commons.DocumentUtils;

import com.aspose.words.SaveFormat;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class DocsUtil {
    


    private Logger log = LoggerFactory.getLogger(DocsUtil.class);
    private final static String tempFile = "temp.docx";

    private String uploadFolder = "/Users/gaomingyaoxuan/files/";
    private String applyModelFolder = "model/";

    private void setNewRed(String docId, String sourceDocPath, String documentType, String outPutPath, String redModelPath) throws Exception {
        log.warn("进入套红方法");
        Map<String, String> map = new HashMap<>();
        String t = null;
        String n = null;
        String g = null;
        String j = null;
        String s = null;
        String d = null;
        String c = null;
        String z = null;
        String r = null;
        String copy = null; 









			/*String numBeg =  apprImportanceDocumentHead.getRefNumber() == null ? "" : apprImportanceDocumentHead.getRefNumber();
			String numMid = apprImportanceDocumentHead.getRefNumberMid() == null ? "" : apprImportanceDocumentHead.getRefNumberMid();
			String numEnd = apprImportanceDocumentHead.getRefNumberEnd() == null ? "" : apprImportanceDocumentHead.getRefNumberEnd();*/


























        
        if (copy != null && !copy.equals("")) {
            redModelPath = uploadFolder + applyModelFolder + File.separator + "redModelCopy.docx";
            map.put("copy", copy);
        }

        map.put("outPutPath", outPutPath);
        map.put("modelDocPath", redModelPath);
        map.put("targetSign", "m");
        map.put("t", t = "");
        map.put("n", n);
        map.put("g", g);
        map.put("j", j);
        map.put("s", s);
        map.put("d", d);
        map.put("c", c);
        map.put("z", z);
        map.put("r", r);
        com.aspose.words.Document document = AsposeWordsUtil.DocumentBuilder(sourceDocPath);
        com.aspose.words.Document document1 = document.deepClone();
        document1.acceptAllRevisions();
        document.save(sourceDocPath, SaveFormat.DOCX);
        document1.save(sourceDocPath + tempFile, SaveFormat.DOCX);
        map.put("sourceDocPath", sourceDocPath + tempFile);
        extractText(map);
    }

    private String extractText(Map<String, String> docMap) throws Exception {
        if (docMap == null || docMap.size() <= 0) {
            return "参数不能为空";
        }
        String targetSign = docMap.get("targetSign");
        String sourceDocPath = docMap.get("sourceDocPath");
        String outPutPath = docMap.get("outPutPath");
        String modelDocPath = docMap.get("modelDocPath");

        if (targetSign == null || targetSign.equals("")) {
            return "正文标记位置不能为空";
        }
        if (sourceDocPath == null || sourceDocPath.equals("")) {
            return "正文文档路径不能为空";
        }
        if (modelDocPath == null || modelDocPath.equals("")) {
            return "模板路径不能为空";
        }
        if (outPutPath == null || outPutPath.equals("")) {
            return "输出路径不能为空";
        }
        
        
        

        File sourceFile = new File(sourceDocPath);

        File modelFile = new File(modelDocPath);

        if (!sourceFile.isFile()) {
            return "正文路径错误，该路径不是一个文件";
        }
        if (!modelFile.isFile()) {
            return "模板路径错误，该路径不是一个文件";
        }
        if (!(modelDocPath.endsWith("docx") || modelDocPath.endsWith("docx"))) {
            return "模板路径的文件不是一个docx文档";
        }
        if (!(sourceDocPath.endsWith("docx") || sourceDocPath.endsWith("doc"))) {
            return "正文路径的文件不是一个docx文档";
        }
        if (!outPutPath.endsWith("docx")) {
            return "输出文档路径不是一个docx文档";
        }

        XWPFDocument modelDocx = new XWPFDocument(new FileInputStream(modelFile));
        com.aspose.words.Document document = AsposeWordsUtil.DocumentBuilder(new FileInputStream(sourceDocPath));
        XWPFDocument mainDocument = new XWPFDocument(new FileInputStream(sourceFile));


        log.warn("outPutPath ====================>" + outPutPath);
        modelDocx.write(new FileOutputStream(outPutPath));
        modelDocx.close();
        return "成功";
    }
}
