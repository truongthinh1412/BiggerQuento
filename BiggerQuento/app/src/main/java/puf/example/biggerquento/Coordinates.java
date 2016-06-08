package puf.example.biggerquento;

import java.util.ArrayList;

public class Coordinates {
    int x, y;

    public Coordinates(){
        x = y = -1;
    }
    public Coordinates(int a, int b){
        x = a;
        y = b;
    }

    public boolean isNeighbor(int a, int b){
        return (x == a + 1 && y == b) ^ (x == a - 1 && y == b) ^ (y == b + 1 && x == a) ^ (y == b - 1 && x == a);
    }

    public boolean isEqual(Coordinates c){
        return (this.getX() == c.getX()) && (this.getY() == c.getY());
    }

    public boolean isNotIn(ArrayList<Coordinates> c){
        boolean r = true;
        for(int i = 0; i < c.size(); i++){
            if(this.isEqual(c.get(i))){
                r = false;
            }
        }
        return  r;
    }

    public String LogOut(){
        return x + "/" + y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
