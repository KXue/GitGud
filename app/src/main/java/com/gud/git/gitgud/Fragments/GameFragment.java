package com.gud.git.gitgud.Fragments;

import android.content.Context;
import android.net.Uri;
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
import com.gud.git.gitgud.R;

public class GameFragment extends BaseFragment implements View.OnClickListener{
    private GameEngine mGameEngine;
    Player mPlayer;

    public GameFragment() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mGameEngine = new GameEngine();
        mGameEngine.setDrawSurfaceHolder(((SurfaceView)getActivity().findViewById(R.id.DrawSurface)).getHolder());
        mGameEngine.setInputController(new InputController(getView()));

        mPlayer = new Player();
        mGameEngine.addGameObject(mPlayer);
        mGameEngine.setPlayer(mPlayer);

        mGameEngine.addGameObject(new Enemy(1920,1080,0));  //remove this on  release

        mGameEngine.startGame();
        //view.findViewById(R.id.btn_play_pause).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_play_pause){
        }
    }
}
