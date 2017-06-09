package com.wine9.cameraapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wine9.cameraapplication.widget.CameraSurfaceView;

/**
 * Created by gaokuncheng on 2017/6/7.
 */
public class CameraActivity extends AppCompatActivity{

    CameraSurfaceView cameraSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraSurfaceView = (CameraSurfaceView) findViewById(R.id.camera);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSurfaceView.takePicture();
            }
        });
    }










}
