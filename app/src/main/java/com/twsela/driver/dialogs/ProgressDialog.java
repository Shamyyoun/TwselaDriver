package com.twsela.driver.dialogs;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.twsela.driver.R;
import com.twsela.driver.interfaces.OnRefreshListener;
import com.twsela.driver.utils.Utils;
import com.twsela.driver.views.EmptyView;
import com.twsela.driver.views.ErrorView;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public abstract class ProgressDialog extends ParentDialog {
    private View mainView;
    private ErrorView errorView;
    private EmptyView emptyView;
    private SwipeRefreshLayout swipeLayout;
    private boolean mainIsVisible;

    public ProgressDialog(Context context) {
        super(context);
        setContentView(getContentViewResId());

        // init views
        mainView = rootView.findViewById(getMainViewResId());
        errorView = (ErrorView) rootView.findViewById(R.id.error_view);
        emptyView = (EmptyView) rootView.findViewById(R.id.empty_view);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);

        // customize views
        errorView.setLayoutResId(R.layout.view_dialog_error);
        emptyView.setLayoutResId(R.layout.view_dialog_empty);

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

        showProgressView();
    }

    protected void showError(String msg) {
        hideProgressView();

        if (errorView != null && !mainIsVisible) {
            errorView.setError(msg);
            errorView.setVisibility(View.VISIBLE);
        } else {
            Utils.showLongToast(context, msg);
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
        hideProgressView();

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
        hideProgressView();

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
