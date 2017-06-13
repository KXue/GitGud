package com.gud.git.gitgud.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gud.git.gitgud.GameObjects.Bullet;
import com.gud.git.gitgud.GameObjects.Enemy;
import com.gud.git.gitgud.GameObjects.Player;
import com.gud.git.gitgud.Input.InputController;
import com.gud.git.gitgud.Managers.GameManager;
import com.gud.git.gitgud.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KevinXue on 5/15/2017.
 */

public class GameEngine {

    private Player mplayer;
    private Paint mPaint;
    private Canvas mCanvas;
    private SurfaceHolder mDrawSurfaceHolder;

    private enum SimulationState{NONE, START, CONFIRM, CANCEL}

    private SimulationState mSimulationState = SimulationState.NONE;
    private List<GameObject> mGameObjects = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToRemove = new ArrayList<GameObject>();
    private SpatialHash mCollisionSpatialHash = new SpatialHash(200);
    private UpdateThread mUpdateThread;
    private DrawThread mDrawThread;

    private int mNumGameObjects;

    public InputController mInputController;

    public Player mPlayer;

    private int screenWidth, screenHeight;

    public GameEngine(){
        mPaint = new Paint();
    }
    public void startGame(){
        stopGame();

        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();

        mDrawThread = new DrawThread(this);
        mDrawThread.start();

        mNumGameObjects = 0;
    }
    public void stopGame() {
        if(mUpdateThread != null){
            mUpdateThread.stopGame();
        }
        if(mDrawThread != null){
            mDrawThread.stopGame();
        }
    }
    public void pauseGame() {
        if (mUpdateThread != null) {
            mUpdateThread.pauseGame();
        }
        if (mDrawThread != null) {
            mDrawThread.pauseGame();
        }
    }

    public void resumeGame() {
        if (mUpdateThread != null) {
            mUpdateThread.resumeGame();
        }
        if (mDrawThread != null) {
            mDrawThread.resumeGame();
        }
    }
    //must be called to
    public void setDrawSurfaceHolder(SurfaceHolder surfaceHolder){
        mDrawSurfaceHolder = surfaceHolder;
    }
    public void setInputController(InputController inputController){
        mInputController = inputController;
    }

    public void addGameObject(GameObject obj){
        mObjectsToAdd.add(obj);
    }
    public void removeGameObject(GameObject obj){
        mObjectsToRemove.add(obj);
    }

    public void onUpdate(long elapsedMillis) {
        if (GameManager.getInstance().isRunning()) {
            GameManager.getInstance().onUpdate(elapsedMillis, this);
            int numGameObjects = mGameObjects.size();
            if(mSimulationState != SimulationState.NONE){
                switch(mSimulationState){
                    case START:
                        for(GameObject object : mGameObjects){
                            object.beginSimulation();
                        }
                        break;
                    case CONFIRM:
                        for(GameObject object : mGameObjects){
                            object.confirmSimulation();
                        }
                        break;
                    case CANCEL:
                        for(GameObject object : mGameObjects){
                            object.cancelSimulation();
                        }
                        break;
                }
                mSimulationState = SimulationState.NONE;
            }

            for (int i = 0; i < numGameObjects; i++) {
                mGameObjects.get(i).onUpdate(elapsedMillis, this);
            }
            synchronized (mGameObjects) {
                while (!mObjectsToRemove.isEmpty()) {
                    mGameObjects.remove(mObjectsToRemove.remove(0));
                    mNumGameObjects--;
                    //Log.d("gameEngine","GameObject removed");
                }
                while (!mObjectsToAdd.isEmpty()) {
                    mGameObjects.add(mObjectsToAdd.remove(0));
                    mNumGameObjects++;
                }
            }
            checkCollision();
        }
        else{
            GameManager.getInstance().onUpdate(elapsedMillis, this);
        }
    }


    public void onDraw() {
        ArrayList<GameObject> copyObjects = new ArrayList<>(mGameObjects);
        if(mDrawSurfaceHolder.getSurface().isValid()){
            mCanvas = mDrawSurfaceHolder.lockCanvas();
            mPaint.setColor(Color.argb(255, 100, 100, 100));
            mCanvas.drawColor(mPaint.getColor());

            if (GameManager.getInstance().isRunning()) {    //draw gameobjects only when the game should be running. game objects wont be on the screen after losing
                for (int i = 0; i < copyObjects.size(); i++) {
                    copyObjects.get(i).onDraw(mPaint, mCanvas);
                }
            }

            GameManager.getInstance().onDraw(mPaint, mCanvas);
            mDrawSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void checkCollision() {
        GameManager manager = GameManager.getInstance();

        for (GameObject o : mGameObjects) {
            mCollisionSpatialHash.insertObject(o);
        }

//        for(GameObject p : mGameObjects){
//            for(GameObject o : mCollisionSpatialHash.getPotentialColliders(p)){
//               if(p.checkCollision(o)){
//                   removeGameObject(o);
//                   removeGameObject(p);
//               }
//            }
//        }

        for (GameObject p : mCollisionSpatialHash.getPotentialColliders(mPlayer)) {
            if (mPlayer.checkCollision(p)) {
                if (mPlayer.isSimulating()) {
                    if (p instanceof Enemy || p instanceof Bullet) {
                        removeGameObject(p);
                    }
                }
            }

        }
        mCollisionSpatialHash.clear();

    }

    public void setPlayer(Player p){
        mPlayer = p;
    }

    public Player getPlayer(){
        return mPlayer;
    }
    public boolean isRunning() {
        return mUpdateThread != null && mUpdateThread.isGameRunning();
    }
    public boolean isPaused() {
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }

    public void beginTimeStop() {
        mSimulationState = SimulationState.START;
    }

    public void cancelTimeStop() {
        mSimulationState = SimulationState.CANCEL;
    }

    public void confirmTimeStop() {
        mSimulationState = SimulationState.CONFIRM;
    }
}
