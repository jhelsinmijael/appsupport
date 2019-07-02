package com.mijael.appsupport.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mijael.appsupport.ui.listener.MyLifecycleObserver;

public class BaseActivity extends AppCompatActivity {

    public MyLifecycleObserver observer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (observer!=null)
            observer.onCreate();

        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {

        if (observer!=null)
            observer.onStart();

        super.onStart();
    }


    @Override
    protected void onResume() {

        if (observer!=null)
            observer.onResume();

        super.onResume();
    }


    @Override
    protected void onPause() {

        if (observer!=null)
            observer.onPause();

        super.onPause();
    }


    @Override
    protected void onStop() {

        if (observer!=null)
            observer.onStop();

        super.onStop();
    }


    @Override
    protected void onDestroy() {

        if (observer!=null)
            observer.onDestroy();

        super.onDestroy();
    }


    public void setLifecycleObserver(MyLifecycleObserver observer){
        this.observer = observer;
    }


}
