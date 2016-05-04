/*
 * Copyright (C) 2016 tao7
 *
 * https://github.com/tao7
 */
package cn.changwentao.plasma;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glEnableVertexAttribArray;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Custom view class show plasma animation.
 */
public class PlasmaRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "PlasmaRenderer";
    private static final int[] sColors = {0xFF7C8489, 0xFF4FB3A4, 0xFFFF7073, 0xFFF5B977, 0xFFFDFC7F};

    private static final int BYTES_PER_FLOAT = 4;
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int STRIDE = POSITION_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private final FloatBuffer vertexData;

    private final Context mContext;
    private final int mVelocity;

    private int program;
    private int aPositionLocation;
    private int aColorLocation;

    private long globalStartTime;

    public PlasmaRenderer(Context context) {
        mContext = context;
        mVelocity = mContext.getResources().getInteger(R.integer.plasma_velocity);

        float[] tableVertices = {
                0f, 0f,
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,
                -0.5f, -0.5f
        };

        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();

        vertexData.put(tableVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        globalStartTime = System.nanoTime();

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper
                .compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        glUseProgram(program);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        float ratio = (float) width / height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }

    String vertexShaderSource = "";
    String fragmentShaderSource = "";
}
