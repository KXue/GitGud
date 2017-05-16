package com.gud.git.gitgud.Engine;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KevinXue on 5/15/2017.
 */

public class DrawThread {
    private static int EXPECTED_FPS=30;
    private static final long TIME_BETWEEN_DRAWS = 1000/EXPECTED_FPS;
    private Timer mTimer;
    private GameEngine mGameEngine;

    public DrawThread(GameEngine gameEngine) {
        mGameEngine = gameEngine;
    }

    public void start() {
        stopGame();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask(){
            @Override
            public void run(){
                mGameEngine.onDraw();
            }
        }, 0, TIME_BETWEEN_DRAWS);
    }

    public void stopGame() {
        if(mTimer != null){
            mTimer.cancel();
            mTimer.purge();
        }
    }

    public void pauseGame(){
        stopGame();
    }
    public void resumeGame(){
        start();
    }
}
