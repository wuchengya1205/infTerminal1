package com.itsync.displayobject;

import com.itsync.infterminal.MainActivity;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextConfig extends TextView {

	private Paint paint = null;

	// private float size;
	public TextConfig(Context context) {
		super(context);
	}

	public TextConfig(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public TextConfig(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public void init(float textsize, float width, float height, String fontType) {
		float newsize = 0;
		if (MainActivity.Oritation == 0) {

			float RATIO;
			float ratioWidth = width / 1920;
			float ratioHeight = height / 1080;
			if (ratioWidth == ratioHeight) {
				RATIO = ratioWidth;
			} else {
				RATIO = Math.min(ratioWidth, ratioHeight);
			}
			newsize = Math.round(textsize * RATIO);
		}else if (MainActivity.Oritation ==1) {
			float RATIO;
			float ratioWidth = width / 1080;
			float ratioHeight = height / 1920;
			if (ratioWidth == ratioHeight) {
				RATIO = ratioWidth;
			} else {
				RATIO = Math.min(ratioWidth, ratioHeight);
			}
			newsize = Math.round(textsize * RATIO);
		}
		paint = getPaint();
		// AssetManager assetManager = getContext().getAssets();
		Typeface typeface = TextFont.getFontType(fontType);
		if (typeface != null) {
			paint.setTypeface(typeface);
		}
		paint.setTextSize(newsize);
		invalidate();
	}

}
