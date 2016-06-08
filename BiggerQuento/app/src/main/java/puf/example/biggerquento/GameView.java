package puf.example.biggerquento;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    //with a 5x5 table, it already seems insanely difficult
    int TableSize = 5;

    int[][] numbers = new int[TableSize][TableSize];
    String[][] signs = new String[TableSize][TableSize];
    Square[][] sqrs = new Square[TableSize][TableSize];

    //last coordinates touched on the sqrs
    Coordinates lastCTouched = new Coordinates();

    //equation string
    String eq = "";

    //some helping integer
    int objective;
    int resultP;
    int step;
    int counter = 0;

    //Coordinate origin of the random route that leads to the objective
    Coordinates Or = new Coordinates();

    //generate random coordinates
    public Coordinates randomC(){
        Random randomGenerator = new Random();
        ArrayList<Coordinates> validC = new ArrayList<>();
        for(int i = 0; i < TableSize; i++){
            for (int j = 0; j < TableSize; j++){
                if(i % 2 == j % 2){
                    Coordinates c = new Coordinates(i,j);
                    validC.add(c);
                }
            }
        }
        return validC.get(randomGenerator.nextInt(validC.size()));
    }

    //random number of step
    public int randomStepNumber(){
        Random random = new Random();
        return (random.nextInt(8)+ 2) * 2;
    }


    //get a random route in an arraylist of routes
    public ArrayList<Coordinates> randomRoute(ArrayList<ArrayList<Coordinates>> c){
        Random randomGenerator = new Random();
        return c.get(randomGenerator.nextInt(c.size()));
    }

    //get content from sqrs at coordinate c
    public String getContentFromC(Coordinates c){
        return sqrs[c.x][c.y].content;
    }

    //get the result from the route taken
    public int resultOfARoute(ArrayList<Coordinates> c){
        String e = "";
        for(int i = 0; i < c.size(); i++){
            e = e + getContentFromC(c.get(i));
        }
        return (int)eval(e);
    }

    //these setups fill the tables with elements to use later on, they have room for optimization
    void setupN(){
        ArrayList<Integer> r = new ArrayList<>();
        for(int i = 0; i < TableSize * TableSize + 2; i++){
            r.add(i);
        }
        Random random = new Random();
        for (int i = 0; i < TableSize; i++){
            for (int j = 0; j < TableSize; j++){
                int index = random.nextInt(r.size());
                numbers[i][j] = r.get(index);
                r.remove(index);
            }
        }
    }


    void setupS(){
        for (int i = 0; i < TableSize; i++){
            for (int j = 0; j < TableSize; j++) {
                if (i % 2 == 0) {
                    signs[i][j]="-";
                }else{
                    signs[i][j]="+";
                }
            }
        }
    }

    public void setup(){
        for (int i = 0; i < TableSize; i++){
            for (int j = 0; j < TableSize; j++) {
                sqrs[i][j] = new Square();
                if (i % 2 == j % 2) {
                    sqrs[i][j].setContent(numbers[i][j] + "");
                }else{
                    sqrs[i][j].setContent(signs[i][j]);
                }
            }
        }
    }

    //setting up is a bit long so we should put it in a function
    public void randomStuff(){
        step = randomStepNumber();
        Or = randomC();
        Log.d("co", Or.x + "/" + Or.y);
        Touch.setup();
        setupN();
        setupS();
        setup();
        ArrayList<Coordinates> route = randomRoute(Step(step,Or));
        String s = "";
        for(int i = 0; i < route.size();i++){
            s = s + getContentFromC(route.get(i));
        }
        Log.d("eq ob",s);
        objective = resultOfARoute(route);
    }

    public GameView(Context context) {
        super(context);
    }

    //setting stuff up in the initialization of the view
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        randomStuff();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //getting the valid neighboring coordinates
    public ArrayList<Coordinates> getNeighbors(Coordinates c){

        int i = c.getX();
        int j = c.getY();

        Coordinates[] cs = new Coordinates[4];
        cs[0] = new Coordinates(i - 1,j);
        cs[1] = new Coordinates(i,j + 1);
        cs[2] = new Coordinates(i + 1,j);
        cs[3] = new Coordinates(i,j - 1);

        //checking validity
        if(i == 0){
            cs[0] = null;
        }if(i == TableSize - 1){
            cs[2] = null;
        }if(j == 0){
            cs[3] = null;
        }if(j == TableSize - 1){
            cs[1] = null;
        }


        //turning the array with null element(s) into an arrayList
        ArrayList<Coordinates> neighbors = new ArrayList<>();
        for (Coordinates neighbor : cs ){
            if(neighbor != null){
                neighbors.add(neighbor);
            }
        }
        return neighbors;

    }

    //getting those first possible routes
    public ArrayList<ArrayList<Coordinates>> firstStep(Coordinates c){

        ArrayList<Coordinates> neighbors = getNeighbors(c);
        ArrayList<ArrayList<Coordinates>> result = new ArrayList<>();
        for (int i = 0; i < neighbors.size(); i++){
            ArrayList<Coordinates> route = new ArrayList<>();
            route.add(0,c);
            route.add(1,neighbors.get(i));
            result.add(i,route);
        }
        return result;
    }

    //what if we want to take more than 1 step ? (n indicates the amount of step we want to take, c is the coordinates of the origin)
    public ArrayList<ArrayList<Coordinates>> Step(int n, Coordinates c){
        ArrayList<ArrayList<Coordinates>> result = new ArrayList<>();
        if(n < 1){
            result = null;
        }
        else if(n == 1){
            result = firstStep(c);
        }else{

            //recursive call, let's hope this works
            ArrayList<ArrayList<Coordinates>> lastStep = Step(n-1,c);

            //iterating through the result of the recursive call
            for(int i = 0; i < lastStep.size(); i++){

                Coordinates lastCor = lastStep.get(i).get(lastStep.get(i).size() - 1);
                ArrayList<Coordinates> allNeighbors = getNeighbors(lastCor);
                ArrayList<Coordinates> route = lastStep.get(i);


                //adding new routes to the output, excluding all the neighbors that has already appeared in the route
                for (int j = 0; j < allNeighbors.size(); j++) {
                    if (allNeighbors.get(j).isNotIn(route)) {
                        ArrayList<Coordinates> newRoute = new ArrayList<>();
                        newRoute.addAll(route);
                        newRoute.add(allNeighbors.get(j));
                        result.add(newRoute);

                        /*String r = "";
                        for (int m = 0; m < newRoute.size(); m++) {
                            r = r + newRoute.get(m).getX() + "/" + newRoute.get(m).getY() + "->";
                        }
                        Log.d("A route", r);*/
                    }
                }
            }
        }
        Log.d("Number of possible ways",result.size()+"");
        return result;
    }

    //return the result of a equation string (tested)
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    //weird idea for a function but still pretty useful
    public void setFalseExcept(int a, int b) {
        for (int i = 0; i < TableSize; i++) {
            for (int j = 0; j < TableSize; j++) {
                if (i != a && i != b) {
                    sqrs[i][j].setActive(false);
                }
            }
        }
    }

    //clear some user's input
    public void clearAll(){
        setFalseExcept(-1,-1);
        eq = "";
        counter = 0;
    }

    //all the paints we are going to use, better initialize them here
    Paint paintBorder = new Paint();
    Paint paintNumber = new Paint();
    Paint paint = new Paint();
    Paint textPaint = new Paint();
    Bitmap replayImage = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_replay_black_24px)).getBitmap();
    Rect replay = new Rect();

    @Override
    public void onDraw(Canvas canvas){

        super.onDraw(canvas);

        int height = canvas.getHeight();
        int width = canvas.getWidth();

        int botMargin = canvas.getHeight() / 10;
        int dimension = width / (TableSize + 2);


        //draw the equation
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(dimension / 2);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 11) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText(eq, xPos, yPos, textPaint);


        //set the border paint
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(Color.BLACK);

        //remake button
        replay = new Rect(0,0,width/30,width/30);
        canvas.drawBitmap(replayImage,null,replay,paintBorder);

        //lets draw those squares
        for (int i = 0; i < sqrs.length; i++){
            for (int j = 0; j < sqrs[i].length; j++) {

                Rect r = new Rect((i + 1) * dimension, height - (j + 1) * dimension - botMargin, (i + 2) * dimension, height - j * dimension - botMargin);
                sqrs[i][j].setR(r);
                canvas.drawRect(sqrs[i][j].r, paintBorder);

                if (i % 2 == j % 2) {

                    if (sqrs[i][j].isActive()) {
                        paint.setColor(Color.BLUE);
                    } else {
                        paint.setColor(Color.BLACK);
                    }

                    paintNumber.setTextSize(dimension / 2);

                    paint.setStyle(Paint.Style.FILL);
                    paintNumber.setColor(Color.WHITE);
                    paintNumber.setTextAlign(Paint.Align.CENTER);

                    canvas.drawRect(sqrs[i][j].r, paint);
                    canvas.drawText(sqrs[i][j].getContent(), r.centerX(), r.centerY() + dimension / 5, paintNumber);

                } else {

                    paintNumber.setTextSize(dimension / 2);
                    paintNumber.setColor(Color.BLACK);
                    paintNumber.setTextAlign(Paint.Align.CENTER);

                    if (sqrs[i][j].isActive()) {
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(sqrs[i][j].r, paint);
                    }

                    canvas.drawText(sqrs[i][j].getContent(), r.centerX(), r.centerY() + dimension / 5, paintNumber);
                }
            }
        }

        //Paint that objective !
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(dimension / 2);
        int xPos1 = (canvas.getWidth() / 2);
        int yPos1 = (int) ((2 * canvas.getHeight() / 11) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText("Try to create " + objective, xPos1, yPos1, textPaint);


        //Paint the number of steps required to get to the objective !
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(dimension / 2);
        int xPos2 = (canvas.getWidth() / 2);
        int yPos2 = (int) ((3 * canvas.getHeight() / 11) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        canvas.drawText("using "+(step + 2) / 2+" numbers", xPos2, yPos2, textPaint);

        this.postInvalidate();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                int id = event.getPointerId(0);
                Touch.touches[id].setX(touchX);
                Touch.touches[id].setY(touchY);
                Touch.touches[id].setActive(true);

                //pretty clunky looking
                for (int i = 0; i < TableSize; i++) {
                    for (int j = 0; j < TableSize; j++) {
                        if (sqrs[i][j].content != "+" && sqrs[i][j].content != "-" || new Coordinates(i,j).isNeighbor(lastCTouched.x,lastCTouched.y) && sqrs[lastCTouched.x][lastCTouched.y].isActive()) {
                            if (sqrs[i][j].r.contains((int) touchX, (int) touchY)) {

                                //Log.d("Square touched", i + " / " + j);
                                if (sqrs[i][j].isActive()) {
                                    clearAll();
                                } else {
                                    //Log.d("isNeighbor", lastCTouched.isNeighbor(i, j) + "");
                                    if (!lastCTouched.isNeighbor(i, j)) {
                                        clearAll();
                                    }
                                    sqrs[i][j].setActive(true);
                                    counter++;
                                    eq = eq + sqrs[i][j].getContent();
                                    lastCTouched = new Coordinates(i, j);
                                }
                            }
                        }
                    }
                }

                if(replay.contains((int)touchX,(int)touchY)){
                    randomStuff();
                    clearAll();
                }

                //count the number of square the player used
                if (counter == step + 1){
                    resultP = (int)eval(eq);
                    Log.d("resultP",resultP+"");
                    counter = 0;
                }

                //congrats you have made it to the objective, you get another one !
                if(resultP == objective){
                    randomStuff();
                    clearAll();
                }
            }
            case MotionEvent.ACTION_UP:{
                int id = event.getPointerId(0);
                Touch.touches[id].setActive(false);
                break;
            }


            case MotionEvent.ACTION_MOVE:{
                for (int index = 0; index < event.getPointerCount(); index++) {
                    int id = event.getPointerId(index);
                    Touch.touches[id].x = event.getX(index);
                    Touch.touches[id].y = event.getY(index);
                    break;
                }
            }
        }
        this.postInvalidate();
        return true;
    }
}
