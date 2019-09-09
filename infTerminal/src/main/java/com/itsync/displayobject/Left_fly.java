package com.itsync.displayobject;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@SuppressWarnings("deprecation")
public class Left_fly extends DisplayObject {
	private int _speed;
	private int _Fontsize;
	private int _TextBlank;
	public int _viewWidth;
	public int _viewHeight;
	private int _fromeTop;
	private int _fromeLeft;
	private String _Font;
	private int _FontColor;
	private AutoScrollTextView _autoScrollTextView;
	private ImageView _bacImageView;
	private String _filename;
	private String _file;
	private Bitmap _bitmap;
	private int _isInterActive;
	private boolean _isActive;

	// private int _HexColorNum;

	public Left_fly() {
		_viewWidth = 1920;
		_viewHeight = 1080;
		_autoScrollTextView = null;
		_Font = null;
		_isInterActive = 1;
		_isActive = true;
	}

	public boolean hasPlaylist() {
		return true;
	}

	@SuppressLint({ "ResourceAsColor", "ClickableViewAccessibility" })
	@Override
	public void initialize(final Context ctrlContext, Point screenSize,
			ViewGroup rootLayout) {

		String text = "";
		
		if (!(_displayItems.getItemNum() == 0)) {
			for (int i = 0; i < _displayItems.getItemNum(); i++) {
				text += "   " + _displayItems.getItem(i).getText();
			}
		}
		if (_isInterActive == 1) {
			_isActive = true;
		}else{
			_isActive = false;
		}
		_bacImageView = new ImageView(ctrlContext);
		_bacImageView.setScaleType(ScaleType.FIT_XY);
		AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
				(int) (screenSize.x * _width), (int) (screenSize.y * _height),
				(int) (screenSize.x * _left), (int) (screenSize.y * _top));
		_bacImageView.setLayoutParams(params1);
		rootLayout.addView(_bacImageView);
		_FontColor = _FontColor | 0xff000000;
		_autoScrollTextView = new AutoScrollTextView(ctrlContext);
		_autoScrollTextView.setLayerType(View.LAYER_TYPE_NONE, null);
		
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		_fromeLeft = (int) (screenSize.x * _left);
		_fromeTop = (int) (screenSize.y * _top);
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				AbsoluteLayout.LayoutParams.FILL_PARENT,
				AbsoluteLayout.LayoutParams.MATCH_PARENT,
				(int) (screenSize.x * _left), (int) (screenSize.y * _top));
//        _autoScrollTextView.setEllipsize(TruncateAt.MARQUEE);
		_autoScrollTextView.setLayoutParams(params);
		_autoScrollTextView.setText(text);
		_autoScrollTextView.setBackgroundColor(Color.argb(0, 0, 0, 0));
		_autoScrollTextView.setGravity(Gravity.CENTER);
		_autoScrollTextView.init(_viewWidth, _speed, _viewHeight, _Fontsize,
				this._displayItems, _TextBlank, screenSize.x, screenSize.y,
				_Font, _FontColor,_fromeLeft,_fromeTop,_isActive);
//		_autoScrollTextView.startScroll();

		rootLayout.addView(_autoScrollTextView);
		String url = StaticImage._imageFolder;
		// if (!(_filename.trim().isEmpty())) {
		_file = url + _filename;

//		File file = new File(_file);
		if (!_filename.equalsIgnoreCase("")) {
			_bitmap = BitmapFactory.decodeFile(_file);
			// }
		} else {
			_bitmap = null;
		}
		final ImageView bacImageView = _bacImageView;
		rootLayout.post(new Runnable() {

			@Override
			public void run() {
				if (!(_bitmap == null)) {
					bacImageView.setImageBitmap(_bitmap);
				}
			}
		});
	}

	@Override
	public void uninitialize() {

		_bacImageView = null;
		_autoScrollTextView = null;
		if (_bitmap != null) {
			_bitmap.recycle();
		}
		_bitmap = null;
//		System.gc();

	}

	static public Left_fly Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("left_fly_text"))
			return null;

		Left_fly retValue = new Left_fly();
		retValue.LoadBaseInfo(xpp);

		retValue._FontColor = Integer.parseInt(xpp.getAttributeValue(null,
				"FontColor"));
		retValue._Font = xpp.getAttributeValue(null, "Font");
		retValue._speed = Integer
				.parseInt(xpp.getAttributeValue(null, "Speed"));
		retValue._Fontsize = Integer.parseInt(xpp.getAttributeValue(null,
				"FontSize"));
		retValue._TextBlank = Integer.parseInt(xpp.getAttributeValue(null,
				"TextBlankSuffix"));
		retValue._filename = xpp.getAttributeValue(null, "BackgroundImageFile");
		if (retValue._filename != null)
			retValue._filename = retValue._filename.replace('\\', '/');
		retValue._isInterActive = Integer.parseInt(xpp.getAttributeValue(null, "Interactive"));
		return retValue;
	}

	@Override
	public void stopWork() {
		// TODO Auto-generated method stub
		
	}

}
