package com.mijael.appsupport.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.mijael.appsupport.R;

public class ImageCardView extends CardView{

    private @DrawableRes int imgRes=0;
    private float paddingRes=0;

    public ImageCardView(@NonNull Context context) {
        super(context);
        init(context);
    }

    //required this contructor
    public ImageCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageCardView, defStyleAttr, 0);

        imgRes = a.getResourceId(R.styleable.ImageCardView_icv_src, 0);
        paddingRes = a.getDimension(R.styleable.ImageCardView_icv_padding, 0);

        a.recycle();

        init(context);
    }

    private void init(Context context){

        //TypedValue outValue = new TypedValue();
        //getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(imgRes);
        //imageView.setBackgroundResource(outValue.resourceId);
        imageView.setPadding((int)paddingRes, (int)paddingRes, (int)paddingRes, (int)paddingRes);
        addView(imageView);

    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(@DrawableRes int imgRes) {
        this.imgRes = imgRes;
    }

}
