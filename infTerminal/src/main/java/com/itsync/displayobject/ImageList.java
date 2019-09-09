package com.itsync.displayobject;

import java.io.IOException;
import java.util.ArrayList;

//import com.nostra13.universalimageloader.core.imageaware.ImageNonViewAware;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewFlipper;

import com.itsync.displaypage.DisplayItem;
import com.itsync.infterminal.R;

//import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressWarnings("deprecation")
public class ImageList extends DisplayObject {

	private int _effectDuration;
	private int _keepDuration;
	static final int ASPECT_TYPE_AUTO = 0;
	static final int ASPECT_TYPE_KEEP = 1;
	static final int ASPECT_TYPE_FILL = 2;
	private int _aspectType;
	@SuppressWarnings("unused")
	private boolean _embedText;
	@SuppressWarnings("unused")
	private int _fontSize;
	@SuppressWarnings("unused")
	private int _fontColor;
	@SuppressWarnings("unused")
	private String _fontName;
	// private ImageLoader imageLoader;
	@SuppressWarnings("unused")
	private int _textAlign;
	@SuppressWarnings("unused")
	private double _textLeft;
	@SuppressWarnings("unused")
	private double _textTop;
	@SuppressWarnings("unused")
	private double _textWidth;
	@SuppressWarnings("unused")
	private double _textHeight;
	// private String _imageUrl;
	private ViewGroup _rootLayout;
	public ViewFlipper _viewflipper;
	private ImageView[] _imageView;
	private Bitmap[] _showBitmaps;
	private final int VIEW_NUM = 3;
	private int _currentViewIndex;
	public int _viewWidth;
	public int _viewHeight;
	private boolean _work;
	private Thread _workThread;
	private Context _context;
	private DisplayItem _showItem;
	private boolean ischanged = false;
	// private Point _size;
	private Bitmap image;
	private int hei;
	private int _lasthei;
	private boolean _isFirst;
	private boolean _isloaded;
	private GestureDetector myGestureDetector;
	// private int length;
	private boolean _isNext;
	private boolean _isNex;
	public static boolean IsAnima = false;
	private long switchTime;
	private long keepDuration;
	private boolean _animateOnce;
	private int _isActive;
	public static boolean _ActiveMode;
	@SuppressWarnings("unused")
	private int _ImageMode;
	@SuppressWarnings("unused")
	private int _screenWidth;
	private int _screenHeight;
	private boolean _scaleMode;
	// private ImageView _bacImageView;
	private TextConfig _subTextView;
	private ImageView _subBacImageView;
	private String _textBacImageFilePath;
	private int _position;
	private int _FontColor;
	private int _Fontsize;
	private String _Font;
	private String _subBacImgPath;
	// private int _currentViewLength;
	@SuppressWarnings("unused")
	private int _canZoom;

	public ImageList() {
		_context = null;
		_effectDuration = 0;
		_keepDuration = 5;
		_aspectType = ASPECT_TYPE_AUTO;

		_embedText = true;

		_fontSize = 20;
		_fontColor = 0xFFFFFFFF;
		_fontName = "微软雅黑";

		_textAlign = 0x11;

		_textLeft = 0.1;
		_textTop = 0.1;
		_textWidth = 0.1;
		_textHeight = 0.1;

		_rootLayout = null;
		_viewflipper = null;
		_imageView = null;
		_showBitmaps = null;
		_subTextView = null;
		_subBacImageView = null;
		_currentViewIndex = 0;
		_viewWidth = 1920;
		_viewHeight = 1080;
		_work = false;
		_workThread = null;
		_lasthei = 0;
		_isFirst = true;
		_isNext = false;
		_isNex = false;
		_animateOnce = false;
		_isActive = 0;
		_ImageMode = 0;
		_scaleMode = false;
		// _currentViewLength = 0;
		_canZoom = 0;

	}

	public boolean hasPlaylist() {
		return true;
	}

	public void initialize(Context ctrlContext, Point screenSize,
			ViewGroup rootLayout) {

		if (null != _viewflipper)
			return;
		_context = ctrlContext;
		_screenWidth = screenSize.x;
		_screenHeight = screenSize.y;
		_isloaded = false;
		// _bacImageView = new ImageView(ctrlContext);
		// _bacImageView.setImageResource(R.drawable.black);

		myGestureDetector = new GestureDetector(ctrlContext,
				new myOnGestureListener());
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		// imageLoader = ImageLoader.getInstance();
		_rootLayout = rootLayout;
		_viewflipper = new ViewFlipper(ctrlContext);
		_viewflipper.setAutoStart(false);
		_currentViewIndex = 0;
		_imageView = new ImageView[VIEW_NUM];
		_showBitmaps = new Bitmap[VIEW_NUM];
		for (int i = 0; i < VIEW_NUM; i++) {
			_imageView[i] = new ImageView(ctrlContext);
			switch (_aspectType) {
			case ASPECT_TYPE_AUTO:
			case ASPECT_TYPE_KEEP:
				_imageView[i].setScaleType(ScaleType.FIT_CENTER);
				break;

			case ASPECT_TYPE_FILL:
			default:
				_imageView[i].setScaleType(ScaleType.FIT_XY);
				break;
			}
			_showBitmaps[i] = null;
			_viewflipper.addView(_imageView[i]);
		}

		// MainActivity mainActivity = (MainActivity)ctrlContext;
		// mainActivity.BindOnTouch(this);

		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				_viewWidth, _viewHeight, (int) (screenSize.x * _left),
				(int) (screenSize.y * _top));
		_viewflipper.setLayoutParams(params);
		rootLayout.addView(_viewflipper);

		if (!_embedText) {
			int textWidth = (int) (screenSize.x * _textWidth);
			int textHeight = (int) (screenSize.y * _textHeight);
			AbsoluteLayout.LayoutParams params1 = new AbsoluteLayout.LayoutParams(
					textWidth, textHeight, (int) (screenSize.x * _textLeft),
					(int) (screenSize.y * _textTop));
			if (!"".equals(_subBacImgPath)) {
				_subBacImageView = new ImageView(_context);
				String path = _subBacImgPath + "/"
						+ _textBacImageFilePath.replaceAll("\\\\", "/");
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				_subBacImageView.setImageBitmap(bitmap);
				_subBacImageView.setLayoutParams(params1);
				rootLayout.addView(_subBacImageView);
			}
			_FontColor = _FontColor | 0xff000000;
			_subTextView = new TextConfig(_context);

			_subTextView.setLayoutParams(params1);

			_subTextView.init(_Fontsize, screenSize.x, screenSize.y, _Font);
			_subTextView.setTextColor(_FontColor);
			if (_position == 0) {
				_subTextView.setGravity(Gravity.START);
			} else if (_position == 1) {
				_subTextView.setGravity(Gravity.CENTER | Gravity.TOP);
			} else if (_position == 2) {
				_subTextView.setGravity(Gravity.END | Gravity.TOP);
			} else if (_position == 16) {
				_subTextView.setGravity(Gravity.START | Gravity.CENTER);
			} else if (_position == 17) {
				_subTextView.setGravity(Gravity.CENTER);
			} else if (_position == 18) {
				_subTextView.setGravity(Gravity.END | Gravity.CENTER);
			} else if (_position == 32) {
				_subTextView.setGravity(Gravity.BOTTOM | Gravity.START);
			} else if (_position == 33) {
				_subTextView.setGravity(Gravity.BOTTOM | Gravity.CENTER);
			} else if (_position == 34) {
				_subTextView.setGravity(Gravity.END | Gravity.BOTTOM);
			}
			rootLayout.addView(_subTextView);
		}

		_work = true;

		_workThread = new Thread(_workTask);
		_workThread.start();

		_viewflipper.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (_isActive == 1 && _ActiveMode) {

					myGestureDetector.onTouchEvent(event);
				}
				return true;
			}
		});

	}

	class myOnGestureListener extends SimpleOnGestureListener {

		// @Override
		// public boolean onScroll(MotionEvent e1, MotionEvent e2,
		// float distanceX, float distanceY) {
		// // _viewflipper.scrollBy(0,(int)(distanceY));
		// // _viewflipper.scrollTo(0,0);
		// // _isNext = true;
		// // _isNex = true;
		// _viewflipper.scrollBy(0, (int)(distanceY));
		// return super.onScroll(e1, e2, distanceX, distanceY);
		// }
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			DisplayItem displayItem = _showItem;
			if (displayItem != null) {

				if (!displayItem.isCrawlImage()) {

					if (e1.getX() - e2.getX() > 150
							&& _viewflipper.getInAnimation() == null) {
						Animation inanimation = AnimationUtils.loadAnimation(
								_context, R.anim.right_in);
						Animation outAnimation = AnimationUtils.loadAnimation(
								_context, R.anim.letf_out);
						_viewflipper.setInAnimation(inanimation);
						_viewflipper.setOutAnimation(outAnimation);
						_isNext = true;
						switchTime = System.currentTimeMillis();
						_animateOnce = true;
					} else if (e1.getX() - e2.getX() > 150
							&& _viewflipper.getInAnimation() != null) {
						if (_viewflipper.getInAnimation().hasEnded()) {
							Animation inanimation = AnimationUtils
									.loadAnimation(_context, R.anim.right_in);
							Animation outAnimation = AnimationUtils
									.loadAnimation(_context, R.anim.letf_out);
							_viewflipper.setInAnimation(inanimation);
							_viewflipper.setOutAnimation(outAnimation);
							_isNext = true;
							switchTime = System.currentTimeMillis();
							_animateOnce = true;
						}
					} else if (e1.getX() - e2.getX() < -150
							&& _viewflipper.getInAnimation() == null) {
						Animation inanimation = AnimationUtils.loadAnimation(
								_context, R.anim.letf_in);
						Animation outAnimation = AnimationUtils.loadAnimation(
								_context, R.anim.right_out);
						_viewflipper.setInAnimation(inanimation);
						_viewflipper.setOutAnimation(outAnimation);
						_isNext = true;
						_isNex = true;
						switchTime = System.currentTimeMillis();
						_animateOnce = true;
					} else if (e1.getX() - e2.getX() < -150
							&& _viewflipper.getInAnimation() != null) {
						if (_viewflipper.getInAnimation().hasEnded()) {

							Animation inanimation = AnimationUtils
									.loadAnimation(_context, R.anim.letf_in);
							Animation outAnimation = AnimationUtils
									.loadAnimation(_context, R.anim.right_out);
							_viewflipper.setInAnimation(inanimation);
							_viewflipper.setOutAnimation(outAnimation);
							_isNext = true;
							_isNex = true;
							switchTime = System.currentTimeMillis();
							_animateOnce = true;
						}
					}
				}
			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}

		// @Override
		// public boolean onDoubleTap(MotionEvent e) {
		// if (_canZoom == 1) {
		//
		// if (_ImageMode == 0) {
		// _scaleMode = true;
		// AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
		// _screenWidth, _screenHeight, 0, 0);
		// _viewflipper.setLayoutParams(params);
		// android.view.ViewGroup.LayoutParams params1 = _viewflipper
		// .getCurrentView().getLayoutParams();
		// params1.height = _screenHeight;
		// _viewflipper.getCurrentView().setLayoutParams(params1);
		// // _viewflipper.getCurrentView().requestLayout();
		// _ImageMode = 1;
		// int LastIndex = _viewflipper.getDisplayedChild() - 1;
		// if (LastIndex < 0) {
		// LastIndex = _viewflipper.getChildCount() - 1;
		// }
		// _viewflipper.getChildAt(LastIndex).setVisibility(
		// View.INVISIBLE);
		// AbsoluteLayout.LayoutParams params2 = new
		// AbsoluteLayout.LayoutParams(
		// _screenWidth, _screenHeight, 0, 0);
		// _bacImageView.setLayoutParams(params2);
		// _bacImageView.setScaleType(ScaleType.FIT_XY);
		// _rootLayout.addView(_bacImageView);
		// _bacImageView.bringToFront();
		// _viewflipper.bringToFront();
		// // _viewflipper.requestLayout();
		// // _viewflipper.invalidate();
		// } else {
		// AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
		// _viewWidth, _viewHeight,
		// (int) (_screenWidth * _left),
		// (int) (_screenHeight * _top));
		// _viewflipper.setLayoutParams(params);
		// android.view.ViewGroup.LayoutParams params1 = _viewflipper
		// .getCurrentView().getLayoutParams();
		// if (_currentViewLength == 0) {
		// params1.height = _viewHeight;
		// } else {
		// params1.height = hei;
		// }
		// _viewflipper.getCurrentView().setLayoutParams(params1);
		// _rootLayout.removeView(_bacImageView);
		// int LastIndex = _viewflipper.getDisplayedChild() - 1;
		// if (LastIndex < 0) {
		// LastIndex = _viewflipper.getChildCount() - 1;
		// }
		// _viewflipper.getChildAt(LastIndex).setVisibility(
		// View.INVISIBLE);
		// _ImageMode = 0;
		// _scaleMode = false;
		//
		// }
		// }
		// return super.onDoubleTap(e);
		// }
	}

	public synchronized void uninitialize() {
		_work = false;
		if (_viewflipper.getParent()!=null && _rootLayout != null) {
			_rootLayout.removeView(_viewflipper);
		}
		if (null != _workThread) {
			try {
				_workThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			_workThread = null;
		}

		_viewflipper = null;
		_imageView = null;
		if (_showBitmaps != null) {
			for (int i = 0; i < VIEW_NUM; i++) {
				if (_showBitmaps[i] != null) {
					_showBitmaps[i].recycle();
				}
			}
			_showBitmaps = null;
			image = null;
		}
		if (null != _subTextView) {
			_subTextView = null;
		}
		_rootLayout = null;

		// System.gc();
	}

	int c_speedFactor = 80;

	Runnable _workTask = new Runnable() {

		@Override
		public void run() {
			switchTime = System.currentTimeMillis();
			keepDuration = _keepDuration;
			double viewAspect = 0;
			if (_viewHeight > 0)
				viewAspect = _viewWidth / (double) _viewHeight;

			ArrayList<String> fileList = new ArrayList<>();
			DisplayItem TemshowItem = null;

			while (_work) {

				if ((System.currentTimeMillis() - switchTime >= -500 && (_isloaded == false))
						|| _isNext) {
					keepDuration = _keepDuration;
					if (fileList.size() <= 0) {
						_showItem = null;

						synchronized (_displayItems) {
							while (_displayItems.getItemNum() > 0) {

								// _showItem = _displayItems.removeHead();
								// if (_showItem.getDataType() ==
								// DisplayItem.DataType_Image) {
								// _displayItems.addTail(_showItem);
								// break;

								if (!_isNex) {

									_showItem = _displayItems.removeHead();
									if (_showItem.getDataType() == DisplayItem.DataType_Image) {
										_displayItems.addTail(_showItem);
										if (_showItem == TemshowItem) {
											_showItem = _displayItems
													.removeHead();
											if (_showItem.getDataType() == DisplayItem.DataType_Image) {
												_displayItems
														.addTail(_showItem);
											}
											break;
										}
										break;
									}
								} else {
									_showItem = _displayItems.removeTail();
									if (_showItem.getDataType() == DisplayItem.DataType_Image) {
										_displayItems.addHead(_showItem);
										if (_showItem == TemshowItem) {
											_showItem = _displayItems
													.removeTail();
											if (_showItem.getDataType() == DisplayItem.DataType_Image) {
												_displayItems
														.addHead(_showItem);
											}
											break;
										}
										break;
									}
								}
							}
							TemshowItem = _showItem;
						}

						if (_showItem != null) {
							int showSeconds = _showItem.getShowSeconds();
							if (showSeconds > 0)
								keepDuration = showSeconds * 1000;
							else
								keepDuration = _keepDuration;

							// }
							if (_showItem.getFileList() != null) {

								final String[] files = _showItem.getFileList();
								for (int j = 0; j < files.length; j++) {
									fileList.add(files[j]);
								}
							}
							if (_showItem.isCrawlImage()) {
								keepDuration = 0;
							}
						}
					}

					if (fileList.size() > 0) {
						final String imageFileName = fileList.remove(0);
						// _imageUrl = Scheme.FILE.wrap(imageFileName);
						if (!_showItem.isCrawlImage()) {
							// ImageNonViewAware imageAware = new
							// ImageNonViewAware(String.valueOf(1), null,
							// ViewScaleType.CROP);
							// image = imageLoader.displayImage(imageFileName,
							// imageAware,null,null,null);
							// image = imageLoader.loadImageSync(_imageUrl);
							// iamge = imageLoader.loadImage(uri, listener);
							image = null;
							image = BitmapFactory.decodeFile(imageFileName);
							if (!_animateOnce) {

								if (IsAnima) {
									//
									Animation inAnimation = AnimationUtils
											.loadAnimation(_context,
													R.anim.fade_in);
									inAnimation.setDuration(_effectDuration);
									_viewflipper.setInAnimation(inAnimation);

									Animation outAnimation = AnimationUtils
											.loadAnimation(_context,
													R.anim.fade_out);
									outAnimation.setDuration(_effectDuration);
									_viewflipper.setOutAnimation(outAnimation);

								} else {

									_viewflipper.setInAnimation(null);
									_viewflipper.setOutAnimation(null);
								}
							}
							// _currentViewLength = 0;
						} else {
							Bitmap bitmap = BitmapFactory
									.decodeFile(imageFileName);
							int width = bitmap.getWidth();
							int height = bitmap.getHeight();
							double proportion = (double) (_viewWidth)
									/ (double) (width);
							Matrix matrix = new Matrix();
							matrix.postScale((float) (proportion),
									(float) (proportion));
							image = null;
							image = Bitmap.createBitmap(bitmap, 0, 0, width,
									height, matrix, true);
							// if (!bitmap.isRecycled()) {
							// bitmap.recycle();
							// }

							// int wid = image.getWidth();
							hei = image.getHeight();
							if (_lasthei == 0) {
								_lasthei = hei;
							}
							if (!_scaleMode) {
								if (hei > _viewHeight) {

									ischanged = true;

									TranslateAnimation longViewInAnimation = new TranslateAnimation(
											0, 0, _viewHeight,
											-(hei - _viewHeight));

									long inDuration = hei * c_speedFactor
											/ _showItem.getCrawlSpeed();
									longViewInAnimation.setDuration(inDuration);
									keepDuration = inDuration;
									longViewInAnimation
											.setInterpolator(new LinearInterpolator());
									_viewflipper
											.setInAnimation(longViewInAnimation);

									// _currentViewLength = 1;
								} else {
									TranslateAnimation shortViewInAnimation = new TranslateAnimation(
											0, 0, _viewHeight, 0);
									long inDuration = _viewHeight
											* c_speedFactor
											/ _showItem.getCrawlSpeed();
									shortViewInAnimation
											.setDuration(inDuration);
									keepDuration = inDuration;
									shortViewInAnimation
											.setInterpolator(new LinearInterpolator());
									_viewflipper
											.setInAnimation(shortViewInAnimation);
									ischanged = false;
									// _currentViewLength = 0;
								}
							} else {
								if (hei > _screenHeight) {
									ischanged = true;

									TranslateAnimation longViewInAnimation = new TranslateAnimation(
											0, 0, _screenHeight,
											-(hei - _screenHeight));

									long inDuration = hei * c_speedFactor
											/ _showItem.getCrawlSpeed();
									longViewInAnimation.setDuration(inDuration);
									keepDuration = inDuration;
									longViewInAnimation
											.setInterpolator(new LinearInterpolator());
									_viewflipper
											.setInAnimation(longViewInAnimation);
								} else {
									TranslateAnimation shortViewInAnimation = new TranslateAnimation(
											0, 0, _screenHeight, 0);
									long inDuration = _screenHeight
											* c_speedFactor
											/ _showItem.getCrawlSpeed();
									shortViewInAnimation
											.setDuration(inDuration);
									keepDuration = inDuration;
									shortViewInAnimation
											.setInterpolator(new LinearInterpolator());
									_viewflipper
											.setInAnimation(shortViewInAnimation);
									ischanged = false;
								}
							}
							if (!_animateOnce) {
								if (!_scaleMode) {
									if (_lasthei > _viewHeight) {
										if (_isFirst) {
											_isFirst = false;
										} else {
											TranslateAnimation translateOutAnimation = new TranslateAnimation(
													0, 0,
													-(_lasthei - _viewHeight),
													-_lasthei);
											long outDuration = _viewHeight
													* c_speedFactor
													/ _showItem.getCrawlSpeed();
											translateOutAnimation
													.setDuration(outDuration);
											// translateOutAnimation.setDuration(_showItem
											// .getCrawlSpeed() * 1000);
											translateOutAnimation
													.setInterpolator(new LinearInterpolator());
											_viewflipper
													.setOutAnimation(translateOutAnimation);
										}
										_lasthei = hei;
									} else {
										if (_isFirst) {
											_isFirst = false;
										} else {
											TranslateAnimation translateOutAnimation = new TranslateAnimation(
													0, 0, 0, -_viewHeight);
											long outDuration = _viewHeight
													* c_speedFactor
													/ _showItem.getCrawlSpeed();
											translateOutAnimation
													.setDuration(outDuration);
											translateOutAnimation
													.setInterpolator(new LinearInterpolator());
											_viewflipper
													.setOutAnimation(translateOutAnimation);
										}
										_lasthei = hei;
									}
								} else {
									if (_lasthei > _screenHeight) {
										if (_isFirst) {
											_isFirst = false;
										} else {
											TranslateAnimation translateOutAnimation = new TranslateAnimation(
													0,
													0,
													-(_lasthei - _screenHeight),
													-_lasthei);
											long outDuration = _screenHeight
													* c_speedFactor
													/ _showItem.getCrawlSpeed();
											translateOutAnimation
													.setDuration(outDuration);
											// translateOutAnimation.setDuration(_showItem
											// .getCrawlSpeed() * 1000);
											translateOutAnimation
													.setInterpolator(new LinearInterpolator());
											_viewflipper
													.setOutAnimation(translateOutAnimation);
										}
										_lasthei = hei;
									} else {
										if (_isFirst) {
											_isFirst = false;
										} else {
											TranslateAnimation translateOutAnimation = new TranslateAnimation(
													0, 0, 0, -_screenHeight);
											long outDuration = _screenHeight
													* c_speedFactor
													/ _showItem.getCrawlSpeed();
											translateOutAnimation
													.setDuration(outDuration);
											translateOutAnimation
													.setInterpolator(new LinearInterpolator());
											_viewflipper
													.setOutAnimation(translateOutAnimation);
										}
										_lasthei = hei;
									}
								}
							}
						}

					}
					_isloaded = true;
					_isNext = false;
					_animateOnce = false;
					_isNex = false;
				} else if ((System.currentTimeMillis() - switchTime >= 0 || _isloaded)) {

					_isloaded = false;
					if (null != image) {
						_currentViewIndex++;
						if (_currentViewIndex >= VIEW_NUM)
							_currentViewIndex = 0;
						final ViewFlipper viewfilpper = _viewflipper;
						final ImageView imageView = _imageView[_currentViewIndex];
						final double windowAspect = viewAspect;
						final Bitmap oldShowBitmaps = _showBitmaps[_currentViewIndex];
						_showBitmaps[_currentViewIndex] = image;
						final DisplayItem showItem = _showItem;
						if (showItem != null) {

							_rootLayout.post(new Runnable() {
								@Override
								public void run() {
									if (null != _subTextView) {
										_subTextView.setText(showItem.getText());
									}
									if (_aspectType == ASPECT_TYPE_AUTO) {
										double imageAspect = 0;
										int cyImage = 0;
										if (image != null) {
											cyImage = image.getHeight();
										}
										if (cyImage > 0)
											imageAspect = image.getWidth()
													/ (double) cyImage;

										boolean keepAspect = (Math
												.abs(windowAspect - imageAspect)
												/ (windowAspect + imageAspect) > 0.05);
										if (!showItem.isCrawlImage()) {

											if (keepAspect) {
												imageView
														.setScaleType(ScaleType.FIT_CENTER);
											} else {
												imageView
														.setScaleType(ScaleType.FIT_XY);
											}
										}
									}

									if (!showItem.isCrawlImage()) {
										if (_scaleMode) {

											android.view.ViewGroup.LayoutParams params = imageView
													.getLayoutParams();
											params.height = _screenHeight;
											imageView.setLayoutParams(params);
											imageView.setImageBitmap(image);
										} else {
											android.view.ViewGroup.LayoutParams params = imageView
													.getLayoutParams();
											params.height = _viewHeight;
											imageView.setLayoutParams(params);
											imageView.setImageBitmap(image);
										}
									} else {
										if (_scaleMode) {
											android.view.ViewGroup.LayoutParams params = imageView
													.getLayoutParams();
											params.height = _screenHeight;
											imageView.setLayoutParams(params);
											imageView.setImageBitmap(image);
										} else {
											if (ischanged) {

												android.view.ViewGroup.LayoutParams params = imageView
														.getLayoutParams();
												int height = 0;
												if (image != null) {

													height = image.getHeight();
												}
												params.height = height;
												imageView
														.setLayoutParams(params);

												ischanged = false;
											} else {
												LayoutParams params = imageView
														.getLayoutParams();
												params.height = _viewHeight;
												imageView
														.setLayoutParams(params);

											}
											imageView.setImageBitmap(image);
										}
									}
									if (oldShowBitmaps != null) {
										oldShowBitmaps.recycle();
									}
									System.gc();
									// if (!_isNex) {

									viewfilpper.showNext();

									// }else {
									// viewfilpper.showPrevious();
									// }
								}
							});
						} else {
							_rootLayout.post(new Runnable() {

								@Override
								public void run() {

									imageView.setImageBitmap(null);
								}
							});
						}
					}
					// _isNex = false;
					switchTime += keepDuration;
				}
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	static public ImageList Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;


		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("image_list"))
			return null;

		ImageList retValue = new ImageList();
		retValue.LoadBaseInfo(xpp);

		Integer.parseInt(xpp.getAttributeValue(null, "EffectID"));

		retValue._effectDuration = Integer.parseInt(xpp.getAttributeValue(null,
				"EffectDuration"));
		retValue._keepDuration = Integer.parseInt(xpp.getAttributeValue(null,
				"KeepDuration"));
		retValue._aspectType = Integer.parseInt(xpp.getAttributeValue(null,
				"AspectType"));

		retValue._embedText = (Integer.parseInt(xpp.getAttributeValue(null,
				"EmbedText")) != 0);

		retValue._fontName = xpp.getAttributeValue(null, "Font");
		retValue._fontSize = Integer.parseInt(xpp.getAttributeValue(null,
				"FontSize"));
		retValue._textAlign = Integer.parseInt(xpp.getAttributeValue(null,
				"TextAlign"));

		retValue._textLeft = Double.parseDouble(xpp.getAttributeValue(null,
				"TextLeft"));
		retValue._textTop = Double.parseDouble(xpp.getAttributeValue(null,
				"TextTop"));
		retValue._textWidth = Double.parseDouble(xpp.getAttributeValue(null,
				"TextWidth"));
		retValue._textHeight = Double.parseDouble(xpp.getAttributeValue(null,
				"TextHeight"));
		retValue._isActive = Integer.parseInt(xpp.getAttributeValue(null,
				"Interactive"));
		retValue._canZoom = Integer.parseInt(xpp.getAttributeValue(null,
				"CanZoom"));
		retValue._textBacImageFilePath = xpp.getAttributeValue(null,
				"TextBackgroundImageFile");
		retValue._position = Integer.parseInt(xpp.getAttributeValue(null,
				"TextAlign"));
		retValue._FontColor = Integer.parseInt(xpp.getAttributeValue(null,
				"FontColor"));
		retValue._Fontsize = Integer.parseInt(xpp.getAttributeValue(null,
				"FontSize"));
		retValue._Font = xpp.getAttributeValue(null, "Font");

		retValue._subBacImgPath = basePath;
		return retValue;
	}

	@Override
	public void stopWork() {
		_work = false;
		_workThread.interrupt();
	}

}
