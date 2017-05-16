package com.gud.git.gitgud.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.GameObject;
import com.gud.git.gitgud.Engine.Renderable;
import com.gud.git.gitgud.Engine.Updateable;
import com.gud.git.gitgud.R;


/**
 * Created by Nue on 5/15/2017.
 */

public class Player extends GameObject implements Renderable,Updateable{

   //private final View mView;

    private int mMaxX,mMaxY;
    private double mPixelFactor;

    double mPositionX,mPositionY;
    double mSpeedFactor;





    //A collision thing
    //elipse 2d

    //todo:animated?
    Bitmap mPlayerBitmap;

    //An image thing


    public Player(){
        //mView = view;

        //todo:add player image
        //mPlayerBitmap = view.getContext().getResources().getDrawable(R.drawable.ship);
        Resources res = getResources();
        mPlayerBitmap = BitmapFactory.decodeResource(res,R.drawable.ship);

        mMaxX -= (mPlayerBitmap.getWidth());
        mMaxY -= (mPlayerBitmap.getHeight());

        mPositionX = 512;
        mPositionY = 512;

    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){
        //canvas.drawBitmap();
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine){

    }

}
