/*
 * Copyright (C) 2016 tao7
 *
 * https://github.com/tao7
 */
package cn.changwentao.plasma;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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

    // Millisecond between two frame.
    private int mVelocity;
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

        int contentWidth = getWidth() - mPaddingLeft - mPaddingRight;
        int contentHeight = getHeight() - mPaddingTop - mPaddingBottom;

        mPaint.setColor(0xffff0000);
        canvas.drawText("哈哈哈", 30f, 100f, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();
    }

    /**
     * Start plasma animation.
     */
    public void play() {
        if(mPause) {
            mPause = false;
            invalidate();
        }
    }

    /**
     * Pause plasma animation.
     */
    public void pause() {
        if(!mPause) {
            mPause = true;
            invalidate();
        }
    }

}
