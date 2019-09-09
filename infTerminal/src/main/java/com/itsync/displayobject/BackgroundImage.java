package com.itsync.displayobject;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@SuppressWarnings("deprecation")
public class BackgroundImage extends DisplayObject {
	private String _fileName;
	private ImageView _imageView;
	private Bitmap bm;
	// private Context cx;
	// private Point scrPoint;
	// @SuppressWarnings("unused")
	// private ViewGroup viewGroup;
//	private Thread _thread;
	@SuppressWarnings("unused")
	private boolean _work;
	private ViewGroup _rootLayout;
	private boolean _runOnce;
//    private int _viewWidth;
//    private int _viewHeight;
	// Runnable _mRunnable;

	public BackgroundImage() {
		_fileName = "";
		_imageView = null;
		_work = false;
		_rootLayout = null;
		_runOnce = false;
	}

	public void initialize(Context ctrlContext, Point screenSize,
			final ViewGroup rootLayout) {
		// if (_imageView != null)
		// return;
		//
		// if (_fileName != null && !_fileName.isEmpty()) {
		// bm = BitmapFactory.decodeFile(_fileName);
		//
		// if (null != bm) {
		// _imageView = new ImageView(ctrlContext);
		// _imageView.setScaleType(ScaleType.FIT_XY);
		// // _imageView.setScaleType(ScaleType.CENTER_CROP);
		//
		// AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
		// (int)(screenSize.x * _width), (int)(screenSize.y * _height),
		// (int)(screenSize.x * _left), (int)(screenSize.y * _top));
		//
		// _imageView.setLayoutParams(params);
		// _imageView.setImageBitmap(bm);
		// rootLayout.addView(_imageView);
		// }
		// // bm= null;
		// }

		if (_imageView != null)
			return;
		_rootLayout = rootLayout;
		_runOnce = true;
		// cx = ctrlContext;
		// scrPoint = screenSize;
		// viewGroup = rootLayout;
		_imageView = new ImageView(ctrlContext);
		_imageView.setLayerType(View.LAYER_TYPE_NONE, null);
		_imageView.setScaleType(ScaleType.FIT_XY);
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				(int) (screenSize.x * _width), (int) (screenSize.y * _height),
				(int) (screenSize.x * _left), (int) (screenSize.y * _top));

		_imageView.setLayoutParams(params);
		_work = true;
		rootLayout.addView(_imageView);
		
		try {
			bm = BitmapFactory.decodeFile(_fileName);
			_imageView.setImageBitmap(bm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		_thread = new Thread(_mRunnable);
//		_thread.start();

	}

	public void uninitialize() {
		if (_imageView.getParent()!=null && _rootLayout != null) {
			_rootLayout.removeView(_imageView);
		}
//		if (null != _thread) {
//			 try {
//				 _runOnce = false;
//			 _thread.join();
//			 } catch (InterruptedException e) {
//			 e.printStackTrace();
//			 }
//			_thread = null;
//		}
//		if (_mRunnable != null) {
//			_mRunnable = null;
//		}
		_work = false;
		_imageView = null;
		_fileName = null;
		if (bm != null) {
			bm.recycle();
		}
		bm = null;
		_rootLayout = null;
		_runOnce = false;
		System.gc();
	}

//	Runnable _mRunnable = new Runnable() {
//
//		@Override
//		public void run() {
//
//			final ImageView bacImageView = _imageView;
//			while (_runOnce && !_fileName.equalsIgnoreCase("")) {
//				if (_fileName != null && !_fileName.isEmpty() && bm == null) {
//					bm = BitmapFactory.decodeFile(_fileName);
////					bm = compressBitmapFromResourse(_fileName,_viewWidth,_viewHeight);
//				}
//				final Bitmap bacBitmap = bm;
//				final ViewGroup rootView = _rootLayout;
//				if (rootView != null && null != bacBitmap && bacImageView != null ) {
//
//					rootView.post(new Runnable() {
//
//						@Override
//						public void run() {
//
//							if (null != bacBitmap && bacImageView != null) {
//								bacImageView.setImageBitmap(bacBitmap);
//								_work =false;
//								_runOnce = false;
//							}
//						}
//					});
//				}
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	};

	
	
	static public BackgroundImage Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("background_image"))
			return null;

		int fileNum = Integer.parseInt(xpp.getAttributeValue(null, "FileNum"));
		if (fileNum <= 0)
			return null;

		BackgroundImage retValue = new BackgroundImage();
		retValue.LoadBaseInfo(xpp);
		if (xpp.getAttributeValue(null, "FilePath") != null) {
			retValue._fileName = String.format("%s/%s/10001.png", basePath,
					xpp.getAttributeValue(null, "FilePath"));
		}

		return retValue;
	}

	@Override
	public void stopWork() {
		_runOnce = false;
//		_thread.interrupt();
	}
}
