package com.wine9.pssc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * 酒咔嚓扫描————相机相框
 * Created by gaokuncheng on 2017/6/13.
 */
public class StokeOnCamera extends View {

    private int mScreenWidth;
    private int mScreenHeight;

    private Paint linePaint;
    private int leftX,topY,rightX,bottomY;
    private int radius;
    private Context mContext;
    private Path mTopPath, mBottomPath;

    public StokeOnCamera(Context context) {
        this(context, null);
    }

    public StokeOnCamera(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StokeOnCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getScreenMetrix(context);
        initViews();
    }

    private void initViews() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);// 抗锯齿
        linePaint.setDither(true);// 防抖动
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(dip2px(mContext, 2));
        linePaint.setStyle(Paint.Style.STROKE);

        leftX = dip2px(mContext, 23);
        rightX = mScreenWidth - leftX;
        topY = dip2px(mContext, 30);
        bottomY = mScreenHeight - dip2px(mContext, 145);

        //画顶部曲线，贝塞尔曲线
        mTopPath = new Path();
        mTopPath.moveTo(leftX, topY);
        mTopPath.quadTo(mScreenWidth/2, dip2px(mContext, 5), rightX, topY);

        //画底部曲线，贝塞尔曲线
        mBottomPath = new Path();
        mBottomPath.moveTo(leftX, bottomY);
        mBottomPath.quadTo(mScreenWidth/2, bottomY+dip2px(mContext, 25), rightX, bottomY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        canvas.drawLine(leftX, topY, leftX, bottomY, linePaint);  //左竖线
        canvas.drawLine(rightX, topY, rightX, bottomY, linePaint);//右竖线
        //canvas.drawPath();

        canvas.drawPath(mTopPath, linePaint);
        canvas.drawPath(mBottomPath, linePaint);
    }

    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    /*** 根据手机的分辨率从 dp 的单位 转成为 px(像素)*/
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getLeftX() {
        return leftX;
    }

    public int getTopY() {
        return topY;
    }

    public int getRightX() {
        return rightX;
    }

    public int getBottomY() {
        return bottomY;
    }
}
