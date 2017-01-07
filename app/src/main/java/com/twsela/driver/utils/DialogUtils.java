package com.twsela.driver.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.twsela.driver.R;


/**
 * Created by Shamyyoun on 2/24/2016.
 */
public class DialogUtils {
    /**
     * method, used to show alert dialog with passed message res id
     *
     * @param context
     * @param messageResId
     * @param onClickListener
     * @return
     */
    public static AlertDialog showAlertDialog(Context context, int messageResId, DialogInterface.OnClickListener onClickListener) {
        return showAlertDialog(context, context.getString(messageResId), onClickListener);
    }

    /**
     * method, used to alert dialog with passed message string
     *
     * @param context
     * @param message
     * @param onClickListener
     */
    public static AlertDialog showAlertDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        // create the dialog builder & set message
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(message);

        // check the click listener
        if (onClickListener != null) {
            // not null
            // add positive click listener
            dialogBuilder.setPositiveButton(context.getString(R.string.ok), onClickListener);
        } else {
            // null
            // add new click listener to dismiss the dialog
            dialogBuilder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        // create and show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        customizeDialogMsgTextView(dialog);

        return dialog;
    }

    /**
     * method, used to show confirm dialog with passed message res id
     *
     * @param context
     * @param messageResId
     * @param positiveClickListener
     */
    public static AlertDialog showConfirmDialog(Context context, int messageResId, DialogInterface.OnClickListener positiveClickListener,
                                                DialogInterface.OnClickListener negativeClickListener) {

        return showConfirmDialog(context, context.getString(messageResId), positiveClickListener, negativeClickListener);
    }

    /**
     * method, used to show confirm dialog with passed message string
     *
     * @param context
     * @param message
     * @param positiveClickListener
     */
    public static AlertDialog showConfirmDialog(Context context, String message, DialogInterface.OnClickListener positiveClickListener,
                                                DialogInterface.OnClickListener negativeClickListener) {
        // create the dialog builder & set message
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(message);

        // add positive click listener
        dialogBuilder.setPositiveButton(context.getString(R.string.yes), positiveClickListener);

        // check negative click listener
        if (negativeClickListener != null) {
            // not null
            // add negative click listener
            dialogBuilder.setNegativeButton(context.getString(R.string.no), negativeClickListener);
        } else {
            // null
            // add new click listener to dismiss the dialog
            dialogBuilder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        // create and show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        customizeDialogMsgTextView(dialog);

        return dialog;
    }

    /**
     * method, used to show progress dialog with passed message res id and not cancelable
     *
     * @param context
     * @param messageResId
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, int messageResId) {
        return showProgressDialog(context, messageResId, false);
    }

    /**
     * method, used to show progress dialog with passed message res id and not cancelable
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {
        return showProgressDialog(context, message, false);
    }

    /**
     * method, used to show progress dialog with passed message res id
     *
     * @param context
     * @param messageResId
     * @param cancelable
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, int messageResId, boolean cancelable) {
        return showProgressDialog(context, context.getString(messageResId), cancelable);
    }

    /**
     * method, used to show progress dialog with passed message string
     *
     * @param context
     * @param message
     * @param cancelable
     * @return the dialog
     */
    public static ProgressDialog showProgressDialog(Context context, String message, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setCancelable(cancelable);
        dialog.show();
        customizeDialogMsgTextView(dialog);
        return dialog;
    }

    /**
     * method, used to customize the message textview of a dialog
     *
     * @param dialog
     */
    private static void customizeDialogMsgTextView(Dialog dialog) {
        try {
            Context context = dialog.getContext();
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            /* textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    context.getResources().getDimension(R.dimen.alert_dialog_text_spacing),
                    context.getResources().getDisplayMetrics()), 1.0f); */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method, used to show list dialog with items array res id
     *
     * @param context
     * @param itemsResId
     * @param itemClickListener
     * @return
     */
    public static AlertDialog showListDialog(Context context, int itemsResId, DialogInterface.OnClickListener itemClickListener) {
        return showListDialog(context, null, itemsResId, itemClickListener);
    }

    /**
     * method, used to show list dialog with items array res id and with passed title
     *
     * @param context
     * @param title
     * @param itemsResId
     * @param itemClickListener
     * @return
     */
    public static AlertDialog showListDialog(Context context, String title, int itemsResId, DialogInterface.OnClickListener itemClickListener) {
        String[] items = context.getResources().getStringArray(itemsResId);
        return showListDialog(context, title, items, itemClickListener);
    }

    /**
     * method, used to show list dialog with string items array
     *
     * @param context
     * @param items
     * @param itemClickListener
     * @return
     */
    public static AlertDialog showListDialog(Context context, String[] items, DialogInterface.OnClickListener itemClickListener) {
        return showListDialog(context, null, items, itemClickListener);
    }

    /**
     * method, used to show list dialog with string items array and with passed title
     *
     * @param context
     * @param title
     * @param items
     * @param itemClickListener
     * @return
     */
    public static AlertDialog showListDialog(Context context, String title, String[] items, DialogInterface.OnClickListener itemClickListener) {
        // create the dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set title if possible
        if (title != null) {
            builder.setTitle(title);
        }

        // set items and items click listener
        if (itemClickListener != null) {
            builder.setItems(items, itemClickListener);
        } else {
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        // create the dialog and show it
        AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }
}
