package com.gud.git.gitgud.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.gud.git.gitgud.App;
import com.gud.git.gitgud.Engine.Circle;
import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.GameObject;
import com.gud.git.gitgud.R;

/**
 * Created by Nue on 5/17/2017.
 */

public class Enemy extends GameObject {

    private int mWidth,mHeight;

    private double mPixelFactor;

    float mPositionX,mPositionY,mRadius;
    double mSpeedFactor;

    float maxSpeedNormal,maxSpeedTimeFreeze;

    final float MAX_SPEED_NORMAL = 1f;

    Circle mHitbox;
    public Bitmap mEnemyBitmap;

    public Enemy() {
        mPositionX = 0;
        mPositionY = 450;
        mRadius = 100;

        mHitbox = new Circle(mPositionX,mPositionY,mRadius);

        maxSpeedNormal = 0.1f;

        Resources res = App.getContext().getResources();
        //mEnemyBitmap = BitmapFactory.decodeResource(res, R.drawable.player);
    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){
        paint.setColor(Color.argb(255, 0, 255,0));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mPositionX,mPositionY,mRadius,paint);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){
        if (elapsedMillis >= 1) {
            if (mPositionX <= 0) {
                maxSpeedNormal = MAX_SPEED_NORMAL * elapsedMillis;
            } else if (mPositionX >= 1920) {
                maxSpeedNormal = -MAX_SPEED_NORMAL * elapsedMillis;
            }

            mPositionX += maxSpeedNormal;
            mHitbox.moveCircle(mPositionX, mPositionY);
        }

    }

    @Override
    public Circle getHitbox(){
        return mHitbox;
    }

    @Override
    public boolean checkCollision(GameObject other) {
        return false;
    }

    public void enemyDie(){

    }

}
