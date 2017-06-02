package com.gud.git.gitgud.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;


import com.gud.git.gitgud.App;
import com.gud.git.gitgud.Engine.Circle;
import com.gud.git.gitgud.Engine.Collideable;
import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.GameObject;
import com.gud.git.gitgud.Engine.Renderable;
import com.gud.git.gitgud.Engine.Updateable;
import com.gud.git.gitgud.Managers.GameManager;
import com.gud.git.gitgud.R;


/**
 * Created by Nue on 5/15/2017.
 */


/*
The positionX and positionY is the centre of the image.

Draw image at position - offset
*/

public class Player extends GameObject implements Renderable,Updateable{

    private int mMinX,mMinY;
    private int mMaxX,mMaxY;
    private int mWidth,mHeight;
    private int mOffsetX,mOffsetY;

    private float mPositionX,mPositionY,mRadius;

    float mSpeedFactor;

    float mMaxSpeedNormal,mMaxSpeedTimeFreeze;

    Circle mHitbox;

    boolean mIsInvincible;

    // Invincible time in milliseconds
    long mInvincibleTime;
    final long INVINCIBLE_TIME = 3000;

    //todo:animated?
    public Bitmap mPlayerBitmap;

    boolean collided; //debug collision

    public Player(){

        //float scaleFactor = 200/1920;

        Resources res = App.getContext().getResources();
        mPlayerBitmap = BitmapFactory.decodeResource(res,R.drawable.player);

        //float scaleFactor = ?; //todo:scaling

        mWidth = mPlayerBitmap.getWidth();
        mHeight = mPlayerBitmap.getHeight();

        mWidth = 75;
        mHeight = 75;

        mPlayerBitmap = Bitmap.createScaledBitmap(mPlayerBitmap,mWidth,mHeight,true);

        Log.d("player width",""+mWidth);
        Log.d("player height",""+mWidth);

        mMaxX = (App.getScreenWidth() - mPlayerBitmap.getWidth()/2);
        mMaxY = (App.getScreenHeight() - mPlayerBitmap.getHeight()/2);

        mMinX = 0 + mPlayerBitmap.getWidth()/2;
        mMinY = 0 + mPlayerBitmap.getHeight()/2;

        mPositionX = App.getScreenWidth() / 2;
        mPositionY = App.getScreenHeight() / 2 +200 ;

        mOffsetX = mPlayerBitmap.getWidth()/2;
        mOffsetY = mPlayerBitmap.getHeight()/2;

        mRadius = mWidth*0.5f;

        mHitbox = new Circle(mPositionX,mPositionY,mRadius);

        mSpeedFactor = 0.001f;
        mMaxSpeedNormal = 2f;

        mIsInvincible = true;
        mInvincibleTime = INVINCIBLE_TIME;

        collided = false; //for debug
    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){

        //invinicble change alpha
        if (mIsInvincible){
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(255, 255, 255, 0));
            canvas.drawText(""+mInvincibleTime, mPositionX - mOffsetX + 5, mPositionY - mOffsetY - 20, paint);
            paint.setAlpha(50);
        }
        else {
            paint.setAlpha(255);
        }

        //DRAW THE PLAYER
        canvas.drawBitmap(mPlayerBitmap, mPositionX - mOffsetX , mPositionY - mOffsetY , paint);


        //debug circle
        paint.setAlpha(255);

        if (collided){
            paint.setColor(Color.argb(255, 255, 0, 255));
        }
        else {
            paint.setColor(Color.argb(255, 0, 0, 255));
        }
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mPositionX,mPositionY,mRadius,paint);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){

        if (mIsInvincible) {
            mInvincibleTime -= elapsedMillis;
            //Log.d("invincible time", "" + mInvincibleTime);
            if (mInvincibleTime <= 0) {
                mIsInvincible = false;
            }
        }
//        temp timefreeze stuff
        /*
        if (mPositionX < 200){
            GameManager.getInstance().setTimeFreeze(true);
        }
        if (mPositionX > 1700){
            GameManager.getInstance().setTimeFreeze(false);
        }
        */

        if (gameEngine.mInputController.getTouched()) {
            PointF newPoint = gameEngine.mInputController.getTouchPoint();
            moveTo(newPoint.x, newPoint.y, elapsedMillis);
        }
    }

    void moveTo(float x, float y, long elapsedMillis){
        float dX = x - mPositionX;
        float dY = y - mPositionY;
        float distance = (float)Math.sqrt((dX*dX)+(dY*dY));

        if (distance != 0) {

            float vX = (dX / distance) * mMaxSpeedNormal * elapsedMillis;
            float vY = (dY / distance) * mMaxSpeedNormal * elapsedMillis;

            if (Math.abs(vX) > Math.abs(dX)){
                mPositionX = x;
            }
            else{
                mPositionX += vX;
            }

            if (Math.abs(vY) > Math.abs(dY)){
                mPositionY = y;
            }
            else{
                mPositionY += vY;
            }
        }

        if (mPositionX < mMinX){
            mPositionX = mMinX;
        }
        if (mPositionX > mMaxX){
            mPositionX = mMaxX;
        }
        if (mPositionY < mMinY){
            mPositionY = mMinY;
        }
        if (mPositionY > mMaxY){
            mPositionY = mMaxY;
        }

        mHitbox.moveCircle(mPositionX,mPositionY);
    }

    public boolean playerCheckCollision(Circle other){

        return mHitbox.intersect(other);
    }


    public void playerDie(){
        if (!mIsInvincible) {
            GameManager.getInstance().playerLoseLife();
            mPositionX = App.getScreenWidth() / 2;
            mPositionY = App.getScreenHeight() / 2 + 300;
            mHitbox.moveCircle(mPositionX,mPositionY);
            mIsInvincible = true;
            mInvincibleTime = INVINCIBLE_TIME;

        }
    }

    @Override
    public boolean checkCollision(Collideable other){
        boolean retVal = false;

        if (other instanceof Enemy){        //COLLIDED WITH ENEMY
            if (playerCheckCollision(other.getHitbox())){
                if (GameManager.getInstance().getTimeFreezeActivated()) {

                }
                else{
                    playerDie();    //NOT IN TIMEFREEZE AND TOUCHED AN ENEMY, PLAYER DIES
                }
                retVal = true;
            }
        }
        else if (other instanceof Bullet){      //COLLIDED WITH BULLET
            if (playerCheckCollision(other.getHitbox())) {      //PLAYER ALWAYS DIE WHEN COLLIDING WITH BULLET  (except when invincible which is checked in playerdie function)

                playerDie();
                retVal = true;
            }
        }
        else{
            //Log.d("Player","this is a game object and idk what it is");
        }
        return retVal;
    }

    @Override
    public Circle getHitbox(){
        return mHitbox;
    }

    public float getmPositionX(){
        return mPositionX;
    }
    public float getmPositionY(){
        return mPositionY;
    }

}
