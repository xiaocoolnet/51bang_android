package cn.xcom.helper.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by Administrator on 2016/8/25.
 */
public class PermissionUtil {

    public static boolean hasCamera(Context context){

        return hasPermission("android.permission.CAMERA", context);
    }


    private static boolean canMakeSmores(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    private static boolean hasPermission(String permission , Context context){

        if(canMakeSmores()){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return(context.checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED);
            }

        }

        return true;

    }
}
