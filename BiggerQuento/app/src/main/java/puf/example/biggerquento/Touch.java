package puf.example.biggerquento;


public class Touch {
    float x;
    float y;
    boolean active;

    //default constructor
    public Touch(){
        x = y = 0f;
        active = false;
    }

    //constructor with parameters
    public Touch(float X, float Y, boolean A){
        x = X;
        y = Y;
        active = A;
    }

    static int max_touch = 10;

    static public Touch[] touches;

    static public void setup(){
        touches = new Touch[max_touch];
        for(int i = 0; i < touches.length; i++){
            touches[i] = new Touch();
        }
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
