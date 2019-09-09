package com.itsync.displayobject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.itsync.infterminal.MainActivity;
import com.itsync.infterminal.TimeBackMainPage;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

public class DisplayTemplate {
	private int _width;

	public int getWidth() {
		return _width;
	}

	private int _height;

	public int getHeight() {
		return _height;
	}

	private ArrayList<DisplayObject> _items;

	public int getItemNum() {
		return _items.size();
	}

	public DisplayObject getItem(int index) {
		return _items.get(index);
	}

	public DisplayObject findItem(String objectName) {
		if (objectName == null)
			return null;

		for (int i = 0; i < _items.size(); i++) {
			DisplayObject item = _items.get(i);
			if (objectName.equalsIgnoreCase(item.getName()))
				return item;
		}
		return null;
	}

	MainActivity _mainActivity;
	public static String _filteName = "主页面";

	public DisplayTemplate() {
		this._items = new ArrayList<DisplayObject>();
		this._width = 1920;
		this._height = 1080;
		this._mainActivity = null;

	}

	static public DisplayTemplate Load(String xmlFileName) {
		File xmlFile = new File(xmlFileName);
		if (!xmlFile.exists())
			return null;

		FileInputStream IS = null;
		try {
			IS = new FileInputStream(xmlFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}

		String basePath = xmlFile.getParent();

		DisplayTemplate retValue = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			// factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			// xpp.setInput(IS, "utf-8");
			xpp.setInput(IS, "gb2312");

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {
					if (xpp.getName().equalsIgnoreCase("OpenShow_Define")) {
						retValue = new DisplayTemplate();
						retValue._width = Integer.parseInt(xpp
								.getAttributeValue(null, "Width"));
						retValue._height = Integer.parseInt(xpp
								.getAttributeValue(null, "Height"));
					} else if (null != retValue) {
						DisplayObject item = null;
						do {
							item = BackgroundImage.Load(basePath, xpp);
							if (item != null)
								break;

							item = ImageList.Load(basePath, xpp);
							if (item != null)
								break;

							item = VideoList.Load(basePath, xpp);
							if (item != null)
								break;

							item = Left_fly.Load(basePath, xpp);
							if (item != null)
								break;

							item = TimeShow.Load(basePath, xpp);
							if (item != null)
								break;

							item = Weather.Load(basePath, xpp);
							if (item != null)
								break;

							item = StaticImage.Load(basePath, xpp);
							if (item != null)
								break;

							item = TerminalName.Load(basePath, xpp);
							if (item != null)
								break;

							item = CountDown.Load(basePath, xpp);
							if (item != null)
								break;

							item = DataText.Load(basePath, xpp);
							if (item != null)
								break;

							item = DataImage.Load(basePath, xpp);
							if (item != null)
								break;

							item = Handwrite.Load(basePath, xpp);
							if (item != null)
								break;

							item = QRCode.Load(basePath, xpp);
							if (item != null)
								break;
						} while (false);
						if (item != null) {
							String[] pages = item.getPage().split(",");
							for (int i = 0; i < pages.length; i++) {
								if (pages[i].equalsIgnoreCase(_filteName)
										|| pages[i].equalsIgnoreCase("<所有页>")) {
									retValue._items.add(item);
									break;
								}
							}
							// if (item.getPage().contains(_filteName) ||
							// item.getPage().contains("<所有页>")) {
							// retValue._items.add(item);
							// }
						}
					}
				}

				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (IS != null) {
			try {
				IS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			IS = null;
		}
		return retValue;
	}

	private TimeBackMainPage mBackThread = TimeBackMainPage.getInstance();

	@SuppressWarnings("null")
	public void initialize(MainActivity mainActivity) {
		// 防止重复初始化
		if (_mainActivity != null) {
			return;
		}
		if (mainActivity == null)
			return;

		_mainActivity = mainActivity;
		ViewGroup rootLayout = _mainActivity.getRootLayout();
		rootLayout.removeAllViews();

		// Bitmap bt = rootLayout.getDrawingCache();
		// Drawable drawable = (Drawable)new BitmapDrawable(bt);
		// _mainActivity.getWindow().setBackgroundDrawable(drawable);

		// int viewCount = rootLayout.getChildCount();
		// View[] views = new View[viewCount];
		// for (int i = 0; i < viewCount; i++) {
		// views[i] = rootLayout.getChildAt(i);
		// }
		Point screenSize = _mainActivity.getRootLayoutSize();
		if (_items.size() != 0) {
			for (int i = 0; i < _items.size(); i++) {
				DisplayObject displayObject = _items.get(i);
				displayObject.initialize(_mainActivity, screenSize, rootLayout);
			}
		}

		if (!(DisplayTemplate._filteName.equals("主页面"))) {
			mBackThread.resetTime();
		}
		// try {
		// Thread.sleep(300);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// for (int i = 0; i < viewCount; i++) {
		// rootLayout.removeView(views[i]);
		// }

	}

	public void uninitialize() {
		if (_mainActivity != null) {
			// _mainActivity.clearContent();

			if (_items.size() != 0) {
				for (int i = 0; i < _items.size(); i++) {
					DisplayObject displayObject = _items.get(i);
					displayObject.stopWork();
				}

				for (int i = 0; i < _items.size(); i++) {
					DisplayObject displayObject = _items.get(i);
					displayObject.uninitialize();
				}
				System.gc();
			}
			_mainActivity = null;
		}
	}

}
