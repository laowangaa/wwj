package cn.cyberict.ncha.business.commons.utils;


public class MultipleImage {
    
    private float width;
    
    private float height;
    
    private float x;

    
    private float y;

    public MultipleImage(float imageWidth, float imageHeight) {
        this.x = imageWidth;
        this.y = imageHeight;
    }

    public MultipleImage(float width, float height, float imageWidth, float imageHeight) {
        this.width = width;
        this.height = height;
        this.x = imageWidth;
        this.y = imageHeight;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}
