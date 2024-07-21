package com.example.wrapper.widget;

public interface IWidget {
    void setPosition(int x, int y);
    void setSize(int width, int height);
    void setVisible(boolean visible);
    void setMessage(String message);
    boolean isMouseOver(double mouseX, double mouseY);

}