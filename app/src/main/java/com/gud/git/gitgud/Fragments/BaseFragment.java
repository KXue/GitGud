package com.gud.git.gitgud.Fragments;

import android.app.Fragment;

import com.gud.git.gitgud.MainActivity;

/**
 * Created by KevinXue on 5/15/2017.
 */

public class BaseFragment extends Fragment {
    public boolean onBackPressed() {
        return false;
    }
    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }
}
