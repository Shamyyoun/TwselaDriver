package com.twsela.driver.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.twsela.driver.R;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.connection.ConnectionListener;
import com.twsela.driver.utils.DialogUtils;
import com.twsela.driver.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ParentActivity extends AppCompatActivity implements View.OnClickListener, ConnectionListener {
    // used to hold connection handlers that should be cancelled when destroyed
    private final List<ConnectionHandler> connectionHandlers = new ArrayList();
    protected AppCompatActivity activity;
    private FrameLayout rootView;
    protected ProgressDialog progressDialog;
    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private int menuId;
    private boolean enableBack;
    private int iconResId;
    private String toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity = this;
        rootView = (FrameLayout) findViewById(android.R.id.content);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // init the view_toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // check the view_toolbar
        if (toolbar != null) {
            // view_toolbar is available >> handle it
            setSupportActionBar(toolbar);
            toolbar.setTitle("");
            tvToolbarTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
            tvToolbarTitle.setText(getTitle());

            // check if enable back
            if (enableBack) {
                // set the suitable back icon
                if (iconResId != 0) {
                    toolbar.setNavigationIcon(iconResId);
                } else {
                    toolbar.setNavigationIcon(R.drawable.back_icon);
                }
            } else if (iconResId != 0) {
                // set this icon
                toolbar.setNavigationIcon(iconResId);
            }
        }
    }

    public void logE(String msg) {
        Utils.logE(msg);
    }

    public void cancelWhenDestroyed(ConnectionHandler connectionHandler) {
        connectionHandlers.add(connectionHandler);
    }

    @Override
    protected void onDestroy() {
        // cancel requests if found
        for (ConnectionHandler connectionHandler : connectionHandlers) {
            if (connectionHandler != null) connectionHandler.cancel(true);
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        logE("onClick has been invoked from ParentActivity");
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();
        Utils.showShortToast(this, R.string.something_went_wrong_please_try_again);
    }

    public void showProgressDialog() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            progressDialog = DialogUtils.showProgressDialog(this, R.string.please_wait_dotted);
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public int getResColor(int id) {
        return getResources().getColor(id);
    }

    public void loadFragment(int container, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment)
                .commitAllowingStateLoss();
    }

    public void hideKeyboard() {
        if (rootView != null) {
            Utils.hideKeyboard(rootView);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbarTitle = title.toString();
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(title);
        } else {
            super.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        toolbarTitle = getString(titleId);
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(titleId);
        } else {
            super.setTitle(titleId);
        }
    }

    public void hideToolbarTitle() {
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setVisibility(View.GONE);
        }
    }

    public void showToolbarTitle() {
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setVisibility(View.VISIBLE);
        }
    }

    public void createOptionsMenu(int menuId) {
        this.menuId = menuId;
        invalidateOptionsMenu();
    }

    public void removeOptionsMenu() {
        menuId = 0;
        invalidateOptionsMenu();
    }

    public void enableBackButton() {
        enableBack = true;
    }

    public void setToolbarIcon(int iconResId) {
        this.iconResId = iconResId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuId != 0) {
            getMenuInflater().inflate(menuId, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && enableBack) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public boolean hasToolbar() {
        return toolbar != null;
    }
}