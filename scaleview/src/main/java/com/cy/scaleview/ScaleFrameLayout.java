package com.cy.scaleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class ScaleFrameLayout extends FrameLayout {
    private float scale = 1f;
    private int width_self, height_self;
    private int width_bigview = 0, height_bigview = 0;

    private boolean basedOnWidthOrHeight = true;//默认基于宽度，按比例缩放

    private Context context;

    private float px,py;//缩放中心点

    public ScaleFrameLayout(Context context) {
        super(context);
        this.context = context;
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    public void config(boolean basedOnWidthOrHeight,int width_bigview, int height_bigview,float px, float py) {
        this.basedOnWidthOrHeight=basedOnWidthOrHeight;
        this.width_bigview = width_bigview;
        this.height_bigview = height_bigview;

        if (this.width_bigview == 0) this.width_bigview = ScreenUtils.getScreenWidth(context);
        if (this.height_bigview == 0) this.height_bigview = ScreenUtils.getScreenHeight(context);

        this.px=px;
        this.py=py;
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        width_self=getMeasuredWidth();
        height_self=getMeasuredHeight();
        super.onMeasure(MeasureSpec.makeMeasureSpec(
                width_bigview, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height_bigview, MeasureSpec.EXACTLY));
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (basedOnWidthOrHeight) {
            scale = (width_self - getPaddingTop() - getPaddingBottom()) * 1f / width_bigview;

        } else {

            scale = (height_self - getPaddingTop() - getPaddingBottom()) * 1f / height_bigview;

        }
        PaintFlagsDrawFilter pfd= new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        /**
         * 对canvas设置抗锯齿的滤镜，防止变化canvas引起画质降低
         */
        canvas.setDrawFilter(pfd);
        canvas.save();
        //x缩放比例，y缩放比例，px,py，缩放中心点，可以设置左顶点或者中心等顶点
        canvas.scale(scale, scale, px, py);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

}
