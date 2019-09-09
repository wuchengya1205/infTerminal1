package com.itsync.displayobject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class HandWriteView extends View {

	
	private float mPreX;
	private float mPreY;

	public HandWriteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeWidth(5f);
	}


	private boolean clear = false;
	private Paint paint = new Paint();
	private Path path = new Path();

	public HandWriteView(Context context) {
		super(context); 

		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
	}

	public void init(int width,int color) {
		paint.setStrokeWidth(width);
		paint.setColor(color);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		if (clear) {
			Paint paint = new Paint();   
			path.reset();
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));    
			canvas.drawPath(path,paint);    
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC)); 
			clear = false;
			return;
		}
		canvas.drawPath(path, paint);
	}

	public void clear() {
		clear = true;
		invalidate();
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		float eventX = event.getX();
//		float eventY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			path.moveTo(eventX, eventY);
			path.moveTo(event.getX(),event.getY());  
            mPreX = event.getX();  
            mPreY = event.getY(); 
			return true;
		case MotionEvent.ACTION_MOVE:
			float endX = (mPreX+event.getX())/2;  
            float endY = (mPreY+event.getY())/2;  
            path.quadTo(mPreX,mPreY,endX,endY);  
            mPreX = event.getX();  
            mPreY =event.getY(); 
//		case MotionEvent.ACTION_UP:
//			path.lineTo(eventX, eventY);
            invalidate();
			break;
		default:
			return false;
		}

		
		return true;
	}
}
