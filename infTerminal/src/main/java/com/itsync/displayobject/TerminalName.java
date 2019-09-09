package com.itsync.displayobject;

import java.io.File;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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

@SuppressWarnings("deprecation")
public class TerminalName extends DisplayObject {

	public int _viewWidth;
	public int _viewHeight;
	private String _Font;
	private int _FontColor;
	private int _Fontsize;
	public static String _terminalName;
	private int _position;
	private Thread _thread;
	private boolean _work;
	private TextConfig _TerminalName;
	private ViewGroup _rootLayout;
	private ImageView _bacImageView;
	private String _fileName;
	private String _file;
	private Bitmap _bitmap;

	public TerminalName() {

		_viewWidth = 1920;
		_viewHeight = 1080;
		_work = false;

	}

	@Override
	public void initialize(Context ctrlContext, Point screenSize,
			ViewGroup rootLayout) {
		_bacImageView = new ImageView(ctrlContext);
		_TerminalName = new TextConfig(ctrlContext);
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		_rootLayout = rootLayout;
		_bacImageView.setScaleType(ScaleType.FIT_XY);
		if (_position == 0) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_TerminalName.setLayoutParams(params1);
			_TerminalName.setGravity(Gravity.START);
		} else if (_position == 1) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_TerminalName.setLayoutParams(params1);
			// _TerminalName.setGravity(Gravity.TOP);
			_TerminalName.setGravity(Gravity.CENTER | Gravity.TOP);
		} else if (_position == 2) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_TerminalName.setLayoutParams(params1);
			_TerminalName.setGravity(Gravity.END | Gravity.TOP);
		} else if (_position == 16) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_TerminalName.setLayoutParams(params1);
			_TerminalName.setGravity(Gravity.START | Gravity.CENTER);
		} else if (_position == 17) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_TerminalName.setLayoutParams(params1);
			_TerminalName.setGravity(Gravity.CENTER);
		} else if (_position == 18) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_TerminalName.setLayoutParams(params1);
			_TerminalName.setGravity(Gravity.END | Gravity.CENTER);
		} else if (_position == 32) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_TerminalName.setLayoutParams(params1);
			_TerminalName.setGravity(Gravity.BOTTOM | Gravity.START);
		} else if (_position == 33) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_TerminalName.setLayoutParams(params1);
			_TerminalName.setGravity(Gravity.BOTTOM | Gravity.CENTER);
		} else if (_position == 34) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_TerminalName.setLayoutParams(params1);
			_TerminalName.setGravity(Gravity.END | Gravity.BOTTOM);
		}
		_FontColor = _FontColor | 0xff000000;
		_TerminalName.setBackgroundColor(Color.argb(0, 0, 0, 0));
		// _TerminalName.setGravity(Gravity.CENTER);
		_TerminalName.init(_Fontsize, screenSize.x, screenSize.y, _Font);
		_TerminalName.setTextColor(_FontColor);

		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				(int) (screenSize.x * _width), (int) (screenSize.y * _height),
				(int) (screenSize.x * _left), (int) (screenSize.y * _top));
		_bacImageView.setLayoutParams(params);

		rootLayout.addView(_bacImageView);
		rootLayout.addView(_TerminalName);
		_work = true;
		_thread = new Thread(new Runnable() {

			@Override
			public void run() {
				String terminal = null;
				String url = StaticImage._imageFolder;
				// if (!(_fileName.trim().isEmpty())) {
				_file = url + _fileName;

				File file = new File(_file);
				if (file.exists()) {
					_bitmap = BitmapFactory.decodeFile(_file);
					// }

				} else {
					_bitmap = null;
				}
				while (_work) {

					if (_terminalName != null && !_terminalName.equals(terminal)) {
						terminal = _terminalName;
						System.out.println(String.format(
								"TerminalName  %s => %s", terminal, _terminalName));

						final String textname = _terminalName;
                        final TextConfig terminalName = _TerminalName;
                        final ImageView bacImageView = _bacImageView;
						_rootLayout.post(new Runnable() {

							@Override
							public void run() {
								if (!(_bitmap == null || bacImageView == null)) {
									bacImageView.setImageBitmap(_bitmap);
								}
								if (!(terminalName == null || textname == null)) {
									
									terminalName.setText(textname);
								}

							}
						});
					}
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

		_TerminalName = null;
		_bacImageView = null;
		if (_bitmap != null) {
			_bitmap.recycle();
			}
		_bitmap = null;
		_rootLayout = null;
//		System.gc();

	}

	static public TerminalName Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("terminal_name_object"))
			return null;

		TerminalName retValue = new TerminalName();
		retValue.LoadBaseInfo(xpp);

		retValue._FontColor = Integer.parseInt(xpp.getAttributeValue(null,
				"FontColor"));
		retValue._Font = xpp.getAttributeValue(null, "Font");
		retValue._Fontsize = Integer.parseInt(xpp.getAttributeValue(null,
				"FontSize"));
		retValue._fileName = xpp.getAttributeValue(null, "BackgroundImageFile");
		if (retValue._fileName != null)
			retValue._fileName = retValue._fileName.replace('\\', '/');
		retValue._position = Integer.parseInt(xpp.getAttributeValue(null,
				"Align"));

		return retValue;
	}

	@Override
	public void stopWork() {
		// TODO Auto-generated method stub
		_work = false;
		_thread.interrupt();
	}

}
