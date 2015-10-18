package com.secb.android.view.components.dialogs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.secb.android.view.UiEngine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ProgressWheel extends View
{
    private static int SPACE_WIDTH=3;
    private int radius = 0;

	// Sizes (with defaults)
	private int fullRadius = 100;
	private int circleRadius = 80;
	private int barLength = 0;
	private int barWidth = 20;
	private int rimWidth = 20;
	private int percentageTextSize = 50;
	private int overHundredTextSize = 30;
	
	private  int timeTextColor = 0xFF000000;
	private final int timePlaceHolderTextColor = 0xFFFFFFFF;
	
	// Padding (with defaults)
	private int paddingTop = 5;
	private int paddingBottom = 5;
	private int paddingLeft = 5;
	private int paddingRight = 5;
	private int textPadding = 10;

	// loading time
	private int loadingTotalTimeInMillis = 0;
	private long loadingMillis = 0;
	private long loadingInterval = 0;
	
	// Rectangles
	private RectF circleBounds = new RectF();

	// Paints
	private Paint barPaint = new Paint();
	private Paint percentageTextPaint = new Paint();
	private Paint overHundredTextPaint = new Paint();
    private Paint shadeCirclePaint = new Paint();

	// The amount of pixels to move the bar by on each draw
	private int spinSpeed = 2;
	// The number of milliseconds to wait inbetween each draw
	private int delayMillis = 0;
	boolean isSpinning = false;
	private int progress = 0;
	private String splitText = "";
	private String placeHolderTime = "";
    private String percentageStr;
    private int degrees;
    private int percentageNumber;


	private int wheel_fill_color;

	public ProgressWheel(Context context)
	{
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ProgressWheel(Context context, AttributeSet attrs)
	{
		super(context, attrs);

	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		init();
	}

	/*
	 * Initialize the view
	 */
	private void init()
	{
        percentageTextSize =(int) (this.getLayoutParams().height * 0.32);
        overHundredTextSize = (int) (this.getLayoutParams().height * 0.1);
        barWidth = (int) (this.getLayoutParams().height * 0.08);

		//Logger.instance().v("Circle", "Width: "+this.getLayoutParams().width+" - Height: "+this.getLayoutParams().height, false);
		circleBounds = new RectF(paddingLeft + barWidth, 
			paddingTop + barWidth, 
			this.getLayoutParams().width - paddingRight - barWidth, 
			this.getLayoutParams().height - paddingBottom - barWidth);

		// barPaint.setColor(barColor);
		barPaint.setAntiAlias(true);
		barPaint.setStyle(Style.STROKE);
		barPaint.setStrokeWidth(barWidth+5);

		changeDegreeColors();


//		barPaint.setShader(new SweepGradient(getResources().getDimension(R.dimen.home_total_score_circle_height_width)/2,getResources().getDimension(R.dimen.home_total_score_circle_height_width)/2,
//				new int[]{Color.parseColor("#e84f3d"), Color.parseColor("#feac5d"), Color.parseColor("#fce378"), Color.parseColor("#84d497"), Color.parseColor("#34a68a")},
//				new float[]{0.0f, 0.25f, 0.5f, 0.75f, 1.0f}));

//
        percentageTextPaint.setColor(timeTextColor);
        percentageTextPaint.setStyle(Style.FILL);
        percentageTextPaint.setAntiAlias(true);
        percentageTextPaint.setTypeface(UiEngine.Fonts.BDCN);

        overHundredTextPaint.setColor(timePlaceHolderTextColor);
        overHundredTextPaint.setStyle(Style.FILL);
        overHundredTextPaint.setAntiAlias(true);
        overHundredTextPaint.setTypeface(UiEngine.Fonts.BDCN);

        shadeCirclePaint.setAntiAlias(true);
        shadeCirclePaint.setStyle(Style.STROKE);
        shadeCirclePaint.setStrokeWidth(barWidth-7);
        shadeCirclePaint.setColor(wheel_fill_color);
        //shadeCirclePaint.setColor(Color.parseColor("#ffffff"));


        overHundredTextPaint.setTextSize(overHundredTextSize);
        percentageTextPaint.setTextSize(percentageTextSize);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{


		// canvas.drawArc(circleBounds, progress - 180, barLength, false, barPaint);
//		canvas.drawCircle((circleBounds.width() / 2) + rimWidth + paddingLeft, (circleBounds.height() / 2) + rimWidth
//		  + paddingTop, circleRadius, barPaint);
        //canvas.drawCircle(getWidth()/2, getWidth()/2, (getWidth()/2)-barWidth, shadeCirclePaint);

//		canvas.drawArc(circleBounds, -90, 360, false, shadeCirclePaint);
		canvas.drawArc(circleBounds, 270, 360, false, shadeCirclePaint);
//
// Logger.instance().v("barLength", "Length: "+barLength+", percentageNumber: "+percentageNumber);
        if(barLength !=0 && percentageNumber != 0)
		{
//			canvas.drawArc(circleBounds, -90, barLength, false, barPaint);
			canvas.drawArc(circleBounds, 90, barLength, false, barPaint);
		}

        int percentage=(int)Math.ceil(((barLength) / 360.0f) * 100.0f);
        if(percentage > percentageNumber)
            percentage = percentageNumber;

        percentageStr=percentageNumber != 0 ? (""+percentage) : "N.D";//placeHolderTime;//""+((barLength/360)*100);
        String over100Str=percentageNumber != 0 ? "/100" : "";
		// Draw the text
		float over100Width = overHundredTextPaint.measureText(over100Str);
        float percentageWidth = percentageTextPaint.measureText(percentageStr);

        Rect percentageBounds = new Rect();
        percentageTextPaint.getTextBounds(percentageStr, 0, percentageStr.length(), percentageBounds);

		if(percentageNumber != 0) {
			Rect over100eBounds = new Rect();
			overHundredTextPaint.getTextBounds(over100Str, 0, percentageStr.length(), over100eBounds);
		}

        float percentageX = (getWidth()-(percentageWidth+SPACE_WIDTH+over100Width))/2;

		float over100X= percentageX+percentageWidth+SPACE_WIDTH;

        float percentageY = (this.getHeight()+percentageBounds.height())/2;
        float over100Y =percentageY;//+(percentageBounds.height()-over100eBounds.height());
        //float percentageY = (this.getHeight()+percentageTextPaint.getTextSize())/2;
        //float over100Y =percentageY;//+(percentageTextPaint.getTextSize()-overHundredTextPaint.getTextSize());

//        canvas.drawText(percentageStr, percentageX, percentageY , percentageTextPaint);
//        canvas.drawText(over100Str, over100X, over100Y, overHundredTextPaint);

//		canvas.drawText(percentageStr,percentageX, getHeight()/2 , percentageTextPaint);

	}

	public void reset() {
		progress = 0;
		barLength = 0;
		isSpinning = false;
	}
	
	/**
	 * Increment the progress by 1 (of 360)
	 */
	int seconds = 0, minutes = 0;
	private void incrementProgress()
	{
//		Logger.instance().v("ProgressWheel", "Progress: "+progress+" Text: "+percentageStr+" ParLength: "+barLength, false);
		isSpinning = true;
		progress++;
		barLength ++;
		loadingMillis = (progress * loadingInterval);
		
		initText();
		changeDegreeColors();
		// setText(Math.round(((float)progress/360)*100) + "%");
		spinHandler.sendEmptyMessage(0);
	}
	
	private void initText()
    {
		seconds = (int) (loadingMillis / 1000) % 60 ;
		minutes = (int) ((loadingMillis / (1000*60)) % 60);
		
		splitText = (((minutes <= 9)? "0" : "")+minutes +" : "+ ((seconds <= 9)? "0" : "")+seconds);

        //TODO
        percentageStr=""+((barLength/360)*100);
    }

	public void startLoading(int percentage, int loadingTimeInMillis, String placeHolderTime , int _radius)
	{
		radius = _radius;
		changeDegreeColors();
		if(percentage == 0 || percentageNumber != percentage)
		{
			setPercentage(percentage);
			this.loadingTotalTimeInMillis = loadingTimeInMillis;
			this.placeHolderTime = placeHolderTime;
			startTimerLoading();
		}
	}

	public void setColors(int wheelColor , int textColor){
		wheel_fill_color = wheelColor;
		timeTextColor=textColor;
	}
	public void changeDegreeColors()
	{
//solid
		int color = UiEngine.getValueColor(this.percentageNumber);
		color = wheel_fill_color;

		barPaint.setShader(new SweepGradient(0, 0,
				new int[]{color, color, color, color, color},
				new float[]{0.0f, 0.25f, 0.5f, 0.75f, 1.0f}));
//		shadeCirclePaint.setColor(wheel_fill_color);
//gradient
/*		int[] colors ={ Color.parseColor("#e84f3d"), Color.parseColor("#fee377"),Color.parseColor("#3ac19c")};
		float[] positions ={0.35f,0.75f,1.0f};
		SweepGradient sweepGradient  = new SweepGradient(radius, radius,colors,null);
		Matrix matrix = new Matrix();
		matrix.setRotate(270 ,radius, radius);
		sweepGradient.setLocalMatrix(matrix);
		barPaint.setShader(sweepGradient );*/

	}
	/*
	 * *********************************************************************
	 * ************************ Timer to load ******************************
	 * *********************************************************************
	 */
	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> schedFeature;
	private Runnable refereshJob = new Runnable()
	{
		@Override
		public void run()
		{
			incrementProgress();
		}
	};
	
	private void startTimerLoading() {
		reset();
		stopTimerLoading();
		loadingInterval = (loadingTotalTimeInMillis/360);
//		Logger.instance().v("Circle Timer", " >> Start Timer", false);
		scheduler = Executors.newScheduledThreadPool(1);
		schedFeature = scheduler.scheduleAtFixedRate(refereshJob, loadingInterval, loadingInterval, TimeUnit.MILLISECONDS);
	}
	private void stopTimerLoading() {
//		Logger.instance().v("Circle Timer", " << Stop Timer", false);
		if(schedFeature != null)
			schedFeature.cancel(true);
		
		if (scheduler != null) {
			scheduler.shutdownNow();
		}
		scheduler = null;
		schedFeature = null;
	}

    public void setPercentage(int percentage) {
        percentageNumber=percentage;
        this.degrees = (percentage*360)/100;
    }

    private Handler spinHandler = new Handler() {
		@Override
		public void handleMessage(Message msg)
		{
			invalidate();
			if (isSpinning)
			{
//				progress += spinSpeed;
				if (progress > degrees)
				{
//					progress = 0;
					loadingMillis = loadingTotalTimeInMillis;
					initText();
					stopTimerLoading();
					invalidate();
				}
//				spinHandler.sendEmptyMessageDelayed(0, delayMillis);
			}
			// super.handleMessage(msg);
		}
	};
}
