package com.wang.kyle.foodfinder.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

import com.wang.kyle.foodfinder.R;


public class ProgressDialog extends Dialog {
    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context ctx, int theme) {
        super(ctx, theme);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_bar);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

    }

}
