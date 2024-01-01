package com.example.simplegesture;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

public class TransformGestureDetector implements View.OnTouchListener {
    private TransformGestureListener mTransformGestureListener;

    private int mPrevPointerCount;
    private float mPrevCenterX;
    private float mPrevCenterY;
    private float mPrevMeanDistance;
    private final float[] mPrevVx = new float[40];
    private final float[] mPrevVy = new float[40];

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPrevPointerCount = 0;
                if (mTransformGestureListener != null) {
                    mTransformGestureListener.onBegin();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTransformGestureListener != null) {
                    mTransformGestureListener.onEnd();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mTransformGestureListener != null) {
                    mTransformGestureListener.onCancel();
                }
                break;
            default:
                break;
        }
        int pointerCount = event.getPointerCount();

        // calc center coordinates
        float centerX = 0;
        float centerY = 0;
        for (int index = 0; index < pointerCount; index++) {
            centerX += event.getX(index);
            centerY += event.getY(index);
        }
        centerX /= pointerCount;
        centerY /= pointerCount;

        double sumDistance = 0;
        double sumRotateDegrees = 0;
        for (int index = 0; index < pointerCount; index++) {
            float vx = event.getX(index) - centerX;
            float vy = event.getY(index) - centerY;

            // calc distance to center coordinates
            sumDistance += Math.sqrt(vx * vx + vy * vy);

            // calc angle between vectors
            int pointerId = event.getPointerId(index);
            sumRotateDegrees = Math.toDegrees(angleBetweenVectors(mPrevVx[pointerId], mPrevVy[pointerId], vx, vy));

            mPrevVx[pointerId] = vx;
            mPrevVy[pointerId] = vy;
        }
        float meanDistance = (float) (sumDistance / pointerCount);
        float rotateDegrees = (float) (sumRotateDegrees / pointerCount);

        if (pointerCount == mPrevPointerCount) {
            float translateX = centerX - mPrevCenterX;
            float translateY = centerY - mPrevCenterY;

            float scale = mPrevMeanDistance < 1e-5 ? 1 : meanDistance / mPrevMeanDistance;

            if (mTransformGestureListener != null) {
                mTransformGestureListener.onTransform(translateX, translateY,
                        centerX, centerY,
                        scale, scale,
                        rotateDegrees);
            }
        }

        mPrevPointerCount = pointerCount;
        mPrevCenterX = centerX;
        mPrevCenterY = centerY;
        mPrevMeanDistance = meanDistance;
        return true;
    }

    private static double cross(float vx1, float vy1, float vx2, float vy2) {
        return vx1 * vy2 - vy1 * vx2;
    }

    private static double angleBetweenVectors(float vx1, float vy1, float vx2, float vy2) {
        double dist1 = Math.sqrt(vx1 * vx1 + vy1 * vy1);
        double dist2 = Math.sqrt(vx2 * vx2 + vy2 * vy2);

        if (dist1 < 1e-5 || dist2 < 1e-5) {
            return 0;
        }
        return Math.asin(cross(vx1, vy1, vx2, vy2) / dist1 / dist2);
    }

    public interface TransformGestureListener {
        void onBegin();

        void onTransform(float translateX, float translateY,
                         float centerX, float centerY,
                         float scaleX, float scaleY,
                         float rotateDegrees);

        void onEnd();

        void onCancel();
    }

    public void setTransformGestureListener(TransformGestureListener listener) {
        mTransformGestureListener = listener;
    }
}
