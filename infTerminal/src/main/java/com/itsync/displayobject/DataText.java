package com.itsync.displayobject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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
public class DataText extends DisplayObject {

	public int _viewWidth;
	public int _viewHeight;
	private String _Font;
	private int _FontColor;
	private int _Fontsize;
	private int _position;
	private int _ActiveSeconds;
	private int _BlinkInterval;
	private int _ActiveTextColor;
	private String _defaultText;
	// private TextConfig _dataText;
	private ViewGroup _rootLayout;
	public String _data = "";
	public static String _objectName;
	private String _filename;
	private String _file;
	private Thread _thread;
	private Thread _colorThread;
	private boolean _work;
	private DataTextBasedonTextView _dataText;
	private int _count = 0;
	public boolean _startColorChange;
	private long switchTime;
	private List<String> _textData;
	private boolean _Tag;
	private ImageView _BacImage;
	private Bitmap _bitmap;

	public DataText() {
		_viewWidth = 1920;
		_viewHeight = 1080;
		_work = false;
		_startColorChange = false;
		_Tag = false;
		_BlinkInterval = 500;

	}

	@Override
	public void initialize(final Context ctrlContext, final Point screenSize, ViewGroup rootLayout) {
		_dataText = new DataTextBasedonTextView(ctrlContext);
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		_rootLayout = rootLayout;
		_BacImage = new ImageView(ctrlContext);
		_BacImage.setScaleType(ScaleType.FIT_XY);
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams((int) (screenSize.x * _width),
				(int) (screenSize.y * _height), (int) (screenSize.x * _left), (int) (screenSize.y * _top));
		_BacImage.setLayoutParams(params);
		rootLayout.addView(_BacImage);
		// _BacImage
		// if (!(_ActiveImage == null || _ActiveImage.isEmpty())) {
		//
		// }
		if (_position == 0) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(_viewWidth, _viewHeight,
					(int) (screenSize.x * _left), (int) (screenSize.y * _top));
			_dataText.setLayoutParams(params1);
			_dataText.setGravity(Gravity.START);
			// _dataText.setSingleLine(true);
		} else if (_position == 1) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(_viewWidth, _viewHeight,
					(int) (screenSize.x * _left), (int) (screenSize.y * _top));
			_dataText.setLayoutParams(params1);
			// _dataText.setGravity(Gravity.TOP);
			_dataText.setGravity(Gravity.CENTER | Gravity.TOP);
			// _dataText.setSingleLine(true);
		} else if (_position == 2) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(_viewWidth, _viewHeight,
					(int) (screenSize.x * _left), (int) (screenSize.y * _top));
			_dataText.setLayoutParams(params1);
			_dataText.setGravity(Gravity.END | Gravity.TOP);
			// _dataText.setSingleLine(true);
		} else if (_position == 16) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(_viewWidth, _viewHeight,
					(int) (screenSize.x * _left), (int) (screenSize.y * _top));
			_dataText.setLayoutParams(params1);
			_dataText.setGravity(Gravity.START | Gravity.CENTER);
			// _dataText.setSingleLine(true);
		} else if (_position == 17) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(_viewWidth, _viewHeight,
					(int) (screenSize.x * _left), (int) (screenSize.y * _top));
			_dataText.setLayoutParams(params1);
			_dataText.setGravity(Gravity.CENTER);
			// _dataText.setSingleLine(true);
		} else if (_position == 18) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(_viewWidth, _viewHeight,
					(int) (screenSize.x * _left), (int) (screenSize.y * _top));
			_dataText.setLayoutParams(params1);
			_dataText.setGravity(Gravity.END | Gravity.CENTER);
			// _dataText.setSingleLine(true);
		} else if (_position == 32) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(_viewWidth, _viewHeight,
					(int) (screenSize.x * _left), (int) (screenSize.y * _top));
			_dataText.setLayoutParams(params1);
			_dataText.setGravity(Gravity.BOTTOM | Gravity.START);
			// _dataText.setSingleLine(true);
		} else if (_position == 33) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(_viewWidth, _viewHeight,
					(int) (screenSize.x * _left), (int) (screenSize.y * _top));
			_dataText.setLayoutParams(params1);
			_dataText.setGravity(Gravity.BOTTOM | Gravity.CENTER);
			// _dataText.setSingleLine(true);
		} else if (_position == 34) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(_viewWidth, _viewHeight,
					(int) (screenSize.x * _left), (int) (screenSize.y * _top));
			_dataText.setLayoutParams(params1);
			_dataText.setGravity(Gravity.END | Gravity.BOTTOM);
			// _dataText.setSingleLine(true);
		}
		// _dataText.setVisibility(ViewGroup.INVISIBLE);
		_FontColor = _FontColor | 0xff000000;
		_dataText.setBackgroundColor(Color.argb(0, 0, 0, 0));
		// Toast.makeText(ctrlContext, _defaultText, 1).show();

		// while (isAdd) {
		// if (!(_data == null || _data.isEmpty())) {
		// _textData.add(_data);
		// }
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// textData.add(_data);
		// _dataText.init(_viewWidth, _viewHeight,
		// _textData.get(_textData.size()-1), _Fontsize, screenSize.x,
		// screenSize.y, _Font, _FontColor);

		_dataText.setTextColor(_FontColor);

		rootLayout.addView(_dataText);
		// String url = StaticImage._imageFolder;
		// if (!(_filename == null || _filename.isEmpty())) {
		// _file = url + _filename;
		// }
		// File file = new File(_file);
		// if (file.exists()) {
		// _bitmap = BitmapFactory.decodeFile(_file);
		// }
		_work = true;
		_thread = new Thread(new Runnable() {

			@Override
			public void run() {
				String showData = null;
				// final long switchTime = System.currentTimeMillis();
				_textData = new ArrayList<String>();

				while (_work) {

					_textData.clear();
					_textData.add(_defaultText);
					synchronized (_data) {

						if (!(_data == null || _data.isEmpty())) {
							_textData.add(_data);
							// _data = null;
						}
					}
					String shouldShowText = null;
					if (_textData.size() > 0)
						shouldShowText = _textData.get(_textData.size() - 1);
					if (!(shouldShowText == null || shouldShowText.equals(showData))) {
						showData = shouldShowText;
						final String textName = shouldShowText;
						_dataText.init(_viewWidth, _viewHeight, textName, _Fontsize, screenSize.x, screenSize.y, _Font, _FontColor);
						final DataTextBasedonTextView dataText = _dataText;
						if (dataText != null) {
							_rootLayout.post(new Runnable() {

								@Override
								public void run() {

									// Toast.makeText(ctrlContext,
									// String.valueOf(textName)+a,
									// Toast.LENGTH_LONG).show();

									if (textName.trim().isEmpty()) {
										dataText.setText("");
									} else {
										//
										if (!dataText._isUpFly) {

											_Tag = true;
											_startColorChange = true;
										}

										dataText.setText(textName);
									}
									// Toast.makeText(ctrlContext,
									// _textData.get(_textData.size()-1),
									// Toast.LENGTH_LONG).show();

								}
							});
						}
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
		_colorThread = new Thread(new Runnable() {

			@Override
			public void run() {
				String url = StaticImage._imageFolder;
				// if (!(_filename == null || _filename.isEmpty())) {
				_file = url + _filename;
				File file = new File(_file);
				if (file.exists()) {
					_bitmap = BitmapFactory.decodeFile(_file);
//					_bitmap = _imageLoader.loadImageSync(Scheme.FILE.wrap(_file));
					// }
				} else {
					_bitmap = null;
				}
				// _bitmap =
				// BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/itt.jpg");
				final DataTextBasedonTextView dataText = _dataText;
				final ImageView bacImage = _BacImage;
				int Interval = _BlinkInterval;

				while (_work && Interval != 0) {
					_rootLayout.post(new Runnable() {

						@Override
						public void run() {
//							Toast.makeText(ctrlContext, String.valueOf(Interval), Toast.LENGTH_LONG).show();
							if (_startColorChange) {
								if (!(_bitmap == null || bacImage == null)) {
									bacImage.setImageBitmap(_bitmap);
								}

								if (_Tag) {
									switchTime = System.currentTimeMillis();
									_Tag = false;

								}
								// if (_ActiveTag) {

								if (_count == 0) {

									bacImage.setVisibility(View.INVISIBLE);
									dataText.setTextColor(_FontColor);
									_count = 1;
								} else if (_count == 1) {
									bacImage.setVisibility(View.VISIBLE);
									dataText.setTextColor(_ActiveTextColor | 0xff000000);
									_count = 0;

								}
								// }
								if (System.currentTimeMillis() - switchTime > _ActiveSeconds * 1000) {
									_startColorChange = false;
									// _ActiveTag = false;
									// work = true;
									dataText.setTextColor(_FontColor);
									bacImage.setVisibility(View.INVISIBLE);
									// _BlinkInterval = 100;
								}

							}
						}
					});

					try {
						Thread.sleep(Interval);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			}
		});
		_colorThread.start();
	}

	@Override
	public void uninitialize() {

		_work = false;
		if (null != _thread && _colorThread != null) {
			try {
				_thread.join();
				_colorThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			_thread = null;
			_colorThread = null;
		}

		_textData.clear();
		_textData = null;
//		_data = null;
		_BacImage = null;
		_dataText = null;
		if (_bitmap != null) {
			_bitmap.recycle();
		}
		_bitmap = null;
		_rootLayout = null;

//		System.gc();

	}

	@SuppressWarnings("static-access")
	static public DataText Load(String basePath, XmlPullParser xpp) throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG || !xpp.getName().equalsIgnoreCase("data_text_object"))
			return null;

		DataText retValue = new DataText();
		retValue.LoadBaseInfo(xpp);

		retValue._FontColor = Integer.parseInt(xpp.getAttributeValue(null, "FontColor"));
		retValue._Font = xpp.getAttributeValue(null, "Font");
		retValue._Fontsize = Integer.parseInt(xpp.getAttributeValue(null, "FontSize"));
		retValue._position = Integer.parseInt(xpp.getAttributeValue(null, "Align"));
		retValue._ActiveSeconds = Integer.parseInt(xpp.getAttributeValue(null, "NewItemActiveSeconds"));
		retValue._BlinkInterval = Integer.parseInt(xpp.getAttributeValue(null, "NewItemBlinkInterval"));
		retValue._ActiveTextColor = Integer.parseInt(xpp.getAttributeValue(null, "ActiveTextColor"));
		retValue._defaultText = xpp.getAttributeValue(null, "DefaultText");
		retValue._objectName = xpp.getAttributeValue(null, "Name");
		retValue._filename = xpp.getAttributeValue(null, "ActiveItemBackgroudImageFile");
		if (retValue._filename != null)
			retValue._filename = retValue._filename.replace('\\', '/');
		return retValue;
	}

	@Override
	public void stopWork() {
		_work = false;
		_thread.interrupt();
	}

	public void setText(String str) {
		this._data = str;
	}

}
