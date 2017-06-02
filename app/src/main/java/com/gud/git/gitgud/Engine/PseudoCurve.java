package com.gud.git.gitgud.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.gud.git.gitgud.Input.InputController;

import java.util.ArrayList;


/**
 * Created by KevinXue on 5/25/2017.
 */

public class PseudoCurve implements Updateable, Renderable {
    private PointF mLastPosition;
    private final int mMinSquareDistance = 15 * 15;
    private double travelledDistance = 0;
    private ArrayList<PointF> mPointList = new ArrayList<>();
    private Path mPath;
    public PseudoCurve(PointF lastPosition){
        mLastPosition = lastPosition;
        mPointList.add(mLastPosition);
        Log.d("PC", "lastPosition: " + mLastPosition.toString());
        mPath = new Path();
        mPath.moveTo(lastPosition.x, lastPosition.y);
    }
    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        InputController controller = gameEngine.mInputController;
        if(controller.getTouched()){
            PointF touchPoint = new PointF(controller.getTouchPoint().x, controller.getTouchPoint().y);
            Log.d("PC", "touchPoint: " + touchPoint.toString());
            float squareDistance = (touchPoint.x - mLastPosition.x) * (touchPoint.x - mLastPosition.x) + (touchPoint.y - mLastPosition.y) * (touchPoint.y - mLastPosition.y);
            if(squareDistance >= mMinSquareDistance){
                mLastPosition = touchPoint;
                addPoint(mLastPosition);
            }
        }
    }
    private void addPoint(PointF newPoint){
        mPointList.add(newPoint);
        mPath.lineTo(newPoint.x, newPoint.y);
    }

    @Override
    public void onDraw(Paint paint, Canvas canvas) {
        paint.setColor(Color.argb(255, 0, 255,0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setPathEffect(new CornerPathEffect(15));
        canvas.drawPath(mPath, paint);
        paint.setStrokeWidth(1);
    }
    public PointF lastPoint(){
        return mPointList.get(mPointList.size() - 1);
    }
    public PointF travel(double travelSpeed){
        this.travelledDistance += travelSpeed;
        return(getPosition(travelledDistance));
    }
    public double getTravelledDistance(){
        return travelledDistance;
    }
    public PointF getPosition(double travelledDistance){
        double totalDistance = travelledDistance;
        double lineDistance;
        PointF retPoint =  mPointList.get(0);
        for(int i = 1; i < mPointList.size(); i++){
            lineDistance = getDistance(mPointList.get(i-1), mPointList.get(i));
            if(totalDistance > lineDistance){
                totalDistance -= lineDistance;
            }
            else{
                retPoint = findInterpolatedPoint(mPointList.get(i-1), mPointList.get(i), totalDistance);
                break;
            }
        }
        if(totalDistance > 0){
            retPoint = mPointList.get(mPointList.size() - 1);
        }
        return retPoint;
    }
    public double getLength(){
        double length = 0;
        for(int i = 1; i < mPointList.size(); i++){
            length += getDistance(mPointList.get(i-1), mPointList.get(i));
        }
        return length;
    }
    private PointF findInterpolatedPoint(PointF from, PointF to, double distance){
        double distanceFraction = distance / getDistance(from, to);
        return new PointF((float)(from.x + (to.x - from.x) * distanceFraction), (float)(from.y + (to.y - from.y) * distanceFraction));
    }
    private double getDistance(PointF a, PointF b){
       return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }
}
