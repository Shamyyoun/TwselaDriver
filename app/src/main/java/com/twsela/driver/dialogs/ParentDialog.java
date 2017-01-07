package com.twsela.driver.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.twsela.driver.R;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.connection.ConnectionListener;
import com.twsela.driver.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class ParentDialog extends Dialog implements View.OnClickListener, ConnectionListener {
    // used to hold connection handlers that should be cancelled when destroyed
    private final List<ConnectionHandler> connectionHandlers = new ArrayList();
    protected Context context;
    protected FrameLayout rootView;
    private TextView tvDialogTitle;
    private ImageButton ibClose;
    private View progressView;

    public ParentDialog(Context context) {
        super(context);
        this.context = context;

        // set no title and transparent bg
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // init and customize the dialog toolbar
        rootView = (FrameLayout) findViewById(android.R.id.content);
        View toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            tvDialogTitle = (TextView) findViewById(R.id.tv_dialog_title);
            ibClose = (ImageButton) findViewById(R.id.ib_close);
            ibClose.setOnClickListener(this);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (tvDialogTitle != null) {
            tvDialogTitle.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(context.getString(titleId));
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        if (ibClose != null) {
            ibClose.setVisibility(flag ? View.VISIBLE : View.GONE);
        }
    }

    public void logE(String msg) {
        Utils.logE(msg);
    }

    public String getString(int strId) {
        return context.getString(strId);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close) {
            dismiss();
        } else {
            logE("onClick has been invoked from ParentDialog");
        }
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressView();
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressView();
        Utils.showShortToast(context, R.string.something_went_wrong_please_try_again);
    }

    public void showProgressView() {
        hideProgressView();

        if (rootView != null && progressView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            progressView = inflater.inflate(R.layout.view_dialog_progress, null);
            rootView.addView(progressView);
        }
        progressView.setVisibility(View.VISIBLE);
        super.setCancelable(false);
    }

    public void hideProgressView() {
        if (progressView != null) {
            progressView.setVisibility(View.GONE);
        }
        super.setCancelable(true);
    }

    public void cancelWhenDestroyed(ConnectionHandler connectionHandler) {
        connectionHandlers.add(connectionHandler);
    }

    @Override
    public void dismiss() {
        // cancel requests if found
        for (ConnectionHandler connectionHandler : connectionHandlers) {
            if (connectionHandler != null) connectionHandler.cancel(true);
        }

        super.dismiss();
    }

    public void hideKeyboard() {
        if (rootView != null) {
            Utils.hideKeyboard(rootView);
        }
    }

    @Override
    public void show() {
        super.show();

        // customize the dialog width
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
    }
}
