package com.gud.git.gitgud.Engine;

/**
 * Created by Nue on 5/17/2017.
 */

public class Circle{

    float mPositionX, mPositionY;
    float mRadius;

    public Circle(float positionX,float positionY, float radius) {

        mPositionX = positionX;
        mPositionY = positionY;
        mRadius = radius;

    }

    public boolean intersect(Circle other) {
        boolean collided = false;

        float dX = other.mPositionX - mPositionX;
        float dY = other.mPositionY - mPositionY;

        double distance = (dX*dX) + (dY*dY);

        if (distance < (other.mRadius + mRadius)*(other.mRadius + mRadius)) {
            collided = true;
        }

        return collided;
    }

    public void moveCircle(float x, float y) {
        mPositionX = x;
        mPositionY = y;
    }

    public float getX(){
        return mPositionX;
    }

    public float getY(){
        return mPositionY;
    }

}
