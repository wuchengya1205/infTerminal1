package com.itsync.displayobject;

import android.content.Context;
import android.widget.VideoView;

public class Video extends VideoView {

	public Video(Context context) {
		super(context);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = getDefaultSize(getWidth(), widthMeasureSpec);
		int height = getDefaultSize(getHeight(), heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

}
