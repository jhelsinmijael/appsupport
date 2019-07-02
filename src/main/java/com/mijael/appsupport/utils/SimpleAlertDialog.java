package com.mijael.appsupport.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.mijael.appsupport.R;

public class SimpleAlertDialog {

    public static AlertDialog get(Context context){

        AlertDialog al = new AlertDialog.Builder(context)
                .setCancelable(false)
                .create();

        return al;

    }


    public static AlertDialog getProgresDialog(Context context, String message){

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(context);
        tvText.setText(message);
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        //AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setCancelable(true);
        //builder.setView(ll);
//
        //AlertDialog dialog = builder.create();
        //dialog.show();

        AlertDialog ad = SimpleAlertDialog.get(context);
        ad.setView(ll);

        Window window = ad.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(ad.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            ad.getWindow().setAttributes(layoutParams);
        }

        return ad;

    }


    public static AlertDialog withOkPositiveButton(Context context, String message){

        AlertDialog al = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();

        al.show();

        Button btn = al.getButton(DialogInterface.BUTTON_POSITIVE);
        btn.setTextColor(context.getResources().getColor(android.R.color.black));
        btn.setAllCaps(true);

        return al;

    }


    public static AlertDialog withPositiveButton(Context context, String message, String buttonName, DialogInterface.OnClickListener listener){

        if (listener==null) {

            listener = (dialog, which) -> dialog.dismiss();

        }

        AlertDialog al = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton(buttonName, listener)
                .create();

        al.show();

        Button btn = al.getButton(DialogInterface.BUTTON_POSITIVE);
        btn.setTextColor(context.getResources().getColor(android.R.color.black));

        return al;

    }


    public static AlertDialog withPositiveNegativeButton(Context context, String message, String positiveName, DialogInterface.OnClickListener listener, String negativeName, DialogInterface.OnClickListener negativeListener){

        if (listener==null) {

            listener = (dialog, which) -> dialog.dismiss();

        }

        if (negativeListener==null){

            negativeListener = (dialog, which) -> dialog.dismiss();

        }

        AlertDialog al = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton(positiveName, listener)
                .setNegativeButton(negativeName, negativeListener)
                .create();

        al.show();

        Button btn = al.getButton(DialogInterface.BUTTON_POSITIVE);
        Button btn_negative = al.getButton(DialogInterface.BUTTON_NEGATIVE);
        btn.setTextColor(context.getResources().getColor(android.R.color.black));
        btn_negative.setTextColor(context.getResources().getColor(android.R.color.black));

        return al;

    }


}
