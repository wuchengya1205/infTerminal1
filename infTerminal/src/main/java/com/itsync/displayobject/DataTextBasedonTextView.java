package com.itsync.displayobject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.widget.TextView;

public class DataTextBasedonTextView extends TextView {

	private List<String> list;
	private String _text;
	// private Paint paint = null;
	private Paint paint = null;
	private float step = 0f;
	private int _textHeight;
	private int _viewHeight;
	public  boolean _isUpFly;
	private int _textColor;

	public DataTextBasedonTextView(Context context) {
		super(context);
		
	}

	public DataTextBasedonTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public DataTextBasedonTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
	}

	public void init(int viewWidth, int viewHeight, String text,
			float textsize, float width, float height, String fontType,
			int Fontcolor) {
		this._textColor = Fontcolor;
		this._viewHeight = viewHeight;

		float RATIO;
		float ratioWidth = width / 1920;
		float ratioHeight = height / 1080;
		if (ratioWidth == ratioHeight) {
			RATIO = ratioWidth;
		} else {
			RATIO = Math.min(ratioWidth, ratioHeight);
		}
		float newsize = Math.round(textsize * RATIO);
		paint = getPaint();
		// AssetManager assetManager = getContext().getAssets();
		paint.setTypeface(TextFont.getFontType(fontType));
		paint.setTextSize(newsize);

		// paint = getPaint();
		float length = 0;
		this._text = text;
		
		list = new ArrayList<>();
		list.clear();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < _text.length(); i++) {
			// Log.e("textviewscroll",""+i+text.charAt(i));
			if (length < viewWidth) {
				builder.append(text.charAt(i));
				length += paint.measureText(text.substring(i, i + 1));
				if (i == text.length() - 1) {
					// Log.e("textviewscroll",""+i+text.charAt(i));
					list.add(builder.toString());
				}
			} else {
				list.add(builder.toString().substring(0,
						builder.toString().length() - 1));
				builder.delete(0, builder.length() - 1);
				length = paint.measureText(text.substring(i, i + 1));
				i--;
			}
		}

		FontMetrics fm = paint.getFontMetrics();
		_textHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);

		if (_textHeight * list.size() > _viewHeight) {
			_isUpFly = true;
		} else {
			_isUpFly = false;
		}
		
		
	}



	@Override
	public void onDraw(Canvas canvas) {

		if (_isUpFly) {
			paint.setColor(_textColor);
			if (list != null) {
				
			
			if (list.size() == 0)
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				canvas.drawText(list.get(i), 0, this.getHeight() + (i + 1)
						* paint.getTextSize() - step, getPaint());
			}

			invalidate();
			step = step + 3f;
			if (step >= _viewHeight + list.size() * paint.getTextSize()) {
				step = 0;
			}
		} else {
			super.onDraw(canvas);
//			paint.setColor(_textColor);
//			if (DataText._position == 16) {
//				canvas.drawText(_text, 0, textCenterVerticalBaselineY, paint);
//			}else if (DataText._position == 0) {
//				canvas.drawText(_text, 0, textCenterVerticalBaselineY0, paint);
//			}
			
			
		}
	}

}
