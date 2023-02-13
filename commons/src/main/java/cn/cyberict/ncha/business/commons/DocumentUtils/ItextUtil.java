package cn.cyberict.ncha.business.commons.DocumentUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@SuppressWarnings("unused")
public class ItextUtil {

    
    public OutputStream mergePDF(List<InputStream> streamList, OutputStream outputStream) {
        try {
            outputStream.flush();
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, outputStream);
            document.open();
            for (InputStream inputStream : streamList) {
                PdfReader reader = new PdfReader(inputStream);
                for (int j = 1; j <= reader.getNumberOfPages(); j++) {
                    document.newPage();
                    PdfImportedPage pdfim = copy.getImportedPage(reader, j);
                    copy.addPage(pdfim);
                }
            }
            document.close();
            copy.close();
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return outputStream;
        }

    }

    
    public void batchPreviewFiles(HttpServletResponse response, String[] filepathArray) {
        try {
            List<InputStream> listPdf = new ArrayList();
            if (filepathArray.length > 0) {
                for (int i = 0; i < filepathArray.length; i++) {
                    String wordfile = filepathArray[i];
                    if (wordfile.endsWith(".docx") || wordfile.endsWith(".doc")) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        AsposeWordsUtil.appendDocForPDF(byteArrayOutputStream, Arrays.asList(wordfile));
                        listPdf.add(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                    } else {
                        listPdf.add(new BufferedInputStream(new FileInputStream(wordfile)));
                    }
                }
                String uuid = UUID.randomUUID().toString();
                String filename = uuid + ".pdf";
                response.setContentType("application/pdf");
                response.setHeader("content-disposition", "inline;filename=" + URLEncoder.encode(filename, "utf-8"));
                ServletOutputStream outputStream = response.getOutputStream();
                mergePDF(listPdf, outputStream);
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
