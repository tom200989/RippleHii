package com.ripplehi.ripplehi;

import android.content.Context;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * 作用:手势识别器
 * 作者:Administrator
 * 日期:2017/1/26
 * 时间:22:33
 * 项目:LiyiBuy
 * 作者:Administrator
 */
public abstract class GestureListener extends SimpleOnGestureListener {

    private Context context;

    public GestureListener(Context context) {
        this.context = context;
    }

    public abstract void getUpXY(float x, float y);

    @Override
    public boolean onDown(MotionEvent e) {
        // *** 留待扩展 ***
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        getUpXY(e.getX(), e.getY());
        return true;
    }
}
