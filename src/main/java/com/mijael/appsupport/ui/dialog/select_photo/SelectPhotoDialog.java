package com.mijael.appsupport.ui.dialog.select_photo;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mijael.appsupport.R;
import com.mijael.appsupport.ui.dialog.base.BaseDialogFragment;
import com.mijael.appsupport.utils.Constants;

public class SelectPhotoDialog extends BaseDialogFragment
        implements View.OnClickListener {


    private Animation translateAnimationToBotton, translateAnimationToTop;
    private OnTypeSelectedListener listener;
    private final int DPI_TRANSLATE = 150;

    private RelativeLayout body;
    private LinearLayout container;
    private LinearLayout btn_camera;
    private LinearLayout btn_gallery;
    private ImageView img_camera;
    private ImageView img_gallery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_or_gallery, container, false);
        if (getDialog() != null) {
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.back_transparent));
            }
        }
        //ButterKnife.bind(this, view);
        bindViews(view);
        init();
        return view;
    }


    @Override
    public void onClick(View v) {

        if (listener == null)
            return;


        if (v.getId() == R.id.btn_camera) {

            dismiss();

            listener.onTypeSelected(ImageProvider.CAMERA);

        } else if (v.getId() == R.id.btn_gallery) {

            dismiss();

            listener.onTypeSelected(ImageProvider.GALLERY);

        } else if (v.getId() == R.id.body) {

            startAnimationToBottom();

        }


    }


    private void bindViews(View view) {

        body = view.findViewById(R.id.body);
        container = view.findViewById(R.id.container);
        btn_camera = view.findViewById(R.id.btn_camera);
        btn_gallery = view.findViewById(R.id.btn_gallery);
        img_camera = view.findViewById(R.id.img_camera);
        img_gallery = view.findViewById(R.id.img_gallery);

    }


    private void init() {

        setListeners();
        setImages();
        initAnimations();
        startAnimationToTop();

    }


    private void setListeners() {

        btn_camera.setOnClickListener(this);
        btn_gallery.setOnClickListener(this);
        body.setOnClickListener(this);

    }


    private void setImages() {

        if (getActivity() == null)
            return;

        //Glide.with(getActivity())
        //        .load(R.drawable.ic_svg_camera_plus)
        //        .into(img_camera);
//
        //Glide.with(getActivity())
        //        .load(R.drawable.ic_svg_camera_plus)
        //        .into(img_gallery);

    }


    private void initAnimations() {

        translateAnimationToBotton = new TranslateAnimation(1, 1f,
                1, Constants.convertDpiToPx(getActivity(), DPI_TRANSLATE));
        translateAnimationToBotton.setDuration(200);
        translateAnimationToBotton.setFillAfter(true);
        translateAnimationToBotton.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateAnimationToTop = new TranslateAnimation(1, 1f,
                (int) Constants.convertDpiToPx(getActivity(), 0), (int) Constants.convertDpiToPx(getActivity(), -DPI_TRANSLATE));
        translateAnimationToTop.setDuration(350);
        //translateAnimationToTop.setFillAfter(true);
        translateAnimationToTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().post(() -> {

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) container.getLayoutParams();
                    layoutParams.bottomMargin = 0;
                    container.setLayoutParams(layoutParams);

                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


    private void startAnimationToTop() {

        container.setAnimation(translateAnimationToTop);
        container.startAnimation(translateAnimationToTop);

    }


    private void startAnimationToBottom() {

        container.setAnimation(translateAnimationToBotton);
        container.startAnimation(translateAnimationToBotton);

    }


    public void setOnTypeSelectedListener(OnTypeSelectedListener listener) {
        this.listener = listener;
    }


    public interface OnTypeSelectedListener {

        void onTypeSelected(ImageProvider imageProvider);

    }


    public enum ImageProvider {

        CAMERA(0),
        GALLERY(1);

        ImageProvider() {
        }

        ImageProvider(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        private int value;

    }
}
