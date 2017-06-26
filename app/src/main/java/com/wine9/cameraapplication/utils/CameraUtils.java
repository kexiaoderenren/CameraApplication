package com.wine9.cameraapplication.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by kexiaoderenren on 2017/6/7.
 */
public class CameraUtils {

    /***Check if this device has a camera*/
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }




}
