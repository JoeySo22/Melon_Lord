package edu.utep.cs4381.melonlord;

import android.content.Context;
import android.content.SharedPreferences;
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
    private long longestTime;

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

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private long timeStarted;
    private long timeTaken;
    private Paint textPaint;

    public MelonLordView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context      = context;
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;

        holder            = getHolder();
        paint             = new Paint();
        textPaint         = new Paint();

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

        preferences = context.getSharedPreferences("HISCORE", Context.MODE_PRIVATE);
        editor = preferences.edit();
        longestTime = preferences.getLong("longestTime",0);

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

        fireBallList.clear();
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
        timeStarted = System.currentTimeMillis();
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
            gameThread.sleep(2); // in milliseconds
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
                gameEnded = player.powerDown();
                break;
            }
        }

        // Record time
        if (!gameEnded) {
            timeTaken = System.currentTimeMillis() - timeStarted;
        } else {
            if (timeTaken > longestTime) {
                editor.putLong("longestTime", timeTaken);
                longestTime = timeTaken;
            }
        }
    }//end update

    private void draw(){
        if(holder.getSurface().isValid()){
            canvas = holder.lockCanvas();

            //Draw background image
            canvas.drawBitmap(bgBitmap, frameToDraw, whereToDraw, paint);
            // Black background
            //canvas.drawColor(Color.argb(255, 0, 0, 0));
            //Draw the character, sokka, the player will be playing as
            if (!gameEnded){
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
                // Draw Score Text
                textPaint.setColor(Color.argb(255,25,255,255));
                textPaint.setTextSize(45);
                textPaint.setTextAlign(Paint.Align.LEFT);
                textPaint.setColor(Color.BLACK);
                canvas.drawText(formatTime("Longest Time: ", timeTaken), 50, 50, textPaint);

            } else {
                textPaint.setColor(Color.argb(255,25,255,255));
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(90);
                canvas.drawText("Game Over!", screenWidth/2, screenHeight/2, textPaint);
                textPaint.setTextSize(45);
                canvas.drawText(formatTime("Best Time", longestTime), screenWidth/2,
                        (screenHeight/2) + 90, textPaint);
            }

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

    // Time in milliseconds
    private String formatTime(String label, long time) {
        return String.format("%s:%d.%03ds", label, time/1000, time %1000);
    }
}//end MelonLordView