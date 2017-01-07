package com.twsela.driver.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.twsela.driver.R;

/**
 * Created by Shamyyoun on 3/9/2016.
 */
public class ErrorView extends FrameLayout {
    private Context context;
    private int layoutResId;
    private View rootView;
    private TextView tvError;

    public ErrorView(Context context) {
        super(context);
        this.context = context;
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(context).inflate(layoutResId, this);
        tvError = (TextView) rootView.findViewById(R.id.tv_error);
    }

    public void setRefreshListener(OnClickListener clickListener) {
        rootView.setOnClickListener(clickListener);
    }

    public void setError(int msgResId) {
        if (tvError != null) {
            tvError.setText(msgResId);
        }
    }

    public void setError(String msg) {
        if (tvError != null) {
            tvError.setText(msg);
        }
    }
}
