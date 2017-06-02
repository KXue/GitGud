package com.gud.git.gitgud.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.gud.git.gitgud.App;
import com.gud.git.gitgud.Engine.GameEngine;
import com.gud.git.gitgud.GameObjects.Enemy;
import com.gud.git.gitgud.GameObjects.Player;
import com.gud.git.gitgud.Input.InputController;
import com.gud.git.gitgud.MainActivity;
import com.gud.git.gitgud.Managers.GameManager;
import com.gud.git.gitgud.R;

public class GameFragment extends BaseFragment implements View.OnClickListener{
    private GameEngine mGameEngine;
    private View mTimeStopView;

    public GameFragment() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mTimeStopView = view.findViewById(R.id.time_stop_dialogue);
        hideConfirmDialogue();
        prepareAndStartGame();
    }

    private void prepareAndStartGame() {
        mGameEngine = new GameEngine();
        mGameEngine.setDrawSurfaceHolder(((SurfaceView)getActivity().findViewById(R.id.DrawSurface)).getHolder());
        mGameEngine.setInputController(new InputController(getView(), mGameEngine));
        Player player = new Player();
        mGameEngine.addGameObject(player);
        mGameEngine.setPlayer(player);
        mGameEngine.addGameObject(new Enemy(1920,1080));
        GameManager.getInstance().setGameFragment(this);
        mGameEngine.startGame();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_play_pause){
        }
    }
    public void showConfirmDialogue(int x, int y){
        class ShowTimeStoptask implements Runnable {
            private int x, y;
            public ShowTimeStoptask(int x, int y){
                this.x = x;
                this.y = y;
            }
            @Override
            public void run(){
                mTimeStopView.setVisibility(View.VISIBLE);
                mTimeStopView.setTranslationX(x);
                mTimeStopView.setTranslationY(y);
            }
        };
        getActivity().runOnUiThread(new ShowTimeStoptask(x, y));
    }
    public void hideConfirmDialogue(){
        mTimeStopView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGameEngine.isRunning()){
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        if (mGameEngine.isRunning() && !mGameEngine.isPaused()){
            pauseGameAndShowPauseDialog();
            return true;
        }
        return super.onBackPressed();
    }

    private void pauseGameAndShowPauseDialog() {
        if (mGameEngine.isPaused()) {
            return;
        }
        mGameEngine.pauseGame();
//        PauseDialog dialog = new PauseDialog(getYassActivity());
//        dialog.setListener(this);
//        showDialog(dialog);
    }

    public void resumeGame() {
        mGameEngine.resumeGame();
    }

    public void exitGame() {
        mGameEngine.stopGame();
        ((MainActivity)getActivity()).navigateBack();
    }

    public void startNewGame() {
        // Exit the current game
        mGameEngine.stopGame();
        // Start a new one
        prepareAndStartGame();
    }
}
