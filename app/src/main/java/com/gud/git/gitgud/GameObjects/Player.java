package com.gud.git.gitgud.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.gud.git.gitgud.App;
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
    private double mPixelFactor;
    float mPositionX,mPositionY;
    double mSpeedFactor;





    //A collision thing
    //elipse 2d

    //todo:animated?
    public Bitmap mPlayerBitmap;

    //An image thing

    public Player(){
        //mView = view;

        Log.d("Player", "height:" + App.getScreenHeight());
        //todo:add player image
        //mPlayerBitmap = view.getContext().getResources().getDrawable(R.drawable.ship);
        Resources res = App.getContext().getResources();
        mPlayerBitmap = BitmapFactory.decodeResource(res,R.drawable.player);
        mMaxX -= (mPlayerBitmap.getWidth());
        mMaxY -= (mPlayerBitmap.getHeight());
        mPositionX = 512;
        mPositionY = 512;
    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){
        canvas.drawBitmap(mPlayerBitmap,mPositionX,mPositionY,paint);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){

    }

}
