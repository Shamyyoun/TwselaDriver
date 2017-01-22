package com.twsela.driver.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.twsela.driver.R;
import com.twsela.driver.activities.ParentActivity;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.connection.ConnectionListener;
import com.twsela.driver.utils.DialogUtils;
import com.twsela.driver.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 5/28/16.
 */
public class ParentFragment extends Fragment implements View.OnClickListener, ConnectionListener {
    // used to hold connection handlers that should be cancelled when destroyed
    private final List<ConnectionHandler> connectionHandlers = new ArrayList();
    protected ParentActivity activity;
    protected View rootView;
    protected ProgressDialog progressDialog;
    protected ParentFragment fragment;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ParentActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (activity.hasToolbar()) {
            setHasOptionsMenu(true);
        }
        fragment = this;
    }

    public View findViewById(int id) {
        if (rootView != null) {
            return rootView.findViewById(id);
        } else {
            return null;
        }
    }

    public int getResColor(int id) {
        return getResources().getColor(id);
    }

    public void logE(String msg) {
        Utils.logE(msg);
    }

    @Override
    public void onClick(View v) {
    }

    public void loadFragment(int container, Fragment fragment) {
        loadFragment(container, fragment, 0, 0);
    }

    public void loadFragment(int container, Fragment fragment, int enterAnim, int exitAnim) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        if (enterAnim != 0 && exitAnim != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim);
        }
        ft.replace(container, fragment);
        ft.commitAllowingStateLoss();
    }

    public void showProgressDialog() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            progressDialog = DialogUtils.showProgressDialog(activity, R.string.please_wait_dotted);
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();
        Utils.showShortToast(activity, R.string.something_went_wrong_please_try_again);
    }

    public void cancelWhenDestroyed(ConnectionHandler connectionHandler) {
        connectionHandlers.add(connectionHandler);
    }

    @Override
    public void onDestroy() {
        // cancel requests if found
        for (ConnectionHandler connectionHandler : connectionHandlers) {
            if (connectionHandler != null) connectionHandler.cancel(true);
        }

        super.onDestroy();
    }

    public void setTitle(CharSequence title) {
        if (activity != null) {
            activity.setTitle(title);
        }
    }

    public void setTitle(int titleId) {
        if (activity != null) {
            activity.setTitle(titleId);
        }
    }

    public void createOptionsMenu(int menuId) {
        if (activity != null) {
            activity.createOptionsMenu(menuId);
        }
    }

    public void removeOptionsMenu() {
        if (activity != null) {
            activity.removeOptionsMenu();
        }
    }

    public void hideKeyboard() {
        if (rootView != null) {
            Utils.hideKeyboard(rootView);
        }
    }

    public boolean hasInternetConnection() {
        return Utils.hasInternetConnection(activity);
    }

    public void printStackTrace(Exception e) {
        if (e != null && Utils.DEBUGGABLE) e.printStackTrace();
    }
}
