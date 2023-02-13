package cn.cyberict.ncha.business.commons.DocumentUtils;

import com.aspose.words.Document;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.itextpdf.text.pdf.*;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


public class BarCodeUtil {


    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");


    
    public void importDocBarCode(Map<String, String> barCodeMap) throws Exception {
        

        String rightText = barCodeMap.get("rightText");
        String barCodeText = barCodeMap.get("barCodeText");
        String fileName = UUID.randomUUID().toString() + ".pdf";

        String wordPath = "" + "importModel.docx"; 
        String outPdfPath = ""; 
        
        Map<String, String> map = this.initReplaceParam(null, 1);
        this.createBarCode(barCodeText, rightText, wordPath, outPdfPath, map);

    }









    
    public static Map<String, String> initBarCodeParam(Map<String, String> param) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        
        String rightText;
        
        String barCodeText;
        stringBuilder.append("ZH");
        String format = simpleDateFormat.format(new Date());
        stringBuilder.append(format);
        stringBuilder.append("发");
        stringBuilder.append(param.get("servialNum"));
        stringBuilder.append("D").append(param.get("printNum")); 
        rightText = stringBuilder.toString();
        
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append("GB0626-2005^");
        stringBuilder.append(rightText).append("^");
        stringBuilder.append("国家文物局^");
        stringBuilder.append(param.get("manuscriptTypeName")).append("^");
        stringBuilder.append(param.get("dispatchNum")).append("^");
        stringBuilder.append(param.get("majorSend")).append("^"); 
        stringBuilder.append(param.get("dispatchTitle")).append("^"); 
        stringBuilder.append("").append("^");
        stringBuilder.append(param.get("isHurryName")).append("^");
        stringBuilder.append(param.get("signIssueDate")).append("^");
        stringBuilder.append("^");
        stringBuilder.append("国家文物局").append("^");
        stringBuilder.append(simpleDateFormat.format(new Date()));
        stringBuilder.append("^^|");
        barCodeText = stringBuilder.toString();
        map.put("rightText", rightText);
        map.put("barCodeText", barCodeText);
        System.out.println("本次条码号:" + barCodeText);
        return map;
    }


    
    public static Map<String, String> initReplaceParam(Map<String, Object> param, Integer isLimit) {
        Map<String, String> map = new HashMap<>();

        map.put("{m}", param.get("pacMonth") == null ? "  " : String.valueOf(param.get("pacMonth"))); 
        map.put("{d}", param.get("pacDay") == null ? "  " : String.valueOf(param.get("pacDay"))); 
        map.put("{wh}", (String) param.get("dispatchNum"));
        map.put("{gkxs}", (String) param.get("disclosureModeName"));
        map.put("{lsh}", (String) param.get("serialNum"));
        map.put("{hj}", (String) param.get("isHurryName"));
        map.put("{qf}", (String) param.get("signIssue"));
        map.put("{ngdw}", (String) param.get("sponsorUnit"));
        map.put("{ngr}", (String) param.get("sponsor"));
        map.put("{dh}", (String) param.get("telNo"));
        map.put("{bt}", (String) param.get("dispatchTitle"));
        map.put("{zs}", (String) param.get("majorSend"));
        map.put("{cs}", (String) param.get("duplicateSend"));
        map.put("{gz}", (String) param.get("commonSealName"));
        
        if (isLimit == 1) {
            map.put("{sy}", "法定办理期限" + (param.get("limitDay") == null ? " " : param.get("limitDay") )+ "个工作日，还剩" + (param.get("remnant") == null ? "" : param.get("remnant")) + "个工作日");
        } else {
            map.put("{sy}", "");
        }
        StringBuilder stringBuilder = new StringBuilder();
        List<Map> list = (List<Map>) param.get("process"); 
        if (list.size() > 0) {
            for (Map opinionItem : list) {
                if (opinionItem.get("opinion") != null && StringUtils.trimToNull(opinionItem.get("opinion").toString()) != null) {
                    stringBuilder.append("\r");
                    stringBuilder.append(opinionItem.get("opinion")).append(" ");
                    stringBuilder.append(opinionItem.get("processedName")).append(" ");
                    stringBuilder.append(opinionItem.get("processedTime"));
                }
                map.put("{hg}", stringBuilder.substring(stringBuilder.indexOf("\r") > -1 ? stringBuilder.indexOf("\r") : 0, stringBuilder.length()));
            }
        } else {
            map.put("{hg}", " ");
        }

        return map;
    }

    
    public static void createBarCode(String barCodeText,
                                     String rightText,
                                     String wordPath,
                                     String outputFile,
                                     Map<String, String> elementMap
    ) throws Exception {
        File wordFile = new File(wordPath);
        if (wordFile.isDirectory()) {
            throw new NullPointerException("word Path is Directory");
        }
        if (!wordFile.exists()) {
            throw new FileNotFoundException("word is not exists");
        }
        byte[] barCodeByte = getBarCode(barCodeText);
        Document document = AsposeWordsUtil.DocumentBuilder(new FileInputStream(wordFile));
        insertBarCodeAndSaveDocByBookMark(document, outputFile, rightText, elementMap, barCodeByte);

    }

    public static void insertBarCodeAndSaveDocByBookMark(Document document,
                                                         String outputFile,
                                                         String rightText,
                                                         Map<String, String> elementMap,
                                                         byte[] barCodeByte) throws Exception {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        insertBarCodeAndSaveDocByBookMark(document, bufferedOutputStream, rightText, elementMap, barCodeByte);
    }

    public static void insertBarCodeAndSaveDocByBookMark(Document document,
                                                         OutputStream outputFile,
                                                         String rightText,
                                                         Map<String, String> elementMap,
                                                         byte[] barCodeByte) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("barCode", barCodeByte);
        map.put("barCodeText", rightText);
        AsposeWordsUtil.replaceElementsForWord(document, elementMap);
        AsposeWordsUtil.replaceElementByBookMark(document, map);
        document.save(outputFile, SaveFormat.DOCX);
    }


    
    public static byte[] getBarCode(String barCodeText) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BarcodePDF417 pdf = new BarcodePDF417();
        pdf.setOptions(BarcodePDF417.PDF417_USE_ERROR_LEVEL);
        pdf.setOptions(BarcodePDF417.PDF417_FIXED_RECTANGLE);
        pdf.setCodeColumns(8);
        pdf.setErrorLevel(3);
        pdf.setYHeight(2);
        pdf.setText(barCodeText.getBytes(Charset.forName("gb2312")));
        Image awtImage = pdf.createAwtImage(Color.black, Color.white);
        int awtHeight = awtImage.getHeight(null);
        int awtWidth = awtImage.getWidth(null);
        double multi = 1.0;
        if (awtHeight < 40) {
            multi = (double) 40 / awtHeight;
            awtHeight = (int) (awtHeight * multi);
        } else {
            multi = (double) awtHeight / 40;
            awtHeight = (int) (awtHeight / multi);
        }
        BufferedImage read = new BufferedImage(awtWidth, awtHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) read.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics.drawImage(awtImage, 0, 0, awtWidth, awtHeight, null);
        graphics.dispose();
        ImageIO.write(read, "jpg", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public static void insertBarCodeAndSavePDF(File wordFile,
                                               String newFileName,
                                               String rightText,
                                               Map<String, String> elementMap,
                                               byte[] barCodeByte) throws Exception {
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
        com.itextpdf.text.Image instance = com.itextpdf.text.Image.getInstance(barCodeByte);
        float width = instance.getWidth();
        float height = instance.getHeight();
        byte[] pdfByte = savePdf(wordFile, elementMap);
        PdfReader pdfReader = new PdfReader(pdfByte);
        com.itextpdf.text.Rectangle pageSize = pdfReader.getPageSize(1);
        float pdfHeight = pageSize.getHeight();
        float pdfWidth = pageSize.getWidth();
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(newFileName));
        float x = (pdfWidth - width) / 2;
        float y = pdfHeight - (height * 1.5f) - 3;
        instance.setAbsolutePosition(x, y);
        PdfContentByte underContent = pdfStamper.getUnderContent(1);
        underContent.addImage(instance);
        underContent.beginText();
        underContent.setFontAndSize(baseFont, 12);
        underContent.showTextAligned(PdfContentByte.ALIGN_CENTER, rightText, x + (int) (width / 1.3), y + 3 + height, 0);
        underContent.endText();
        pdfStamper.close();
    }

    public static byte[] savePdf(File file, Map<String, String> elementMap) throws Exception {
        InputStream is = Document.class.getResourceAsStream("/com.aspose.words.lic_2999.xml");
        License aposeLic = new License();
        aposeLic.setLicense(is);
        is.close();
        Document document = AsposeWordsUtil.DocumentBuilder(new FileInputStream(file));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (elementMap != null || elementMap.size() > 0) {
            for (Map.Entry<String, String> entry : elementMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                FindReplaceOptions options = new FindReplaceOptions();
                options.setMatchCase(false);
                options.setFindWholeWordsOnly(false);
                document.getRange().replace(key, value, options);
            }
        }
        document.save(byteArrayOutputStream, SaveFormat.PDF);
        return byteArrayOutputStream.toByteArray();
    }
}
