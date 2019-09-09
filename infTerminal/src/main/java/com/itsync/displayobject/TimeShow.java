package com.itsync.displayobject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@SuppressWarnings("deprecation")
public class TimeShow extends DisplayObject {

	private final int msgKey1 = 1;// %Y-%m-%d %H:%M:%S
	private final int msgKey2 = 2;// %A
	private final int msgKey3 = 3;
	private final int msgKey4 = 4;
	private final int msgKey5 = 5;
	@SuppressWarnings("unused")
	private final int msgKey6 = 6;
	private final int msgKey7 = 7;
	@SuppressWarnings("unused")
	private final int msgKey8 = 8;
	private final int msgKey9 = 9;
	@SuppressWarnings("unused")
	private final int msgKey10 = 10;
	private final int msgKey11 = 11;
	@SuppressWarnings("unused")
	private final int msgKey12 = 12;
	private final int msgKey13 = 13;
	@SuppressWarnings("unused")
	private final int msgKey14 = 14;
	private final int msgKey15 = 15;
	@SuppressWarnings("unused")
	private final int msgKey16 = 16;
	private final int msgKey17 = 17;
	@SuppressWarnings("unused")
	private final int msgKey18 = 18;
	private final int msgKey19 = 19;
	@SuppressWarnings("unused")
	private final int msgKey20 = 20;
	private final int msgKey21 = 21;
	@SuppressWarnings("unused")
	private final int msgKey22 = 22;
	private final int msgKey23 = 23;
	@SuppressWarnings("unused")
	private final int msgKey24 = 24;
	private final int msgKey25 = 25;
	@SuppressWarnings("unused")
	private final int msgKey26 = 26;
	private final int msgKey27 = 27;
	@SuppressWarnings("unused")
	private final int msgKey28 = 28;
	private int _Fontsize;
	public int _viewWidth;
	public int _viewHeight;
	public String _Font;
	private TextConfig sTime;
	private Calendar c;
	private SimpleDateFormat cFormat;
	private SimpleDateFormat cFormat2;
	private String foString;
	private String foString2;
	private int _FontColor;
	private ImageView _bacImageView;
	private String _filename;
	private String _file;
	private ViewGroup _rootLayout;
	private Bitmap _bitmap;
	private Thread _thread;
	private boolean _work;
	private String _format;
	private Message _ms;
	private String[] _weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    private int _Week;
    private int _position;
	public TimeShow() {

		_work = false;
		_viewWidth = 1920;
		_viewHeight = 1080;
//		_format = "%Y-%m-%d %H:%M:%S";
	}

	@SuppressLint({ "ResourceAsColor", "SimpleDateFormat", "HandlerLeak" })
	@Override
	public void initialize(Context ctrlContext, Point screenSize,
			ViewGroup rootLayout) {
		_rootLayout = rootLayout;
		sTime = new TextConfig(ctrlContext);
//		sTime.setSingleLine(true);
		_bacImageView = new ImageView(ctrlContext);
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		_work = true;
		_bacImageView.setScaleType(ScaleType.FIT_XY);
		_FontColor = _FontColor | 0xff000000;
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				_viewWidth, _viewHeight, (int) (screenSize.x * _left),
				(int) (screenSize.y * _top));
		sTime.setLayoutParams(params);
		sTime.setBackgroundColor(Color.argb(0, 0, 0, 0));
//		sTime.setGravity(Gravity.CENTER);
		sTime.init(_Fontsize, screenSize.x, screenSize.y, _Font);
		sTime.setTextColor(_FontColor);
		sTime.setSingleLine(true);
		if (_position == 0) {
			sTime.setGravity(Gravity.START);
		}else if (_position == 1) {
			sTime.setGravity(Gravity.CENTER | Gravity.TOP);
		}else if (_position == 2) {
			sTime.setGravity(Gravity.END | Gravity.TOP);
		}else if (_position == 16) {
			sTime.setGravity(Gravity.START | Gravity.CENTER);
		}else if (_position == 17) {
			sTime.setGravity(Gravity.CENTER);
		}else if (_position == 18) {
			sTime.setGravity(Gravity.END | Gravity.CENTER);
		}else if (_position == 32) {
			sTime.setGravity(Gravity.BOTTOM | Gravity.START);
		}else if (_position == 33) {
			sTime.setGravity(Gravity.BOTTOM | Gravity.CENTER);
		}else if (_position == 34) {
			sTime.setGravity(Gravity.END | Gravity.BOTTOM);
		}
		// sTime.setGravity(Gravity.CENTER);
		String url = StaticImage._imageFolder;
		// if (!(_filename.trim().isEmpty())) {
		_file = url + _filename;

		if (!_filename.equalsIgnoreCase("")) {
			_bitmap = BitmapFactory.decodeFile(_file);
			// }
		} else {
			_bitmap = null;
		}
		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case msgKey1:
					c = Calendar.getInstance();
					cFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					foString = cFormat.format(c.getTime());
					if (!(sTime == null || foString == null)) {

						sTime.setText(foString);
					}

					break;
				case msgKey2:
					c = Calendar.getInstance();
					String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
					_Week = c.get(Calendar.DAY_OF_WEEK) - 1;
					if (!(sTime == null || String.valueOf(_Week)== null )) {

						sTime.setText(weekDays[_Week]);
					}
					break;
				case msgKey3:
					c = Calendar.getInstance();
					cFormat = new SimpleDateFormat("yyyy-MM-dd");
					foString = cFormat.format(c.getTime());
					if (!(sTime == null || foString == null)) {

						sTime.setText(foString);
					}
					break;
				case msgKey4:
					c = Calendar.getInstance();
					cFormat = new SimpleDateFormat("yyyy-M-d");
					foString = cFormat.format(c.getTime());
					if (!(sTime == null || foString == null)) {

						sTime.setText(foString);
					}
					break;
				case msgKey5:
					c = Calendar.getInstance();
					cFormat = new SimpleDateFormat("HH:mm:ss");
					foString = cFormat.format(c.getTime());
					if (!(sTime == null || foString == null)) {

						sTime.setText(foString);
					}
					break;
				case msgKey7:
					c = Calendar.getInstance();
					_Week = c.get(Calendar.DAY_OF_WEEK)- 1;
					cFormat = new SimpleDateFormat("yyyy-MM-dd");
					cFormat2 = new SimpleDateFormat("HH:mm:ss");
					foString = cFormat.format(c.getTime());
					foString2= cFormat2.format(c.getTime());
					if (!(sTime == null || foString == null || foString2 == null || String.valueOf(_Week) == null)) {

						sTime.setText(foString+" "+_weekDays[_Week]+" "+foString2);
					}
					break;
				case msgKey9:
					c = Calendar.getInstance();
					cFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
					foString = cFormat.format(c.getTime());
					if (!(sTime == null || foString == null)) {

						sTime.setText(foString);
					}
					break;
				case msgKey11:
					c = Calendar.getInstance();
					_Week = c.get(Calendar.DAY_OF_WEEK)- 1;
					cFormat = new SimpleDateFormat("yy-MM-dd");
					cFormat2 = new SimpleDateFormat("HH:mm:ss");
					foString = cFormat.format(c.getTime());
					foString2= cFormat2.format(c.getTime());
					if (!(sTime == null || foString == null || foString2 == null || String.valueOf(_Week) == null)) {

						sTime.setText(foString+" "+_weekDays[_Week]+" "+foString2);
					}
					break;
				case msgKey13:
					c = Calendar.getInstance();
					cFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
					foString = cFormat.format(c.getTime());
					if (!(sTime == null || foString == null)) {

						sTime.setText(foString);
					}
					break;
				case msgKey15:
					c = Calendar.getInstance();
					_Week = c.get(Calendar.DAY_OF_WEEK)- 1;
					cFormat = new SimpleDateFormat("MM-dd");
					cFormat2 = new SimpleDateFormat("HH:mm:ss");
					foString = cFormat.format(c.getTime());
					foString2= cFormat2.format(c.getTime());
					if (!(sTime == null || foString == null || foString2 == null || String.valueOf(_Week) == null)) {

						sTime.setText(foString+" "+_weekDays[_Week]+" "+foString2);
					}
					break;
				case msgKey17:
					c = Calendar.getInstance();
					cFormat = new SimpleDateFormat("yyyy年MM月dd日");
					cFormat2 = new SimpleDateFormat("HH时mm分ss秒");
					foString = cFormat.format(c.getTime());
					foString2= cFormat2.format(c.getTime());
					if (!(sTime == null || foString == null || foString2 == null)) {

						sTime.setText(foString+" "+foString2);
					}
					break;
				case msgKey19:
					c = Calendar.getInstance();
					_Week = c.get(Calendar.DAY_OF_WEEK)- 1;
					cFormat = new SimpleDateFormat("yyyy年MM月dd日");
					cFormat2 = new SimpleDateFormat("HH时mm分ss秒");
					foString = cFormat.format(c.getTime());
					foString2= cFormat2.format(c.getTime());
					if (!(sTime == null || foString == null || foString2 == null || String.valueOf(_Week) == null)) {

						sTime.setText(foString+" "+_weekDays[_Week]+" "+foString2);
					}
					break;
				case msgKey21:
					c = Calendar.getInstance();
					cFormat = new SimpleDateFormat("yy年MM月dd日");
					cFormat2 = new SimpleDateFormat("HH时mm分ss秒");
					foString = cFormat.format(c.getTime());
					foString2= cFormat2.format(c.getTime());
					if (!(sTime == null || foString == null || foString2 == null)) {

						sTime.setText(foString+" "+foString2);
					}
					break;
				case msgKey23:
					c = Calendar.getInstance();
					_Week = c.get(Calendar.DAY_OF_WEEK)- 1;
					cFormat = new SimpleDateFormat("yy年MM月dd日");
					cFormat2 = new SimpleDateFormat("HH时mm分ss秒");
					foString = cFormat.format(c.getTime());
					foString2= cFormat2.format(c.getTime());
					if (!(sTime == null || foString == null || foString2 == null || String.valueOf(_Week) == null)) {

						sTime.setText(foString+" "+_weekDays[_Week]+" "+foString2);
					}
					break;
				case msgKey25:
					c = Calendar.getInstance();
					_Week = c.get(Calendar.DAY_OF_WEEK)- 1;
					cFormat = new SimpleDateFormat("MM月dd日");
					cFormat2 = new SimpleDateFormat("HH时mm分ss秒");
					foString = cFormat.format(c.getTime());
					foString2= cFormat2.format(c.getTime());
					if (!(sTime == null || foString == null || foString2 == null || String.valueOf(_Week) == null)) {

						sTime.setText(foString+" "+_weekDays[_Week]+" "+foString2);
					}
					break;
				case msgKey27:
					c = Calendar.getInstance();
					cFormat = new SimpleDateFormat("MM月dd日");
					cFormat2 = new SimpleDateFormat("HH时mm分ss秒");
					foString = cFormat.format(c.getTime());
					foString2= cFormat2.format(c.getTime());
					if (!(sTime == null || foString == null || foString2 == null)) {

						sTime.setText(foString+" "+foString2);
					}
					break;
				default:
					break;
				}
			}
		};

		_thread = new Thread(new Runnable() {

			@Override
			public void run() {

				_rootLayout.post(new Runnable() {

					@Override
					public void run() {
						
						if (!(_bitmap == null)) {
							_bacImageView.setImageBitmap(_bitmap);
						}
					}
				});

				do {

					try {
						Thread.sleep(1000);
						_ms = new Message();
						if (_format.equals("%Y-%m-%d %H:%M:%S")) {
							_ms.what = msgKey1;
						} else if (_format.equals("%A")) {
							_ms.what = msgKey2;
						} else if (_format.equals("%Y-%m-%d")) {
							_ms.what = msgKey3;
						} else if (_format.equals("%Y-%#m-%#d")) {
							_ms.what = msgKey4;
						} else if (_format.equals("%H:%M:%S")) {
							_ms.what = msgKey5;
						} else if (_format.equals("%Y-%#m-%#d %H:%M:%S")) {
							_ms.what = msgKey1;
						} else if (_format.equals("%Y-%m-%d %A %H:%M:%S")) {
							_ms.what = msgKey7;
						} else if (_format.equals("%Y-%#m-%#d %A %H:%M:%S")) {
							_ms.what = msgKey7;
						} else if (_format.equals("%y-%m-%d %H:%M:%S")) {
							_ms.what = msgKey9;
						} else if (_format.equals("%y-%#m-%#d %H:%M:%S")) {
							_ms.what = msgKey9;
						} else if (_format.equals("%y-%m-%d %A %H:%M:%S")) {
							_ms.what = msgKey11;
						} else if (_format.equals("%y-%#m-%#d %A %H:%M:%S")) {
							_ms.what = msgKey11;
						} else if (_format.equals("%m-%d %H:%M:%S")) {
							_ms.what = msgKey13;
						} else if (_format.equals("%#m-%#d %H:%M:%S")) {
							_ms.what = msgKey13;
						} else if (_format.equals("%m-%d %A %H:%M:%S")) {
							_ms.what = msgKey15;
						} else if (_format.equals("%#m-%#d %A %H:%M:%S")) {
							_ms.what = msgKey15;
						} else if (_format.equals("%Y年%m月%d日 %H时%M分%S秒")) {
							_ms.what = msgKey17;
						} else if (_format.equals("%Y年%#m月%#d日 %H时%M分%S秒")) {
							_ms.what = msgKey17;
						} else if (_format.equals("%Y年%m月%d日 %A %H时%M分%S秒")) {
							_ms.what = msgKey19;
						} else if (_format.equals("%Y年%#m月%#d日 %A %H时%M分%S秒")) {
							_ms.what = msgKey19;
						} else if (_format.equals("%y年%m月%d日 %H时%M分%S秒")) {
							_ms.what = msgKey21;
						} else if (_format.equals("%y年%#m月%#d日 %H时%M分%S秒")) {
							_ms.what = msgKey21;
						} else if (_format.equals("%y年%m月%d日 %A %H时%M分%S秒")) {
							_ms.what = msgKey23;
						} else if (_format.equals("%y年%#m月%#d日 %A %H时%M分%S秒")) {
							_ms.what = msgKey23;
						} else if (_format.equals("%m月%d日 %A %H时%M分%S秒")) {
							_ms.what = msgKey25;
						} else if (_format.equals("%#m月%#d日 %A %H时%M分%S秒")) {
							_ms.what = msgKey25;
						} else if (_format.equals("%m月%d日 %H时%M分%S秒")) {
							_ms.what = msgKey27;
						} else if (_format.equals("%#m月%#d日 %H时%M分%S秒")) {
							_ms.what = msgKey27;
						}else {
							_ms.what = msgKey1;
						}
						mHandler.sendMessage(_ms);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				} while (_work);

			}
		});
		_thread.start();
		AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
				(int) (screenSize.x * _width), (int) (screenSize.y * _height),
				(int) (screenSize.x * _left), (int) (screenSize.y * _top));
		_bacImageView.setLayoutParams(params1);
		
		rootLayout.addView(_bacImageView);
		rootLayout.addView(sTime);
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
		sTime = null;
		_bacImageView = null;
		_bitmap = null;
		_rootLayout = null;
//		System.gc();

	}

	static public TimeShow Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("date_time_info"))
			return null;

		TimeShow retValue = new TimeShow();
		retValue.LoadBaseInfo(xpp);
		retValue._Font = xpp.getAttributeValue(null, "Font");
		retValue._Fontsize = Integer.parseInt(xpp.getAttributeValue(null,
				"FontSize"));
		retValue._FontColor = Integer.parseInt(xpp.getAttributeValue(null,
				"FontColor"));
		retValue._filename = xpp.getAttributeValue(null, "BackgroundImageFile");
		retValue._position =Integer.parseInt(xpp.getAttributeValue(null, "Align"));
		if (retValue._filename != null)
			retValue._filename = retValue._filename.replace('\\', '/');
        retValue._format = xpp.getAttributeValue(null, "Format");
		return retValue;
	}

	@Override
	public void stopWork() {
		_work = false;
		_thread.interrupt();
	}

}
