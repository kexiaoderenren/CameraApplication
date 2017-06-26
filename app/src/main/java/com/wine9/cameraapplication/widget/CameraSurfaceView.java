package com.wine9.cameraapplication.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wine9.cameraapplication.utils.CaptureUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by kexiaoderenren on 2017/6/8.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG = "test";

    private Context mContext;
    private SurfaceHolder holder;
    private Camera mCamera;

    private int mScreenWidth;
    private int mScreenHeight;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getScreenMetrix(context);
        initView();
    }

    /**
     * 设置相机参数
     * @param camera 相机
     * @param width 目标宽度
     * @param height 目标高度
     */
    private void setCameraParams(Camera camera, int width, int height) {
        Log.i(TAG,"setCameraParams  width="+width+"  height="+height);
        //返回当前相机设置参数
        Camera.Parameters parameters = camera.getParameters();
        //获取当前相机支持相片大小，并以队列形式返回
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        /**从列表中选取合适的分辨率*/
        Camera.Size picSize = getProperSize(pictureSizeList, ((float) height / width));
        if (null == picSize) {
            picSize = parameters.getPictureSize();
        }
        Log.i(TAG, "picSize.width=" + picSize.width + "  picSize.height=" + picSize.height);

        float w = picSize.width;
        float h = picSize.height;
        //设置相片宽度和高度
        parameters.setPictureSize(picSize.width,picSize.height);
        this.setLayoutParams(new FrameLayout.LayoutParams((int) (height*(h/w)), height));

        //获取当前相机支持预览界面大小，并以队列形式返回
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size preSize = getProperSize(previewSizeList, ((float) height) / width);
        if (null != preSize) {
            Log.i(TAG, "preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
            parameters.setPreviewSize(preSize.width, preSize.height);
        }
        //设置相机拍摄图片质量，数值范围1~100，数值越大图片质量越高
        parameters.setJpegQuality(100);
        //如果相机支持自动对焦，则开启
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCamera.cancelAutoFocus();//自动对焦。
        // 设置PreviewDisplay的方向，将捕获的画面旋转多少度显示
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);
    }

    /**
     * 从列表中选取合适的分辨率
     * 默认w:h = 4:3
     * <p>注意：这里的w对应屏幕的height, h对应屏幕的width<p/>
     */
    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        Log.i(TAG, "screenRatio=" + screenRatio);
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            if (currentRatio - screenRatio == 0) {
                result = size;
                break;
            }
        }
        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }
        return result;
    }

    public void takePicture() {
        mCamera.takePicture(shutterCallback, raw, jpeg);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "----surfaceCreated()");
        if (mCamera == null) {
            mCamera = Camera.open();
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged");
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        mCamera.startPreview();
    }

    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.i(TAG,"shutter");
        }
    };

    private Camera.PictureCallback raw = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            Log.i(TAG, "raw");
        }
    };

    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            BufferedOutputStream bos = null;
            Bitmap bm = null;
            try {
                // 获得图片
                bm = BitmapFactory.decodeByteArray(data, 0, data.length);

                Bitmap bMapRotate;
                Matrix matrix = new Matrix();
                matrix.reset();
                matrix.postRotate(CaptureUtils.getOrientation(mContext));
                bMapRotate = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                bm = bMapRotate;

                String filePath = CaptureUtils.getOutputMediaPath(CaptureUtils.MEDIA_TYPE_IMAGE); //照片保存路径
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.flush();//输出
                    bos.close();//关闭
                    bm.recycle();// 回收bitmap空间
                    mCamera.stopPreview();// 关闭预览
                    mCamera.startPreview();// 开启预览
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        mCamera.stopPreview();//停止预览
        mCamera.release();//释放相机资源
        mCamera = null;
        holder = null;
    }

    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    private void initView() {
        holder = getHolder();//获得surfaceHolder引用
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置类型
    }



}
