package com.mijael.appsupport.annotation.destroy;

import androidx.annotation.NonNull;

import com.mijael.appsupport.ui.activity.BaseActivity;
import com.mijael.appsupport.ui.fragment.BaseFragment;

public class Subscriber {

    private BaseActivity activity;
    private String token;
    private BaseFragment fragment;

    public Subscriber(BaseActivity activity, String token) {
        this.activity = activity;
        this.token = token;
    }


    public Subscriber(BaseFragment fragment, String token) {
        this.token = token;
        this.fragment = fragment;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BaseActivity getActivity() {
        return activity;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    @NonNull
    @Override
    public String toString() {
        if (fragment == null) {
            return "[acticity=" + activity.getClass().getSimpleName() + ", token=" + token + ", fragment=null]";
        } else {
            return "[acticity=null, token=" + token + ", fragment=" + fragment.getClass().getSimpleName() + "]";
        }
    }

}
