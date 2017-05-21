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
        pattern = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){
        if (elapsedMillis >= 1){
            mTime += elapsedMillis;

            if (mTime >= 6000){
                mTime = 0;
                int centreX = 960;
                int centreY = 540;


                //Log.d("gamemanager",""+centreX+" "+centreY);
                Log.d("gamemanager","pattern "+pattern);

                if (pattern == 0) {


                    float radius = 1000;

                    int numEnemies = 120; //could randomize

                    float angleToAdd = (float) (360/numEnemies * Math.PI / 180);
                    float angle = angleToAdd;
                    int enemiesCreated = 0;


                    for (int i = 0; i < numEnemies; i++) {
                        gameEngine.addGameObject(new Enemy(centreX + radius * (float) Math.sin(angle), centreY + radius * (float) Math.cos(angle),0));
                        angle += angleToAdd;
                        enemiesCreated++;
                    }
                    Log.d("GameManager",enemiesCreated+" enemies created");
                }
                else if (pattern == 1){
                    float width = 1600;
                    float height = 1600;

                    int enemies = 4;



                    float spacing = 200;

                    float lengthX = (enemies * spacing) - spacing;


                    float startX = centreX - (lengthX /2);

                    for (int i = 0; i < enemies; i++){
                        gameEngine.addGameObject(new Enemy(startX,10,1));
                        startX += spacing;
                    }
                    //gameEngine.addGameObject(new Enemy());
                }
            }
        }
    }
}
