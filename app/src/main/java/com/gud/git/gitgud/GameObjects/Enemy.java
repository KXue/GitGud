package com.gud.git.gitgud.GameObjects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gud.git.gitgud.Engine.Circle;
import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.GameObject;

/**
 * Created by Nue on 5/17/2017.
 */

public class Enemy extends GameObject {

    private int mWidth,mHeight;

    private double mPixelFactor;

    float mPositionX,mPositionY,mRadius;
    double mSpeedFactor;

    float maxSpeedNormal,maxSpeedTimeFreeze;

    Circle mHitbox;

    public Enemy() {
        mPositionX = 0;
        mPositionY = 400;

        mHitbox = new Circle(mPositionX,mPositionY,10);

        maxSpeedNormal = 5;
    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){
        canvas.drawCircle(mPositionX,mPositionY,mRadius,paint);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){

        if (mPositionX <= 0){
            maxSpeedNormal = 5;
        }
        else if (mPositionX >= 1920){
            maxSpeedNormal = -5;
        }

        mPositionX += maxSpeedNormal;
        mHitbox.moveCircle(mPositionX,mPositionY);

    }
}
