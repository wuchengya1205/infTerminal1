package com.itsync.infterminal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.SocketException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

//import com.itsync.aidl.IService;
import com.itsync.displayobject.AutoScrollTextView;
import com.itsync.displayobject.DataText;
import com.itsync.displayobject.DisplayObject;
import com.itsync.displayobject.DisplayTemplate;
import com.itsync.displayobject.ImageList;
import com.itsync.displayobject.StaticImage;
import com.itsync.displayobject.TerminalName;
import com.itsync.displayobject.VideoList;
import com.itsync.displaypage.DisplayItem;
import com.itsync.displaypage.DisplayItems;
import com.itsync.displaypage.DisplayPage;
import com.itsync.infbase.DeliverItem;
import com.itsync.infbase.IInfoClientListener;
import com.itsync.infbase.InfoClient;
import com.itsync.infbase.WeatherInfo;
import com.itsync.infterminal.ShellUtils.CommandResult;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements IInfoClientListener, OnLayoutChangeListener {
	// private GestureDetector detector;
	private boolean _work;
	private Thread _workThread;
	private ImageView imageView;
	public boolean _displayPageChanged;
	private String _displayPageFileName;
	private Config _config;
	public Point _rootLayoutSize;
	private AudioManager audioManager;
	public static int Oritation;
	private String temDisplayPage;
	private String temTemplate;
	private boolean _isfirstLoad;
	private String temDisPlayPageFileMd5;
	private String temTemplateFileMd5;
	ArrayList<DeliverItem> _changedDeliverItems;
	ArrayList<DeliverItem> _waitDeliveredItems;
	ArrayList<DeliverItem> _deliveredItems;
	ArrayList<UUID> _removedItems;
	List<String> _cardData;
	// private static final String chars =
	// "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,;.'?/+-_=~!@#`";
	private AbsoluteLayout _rootLayout;
	private Thread _readCardThread;
	// private IService iService;
//	private Intent intent;
//	private Intent lbthdIntent;
	private Intent campusService;
//	private Intent campusIntent;
//	private Intent showStudentIntent;
//	private Intent hideStudentIntent;
//	private boolean _startService;
//	private boolean _canReadCard;
//	public boolean _isFirst;
	private Calendar calendar;
	public static final String COMMAND_SU = "su";
	public static final String COMMAND_SH = "sh";
	public static final String COMMAND_EXIT = "exit\n";
	public static final String COMMAND_LINE_END = "\n";
	Process logcatProcess;
	Process kernalProcess;
	boolean templateChanged;
	Thread _checkServerConnected;
	Thread _WatchDog;
	boolean _wdWork;
	private File file;
	private String handWrite_path = Environment.getExternalStorageDirectory() + "/MagicInfData/HandWrite-Cache";
	private static final String APF = "/data/poweralarmd/alarm.txt";
	private DisplayTemplate newTemplate;
	private LbthdBroadCastReceiver mReceiver;
//	private ItsyncCampusReceiver mItsyncReceiver;
	private TimeBackMainPage mBackThread;
	private String[] permission = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE };
	private List<String> mPermissionList = new ArrayList<>();
	private int REQUEST_CODE = 0x00;
	private boolean flag;

	public ViewGroup getRootLayout() {
		return _rootLayout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		Configuration mConfiguration = this.getResources().getConfiguration();
		int ori = mConfiguration.orientation;
		if (ori == Configuration.ORIENTATION_LANDSCAPE) {
			Oritation = 0;
		} else if (ori == Configuration.ORIENTATION_PORTRAIT) {
			Oritation = 1;
		}
		_changedDeliverItems = new ArrayList<DeliverItem>();
		_waitDeliveredItems = new ArrayList<DeliverItem>();
		_deliveredItems = new ArrayList<DeliverItem>();
		_removedItems = new ArrayList<UUID>();
		_cardData = new ArrayList<String>();
		_rootLayout = new AbsoluteLayout(this);
		// _rootLayout.setBackgroundColor(Color.TRANSPARENT);
		setContentView(_rootLayout);
		getWindow().setBackgroundDrawable(null);
		imageView = new ImageView(this);
		_rootLayoutSize = new Point(100, 100);
		_work = false;
		_workThread = null;
		_displayPageChanged = false;
		_displayPageFileName = "";
//		_config = null;
		temDisplayPage = "";
		_isfirstLoad = true;
		temDisPlayPageFileMd5 = "";
		temTemplateFileMd5 = "";
		temTemplate = "";
//		_startService = false;
//		_isFirst = true;
//		_canReadCard = false;
		templateChanged = false;
		_wdWork = false;
		file = new File(handWrite_path);
		// _loaded = false;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			getPermission();
		} else {
			initData();
		}

	}

	private void initData() {
		_config = Config.load();
		startInfoClient();
		Update();
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		imageView.setBackgroundResource(R.drawable._itsyncv2);
		_rootLayout.addView(imageView);
//		intent = new Intent();
//		intent.setAction("com.itsync.mService");
//		intent.setComponent(new ComponentName("com.itsync.cardService",
//				"com.itsync.cardService.mService"));
//
//		lbthdIntent = new Intent();
//		lbthdIntent.setAction("com.itsync.lbthdService");
//		lbthdIntent.setComponent(new ComponentName("com.itsync.lbtableservice",
//				"com.itsync.lbtableservice.lbthdService"));

		if (null != CommenConfigs.ACTION) {
			campusService = new Intent();
			campusService.setAction(CommenConfigs.ACTION);
			campusService.setComponent(new ComponentName(CommenConfigs.PACKAGE, CommenConfigs.CLASS));
		} else {
			campusService = null;
		}

//		campusIntent = new Intent();
//		campusIntent.setAction("com.itsync.campusDemo");
//		campusIntent.setComponent(new ComponentName("com.itsync.CoreService",
//				"com.itsync.CoreService.CampusService"));
//		campusIntent.putExtra("Tag", "移除所有项目");

//		showStudentIntent = new Intent();
//		showStudentIntent.setAction("com.itsync.campusDemo");
//		showStudentIntent.setComponent(new ComponentName(
//				"com.itsync.CoreService",
//				"com.itsync.CoreService.CampusService"));
//		showStudentIntent.putExtra("Tag", "刷卡");

//		hideStudentIntent = new Intent();
//		hideStudentIntent.setAction("com.itsync.campusDemo");
//		hideStudentIntent.setComponent(new ComponentName(
//				"com.itsync.CoreService",
//				"com.itsync.CoreService.CampusService"));
//		hideStudentIntent.putExtra("Tag", "移除学生课表");

		ScreenShotFb.init(MainActivity.this);

	}

	private void getPermission() {
		for (int i = 0; i < permission.length; i++) {
			if (ContextCompat.checkSelfPermission(MainActivity.this,
					permission[i]) != PackageManager.PERMISSION_GRANTED) {
				mPermissionList.add(permission[i]);
			}
		}
		if (mPermissionList.size() > 0) {
			ActivityCompat.requestPermissions(MainActivity.this, permission, REQUEST_CODE);
		} else {
			initData();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (REQUEST_CODE == requestCode) {
			for (int i = 0; i < grantResults.length; i++) {
				if (grantResults[i] == -1) {
					flag = true;
				} else {
					flag = false;
				}
			}
		}
		if (!flag) {
			initData();
		} else {
			Toast.makeText(MainActivity.this, "请授予权限", Toast.LENGTH_LONG).show();
			Uri packageURI = Uri.parse("package:" + getPackageName());
			Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
			startActivity(intent);
			finish();
		}
	}

	public void enableReadCard() {
//		this._canReadCard = true;
//		this._startService = true;
	}

	private void clearRunningData() {
		synchronized (this) {
			_changedDeliverItems.clear();
			_waitDeliveredItems.clear();
			_deliveredItems.clear();
			_removedItems.clear();
		}
	}

	private void stopServices() {
//		stopService(intent);
//		stopService(lbthdIntent);
		if (null != campusService)
			stopService(campusService);
//		stopService(campusIntent);
//		stopService(showStudentIntent);
	}

	private void Update() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// _config = Config.load();

				if (_config.IsAutoUpdate) {
					Looper.prepare();
					PackageManager packageManager = getPackageManager();
					UpdateManager manager = new UpdateManager(MainActivity.this, _config.UpdateIP, packageManager);
					manager.checkUpdate();
					Looper.loop();

				}
			}
		}).start();
	}

	private void startInfoClient() {
		clearRunningData();
//		_config = Config.load();
		CommenConfigs.ACTION = _config.action;
		CommenConfigs.PACKAGE = _config.packageName;
		CommenConfigs.CLASS = _config.className;
		CommenConfigs.DATA_TAGS = _config.dataTagList;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				InfoClient client = InfoClient.GetInstance();
				ImageList.IsAnima = _config.IsAnima;
				AutoScrollTextView.ActiveMode = _config.isActive;
				VideoList._ActiveMode = _config.isActive;
				ImageList._ActiveMode = _config.isActive;
				StaticImage._ActiveMode = _config.isActive;
				if (null != _config.mCurrentArea && _config.mCurrentArea.length() > 0) {
					WeatherInfo.setLocalWeatherArea(_config.mCurrentArea);
				}
				try {
					FileUtils.addTxtToFileBuffered("初始化一次");
					// client.SetPowerControllerPort("/dev/ttyS4");
					if (_config.isActive) {
						client.Initialize(_config.ServerIP, InfoClient.ClientType.Interactive, _config.TerminalName);

					} else {
						client.Initialize(_config.ServerIP, InfoClient.ClientType.Normal, _config.TerminalName);
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}
//				TerminalName._terminalName = client.getTerminalName();

			}
		});

		thread.start();
	}

	public void clearContent(boolean change) {

		if (null != CommenConfigs.ACTION && isServiceRunning(this, CommenConfigs.ACTION)) {
			Intent intent = new Intent();
			intent.setAction(CommenConfigs.ACTION);
			intent.setComponent(new ComponentName(CommenConfigs.PACKAGE, CommenConfigs.CLASS));
			intent.putExtra("Tag", "移除所有项目");
			startService(intent);
		}
		if (null != campusService)
			stopService(this.campusService);

//		_startService = false;
//		_canReadCard = false;
		if (imageView.getParent() != null) {
			_rootLayout.removeView(imageView);
		}
		// imageView.setBackgroundResource(0);
		if (_rootLayout != null) {

			// _rootLayout.removeAllViews();
			if (change) {
				_rootLayout.addView(imageView);
				// imageView.setBackgroundResource(R.drawable._itsyncv2);
			}
		}

//		_isFirst = true;
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (_rootLayout != null) {

			_rootLayout.addOnLayoutChangeListener(this);
		}
		startWork();
		registerLbthdReceiver();
		if (mBackThread == null) {
			mBackThread = TimeBackMainPage.getInstance();
			mBackThread.startWork(this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// stopWork();
		// if (_rootLayout != null) {
		//
		// _rootLayout.removeOnLayoutChangeListener(this);
		// }
		//
		// if (mBackThread != null) {
		// mBackThread.stopWork();
		// }

	}

	@Override
	protected void onStop() {
		super.onStop();
		stopWork();
		if (_rootLayout != null) {
			_rootLayout.removeOnLayoutChangeListener(this);
		}

		stopServices();
//		_canReadCard = false;
		InfoClient client = InfoClient.GetInstance();
		client.Uninitialize();

		clearRunningData();
		// _rootLayout = null;
		_wdWork = false;
		if (_checkServerConnected != null) {
			_checkServerConnected.interrupt();
		}
		if (mBackThread != null) {
			mBackThread.stopWork();
			mBackThread = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		InfoClient client = InfoClient.GetInstance();
		client.Uninitialize();

		clearRunningData();
		stopServices();
		// if (intent != null) {
		// stopService(intent);
		// }
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
//		if (mItsyncReceiver != null) {
//			unregisterReceiver(mItsyncReceiver);
//		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		startInfoClient();
		_displayPageChanged = true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (mBackThread != null) {
			mBackThread.stopWork();
			mBackThread = null;
		}
	}

	private void startWork() {
		InfoClient client = InfoClient.GetInstance();
		client.addEventListener(this);

		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		client.SetScreenRotation(rotation * 90);

		_work = true;
		// _loaded = true;
		_workThread = new Thread(_workTask);
		_workThread.start();

		_readCardThread = new Thread(readCardRunnable);
		_readCardThread.start();

		_checkServerConnected = new Thread(_checkRunnable);
		_checkServerConnected.start();

		if (null != _config && _config.openWatchDog) {
			_WatchDog = new Thread(_watchDog);
			_WatchDog.start();
		}
		removeLztdFile();
	}

	private void stopWork() {

		// if (_startService) {
		// stopService(intent);
		// }
		// _startService = false;
		InfoClient client = InfoClient.GetInstance();
		client.removeEventListener(this);

		_work = false;
		if (null != _workThread) {
			try {
				_workThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			_workThread = null;
		}
		if (null != _readCardThread) {
			try {
				_readCardThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			_readCardThread = null;
		}
	}

	@Override
	public void onDisplayPageChanged(final ArrayList<String> displayPages) {
		if (displayPages != null && displayPages.size() > 0) {
			synchronized (this) {
				_displayPageFileName = displayPages.get(0);
				FileUtils.addTxtToFileBuffered("Itsync页面名称:" + _displayPageFileName);
				try {
					if (displayPagesRealChanged() || _isfirstLoad) {
						_displayPageChanged = true;
						_isfirstLoad = false;
						_config.LastTemplate = _displayPageFileName;
						_config.save();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onInitialized(boolean arg0) {
	}

	@Override
	public void onRemoveDeliverItems(ArrayList<UUID> removedItems) {
		synchronized (this) {
			_removedItems.addAll(removedItems);
		}
	}

	@Override
	public void onSetPromptInfo(String objectName, String text, String imageFileName, int showTimeInMs) {
	}

	@Override
	public void onSetServerIP(String serverIP) {
		synchronized (MainActivity.this) {
			_config.ServerIP = serverIP;
			_config.save();
		}
		startInfoClient();
	}

	@Override
	public void onSetTextData(final String objectName, final String text) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				if (null == objectName || null == newTemplate)
					return;
				DisplayObject item = newTemplate.findItem(objectName);
				if (null != item && item instanceof DataText) {
					((DataText) item).setText(text);
				}

			}
		}).start();

	}

	@Override
	public void onUpdateDeliverItems(ArrayList<DeliverItem> deliverItems) {
		synchronized (this) {
			_changedDeliverItems.addAll(deliverItems);
		}
	}

	ByteArrayOutputStream outStream = null;
	Boolean captureFinished = false;

	@Override
	public ByteArrayOutputStream onCaptureScreen() {
		outStream = null;
		captureFinished = false;
//		if (_config.fastCapScreen) {
//			_rootLayout.post(new Runnable() {
//				@Override
//				public void run() {
//					outStream = ScreenShotFb.shoot(MainActivity.this);
//					captureFinished = true;
//				}
//			});
//			
//			while (!captureFinished) {
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		if (null == outStream)
			System.out.println("Capture screen return null");
		else
			System.out.println(String.format("Capture screen return %d bytes", outStream.size()));
		return outStream;

		// return ScreenCatch.shoot(this, this.getWindow().getDecorView());
//		if (_config.fastCapScreen)
//			return ScreenShotFb.shoot(MainActivity.this);
//		else
//			return null;
		// return ScreenCatch.shoot(this,this.getWindow().getDecorView());
		// ScreenCatch screenCatch = new ScreenCatch();
		// return screenCatch.shoot(this, this.getWindow().getDecorView());
	}

	Runnable _workTask = new Runnable() {
		@Override
		public void run() {
			DisplayTemplate displayTemplate = null;
			String displayPageName = null;
			String pageChange = "";
			// _loaded = true;

			InfoClient client = InfoClient.GetInstance();
			String lastTerminaName = client.getTerminalName();
			TerminalName._terminalName = lastTerminaName;

			while (_work) {
				String newTerminaName = client.getTerminalName();
				if (null != newTerminaName && lastTerminaName != newTerminaName) {
					lastTerminaName = newTerminaName;
					TerminalName._terminalName = lastTerminaName;
				}

				String displayPageFileName = null;
				synchronized (this) {
					if (_displayPageChanged) {
						_displayPageChanged = false;
						temDisplayPage = _displayPageFileName;
						displayPageFileName = _displayPageFileName;
						if (!pageChange.equalsIgnoreCase(_displayPageFileName)) {
							DisplayTemplate._filteName = "主页面";
							templateChanged = true;
						} else {
							templateChanged = false;
						}
						pageChange = _displayPageFileName;
						if (displayPageFileName != "") {
							try {
								temDisPlayPageFileMd5 = getMd5ByFile(new File(displayPageFileName));
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}

				if (displayPageFileName != null && !displayPageFileName.isEmpty()) {
					// _loaded = false;
					_rootLayout.post(new Runnable() {

						@Override
						public void run() {
							clearContent(templateChanged);
						}
					});
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}

					final DisplayPage newDisplayPage = DisplayPage.load(displayPageFileName);
					if (newDisplayPage != null) {
						String newDisplayPageName = displayPageFileName
								.substring(displayPageFileName.indexOf("/DisplayPages/"));
						newTemplate = DisplayTemplate.Load(newDisplayPage.getTemplateFileName());
						temTemplate = newDisplayPage.getTemplateFileName();
						try {
							temTemplateFileMd5 = getMd5ByFile(new File(newDisplayPage.getTemplateFileName()));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						// new Thread(new Runnable() {
						//
						// @Override
						// public void run() {
						StaticImage._imageFolder = newDisplayPage.getstaticOriginalFileNameString();
						// }
						// }).start();

						if (newTemplate != null) {

							for (int deliveritemIndex = 0; deliveritemIndex < _deliveredItems
									.size(); deliveritemIndex++) {
								DeliverItem deliverItem = _deliveredItems.get(deliveritemIndex);
								for (int i = 0; i < newTemplate.getItemNum(); i++) {
									DisplayObject displayObject = newTemplate.getItem(i);

									if (deliverItem.shouldDeliverTo(newDisplayPageName, displayObject.getName())) {
										displayObject.addDisplayItem(new DisplayItem(deliverItem));
									}
								}
							}

							for (int i = 0; i < newDisplayPage.getItemNum(); i++) {
								DisplayItems displayItems = newDisplayPage.getItem(i);
								DisplayObject displayObject = newTemplate.findItem(displayItems.getDisplayObjectName());

								if (displayObject != null) {
									if (displayObject.hasPlaylist()) {
										for (int j = 0; j < displayItems.getItemNum(); j++)
											displayObject.addDisplayItem(displayItems.getItem(j));
									} else {
										for (int j = displayItems.getItemNum() - 1; j >= 0; j--)
											displayObject.addDisplayItemToHead(displayItems.getItem(j));
									}
								}
							}

							final DisplayTemplate oldTemplate = displayTemplate;
							displayTemplate = newTemplate;
							displayPageName = newDisplayPageName;

							_rootLayout.post(new Runnable() {

								@Override
								public void run() {

									if (oldTemplate != null) {
										oldTemplate.uninitialize();
									}
									newTemplate.initialize(MainActivity.this);
									// }
									// System.gc();
								}
							});
						}

					}

				}

				if (null != displayTemplate) {
					for (int deliveritemIndex = _deliveredItems.size() - 1; deliveritemIndex >= 0; deliveritemIndex--) {
						DeliverItem deliverItem = _deliveredItems.get(deliveritemIndex);
						if (deliverItem.isFinish()) {
							for (int i = 0; i < displayTemplate.getItemNum(); i++) {
								DisplayObject displayObject = displayTemplate.getItem(i);
								displayObject.removeDisplayItem(deliverItem.ItemGuid);
							}
							_deliveredItems.remove(deliveritemIndex);
						}
					}
				}

				synchronized (this) {
					int removeNum = _removedItems.size();
					if (removeNum > 0) {
						for (int removeIndex = 0; removeIndex < removeNum; removeIndex++) {
							UUID itemID = _removedItems.get(removeIndex);

							if (null != displayTemplate) {
								for (int deliveritemIndex = _deliveredItems.size()
										- 1; deliveritemIndex >= 0; deliveritemIndex--) {
									for (int i = 0; i < displayTemplate.getItemNum(); i++) {
										DisplayObject displayObject = displayTemplate.getItem(i);
										displayObject.removeDisplayItem(itemID);
									}
								}
							}

							for (int deliveritemIndex = _waitDeliveredItems.size()
									- 1; deliveritemIndex >= 0; deliveritemIndex--) {
								DeliverItem deliverItem = _waitDeliveredItems.get(deliveritemIndex);
								if (deliverItem.ItemGuid.equals(itemID))
									_waitDeliveredItems.remove(deliveritemIndex);
							}

							for (int deliveritemIndex = _deliveredItems.size()
									- 1; deliveritemIndex >= 0; deliveritemIndex--) {
								DeliverItem deliverItem = _deliveredItems.get(deliveritemIndex);
								if (deliverItem.ItemGuid.equals(itemID))
									_deliveredItems.remove(deliveritemIndex);
							}
						}
						_removedItems.clear();
					}

					for (int changedItemIndex = 0; changedItemIndex < _changedDeliverItems.size(); changedItemIndex++) {
						DeliverItem newDeliverItem = _changedDeliverItems.get(changedItemIndex);

						if (null != displayTemplate) {
							for (int deliveritemIndex = _deliveredItems.size()
									- 1; deliveritemIndex >= 0; deliveritemIndex--) {
								for (int i = 0; i < displayTemplate.getItemNum(); i++) {
									DisplayObject displayObject = displayTemplate.getItem(i);
									displayObject.removeDisplayItem(newDeliverItem.ItemGuid);
								}
							}
						}

						for (int deliveritemIndex = _waitDeliveredItems.size()
								- 1; deliveritemIndex >= 0; deliveritemIndex--) {
							DeliverItem deliverItem = _waitDeliveredItems.get(deliveritemIndex);
							if (deliverItem.ItemGuid.equals(newDeliverItem.ItemGuid))
								_waitDeliveredItems.remove(deliveritemIndex);
						}

						for (int deliveritemIndex = _deliveredItems.size()
								- 1; deliveritemIndex >= 0; deliveritemIndex--) {
							DeliverItem deliverItem = _deliveredItems.get(deliveritemIndex);
							if (deliverItem.ItemGuid.equals(newDeliverItem.ItemGuid))
								_deliveredItems.remove(deliveritemIndex);
						}

						_waitDeliveredItems.add(0, newDeliverItem);
					}
					_changedDeliverItems.clear();
				}

				if (null != displayTemplate) {
					for (int waitItemIndex = _waitDeliveredItems.size() - 1; waitItemIndex >= 0; waitItemIndex--) {
						DeliverItem waitDeliverItem = _waitDeliveredItems.get(waitItemIndex);
						if (waitDeliverItem.shouldDeliver()) {
							_waitDeliveredItems.remove(waitItemIndex);
							for (int i = 0; i < displayTemplate.getItemNum(); i++) {
								DisplayObject displayObject = displayTemplate.getItem(i);

								if (waitDeliverItem.shouldDeliverTo(displayPageName, displayObject.getName())) {
									if (displayObject.hasPlaylist()) {
										displayObject.addDisplayItemToHead(new DisplayItem(waitDeliverItem));
									} else {
										displayObject.addDisplayItem(new DisplayItem(waitDeliverItem));
									}
								}
							}

							_deliveredItems.add(waitDeliverItem);
						}
					}
				}

				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// System.gc();
			}

			if (displayTemplate != null) {
				final DisplayTemplate oldTemplate2 = displayTemplate;
				displayTemplate = null;
				_rootLayout.post(new Runnable() {
					@Override
					public void run() {
						if (oldTemplate2 != null) {
							oldTemplate2.uninitialize();
						}
					}
				});
			}

		}
	};
	String cardIdData = "";
	long lastInputTime = System.currentTimeMillis();
	//
	// private void PrintCardNo(){
	// cardIdData = "";
	// }

	Runnable readCardRunnable = new Runnable() {

		@Override
		public void run() {

			while (_work) {

				synchronized (_cardData) {
					if (System.currentTimeMillis() - lastInputTime > 500 && !cardIdData.isEmpty()) {
						lastInputTime = System.currentTimeMillis();
						if (// _canReadCard
							// &&
						null != campusService) {
							if (cardIdData.length() == 10) {
								campusService.putExtra("ID", cardIdData);
								campusService.putExtra("Tag", "刷卡");
								startService(campusService);
//								startService(showStudentIntent);
							}
							cardIdData = "";
						} else {
							cardIdData = "";
						}
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	Runnable _checkRunnable = new Runnable() {

		@Override
		public void run() {

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (_displayPageFileName.equalsIgnoreCase("")) {
				_displayPageFileName = _config.LastTemplate;
				_displayPageChanged = true;
				Looper.prepare();
				Toast.makeText(MainActivity.this, "启动单机运行模式", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}
	};
//看门狗
	Runnable _watchDog = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			_wdWork = true;
			while (_wdWork) {
				intent.setAction("com.itsync.mWatchDogService");
				intent.setComponent(new ComponentName("com.itsync.infconfig", "com.itsync.infconfig.WatchDogService"));
				startService(intent);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public boolean dispatchKeyEvent(final KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			synchronized (cardIdData) {
				lastInputTime = System.currentTimeMillis();
				char ch = event.getNumber();
				if (ch != '\0') {
					cardIdData += ch;
				}
				// else {
				// PrintCardNo();
				// }
			}
		}

		return super.dispatchKeyEvent(event);
	}

	public synchronized Point getRootLayoutSize() {
		return _rootLayoutSize;
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
			int oldBottom) {

		// if (left != oldLeft || top != oldTop
		// || right != oldRight || bottom != oldBottom) {
		// System.out.println(String.format("onLayoutChange (%d,%d)-(%d,%d) =>
		// (%d,%d)-(%d,%d)",
		// oldLeft, oldTop, oldRight, oldBottom,
		// left, top, right, bottom));
		//
		// synchronized (this) {
		// _rootLayoutSize.x = right - left;
		// _rootLayoutSize.y = bottom - top;
		// _displayPageChanged = true;
		// }
		// }

		int newWidth = right - left;
		int newHeight = bottom - top;
		synchronized (this) {
			if (newWidth != _rootLayoutSize.x || newHeight != _rootLayoutSize.y) {
				// System.out.println(String.format("onLayoutChange (%d,%d)-(%d,%d)[%d,%d] =>
				// (%d,%d)-(%d,%d)[%d,%d]",
				// oldLeft, oldTop, oldRight, oldBottom, _rootLayoutSize.x,
				// _rootLayoutSize.y,
				// left, top, right, bottom, newWidth, newHeight));
				System.out.println(String.format("onLayoutChange [%d,%d] => [%d,%d]", _rootLayoutSize.x,
						_rootLayoutSize.y, newWidth, newHeight));

				_rootLayoutSize.x = newWidth;
				_rootLayoutSize.y = newHeight;
				_displayPageChanged = true;
			}
		}

	}

	@Override
	public void onSetVolumn(int volumn) {
		if (volumn == 0) {
			if (!(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)) {

				audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
			}
		} else {
			if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {

				audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
			}
		}
	}

	public boolean displayPagesRealChanged() throws FileNotFoundException {
		if (getMd5ByFile(new File(_displayPageFileName)).equalsIgnoreCase(temDisPlayPageFileMd5)
				&& _displayPageFileName.equalsIgnoreCase(temDisplayPage)
				&& DisplayPage.load(_displayPageFileName).getTemplateFileName().equalsIgnoreCase(temTemplate)
				&& getMd5ByFile(new File(DisplayPage.load(_displayPageFileName).getTemplateFileName()))
						.equalsIgnoreCase(temTemplateFileMd5)) {

			return false;
		}
		return true;
	}

	public synchronized String getMd5ByFile(File file) throws FileNotFoundException {
		String value = "";
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			System.out.println("exception:" + file.getName());
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("finally:" + file.getName());
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onSetPowerOnTime(int year, int month, int day, int hour, int minute) {

//		// 停止 看门狗
//	    Intent watchDogIntent = new Intent(); 
//	    watchDogIntent.setAction("com.itsync.mWatchDogService");
//	    watchDogIntent.setComponent(new ComponentName("com.itsync.infconfig",
//					"com.itsync.infconfig.WatchDogService"));
//			stopService(watchDogIntent);

		// 未设置有效的开机时间，不使用定时关机功能
		if (year == 0 || month == 0 || day == 0)
			return;

		/** Z19定时开关机 **/
		int[] timeOn = { year, month, day, hour, minute };
		int[] timeOff = new int[5];
		Calendar calendar = (Calendar) Calendar.getInstance().clone();
		calendar.add(Calendar.SECOND, 5);
		timeOff[0] = calendar.get(Calendar.YEAR);
		timeOff[1] = calendar.get(Calendar.MONTH) + 1;
		timeOff[2] = calendar.get(Calendar.DAY_OF_MONTH);
		timeOff[3] = calendar.get(Calendar.HOUR_OF_DAY);
		timeOff[4] = calendar.get(Calendar.MINUTE);
		FileUtils.addTxtToFileBuffered(
				"设置本次关机时间:" + timeOff[0] + "-" + timeOff[1] + "-" + timeOff[2] + "-" + timeOff[3] + "-" + timeOff[4]);
		FileUtils.addTxtToFileBuffered("设置下次开机时间:" + year + "-" + month + "-" + day + "-" + hour + "-" + minute);
		Intent intent = new Intent();
		intent.putExtra("timeoff", timeOff);
		intent.putExtra("timeon", timeOn);
		intent.putExtra("enable", true);
		intent.setAction("android.56iq.intent.action.setpoweronoff");
		sendBroadcast(intent);
		FileUtils.addTxtToFileBuffered("设置成功！！");
		// /** 众云世纪定时开关机 **/
		// String date = String.valueOf(year) + "-" + String.valueOf(month) +
		// "-"
		// + String.valueOf(day);
		// String time = String.valueOf(hour) + ":" + String.valueOf(minute);
		// Intent intentSjzy = new Intent("zysd.alarm.poweron.time");
		// intent.putExtra("poweronday", date);
		// intent.putExtra("powerontime", time);
		// sendBroadcast(intentSjzy);

		/*
		 * 普诺兴定时开关机 DeviceController controller = (DeviceController)
		 * SystemApi.getService(this, SystemApi.SERVICE_DEVICE); Calendar calendar =
		 * Calendar.getInstance(); calendar.set(year, month, day, hour, minute);
		 * controller.setRegularBootTime(calendar);
		 */
		// SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm");
		// String time= sDateFormat.format(new Date());
		// smdtManager.smdtSetTimingSwitchMachine(time,hour+":"+new
		// DecimalFormat("00").format(minute),
		// "1");

		// /** 联智通达定时开关机 **/
		//
		// int[] timeOffLztd = new int[3];
		// Calendar calendarLztd = Calendar.getInstance();
		// timeOffLztd[0] = calendarLztd.get(Calendar.HOUR_OF_DAY);
		// timeOffLztd[1] = calendarLztd.get(Calendar.MINUTE);
		// timeOffLztd[2] = calendarLztd.get(Calendar.SECOND);
		//
		// if (timeOffLztd[2] + 15 > 59) {
		// if (timeOffLztd[1] + 1 > 59) {
		// if (timeOffLztd[0] + 1 > 24) {
		// timeOffLztd[0] = timeOffLztd[1] = timeOffLztd[2] = -1;
		// } else {
		// timeOffLztd[0] += 1;
		// timeOffLztd[1] = 0;
		// timeOffLztd[2] -= 45;
		// }
		// } else {
		// timeOffLztd[1] += 1;
		// timeOffLztd[2] -= 45;
		// }
		// } else {
		// timeOffLztd[2] += 15;
		// }
		// String aNewAlarm = String.format("%02d", timeOffLztd[0]) + ":"
		// + String.format("%02d", timeOffLztd[1]) + ":"
		// + String.format("%02d", timeOffLztd[2]) + ","
		// + String.format("%02d", hour) + ":"
		// + String.format("%02d", minute) + ":"
		// + String.format("%02d", 0);
		//
		// insertAPF(aNewAlarm);
	}

	// private ServiceConnection connection = new ServiceConnection() {
	//
	// @Override
	// public void onServiceDisconnected(ComponentName name) {
	// iService = null;
	// }
	//
	// @Override
	// public void onServiceConnected(ComponentName name, IBinder service) {
	// iService = IService.Stub.asInterface(service);
	// if (iService == null) {
	// System.out.println("the iService is null");
	// } else {
	// try {
	//
	// String value = iService.SerData2(1, 1, 1, 1);
	// Toast.makeText(MainActivity.this, value, 1).show();
	// } catch (RemoteException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// };
	@Override
	public void onStartLogService() {
		final String path = Environment.getExternalStorageDirectory().getPath().toString() + "/ItsyncLog";
		calendar = Calendar.getInstance();
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}

		try {
			String command = "logcat -v time > " + path + "/logcat" + calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + ".txt";
			logcatProcess = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
		} catch (IOException e) {
			e.printStackTrace();
		}

		String command = "cat /proc/kmsg > " + path + "/kernal" + calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + ".txt";
		try {
			kernalProcess = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void onStopLogService() {

		try {
			CommandResult commandResult = ShellUtils.execCommand(new String[] { "ps | grep logcat" }, true);
			String[] arr = commandResult.successMsg.split("\\s+");
			if (arr[1] != null) {
				ShellUtils.execCommand(new String[] { "kill -9 " + arr[1] }, true);
			}
			CommandResult commandResult2 = ShellUtils.execCommand(new String[] { "ps | grep tmp-mksh" }, true);
			String[] arr2 = commandResult2.successMsg.split("\\s+");
			if (arr2[1] != null) {
				ShellUtils.execCommand(new String[] { "kill -9 " + arr2[1] }, true);
			}
		} catch (Exception e) {
		}

	}

	// private long getFolderSize(File file) {
	//
	// long size = 0;
	// try {
	// File[] fileList = file.listFiles();
	// for (int i = 0; i < fileList.length; i++) {
	// if (fileList[i].isDirectory()) {
	// size = size + getFolderSize(fileList[i]);
	//
	// } else {
	// size = size + fileList[i].length();
	//
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return size;
	// }

	private void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			// file.delete();
		}
	}

	private void insertAPF(String aNewAlarm) {
		if (aNewAlarm == null)
			return;

		ArrayList<String> apList = readAPList();

		apList.add(aNewAlarm);
		Collections.sort(apList);

		FileOutputStream fos = null;
		FileLock fl = null;

		try {
			fos = new FileOutputStream(APF, false);

			while (true) {
				try {
					fl = fos.getChannel().tryLock();
					if (fl != null && fl.isValid())
						break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			String apStrList = "";
			for (String s : apList) {
				apStrList += s + "\n";
			}

			PrintStream ps = new PrintStream(fos);
			ps.print(apStrList);
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					if (fl != null && fl.isValid())
						fl.release();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private ArrayList<String> readAPList() {
		ArrayList<String> apList = new ArrayList<String>();

		ShellUtils.execCommand("chmod 777 /data/", true);
		try {
			File file = new File(APF);
			if (!file.getParentFile().exists()) {
				boolean state = file.getParentFile().mkdirs();
				System.out.println("创建成功？" + state);
				file.createNewFile();
			}
			FileInputStream fis = new FileInputStream(file);
			BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
			String strLine = null;
			while ((strLine = bfr.readLine()) != null) {
				apList.add(strLine.trim().replaceAll("\n", ""));
			}
			bfr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return apList;
	}

	private void removeLztdFile() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				File file = new File(APF);
				if (file.exists()) {
					file.delete();
				}
			}
		}).start();
	}

	private void registerLbthdReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("itsync.intent.action.lbthdService");
		mReceiver = new LbthdBroadCastReceiver();
		this.registerReceiver(mReceiver, filter);
//		IntentFilter filter2 = new IntentFilter();
//		filter2.addAction("com.itsync.campus");
//		mItsyncReceiver = new ItsyncCampusReceiver();
//		this.registerReceiver(mItsyncReceiver, filter2);
	}

	public class LbthdBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (null == intent.getStringExtra("lessonName") || null == intent.getStringExtra("teacherName")
					|| null == newTemplate)
				return;
			DisplayObject item = newTemplate.findItem("当前课程");
			if (null != item && item instanceof DataText) {
				((DataText) item).setText(intent.getStringExtra("lessonName"));
			}

			DisplayObject item2 = newTemplate.findItem("任课教师");
			if (null != item2 && item2 instanceof DataText) {
				((DataText) item2).setText(intent.getStringExtra("teacherName"));
			}
		}
	}

//	public class ItsyncCampusReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (null == newTemplate)
//				return;
//			int signTag = intent.getIntExtra("sign", -1);
//			int resetTag = intent.getIntExtra("reset", -1);
//
//			if (signTag != -1) {
//				resetTag = 1;
//			}
//			
//			DisplayObject item = newTemplate.findItem("实到人数");
//			if (null != item && item instanceof DataText && signTag != -1) {
//				System.out.println("收到"+signTag+resetTag);
//				if (signTag == 1) {
//					((DataText) item).setText("34人");
//				} else {
//					((DataText) item).setText("33人");
//				}
//			}
//
//			DisplayObject item2 = newTemplate.findItem("未到人数");
//			if (null != item2 && item2 instanceof DataText && signTag != -1) {
//				if (signTag == 1) {
//					((DataText) item2).setText("1人");
//				} else {
//					((DataText) item2).setText("2人");
//				}
//			}
//
//			if (resetTag == 1) {
//				ResetTimeBack();
//			}
//		}
//	}

	private static boolean isServiceRunning(Context context, String ServiceName) {
		if (TextUtils.isEmpty(ServiceName)) {
			return false;
		}
		ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString().equals(ServiceName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub

		// float x = ev.getRawX();
		// float y = ev.getRawY();
		// if ((x > 1900 && x < 1920) && (y < 1080 && y > 1000)) {
		// ShellUtils.execCommand(
		// "am start -n com.android.launcher3/.Launcher", true);
		// return true;
		// }
		// synchronized (DisplayTemplate._filteName) {
		// if (!(DisplayTemplate._filteName.equals("主页面"))) {
		// mBackThread.resetTime();
		// }
		// }
		ResetTimeBack();
		return super.dispatchTouchEvent(ev);
	}

	private void ResetTimeBack() {
		synchronized (DisplayTemplate._filteName) {
			try {
				if (!(DisplayTemplate._filteName.equals("主页面"))) {
					mBackThread.resetTime();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
