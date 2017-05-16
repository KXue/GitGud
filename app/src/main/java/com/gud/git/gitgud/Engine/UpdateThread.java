package com.gud.git.gitgud.Engine;

/**
 * Created by KevinXue on 5/15/2017.
 */

public class UpdateThread extends Thread {
    private boolean mGameIsRunning;
    private GameEngine mGameEngine;
    private boolean mPauseGame;
    private Object mLock = new Object();

    public UpdateThread(GameEngine gameEngine) {
        mGameEngine = gameEngine;
    }

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
        long previousTimeMillis;
        long currentTimeMillis;
        long elapsedMillis;
        previousTimeMillis = System.currentTimeMillis();

        while(mGameIsRunning){
            currentTimeMillis = System.currentTimeMillis();
            elapsedMillis = currentTimeMillis - previousTimeMillis;
            if(mPauseGame){
                while(mPauseGame){
                    try{
                        synchronized (mLock) {
                            mLock.wait();
                        }
                    }catch (InterruptedException e){

                    }
                }
                currentTimeMillis = System.currentTimeMillis();
            }
            mGameEngine.onUpdate(elapsedMillis);
            previousTimeMillis = currentTimeMillis;
        }
    }
    public void pauseGame(){
        mPauseGame = true;
    }
    public void resumeGame(){
        if(mPauseGame){
            mPauseGame = false;
            synchronized (mLock){
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
