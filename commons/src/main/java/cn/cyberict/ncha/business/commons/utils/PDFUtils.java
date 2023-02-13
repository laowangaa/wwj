package cn.cyberict.ncha.business.commons.utils;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;


public class PDFUtils {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    
    public PdfFont getDefaultFont() {
        try {
            return PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        return null;
    }

    
    public void replaceFieldPdf(PdfDocument pdf, Map<String, Object> map) {
        
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        
        Map<String, PdfFormField> fields = form.getFormFields();
        Iterator<String> it = fields.keySet().iterator();
        while (it.hasNext()) {
            String tableKey = it.next();
            Optional.ofNullable(tableKey).filter(map::containsKey).ifPresent(Key -> {
                Object obj = map.get(Key);
                PdfFormField formField = fields.get(Key);
                Optional.ofNullable(obj).ifPresent(o -> {
                    try {
                        this.setField(formField, o, form);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        }
        
        form.flattenFields();
    }

    
    public void setField(PdfFormField formField, Object objValue, PdfAcroForm form) {
        if (objValue instanceof String) {
            formField.setFont(getDefaultFont()).setValue(String.valueOf(objValue)); 
        }
        if (objValue instanceof ImageData) {
            ImageData imageData = (ImageData) objValue;
            this.setImage(formField, imageData, form);
        }
        if (objValue instanceof Date) {
            Date date = (Date) objValue;
            formField.setFont(getDefaultFont()).setValue(simpleDateFormat.format(date)); 
        }
    }

    
    public void setImage(PdfFormField formField, ImageData imageData, PdfAcroForm form) {
        
        PdfCanvas canvas = new PdfCanvas(form.getPdfDocument().getPage(1));
        
        Rectangle rectangle = formField.getWidgets().get(0).getRectangle().toRectangle();
        
        float x = rectangle.getLeft();
        
        float y = rectangle.getBottom();
        
        float relicImageWidth = rectangle.getWidth();
        
        float relicImageHeight = rectangle.getHeight();
        
        float imageWidth = imageData.getWidth();
        
        float imageHeight = imageData.getHeight();
        
        MultipleImage multipleImage = null;
        if (imageHeight >= imageWidth) {
            if (relicImageHeight >= imageHeight && relicImageWidth >= imageWidth) {
                
                multipleImage = this.notMultipleImage(imageWidth, imageHeight, relicImageWidth, relicImageHeight);
            } else {
                multipleImage = this.multipleImageByHeight(imageData, rectangle);
            }
        }
        if (imageHeight < imageWidth) {
            if (relicImageHeight >= imageHeight && relicImageWidth >= imageWidth) {
                
                multipleImage = this.notMultipleImage(imageWidth, imageHeight, relicImageWidth, relicImageHeight);
            } else {
                multipleImage = this.multipleImageByWidth(imageData, rectangle);
            }
        }
        
        if (multipleImage != null) {
            
            x = x + multipleImage.getX();
            y = y + multipleImage.getY();
            canvas.addImage(imageData, multipleImage.getWidth(), 0, 0, multipleImage.getHeight(), x, y);
        }
    }

    
    public MultipleImage notMultipleImage(float imageWidth, float imageHeight, float areaWidth, float areaHeight) {
        float imageWidthCenterValue = imageWidth / 2;
        float imageHeightCenterValue = imageHeight / 2;
        float widthCenterPosition = areaWidth / 2;
        float heightCenterPosition = areaHeight / 2; 
        float imageWidthMoveValue = widthCenterPosition - imageWidthCenterValue;
        float imageHeightMoveValue = heightCenterPosition - imageHeightCenterValue;
        return new MultipleImage(imageWidth, imageHeight, imageWidthMoveValue, imageHeightMoveValue);
    }

    
    public MultipleImage multipleImageByHeight(ImageData image, Rectangle signRect) {
        return Optional.ofNullable(image).flatMap(ima -> Optional.ofNullable(signRect).map(sin -> {
            
            float HeightMultiple = ima.getHeight() * 1.2f / sin.getHeight(); 
            
            float imageWidthValue = ima.getWidth() / HeightMultiple;
            
            float imageHeightValue = ima.getHeight() / HeightMultiple;
            
            if (imageWidthValue > sin.getWidth()) {
                imageHeightValue = imageHeightValue - imageHeightValue * 0.3f;
                imageWidthValue = imageWidthValue - imageWidthValue * 0.3f;
            }
            return this.notMultipleImage(imageWidthValue, imageHeightValue, sin.getWidth(), sin.getHeight());
        })).orElseThrow(() -> new NullPointerException("图片对象不能为空"));
    }

    
    public MultipleImage multipleImageByWidth(ImageData image, Rectangle signRect) {
        return Optional.ofNullable(image).flatMap(ima -> Optional.ofNullable(signRect).map(sin -> {
            float widthCenterPosition = sin.getWidth() / 2;
            float heightCenterPosition = sin.getHeight() / 2; 
            
            float widthMultiple = ima.getWidth() * 1.2f / sin.getWidth(); 
            
            float imageWidthValue = ima.getWidth() / widthMultiple;
            
            float imageHeightValue = ima.getHeight() / widthMultiple;
            
            if (imageHeightValue > sin.getHeight()) {
                imageHeightValue = imageHeightValue - imageHeightValue * 0.3f;
                imageWidthValue = imageWidthValue - imageWidthValue * 0.3f;
            }
            return this.notMultipleImage(imageWidthValue, imageHeightValue, sin.getWidth(), sin.getHeight());
        })).orElseThrow(() -> new NullPointerException("图片对象不能为空"));
    }
}
