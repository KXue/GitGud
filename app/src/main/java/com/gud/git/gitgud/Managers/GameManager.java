package com.gud.git.gitgud.Managers;

import android.util.Log;

import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.Updateable;
import com.gud.git.gitgud.GameObjects.Enemy;

/**
 * Created by Nue on 5/19/2017.
 */

/*
have an enumeration/int to name enemy patterns.

Pick the next pattern in order or at random

when x time has passed, spawn the pattern
 */

public class GameManager implements Updateable{

    long mTime;
    int mPattern;
    int mPlayerLives;
    boolean mTimeFreezeActivated;

    public GameManager(){
        mTime = 0;
        mPattern = 0;
        mPlayerLives = 100;
        mTimeFreezeActivated = true;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){
        if (elapsedMillis >= 1){
            if (!getTimeFreezeActivated()) {
                mTime += elapsedMillis;

                if (mTime >= 2000) {
                    mTime = 0;
                    int centreX = 960;
                    int centreY = 540;

                    //Log.d("gamemanager",""+centreX+" "+centreY);
                    Log.d("gamemanager", "pattern " + mPattern);

                    if (mPattern == 0) {

                        float radius = 1500;

                        int numEnemies = 6; //could randomize


                        float angleToAdd = (float) (360 / numEnemies * Math.PI / 180);
                        float angle = angleToAdd;
                        int enemiesCreated = 0;


                        for (int i = 0; i < numEnemies; i++) {
                            gameEngine.addGameObject(new Enemy(centreX + radius * (float) Math.sin(angle), centreY + radius * (float) Math.cos(angle), 0));
                            angle += angleToAdd;
                            enemiesCreated++;
                        }

                      //  Log.d("GameManager", enemiesCreated + " enemies created");
                    } else if (mPattern == 1) {
                        float width = 1600;
                        float height = 1600;

                        int enemies = 8;

                        float spacing = 200;

                        float lengthX = (enemies * spacing) - spacing;

                        float startX = centreX - (lengthX / 2);

                        for (int i = 0; i < enemies; i++) {
                            gameEngine.addGameObject(new Enemy(startX, -200, 1));
                            startX += spacing;
                        }

                    }
                }
            }
        }
    }


    public boolean getTimeFreezeActivated(){
        return mTimeFreezeActivated;
    }

    public void setTimeFreeze(boolean state){
        mTimeFreezeActivated = state;
    }



}
