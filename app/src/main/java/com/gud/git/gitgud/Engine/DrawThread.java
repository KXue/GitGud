package com.gud.git.gitgud.Engine;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KevinXue on 5/15/2017.
 */

public class DrawThread extends Thread{
    private final GameEngine mGameEngine;
    private boolean mGameIsRunning = true;
    private boolean mPauseGame = false;
    private final int FPS = 60;

    private Object mLock = new Object();

    public DrawThread(GameEngine gameEngine) {
        mGameEngine = gameEngine;
    }

    @Override
    public void start() {
        mGameIsRunning = true;
        mPauseGame = false;
        super.start();
    }

    public void stopGame() {
        mGameIsRunning = false;
        resumeGame();
    }

    @Override
    public void run(){
        long elapsedMillis;
        long currentTimeMillis;
        long previousTimeMillis = System.currentTimeMillis();
        int timeBetweenFrames = 1000/FPS;
        while(mGameIsRunning){
            currentTimeMillis = System.currentTimeMillis();
            elapsedMillis = currentTimeMillis - previousTimeMillis;
            if(mPauseGame){
                while(mPauseGame){
                    try {
                        synchronized (mLock){
                            mLock.wait();
                        }
                    } catch (InterruptedException e){

                    }
                }
                currentTimeMillis = System.currentTimeMillis();
            }
            if(elapsedMillis > timeBetweenFrames){
                mGameEngine.onDraw();
                previousTimeMillis = currentTimeMillis;
            }
            else{
                try {
                    Thread.sleep(timeBetweenFrames-elapsedMillis);
                } catch (InterruptedException e) {
                }
            }
        }
    }


    public void pauseGame(){
        mPauseGame = true;
    }
    public void resumeGame(){
        if (mPauseGame == true) {
            mPauseGame = false;
            synchronized (mLock) {
                mLock.notify();
            }
        }
    }
    public boolean isGameRunning() {
        return mGameIsRunning;
    }

    public boolean isGamePaused() {
        return mPauseGame;
    }
}
