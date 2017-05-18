package com.gud.git.gitgud.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;


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

public class Player extends GameObject implements Renderable,Updateable{

    private int mMaxX,mMaxY;
    private int mWidth,mHeight;
    private double mPixelFactor;

    float mPositionX,mPositionY,mRadius;

    double mSpeedFactor;

    float maxSpeedNormal,maxSpeedTimeFreeze;

    boolean touchingMove;

    Circle mHitbox;



    //A collision thing
    //elipse 2d

    //todo:animated?
    public Bitmap mPlayerBitmap;

    //An image thing

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
        mPositionY = 512;
        mRadius = 5;

        mHitbox = new Circle(mPositionX,mPositionY,mRadius);

        maxSpeedNormal = 1f;
        touchingMove = false;

    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){
        canvas.drawBitmap(mPlayerBitmap,mPositionX,mPositionY,paint);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){
        if(gameEngine.mInputController.getTouched()){
            PointF newPoint = gameEngine.mInputController.getTouchPoint();
            MoveTo((int)newPoint.x, (int)newPoint.y);
        }
    }

    void MoveTo(int x, int y){
        mPositionX = x;
        mPositionY = y;
        mHitbox.moveCircle(x,y);
    }

}
