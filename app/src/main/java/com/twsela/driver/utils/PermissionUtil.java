package com.twsela.driver.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 4/2/16.
 */
public class PermissionUtil {
    public static boolean isGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static List<String> getNotGranted(Context context, String[] permissions) {
        List notGrantedPermissions = new ArrayList();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (!isGranted(context, permission)) {
                notGrantedPermissions.add(permission);
            }
        }
        return notGrantedPermissions;
    }

    public static boolean isAllGranted(int[] grantResults) {
        if (grantResults.length == 0) {
            return false;
        } else {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }

            return true;
        }
    }
}
