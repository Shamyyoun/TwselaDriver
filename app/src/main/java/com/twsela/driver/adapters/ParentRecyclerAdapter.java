package com.twsela.driver.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.twsela.driver.R;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.interfaces.OnItemClickListener;
import com.twsela.driver.interfaces.OnItemRemovedListener;
import com.twsela.driver.utils.DialogUtils;
import com.twsela.driver.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 5/11/16.
 */
public abstract class ParentRecyclerAdapter<Item> extends RecyclerView.Adapter<ParentRecyclerViewHolder> {
    protected Context context;
    protected List<Item> data;
    protected int layoutId;
    protected OnItemClickListener itemClickListener;
    protected OnItemRemovedListener itemRemovedListener;
    protected ProgressDialog progressDialog;
    // used to hold connection handlers that should be cancelled when destroyed
    private final List<ConnectionHandler> connectionHandlers = new ArrayList();

    public ParentRecyclerAdapter(Context context, List<Item> data, int layoutId) {
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }

    public ParentRecyclerAdapter(Context context, Item[] data, int layoutId) {
        this.context = context;
        this.data = Arrays.asList(data);
        this.layoutId = layoutId;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemRemovedListener(OnItemRemovedListener itemRemovedListener) {
        this.itemRemovedListener = itemRemovedListener;
    }

    protected void logE(String msg) {
        Utils.logE(msg);
    }

    protected String getString(int resId) {
        return context.getString(resId);
    }

    public void showProgressDialog() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            progressDialog = DialogUtils.showProgressDialog(context, R.string.please_wait_dotted);
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void cancelWhenDestroyed(ConnectionHandler connectionHandler) {
        connectionHandlers.add(connectionHandler);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        // cancel requests if found
        for (ConnectionHandler connectionHandler : connectionHandlers) {
            if (connectionHandler != null) connectionHandler.cancel(true);
        }

        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);

        if (itemRemovedListener != null) {
            itemRemovedListener.onItemRemoved(position);
        }
    }

    public boolean hasInternetConnection() {
        return Utils.hasConnection(context);
    }

    public void printStackTrace(Exception e) {
        if (e != null && Utils.DEBUGGABLE) e.printStackTrace();
    }
}
