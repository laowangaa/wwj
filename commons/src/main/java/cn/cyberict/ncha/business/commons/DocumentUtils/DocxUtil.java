package cn.cyberict.ncha.business.commons.DocumentUtils;

import com.aspose.words.SaveFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocxUtil {


    public static void formatDocument(Map info, Map content) throws Exception {
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
        t = (String) info.get("dispatchTitle");

        n = info.get("dispatchNumTypeName") + "〔" + StringUtils.trim(info.get("dispatchNumMidd")+"") + "〕" + info.get("dispatchNumBack")+"号";

        g = (String) info.get("disclosureModeName");

        j = (String) info.get("majorSend");





        r = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));






        map.put("outPutPath", (String) content.get("output"));
        map.put("modelDocPath", (String) content.get("model"));
        map.put("targetSign", "m");
        map.put("t", t);
        map.put("n", n);
        map.put("g", g);
        map.put("j", j);
        map.put("s", s);
        map.put("d", d);
        map.put("c", c);
        map.put("z", z);
        map.put("r", r);
        com.aspose.words.Document document = AsposeWordsUtil.DocumentBuilder((String) content.get("filePath"));
        com.aspose.words.Document document1 = document.deepClone();
        document1.acceptAllRevisions();
        document.save((String) content.get("filePath"), SaveFormat.DOCX);
        document1.save((String) content.get("output"), SaveFormat.DOCX);
        map.put("sourceDocPath", (String) content.get("output"));
        System.out.println(extractText(map));
    }

    private static String extractText(Map<String, String> docMap) throws Exception {
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

        XWPFDocument mainDocument = new XWPFDocument(new FileInputStream(sourceFile));
        int markPoint = getMarkPoint(modelDocx, targetSign, docMap);
        setRed(markPoint, mainDocument, modelDocx);
        File file = new File(outPutPath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        modelDocx.write(new FileOutputStream(file));
        com.aspose.words.Document document = AsposeWordsUtil.DocumentBuilder(new FileInputStream(outPutPath));
        File pdf = new File(outPutPath.replace(".docx", ".pdf"));
        pdf.createNewFile();
        OutputStream os = new FileOutputStream(pdf);
        AsposeWordsUtil.saveWordToPdf(document, os);
        os.close();
        modelDocx.close();
        return "成功";
    }

    private static int getMarkPoint(XWPFDocument modelDocx, String targetSign, Map<String, String> docMap) {
        int startPoint = 0;
        
        List<XWPFParagraph> modelParagraphs = modelDocx.getParagraphs();
        List<XWPFTable> modelTables = modelDocx.getTables();
        for (int i = 0; i < modelParagraphs.size(); i++) {
            XWPFParagraph xwpfParagraph = modelParagraphs.get(i);
            List<XWPFRun> runs = xwpfParagraph.getRuns();
            for (int l = 0; l < runs.size(); l++) {
                XWPFRun run = runs.get(l);
                String runText = run.text();
                runText = StringUtils.deleteWhitespace(runText);
                if (runText.equals(targetSign)) {
                    xwpfParagraph.removeRun(l);
                    startPoint = i;
                    
                }
                
                if (docMap.containsKey(runText)) {
                    
                    run.setText(docMap.get(runText), 0);
                    
                }
            }
        }
        for (XWPFTable modelTable : modelTables) {
            List<XWPFTableRow> rows = modelTable.getRows();
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> tableCells = row.getTableCells();
                for (int k = 0; k < tableCells.size(); k++) {
                    XWPFTableCell tableCell = tableCells.get(k);
                    String cellText = tableCell.getText();
                    if (docMap.containsKey(cellText)) {
                        List<XWPFParagraph> paragraphs = tableCell.getParagraphs();
                        for (XWPFParagraph paragraph : paragraphs) {
                            List<XWPFRun> runs = paragraph.getRuns();
                            for (XWPFRun run : runs) {
                                if (docMap.containsKey(run.text())) {
                                    
                                    run.setText(docMap.get(cellText), 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return startPoint;
    }

    private static void setRed(int startPoint, XWPFDocument mainDocument, XWPFDocument modelDocx) {
        List<XWPFParagraph> paraList = mainDocument.getParagraphs();
        XWPFParagraph insertNewParagraph = modelDocx.getParagraphs().get(startPoint);
        XmlCursor xmlCursor = insertNewParagraph.getCTP().newCursor();
        for (int j = 0; j < paraList.size(); j++) {

            XWPFParagraph oldParagraph = paraList.get(j);
            CTPPr oldPPr = oldParagraph.getCTP().getPPr();

            if (oldPPr != null) {

                CTParaRPr oldPPrRpr = oldPPr.getRPr();
                CTPPrChange oldPPrChange = oldPPr.getPPrChange();

                CTTrackChange rprIns = null;
                CTParaRPrChange oldPraRprChange = null;
                CTTrackChange pprRprDel = null;

                if (oldPPrRpr != null) {
                    rprIns = oldPPrRpr.getIns();
                    oldPraRprChange = oldPPrRpr.getRPrChange();
                    pprRprDel = oldPPrRpr.getDel();
                }

                if (oldPPrChange != null) {
                    oldPPr.unsetPPrChange();
                }

                if (oldPraRprChange != null) {
                    oldPPrRpr.unsetRPrChange();
                }
                if (rprIns != null) {
                    oldPPrRpr.unsetIns();
                }
                if (pprRprDel != null) {
                    oldPPrRpr.unsetDel();
                }
            }

            insertNewParagraph.getCTP().setPPr(oldPPr);
            List<XWPFRun> runs = oldParagraph.getRuns();

            for (int k = 0; k < runs.size(); k++) {

                XWPFRun oldRun = runs.get(k);
                CTRPr oldRPr = oldRun.getCTR().getRPr();
                if (oldRPr != null) {
                    CTRPrChange oldRPrChange = oldRPr.getRPrChange();
                    if (oldRPrChange != null) {
                        oldRPr.unsetRPrChange();
                    }
                }

                XWPFRun newRun = insertNewParagraph.createRun();
                newRun.getCTR().setRPr(oldRPr);
                newRun.setText(oldRun.text());
                insertNewParagraph.addRun(newRun);
            }

            xmlCursor.toNextSibling();
            insertNewParagraph = modelDocx.insertNewParagraph(xmlCursor);
            xmlCursor = insertNewParagraph.getCTP().newCursor();

        }
    }
}
