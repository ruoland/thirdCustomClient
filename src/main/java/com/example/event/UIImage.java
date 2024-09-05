package com.example.event;

public class UIImage {
    String imgPath;
    int x, y, width, height, duration;

    public UIImage(String imgPath, int x, int y, int width, int height, int duration) {
        this.imgPath = imgPath;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.duration = duration;
    }

    public String getImgPath() {
        return imgPath;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDuration() {
        return duration;
    }
}
