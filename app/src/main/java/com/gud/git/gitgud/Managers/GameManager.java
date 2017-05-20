package com.gud.git.gitgud.Managers;

import android.util.Log;

import com.gud.git.gitgud.App;
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
    int pattern;

    public GameManager(){
        mTime = 0;
        int pattern = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){
        if (elapsedMillis >= 1){
            mTime += elapsedMillis;

            if (mTime >= 10000){
                mTime = 0;
                int centreX = 960;
                int centreY = 540;


                Log.d("gamemanager",""+centreX+" "+centreY);

                if (pattern == 0) {


                    float radius = 2500;

                    int numEnemies = 6; //could randomize

                    float angleToAdd = (float) (360/numEnemies * Math.PI / 180);
                    float angle = angleToAdd;

                    for (int i = 0; i < numEnemies; i++) {
                        gameEngine.addGameObject(new Enemy(centreX + radius * (float) Math.sin(angle), centreY + radius * (float) Math.cos(angle)));
                        angle += angleToAdd;
                        gameEngine.addGameObject(new Enemy(centreX + radius * (float) Math.sin(angle), centreY + radius * (float) Math.cos(angle)));
                        angle += angleToAdd;
                        gameEngine.addGameObject(new Enemy(centreX + radius * (float) Math.sin(angle), centreY + radius * (float) Math.cos(angle)));
                        angle += angleToAdd;
                        gameEngine.addGameObject(new Enemy(centreX + radius * (float) Math.sin(angle), centreY + radius * (float) Math.cos(angle)));
                        angle += angleToAdd;
                        gameEngine.addGameObject(new Enemy(centreX + radius * (float) Math.sin(angle), centreY + radius * (float) Math.cos(angle)));
                        angle += angleToAdd;
                        gameEngine.addGameObject(new Enemy(centreX + radius * (float) Math.sin(angle), centreY + radius * (float) Math.cos(angle)));
                    }
                }
                else if (pattern == 1){
                    float width = 1600;
                    float height = 1600;

                    //gameEngine.addGameObject(new Enemy());
                }
            }
        }
    }
}
