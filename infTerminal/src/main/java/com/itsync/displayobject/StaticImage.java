package com.itsync.displayobject;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.itsync.displaypage.DisplayItem;
import com.itsync.infterminal.MainActivity;

@SuppressWarnings("deprecation")
public class StaticImage extends DisplayObject {

	public int _viewWidth;
	public int _viewHeight;
	private String _Font;
	private int _FontColor;
	private int _Fontsize;
	private ImageView _mImageView;
	// private ImageView _mImageView;
	private Thread _thread;
	private boolean _work;
	private ViewGroup _rootLayout;
	public static String _imageFolder;
	private String _bgImage;
	private TextConfig _staticText;
	String backgroundimagefile;
	private Bitmap _bitmap;
	private int _position;
	private String _bgText;
	private int _isInterActive;
	public static boolean _ActiveMode;
	private MainActivity _mainActivity;
	private GestureDetector myGestureDetector;

	public StaticImage() {
		_viewWidth = 1920;
		_viewHeight = 1080;
		_work = false;
		_rootLayout = null;
		_isInterActive = 0;
	}

	@Override
	public void initialize(Context ctrlContext, Point screenSize,
			ViewGroup rootLayout) {
		_mainActivity = (MainActivity) ctrlContext;
		_rootLayout = rootLayout;
		_staticText = new TextConfig(ctrlContext);
//		_mImageView = new mImageView(ctrlContext);
		 _mImageView = new ImageView(ctrlContext);
		_mImageView.setScaleType(ScaleType.FIT_XY);
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		if (_position == 0) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_staticText.setLayoutParams(params1);
			_staticText.setGravity(Gravity.START);
		} else if (_position == 1) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_staticText.setLayoutParams(params1);
			// _staticText.setGravity(Gravity.TOP);
			_staticText.setGravity(Gravity.CENTER | Gravity.TOP);
		} else if (_position == 2) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_staticText.setLayoutParams(params1);
			_staticText.setGravity(Gravity.END | Gravity.TOP);
		} else if (_position == 16) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_staticText.setLayoutParams(params1);
			_staticText.setGravity(Gravity.START | Gravity.CENTER);
		} else if (_position == 17) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_staticText.setLayoutParams(params1);
			_staticText.setGravity(Gravity.CENTER);
		} else if (_position == 18) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_staticText.setLayoutParams(params1);
			_staticText.setGravity(Gravity.END | Gravity.CENTER);
		} else if (_position == 32) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_staticText.setLayoutParams(params1);
			_staticText.setGravity(Gravity.BOTTOM | Gravity.START);
		} else if (_position == 33) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_staticText.setLayoutParams(params1);
			_staticText.setGravity(Gravity.BOTTOM | Gravity.CENTER);
		} else if (_position == 34) {
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					_viewWidth, _viewHeight, (int) (screenSize.x * _left),
					(int) (screenSize.y * _top));
			_staticText.setLayoutParams(params1);
			_staticText.setGravity(Gravity.END | Gravity.BOTTOM);
		}

		_FontColor = _FontColor | 0xff000000;
		_staticText.setBackgroundColor(Color.argb(0, 0, 0, 0));
		// _staticText.setGravity(Gravity.CENTER);
		_staticText.init(_Fontsize, screenSize.x, screenSize.y, _Font);
		_staticText.setTextColor(_FontColor);

		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				(int) (screenSize.x * _width), (int) (screenSize.y * _height),
				(int) (screenSize.x * _left), (int) (screenSize.y * _top));
		_mImageView.setLayoutParams(params);
		_mImageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		rootLayout.addView(_mImageView);
		rootLayout.addView(_staticText);
		
		try {
			String filePath = _imageFolder + _bgImage;
			_bitmap = BitmapFactory.decodeFile(filePath);
			_mImageView.setImageBitmap(_bitmap);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		_work = true;

		myGestureDetector = new GestureDetector(ctrlContext,
				new myOnGestureListener());
		
		_staticText.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (_isInterActive == 1 && _ActiveMode) {

					myGestureDetector.onTouchEvent(event);
				}
				return true;
			}
		});
		
		
		

		_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String showText = null;
				String showImageFile = null;

				ArrayList<String> imageList = new ArrayList<String>();
				final ArrayList<String> textList = new ArrayList<String>();
				String url = _imageFolder;
//				final Counter counter = Counter.GetInstance();
				String shouldShowText = null;
				
				while (_work) {
					backgroundimagefile = null;
					// if (_bgImage != null) {
					if (!(_bgImage == null || _bgImage.isEmpty())) {
						backgroundimagefile = url + _bgImage;
					}
					imageList.clear();
					// imageList = null;
					textList.clear();
					// textList.add(object);
					if (backgroundimagefile != null) {
                      
						imageList.add(backgroundimagefile);
					}
					if (_bgText != null) {
						textList.add(_bgText);
					}
					synchronized (_displayItems) {

						for (int i = 0; i < _displayItems.getItemNum(); i++) {
							if (_displayItems.getItem(i).getDataType() == DisplayItem.DataType_Image) {
								String[] imageStrings = _displayItems
										.getItem(i).getFileList();
								for (int j = 0; j < imageStrings.length; j++) {
									imageList.add(imageStrings[j]);
								}
							} else {
								textList.add(_displayItems.getItem(i).getText());
							}
						}
					}

					
					if (textList.size() > 0)
						shouldShowText = textList.get(textList.size() - 1);
					if (!(shouldShowText == null || shouldShowText
							.equals(showText))) {
						showText = shouldShowText;
						final String textName = shouldShowText;
						final TextConfig staticText = _staticText;
						_rootLayout.post(new Runnable() {

							@Override
							public void run() {
								if (textName.trim().isEmpty()
										|| staticText == null) {
									// _staticText.setText("");
								} else {
									staticText.setText(textName);
								}
							}
						});

					}

//					String shouldShowImageFile = null;
//					if (imageList.size() > 0)
//						shouldShowImageFile = imageList.get(imageList.size() - 1);
//					if (!(shouldShowImageFile == null || shouldShowImageFile
//							.equals(showImageFile))) {
//						showImageFile = shouldShowImageFile;
//						final String imageFileName = shouldShowImageFile;
////						if (_jumpToPage.equalsIgnoreCase("办事指南") || _jumpToPage.equalsIgnoreCase("主页面")) {
////							if (_bitmap != null && new File(imageFileName).exists()) {
//								_bitmap = BitmapFactory.decodeFile(imageFileName);
////								_bitmap = compressBitmapFromResourse(imageFileName,_viewWidth,_viewHeight);
////							}
////						}
//						final ImageView mImageView = _mImageView;
////						final Bitmap temBitmap = _bitmap;
////						synchronized (this) {
////							if (counter.count >= 5) {
////								try {
////									wait();
////								} catch (InterruptedException e) {
////									e.printStackTrace();
////								}
////							}
////							counter.count++;
//							_rootLayout.post(new Runnable() {
//
//								@Override
//								public void run() {
//
//									// if (imageFileName.trim().isEmpty()) {
//									// _mImageView.setImageBitmap(null);
//									// } else {
//									// _mImageView.setImageBitmap(_bitmap);
//									// }
//									if (_bitmap != null && mImageView != null) {
////										if (!(temBitmap == null || mImageView == null)) {
//										mImageView.setImageBitmap(_bitmap);
////										System.gc();
////										_work = false;
//									}
//								}
//							});
//							
//						}
//					}
//                    synchronized (this) {
//                    	if (counter.count >= 5) {
//                    		counter.count = 0;
//                    		notify();
//                    	}
//					}
                    
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
		if (_mImageView.getParent()!=null && _rootLayout != null) {
			_rootLayout.removeView(_mImageView);
		}

		if (null != _thread) {
			try {
				_thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			_thread = null;
		}

		_staticText = null;
		_mImageView = null;
		if (_bitmap != null && !_bitmap.isRecycled()) {
			_bitmap.recycle();
			_bitmap = null;
		}
		_rootLayout = null;
//		System.gc();

	}

	
	class myOnGestureListener extends SimpleOnGestureListener{
		
		@Override
		public boolean onDown(MotionEvent e) {
			
			if ( !_jumpToPage.equals("")) {
				DisplayTemplate._filteName = _jumpToPage;
				_mainActivity._displayPageChanged = true;
			}
			
			
//			AnimatorSet animatorSet = new AnimatorSet();//组合动画  
//	        ObjectAnimator scaleX = ObjectAnimator.ofFloat(_mImageView, "scaleX", 0.9f);  
//	        ObjectAnimator scaleY = ObjectAnimator.ofFloat(_mImageView, "scaleY", 0.9f);  
//	  
//	        animatorSet.setDuration(100);  
//	        animatorSet.setInterpolator(new LinearInterpolator());  
//	        animatorSet.play(scaleX).with(scaleY);
//	        animatorSet.start(); 
			
			
			return super.onDown(e);
		}
		
//		@Override
//		public boolean onSingleTapUp(MotionEvent e) {
//			
//			AnimatorSet animatorSet = new AnimatorSet();
//	        ObjectAnimator scaleX = ObjectAnimator.ofFloat(_mImageView, "scaleX", 1.0f);  
//	        ObjectAnimator scaleY = ObjectAnimator.ofFloat(_mImageView, "scaleY", 1.0f);  
//	  
//	        animatorSet.setDuration(100);  
//	        animatorSet.setInterpolator(new LinearInterpolator());  
//	        animatorSet.play(scaleX).with(scaleY);
//	        animatorSet.start(); 
//	        
//	        animatorSet.addListener(new AnimatorListener() {
//				
//				@Override
//				public void onAnimationStart(Animator animation) {
//					
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animator animation) {
//					
//				}
//				
//				@Override
//				public void onAnimationEnd(Animator animation) {
//					if ( !_jumpToPage.equals("")) {
//						DisplayTemplate._filteName = _jumpToPage;
//						_mainActivity._displayPageChanged = true;
//					}
//				}
//				
//				@Override
//				public void onAnimationCancel(Animator animation) {
//					
//				}
//			});
//	        
//			return super.onSingleTapUp(e);
//		}
//		@Override
//		public void onLongPress(MotionEvent e) {
//			super.onLongPress(e);
//			
//			AnimatorSet animatorSet = new AnimatorSet();
//	        ObjectAnimator scaleX = ObjectAnimator.ofFloat(_mImageView, "scaleX", 1.0f);  
//	        ObjectAnimator scaleY = ObjectAnimator.ofFloat(_mImageView, "scaleY", 1.0f);  
//	  
//	        animatorSet.setDuration(100);  
//	        animatorSet.setInterpolator(new LinearInterpolator());  
//	        animatorSet.play(scaleX).with(scaleY);
//	        animatorSet.start(); 
//			
//		}
	}
	
	static public StaticImage Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("static_object"))
			return null;

		StaticImage retValue = new StaticImage();
		retValue.LoadBaseInfo(xpp);

		retValue._FontColor = Integer.parseInt(xpp.getAttributeValue(null,
				"FontColor"));
		retValue._Font = xpp.getAttributeValue(null, "Font");
		retValue._Fontsize = Integer.parseInt(xpp.getAttributeValue(null,
				"FontSize"));
		retValue._bgImage = xpp.getAttributeValue(null, "BackgroundImageFile");
		if (retValue._bgImage != null)
			retValue._bgImage = retValue._bgImage.replace('\\', '/');
		retValue._position = Integer.parseInt(xpp.getAttributeValue(null,
				"Align"));
		retValue._bgText = xpp.getAttributeValue(null, "Text");

		retValue._isInterActive = Integer.parseInt(xpp.getAttributeValue(null,
				"Interactive"));

		return retValue;
	}

	@Override
	public void stopWork() {
		// TODO Auto-generated method stub
		_work = false;
		_thread.interrupt();
	}

}
