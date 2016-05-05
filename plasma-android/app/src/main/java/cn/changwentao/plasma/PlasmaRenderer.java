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
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetError;
import static android.opengl.GLES20.GL_NO_ERROR;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.Log;

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
    private static final int[] sColors = {0xFF5FD9CD, 0xFFEAF786, 0xFFFFB5A1, 0xFFB8FFB8, 0xFFB8F4FF};

    private static final int BYTES_PER_FLOAT = 4;
    private static final String A_POSITION = "a_Position";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int STRIDE = 4 * BYTES_PER_FLOAT;

    private final Context mContext;
    private final int mVelocity;

    private final FloatBuffer vertexData;
    private int program;
    private int aPositionLocation;
    private int aTextureLocation;
    private int uTimeLocation;

    private long globalStartTime;

    public PlasmaRenderer(Context context) {
        mContext = context;
        mVelocity = mContext.getResources().getInteger(R.integer.plasma_velocity);

        float[] tableVertices = {
                0.0f, 0.0f, 0.5f, 0.5f,
                -1.0f, -1.0f, 0.0f, 1.0f,
                1.0f, -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f, 0.0f,
                -1.0f, -1.0f, 0.0f, 1.0f
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
        aTextureLocation = glGetAttribLocation(program, "a_TextureCoord");

        uTimeLocation = glGetUniformLocation(program, "uTime");
        checkGlError("glGetUniformLocation uTime");
        if (uTimeLocation == -1) {
            throw new RuntimeException("Could not get attrib location for uTime");
        }

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(2);
        glVertexAttribPointer(aTextureLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false,
                STRIDE, vertexData);
        glEnableVertexAttribArray(aTextureLocation);




    }

    private void checkGlError(String op) {
        int error;
        while ((error = glGetError()) != GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
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
        glUniform1f(uTimeLocation, currentTime);
    }

    String vertexShaderSource =
            "attribute vec4 a_Position;" +
                    "attribute vec2 a_TextureCoord;" +
                    "varying vec2 v_TextureCoord;" +
                    "void main()" +
                    "{" +
                    "    gl_Position = a_Position;" +
                    "    v_TextureCoord = a_TextureCoord;\n" +
                    "}";

    String fragmentShaderSource =
            "precision mediump float;" +
                    "uniform float uTime;" +
                    "varying vec2 v_TextureCoord;" +
                    "void main()" +
                    "{" +
                    "    vec2 ca = vec2(0.1, 0.2);" +
                    "    vec2 cb = vec2(0.7, 0.9);" +
                    "    float da = distance(v_TextureCoord, ca);" +
                    "    float db = distance(v_TextureCoord, cb);" +
                    "    float t = uTime * 0.3;" +
                    "    float c1 = sin(da * cos(t) * 16.0 + t * 4.0);" +
                    "    float c2 = cos(v_TextureCoord.y * 8.0 + t);" +
                    "    float c3 = cos(db * 14.0) + sin(t);" +
                    "    float p = (c1 + c2 + c3) / 3.0;" +
                    "    vec4 fColor;" +
                    "    if(p < 0.2) {" +
                    "        fColor = vec4("+ Color.red(sColors[0])/255.0f+", "+ Color.green(sColors[0])/255.0f+", "+ Color.blue(sColors[0])/255.0f+", 1.0);" +
                    "    } else if(p < 0.4) {" +
                    "        fColor = vec4("+ Color.red(sColors[1])/255.0f+", "+ Color.green(sColors[1])/255.0f+", "+ Color.blue(sColors[1])/255.0f+", 1.0);" +
                    "    } else if(p < 0.6) {" +
                    "        fColor = vec4("+ Color.red(sColors[2])/255.0f+", "+ Color.green(sColors[2])/255.0f+", "+ Color.blue(sColors[2])/255.0f+", 1.0);" +
                    "    } else if(p < 0.8) {" +
                    "        fColor = vec4("+ Color.red(sColors[3])/255.0f+", "+ Color.green(sColors[3])/255.0f+", "+ Color.blue(sColors[3])/255.0f+", 1.0);" +
                    "    } else {" +
                    "        fColor = vec4("+ Color.red(sColors[4])/255.0f+", "+ Color.green(sColors[4])/255.0f+", "+ Color.blue(sColors[4])/255.0f+", 1.0);" +
                    "    }" +
                    "    gl_FragColor = fColor;" +
                    "}";
}
