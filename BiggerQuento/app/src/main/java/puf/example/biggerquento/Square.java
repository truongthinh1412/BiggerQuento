package puf.example.biggerquento;

import android.graphics.Rect;


public class Square {
    Rect r;
    boolean active;
    String content;

    public Square(){
        active = false;
        content = "";
        r = new Rect();
    }

    public Square(boolean A, Rect R){
        r = R;
        active = A;
        content = "";
    }


    public Rect getR() {
        return r;
    }

    public void setR(Rect r) {
        this.r = r;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
