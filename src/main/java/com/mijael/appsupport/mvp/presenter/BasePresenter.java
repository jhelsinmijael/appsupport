package com.mijael.appsupport.mvp.presenter;

import com.mijael.appsupport.mvp.model.Message;

public interface BasePresenter {

    void onStart();

    void onStop();

    void onMessageReceived(Message message);

}
