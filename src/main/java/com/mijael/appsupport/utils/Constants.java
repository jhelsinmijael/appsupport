package com.mijael.appsupport.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.mijael.appsupport.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Constants {

    //  WS STATUS APP
    public static final int WS_STATUS_SUCCESS = 99;
    public static final int WS_STATUS_FAIL = 0;

    public static final String BASIC_AUTH_USER = "8mjTy4Y522t7f3U";
    public static final String BASIC_AUTH_PASS = "FJjnrkHyn9uysGG";

    //  OP

    public static final int OP_SEND_TO_SERVER = -1;
    public static final int OP_SUBSCRIBE_MAIN_TOPIC = -2;
    public static final int OP_SUBSCRIBE_MAIN_TOPIC_SUCCESS = -3;


    //METHODS

    public static void tooglePassword(EditText editText) {

        //show

        if (editText.getInputType() == InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) {

            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setSelection(editText.getText().length());

        } else {

            editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            editText.setSelection(editText.getText().length());

        }


    }


    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static void hideKeyboardd(Context context, View view) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            view.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void showKeyboard(Context context, View view) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            //inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            inputMethodManager.showSoftInput(view, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public static float convertDpiToPx(Context context, float dpi) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpi,
                context.getResources().getDisplayMetrics()
        );
    }


    public static Bitmap downloadImage(String url_image) {
        URL imageUrl = null;
        Bitmap imagen = null;
        try {
            imageUrl = new URL(url_image);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return imagen;
    }


    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1600);
    }


    public static void screenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);//VERIFICANDO SI LA PANTALLA SE ENCUENTRA BLOQUEADA Y/O APAGADA
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn == false) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire(10000);
        }
    }


    public static void openWaze(final Context context, final String lat, final String lng) {
        try {
            String url = "waze://?ll=" + lat + "," + lng + "&navigates=yes&z=10";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {

            SimpleAlertDialog.withPositiveNegativeButton(
                    context,
                    context.getResources().getString(R.string.debe_instalar_waze),
                    context.getResources().getString(R.string.si_ir_play_store), (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                        context.startActivity(intent);
                    },
                    context.getResources().getString(android.R.string.no), (dialog, which) -> dialog.dismiss()
            );
        }
    }


    public static void openGoogleMaps(Context context, String lat, String lon, String descripcion) {

        try {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } catch (ActivityNotFoundException e) {
            SimpleAlertDialog.withPositiveNegativeButton(
                    context,
                    context.getResources().getString(R.string.debe_instalar_google_maps),
                    context.getResources().getString(R.string.si_ir_play_store), (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                        context.startActivity(intent);
                    },
                    context.getResources().getString(android.R.string.no), (dialog, which) -> dialog.dismiss()
            );
        }


    }


    public static void dialPhoneNumber(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }


    public static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }


    public static boolean isInternetConnected(Context context) {

        //ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        //
        //        //should check null because in airplane mode it will be null
        //        return netInfo != null && netInfo.isAvailable() && netInfo.isConnected();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


            }
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable();
    }


    public static void isInternetOnline(Activity activity, OnInternetOnlineListener listener) {

        new Thread(() -> {

            try {
                Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

                int val = p.waitFor();
                boolean reachable = (val == 0);
                activity.runOnUiThread(() -> listener.onInternetOnline(reachable));
                return;
                //return reachable;

            } catch (Exception e) {
                e.printStackTrace();
            }
            activity.runOnUiThread(() -> listener.onInternetOnline(false));

            //return false;

        }).start();

    }


    public static boolean hasPermission(Context context, String permission) {

        if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }


    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity, @DrawableRes int res) {

        Window window = activity.getWindow();
        Drawable background = activity.getResources().getDrawable(res);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);

    }


    public static void isInternetConectedAndOnline(Activity activity, OnInternetOnlineListener listener) {
    }


    public interface OnInternetOnlineListener {

        void onInternetOnline(boolean online);

    }


}
