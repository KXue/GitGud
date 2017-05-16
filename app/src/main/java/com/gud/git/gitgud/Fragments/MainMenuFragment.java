package com.gud.git.gitgud.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gud.git.gitgud.MainActivity;
import com.gud.git.gitgud.R;

public class MainMenuFragment extends BaseFragment implements View.OnClickListener {

    public MainMenuFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).startGame();
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
