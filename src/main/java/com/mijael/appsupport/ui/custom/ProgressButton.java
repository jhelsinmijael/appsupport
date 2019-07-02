package com.mijael.appsupport.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mijael.appsupport.R;

public class ProgressButton extends RelativeLayout {

    private final int DEFAULT_COLOR = Color.BLACK;
    private final int DEFAULT_WIDTH = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int DEFAULT_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private boolean loading = false;
    private boolean oneTimerSupport = false;
    private String text = "";
    private int textColor = DEFAULT_COLOR;
    private float progressWidth = DEFAULT_WIDTH;
    private float progressHeight = DEFAULT_HEIGHT;

    //widgets
    private TextView textView;
    private ProgressBar progressBar;

    public ProgressButton(Context context) {
        super(context);

        init(context);

    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton, defStyleAttr, 0);

        text = a.getString(R.styleable.ProgressButton_progress_text);
        textColor = a.getColor(R.styleable.ProgressButton_progress_text_color, DEFAULT_COLOR);
        progressWidth = a.getDimension(R.styleable.ProgressButton_progress_with, DEFAULT_WIDTH);
        progressHeight = a.getDimension(R.styleable.ProgressButton_progress_height, DEFAULT_HEIGHT);

        a.recycle();

        init(context);


    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (oneTimerSupport) {

            if (enabled) {
                setLoading(false);
            } else {
                setLoading(true);
            }

        }

    }

    private void init(Context context){

        ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((LayoutParams) layoutParams).addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);

        //Textview
        textView = new TextView(context);
        //textView.setId(R.id.txt_progress_button);
        textView.setText(text);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "metropolis_bold.otf"));
        textView.setTextColor(textColor);
        textView.setLayoutParams(layoutParams);

        //progress config
        layoutParams.width = (int) progressWidth;
        layoutParams.height = (int) progressHeight;
        //ProgressBar
        progressBar = new ProgressBar(context);
        //progressBar.setId(R.id.progress_progress_button);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setVisibility(GONE);

        addView(textView);
        addView(progressBar);


    }

    public boolean isLoading(){
        return loading;
    }

    public void setLoading(boolean loading){
        this.loading = loading;

        if (this.loading){
            textView.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        }else {
            textView.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
        }

    }

    public boolean isOneTimerSupport() {
        return oneTimerSupport;
    }

    public void setOneTimerSupport(boolean oneTimerSupport) {
        this.oneTimerSupport = oneTimerSupport;
    }

}

