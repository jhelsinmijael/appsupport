package com.mijael.appsupport.utils.asynchttp;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.loopj.android.http.TextHttpResponseHandler;
import com.mijael.appsupport.mvp.model.Status;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public abstract class JSONHttpResponseHandler extends TextHttpResponseHandler {

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        if (throwable==null){
            onFailure(statusCode, headers, responseString, throwable, "");
        }else {
            onFailure(statusCode, headers, responseString, throwable, TextUtils.isEmpty(throwable.getMessage()) ? "" : throwable.getMessage());
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {

        JSONObject object;

        try {
            object = new JSONObject(responseString);
        }catch (Exception e){
            object = null;
            e.printStackTrace();
        }

        if (object==null){
            onFailure(statusCode, headers, responseString, new Exception("Can not conver response string to JSONObject: responseString = ".concat(responseString)), "Can not conver response string to JSONObject: responseString = ".concat(responseString) );
        }else {

            int status = 0, reason=-1;//fail

            try {
                status = object.getInt("status");
            }catch (Exception e){
                status = 0;
                e.printStackTrace();
            }

            if (status == 0){
                try {
                    reason = object.getInt("reason");
                }catch (Exception e){
                    reason = -1;
                    e.printStackTrace();
                }
            }

            onSuccess(statusCode, headers, object, Status.getStatus(status), reason);
        }

    }


    /**
     * Called when request fails
     *
     * @param statusCode     http response status line
     * @param headers        response headers if any
     * @param responseString string response of given charset
     * @param throwable      throwable returned when processing request
     */
    public abstract void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable,@NonNull String errorMessage);

    /**
     * Called when request succeeds
     *
     * @param statusCode     http response status line
     * @param headers        response headers if any
     * @param responseJSONObject JSONObject response of given charset
     * @param status int response status (can be 99 or 0)(99=success)(0==fail)
     * @param reason int response (default value -1) *should be used only if Param(status=0)
     */
    public abstract void onSuccess(int statusCode, Header[] headers, @NonNull JSONObject responseJSONObject, @NonNull Status status, int reason);


}
