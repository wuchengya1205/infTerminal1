package com.itsync.displayobject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("deprecation")
public class CountDown extends DisplayObject {

	public int _viewWidth;
	public int _viewHeight;
	private String _Font;
	private int _FontColor;
	private int _Fontsize;
	private int _position;
	private Thread _thread;
	private boolean _work;
	private TextConfig _countDown;
	private ViewGroup _rootLayout;
	private String _date;
	private String _filename;
	private String _file;
	private ImageView _bacImageView;
	private Bitmap _bitmap;

	public CountDown() {
		_viewHeight = 1080;
		_viewWidth = 1920;
		_work = false;
		_rootLayout = null;
	}

	@Override
	public void initialize(final Context ctrlContext, Point screenSize,
			ViewGroup rootLayout) {

		_countDown = new TextConfig(ctrlContext);
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		_bacImageView = new ImageView(ctrlContext);
		_bacImageView.setScaleType(ScaleType.FIT_XY);
		_rootLayout = rootLayout;
		if (_position == 0) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_countDown.setLayoutParams(params1);
			_countDown.setGravity(Gravity.START);
		} else if (_position == 1) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_countDown.setLayoutParams(params1);
			// _countDown.setGravity(Gravity.TOP);
			_countDown.setGravity(Gravity.CENTER | Gravity.TOP);
		} else if (_position == 2) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_countDown.setLayoutParams(params1);
			_countDown.setGravity(Gravity.END | Gravity.TOP);
		} else if (_position == 16) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_countDown.setLayoutParams(params1);
			_countDown.setGravity(Gravity.START | Gravity.CENTER);
		} else if (_position == 17) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_countDown.setLayoutParams(params1);
			_countDown.setGravity(Gravity.CENTER);
		} else if (_position == 18) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_countDown.setLayoutParams(params1);
			_countDown.setGravity(Gravity.END | Gravity.CENTER);
		} else if (_position == 32) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_countDown.setLayoutParams(params1);
			_countDown.setGravity(Gravity.BOTTOM | Gravity.START);
		} else if (_position == 33) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_countDown.setLayoutParams(params1);
			_countDown.setGravity(Gravity.BOTTOM | Gravity.CENTER);
		} else if (_position == 34) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_countDown.setLayoutParams(params1);
			_countDown.setGravity(Gravity.END | Gravity.BOTTOM);
		}
		_FontColor = _FontColor | 0xff000000;
		_countDown.setSingleLine(true);
		_countDown.setBackgroundColor(Color.argb(0, 0, 0, 0));
		// _countDown.setGravity(Gravity.CENTER);
		_countDown.init(_Fontsize, screenSize.x, screenSize.y, _Font);
	
		_countDown.setTextColor(_FontColor);

		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				(int) (screenSize.x * _width), (int) (screenSize.y * _height),
				(int) (screenSize.x * _left), (int) (screenSize.y * _top));
		_bacImageView.setLayoutParams(params);
		rootLayout.addView(_bacImageView);
		rootLayout.addView(_countDown);
		_work = true;
		_thread = new Thread(new Runnable() {

			@Override
			public void run() {

				long daysBetween = 0;
				String url = StaticImage._imageFolder;
				// if (!(_filename.trim().isEmpty())) {
				_file = url + _filename;
				File file = new File(_file);
				if (file.exists()) {
					_bitmap = BitmapFactory.decodeFile(_file);
					// }
				} else {
					_bitmap = null;
				}
				while (_work) {

					final String shouldshowText = _date;

					Calendar cal = Calendar.getInstance();
					final int day = cal.get(Calendar.DATE);
					final int month = cal.get(Calendar.MONTH) + 1;
					final int year = cal.get(Calendar.YEAR);
					String currentDate = year + "-" + month + "-" + day;
					SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date date1 = matter.parse(currentDate);
						Date date2 = matter.parse(shouldshowText);
						daysBetween = (date2.getTime() - date1.getTime() + 1000000)
								/ (3600 * 24 * 1000);
						if (daysBetween <= 0) {
							daysBetween = 0;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					final String shouldshow = Long.toString(daysBetween);
                    final TextConfig contDown = _countDown;
                    final ImageView _bacimageView = _bacImageView;
					_rootLayout.post(new Runnable() {

						@Override
						public void run() {
							if (_bitmap != null) {
								_bacimageView.setImageBitmap(_bitmap);
							}
							if (!(contDown == null || shouldshow == null)) {

								contDown.setText(shouldshow);
							}
						}
					});

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		});
		_thread.start();

	}

	@Override
	public void uninitialize() {

		_work = false;
		if (null != _thread) {
			try {
				_thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			_thread = null;
		}

		_bacImageView = null;
		_countDown = null;
		if (_bitmap != null) {
			_bitmap.recycle();
		}
		_bitmap = null;
		_rootLayout = null;
//		System.gc();
	}

	static public CountDown Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("count_down"))
			return null;

		CountDown retValue = new CountDown();
		retValue.LoadBaseInfo(xpp);

		retValue._FontColor = Integer.parseInt(xpp.getAttributeValue(null,
				"FontColor"));
		retValue._Font = xpp.getAttributeValue(null, "Font");
		retValue._Fontsize = Integer.parseInt(xpp.getAttributeValue(null,
				"FontSize"));
		retValue._position = Integer.parseInt(xpp.getAttributeValue(null,
				"Align"));
		retValue._date = xpp.getAttributeValue(null, "EndDate");
		retValue._filename = xpp.getAttributeValue(null, "BackgroundImageFile");
		if (retValue._filename != null)
			retValue._filename = retValue._filename.replace('\\', '/');
		return retValue;
	}

	@Override
	public void stopWork() {
		// TODO Auto-generated method stub
		_work = false;
		_thread.interrupt();
		
	}

}
