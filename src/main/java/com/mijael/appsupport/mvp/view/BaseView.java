package com.mijael.appsupport.mvp.view;

public interface BaseView {

    void showLoading(boolean show, String message);

    default void showLoading(boolean show){
        showLoading(show, "");
    }

}
