package com.wine9.cameraapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wine9.cameraapplication.utils.CaptureUtils;

import java.io.File;

/**
 * Created by kexiaoderenren on 2017/6/7.
 */
public class IntentCaptureActivity  extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        findViewById(R.id.tv_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCapture();
            }
        });
    }

    private void startCapture() {
        imagePath = CaptureUtils.getOutputMediaPath(CaptureUtils.MEDIA_TYPE_IMAGE);
        if (imagePath == null) {
            return ;
        }
        Log.d("test","---path:" + imagePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("test","---requestCode:" + requestCode + "----resultCode:" + resultCode);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {

                } else {

                }
            }
        }
    }















}
