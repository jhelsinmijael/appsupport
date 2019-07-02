package com.mijael.appsupport.utils.gps;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.lang.ref.WeakReference;

import static android.app.Activity.RESULT_OK;

public class GpsStatusDetector {

    private static final int REQUEST_CODE = 2;

    private WeakReference<Activity> mActivityWeakReference;
    private WeakReference<GpsStatusDetector.GpsStatusDetectorCallBack> mCallBackWeakReference;

    public GpsStatusDetector(Activity activity) {
        this.mActivityWeakReference = new WeakReference<>(activity);
        this.mCallBackWeakReference = new WeakReference<>((GpsStatusDetector.GpsStatusDetectorCallBack) activity);
    }

    public GpsStatusDetector(Fragment fragment) {
        this.mActivityWeakReference = new WeakReference<>((Activity) fragment.getActivity());
        this.mCallBackWeakReference = new WeakReference<>((GpsStatusDetector.GpsStatusDetectorCallBack) fragment);
    }

    public void checkGpsStatus() {
        Activity activity = mActivityWeakReference.get();
        GpsStatusDetector.GpsStatusDetectorCallBack callBack = mCallBackWeakReference.get();
        if (activity == null || callBack == null) {
            return;
        }

        if (isGpsEnabled(activity)) {
            callBack.onGpsSettingStatus(true);
        } else {
            setLocationRequest(activity, callBack);
        }
    }

    private boolean isGpsEnabled(Activity activity) {
        String provider = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains(LocationManager.GPS_PROVIDER)){
            return true;
        }else return provider.contains(LocationManager.NETWORK_PROVIDER);
        // otherwise return false
    }


    private void setLocationRequest(final Activity activity, final GpsStatusDetector.GpsStatusDetectorCallBack callBack) {
        final GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30 * 1000)
                .setFastestInterval(5 * 1000);

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true) // important!
                .setNeedBle(true)
                .build();

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, locationSettingsRequest);

        result.setResultCallback(locationSettingsResult -> {
            final Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    //callBack.onGpsSettingStatus(true);

                    final AlertDialog.Builder builder =  new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                    final String message = "Para el correcto funcionamiendo del app active el GPS";

                    builder.setMessage(message)
                            .setPositiveButton("OK",(DialogInterface d, int id)-> {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            })
                            .setNegativeButton("Cancel",(DialogInterface d, int id) -> d.cancel());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    btn.setTextColor(activity.getResources().getColor(android.R.color.black));
                    btn.setAllCaps(true);
                    Button btn_negative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    btn_negative.setTextColor(activity.getResources().getColor(android.R.color.black));
                    btn_negative.setAllCaps(true);



                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(activity, REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        callBack.onGpsSettingStatus(false);
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    callBack.onGpsSettingStatus(false);
                    break;
            }

            mGoogleApiClient.disconnect(); // If you do not disconnect, causes a memory leak
        });



    }


    public void checkOnActivityResult(int requestCode, int resultCode) {
        Activity activity = mActivityWeakReference.get();
        GpsStatusDetector.GpsStatusDetectorCallBack callBack = mCallBackWeakReference.get();
        if (activity == null || callBack == null) {
            return;
        }

        if (requestCode == GpsStatusDetector.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                callBack.onGpsSettingStatus(true);
            } else {
                callBack.onGpsSettingStatus(false);
                callBack.onGpsAlertCanceledByUser();
            }
        }
    }

    public interface GpsStatusDetectorCallBack {
        void onGpsSettingStatus(boolean enabled);

        void onGpsAlertCanceledByUser();
    }

}
