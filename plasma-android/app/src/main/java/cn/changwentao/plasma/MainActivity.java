/*
 * Copyright (C) 2016 tao7
 *
 * https://github.com/tao7
 */
package cn.changwentao.plasma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private PlasmaView mPlasmaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlasmaView = (PlasmaView) findViewById(R.id.plasmaView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlasmaView.play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlasmaView.pause();
    }
}
