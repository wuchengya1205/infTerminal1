package com.itsync.displayobject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.itsync.infbase.InfoClient;
import com.itsync.infterminal.MainActivity;
import com.itsync.infterminal.ScreenShotFb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Handwrite extends DisplayObject {

	private int _viewWidth;
	private int _viewHeight;
	private int _sendViewWidth;
	private int _sendViewHeight;
	private int _clearViewWidth;
	private int _clearViewHeight;
	private double _sendLetf;
	private double _sendTop;
	private double _sendWidth;
	private double _sendHeight;
	private double _clearLeft;
	private double _clearTop;
	private double _clearWidth;
	private double _clearHeight;
	private int _penWith;
	private int _penColor;
	private HandWriteView _handWriteView;
	private Button _sendButton;
	private Button _clearButton;
	private String fileName;

	private int _padLeft;
	private int _padTop;

	private InfoClient client;
	private boolean _sendComplete;

	public Handwrite() {

		_handWriteView = null;
		_sendButton = null;
		_clearButton = null;
		_sendComplete = false;

	}

	@Override
	public void initialize(final Context ctrlContext, Point screenSize,
			ViewGroup rootLayout) {
		client = InfoClient.GetInstance();
		Calendar.getInstance();
		_handWriteView = new HandWriteView(ctrlContext);
		_sendButton = new Button(ctrlContext);
		_clearButton = new Button(ctrlContext);

		_handWriteView.init(_penWith, _penColor | 0xff000000);
		_sendButton.setAlpha(0);
		_clearButton.setAlpha(0);

		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		_padLeft = (int) (screenSize.x * _left);
		_padTop = (int) (screenSize.y * _top);
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				_viewWidth, _viewHeight, _padLeft, _padTop);
		_handWriteView.setLayoutParams(params);

		_sendViewWidth = (int) (screenSize.x * _sendWidth);
		_sendViewHeight = (int) (screenSize.y * _sendHeight);
		AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
				_sendViewWidth, _sendViewHeight,
				(int) (screenSize.x * _sendLetf),
				(int) (screenSize.y * _sendTop));
		_sendButton.setLayoutParams(params1);

		_clearViewWidth = (int) (screenSize.x * _clearWidth);
		_clearViewHeight = (int) (screenSize.y * _clearHeight);
		AbsoluteLayout.LayoutParams params2 = new AbsoluteLayout.LayoutParams(
				_clearViewWidth, _clearViewHeight,
				(int) (screenSize.x * _clearLeft),
				(int) (screenSize.y * _clearTop));
		_clearButton.setLayoutParams(params2);

		rootLayout.addView(_handWriteView);
		rootLayout.addView(_sendButton);
		rootLayout.addView(_clearButton);

		_sendComplete = true;
		_sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_sendComplete) {
					_sendComplete = false;

					final Bitmap bitmap = getBitmapFromView(_handWriteView);
					// final Bitmap bitmap = ScreenShotFb.shootHandWrite(
					// (MainActivity) ctrlContext, _padLeft, _padTop,
					// _viewWidth, _viewHeight);
					new Thread(new Runnable() {
						@Override
						public void run() {
							saveImage(bitmap);
							client.UploadHandwritingFile(Environment
									.getExternalStorageDirectory()
									+ "/MagicInfData/HandWrite-Cache/"
									+ fileName);
							_sendComplete = true;
						}
					}).start();
					_handWriteView.clear();
					// Toast.makeText(ctrlContext, "发送成功",
					// Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ctrlContext, "发送间隔过短，请稍后再试",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		_clearButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				_handWriteView.clear();
			}
		});

	}

	private void saveImage(Bitmap bmp) {
		File appDir = new File(Environment.getExternalStorageDirectory(),
				"MagicInfData/HandWrite-Cache");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		// fileName = "Android-" + calendar.get(Calendar.YEAR) + "-"
		// + (calendar.get(Calendar.MONTH) + 1) + "-"
		// + calendar.get(Calendar.DAY_OF_MONTH) + "-"
		// + calendar.get(Calendar.HOUR_OF_DAY) + "-"
		// + calendar.get(Calendar.MINUTE) + "-"
		// + calendar.get(Calendar.SECOND) + ".png";

		fileName = "Android-" + System.currentTimeMillis() + ".png";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void uninitialize() {

		_handWriteView = null;
		_sendButton = null;
		_clearButton = null;
	}

	@Override
	public void stopWork() {

	}

	static public Handwrite Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("handwriting_panel"))
			return null;

		Handwrite retValue = new Handwrite();
		retValue.LoadBaseInfo(xpp);
		retValue._sendLetf = Double.parseDouble(xpp.getAttributeValue(null,
				"SendAreaLeft"));
		retValue._sendTop = Double.parseDouble(xpp.getAttributeValue(null,
				"SendAreaTop"));
		retValue._sendHeight = Double.parseDouble(xpp.getAttributeValue(null,
				"SendAreaHeight"));
		retValue._sendWidth = Double.parseDouble(xpp.getAttributeValue(null,
				"SendAreaWidth"));
		retValue._clearLeft = Double.parseDouble(xpp.getAttributeValue(null,
				"ClearAreaLeft"));
		retValue._clearTop = Double.parseDouble(xpp.getAttributeValue(null,
				"ClearAreaTop"));
		retValue._clearWidth = Double.parseDouble(xpp.getAttributeValue(null,
				"ClearAreaWidth"));
		retValue._clearHeight = Double.parseDouble(xpp.getAttributeValue(null,
				"ClearAreaHeight"));
		retValue._penWith = Integer.parseInt(xpp.getAttributeValue(null,
				"PenWidth"));
		retValue._penColor = Integer.parseInt(xpp.getAttributeValue(null,
				"PenColor"));

		return retValue;
	}

	public static Bitmap getBitmapFromView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//		Drawable bgDrawable = v.getBackground();
//		if (bgDrawable != null)
//			bgDrawable.draw(c);
//		else
//			c.drawColor(Color.WHITE);
		v.draw(c);
		return b;
	}
}
