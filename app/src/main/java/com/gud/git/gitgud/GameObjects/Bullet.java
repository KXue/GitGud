package com.gud.git.gitgud.GameObjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.gud.git.gitgud.App;
import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.GameObject;
import com.gud.git.gitgud.Engine.Renderable;
import com.gud.git.gitgud.Engine.Updateable;
import com.gud.git.gitgud.Managers.GameManager;
import com.gud.git.gitgud.R;
import com.gud.git.gitgud.Engine.Circle;
import java.util.Random;

import static android.R.attr.bitmap;


/**
 * Created by Fires on 2017-05-18.
 */

public class Bullet extends GameObject implements Renderable,Updateable {
    //bullet visual projection
    private Bitmap mBulletBitmap;
    //general speed for bullets moving across the screen
    float mMaxSpeed,maxSpeedTimeFreeze;
    private int mWidth, mHeight;
    float mPositionX, mPositionY, mRadius;
    Circle mHitbox;
    PointF mVelocity;


    public Bullet(float startX, float startY, float endX, float endY){
        Resources res = App.getContext().getResources();
        mBulletBitmap = BitmapFactory.decodeResource(res,R.drawable.bullet);
        mHitbox = new Circle(mPositionX,mPositionY,10);
        mWidth = mBulletBitmap.getWidth();
        mHeight = mBulletBitmap.getHeight();
        mPositionX = startX;
        mPositionY = startY;
        mWidth = 25;
        mHeight = 50;
        mRadius = 5;
        mBulletBitmap = Bitmap.createScaledBitmap(mBulletBitmap,mWidth,mHeight,true);
        mMaxSpeed = 5f;
        mHitbox = new Circle(mPositionX,mPositionY,10);
        float distanceX = endX - startX;
        float distanceY = endY - startY;
        float magnitude = (float)Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        mVelocity = new PointF();
        if(magnitude == 0) {
            mVelocity.x = 0;
            mVelocity.y = 0;
        }else{
            mVelocity.x = distanceX / magnitude;
            mVelocity.y = distanceY / magnitude;
        }


    }



    //spawn yo bullets
    public void setMaxSpeedNormal(float mMaxSpeed){
        this.mMaxSpeed = mMaxSpeed;
    }



    @Override
    public void onDraw(Paint paint, Canvas canvas){
        canvas.drawBitmap(mBulletBitmap,mPositionX,mPositionY,paint);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){
     //  mMaxSpeed = (100f/1000f) * elapsedMillis;

        if (mPositionX <= 0){

                gameEngine.removeGameObject(this);

           // maxSpeedNormal = (5f / 1000f) * elapsedMillis;
        }



        else if (mPositionX >= 1920){
            gameEngine.removeGameObject(this);


          //  maxSpeedNormal = (-5f / 1000f) * elapsedMillis;
        }


        mPositionX += mMaxSpeed * mVelocity.x;
        mPositionY += mMaxSpeed * mVelocity.y;

        mHitbox.moveCircle(mPositionX,mPositionY);

    }

    public boolean checkCollision(GameObject other, GameManager gameManager){
        return false;
    }
    public Circle getHitbox(){
        return mHitbox;
    }
}
