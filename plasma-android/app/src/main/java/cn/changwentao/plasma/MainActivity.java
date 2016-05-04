/*
 * Copyright (C) 2016 tao7
 *
 * https://github.com/tao7
 */
package cn.changwentao.plasma;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mPlasmaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlasmaView = new GLSurfaceView(this);
        mPlasmaView.setRenderer(new PlasmaRenderer(this));

        setContentView(mPlasmaView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlasmaView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlasmaView.onPause();
    }
}
