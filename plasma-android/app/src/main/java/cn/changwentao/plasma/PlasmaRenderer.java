/*
 * Copyright (C) 2016 tao7
 *
 * https://github.com/tao7
 */
package cn.changwentao.plasma;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Custom view class show plasma animation.
 */
public class PlasmaRenderer implements GLSurfaceView.Renderer {
    private static final int[] sColors = {0xFF7C8489, 0xFF4FB3A4, 0xFFFF7073, 0xFFF5B977, 0xFFFDFC7F};

    private final Context mContext;
    private final int mVelocity;

    private long globalStartTime;

    public PlasmaRenderer(Context context) {
        mContext = context;
        mVelocity = mContext.getResources().getInteger(R.integer.plasma_velocity);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0, 0, 0, 0);

        globalStartTime = System.nanoTime();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
    }
}
