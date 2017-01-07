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
public class EmptyView extends FrameLayout {
    private Context context;
    private int layoutResId;
    private View rootView;
    private TextView tvEmpty;

    public EmptyView(Context context) {
        super(context);
        this.context = context;
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(context).inflate(layoutResId, this);
        tvEmpty = (TextView) rootView.findViewById(R.id.tv_empty);
    }

    public void setEmpty(int msgResId) {
        if (tvEmpty != null) {
            tvEmpty.setText(msgResId);
        }
    }

    public void setEmpty(String msg) {
        if (tvEmpty != null) {
            tvEmpty.setText(msg);
        }
    }
}
