package com.gud.git.gitgud.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gud.git.gitgud.GameObjects.Player;
import com.gud.git.gitgud.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KevinXue on 5/15/2017.
 */

public class GameEngine {
    private Paint mPaint;
    private Canvas mCanvas;
    private SurfaceHolder mDrawSurfaceHolder;

    private List<GameObject> mGameObjects = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToRemove = new ArrayList<GameObject>();
    private UpdateThread mUpdateThread;
    private DrawThread mDrawThread;

    private int screenWidth, screenHeight;

    public GameEngine(){

        mPaint = new Paint();
    }
    public void startGame(){
        stopGame();

//        int numGameObjects = mGameObjects.size();
//        for(int i = 0; i < numGameObjects; i++){
//            mGameObjects.get(i).startGame();
//        }

        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();

        mDrawThread = new DrawThread(this);
        mDrawThread.start();
    }
    public void stopGame() {
        if(mUpdateThread != null){
            mUpdateThread.stopGame();
        }
        if(mDrawThread != null){
            mDrawThread.stopGame();
        }
    }
    //must be called to
    public void setDrawSurfaceHolder(SurfaceHolder surfaceHolder){
        mDrawSurfaceHolder = surfaceHolder;
    }

    public void addGameObject(GameObject obj){
        mObjectsToAdd.add(obj);
    }
    public void removeGameObject(GameObject obj){
        mObjectsToRemove.add(obj);
    }

    public void onUpdate(long elapsedMillis) {
        int numGameObjects = mGameObjects.size();
        for (int i=0; i<numGameObjects; i++) {
            mGameObjects.get(i).onUpdate(elapsedMillis, this);
        }
        synchronized (mGameObjects) {
            while (!mObjectsToRemove.isEmpty()) {
                mGameObjects.remove(mObjectsToRemove.remove(0));
            }
            while (!mObjectsToAdd.isEmpty()) {
                mGameObjects.add(mObjectsToAdd.remove(0));
            }
        }
    }


    public void onDraw() {
        if(mDrawSurfaceHolder.getSurface().isValid()){
            mCanvas = mDrawSurfaceHolder.lockCanvas();
            mPaint.setColor(Color.argb(255, 0, 0,0));
            mCanvas.drawColor(Color.argb(255, 0, 0,0));
            int numGameObjects = mGameObjects.size();
//            Log.d("Engine", numGameObjects + "");
            for(int i = 0; i < numGameObjects; i++){
                mGameObjects.get(i).onDraw(mPaint, mCanvas);
            }
            mDrawSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }
}
