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

    private List<GameObject> mGameObjects = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToRemove = new ArrayList<GameObject>();
    private SpatialHash mCollisionSpatialHash = new SpatialHash(300);
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
        GameManager.getInstance().onUpdate(elapsedMillis,this);
        int numGameObjects = mGameObjects.size();
        for(GameObject o : mGameObjects){
            mCollisionSpatialHash.insertObject(o);
        }
        for(GameObject o : mGameObjects){
            for(GameObject p : mCollisionSpatialHash.getPotentialColliders(o)){
                o.checkCollision(p);
            }
        }
        mCollisionSpatialHash.clear();
        for (int i=0; i<numGameObjects; i++) {
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
//        checkCollision();
    }


    public void onDraw() {
        if(mDrawSurfaceHolder.getSurface().isValid()){
            mCanvas = mDrawSurfaceHolder.lockCanvas();
            mPaint.setColor(Color.argb(255, 100, 100, 100));
            mCanvas.drawColor(mPaint.getColor());

            //int numGameObjects = mGameObjects.size();
            //Log.d("gameEngine onDraw START","numGameObjects:"+mNumGameObjects);
            for(int i = 1; i < mNumGameObjects; i++){
                //Log.d("gameEngine onDrawonDraw","numGameObjects:"+mNumGameObjects);
                mGameObjects.get(i).onDraw(mPaint, mCanvas);

            }
            //Log.d("gameEngine onDraw","END");

            mGameObjects.get(0).onDraw(mPaint, mCanvas);

            mDrawSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }
    public Player getMplayer () {return mplayer;}
    public void checkCollision(){

        int numGameObjects = mGameObjects.size();
        for (int i=0; i<numGameObjects-1; i++) {
            for (int j=i+1; j<numGameObjects; j++) {
                //Log.d("checkCollision","j="+j);
                //Log.d("checkCollision","check "+i+" "+j);
                GameManager manager = GameManager.getInstance();
                if (mGameObjects.get(i).checkCollision(mGameObjects.get(j))){

                    if (manager.getTimeFreezeActivated()){
                        removeGameObject(mGameObjects.get(j));
                    }
                    else{

                    }
                    //Log.d("checkCollision",mGameObjects.get(j).toString());
                    //Log.d("checkCollision","remove gameobject index "+j);
                    //Log.d("checkCollision","gameobjects remaining:"+numGameObjects);

                }

            }
        }
    }

    public void setPlayer(Player p){
        mPlayer = p;
    }

    public Player getPlayer(){
        return mPlayer;
    }
}
