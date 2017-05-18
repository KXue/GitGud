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

import com.gud.git.gitgud.GameObjects.Enemy;
import com.gud.git.gitgud.GameObjects.Player;
import com.gud.git.gitgud.Input.InputController;
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

    public InputController mInputController;

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
        if (numGameObjects == 2 && elapsedMillis > 0) {
            Player a = (Player) mGameObjects.get(0);
            Enemy e = (Enemy) mGameObjects.get(1);
            if (a.playerCheckCollision(e.getEnemyHitbox())){
                a.collidedTrue();
                Log.d("Collided","yes");
            }
            else{
                a.collidedFalse();
            }
            //Log.d("Collided", "" + a.playerCheckCollision(e.getEnemyHitbox()));
        }
    }


    public void onDraw() {
        if(mDrawSurfaceHolder.getSurface().isValid()){
            mCanvas = mDrawSurfaceHolder.lockCanvas();
            mPaint.setColor(Color.argb(255, 100, 100, 100));
            mCanvas.drawColor(mPaint.getColor());

            int numGameObjects = mGameObjects.size();
            for(int i = 0; i < numGameObjects; i++){
                mGameObjects.get(i).onDraw(mPaint, mCanvas);
            }


            mDrawSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }
}
