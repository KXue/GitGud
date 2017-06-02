package com.gud.git.gitgud.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.gud.git.gitgud.App;
import com.gud.git.gitgud.Engine.Collideable;
import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.GameObject;
import com.gud.git.gitgud.Engine.Renderable;
import com.gud.git.gitgud.Engine.Updateable;
import com.gud.git.gitgud.Managers.GameManager;
import com.gud.git.gitgud.R;
import com.gud.git.gitgud.Engine.Circle;


/**
 * Created by Fires on 2017-05-18.
 */

public class Bullet extends GameObject implements Renderable,Updateable {

    private int mWidth, mHeight;

    private int mOffsetX,mOffsetY;
    private PointF mSimulationStartPoint;

    private float mPositionX, mPositionY, mRadius;

    private float mMaxSpeed;
    private long mDelay;

    private float mLifeTime; // remove this game object after a certain amount of time

    //bullet visual projection
    private static Bitmap mBulletBitmap;
    private static boolean bitmapCreated = false;

    private Circle mHitbox;
    private PointF mDirection;


    //speed should be below 0.5f or so
    public Bullet(float startX, float startY, float dirX, float dirY, float speed, long delay){

        //Log.d("bullet cons","start:"+startX+" "+startY);
        //Log.d("bullet cons","end:"+dirX+" "+dirY);
        mWidth = 25;
        mHeight = 25;

        if (!bitmapCreated) {
            bitmapCreated = true;

            Resources res = App.getContext().getResources();
            mBulletBitmap = BitmapFactory.decodeResource(res,R.drawable.bullet);
            mBulletBitmap = Bitmap.createScaledBitmap(mBulletBitmap, mWidth, mHeight, true);
        }

        mPositionX = startX;
        mPositionY = startY;
        mRadius = mWidth * 0.5f;


        //mWidth = mBulletBitmap.getWidth();
        //mHeight = mBulletBitmap.getHeight();


        mOffsetX = mBulletBitmap.getWidth()/2;
        mOffsetY = mBulletBitmap.getHeight()/2;

        mMaxSpeed = speed;
        mDelay = delay;

        mLifeTime = 7000; //7 seconds moving across the screen diagonally at 0.3f

        mHitbox = new Circle(mPositionX,mPositionY,10);

        mDirection = new PointF();
        mDirection.x = dirX;
        mDirection.y = dirY;
        /*
        float distanceX = endX - startX;
        float distanceY = endY - startY;
        float magnitude = (float)Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        mDirection = new PointF();
        if(magnitude == 0) {
            mDirection.x = 0;
            mDirection.y = 0;
        }else{
            mDirection.x = distanceX / magnitude;
            mDirection.y = distanceY / magnitude;
        }
        */


    }



    //spawn yo bullets
    public void setMaxSpeedNormal(float mMaxSpeed){
        this.mMaxSpeed = mMaxSpeed;
    }



    @Override
    public void onDraw(Paint paint, Canvas canvas){
        canvas.drawBitmap(mBulletBitmap,mPositionX - mOffsetX,mPositionY - mOffsetY,paint);

        paint.setColor(Color.argb(255, 0, 255,0));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mPositionX,mPositionY,mRadius,paint);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){



        if (!GameManager.getInstance().getTimeFreezeActivated()) {
            mDelay -= elapsedMillis;

            if (mPositionX < 0 || mPositionX > 1920 || mPositionY < 0 || mPositionY > 1080 || mLifeTime <= 0) {

                gameEngine.removeGameObject(this);
            }

            if (mDelay <= 0) {
                mLifeTime -= elapsedMillis;

                mPositionX += mMaxSpeed * mDirection.x * elapsedMillis;
                mPositionY += mMaxSpeed * mDirection.y * elapsedMillis;

                mHitbox.moveCircle(mPositionX, mPositionY);
            }
        }

    }

    @Override
    public boolean checkCollision(Collideable other) {
        return false;
    }

    public Circle getHitbox(){
        return mHitbox;
    }
}
