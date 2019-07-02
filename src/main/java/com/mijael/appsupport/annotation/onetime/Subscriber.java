package com.mijael.appsupport.annotation.onetime;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.mijael.appsupport.ui.activity.BaseActivity;
import com.mijael.appsupport.ui.fragment.BaseFragment;

public class Subscriber {

    private BaseActivity activity;
    private BaseFragment fragment;
    private View view;

    //activity

    public Subscriber(BaseActivity activity, View view) {
        this.activity = activity;
        this.view = view;
    }


    //fragment

    public Subscriber(BaseFragment fragment, View view) {
        this.fragment = fragment;
        this.view = view;
    }



    public BaseActivity getActivity() {
        return activity;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public View getView() {
        return view;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "activity=" + (activity==null?"null":activity.getClass().getSimpleName()) +
                ", fragment=" + (fragment==null?"null":fragment.getClass().getSimpleName()) +
                ", view=" +( (view==null)?"null":view.getId() ) +
                '}';
    }

}
