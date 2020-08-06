package com.sweetmay.weatherproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class SunriseView extends View {

    private static final int PICTURE_SIZE = 40;

    private Paint sunrisePaint;
    private Paint placeHolderPaint;
    private float percent;
    private double[] vector;
    private float[] vectorOrigin = {290,290};
    private double sweepAngle;
    private int[] destVector = new int[2];
    private Bitmap sun;
    private Rect dst;

    public SunriseView(Context context) {
        super(context);
        initView();
    }


    public SunriseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        placeHolderPaint = new Paint();
        sunrisePaint = new Paint();
        placeHolderPaint.setStyle(Paint.Style.STROKE);
        placeHolderPaint.setColor(Color.rgb(242, 220, 89));
        placeHolderPaint.setStrokeWidth(10f);
        placeHolderPaint.setAntiAlias(true);
        sunrisePaint.setColor(Color.rgb(227, 103, 0));
        sunrisePaint.setStyle(Paint.Style.STROKE);
        sunrisePaint.setStrokeWidth(12f);
        sunrisePaint.setAntiAlias(true);
        sun = BitmapFactory.decodeResource(getResources(), R.drawable.sun1);
    }

    public void setSunriseSunset(long sunrise, long sunset, long dt){
        long duration = sunset - sunrise;
        long timeSinceRise = dt - sunrise;
        percent = (float) timeSinceRise/duration;
        calculateSunImgPos();
        invalidate();
    }

    private boolean calculateSunImgPos(){
        if(percent > 0 && percent < 1){
            sweepAngle = 180*percent;
            vector = new double[2];
            vector[0] = 275 * Math.cos(Math.toRadians(180 - sweepAngle));
            vector[1] = -(275 * Math.sin(Math.toRadians(180 - sweepAngle)));
            destVector[0] = (int)(vector[0]+vectorOrigin[0]);
            destVector[1] = (int)(vector[1]+vectorOrigin[1]);
            int leftPos = destVector[0]-PICTURE_SIZE/2;
            int topPos = destVector[1]-PICTURE_SIZE/2;
            int rightPos = destVector[0]+PICTURE_SIZE;
            int bottomPos = destVector[1]+PICTURE_SIZE;
            dst = new Rect(leftPos, topPos, rightPos, bottomPos);
            return true;
        }else return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(600, 600);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(dst != null){
            canvas.drawArc(30, 25, 570, 575, -180, 180, false, placeHolderPaint);
            canvas.drawArc(30, 25, 570, 575, -180, (float) sweepAngle, false, sunrisePaint);
            canvas.drawBitmap(sun, null, dst, null);
        }
    }
}
