package com.itsync.displayobject;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.itsync.displaypage.DisplayItem;
import com.itsync.infbase.ItSyncRtspPlayer;
import com.itsync.infterminal.MainActivity;
import com.itsync.infterminal.R;

@SuppressLint("ClickableViewAccessibility")
@SuppressWarnings("deprecation")
public class VideoList extends DisplayObject {
	private ViewGroup _rootLayout;
	private Video videoView;
	public int _viewWidth;
	public int _viewHeight;
	private static int viewWidth;
	private static int viewHeight;
	private boolean _work;
	private Thread thread;
	// private MediaController mediaController;
	private boolean _shouldLoadVideo;
	private SeekBar seekBar;
	private boolean _isShow;
	private long _disappearTime;
	private boolean tag;
	private ImageView playorpause;
	private LinearLayout linearGroup;
	private int _isActive;
	private Thread activeThread;
	public static boolean _ActiveMode;
	private int count;
	private long firstClick;
	private long lastClick;
	private int videoMode;
	private boolean preventDouble;
	private int _isZoom;
	private int pro = 0;
	private boolean isChanges;
	public static int _padLeft;
	public static int _padTop;
	public int left;
	public int top;
	private static int _currentTime = 0;
	MediaMetadataRetriever retriever;
	// private DisplayItem _currentDisplayItem;
	private WebView _webView;
	@SuppressWarnings("unused")
	private int startX;
	private GestureDetector mGestureDetector;
	private String _webPath;
	private ProgressBar _webProgressBar;
	static String videoPath;
	static String _type;

	// private SurfaceView surfaceView;
	 private ItSyncRtspPlayer _player;
	// private LinearLayout linearLayout;
	// ObjectAnimator anim;

	public VideoList() {
		_shouldLoadVideo = true;
		_rootLayout = null;
		videoView = null;
		// _viewWidth = 1920;
		// _viewHeight = 1080;
		_work = false;
		_isShow = false;
		tag = true;
		_isActive = 0;
		videoMode = 0;
		preventDouble = true;
		_isZoom = 0;
		isChanges = false;
		// _padLeft = 0;
		// _padTop = 0;
		retriever = new MediaMetadataRetriever();
		_webView = null;
		startX = 0;
		_webProgressBar = null;
		// videoPath = "12121212";

		// SurfaceView view = new SurfaceView(context);
		// ItSyncRtspPlayer _player = new ItSyncRtspPlayer();

		// _player.close();
	}

	public boolean hasPlaylist() {
		return true;
	}

	@SuppressLint("ClickableViewAccessibility")
	public void initialize(final Context ctrlContext, final Point screenSize,
			final ViewGroup rootLayout) {
		if (null != videoView)
			return;
		// surfaceView = new SurfaceView(ctrlContext);
		 _player = new ItSyncRtspPlayer();
		linearGroup = new LinearLayout(ctrlContext);
		_webView = new WebView(ctrlContext);
		// mediaController = new MediaController(ctrlContext);
		// linearLayout = new LinearLayout(ctrlContext);
		_webProgressBar = new ProgressBar(ctrlContext, null,
				android.R.attr.progressBarStyleHorizontal);
		// _webProgressBar.setProgressDrawable(android.R.drawable.progress_horizontal);
		seekBar = new SeekBar(ctrlContext);
		Drawable draw = ctrlContext.getResources().getDrawable(
				R.drawable.player_setting_bright_progressbar);
		Drawable draw1 = ctrlContext.getResources()
				.getDrawable(R.drawable.thum);
		draw.setBounds(seekBar.getProgressDrawable().getBounds());
		seekBar.setProgressDrawable(draw);
		seekBar.setThumb(draw1);
		seekBar.setVisibility(View.INVISIBLE);
		playorpause = new ImageView(ctrlContext);
		videoView = new Video(ctrlContext);
		_rootLayout = rootLayout;
		_viewWidth = (int) (screenSize.x * _width);
		_viewHeight = (int) (screenSize.y * _height);
		AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
				_viewWidth, _viewHeight, (int) (screenSize.x * _left),
				(int) (screenSize.y * _top));
		// _padLeft = (int) (screenSize.x * _left);
		// _padTop = (int) (screenSize.y * _top);
		videoView.setLayoutParams(params);
		// surfaceView.setLayoutParams(params);
		// videoView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		// linearLayout.setLayoutParams(params);
		// LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// // lp1.gravity = Gravity.CENTER;/
		// videoView.setLayoutParams(lp1);
		// linearLayout.addView(videoView);
		AbsoluteLayout.LayoutParams params2 = new AbsoluteLayout.LayoutParams(
				_viewWidth, LayoutParams.WRAP_CONTENT,
				(int) (screenSize.x * _left), (int) (screenSize.y * _top)
						+ _viewHeight * 6 / 7);
		AbsoluteLayout.LayoutParams params3 = new AbsoluteLayout.LayoutParams(
				_viewWidth, _viewHeight, (int) (screenSize.x * _left),
				(int) (screenSize.y * _top));
		linearGroup.setLayoutParams(params3);
		linearGroup.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		playorpause.setLayoutParams(lp);
		linearGroup.addView(playorpause);
		seekBar.setLayoutParams(params2);
		videoView.setVisibility(View.INVISIBLE);
		rootLayout.addView(videoView);
		rootLayout.addView(seekBar);
		rootLayout.addView(linearGroup);
		// rootLayout.addView(surfaceView);
		mGestureDetector = new GestureDetector(ctrlContext,
				new mGestureListener());
		if (_isActive == 1 && _ActiveMode) {

			playorpause.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (videoView.isPlaying()) {
						videoView.pause();
						playorpause.setImageResource(R.drawable.playvf);
					} else {
						videoView.start();
						playorpause.setImageResource(R.drawable.pausevf);
						_isShow = true;
					}
				}
			});
			videoView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (preventDouble) {
						if (seekBar.isShown()) {
							seekBar.setVisibility(View.INVISIBLE);
							// playorpause.setVisibility(View.VISIBLE);
							if (videoView.isPlaying()) {
								playorpause.setImageResource(0);
							}
							_isShow = false;
						} else {

							seekBar.setVisibility(View.VISIBLE);
							playorpause.setVisibility(View.VISIBLE);
							if (videoView.isPlaying()) {
								playorpause
										.setImageResource(R.drawable.pausevf);
							}
							_isShow = true;
						}
					}
					if (event.getAction() == MotionEvent.ACTION_DOWN
							&& _isZoom == 1) {
						if (firstClick != 0
								&& System.currentTimeMillis() - firstClick > 400) {
							count = 0;
						}
						count++;
						if (count == 1) {
							firstClick = System.currentTimeMillis();
						} else if (count == 2) {
							lastClick = System.currentTimeMillis();
							if (lastClick - firstClick < 400) {
								preventDouble = false;
								if (videoMode == 0) {
//									if (((MainActivity)ctrlContext)._canReadCard) {
//										Toast.makeText(ctrlContext, "有课表", Toast.LENGTH_LONG).show();
//									}
									AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
											screenSize.x, screenSize.y, 0, 0);
									videoView.setLayoutParams(params);
									/*
									 * 待完善视频全屏特效 ValueAnimator valueAnimator =
									 * ValueAnimator.ofInt(_viewWidth,
									 * screenSize.x); //监听变化过程
									 * valueAnimator.addUpdateListener(new
									 * ValueAnimator.AnimatorUpdateListener() {
									 * 
									 * @Override public void
									 * onAnimationUpdate(ValueAnimator
									 * animation) { //获取当前值 // int
									 * animatedValueMargin = (int)
									 * animation.getAnimatedValue();
									 * 
									 * // android.view.ViewGroup.LayoutParams //
									 * layoutParams =
									 * videoView.getLayoutParams();
									 * 
									 * AbsoluteLayout.LayoutParams params =
									 * (LayoutParams)
									 * videoView.getLayoutParams(); params.x =
									 * 0; params.y = 0; params.width = (Integer)
									 * animation.getAnimatedValue();
									 * videoView.setLayoutParams(params);
									 * rootLayout.requestLayout(); } });
									 * valueAnimator.setDuration(3000);
									 * 
									 * valueAnimator.setTarget(videoView);
									 * valueAnimator.start();
									 * 
									 * ValueAnimator valueAnimator1 =
									 * ValueAnimator.ofInt(_viewHeight,
									 * screenSize.y); //监听变化过程
									 * valueAnimator1.addUpdateListener(new
									 * ValueAnimator.AnimatorUpdateListener() {
									 * 
									 * @Override public void
									 * onAnimationUpdate(ValueAnimator
									 * animation) { //获取当前值 // int
									 * animatedValueMargin = (int)
									 * animation.getAnimatedValue();
									 * 
									 * AbsoluteLayout.LayoutParams params =
									 * (LayoutParams)
									 * videoView.getLayoutParams(); params.x =
									 * 0; params.y = 0; params.height =
									 * (Integer) animation.getAnimatedValue();
									 * videoView.setLayoutParams(params); } });
									 * valueAnimator1.setDuration(3000);
									 * 
									 * valueAnimator1.setTarget(videoView);
									 * valueAnimator1.start();
									 */

									AbsoluteLayout.LayoutParams params2 = new AbsoluteLayout.LayoutParams(
											screenSize.x,
											LayoutParams.WRAP_CONTENT, 0,
											screenSize.y * 6 / 7);
									seekBar.setLayoutParams(params2);
									AbsoluteLayout.LayoutParams params3 = new AbsoluteLayout.LayoutParams(
											screenSize.x, screenSize.y, 0, 0);
									linearGroup.setLayoutParams(params3);
									videoMode = 1;
									preventDouble = true;
									videoView.bringToFront();
									seekBar.bringToFront();
									linearGroup.bringToFront();
									// videoView.setAnimation(new
									// ScaleAnimation(0, 0, 0, 0));
									// videoView.setVisibility(View.INVISIBLE);
									// ObjectAnimator scaleX =
									// ObjectAnimator.ofFloat(linearLayout,
									// "scaleX", 1f, 2f);
									// ObjectAnimator scaleY =
									// ObjectAnimator.ofFloat(linearLayout,
									// "scaleY", 1f, 2F);
									// AnimatorSet animSet = new AnimatorSet();
									// animSet.play(scaleX).with(scaleY);
									// animSet.setDuration(3000);
									// animSet.start();
									// videoView.setVisibility(View.VISIBLE);
									// videoView.postInvalidate();
									// anim = ObjectAnimator//
									// .ofFloat(videoView, "scaleX", 1.0F,
									// 8.2F)//
									// .setDuration(5000);//
									// anim.start();

								} else {
									AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
											_viewWidth, _viewHeight,
											(int) (screenSize.x * _left),
											(int) (screenSize.y * _top));
									videoView.setLayoutParams(params);
									AbsoluteLayout.LayoutParams params2 = new AbsoluteLayout.LayoutParams(
											_viewWidth,
											LayoutParams.WRAP_CONTENT,
											(int) (screenSize.x * _left),
											(int) (screenSize.y * _top)
													+ _viewHeight * 6 / 7);
									seekBar.setLayoutParams(params2);
									AbsoluteLayout.LayoutParams params3 = new AbsoluteLayout.LayoutParams(
											_viewWidth, _viewHeight,
											(int) (screenSize.x * _left),
											(int) (screenSize.y * _top));
									linearGroup.setLayoutParams(params3);
									videoView.bringToFront();
									seekBar.bringToFront();
									linearGroup.bringToFront();
									videoMode = 0;
									preventDouble = true;

								}

							}
							count = 0;
							firstClick = 0;
							lastClick = 0;
						}
					}

					return false;
				}
			});
		}
		videoView.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {
				synchronized (VideoList.this) {
					_shouldLoadVideo = true;
				}
			}
		});

		videoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// 设置当前播放的位置
				return true;// 如果设置true就可以防止他弹出错误的提示框！
			}
		});
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(final SeekBar seekBar) {
				// videoView.seekTo(seekBar.getProgress());
				tag = true;
				_isShow = true;
				isChanges = false;
				// videoView.start();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				tag = false;
				isChanges = true;
				// pro = seekBar.getProgress();
				// videoView.pause();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				// seekBar.setMax(videoView.getDuration());
				if (fromUser) {
					// if (videoView != null
					// && Math.abs(seekBar.getProgress() - pro) > 20000) {//
					// 暂停状态下拖动seekbar低频率刷新帧画面
					pro = seekBar.getProgress();
					videoView.seekTo(pro);

					// }

				}

			}
		});
		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				try {
					seekBar.setMax(videoView.getDuration());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		activeThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (_work) {
					// _currentTime = seekBar.getProgress();
					if (tag) {
						if (_isShow) {
							_disappearTime = System.currentTimeMillis();
							_isShow = false;
						}
					}
					if (_disappearTime > 0) {
						if (System.currentTimeMillis() - _disappearTime > 3000) {
							_disappearTime = 0;
							if (tag) {

								_rootLayout.post(new Runnable() {
									public void run() {
										if (seekBar != null) {
											seekBar.setVisibility(View.INVISIBLE);
										}
										if (videoView != null) {

											if (videoView.isPlaying()) {
												playorpause.setImageResource(0);
											}
										}
									}
								});
							}
						}

					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		activeThread.start();
		_shouldLoadVideo = true;
		_work = true;

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (_work) {
					Boolean shouldLoadVideo = false;
					if (seekBar.getProgress() != 0) {
						_currentTime = seekBar.getProgress();
					}
					synchronized (VideoList.this) {
						shouldLoadVideo = _shouldLoadVideo;
					}
					if (!isChanges) {
						try {

							int current = videoView.getCurrentPosition();
							seekBar.setProgress(current);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					DisplayItem videoItem = null;
					if (shouldLoadVideo) {
						synchronized (_displayItems) {
							while (_displayItems.getItemNum() > 0) {
								videoItem = _displayItems.removeHead();
								// _currentDisplayItem = videoItem;

								// retriever.setDataSource(videoItem.getFileName());
								if (videoItem.getDataType() == DisplayItem.DataType_Video) {
									_displayItems.addTail(videoItem);
									break;
								} else if (videoItem.getDataType() == DisplayItem.DataType_WebPage) {
									break;
								}
							}
						}
					}

					if (videoItem != null) {

						switch (videoItem.getDataType()) {
						case DisplayItem.DataType_Video:
							_type = "1";
							viewWidth = _viewWidth;
							viewHeight = _viewHeight;
							_padLeft = (int) (screenSize.x * _left);
							_padTop = (int) (screenSize.y * _top);
							synchronized (VideoList.this) {
								_shouldLoadVideo = false;
							}

							final String fileName = videoItem.getFileName();
							final Video video = videoView;
							videoItem = null;
							_rootLayout.post(new Runnable() {

								@Override
								public void run() {
									if (!(video == null || fileName == null)) {
										video.setVisibility(View.VISIBLE);
//										 videoView.setVisibility(View.INVISIBLE);
										// _rootLayout.addView(surfaceView);
										// surfaceView.setLayerType(View.LAYER_TYPE_NONE,
										// null);
//										 _player.open("ItSyncRtsp://192.168.99.67/CH1_sub",
//										 video.getHolder().getSurface());
										videoPath = fileName.substring(7);
										video.setVideoPath(fileName);// file.getAbsolutePath());
//										 video.setMediaController(mediaController);
										// mediaController.setAnchorView(videoView);
										video.start();
									}
								}
							});
							break;

						case DisplayItem.DataType_WebPage:
							// _type = "2";
							try {
								final AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
										_viewWidth, _viewHeight,
										(int) (screenSize.x * _left),
										(int) (screenSize.y * _top));

								final AbsoluteLayout.LayoutParams params2 = new AbsoluteLayout.LayoutParams(
										_viewWidth, LayoutParams.WRAP_CONTENT,
										(int) (screenSize.x * _left),
										(int) (screenSize.y * _top));
								// _webView.setLayoutParams(params);
								// rootLayout.addView(_webView);

								synchronized (VideoList.this) {
									_shouldLoadVideo = false;
								}
								final String webPath = videoItem.getFileName();
								_webPath = webPath;
								_rootLayout.post(new Runnable() {

									@SuppressLint("SetJavaScriptEnabled")
									@Override
									public void run() {
										if (videoView != null
												&& seekBar != null
												&& playorpause != null) {
											videoView.setVisibility(View.GONE);
											seekBar.setVisibility(View.GONE);
											playorpause
													.setVisibility(View.GONE);
										}
										_webProgressBar
												.setLayoutParams(params2);
										_webView.setLayoutParams(params);
										rootLayout.addView(_webView);
										rootLayout.addView(_webProgressBar);
										_webView.setLayerType(
												View.LAYER_TYPE_NONE, null);
										WebSettings setting = _webView
												.getSettings();
										// setting.setSupportZoom(true);
										// setting.setBuiltInZoomControls(true);
										setting.setDomStorageEnabled(true);
										setting.setUseWideViewPort(true);
										setting.setLoadWithOverviewMode(true);
										setting.setJavaScriptEnabled(true);
										//setting.setPluginsEnabled(true);
										setting.setJavaScriptCanOpenWindowsAutomatically(true);
										setting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
										// setting.setUseWideViewPort(true);
										setting.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; rk3288 Build/KTU84Q) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Safari/537.36");
										// setting.setUserAgentString("电脑");
										// setting.setUserAgentString("Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X;en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334bSafari/531.21.10");
										_webView.setWebViewClient(new WebViewClient() {
											@Override
											public boolean shouldOverrideUrlLoading(
													WebView view, String url) {
												// view.loadUrl(url);
												return false;
											}

											@Override
											public void onPageFinished(
													WebView view, String url) {
												// TODO Auto-generated method
												// stub
												super.onPageFinished(view, url);
												// view.loadUrl("javascript:try{autoplay();}catch(e){}");
											}
										});
										_webView.setWebChromeClient(new WebChromeClient() {
											@Override
											public void onProgressChanged(
													WebView view,
													int newProgress) {
												super.onProgressChanged(view,
														newProgress);
												if (newProgress == 100) {
													_webProgressBar
															.setVisibility(View.GONE);
												} else {
													_webProgressBar
															.setVisibility(View.VISIBLE);
													_webProgressBar
															.setProgress(newProgress);
												}
											}

										});

										_webView.loadUrl(webPath);

									}
								});
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
							
						case DisplayItem.DataType_PlayVideo:
							
							final String UrlName = videoItem.getFileName();
							final Video PlayVideo = videoView;
							videoItem = null;
							_rootLayout.post(new Runnable() {

								@Override
								public void run() {
									if (!(PlayVideo == null || UrlName == null)) {
										PlayVideo.setVisibility(View.VISIBLE);
										 _player.open(UrlName,
												 PlayVideo.getHolder().getSurface());
									}
								}
							});
							break;
						default:
							break;
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
		thread.start();

		_webView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return false;
			}
		});

	}

	public Bitmap VideoViewImage() {
		// DisplayItem _dItem = null ;
		// synchronized (_displayItems) {
		//
		// }
		// VideoList v = new VideoList();

		// Class<VideoList> clazz = VideoList.class;

		// try {
		// Class class1 = Class.forName("com.itsync.displaypage.DisplayItems");
		// // Object displayItems = class1.newInstance();
		// Method m = class1.getDeclaredMethod("getTail", null);
		// DisplayItem dt = (DisplayItem) m.invoke(class1.newInstance(), null);
		// } catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }catch (NoSuchMethodException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalArgumentException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InvocationTargetException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InstantiationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		int tempWidth = viewWidth;
		int temHeight = viewHeight;
		String tempType = _type;
		String tempPath = videoPath;
		if (tempPath != null && tempType.equals("1")) {
			// _type = null;
			retriever.setDataSource(Environment.getExternalStorageDirectory()
					.getPath() + VideoList.videoPath);
			Bitmap bitmap = null;
			bitmap = retriever.getFrameAtTime(_currentTime * 1000,
					MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
			Bitmap newBitmap = ThumbnailUtils.extractThumbnail(bitmap,
					tempWidth, temHeight);
			bitmap.recycle();
//			System.gc();
			return newBitmap;
		}
		return null;
	}

	class mGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() - e2.getX() > 200 && _webView.canGoForward()) {
				_webView.goForward();
			} else if (e2.getX() - e1.getX() > 200 && _webView.canGoBack()) {
				_webView.goBack();
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			_webView.loadUrl(_webPath);
			super.onLongPress(e);
		}
	}

	public void uninitialize() {

		_work = false;
		if (null != thread) {
			try {
				thread.join();
				activeThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			thread = null;
			activeThread = null;
		}

		videoView = null;
		_rootLayout = null;
		seekBar = null;
		playorpause = null;
		if (_webView != null) {
			_webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
			_webView.clearHistory();

			// ((ViewGroup) _webView.getParent()).removeView(_webView);
			_webView.destroy();
			_webView = null;
		}
		// _currentDisplayItem = null;
		videoPath = null;
		_type = null;
		if (_player != null) {
			_player.close();
		}
	}

	static public VideoList Load(String basePath, XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;

		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
				|| !xpp.getName().equalsIgnoreCase("video_object"))
			return null;

		VideoList retValue = new VideoList();
		retValue.LoadBaseInfo(xpp);
		retValue._isActive = Integer.parseInt(xpp.getAttributeValue(null,
				"Interactive"));
		retValue._isZoom = Integer.parseInt(xpp.getAttributeValue(null,
				"CanZoom"));
		return retValue;
	}

	@Override
	public void stopWork() {
		// TODO Auto-generated method stub
		_work = false;
		thread.interrupt();
	}

}
