package com.mijael.appsupport.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.mijael.appsupport.R;

@SuppressLint("AppCompatCustomView")
public class FavoriteSwitcher extends ImageView
    implements View.OnClickListener {

    public static final int FAVORITE = 1;
    public static final int NO_FAVORITE = 0;
    private boolean isFavorite = false;


    public FavoriteSwitcher(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_star_o));
        this.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        isFavorite = !isFavorite;

        if (isFavorite){

            setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_star));

        }else {

            setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_star_o));

        }

    }


    /**
     * determinate if is favorite or not
     * */
    public boolean isFavorite() {
        return isFavorite;
    }


    /**
     *  Set favorite
     * @param favorite boolen indicate favorite
     * */
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;

        if (isFavorite){

            setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_star));

        }else {

            setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_star_o));

        }

    }


    /**
     * use  public static final int FAVORITE = 1;
     *     public static final int NO_FAVORITE = 0;
     * @param favorite indicate favorite or not
     *
     * */
    public void setFavorite(int favorite) {

        if (favorite == FAVORITE)
            isFavorite = true;
        else
            isFavorite = false;

        if (isFavorite){

            setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_star));

        }else {

            setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_star_o));

        }

    }
}
