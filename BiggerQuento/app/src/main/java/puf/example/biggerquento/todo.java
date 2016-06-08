package puf.example.biggerquento;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Admin on 6/6/2016.
 */
public class todo {


    /*int count = 0;
    //random route with fixed length algorithm

    ArrayList<Coordinates> randomStep(int n, Coordinates c){
        ArrayList<Coordinates> result = new ArrayList<>();
        if(n == 0){
            result.add(c);
        }else {
            result = randomStep(n - 1, c);

            Coordinates lastC = result.get(result.size() - 1);

            ArrayList<Coordinates> neighborsOfLast = getNeighbors(lastC);
            ArrayList<Coordinates> validNeighbors = new ArrayList<>();

            for (int i = 0; i < neighborsOfLast.size(); i++) {
                if (neighborsOfLast.get(i).isNotIn(result)) {
                    //Log.d("valid neighbor",neighborsOfLast.get(i).LogOut());
                    validNeighbors.add(neighborsOfLast.get(i));
                }
            }

            if (validNeighbors.isEmpty() && (count < n)) {
                count = 0;
                result = randomStep(n, c);
            }else if(validNeighbors.isEmpty() && (count == n)){
                return result;
            }else{
                count++;
                Log.d("emptiness",validNeighbors.isEmpty()+"");
                Log.d("count",(count)+"");
                result.add(randomCFromList(validNeighbors));
            }

            String r = "";
            for (int m = 0; m < result.size(); m++) {
                r = r + result.get(m).getX() + "/" + result.get(m).getY() + "->";

            }
            Log.d("result", r);
        }
        return result;
    }*/
}
