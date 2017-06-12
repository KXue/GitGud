package com.gud.git.gitgud.Input;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.Managers.GameManager;
import com.gud.git.gitgud.R;

/**
 * Created by KevinXue on 5/18/2017.
 */

public class InputController{
    private GameEngine mGameEngine;
    private boolean mTouched;
    private float mTouchX, mTouchY;
    private int mMainFingerID;


    public InputController(View view, GameEngine gameEngine){
        view.findViewById(R.id.DrawSurface).setOnTouchListener(new RegularTouchListener());
        view.findViewById(R.id.time_stop_confirm_button).setOnTouchListener(new DialogueConfirmTouchListener());
        view.findViewById(R.id.time_stop_cancel_button).setOnTouchListener(new DialogueCancelTouchListener());

        mGameEngine = gameEngine;
        mTouched = false;
        mTouchX = -1;
        mTouchY = -1;
    }
    private class DialogueConfirmTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                mGameEngine.confirmTimeStop();
                GameManager.getInstance().setTimeFreeze(false);
                GameManager.getInstance().hideConfirmDialogue();
            }
            return false;
        }
    }
    private class DialogueCancelTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
                mGameEngine.cancelTimeStop();
                GameManager.getInstance().setTimeFreeze(false);
                GameManager.getInstance().hideConfirmDialogue();
            }
            return false;
        }
    }
    private class RegularTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    mMainFingerID = event.getPointerId(0);
                    mTouched = true;
                    mTouchX = event.getX(0);
                    mTouchY = event.getY(0);
                    if(GameManager.getInstance().getTimeFreezeActivated()) {
                        GameManager.getInstance().hideConfirmDialogue();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mTouched = false;
                    if(GameManager.getInstance().getTimeFreezeActivated()){
                        GameManager.getInstance().showConfirmDialogue((int)event.getX(), (int)event.getY());
                    }
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
                case MotionEvent.ACTION_POINTER_DOWN:
                    GameManager.getInstance().setTimeFreeze(true);
                    mGameEngine.beginTimeStop();
                break;
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
