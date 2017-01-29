package com.ripplehi.ripplehi;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作用:自定义波纹控件
 * 作者:Administrator
 * 日期:2017/1/23
 * 时间:2:06
 * 项目:LiyiBuy
 * 作者:Administrator
 */
public class RippleHii extends View implements View.OnTouchListener {

    private int CIRCLE = 0;// 绘制圆
    private int CLEAR = 1;// 清空
    private String COLORHEX = "#009688";// 默认波纹颜色
    private int DEFAULTREDIU = 20;// 默认开始圆圈大小

    /* -------------------------------------------- 可调节参数 START -------------------------------------------- */
    private float rateSpeed = 1.2f;// 控制绘制圆圈速率
    private float rateMaxAlpha = 0.31f;// 控制最大透明度
    private float rateMinAlpha = 0.25f;// 控制最小透明度
    private int color = Color.parseColor(COLORHEX);// 波纹颜色
    /* -------------------------------------------- 可调节参数 END -------------------------------------------- */

    private int MAXALPHA = (int) (255 * rateMaxAlpha);// 透明度最大值
    private int MINALPHA = (int) (255 * rateMinAlpha);// 透明度最小值

    private int alpha = MAXALPHA;// 初始透明度

    private int initRadiu = 0;// 初始绘制半径
    private int changeRadiu = 0;// 循环次数(递增绘制半径), 初始值为0, 修改可以改变点击时的初始半径

    private int maxRadiu = 200;// 循环次数(最大绘制半径)
    private int interupt = 1;// 循环间隔

    private int circleSpeed = 2;// 绘制圆圈速率, 即每次绘制递增5个px
    private int alphaSpeed = 1;// 绘制圆圈速率, 即每次绘制递增5个px

    private Canvas canvas;// 画布
    private Paint paint;// 画笔

    private int type = 0;// 类型标记

    private float x = 0;// 手指触摸的坐标
    private float y = 0;// 手指触摸的坐标

    private boolean can = true;// 是否允许点击标记;用于防止用户频繁点击
    private Context context;
    private int viewBackgroudColor;

    private RippleListener rippleListener;
    private GestureDetector gestureDetector;


    public RippleHii(Context context) {
        this(context, null, 0);
    }

    public RippleHii(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleHii(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // 1.设置XML属性
        initAttrs(context, attrs);
        // 2.初始化画笔
        initPaint();
        // 3.设置触摸监听器
        setOnTouchListener(this);

        // 4.添加手势识别器, 用于取代ontouch事件的event,因为自定义view的event不能响应action_down.
        
        /* 使用手势识别器的目的是为了防止自定义的Ontouch出现了action_down不能响应, 并且简化一大堆的自定义手势逻辑
         * 如果不使用手势识别器则需要自定义xy坐标. 比较麻烦
         * */

        gestureDetector = new GestureDetector(context, new GestureListener(context) {
            @Override
            public void getUpXY(float x, float y) {
                // 手指抬起后的操作
                if (can) {
                    drawRipple(x, y);
                }
            }
        });
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleHii);
        rateSpeed = typedArray.getFloat(R.styleable.RippleHii_speed, 1.2f);
        rateSpeed = rateSpeed > 2.0f ? 2.0f : rateSpeed;// 限制在2.0
        rateMaxAlpha = typedArray.getFloat(R.styleable.RippleHii_maxAlpha, 0.31f);
        rateMaxAlpha = rateMaxAlpha > 1.0f ? 1.0f : rateMaxAlpha;// 限制在1.0
        rateMinAlpha = typedArray.getFloat(R.styleable.RippleHii_minAlpha, 0.25f);
        rateMinAlpha = rateMinAlpha > 1.0f ? 1.0f : rateMinAlpha;// 限制在1.0
        color = typedArray.getColor(R.styleable.RippleHii_color, Color.parseColor(COLORHEX));
        typedArray.recycle();
    }


    /* 1.先于onDraw()方法前执行, 用于获取自定义view的宽高,从而获取该view中心点 */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取本view的中心点(测试)
        //w12 = this.getMeasuredWidth() * 1f / 2
        //h12 = this.getMeasuredHeight() * 1f / 2;

        // 获取自定义view的背景色
        viewBackgroudColor = this.getSolidColor();

        float mw = getMeasuredWidth() * 1f;
        float mh = getMeasuredHeight() * 1f;

        maxRadiu = (int) Math.round(Math.abs(Math.sqrt(mw * mw + mh * mh)) + 0.5f);

        int setWidth = measureWidth(widthMeasureSpec);// 需要设置的宽度
        int setHeight = measureHeight(heightMeasureSpec);// 需要设置的高度
        
        /* 设置宽高 */
        setMeasuredDimension(setWidth, setHeight);
    }

    /* 2.开始绘制(绘制根据业务需要进行类型区分) */
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        if (!isInEditMode()) {
            if (type == CIRCLE) {
                paint.setAlpha(alpha);
                canvas.drawCircle(x, y, initRadiu, paint);
            } else if (type == CLEAR) {
                canvas.drawColor(viewBackgroudColor);
            }
        }
    }

    /* 3.为自定义view添加触发事件(此处设置为按下即触发) */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 返回手势识别器的事件处理, 而不去直接ontouch响应
        return gestureDetector.onTouchEvent(event);
    }

    /* -------------------------------------------- HELPER -------------------------------------------- */
    /* -------------------------------------------- HELPER -------------------------------------------- */
    /* -------------------------------------------- HELPER -------------------------------------------- */

    /**
     * 初始化画笔
     *
     * @call SelfRipple()
     */
    private void initPaint() {
        paint = new Paint();
        paint.setColor(color);
    }

    /**
     * 返回 基于当前测量设备所测量出来的宽度
     *
     * @param measureSpec
     * @return 即将 需要设置的宽度
     * @attention: 如果wrap_content, 则.....
     * @attention: fill_parent, 则.....
     * @attention: 根据具体需求所返回 即将 需要设置的宽度
     * @call onMeasure()
     */
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {
        }
        return specSize;
    }

    /**
     * 返回 基于当前测量设备所测量出来的高度
     *
     * @param measureSpec
     * @return 即将 需要设置的高度
     * @attention: 如果wrap_content, 则.....
     * @attention: fill_parent, 则.....
     * @attention: 根据具体需求所返回 即将 需要设置的高度
     * @call onMeasure()
     */
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {
        }
        return specSize;
    }

    /**
     * **** 绘制波纹流程 ****
     *
     * @param x
     * @param y
     * @call onTouch()
     */
    private void drawRipple(final float x, final float y) {
        can = false;

        new Thread(new Runnable() {
            @Override
            public void run() {

                // 1.开始画圆
                while (changeRadiu < maxRadiu) {
                    try {
                        Thread.sleep(interupt);

                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RippleHii.this.drawIt(changeRadiu, CIRCLE, x, y);// 绘制
                            }
                        });

                        changeRadiu += circleSpeed * rateSpeed;// 更新下次的绘制半径

                        if (alpha <= MINALPHA) {// 限定透明度最小值
                            alpha = MINALPHA;
                        } else {
                            alpha -= alphaSpeed;// 更新下次的绘制透明度
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 2.清空画布
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RippleHii.this.clearIt(CLEAR);
                        // 3.恢复初始值, 为下一次点击做准备
                        // 注意: 此3步一定要放在UI线程操作, 否则会出现抖动闪烁
                        changeRadiu = DEFAULTREDIU;
                        alpha = MAXALPHA;
                        can = true;
                        // 波纹执行完成的接口行为
                        setFinish();
                    }
                });


            }
        }).start();
    }


    /**
     * 循环画圆
     *
     * @param radiu    绘制半径
     * @param drawcode 绘制类型: 画圆 | 清空
     * @call drawRipple()
     */
    private void drawIt(int radiu, int drawcode, float x, float y) {
        initRadiu = radiu;
        this.x = x;
        this.y = y;
        type = drawcode;
        invalidate();
    }

    /**
     * 清空画布
     *
     * @param drawcode 绘制类型: 画圆 | 清空
     * @call drawRipple()
     */
    private void clearIt(int drawcode) {
        type = drawcode;
        invalidate();
    }

    /* 4.对外提供接口 */

    /**
     * **** 对外提供波纹完成的接口 ****
     *
     * @param rippleListener
     */
    public void setOnRippleListener(RippleListener rippleListener) {
        this.rippleListener = rippleListener;
    }


    /**
     * 锁定操作2
     */
    public void setFinish() {
        if (rippleListener != null) {
            rippleListener.finish();
        }
    }


    /**
     * 对外接口
     */
    public interface RippleListener {
        void finish();
    }

}
