package com.mijael.appsupport.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mijael.appsupport.ui.listener.MyLifecycleObserver;

public class BaseFragment extends Fragment {

    public MyLifecycleObserver observer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if (observer!=null)
            observer.onCreate();

        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {

        if (observer!=null)
            observer.onStart();

        super.onStart();
    }


    @Override
    public void onResume() {

        if (observer!=null)
            observer.onResume();

        super.onResume();
    }


    @Override
    public void onPause() {

        if (observer!=null)
            observer.onPause();

        super.onPause();
    }


    @Override
    public void onStop() {

        if (observer!=null)
            observer.onStop();

        super.onStop();
    }


    @Override
    public void onDestroy() {

        if (observer!=null)
            observer.onDestroy();

        super.onDestroy();
    }


    public void setLifecycleObserver(MyLifecycleObserver observer){
        this.observer = observer;
    }


}
