package com.twsela.driver.utils;

import android.content.Context;

import com.twsela.driver.Const;
import com.twsela.driver.R;
import com.twsela.driver.models.responses.ServerResponse;

/**
 * Created by Shamyyoun on 22/1/16.
 * A class, with utility methods useful only for the current project "private car"
 */
public class AppUtils {
    /**
     * method, used to concatenate all parts and form a valid url
     *
     * @param pathParts
     * @return
     */
    public static String getApiUrl(Object... pathParts) {
        String fullUrl = Const.END_POINT;
        for (Object pathPart : pathParts) {
            fullUrl += "/" + pathPart.toString();
        }
        return fullUrl;
    }

    /**
     * method, used to concatenate all parts and form a request tag
     *
     * @param pathParts
     * @return
     */
    public static String getRequestTag(Object... pathParts) {
        String tag = "";
        for (int i = 0; i < pathParts.length; i++) {
            if (i != 0) {
                tag += "/";
            }

            tag += pathParts[i].toString();
        }
        return tag;
    }

    /**
     * method, used to concatenate all parts and form a valid url
     *
     * @param pathParts
     * @return
     */
    public static String getDriverApiUrl(Object... pathParts) {
        String fullUrl = Const.END_POINT + "/" + Const.ROUTE_DRIVER;
        for (Object pathPart : pathParts) {
            fullUrl += "/" + pathPart.toString();
        }
        return fullUrl;
    }

    /**
     * method, used to concatenate all parts and form a request tag
     *
     * @param pathParts
     * @return
     */
    public static String getDriverTag(Object... pathParts) {
        String tag = Const.ROUTE_DRIVER;
        for (Object pathPart : pathParts) {
            tag += "/" + pathPart.toString();
        }
        return tag;
    }

    /**
     * method, used to return server response msg or the default msg
     *
     * @param context
     * @param serverResponse
     * @return
     */
    public static String getResponseMsg(Context context, ServerResponse serverResponse) {
        return getResponseMsg(context, serverResponse, null);
    }

    /**
     * method, used to return server response msg or the default msg
     *
     * @param context
     * @param serverResponse
     * @param defaultMsgId
     * @return
     */
    public static String getResponseMsg(Context context, ServerResponse serverResponse, int defaultMsgId) {
        return getResponseMsg(context, serverResponse, context.getString(defaultMsgId));
    }

    /**
     * method, used to return server response msg or the default msg
     *
     * @param context
     * @param serverResponse
     * @param defaultMsg
     * @return
     */
    public static String getResponseMsg(Context context, ServerResponse serverResponse, String defaultMsg) {
        String msg;

        // check server response
        if (serverResponse != null && serverResponse.getValidation() != null && !serverResponse.getValidation().isEmpty()) {
            // prepare msg
            msg = "";
            for (int i = 0; i < serverResponse.getValidation().size(); i++) {
                if (i != 0) {
                    msg += "\n";
                }

                msg += serverResponse.getValidation().get(i);
            }
        } else if (defaultMsg != null) {
            // return default msg
            msg = defaultMsg;
        } else {
            // return default msg
            msg = context.getString(R.string.something_went_wrong_please_try_again);
        }

        return msg;
    }
}
