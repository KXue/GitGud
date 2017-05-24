package com.gud.git.gitgud.Input;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.gud.git.gitgud.R;

/**
 * Created by KevinXue on 5/18/2017.
 */

public class InputController{
    private boolean mTouched, mTimeStop;
    private float mTouchX, mTouchY;
    private int mMainFingerID;


    public InputController(View view){
        view.findViewById(R.id.DrawSurface).setOnTouchListener(new RegularTouchListener());
        mTouched = false;
        mTimeStop = false;
        mTouchX = -1;
        mTouchY = -1;
    }
    private class RegularTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getPointerCount() > 1 && !mTimeStop) {
                mTimeStop = true;
            }

            int action = event.getActionMasked();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    mMainFingerID = event.getPointerId(0);
                    mTouched = true;
                    mTouchX = event.getX(0);
                    mTouchY = event.getY(0);
                    mMainFingerID = event.getPointerId(0);
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d("InputUp", event.toString());
                    mTouched = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    for(int i = 0; i < event.getPointerCount(); i++){
                        if(event.getPointerId(i) == mMainFingerID){
                            mTouchX = event.getX(i);
                            mTouchY = event.getY(i);
                            break;
                        }
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.d("InputPUp", event.toString());
                    if(event.getPointerId(event.getActionIndex()) == mMainFingerID){
                        for(int i = 0; i < event.getPointerCount(); i++){
                            if(event.getPointerId(i) != mMainFingerID){
                                mTouchX = event.getX(i);
                                mTouchY = event.getY(i);
                                mMainFingerID = event.getPointerId(i);
                                break;
                            }
                        }
                    }
                    break;
            }
            return true;
        }
    }
    public boolean isTimeStop(){
        return mTimeStop;
    }
    public void resetTimeStop(){
        mTimeStop = false;
    }
    public PointF getTouchPoint(){
        return new PointF(mTouchX, mTouchY);
    }
    public boolean getTouched(){
        return mTouched;
    }
}
