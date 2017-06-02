package com.gud.git.gitgud.Managers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.gud.git.gitgud.App;
import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.Renderable;
import com.gud.git.gitgud.Engine.Updateable;
import com.gud.git.gitgud.Fragments.GameFragment;
import com.gud.git.gitgud.GameObjects.Enemy;
import com.gud.git.gitgud.GameObjects.GitGudButton;

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
    private boolean mRunning;
    private GitGudButton mHomeButton;
    private GitGudButton mPauseButton,mUnpauseButton;
    private GameFragment mFragment;
    private int mScore;
    private int mTimeFreezeTime;    //time freeze time in milliseconds
    private final int MAX_TIME_FREEZE = 5000;
    private final int MIN_SPAWN_TIME = 3000;
    private final int SPAWN_TIME_VARIANCE = 3000;
    private int mNextSpawnTime;

    private GameManager(){
        Log.d("GM","New GM created");
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){
        if (elapsedMillis >= 1){// game is updating
            if (isRunning()) {  //game is running

                //check time freeze button presses
                if (gameEngine.mInputController.getTouched()) {
                    PointF newPoint = gameEngine.mInputController.getTouchPoint();
                    if (mPauseButton.checkClick((int)newPoint.x,(int)newPoint.y)){
                        setTimeFreeze(true);
                    }
                    else if(mUnpauseButton.checkClick((int)newPoint.x,(int)newPoint.y)) {
                        setTimeFreeze(false);
                    }
                }

                if (!getTimeFreezeActivated()) {    //not time freezed


                    mTime += elapsedMillis;
                    mScore += elapsedMillis;

                    //refill time freeze
                    if (mTimeFreezeTime < MAX_TIME_FREEZE){
                        mTimeFreezeTime += elapsedMillis;
                        if (mTimeFreezeTime > MAX_TIME_FREEZE){
                            mTimeFreezeTime = MAX_TIME_FREEZE;
                        }
                    }

                    if (mTime >= mNextSpawnTime) {
                        mTime = 0;
                        int centreX = App.getScreenWidth()/2;
                        int centreY = App.getScreenHeight()/2;

                        //Log.d("gamemanager",""+centreX+" "+centreY);
                        Log.d("gamemanager", "pattern " + mPattern);
                        Random r = new Random();
                        mPattern = r.nextInt(2);

                        if (mPattern == 0) {

                            float radius = 1100;

                            int numEnemies = r.nextInt(8)+2; //could randomize


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

                            int enemies = r.nextInt(5)+3;

                            float spacing = 200;

                            float lengthX = (enemies * spacing) - spacing;

                            float startX = centreX - (lengthX / 2);

                            for (int i = 0; i < enemies; i++) {
                                gameEngine.addGameObject(new Enemy(startX, -200));
                                startX += spacing;
                            }

                        }
                        mNextSpawnTime = r.nextInt(SPAWN_TIME_VARIANCE)+MIN_SPAWN_TIME;
                    }

                }
                else{   //during time freeze
                    mTimeFreezeTime -= elapsedMillis;
                    if (mTimeFreezeTime <= 0){
                        setTimeFreeze(false);
                        mTimeFreezeTime = 0;
                    }
                }
            }
            else {
                if (gameEngine.mInputController.getTouched()) {
                    PointF newPoint = gameEngine.mInputController.getTouchPoint();
                    if (mHomeButton.checkClick((int)newPoint.x,(int)newPoint.y)){
                        Log.d("gm","clicked inside");
                        mFragment.startNewGame();
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
        canvas.drawText("Score: "+mScore, 20, 60, paint);
        canvas.drawText("Time Freeze: "+mTimeFreezeTime+" ms", 20, 90, paint);
        canvas.drawLine(200,0,200,1080,paint);
        canvas.drawLine(1700,0,1700,1080,paint);
        if (!isRunning()){
            mHomeButton.onDraw(paint,canvas);
            paint.setTextSize(100);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawText("GAME OVER", 680, 400, paint);
        }
        else{
            mPauseButton.onDraw(paint,canvas);
            mUnpauseButton.onDraw(paint,canvas);
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

            mHomeButton = new GitGudButton("home2", App.getScreenWidth()/2,App.getScreenHeight()/2+300,400,400);
            Log.d("gm","home button created");
            Log.d("gm","hb:"+mHomeButton.button.toString());
            mRunning = false;

        }
    }

    public boolean isRunning(){
        return mRunning;
    }

    public void setGameFragment(GameFragment fragment){
        mFragment = fragment;
        newGame();
    }

    public void newGame(){
        mTime = 0;
        mPattern = 0;
        mPlayerLives = 3;
        mTimeFreezeActivated = true;
        mRunning = true;
        mHomeButton = null;
        mScore = 0;
        mTimeFreezeTime = 5000;
        mNextSpawnTime = 0;
        Log.d("gm","next spawn time:"+mNextSpawnTime);

        mPauseButton = new GitGudButton("pause2",100,980,700,670);
        mUnpauseButton = new GitGudButton("play2",1820,980,700,670);
        Log.d("gm","pause button:"+mPauseButton.button.toString());

    }

}
