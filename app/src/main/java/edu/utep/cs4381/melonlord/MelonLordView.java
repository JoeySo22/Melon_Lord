package edu.utep.cs4381.melonlord;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.utep.cs4381.melonlord.model.FireBall;
import edu.utep.cs4381.melonlord.model.Player;
import edu.utep.cs4381.melonlord.model.PowerUp;

/** Authors:
 *  Jose Eduardo Soto <jesoto4@miners.utep.edu>
 *   Ruth Trejo <rtrejo9@miners.utep.edu>
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

    //Button design
    private Paint buttonPaint;
    private int leftButtonXCoord;
    private int leftButtonYCoord;
    private int rightButtonXCoord;
    private int rightButtonYCoord;
    private int buttonWidth;
    private int buttonHeight;
    private int xRadiusButton;
    private int yRadiusButton;
    private int paddingButton;

    //Holder for drawing and updating
    private SurfaceHolder holder;

    private Player player;
    private List<FireBall> fireBallList = new CopyOnWriteArrayList<>();
    private PowerUp powerUp;

    //Drawing background image for game play to fit any phone screen
    DisplayMetrics bgMetrics;
    int width, height;
    Bitmap bgBitmap;
    Rect frameToDraw;
    Rect whereToDraw;
    private Rect leftButtonRect;
    private Rect rightButtonRect;

    public MelonLordView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context      = context;
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;

        holder            = getHolder();
        paint             = new Paint();

        buttonPaint       = new Paint();
        buttonWidth       = (int) (screenWidth * (.1428));
        buttonHeight      = (int) (screenHeight * (.0714));
        leftButtonXCoord  = paddingButton;
        leftButtonYCoord  = (int) (screenHeight * .8000);
        rightButtonXCoord = screenWidth - paddingButton - buttonWidth;
        rightButtonYCoord = leftButtonYCoord;
        xRadiusButton     = (int) (screenWidth * .0010);
        yRadiusButton     = xRadiusButton;
        leftButtonRect    = new Rect(leftButtonXCoord, leftButtonYCoord,
                leftButtonXCoord + buttonWidth, leftButtonYCoord + buttonHeight);
        rightButtonRect   = new Rect(rightButtonXCoord, rightButtonYCoord,
                rightButtonXCoord + buttonWidth, rightButtonYCoord + buttonHeight);

        xRadiusButton     = 50;
        yRadiusButton     = 50;

        bgMetrics         = context.getResources().getDisplayMetrics();
        width             = bgMetrics.widthPixels;
        height            = bgMetrics.heightPixels;

        startGame(context, screenWidth, screenHeight);
    }

    private void startGame(Context context, int x, int y){
        //Initialize player at the start of the game
        player = new Player(context, x, y, BitmapFactory.decodeResource(context.getResources(),
                R.drawable.sokka_10p_smaller),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.sokka_armor)
        );

        //Scale the BACKGROUND image to any phone screen when the game starts
        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        bgBitmap = Bitmap.createScaledBitmap(bgBitmap, width, height, false);

        frameToDraw = new Rect(0, 0, bgBitmap.getWidth(), bgBitmap.getHeight());
        whereToDraw = new Rect(0, 0, screenWidth, screenHeight);

        // ADD ALL FIREBALL OBJECTS TO fireBallList
        for (int i = 1; i <= 4; i++)
            fireBallList.add(new FireBall(context,screenWidth,screenHeight,
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_smaller)));

        // Only 1 PowerUp per session
        powerUp = new PowerUp(context,screenWidth,screenHeight,
                BitmapFactory.decodeResource(context.getResources(),R.drawable.armor));

        gameEnded = false;
        Log.d("View/StartGame", String.format("\nleftButtonRect = %s\nrightButtonRect = %s",
                leftButtonRect.toShortString(), rightButtonRect.toShortString()));
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
        } catch (InterruptedException e) { System.out.println(e.getStackTrace());}
    }//end pause

    private void control() {
        try {
            // This slows down our CPU so that we can catch frames and changes.
            // tempo of the game, can increase or decrease.
            gameThread.sleep(17); // in milliseconds
        } catch (InterruptedException e) {
            System.out.print(e.getStackTrace());
        }

    }//end control

    private void update(){

        //update player speed
        player.update(0);

        //update powerup speed
        powerUp.update(0);

        //update fireball speed
        for (FireBall fb: fireBallList) {
            fb.update(0);
        }

        // Check collisions with player and powerup
        if (Rect.intersects(player.getHitBox(), powerUp.getHitBox())) {
            Log.d("View/Update","player hits powerup");
            powerUp.destroy();
            player.powerUp();
        }

        // Check collisions with player and fireball
        for (FireBall fb: fireBallList) {
            if (Rect.intersects(player.getHitBox(), fb.getHitBox())) {
                Log.d("View/Update","player hits fireball");
                Log.d("View/Update", String.format(
                        "\nplayerCornerCoord = (%d,%d)\nfireballCornerCoord = (%d,%d)",
                        player.getX(), player.getY(), fb.getX(), fb.getY())
                );
                Log.d("View/Update", String.format("playerHitboxString = %s\nfireballHitboxString = %s",
                        player.getHitBox().toShortString(), fb.getHitBox().toShortString()));
                fb.destroy();
                player.powerDown();
            }
        }

        //
    }//end update

    private void draw(){
        if(holder.getSurface().isValid()){
            canvas = holder.lockCanvas();

            //Draw background image
            canvas.drawBitmap(bgBitmap, frameToDraw, whereToDraw, paint);
            // Black background
            //canvas.drawColor(Color.argb(255, 0, 0, 0));

            //Draw the character, sokka, the player will be playing as
            canvas.drawBitmap(player.getBitMap(),player.getX(), player.getY(), paint);


            //Draw fireballs falling down from top of the screen
            for( FireBall fb: fireBallList ){
                canvas.drawBitmap(fb.getBitMap(),fb.getX(), fb.getY(), paint);
            }

            if (powerUp.isMoving())
                canvas.drawBitmap(powerUp.getBitMap(),powerUp.getX(), powerUp.getY(), paint);

            // Draw buttons
            buttonPaint.setColor(Color.argb(200, 255,255,255));
            // Draw Left Button
            canvas.drawRoundRect(leftButtonXCoord, leftButtonYCoord,
                    leftButtonXCoord + buttonWidth, leftButtonYCoord + buttonHeight,
                    xRadiusButton, yRadiusButton, buttonPaint);
            // Draw Right Button
            canvas.drawRoundRect(rightButtonXCoord, rightButtonYCoord,
                    rightButtonXCoord + buttonWidth, rightButtonYCoord + buttonHeight,
                    xRadiusButton, yRadiusButton, buttonPaint);

            holder.unlockCanvasAndPost(canvas);
        }
    }//end draw

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect toucharea = new Rect((int)event.getX(), (int)event.getY(),
                (int)event.getX(), (int)event.getY());
        Log.d("View/TouchEvent", toucharea.toShortString());
        switch(event.getActionMasked() ){
            // TODO: Handle coords of the event to see if they are on either button.
            case MotionEvent.ACTION_DOWN:
                if (Rect.intersects(leftButtonRect,toucharea)) {
                    player.pressingLeft(true);
                }
                if (Rect.intersects(rightButtonRect, toucharea)) {
                    player.pressingRight(true);
                }
                if(gameEnded)
                    startGame(context, screenWidth, screenHeight);
                break;
            case MotionEvent.ACTION_UP:
                player.pressingLeft(false);
                player.pressingRight(false);
                break;
        }
        return true;
    }
}//end MelonLordView