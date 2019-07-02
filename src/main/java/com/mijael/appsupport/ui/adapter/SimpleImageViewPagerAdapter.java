package com.mijael.appsupport.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mijael.appsupport.R;

import java.util.List;

public class SimpleImageViewPagerAdapter<T extends List> extends PagerAdapter {


    private static final String TAG = SimpleImageViewPagerAdapter.class.getSimpleName();
    private Context mContext;
    private View view;
    private LayoutInflater mLayoutInflater;
    @DrawableRes
    private int[] mResourceIds;
    private T items; //String / File / Bitmap

    public SimpleImageViewPagerAdapter(Context context, T items) {
        this(context, null, items);
    }


    public SimpleImageViewPagerAdapter(Context context, @DrawableRes int[] res){
        this(context, res, null);
    }


    private SimpleImageViewPagerAdapter(Context mContext, @DrawableRes int[] mResourceIds, T items) {
        this.mContext = mContext;
        this.mResourceIds = mResourceIds;
        this.items = items;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
       if (items==null){
           return mResourceIds==null?0:mResourceIds.length;
       }else {
           return items.size();
       }
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem() called with: " + "container = [" + container + "], position = [" + position + "]");
        View itemView = mLayoutInflater.inflate(R.layout.simple_image_pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.img_photo);

        if (items!=null){

                Glide.with(mContext)
                        .load(items.get(position))
                        .apply(new RequestOptions().centerCrop())
                        .into(imageView);


        }else if (mResourceIds!=null){

                Glide.with(mContext)
                        .load(mResourceIds[position])
                        .apply(new RequestOptions().centerCrop())
                        .into(imageView);

        }

        container.addView(itemView);

        return itemView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.d(TAG, "destroyItem() called with: " + "container = [" + container + "], position = [" + position + "], object = [" + object + "]");
        container.removeView((RelativeLayout) object);
    }


}
