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
import com.gud.git.gitgud.Managers.GameManager;
import com.gud.git.gitgud.R;

import static com.gud.git.gitgud.R.drawable.bullet;

/**
 * Created by Nue on 5/17/2017.
 */

public class Enemy extends GameObject {

    private int mWidth,mHeight;

    private double mPixelFactor;
    private int mOffsetX,mOffsetY;

    float mPositionX,mPositionY,mRadius;
    double mSpeedFactor;
    int mMoveType;

    float mMaxSpeedNormal,mMaxSpeedTimeFreeze;

    final float MAX_SPEED_NORMAL = 1f;

    Circle mHitbox;
    public static Bitmap mEnemyBitmap;
    static boolean bitmapCreated = false;

    public Enemy(float startX, float startY, int moveType) {

        if (!bitmapCreated){
            bitmapCreated = true;

            Resources res = App.getContext().getResources();
            mEnemyBitmap = BitmapFactory.decodeResource(res,R.drawable.enemy);
            mWidth = mEnemyBitmap.getWidth();
            mHeight = mEnemyBitmap.getHeight();

            mWidth = 150;
            mHeight = 150;

            mEnemyBitmap = Bitmap.createScaledBitmap(mEnemyBitmap,mWidth,mHeight,true);
        }

        mWidth = 150;
        mHeight = 150;

        mPositionX = startX;
        mPositionY = startY;
        mRadius = mWidth*0.5f;

        mOffsetX = mEnemyBitmap.getWidth()/2;
        mOffsetY = mEnemyBitmap.getHeight()/2;

        mHitbox = new Circle(mPositionX,mPositionY,mRadius);

        mMaxSpeedNormal = 0.2f;
        mMoveType = moveType;

    }

    void moveTo(float x, float y, long elapsedMillis) {
        float dX = x - mPositionX;
        float dY = y - mPositionY;
        float distance = (float) Math.sqrt((dX * dX) + (dY * dY));


        if (distance != 0) {
            float vX = 0;
            float vY = 0;
            if (mMoveType == 0) {
                vX = (dX / distance) * mMaxSpeedNormal * elapsedMillis;
                vY = (dY / distance) * mMaxSpeedNormal * elapsedMillis;

            } else if (mMoveType == 1) {
                if (mPositionX > x){
                    vX = -mMaxSpeedNormal * elapsedMillis;
                }
                else if (mPositionX < x){
                    vX = mMaxSpeedNormal * elapsedMillis;
                }
                if (mPositionY > y){
                    vY = -mMaxSpeedNormal * elapsedMillis;
                }
                else if (mPositionY < y){
                    vY = mMaxSpeedNormal * elapsedMillis;
                }
            }

            if (Math.abs(vX) > Math.abs(dX)) {
                mPositionX = x;
            } else {
                mPositionX += vX;
            }

            if (Math.abs(vY) > Math.abs(dY)) {
                mPositionY = y;
            } else {
                mPositionY += vY;
            }
        }


        mHitbox.moveCircle(mPositionX, mPositionY);
    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){
        //draw the enemy
        canvas.drawBitmap(mEnemyBitmap, mPositionX - mOffsetX, mPositionY - mOffsetY, paint);

        paint.setColor(Color.argb(255, 0, 255,0));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mPositionX,mPositionY,mRadius,paint);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){
        if (elapsedMillis >= 1) {
            /*
            if (mPositionX <= 0) {
                maxSpeedNormal = MAX_SPEED_NORMAL * elapsedMillis;
            } else if (mPositionX >= 1920) {
                maxSpeedNormal = -MAX_SPEED_NORMAL * elapsedMillis;
            }
            */
            if (!gameEngine.getmGameManager().getTimeFreezeActivated()){
                moveTo(960,540,elapsedMillis);

                gameEngine.addGameObject(new  Bullet(mPositionX, mPositionY, 50, 50));

            }

        }

    }

    @Override
    public Circle getHitbox(){
        return mHitbox;
    }

    @Override
    public boolean checkCollision(GameObject other, GameManager gameManager) {
        return false;
    }

    public void enemyDie(){

    }

}
