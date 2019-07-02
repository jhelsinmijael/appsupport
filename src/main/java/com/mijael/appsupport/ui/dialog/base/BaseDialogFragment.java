package com.mijael.appsupport.ui.dialog.base;

import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onResume() {
        super.onResume();

        if (getDialog().getWindow()==null)
            return;

        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(layoutParams);

    }

}
