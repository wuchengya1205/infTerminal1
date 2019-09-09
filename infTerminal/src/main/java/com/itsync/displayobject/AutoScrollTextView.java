package com.itsync.displayobject;

import com.itsync.displaypage.DisplayItem;
import com.itsync.displaypage.DisplayItems;
import com.itsync.infterminal.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class AutoScrollTextView extends TextView {
	// public final static String TAG =
	// AutoScrollTextView.class.getSimpleName();
	public int speed;
	private float textLength = 0f;// 锟侥憋拷锟斤拷锟斤拷
	private float viewWidth = 0f;
	@SuppressWarnings("unused")
	private float _temp_view_plus_text_length = 0.0f;// 锟斤拷锟节硷拷锟斤拷锟斤拷锟绞憋拷锟斤拷锟�
	@SuppressWarnings("unused")
	private float _temp_view_plus_two_text_length = 0.0f;// 锟斤拷锟节硷拷锟斤拷锟斤拷锟绞憋拷锟斤拷锟�
	public boolean _isStarting = false;// 锟角凤拷始锟斤拷锟斤拷
	public boolean _isAssignment = true;
	private Paint _paint = null;// 锟斤拷图锟斤拷式
	private DisplayItems _displayItems;
	private FontMetrics fm;
	private int Height;
	// private float size;
	private int _TextBlank;
	private int _textColor;
	public float nextShowPos;
	private int lastX;
	private int Speed;
	private int toTop;
	private int toLeft;
	private boolean isActive;
	public static boolean ActiveMode;

	private float textCenterVerticalBaselineY;

	public AutoScrollTextView(Context context) {
		super(context);
	}

	public AutoScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(int Width, int speed1, int heigh, float fontsize,
			DisplayItems displayItems, int TextBlank, float width,
			float height, String fontType, int color, int left, int top,
			boolean acti) {
		this._textColor = color;
		float RATIO;
		float newsize = 0;
		if (MainActivity.Oritation == 0) {

			float ratioWidth = width / 2010;
			float ratioHeight = height / 1080;
			if (ratioWidth == ratioHeight) {
				RATIO = ratioWidth;
			} else {
				RATIO = Math.min(ratioWidth, ratioHeight);
			}
			// float RATIO = Math.min(ratioWidth, ratioHeight);

			newsize = Math.round(fontsize * RATIO);
		} else if (MainActivity.Oritation == 1) {
			float ratioWidth = width / 1080;
			float ratioHeight = height / 2010;
			if (ratioWidth == ratioHeight) {
				RATIO = ratioWidth;
			} else {
				RATIO = Math.min(ratioWidth, ratioHeight);
			}
			// float RATIO = Math.min(ratioWidth, ratioHeight);

			newsize = Math.round(fontsize * RATIO);
		}
		// this.size = fontsize;
		this._TextBlank = TextBlank;
		_paint = getPaint();
		_paint.setTextSize(newsize);
		// _paint.setTypeface(TextFont.getFontType(fontType));
		Typeface typeface = TextFont.getFontType(fontType);
		if (typeface != null) {
			_paint.setTypeface(typeface);
		}
		// _paint.setTypeface(Typeface.createFromFile(""));
		// _paint.setTextSize
		this._displayItems = displayItems;
		viewWidth = getWidth();
		this.toTop = top;
		this.toLeft = left;
		this.isActive = acti;
		// Height = heigh;
		if (viewWidth == 0) {
			if (_isAssignment) {
				viewWidth = Width;
				Height = heigh;
				_isAssignment = false;
			}
			Speed = speed1;
			speed = speed1;
		}

		_temp_view_plus_text_length = viewWidth + textLength;// TextView锟斤拷锟�+锟侥憋拷锟侥筹拷锟斤拷
		_temp_view_plus_two_text_length = viewWidth + textLength * 2;// TextView锟斤拷锟�
																		// +
																		// 锟斤拷锟斤拷锟侥憋拷锟侥筹拷锟斤拷
		_paint.setColor(_textColor);
		_paint.setAntiAlias(true);

		fm = _paint.getFontMetrics();
		textCenterVerticalBaselineY = Height / 2 - fm.descent
				+ (fm.descent - fm.ascent) / 2;

		for (int i = 0; i < _displayItems.getItemNum(); i++) {
			allLength += _paint.measureText(_displayItems.getItem(i).getText())
					+ _TextBlank;
		}
		System.out.println("锟杰筹拷锟斤拷" + allLength);
	}

	/**
	 * 锟斤拷始锟斤拷锟斤拷
	 */
	public void startScroll() {
		_isStarting = true;
		invalidate();

	}

	private DisplayItem _firstShownItem = null;
	public float _firstShownShift = 0;

	private float allLength;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 锟斤拷取锟斤拷前锟斤拷锟斤拷锟侥撅拷锟斤拷锟斤拷锟斤拷
		int rawX = (int) event.getRawX();
		// int rawX = (int) event.getRawX();
		int rawY = (int) event.getRawY();
		if (isActive && ActiveMode) {

			if (toTop + Height > rawY && rawY > toTop
					&& toLeft + viewWidth > rawX && rawX > toLeft) {

				// }
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 锟斤拷一锟斤拷锟诫开时锟斤拷锟斤拷锟斤拷
					speed = 0;
					lastX = rawX;
					// lastY = rawY;
					break;
				case MotionEvent.ACTION_MOVE:
					// 锟斤拷锟轿碉拷偏锟斤拷锟斤拷
					int offsetX = rawX - lastX;
					// 锟斤拷锟斤拷锟睫革拷锟较达拷锟狡讹拷锟斤拷珊锟斤拷锟斤拷锟�
					lastX = rawX;

					this._firstShownShift += offsetX;

					break;
				case MotionEvent.ACTION_UP:
					speed = Speed;
				default:
					break;
				}
			} else {
				speed = Speed;
			}
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {

		// _paint = getPaint();

		// fm = _paint.getFontMetrics();
		// float textCenterVerticalBaselineY = Height / 2 - fm.descent
		// + (fm.descent - fm.ascent) / 2;
		try {
			canvas.clipRect(0, 0, viewWidth, Height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// canvas.drawRect(new Rect(0, 0, (int)100, 100), _paint);
		synchronized (_displayItems) {
			int itemNum = _displayItems.getItemNum();
			if (itemNum > 0) {

				int firstItemIndex = _displayItems
						.findItemIndex(_firstShownItem);
				if (firstItemIndex < 0) {
					firstItemIndex = 0;
					_firstShownItem = _displayItems.getItem(0);
					_firstShownShift = viewWidth;
				}
				int nextShowItemIndex = firstItemIndex;
				nextShowPos = _firstShownShift;
				while (true) {

					DisplayItem showItem = _displayItems
							.getItem(nextShowItemIndex);
					// if (nextShowPos <= 0 {
					if (nextShowPos <= -(3 * allLength)) {
						// _firstShownItem = showItem;
						nextShowPos = -(2 * allLength);
						_firstShownShift = nextShowPos;
					}
					canvas.drawText(showItem.getText(), nextShowPos,
							textCenterVerticalBaselineY, _paint);

					float textLength = _paint.measureText(showItem.getText());

					nextShowPos += textLength + _TextBlank;

					// allLength += textLength + _TextBlank;
					nextShowItemIndex++;
					if (nextShowItemIndex >= itemNum) {
						nextShowItemIndex = 0;
					}
					if (nextShowPos > viewWidth) {
						while (nextShowItemIndex > 0) {
							_displayItems.addTail(_displayItems.removeHead());
							nextShowItemIndex--;
						}
						break;
					}

				}
			}
		}

		_firstShownShift -= speed;

		invalidate();
	}
}
