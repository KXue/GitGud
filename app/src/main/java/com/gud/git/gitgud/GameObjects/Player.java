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
import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.GameObject;
import com.gud.git.gitgud.Engine.Renderable;
import com.gud.git.gitgud.Engine.Updateable;
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
    private double mPixelFactor;
    private int mOffsetX,mOffsetY;

    float mPositionX,mPositionY,mRadius;

    float mSpeedFactor;

    float mMaxSpeedNormal,mMaxSpeedTimeFreeze;

    boolean touchingMove;

    Circle mHitbox;

    boolean mIsInvincible;

    // Invincible time in milliseconds
    long mInvincibleTime;
    final long INVINCIBLE_TIME = 3000;

    int zero = 0;


    //A collision thing
    //elipse 2d

    //todo:animated?
    public Bitmap mPlayerBitmap;

    //An image thing
    boolean collided;

    public Player(){

        //float scaleFactor = 200/1920;

        Resources res = App.getContext().getResources();
        mPlayerBitmap = BitmapFactory.decodeResource(res,R.drawable.player);

        //float scaleFactor = ?; //todo:scaling

        mWidth = mPlayerBitmap.getWidth();
        mHeight = mPlayerBitmap.getHeight();

        mWidth = 150;
        mHeight = 150;

        mPlayerBitmap = Bitmap.createScaledBitmap(mPlayerBitmap,mWidth,mHeight,true);

        Log.d("player width",""+mWidth);
        Log.d("player height",""+mWidth);

        mMaxX = (App.getScreenWidth() - mPlayerBitmap.getWidth()/2);
        mMaxY = (App.getScreenHeight() - mPlayerBitmap.getHeight()/2);

        mMinX = 0 + mPlayerBitmap.getWidth()/2;
        mMinY = 0 + mPlayerBitmap.getHeight()/2;

        mPositionX = 512;
        mPositionY = 700;

        mOffsetX = mPlayerBitmap.getWidth()/2;
        mOffsetY = mPlayerBitmap.getHeight()/2;

        mRadius = mWidth*0.5f;

        mHitbox = new Circle(mPositionX,mPositionY,mRadius);

        mSpeedFactor = 0.001f;
        mMaxSpeedNormal = 2f;

        touchingMove = false;

        collided = false;

        mIsInvincible = true;
        mInvincibleTime = INVINCIBLE_TIME;
    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){
        if (mIsInvincible){
            paint.setAlpha(50);
        }
        else {
            paint.setAlpha(255);
        }

        canvas.drawBitmap(mPlayerBitmap, mPositionX - mOffsetX, mPositionY - mOffsetY, paint);

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
            if (mInvincibleTime <= 0) {
                mIsInvincible = false;
            }
        }
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

    public void collidedTrue(){
        collided = true;
    }

    public void collidedFalse(){
        collided = false;
    }

    public void playerDie(){
        if (!mIsInvincible) {
            mPositionX = App.getScreenWidth() / 2;
            mPositionY = App.getScreenHeight() / 2;
            mHitbox.moveCircle(mPositionX,mPositionY);
            mIsInvincible = true;
            mInvincibleTime = INVINCIBLE_TIME;
        }
    }

}
