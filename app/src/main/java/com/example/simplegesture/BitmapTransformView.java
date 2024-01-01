package com.example.simplegesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BitmapTransformView extends View {
    private Bitmap mBitmap;
    private final Matrix mMatrix = new Matrix();
    private final TransformGestureDetector mTransformGestureDetector = new TransformGestureDetector();

    public BitmapTransformView(Context context) {
        super(context);
    }

    public BitmapTransformView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BitmapTransformView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BitmapTransformView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mTransformGestureDetector.setTransformGestureListener(mTransformGestureListener);
        mMatrix.reset();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTransformGestureDetector.onTouch(this, event);
    }

    private final TransformGestureDetector.TransformGestureListener mTransformGestureListener
            = new TransformGestureDetector.TransformGestureListener() {
        @Override
        public void onBegin() {

        }

        @Override
        public void onTransform(float translateX, float translateY,
                                float centerX, float centerY,
                                float scaleX, float scaleY,
                                float rotateDegrees) {
            mMatrix.postTranslate(translateX, translateY);
            mMatrix.postScale(scaleX, scaleY, centerX, centerY);
            mMatrix.postRotate(rotateDegrees, centerX, centerY);
            invalidate();
        }

        @Override
        public void onEnd() {

        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
}
