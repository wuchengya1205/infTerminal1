package com.itsync.displayobject;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

public class WeatherText extends TextView {

	private float textsize;
	private Paint paint = null;
	
	
	public WeatherText(Context context) {
		super(context);
		
	}
	
	public void init(float size) {
		
		this.textsize = size;
		paint = getPaint();
		paint.setTextSize(textsize);
		paint.setColor(0xffffffff);
		
	}
	

}
