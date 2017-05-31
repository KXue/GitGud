package com.gud.git.gitgud.Managers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.Renderable;
import com.gud.git.gitgud.Engine.Updateable;
import com.gud.git.gitgud.Fragments.GameFragment;
import com.gud.git.gitgud.GameObjects.Enemy;
import com.gud.git.gitgud.GameObjects.HomeButton;

import java.util.Random;

/**
 * Created by Nue on 5/19/2017.
 */

/*
have an enumeration/int to name enemy patterns.

Pick the next pattern in order or at random

when x time has passed, spawn the pattern
 */

public class GameManager implements Updateable,Renderable{

    private static GameManager sGameManager;

    public static GameManager getInstance(){
        if(sGameManager == null){
            sGameManager = new GameManager();
        }
        return sGameManager;
    }

    private long mTime;
    private int mPattern;
    private int mPlayerLives;
    private boolean mTimeFreezeActivated;
    private boolean running;
    private HomeButton hb;

    private GameManager(){
        mTime = 0;
        mPattern = 0;
        mPlayerLives = 2;
        mTimeFreezeActivated = true;
        running = true;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){
        if (elapsedMillis >= 1){
            if (isRunning()) {
                if (!getTimeFreezeActivated()) {
                    mTime += elapsedMillis;

                    if (mTime >= 5000) {
                        mTime = 0;
                        int centreX = 960;
                        int centreY = 540;

                        //Log.d("gamemanager",""+centreX+" "+centreY);
                        Log.d("gamemanager", "pattern " + mPattern);
                        Random r = new Random();
                        mPattern = r.nextInt(2);

                        if (mPattern == 0) {

                            float radius = 1000;

                            int numEnemies = r.nextInt(6)+2; //could randomize


                            //The circle starts from the bottom and goes counter clockwise

                            double angleToAdd = ((180.0f / (numEnemies - 1)) * Math.PI / 180);
                            double angle = (90 * Math.PI / 180);
                            int enemiesCreated = 0;
                            for (int i = 0; i < numEnemies; i++) {
                                gameEngine.addGameObject(new Enemy(centreX + radius * (float) Math.sin(angle), centreY + radius * (float) Math.cos(angle)));
                                angle += angleToAdd;
                                enemiesCreated++;

                            }

                            Log.d("GameManager", enemiesCreated + " enemies created");
                        } else if (mPattern == 1) {

                            int enemies = r.nextInt(3)+3;

                            float spacing = 200;

                            float lengthX = (enemies * spacing) - spacing;

                            float startX = centreX - (lengthX / 2);

                            for (int i = 0; i < enemies; i++) {
                                gameEngine.addGameObject(new Enemy(startX, -200));
                                startX += spacing;
                            }

                        }
                    }
                }
            }
            else {
                if (gameEngine.mInputController.getTouched()) {
                    PointF newPoint = gameEngine.mInputController.getTouchPoint();
                    if (hb.checkClick((int)newPoint.x,(int)newPoint.y)){
                        Log.d("gm","clicked inside");
                    }
                }
            }
        }

    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255, 255, 255, 0));
        canvas.drawText("Lives: "+mPlayerLives, 20, 30, paint);

        if (!isRunning()){
            hb.onDraw(paint,canvas);
            paint.setTextSize(100);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawText("GAME OVER", 680, 400, paint);
        }
    }


    public boolean getTimeFreezeActivated(){
        return mTimeFreezeActivated;
    }

    public void setTimeFreeze(boolean state){
        mTimeFreezeActivated = state;
    }

    public void playerLoseLife(){
        mPlayerLives--;

        if (mPlayerLives <= 0){

            hb = new HomeButton();
            Log.d("gm","home button created");
            Log.d("gm","hb:"+hb.button.toString());
            running = false;
        }
    }

    public boolean isRunning(){
        return running;
    }

}
