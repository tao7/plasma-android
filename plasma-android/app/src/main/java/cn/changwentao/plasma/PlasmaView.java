/*
 * Copyright (C) 2016 tao7
 *
 * https://github.com/tao7
 */
package cn.changwentao.plasma;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom view class show plasma animation.
 */
public class PlasmaView extends View {
    private static final int[] sColors = {0xFF7C8489, 0xFF4FB3A4, 0xFFFF7073, 0xFFF5B977, 0xFFFDFC7F};
    private static final int DEFAULT_VELOCITY = 50;

    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;
    private int mContentWidth;
    private int mContentHeight;

    // Millisecond between two frame.
    private int mVelocity;
    private long mStartTime;
    private boolean mPause = true;
    private Paint mPaint;

    public PlasmaView(Context context) {
        super(context);
        init(null, 0);
    }

    public PlasmaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PlasmaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PlasmaView, defStyle, 0);

        mVelocity = a.getInt(R.styleable.PlasmaView_velocity, DEFAULT_VELOCITY);
        a.recycle();

        // Set up a default Paint object
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPause) {
            return;
        }


        long currentTime = time();


        canvas.save();
        canvas.translate(mPaddingLeft, mPaddingTop);
        for (int x = 0; x < mContentWidth; x++) {
            for (int y = 0; y < mContentHeight; y++) {
//                int color = (int) (128.0f + (128.0f * Math.sin((x + currentTime) / 8.0f)));
//                mPaint.setColor(Color.rgb(color, color, color));
//                canvas.drawLine(x, 0, x, mContentHeight, mPaint);

                float c1 = (float) Math.sin(x * Math.cos(currentTime) * 16.0 + currentTime * 4.0);
                float c2 = (float) Math.cos(y * 8.0 + currentTime);
                float c3 = (float) Math.cos(y * 14.0) + (float) Math.sin(currentTime);
                float p = (c1 + c2 + c3) / 3.0f;

                if (p < 0.2) {
                    mPaint.setColor(sColors[0]);
                } else if (p < 0.4) {
                    mPaint.setColor(sColors[1]);
                } else if (p < 0.6) {
                    mPaint.setColor(sColors[2]);
                } else if (p < 0.8) {
                    mPaint.setColor(sColors[3]);
                } else {
                    mPaint.setColor(sColors[4]);
                }

                canvas.drawPoint(x, y, mPaint);
            }
        }
        canvas.restore();
        invalidate();
    }

    //private float dx(int x, int y){
    //float x1 = 0.1f*mContentWidth,x2=0.7f*mContentWidth,y1=0.2f*mContentHeight,y2=0.9f*mContentHeight;
    // Math.sqrt(Math.pow(x-x1,2)+Math.pow(-x1,2)+)
    //}

    //private float dy(int x, int y){
    //float x1 = 0.1f,x2=0.7f,y1=0.2f,y2=0.9f;
    //}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();
        mContentWidth = getWidth() - mPaddingLeft - mPaddingRight;
        mContentHeight = getHeight() - mPaddingTop - mPaddingBottom;
    }

    private long time() {
        return (System.currentTimeMillis() - mStartTime) / 10;
    }

    /**
     * Start plasma animation.
     */
    public void play() {
        if (mPause) {
            mPause = false;
            mStartTime = System.currentTimeMillis();
            invalidate();
        }
    }

    /**
     * Pause plasma animation.
     */
    public void pause() {
        if (!mPause) {
            mPause = true;
            invalidate();
        }
    }

}
