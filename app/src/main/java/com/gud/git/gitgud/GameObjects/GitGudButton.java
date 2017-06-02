package com.gud.git.gitgud.GameObjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.gud.git.gitgud.App;
import com.gud.git.gitgud.Engine.Renderable;
import com.gud.git.gitgud.Managers.GameManager;
import com.gud.git.gitgud.R;

/**
 * Created by Nue on 5/31/2017.
 */

public class GitGudButton implements Renderable{

    public Bitmap mBitmap;

    private int mWidth,mHeight;
    private int mOffsetX,mOffsetY;
    private float mPositionX,mPositionY;
    public Rect button;

    public GitGudButton(String imageName,int x,int y, int xscale, int yscale){
        Resources res = App.getContext().getResources();
        mBitmap = BitmapFactory.decodeResource(res, res.getIdentifier(imageName , "drawable", App.getContext().getPackageName()));

        mPositionX = x;
        mPositionY = y;

        mWidth = mBitmap.getWidth() - xscale;
        mHeight = mBitmap.getHeight() - yscale;

        mBitmap = Bitmap.createScaledBitmap(mBitmap,mWidth,mHeight,true);

        mOffsetX = mWidth/2;
        mOffsetY = mHeight/2;

        button = new Rect ((int)(mPositionX - mOffsetX),(int) (mPositionY - mOffsetX),(int) (mPositionX + mOffsetX),(int) (mPositionY + mOffsetY));

    }

    @Override
    public void onDraw(Paint paint, Canvas canvas){

        canvas.drawBitmap(mBitmap, mPositionX - mOffsetX, mPositionY - mOffsetY, paint);
    }

    public boolean checkClick(int x, int y){

        return button.contains(x,y);
    }

}
