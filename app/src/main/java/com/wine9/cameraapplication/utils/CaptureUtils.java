package com.wine9.cameraapplication.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kexiaoderenren on 2017/6/7.
 */
public class CaptureUtils {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final String POSTFIX_IMG = ".jpg";

    public static String getOutputMediaPath(int type) {
        // To be safe, you should check that the SDCard is mounted,using Environment.getExternalStorageState() before doing this.
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;
        // This location works best if you want the created images to be shared,between applications and persist after your app has been uninstalled.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraApplication");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("test","failed to create directory");
                return null;
            }
        }
        String path = mediaStorageDir.getPath();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if (type == MEDIA_TYPE_IMAGE){
            path = path + File.separator + "IMG_"+ timeStamp + POSTFIX_IMG;
        }  else {
            return null;
        }
        return path;
    }

    /**
     * from http://blog.csdn.net/wangbaochu/article/details/44345903
     * 先假设手机为竖屛手机，然后按照竖屛手机的角度关系计算Camera 物理位置与屏幕Display自然方向间的夹角，
     *      最后根据长宽关系判断当前手机是否为竖屛，如果不是，补偿一个270度然后对360取模
     * @param context
     */
    public static int getOrientation(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int orientation;
        boolean expectPortrait;
        switch (rotation) {
            case Surface.ROTATION_0:
            default:
                orientation = 90;
                expectPortrait = true;
                break;
            case Surface.ROTATION_90:
                orientation = 0;
                expectPortrait = false;
                break;
            case Surface.ROTATION_180:
                orientation = 270;
                expectPortrait = true;
                break;
            case Surface.ROTATION_270:
                orientation = 180;
                expectPortrait = false;
                break;
        }
        boolean isPortrait = display.getHeight() > display.getWidth();
        if (isPortrait != expectPortrait) {
            orientation = (orientation + 270) % 360;
        }
        return orientation;
    }

}
