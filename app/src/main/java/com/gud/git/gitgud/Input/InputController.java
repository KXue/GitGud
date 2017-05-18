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
    private boolean mTouched;
    private float mTouchX, mTouchY;

    public InputController(View view){
        view.findViewById(R.id.DrawSurface).setOnTouchListener(new RegularTouchListener());
        mTouched = false;
        mTouchX = -1;
        mTouchY = -1;
    }
    private class RegularTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                mTouched = true;
                mTouchX = event.getX(0);
                mTouchY = event.getY(0);
            }
            else if (action == MotionEvent.ACTION_UP) {
                mTouched = false;
                mTouchX = -1;
                mTouchY = -1;
            }
            else if (action == MotionEvent.ACTION_MOVE) {
                mTouched = true;
                mTouchX = event.getX(0);
                mTouchY = event.getY(0);
            }
            return true;
        }
    }
    public PointF getTouchPoint(){
        return new PointF(mTouchX, mTouchY);
    }
    public boolean getTouched(){
        return mTouched;
    }
}
