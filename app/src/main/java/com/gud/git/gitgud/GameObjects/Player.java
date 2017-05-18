package com.gud.git.gitgud.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private int mMaxX,mMaxY;
    private int mWidth,mHeight;
    private double mPixelFactor;
    private int mOffsetX,mOffsetY;

    float mPositionX,mPositionY,mRadius;
    float mSpeedFactor;

    float mMaxSpeedNormal,mMaxSpeedTimeFreeze;

    boolean touchingMove;

    Circle mHitbox;




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

        mWidth = 200;
        mHeight = 200;

        mPlayerBitmap = Bitmap.createScaledBitmap(mPlayerBitmap,mWidth,mHeight,true);

        Log.d("player width",""+mWidth);
        Log.d("player height",""+mWidth);

        mMaxX = (App.getScreenWidth() - mPlayerBitmap.getWidth());
        mMaxY = (App.getScreenHeight() - mPlayerBitmap.getHeight());

        mPositionX = 512;
        mPositionY = 700;

        mOffsetX = mPlayerBitmap.getWidth()/2;
        mOffsetY = mPlayerBitmap.getHeight()/2;

        mRadius = 100;

        mHitbox = new Circle(mPositionX,mPositionY,mRadius);

        mSpeedFactor = 0.001f;
        mMaxSpeedNormal = 2f;

        touchingMove = false;
        collided = false;
    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){
        canvas.drawBitmap(mPlayerBitmap,mPositionX-mOffsetX,mPositionY-mOffsetY,paint);

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
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        //get input
        //move to
        MoveTo(1000,400);
        //if (gameEngine.inputcontroller.getouched){
        //moveTo(
    //}


    }

    void MoveTo(int x, int y){
        float dX = x - mPositionX;
        float dY = y - mPositionY;
        float distance = (float)Math.sqrt((dX*dX)+(dY*dY));

        if (distance != 0) {

            float vX = (dX / distance) * mSpeedFactor * mMaxSpeedNormal;
            float vY = (dY / distance) * mSpeedFactor * mMaxSpeedNormal;

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

}
