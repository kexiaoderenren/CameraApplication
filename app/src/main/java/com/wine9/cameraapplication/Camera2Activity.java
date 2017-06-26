package com.wine9.cameraapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kexiaoderenren on 2017/6/9.
 */
public class Camera2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, Camera2BasicFragment.newInstance())
                .commit();

    }
}
