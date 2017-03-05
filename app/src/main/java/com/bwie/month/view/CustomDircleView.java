package com.bwie.month.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bwie.month.R;

/**
 * Created by lenovo on 2017/2/22.
 */

public class CustomDircleView extends View {

    Paint mDefaultPaint = new Paint();
    private Region rectRegion,outRegion,inRegion;
    private Path rectPath,outPath,inPath;
    private int textSize;
    private String middleTxt;
    private int outCirRadius;
    private int inCirRadius;
    private int circleColor;
    private int x;
    private int y;

    public CustomDircleView(Context context) {
        super(context);
        initViews();
    }

    public CustomDircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomDircleView);
        if (typedArray != null) {
            //字体大小
            textSize = typedArray.getDimensionPixelSize(R.styleable.CustomDircleView_textSize, 20);
            //中间的文字
            middleTxt = typedArray.getString(R.styleable.CustomDircleView_middleTxt);
            //外圆半径
            outCirRadius = typedArray.getInteger(R.styleable.CustomDircleView_outCirRadius, 60);
            //内圆半径
            inCirRadius = typedArray.getInteger(R.styleable.CustomDircleView_inCirRadius, 40);
            //圆环的颜色
            circleColor = typedArray.getColor(R.styleable.CustomDircleView_circleColor, Color.YELLOW);
            typedArray.recycle();
        }

    }

    //初始化区域和路径
    private void initViews() {
        rectRegion = new Region();
        outRegion = new Region();
        inRegion = new Region();
        rectPath = new Path();
        outPath = new Path();
        inPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path rect = rectPath;
        Path outCir = outPath;
        Path inCir = inPath;
        //矩形的颜色
        mDefaultPaint.setColor(Color.GREEN);
        canvas.drawPath(rect,mDefaultPaint);
        //外圆
        mDefaultPaint.setColor(circleColor);
        canvas.drawPath(outCir,mDefaultPaint);
        //内圆
        mDefaultPaint.setColor(Color.WHITE);
        canvas.drawPath(inCir,mDefaultPaint);
        mDefaultPaint.setColor(Color.BLACK);
        mDefaultPaint.setTextSize(textSize);
        canvas.drawText(middleTxt,x/2-20,y/2+10,mDefaultPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if(inRegion.contains(x,y)){
                    Toast.makeText(getContext(),"在小圆内",Toast.LENGTH_SHORT).show();
                }else if(outRegion.contains(x,y)){
                    Toast.makeText(getContext(),"在圆环内",Toast.LENGTH_SHORT).show();
                }else if(rectRegion.contains(x,y)){
                    Toast.makeText(getContext(),"在圆环外",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        x = w;
        y = h;
        rectPath.addRect(w/2-outCirRadius,h/2-outCirRadius,w/2+outCirRadius,h/2+outCirRadius,Path.Direction.CW);
        outPath.addCircle(w/2,h/2,outCirRadius,Path.Direction.CW);
        inPath.addCircle(w/2,h/2,inCirRadius,Path.Direction.CW);
        // ▼将剪裁边界设置为视图大小
        Region globalRegion = new Region(-w, -h, w, h);
        // ▼将 Path 添加到 Region 中
        rectRegion.setPath(rectPath, globalRegion);

        outRegion.setPath(outPath, globalRegion);
        inRegion.setPath(inPath, globalRegion);
    }
}
