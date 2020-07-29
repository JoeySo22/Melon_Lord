package edu.utep.cs4381.melonlord;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import edu.utep.cs4381.melonlord.model.Player;
import edu.utep.cs4381.melonlord.model.VillainBackground;

public class MelonLordView extends SurfaceView implements Runnable{

    private final Context context;
    private final int screenWidth;
    private final int screenHeight;

    //Game thread
    private Thread gameThread;

    //Flag used to play and pause
    private boolean isPlaying;
    private boolean gameEnded;

    //Used for drawing on screen
    private Canvas canvas;
    private Paint paint;

    //Holder for drawing and updating
    private SurfaceHolder holder;

    private Player player;
    private VillainBackground melonLord;

    public MelonLordView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        holder = getHolder();
        paint  = new Paint();

        startGame(context, screenWidth, screenHeight);
    }

    private void startGame(Context context, int x, int y){
        //Initialize player at the start of the game
        player = new Player(context, x, y, BitmapFactory.decodeResource(context.getResources(),
                R.drawable.sokka));
        melonLord = new VillainBackground(context, x, y,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_view));
        gameEnded = false;

    }//end startGame

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            control();
        }
    }//end run

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }//end resume

    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) { }
    }//end pause

    private void control() {
        try {
            // This slows down our CPU so that we can catch frames and changes.
            // tempo of the game, can increase or decrease.
            gameThread.sleep(17); // in milliseconds
        } catch (InterruptedException e) { }

    }//end control

    private void update(){
        //update player speed
        player.update(0);
    }//end update

    private void draw(){
        if(holder.getSurface().isValid()){
            canvas = holder.lockCanvas();
            //Idk how to draw a background with an image :( maybe just a bitmap but idk
            canvas.drawBitmap(melonLord.getBitMap(), melonLord.getX(), melonLord.getY(), paint);
            canvas.drawBitmap(player.getBitMap(),player.getX(), player.getY(), paint);


            holder.unlockCanvasAndPost(canvas);
        }
    }//end draw

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch( event.getActionMasked() ){
            case MotionEvent.ACTION_DOWN:
                player.setMoveLR(true);
                if(gameEnded)
                    startGame(context,screenWidth,screenHeight);
                break;
            case MotionEvent.ACTION_UP:
                player.setMoveLR(false);
                break;
        }
        return true;
    }
}
