package com.mijael.appsupport.utils;

import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyOutlineProvider extends ViewOutlineProvider {

    private int radius;
    private float alpha;


    public MyOutlineProvider(int radius, float alpha) {
        this.radius = radius;
        this.alpha = alpha;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setRoundRect(0, 0, view.getWidth(),view.getHeight(), radius);
        outline.setAlpha(alpha);

        if (outline.canClip())
            view.setClipToOutline(true);
    }

}
