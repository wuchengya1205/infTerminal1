package com.itsync.displayobject;

import java.io.File;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.itsync.infbase.WeatherInfo;
import com.itsync.infterminal.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class Weather extends DisplayObject {
	
	private int _areaPosition;
	@SuppressWarnings("unused")
	private int _iconPosition;
	private int _weatPosition;

	private boolean _work;
	private Thread _workThread;
	private double _AreaLeft;
	private double _AreaTop;
	private double _AreaHeight;
	private double _AreaWidth;
	private int showType;
	private double _IconLeft;
	private double _IconTop;
	private double _IconWidth;
	private double _IconHeight;
	private double _WeatherLeft;
	private double _WeatherTop;
	private double _WeatherWidth;
	private double _WeatherHeight;

	private ViewGroup _rootLayout;
	private int _viewWidth;
	private int _viewHeight;
	private int _areaWidth;
	private int _areaHeight;
	private int _iconWidth;
	private int _iconHeight;
	private int _weaWidth;
	private int _weaHeight;

	private TextView tx;
	// private WeatherText areaText;
	private TextConfig areaText;
	private ImageView weatherImage;
	// private WeatherText temText;
	private TextConfig temText;

	private int _weaColor;
	private int _areaColor;
	private int areasize;
	private int weasize;
	private String _temFont;
	private String _areaFont;
	private ImageView im1;// 分开显示天气图标1
	private ImageView im2;// 分开显示天气图标2
	private int _im1width;
	private int _im1height;
	private int _im2width;
	private int _im2height;
	private Point temPoint;
	private ImageView _bacImageView;
	private String _filename;
	private String _file;
	private Bitmap _bitmap;
	private Context cx;
//	private WeatherBasedonTextView

	public Weather() {

		temPoint = null;
		cx = null;
		tx = null;
		_rootLayout = null;
		_viewWidth = 1920;
		_viewHeight = 1080;

	}

	@Override
	public void initialize(Context ctrlContext, Point screenSize,
			ViewGroup rootLayout) {
		ImageLoader.getInstance();
		temPoint = screenSize;
		cx = ctrlContext;
		_rootLayout = rootLayout;
		_bacImageView = new ImageView(ctrlContext);
		_bacImageView.setScaleType(ScaleType.FIT_XY);
		tx = new TextView(ctrlContext);
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
				(int) (screenSize.x * _width), (int) (screenSize.y * _height),
				(int) (screenSize.x * _left), (int) (screenSize.y * _top));
		_bacImageView.setLayoutParams(params1);
		rootLayout.addView(_bacImageView);
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				_viewWidth, _viewHeight, (int) (screenSize.x * _left),
				(int) (screenSize.y * _top));

		tx.setLayoutParams(params);
		tx.setGravity(Gravity.CENTER);
		rootLayout.addView(tx);
		// areaText = new WeatherText(ctrlContext);// 显示地区控件
		areaText = new TextConfig(ctrlContext); 
		_areaWidth = (int) (_viewWidth * _AreaWidth);
		_areaHeight = (int) (_viewHeight * _AreaHeight);
		AbsoluteLayout.LayoutParams params2 = new AbsoluteLayout.LayoutParams(
				_areaWidth, _areaHeight, (int) (_viewWidth * _AreaLeft)
						+ (int) (screenSize.x * _left),
				(int) (_viewHeight * _AreaTop) + (int) (screenSize.y * _top));
//		areaText.setGravity(Gravity.CENTER);
		areaText.setIncludeFontPadding(false);
		areaText.init(areasize, screenSize.x, screenSize.y, _areaFont);
		areaText.setTextColor(_areaColor | 0xff000000);
		areaText.setSingleLine(true); 
		areaText.setLayoutParams(params2);
		if (_areaPosition == 0) {
			areaText.setGravity(Gravity.START);
		}else if (_areaPosition == 1) {
			areaText.setGravity(Gravity.CENTER | Gravity.TOP);
		}else if (_areaPosition == 2) {
			areaText.setGravity(Gravity.END | Gravity.TOP);
		}else if (_areaPosition == 16) {
			areaText.setGravity(Gravity.START | Gravity.CENTER);
		}else if (_areaPosition == 17) {
			areaText.setGravity(Gravity.CENTER);
		}else if (_areaPosition == 18) {
			areaText.setGravity(Gravity.END | Gravity.CENTER);
		}else if (_areaPosition == 32) {
			areaText.setGravity(Gravity.BOTTOM | Gravity.START);
		}else if (_areaPosition == 33) {
			areaText.setGravity(Gravity.BOTTOM | Gravity.CENTER);
		}else if (_areaPosition == 34) {
			areaText.setGravity(Gravity.END | Gravity.BOTTOM);
		}
		rootLayout.addView(areaText);
		weatherImage = new ImageView(ctrlContext);// 显示图标控件

		_iconWidth = (int) (_viewWidth * _IconWidth);
		_iconHeight = (int) (_viewHeight * _IconHeight);
		AbsoluteLayout.LayoutParams params4 = new AbsoluteLayout.LayoutParams(
				_iconWidth, _iconHeight, (int) (_viewWidth * _IconLeft)
						+ (int) (screenSize.x * _left),
				(int) (_viewHeight * _IconTop) + (int) (screenSize.y * _top));
		weatherImage.setLayoutParams(params4);
		rootLayout.addView(weatherImage);
		// temText = new WeatherText(ctrlContext);// 显示温度控件
		temText = new TextConfig(ctrlContext);
		_weaWidth = (int) (_viewWidth * _WeatherWidth);
		_weaHeight = (int) (_viewHeight * _WeatherHeight);
		AbsoluteLayout.LayoutParams params3 = new AbsoluteLayout.LayoutParams(
				_weaWidth, _weaHeight, (int) (_viewWidth * _WeatherLeft)
						+ (int) (screenSize.x * _left),
				(int) (_viewHeight * _WeatherTop) + (int) (screenSize.y * _top));
		temText.setLayoutParams(params3);
		temText.init(weasize, screenSize.x, screenSize.y, _temFont);
		temText.setSingleLine(true);
//		temText.setGravity(Gravity.CENTER);
		if (_weatPosition == 0) {
			temText.setGravity(Gravity.START);
		}else if (_weatPosition == 1) {
			temText.setGravity(Gravity.CENTER | Gravity.TOP);
		}else if (_weatPosition == 2) {
			temText.setGravity(Gravity.END | Gravity.TOP);
		}else if (_weatPosition == 16) {
			temText.setGravity(Gravity.START | Gravity.CENTER);
		}else if (_weatPosition == 17) {
			temText.setGravity(Gravity.CENTER);
		}else if (_weatPosition == 18) {
			temText.setGravity(Gravity.END | Gravity.CENTER);
		}else if (_weatPosition == 32) {
			temText.setGravity(Gravity.BOTTOM | Gravity.START);
		}else if (_weatPosition == 33) {
			temText.setGravity(Gravity.BOTTOM | Gravity.CENTER);
		}else if (_weatPosition == 34) {
			temText.setGravity(Gravity.END | Gravity.BOTTOM);
		}
		temText.setTextColor(_weaColor | 0xff000000);
		im1 = new ImageView(cx);// 天气1显示空间
		_im1width = _iconWidth / 2;
		_im1height = _iconHeight;
		AbsoluteLayout.LayoutParams im1parParams = new AbsoluteLayout.LayoutParams(
				_im1width, _im1height, (int) (_viewWidth * _IconLeft)
						+ (int) (temPoint.x * _left),
				(int) (_viewHeight * _IconTop) + (int) (temPoint.y * _top));
		im1.setLayoutParams(im1parParams);
		_rootLayout.addView(im1);
		im2 = new ImageView(cx);// 天气2显示控件
		_im2width = _iconWidth / 2;
		_im2height = _iconHeight;
		AbsoluteLayout.LayoutParams im2parParams = new AbsoluteLayout.LayoutParams(
				_im2width, _im2height, (int) (_viewWidth * _IconLeft)
						+ (int) (temPoint.x * _left) + _im2width,
				(int) (_viewHeight * _IconTop) + (int) (temPoint.y * _top));
		im2.setLayoutParams(im2parParams);
		_rootLayout.addView(im2);
		rootLayout.addView(temText);
		
//		String url = StaticImage._imageFolder;
		
//		if (!(_filename.trim().isEmpty())) {
//				_file = url + _filename;
//			
//			File file = new File(_file);
//			if (file.exists()) {
//				_bitmap = BitmapFactory.decodeFile(_file);
//			}
//		}else {
//			_bitmap = null;
//		}
		
		
		
		
		
		_work = true;
		_workThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				

				String url = StaticImage._imageFolder;
//				if (!(_filename.trim().isEmpty())) {
//				if (_filename != null || !_filename.isEmpty()) {
					
				
					_file = url + _filename;
					File file = new File(_file);
					if (file.exists()) {
						_bitmap = BitmapFactory.decodeFile(_file);
//					}
				}else {
					_bitmap = null;
				}
				
				final WeatherInfo weatherInfo = new WeatherInfo();
				while (_work) {
					final boolean getNextWeatherOK = weatherInfo
							.getNextWeather(showType);
                    final ImageView bacImageView = _bacImageView;
                    final TextConfig areatext = areaText;
                    final TextConfig temtext = temText;
                    final ImageView imageView1 = im1;
                    final ImageView imageView2 = im2;
                    final ImageView weatherImageView = weatherImage;
					_rootLayout.post(new Runnable() {

						@Override
						public void run() {
							if (!(_bitmap == null || bacImageView == null)) {
								bacImageView.setImageBitmap(_bitmap);
							}
							if (getNextWeatherOK) {
								if (!(areatext == null || weatherInfo.AreaName == null)) {
									
									areatext.setText(new StringBuilder().append(String
										.format("%s\n", weatherInfo.AreaName)));
								}
								if (!(temtext == null || weatherInfo.Temperature == null)) {
									
									temtext.setText(new StringBuilder()
											.append(String.format("%s\n",
													weatherInfo.Temperature)));
								}
								

								int weather1 = weatherInfo.Weather1 - 1;
								int weather2 = weatherInfo.Weather2 - 1;
								if (weather1 == weather2 || weather2 < 0) {
									if (imageView1 != null) {
										imageView1.setVisibility(View.INVISIBLE);
									}if (imageView2 != null) {
										imageView2.setVisibility(View.INVISIBLE);
									}if (weatherImageView != null) {
										
										weatherImageView.setVisibility(View.VISIBLE);
//										weatherImage.setScaleType(ScaleType.CENTER_CROP);
										weatherImageView.setImageResource(getWeatherImageID(weather1));
										weatherImageView.setScaleType(ScaleType.FIT_CENTER);
									}
								} else {
									weatherImageView.setVisibility(View.INVISIBLE);
									if (imageView1 != null) {
										
										imageView1.setVisibility(View.VISIBLE);
//										imageView1.setScaleType(ScaleType.FIT_START);
										imageView1.setImageResource(getWeatherImageID(weather1));
									}
									if (imageView2 != null) {
										imageView2.setVisibility(View.VISIBLE);
//										imageView2.setScaleType(ScaleType.FIT_END);
										imageView2.setImageResource(getWeatherImageID(weather2));
									}
								}
							}

						}
					});
					try {
						Thread.sleep(3000);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			
				
			}
		});
		_workThread.start();

	}

	static int c_weatherImageID[] = { R.drawable.a, R.drawable.b, R.drawable.c,
			R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g,
			R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k,
			R.drawable.l, R.drawable.m, R.drawable.n, R.drawable.o,
			R.drawable.p, R.drawable.q, R.drawable.r, R.drawable.s,
			R.drawable.t, R.drawable.u, R.drawable.v, R.drawable.w,
			R.drawable.x, R.drawable.y, R.drawable.z, R.drawable.aa,
			R.drawable.bb, R.drawable.cc, R.drawable.dd, R.drawable.ee,
			R.drawable.ff,

	};

	static int getWeatherImageID(int weather) {
		if (weather == 53)
			return R.drawable.gg;
		else if (weather >= 0 && weather < c_weatherImageID.length)
			return c_weatherImageID[weather];
		else
			return R.drawable.a;
	}

//	Runnable _workTask = new Runnable() {
//
//		@Override
//		public void run() {
//			String url = StaticImage._imageFolder;
//			if (!(_filename.trim().isEmpty())) {
//				_file = url + _filename;
//				File file = new File(_file);
//				if (file.exists()) {
//					_bitmap = BitmapFactory.decodeFile(_file);
//				}
//			}else {
//				_bitmap = null;
//			}
//			
//			final WeatherInfo weatherInfo = new WeatherInfo();
//			while (_work) {
//				final boolean getNextWeatherOK = weatherInfo
//						.getNextWeather(showType);
//
//				_rootLayout.post(new Runnable() {
//
//					@Override
//					public void run() {
//						if (!(_bitmap == null)) {
//							_bacImageView.setImageBitmap(_bitmap);
//						}
//						if (getNextWeatherOK) {
//							areaText.setText(new StringBuilder().append(String
//									.format("%s\n", weatherInfo.AreaName)));
//							temText.setText(new StringBuilder().append(String
//									.format("%s\n", weatherInfo.Temperature)));
//
//							int weather1 = weatherInfo.Weather1 - 1;
//							int weather2 = weatherInfo.Weather2 - 1;
//							if (weather1 == weather2 || weather2 < 0) {
//								im1.setVisibility(View.INVISIBLE);
//								im2.setVisibility(View.INVISIBLE);
//								weatherImage.setVisibility(View.VISIBLE);
//								weatherImage
//										.setScaleType(ScaleType.CENTER_CROP);
//								weatherImage
//										.setImageResource(getWeatherImageID(weather1));
//								weatherImage.setScaleType(ScaleType.FIT_CENTER);
//							} else {
//								weatherImage.setVisibility(View.INVISIBLE);
//								im1.setVisibility(View.VISIBLE);
//								im2.setVisibility(View.VISIBLE);
//								im1.setScaleType(ScaleType.FIT_START);
//								im1.setImageResource(getWeatherImageID(weather1));
//								im2.setImageResource(getWeatherImageID(weather2));
//							}
//						}
//
//					}
//				});
//				try {
//					Thread.sleep(3000);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//
//		}
//	};

	@Override
	public void uninitialize() {

		_work = false;
		if (null != _workThread) {
			try {
				_workThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			_workThread = null;
		}
        weatherImage = null;
		_rootLayout = null;
		areaText = null;
		temText = null; 
		im1 = null;
		im2 = null;
		_bacImageView = null;
		_bitmap = null;
//		System.gc();

	}

	static public Weather Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("weather_object"))
			return null;

		Weather retValue = new Weather();
		retValue.LoadBaseInfo(xpp);

		retValue._AreaLeft = Double.parseDouble(xpp.getAttributeValue(null,
				"AreaLeft"));
		retValue._AreaTop = Double.parseDouble(xpp.getAttributeValue(null,
				"AreaTop"));
		retValue._AreaHeight = Double.parseDouble(xpp.getAttributeValue(null,
				"AreaHeight"));
		retValue._AreaWidth = Double.parseDouble(xpp.getAttributeValue(null,
				"AreaWidth"));

		retValue._IconLeft = Double.parseDouble(xpp.getAttributeValue(null,
				"IconLeft"));
		retValue._IconTop = Double.parseDouble(xpp.getAttributeValue(null,
				"IconTop"));
		retValue._IconWidth = Double.parseDouble(xpp.getAttributeValue(null,
				"IconWidth"));
		retValue._IconHeight = Double.parseDouble(xpp.getAttributeValue(null,
				"IconHeight"));

		retValue._WeatherLeft = Double.parseDouble(xpp.getAttributeValue(null,
				"WeatherLeft"));
		retValue._WeatherTop = Double.parseDouble(xpp.getAttributeValue(null,
				"WeatherTop"));
		retValue._WeatherHeight = Double.parseDouble(xpp.getAttributeValue(
				null, "WeatherHeight"));
		retValue._WeatherWidth = Double.parseDouble(xpp.getAttributeValue(null,
				"WeatherWidth"));

		retValue.areasize = Integer.parseInt(xpp.getAttributeValue(null,
				"AreaFontSize"));
		retValue.weasize = Integer.parseInt(xpp.getAttributeValue(null,
				"WeatherFontSize"));
		retValue.showType = Integer.parseInt(xpp.getAttributeValue(null,
				"ShowType"));
		retValue._temFont = xpp.getAttributeValue(null, "WeatherFont");
		retValue._areaFont = xpp.getAttributeValue(null, "AreaFont");
		retValue._weaColor = Integer.parseInt(xpp.getAttributeValue(null,
				"WeatherFontColor"));
		retValue._areaPosition = Integer.parseInt(xpp.getAttributeValue(null, "AreaAlign"));
		retValue._iconPosition = Integer.parseInt(xpp.getAttributeValue(null, "IconAlign"));
		retValue._weatPosition = Integer.parseInt(xpp.getAttributeValue(null, "WeatherAlign"));
		retValue._areaColor = Integer.parseInt(xpp.getAttributeValue(null,
				"AreaFontColor"));
		retValue._filename = xpp.getAttributeValue(null, "BackgroundImageFile");
		if (retValue._filename != null) 
			retValue._filename = retValue._filename.replace('\\', '/');
		return retValue;
	}

	@Override
	public void stopWork() {
		_work = false;
		_workThread.interrupt();
	}

}
