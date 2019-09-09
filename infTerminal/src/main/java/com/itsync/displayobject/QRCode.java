package com.itsync.displayobject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.itsync.displaypage.DisplayItem;
import com.itsync.helper.DateFormatMap;
import com.itsync.qrCode.QRCodeUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@SuppressWarnings("deprecation")
public class QRCode extends DisplayObject {
	public int _viewWidth;
	public int _viewHeight;

	private String _text;
	private boolean _appendTime;
	private String _timeFormat;
	private int _codeColor;
	private String _iconImageFile;

	private ViewGroup _rootLayout;
	private ImageView _imageView;
	
	private Thread _thread;
	private boolean _work;

	public boolean hasPlaylist() {
		return true;
	}

	public QRCode() {
		_viewWidth = 1920;
		_viewHeight = 1080;
		_work = false;

		this._text = null;
		this._appendTime = false;
		this._timeFormat = null;
		this._codeColor = Color.BLACK;
		this._iconImageFile = null;
	}
	
	static boolean startWith(CharSequence str1, int start1, CharSequence str2)
	{
		if (null == str1 || null == str2)
			return false;
		
		int len1 = str1.length() - start1;
		int len2 = str2.length();
		if (len1 > len2) len1 = len2; 

		for (int i = 0; i < len1; i++) {
			if (str1.charAt(i + start1)!= str2.charAt(i)) return false;
		}
		return true;
	}
	
	final String c_TerminalNameTag = "<TERMINAL_NAME>";
	final String c_TextTag = "<TEXT>";
	
	


	String buildText(){
		if (this._text == null || this._text.isEmpty())
			return null;

		final int MAX_CODE_LEN = 4096;
		char[] data = new char[MAX_CODE_LEN + 1];
		int textLen = this._text.length();
		int srcIndex = 0;
		int dataIndex = 0;
		int displayItemIndex = 0;
		
		String text = this._text;
		String terminalName = TerminalName._terminalName;
		while (srcIndex < textLen && dataIndex < MAX_CODE_LEN) {
			if (startWith(text, srcIndex,  c_TerminalNameTag)) {
				if (null != terminalName) {
					for (int i = 0; i < terminalName.length(); i++) 
						data[dataIndex++] = terminalName.charAt(i);
				}

				srcIndex += c_TerminalNameTag.length();			
			} else if (startWith(text, srcIndex,  c_TextTag)) {
				String itemText = null;
				while (itemText == null && displayItemIndex < this._displayItems.getItemNum()) {
					DisplayItem displayItem = this._displayItems.getItem(displayItemIndex++);
					if (displayItem.getDataType() == DisplayItem.DataType_Text)
						itemText = displayItem.getText();
				}
				
				if (null != itemText) {
					for (int i = 0; i < itemText.length(); i++)
						data[dataIndex++] = itemText.charAt(i);
				}
			
				srcIndex += c_TextTag.length();
			} else {
				data[dataIndex++] = text.charAt(srcIndex++);
			}
		}

		String retValue = String.copyValueOf(data, 0, dataIndex);
		
		if (this._appendTime && null != this._timeFormat && !this._timeFormat.isEmpty()) {
			Calendar calendar = Calendar.getInstance();
			String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
			int weekDayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			
			String formatString = this._timeFormat.replace("%A", weekDays[weekDayIndex]);
			SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatMap.TranslateDateFormat(formatString));
			
			retValue = retValue + dateFormat.format(calendar.getTime());
		}
		
//		System.out.println("buildText:" + retValue);
		return retValue;
	}

	@Override
	public void initialize(Context ctrlContext, Point screenSize,
			ViewGroup rootLayout) {
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);

		_imageView = new ImageView(ctrlContext);
		_imageView.setScaleType(ScaleType.FIT_XY);
		_rootLayout = rootLayout;

		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				(int) (screenSize.x * _width), (int) (screenSize.y * _height),
				(int) (screenSize.x * _left), (int) (screenSize.y * _top));
		_imageView.setLayoutParams(params);

		rootLayout.addView(_imageView);
		_work = true;
		_thread = new Thread(new Runnable() {

			@Override
			public void run() {
		        Bitmap iconBitmap = null;
				if (_iconImageFile != null && !_iconImageFile.isEmpty()) {
					String iconImageFile = StaticImage._imageFolder + _iconImageFile;
					iconBitmap = BitmapFactory.decodeFile(iconImageFile);
				}

				String lastTextString = null;
				final ArrayList<Bitmap> codeList = new ArrayList<Bitmap>();

				while (_work) {
					if (null != _text && !_text.isEmpty()) {
						String newText = buildText();
						
						if (newText != null && !newText.isEmpty() && !newText.equals(lastTextString)) {
							lastTextString = newText;
							
	                        final ImageView bacImageView = _imageView;
	                        final String showText = lastTextString;
					        final Bitmap logoBitmap = iconBitmap;
							_rootLayout.post(new Runnable() {
	
								@Override
								public void run() {
							        int width = 230;
							        int height = 230;
							        String error_correction_level = "H";
							        String margin = null;
							        int color_black = _codeColor | 0xFF000000;
							        int color_white = Color.WHITE;
							        Bitmap blackBitmap = null;
							        try {
								        Bitmap qrcode_bitmap = QRCodeUtil.createQRCodeBitmap(showText, width, height, "UTF-8",
								                error_correction_level, margin, color_black, color_white, logoBitmap, 0.2F, blackBitmap);
		
										if (qrcode_bitmap != null && bacImageView != null) {
											bacImageView.setImageBitmap(qrcode_bitmap);
										}
										
										for (Bitmap bitmap : codeList) {
											if (bitmap != null) {
												bitmap.recycle();
											}
										}
										codeList.clear();
										codeList.add(qrcode_bitmap);
									} catch (Exception e) {
									}
									
								}
							});
						}
					}
					try {
						for (int i = 0; _work && i < 100; i++)
							Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if (iconBitmap != null) {
					iconBitmap.recycle();
					iconBitmap = null;
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

		_imageView = null;
		_rootLayout = null;
//		System.gc();

	}

	static public QRCode Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("qr_code_object"))
			return null;

		QRCode retValue = new QRCode();
		retValue.LoadBaseInfo(xpp);

		retValue._text = xpp.getAttributeValue(null, "Text");
		retValue._appendTime = (0 != Integer.parseInt(xpp.getAttributeValue(null, "AppendTime")));
		retValue._timeFormat = xpp.getAttributeValue(null, "TimeFormat");

		retValue._codeColor = Integer.parseInt(xpp.getAttributeValue(null, "CodeColor"));
		retValue._iconImageFile = xpp.getAttributeValue(null, "IconImageFile");
		if (retValue._iconImageFile != null)
			retValue._iconImageFile = retValue._iconImageFile.replace('\\', '/');

		return retValue;
	}

	@Override
	public void stopWork() {
		_work = false;
		_thread.interrupt();
	}
}
