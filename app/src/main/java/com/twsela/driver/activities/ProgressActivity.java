package com.twsela.driver.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.twsela.driver.R;
import com.twsela.driver.interfaces.OnRefreshListener;
import com.twsela.driver.utils.Utils;
import com.twsela.driver.views.EmptyView;
import com.twsela.driver.views.ErrorView;


/*
 * Created by Shamyyoun on 10/31/16.
 */
public abstract class ProgressActivity extends ParentActivity {
    private View mainView;
    private ErrorView errorView;
    private EmptyView emptyView;
    private SwipeRefreshLayout swipeLayout;
    private boolean mainIsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());

        // init views
        mainView = findViewById(getMainViewResId());
        errorView = (ErrorView) findViewById(R.id.error_view);
        emptyView = (EmptyView) findViewById(R.id.empty_view);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);

        // customize views
        errorView.setLayoutResId(R.layout.view_error);
        emptyView.setLayoutResId(R.layout.view_empty);

        // add refresh click listener if error view is not null
        if (errorView != null) {
            errorView.setRefreshListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnRefreshListener().onRefresh();
                }
            });
        }

        // add refresh listener
        if (swipeLayout != null) {
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getOnRefreshListener().onRefresh();
                }
            });
        }
    }

    protected abstract int getContentViewResId();

    protected abstract int getMainViewResId();

    protected abstract OnRefreshListener getOnRefreshListener();

    protected void showProgress() {
        if (errorView != null)
            errorView.setVisibility(View.GONE);
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
        if (swipeLayout != null)
            swipeLayout.setRefreshing(false);

        showProgressDialog();
    }

    protected void showError(String msg) {
        hideProgressDialog();

        if (errorView != null && !mainIsVisible) {
            errorView.setError(msg);
            errorView.setVisibility(View.VISIBLE);
        } else {
            Utils.showLongToast(activity, msg);
        }
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
        if (mainView != null && !mainIsVisible)
            mainView.setVisibility(View.GONE);
        if (swipeLayout != null)
            swipeLayout.setRefreshing(false);
    }

    protected void showError(int msgResId) {
        showError(getString(msgResId));
    }

    protected void showEmpty(String msg) {
        hideProgressDialog();

        if (errorView != null)
            errorView.setVisibility(View.GONE);
        if (emptyView != null) {
            emptyView.setEmpty(msg);
            emptyView.setVisibility(View.VISIBLE);
        }
        if (mainView != null)
            mainView.setVisibility(View.GONE);
        if (swipeLayout != null)
            swipeLayout.setRefreshing(false);

        mainIsVisible = false;
    }

    protected void showEmpty(int msgResId) {
        showEmpty(getString(msgResId));
    }

    protected void showMain() {
        hideProgressDialog();

        if (errorView != null)
            errorView.setVisibility(View.GONE);
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
        if (mainView != null)
            mainView.setVisibility(View.VISIBLE);
        if (swipeLayout != null)
            swipeLayout.setRefreshing(false);

        mainIsVisible = true;
    }
}
