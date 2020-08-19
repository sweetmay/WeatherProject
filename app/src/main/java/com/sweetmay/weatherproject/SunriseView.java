package com.sweetmay.weatherproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

public class SunriseView extends View {

    //Dimensions relative to diameter of the arc
    private static final float PICTURE_DIMENSIONS = 0.08f;
    private static final float STROKE_WIDTH = 0.05f;


    private float pictureSize;
    private Paint sunrisePaint;
    private Paint placeHolderPaint;
    private float percent;
    private double[] vector;
    private float[] vectorOrigin;
    private double sweepAngle;
    private int[] destVector = new int[2];
    private Bitmap sun;
    private Rect dst;
    private int initialDiameter;

    private float leftTopOffSet;
    private float rightBottomOffSet;

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

        placeHolderPaint.setAntiAlias(true);
        sunrisePaint.setColor(Color.rgb(227, 103, 0));
        sunrisePaint.setStyle(Paint.Style.STROKE);

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
            calculateSizes();
            sweepAngle = 180*percent;
            vector = new double[2];
            vector[0] = initialDiameter *(1-2*STROKE_WIDTH)/2 * Math.cos(Math.toRadians(180 - sweepAngle));
            vector[1] = -(initialDiameter *(1-2*STROKE_WIDTH)/2 * Math.sin(Math.toRadians(180 - sweepAngle)));
            destVector[0] = (int)(vector[0]+vectorOrigin[0]);
            destVector[1] = (int)(vector[1]+vectorOrigin[1]);
            float leftPos = destVector[0]-pictureSize/2-initialDiameter*STROKE_WIDTH/2;
            float topPos = destVector[1]-pictureSize/2-initialDiameter*STROKE_WIDTH/2;
            float rightPos = destVector[0]+pictureSize;
            float bottomPos = destVector[1]+pictureSize;
            dst = new Rect((int)leftPos, (int)topPos, (int)rightPos, (int)bottomPos);

            return true;
        }else return false;
    }

    private void calculateSizes() {
        sunrisePaint.setStrokeWidth(initialDiameter * STROKE_WIDTH);
        placeHolderPaint.setStrokeWidth(initialDiameter * STROKE_WIDTH);
        pictureSize = initialDiameter * PICTURE_DIMENSIONS;
        rightBottomOffSet = (int) (initialDiameter * (1f - STROKE_WIDTH));
        leftTopOffSet = (int) (initialDiameter * STROKE_WIDTH);
        vectorOrigin = new float[]{initialDiameter /2f, initialDiameter /2f};

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(sizeWidth, sizeHeight);

        initialDiameter = sizeWidth;

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(dst != null){
            canvas.drawArc(leftTopOffSet, leftTopOffSet, rightBottomOffSet, initialDiameter, -180, 180, false, placeHolderPaint);
            canvas.drawArc(leftTopOffSet, leftTopOffSet, rightBottomOffSet, initialDiameter, -180, (float) sweepAngle, false, sunrisePaint);
            canvas.drawBitmap(sun, null, dst, null);
        }
    }
}
