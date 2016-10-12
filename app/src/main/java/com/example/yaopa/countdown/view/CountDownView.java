package com.example.yaopa.countdown.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.yaopa.countdown.R;


/**
 * 作者：yaohe on 2016/10/12 09:36
 * <p>
 * 邮箱：yaohe@yilinker.com
 */

public class CountDownView extends View {
    private static final String TAG = CountDownView.class.getSimpleName();
    private static final int BACKGROUND_COLOR = 0x50555555;
    private static final float BORDER_WIDTH = 15f;
    private static final int BORDER_COLOR = 0xFF6ADBFE;
    private static final String TEXT = "跳过广告";
    private static final float TEXT_SIZE = 50f;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private int mBackgroundColor;
    private int mBorderColor;
    private int mTextColor;
    private String text;
    private float mBorderWidth;
    private float mTextSize;

    private StaticLayout mStaticLayout;
    private Paint circlePaint, borderPaint;

    private int progress;
    private float time;
    private int initProgress;

    private CountDownTimer countDownTimer;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        mBackgroundColor = typedArray.getColor(R.styleable.CountDownView_background_color, BACKGROUND_COLOR);
        mBorderColor = typedArray.getColor(R.styleable.CountDownView_border_color, BORDER_COLOR);
        mTextColor = typedArray.getColor(R.styleable.CountDownView_text_color, TEXT_COLOR);
        text = typedArray.getString(R.styleable.CountDownView_text);
        mBorderWidth = typedArray.getDimension(R.styleable.CountDownView_border_width, BORDER_WIDTH);
        mTextSize = typedArray.getDimension(R.styleable.CountDownView_text_size, TEXT_SIZE);

        if (text == null) {
            text = TEXT;
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        initProgress = 50;
        progress = initProgress;

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setColor(mBackgroundColor);
        circlePaint.setStyle(Paint.Style.FILL);

        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setDither(true);
        borderPaint.setColor(mBorderColor);
        borderPaint.setStrokeWidth(mBorderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);

        int textWidth = (int) textPaint.measureText(text.substring(0, (text.length() + 1) / 2));
        mStaticLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = mStaticLayout.getWidth();
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = mStaticLayout.getHeight();
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int min = Math.min(width, height);
        //画底盘
        canvas.drawCircle(width / 2, height / 2, min / 2, circlePaint);
        //画边框
        RectF rectF;
        if (width > height) {
            rectF = new RectF(width / 2 - min / 2 + mBorderWidth / 2, 0 + mBorderWidth / 2, width / 2 + min / 2 - mBorderWidth / 2, height - mBorderWidth / 2);
        }
        else {
            rectF = new RectF(mBorderWidth / 2, height / 2 - min / 2 + mBorderWidth / 2, width - mBorderWidth / 2, height / 2 - mBorderWidth / 2 + min / 2);
        }
        canvas.drawArc(rectF, -90, progress, false, borderPaint);

        //画文字
        canvas.translate(width / 2, height / 2 - mStaticLayout.getHeight() / 2);
        mStaticLayout.draw(canvas);
    }

    public void start() {
        if (listener != null) {
            listener.onStartCount();
        }
        if (time == 0f) time = 3f;
        final long l = (long) (time * 1000);

        countDownTimer = new CountDownTimer(l, 36) {
            @Override
            public void onTick(long millisUntilFinished) {
                /*progress = initProgress + (int) (((2600 - millisUntilFinished) * 1.0f / 3600) * 360);*/
                progress = initProgress + (int) (360 * 360 * 1.0f * (l - millisUntilFinished) / (360 + initProgress) / l);
                Log.e(TAG, "progress: " + progress + "    millisUntilFinished:" + millisUntilFinished);
                invalidate();
            }

            @Override
            public void onFinish() {
                progress = 360;
                invalidate();
                if (listener != null) {
                    listener.onFinishCount();
                }
            }
        }.start();
    }

    public void stop() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }





    
    public interface CountDownListner {
        void onStartCount();

        void onFinishCount();
    }

    private CountDownListner listener;

    public void setCountListener(CountDownListner listener) {
        this.listener = listener;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
