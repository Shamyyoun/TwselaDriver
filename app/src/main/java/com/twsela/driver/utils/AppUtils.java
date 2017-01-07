package com.twsela.driver.utils;

import com.twsela.driver.Const;

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
}
