package edu.utep.cs4381.melonlord;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.utep.cs4381.melonlord.model.FireBall;
import edu.utep.cs4381.melonlord.model.GameObject;
import edu.utep.cs4381.melonlord.model.Player;
import edu.utep.cs4381.melonlord.model.PowerUp;
import edu.utep.cs4381.melonlord.model.VillainBackground;

/** @author: Jose Eduardo Soto <jesoto4@miners.utep.edu>
*   @author:        Ruth Trejo <rtrejo9@miners.utep.edu>
*/
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
    private List<FireBall> fireBallList = new CopyOnWriteArrayList<>();
    private PowerUp powerUp;
    private int xLeftButtonCoord;
    private int yLeftButtonCoord;
    private int xRightButtonCoord;
    private int yRightButtonCoord;
    private int padding;

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
        // Add all fireballs
        for (int i = 0; i < 5; i++)
            fireBallList.add(new FireBall(context,screenWidth,screenHeight,
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball)));

        // Only 1 PowerUp per session
        powerUp = new PowerUp(context,screenWidth,screenHeight,
                BitmapFactory.decodeResource(context.getResources(),R.drawable.armor));
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

        //update powerup speed
        powerUp.update(0);

        //update fireball speed
        for (FireBall go: fireBallList) {
            go.update(0);
        }


    }//end update

    private void draw(){
        if(holder.getSurface().isValid()){
            canvas = holder.lockCanvas();

            //Draw background image
            canvas.drawBitmap(melonLord.getBitMap(), melonLord.getX(), melonLord.getY(), paint);

            //Draw the character, sokka, the player will be playing as
            canvas.drawBitmap(player.getBitMap(),player.getX(), player.getY(), paint);


            //Draw fireballs falling down from top of the screen
            for( FireBall fb: fireBallList ){
                canvas.drawBitmap(fb.getBitMap(),fb.getX(), fb.getY(), paint);
            }

            // DrawButtons

            holder.unlockCanvasAndPost(canvas);
        }
    }//end draw

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch( event.getActionMasked() ){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}
