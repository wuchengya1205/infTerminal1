package com.itsync.displayobject;

import java.io.IOException;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.view.ViewGroup;

import com.itsync.displaypage.DisplayItem;
import com.itsync.displaypage.DisplayItems;

public abstract class DisplayObject {

	protected String _name;

	public String getName() {
		return _name;
	}

	protected double _left;

	public double getLeft() {
		return _left;
	}

	protected double _top;

	public double getTop() {
		return _top;
	}

	protected double _width;

	public double getWidth() {
		return _width;
	}

	protected double _height;

	public double getHeight() {
		return _height;
	}

	protected boolean _interactive;

	public boolean isInteractive() {
		return _interactive;
	}

	protected boolean _canZoom;

	public boolean canZoom() {
		return _canZoom;
	}

	protected String[] _shownPages;

	public boolean isShownInPage(String pageName) {
		if (_shownPages == null)
			return true;

		for (int i = 0; i < _shownPages.length; i++) {
			if (pageName.equalsIgnoreCase(_shownPages[i]))
				return true;
		}

		return false;
	}

	protected String _jumpToPage;

	public String getJumpToPage() {
		return _jumpToPage;
	}

	protected DisplayItems _displayItems;

	public void addDisplayItemToHead(DisplayItem item) {
		synchronized (_displayItems) {
			_displayItems.addHead(item);
		}
	}

	public void addDisplayItem(DisplayItem item) {
		synchronized (_displayItems) {
			_displayItems.addTail(item);
		}
	}

	public void removeDisplayItem(UUID deliverItemID) {
		synchronized (_displayItems) {
			_displayItems.removeDisplayItem(deliverItemID);
		}
	}

	public boolean hasPlaylist() {
		return false;
	}

	protected String _currentPage;
	public String getPage() {
		
		return _currentPage;
	}

	public DisplayObject() {
		_name = "";
		_left = 0;
		_top = 0;
		_width = 1;
		_height = 1;

		_interactive = false;
		_canZoom = false;

		_shownPages = null;
		_jumpToPage = "";

		_currentPage = "";
		_displayItems = new DisplayItems();
	}

	public abstract void initialize(Context ctrlContext, Point screenSize,
			ViewGroup rootLayout);

	public abstract void uninitialize();

	public abstract void stopWork();
	static final String allPages = "&lt;所有页&gt;";
	
	public synchronized Bitmap compressBitmapFromResourse(String filePath,
			int reqWidth, int reqHeight) {

		Bitmap bitmap = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();

		while (reqWidth * reqHeight > 512000) {
			reqWidth /= 2;
			reqHeight /= 2;
		}

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		options.inSampleSize = inSampleSize;
		options.inPurgeable = true;
		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filePath, options);
		return bitmap;
	} 
	

	public void LoadBaseInfo(XmlPullParser xpp) throws XmlPullParserException,
			IOException {
		this._name = xpp.getAttributeValue(null, "Name");
		this._left = Double.parseDouble(xpp.getAttributeValue(null, "Left"));
		this._top = Double.parseDouble(xpp.getAttributeValue(null, "Top"));
		this._width = Double.parseDouble(xpp.getAttributeValue(null, "Width"));
		this._height = Double.parseDouble(xpp.getAttributeValue(null, "Height"));
		this._interactive = (Integer.parseInt(xpp.getAttributeValue(null,
				"Interactive")) != 0);
		try {
			String canZoomString = xpp.getAttributeValue(null, "CanZoom");
			if (null != canZoomString) 
				this._canZoom = (Integer.parseInt(canZoomString) != 0);
			else
				this._canZoom = false;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String shownPages = _currentPage = xpp.getAttributeValue(null, "ShownPages");
		if (!shownPages.contains(allPages))
			this._shownPages = shownPages.split(";");

		this._jumpToPage = xpp.getAttributeValue(null, "JumpToPage");
	}
}
