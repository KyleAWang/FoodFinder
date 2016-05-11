package com.wang.kyle.foodfinder.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mActivity = (Activity)context;
        }
    }

}
