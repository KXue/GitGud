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
import com.gud.git.gitgud.Engine.Circle;
import com.gud.git.gitgud.Engine.Collideable;
import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Engine.GameObject;
import com.gud.git.gitgud.Managers.GameManager;
import com.gud.git.gitgud.R;

import java.util.Random;

/**
 * Created by Nue on 5/17/2017.
 */

public class Enemy extends GameObject {

    private int mWidth,mHeight;

    private int mOffsetX,mOffsetY;

    private float mPositionX,mPositionY,mRadius;
    private int mMoveType = 0;

    private float mMaxSpeed;

    private final long RELOAD_TIME = 1000;
    private long currentReload;

    private Circle mHitbox;
    private static Bitmap mEnemyBitmap;
    private static boolean bitmapCreated = false;
    private boolean isDead = false;

    private int bulletPattern;

    public Enemy(float startX, float startY) {

        mWidth = 75;
        mHeight = 75;

        if (!bitmapCreated){
            bitmapCreated = true;

            Resources res = App.getContext().getResources();
            mEnemyBitmap = BitmapFactory.decodeResource(res,R.drawable.enemy);
            //mWidth = mEnemyBitmap.getWidth();
            //mHeight = mEnemyBitmap.getHeight();

            mEnemyBitmap = Bitmap.createScaledBitmap(mEnemyBitmap,mWidth,mHeight,true);
        }



        mPositionX = startX;
        mPositionY = startY;
        mRadius = mWidth * 0.6f;

        mOffsetX = mEnemyBitmap.getWidth() / 2;
        mOffsetY = mEnemyBitmap.getHeight() / 2;

        mHitbox = new Circle(mPositionX,mPositionY,mRadius);

        mMaxSpeed = 0.05f;

        currentReload = RELOAD_TIME;
        Random r = new Random();
        bulletPattern = r.nextInt(4);
        //Log.d("enemy cons","bullet pattern "+bulletPattern);
    }
    void moveTo(float x, float y, long elapsedMillis) {
        float dX = x - mPositionX;
        float dY = y - mPositionY;
        float distance = (float) Math.sqrt((dX * dX) + (dY * dY));


        if (distance != 0) {
            float vX = 0;
            float vY = 0;
            if (mMoveType == 0) {
                vX = (dX / distance) * mMaxSpeed * elapsedMillis;
                vY = (dY / distance) * mMaxSpeed * elapsedMillis;

            } else if (mMoveType == 1) {
                if (mPositionX > x){
                    vX = -mMaxSpeed * elapsedMillis;
                }
                else if (mPositionX < x){
                    vX = mMaxSpeed * elapsedMillis;
                }
                if (mPositionY > y){
                    vY = -mMaxSpeed * elapsedMillis;
                }
                else if (mPositionY < y){
                    vY = mMaxSpeed * elapsedMillis;
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

    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if (elapsedMillis >= 1) {

            if (!GameManager.getInstance().getTimeFreezeActivated()) {
                currentReload -= elapsedMillis;
                moveTo(App.getScreenWidth() / 2, App.getScreenHeight() / 2, elapsedMillis);

                if (currentReload <= 0) {
                    float dX = gameEngine.getPlayer().getmPositionX() - mPositionX;
                    float dY = gameEngine.getPlayer().getmPositionY() - mPositionY;

                    float magnitude = (float)Math.sqrt((dX *dX)+(dY*dY));

                    float unitX;
                    float unitY;

                    if (magnitude == 0){
                        unitX = 20;
                        unitY = 0;
                    }
                    else{
                        unitX = dX / magnitude;
                        unitY = dY / magnitude;
                    }

                    float pdX = gameEngine.getPlayer().getmPositionY() - mPositionY;
                    float pdY = -(gameEngine.getPlayer().getmPositionX() - mPositionX);

                    float pmagnitude = (float)Math.sqrt((pdX *pdX)+(pdY*pdY));

                    float punitX;
                    float punitY;

                    if (pmagnitude == 0){
                        punitX = 20;
                        punitY = 0;
                    }
                    else{
                        punitX = pdX / pmagnitude;
                        punitY = pdY / pmagnitude;
                    }

                    float spreadFactor;
                    Log.d("enemy cons","bullet pattern "+bulletPattern);
                    switch (bulletPattern) {
                        case 0: //1 bullet straight at player
                            gameEngine.addGameObject(new Bullet(mPositionX, mPositionY, unitX, unitY,0.4f,0));
                            break;
                        case 1: //1 bullet straight at player, 2 at side of player

                            spreadFactor = 0.4f;
                            Log.d("enemy cons",""+unitX+" "+unitY);
                            Log.d("enemy cons",""+punitX+" "+punitY);
                            Log.d("enemy cons",""+unitX + spreadFactor*punitX+" "+unitY+ spreadFactor*punitY);
                            Log.d("enemy cons",""+(unitX - spreadFactor*punitX)+" "+(unitY- spreadFactor*punitY));
                            gameEngine.addGameObject(new Bullet(mPositionX, mPositionY, unitX, unitY,0.3f,0));
                            gameEngine.addGameObject(new Bullet(mPositionX, mPositionY, unitX + spreadFactor*punitX, unitY+ spreadFactor*punitY,0.3f,0));
                            gameEngine.addGameObject(new Bullet(mPositionX, mPositionY, unitX - spreadFactor*punitX, unitY- spreadFactor*punitY,0.3f,0));

                            break;
                        case 2: //10 bullets in a circle from enemy
                            int numBullets = 10;
                            double angleToAdd =  360f / (numBullets-1) * Math.PI / 180;
                            double angle = 0;

                            for (int i = 0; i < numBullets; i++) {
                                gameEngine.addGameObject(new Bullet(mPositionX, mPositionY,(float)(Math.sin(angle)),(float)(Math.cos(angle)),0.2f,0));
                                angle += angleToAdd;
                            }
                            break;

                        case 3: // 5 bullets in arrow formation straight at player
                            spreadFactor = 50f;

                            gameEngine.addGameObject(new Bullet(mPositionX + (unitX * spreadFactor), mPositionY + (unitY * spreadFactor), unitX, unitY, 0.5f, 1500));

                            gameEngine.addGameObject(new Bullet(mPositionX + (punitX * spreadFactor), mPositionY + (punitY * spreadFactor), unitX, unitY, 0.5f, 1500));
                            gameEngine.addGameObject(new Bullet(mPositionX - (punitX * spreadFactor), mPositionY - (punitY * spreadFactor), unitX, unitY, 0.5f, 1500));

                            gameEngine.addGameObject(new Bullet(mPositionX + (unitX * spreadFactor * -1) + (punitX * 2 * spreadFactor), mPositionY + (unitY * spreadFactor * -1) + (punitY * 2 * spreadFactor), unitX, unitY, 0.5f, 1500));
                            gameEngine.addGameObject(new Bullet(mPositionX + (unitX * spreadFactor * -1) - (punitX * 2 * spreadFactor), mPositionY + (unitY * spreadFactor * -1) - (punitY * 2 * spreadFactor), unitX, unitY, 0.5f, 1500));
                            break;


                        default:
                    }
                    currentReload = RELOAD_TIME;
                }


            }

        }
        if (isDead) {
            gameEngine.removeGameObject(this);
        }

    }

    @Override
    public Circle getHitbox(){
        return mHitbox;
    }

    @Override
    public boolean checkCollision(Collideable other) {
        return false;
    }

    public void enemyDie(){
        isDead = true;
    }

}
