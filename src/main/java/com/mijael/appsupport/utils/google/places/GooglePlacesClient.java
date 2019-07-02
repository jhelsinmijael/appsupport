package com.mijael.appsupport.utils.google.places;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mijael.appsupport.utils.Constants;
import com.mijael.appsupport.utils.google.places.model.PlaceAutocomplete;
import com.mijael.appsupport.utils.google.places.request.GooglePlacesRequest;
import com.mijael.appsupport.utils.google.places.response.GooglePlacesResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GooglePlacesClient {

    private ObserverFail failListener;
    private ObserverSuccess successListener;
    List<PlaceAutocomplete> placeAutocompletes;

    private String apiKey;

    private static GooglePlacesClient instance;

    private  GooglePlacesClient(String apiKey){
        this.apiKey = apiKey;
    }

    public static GooglePlacesClient getInstance(String apiKey) {
        if (instance==null)
            instance = new GooglePlacesClient(apiKey);
        return instance;
    }


    @NonNull
    public GooglePlacesClient findAutocompletePredictions(AppCompatActivity activity, @NonNull GooglePlacesRequest request) {

        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?"
                .concat("input=").concat(request.getQuery())
                .concat("&types=").concat(request.getTypeFilter()==null?"":request.getTypeFilter().getType())
                .concat("&location=").concat((request.getLocation()==null)?"":String.valueOf(request.getLocation().latitude)).concat(",").concat(String.valueOf(request.getLocation().longitude))
                .concat("&radius=").concat(String.valueOf(request.getRadius()))
                .concat(request.isStrictBounds()==null?"":(request.isStrictBounds().booleanValue()?"&strictbounds":""))
                .concat("&key=").concat(apiKey);

        Log.i(GooglePlacesClient.class.getSimpleName(), url);
        //Log.e("mqtt", url);
        Log.e("mqtt", request.getQuery());

        new Thread(() -> operate(activity, url)).start();

        return this;
    }


    private void operate(AppCompatActivity activity, String url){

        OkHttpClient client = new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    String credential = Credentials.basic(Constants.BASIC_AUTH_USER, Constants.BASIC_AUTH_PASS);
                    return response.request().newBuilder().header("Authorization", credential).build();
                })
                .build();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(Exception::new);
            }

            @Override
            public void onResponse(Call call, Response response) {

                if (response==null)
                    return;

                if (response.body()==null)
                    return;

                if (!response.isSuccessful())
                    return;

                activity.runOnUiThread(() -> {
                    //try {
                    //Log.i(GooglePlacesClient.class.getSimpleName(), response.body().string());
                    //} catch (IOException e) {
                    //  e.printStackTrace();
                    //}
                });

                try {
                    placeAutocompletes = new Gson().fromJson(
                            new JSONObject(response.body().string()).getJSONArray("predictions").toString(),
                            new TypeToken<List<PlaceAutocomplete>>(){}.getType()
                    );
                }catch (Exception e){
                    placeAutocompletes = null;
                    e.printStackTrace();
                }

                if (placeAutocompletes==null)
                    placeAutocompletes = new ArrayList<>();

                Log.e("mqtt", placeAutocompletes.toString());

                activity.runOnUiThread(() -> successListener.onSuccess(GooglePlacesResponse.newInstance(placeAutocompletes)));

            }
        });

    }


    public GooglePlacesClient addOnSuccessListener(ObserverSuccess listener){
        this.successListener = listener;
        return this;
    }


    public GooglePlacesClient addOnFailureListener(ObserverFail listener){
        this.failListener = listener;
        return this;
    }



    @FunctionalInterface
    public interface ObserverSuccess{

        void onSuccess(GooglePlacesResponse response);

    }

    @FunctionalInterface
    public interface ObserverFail{

        void onFail(GooglePlacesClientException e);

    }



    public class GooglePlacesClientException extends Exception{

    }

}
