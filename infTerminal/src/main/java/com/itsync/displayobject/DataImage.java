package com.itsync.displayobject;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.itsync.infterminal.CommenConfigs;
//import com.itsync.aidl.IService;
import com.itsync.infterminal.MainActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.ViewGroup;

public class DataImage extends DisplayObject {

	public double left;
	public double top;
	public double width;
	public double height;
	public String _name;
	// private IService iService;
	public static boolean _isStartService;
	private MainActivity mainActivity;
	private Intent intent;
	public String _action;
	public String _package;
	public String _class;
	public String[] _dataTags;

	public DataImage() {

		_isStartService = false;
		// this.left = _left;
		// this.top = _top;
		// this.width = _width;
		// this.height = _height;
	}

	@Override
	public void initialize(Context ctrlContext, Point screenSize, ViewGroup rootLayout) {
		mainActivity = (MainActivity) ctrlContext;
		intent = new Intent();
		// this._screenSize = screenSize;
		left = screenSize.x * _left;
		top = screenSize.y * _top;
		width = screenSize.x * _width;
		height = screenSize.y * _height;

		_action = CommenConfigs.ACTION;
		_package = CommenConfigs.PACKAGE;
		_class = CommenConfigs.CLASS;
		_dataTags = CommenConfigs.DATA_TAGS;
		if (null == _action || null == _package || null == _class || null == _dataTags)
			return;
		for (String dataTag : _dataTags) {
			if (_name.contains(dataTag)) {
				mainActivity.enableReadCard();
				intent.setAction(_action);
				intent.setComponent(new ComponentName(_package, _class));
				intent.putExtra("Tag", dataTag);
				intent.putExtra("padLeft", left);
				intent.putExtra("padTop", top);
				intent.putExtra("W_width", width);
				intent.putExtra("W_height", height);
				ctrlContext.startService(intent);
				break;
			}
		}

//		if (_name.contains("学生课表")) {
//			mainActivity.enableReadCard();
//			intent.setAction(_action);
//			intent.setComponent(new ComponentName(_package, _class));
//			intent.putExtra("Tag", "学生课表");
//			intent.putExtra("padLeft", left);
//			intent.putExtra("padTop", top);
//			intent.putExtra("W_width", width);
//			intent.putExtra("W_height", height);
//			ctrlContext.startService(intent);
//		} else if (_name.contains("教室课表")) {
//			intent.setAction(_action);
//			intent.setComponent(new ComponentName(_package, _class));
//			intent.putExtra("Tag", "教室课表");
//			intent.putExtra("padLeft", left);
//			intent.putExtra("padTop", top);
//			intent.putExtra("W_width", width);
//			intent.putExtra("W_height", height);
//			ctrlContext.startService(intent);
//		} else if (_name.contains("作息时间表")) {
//			intent.setAction(_action);
//			intent.setComponent(new ComponentName(_package, _class));
//			intent.putExtra("Tag", "作息时间表");
//			intent.putExtra("padLeft", left);
//			intent.putExtra("padTop", top);
//			intent.putExtra("W_width", width);
//			intent.putExtra("W_height", height);
//			ctrlContext.startService(intent);
//		} else if (_name.contains("校历表")) {
//			intent.setAction(_action);
//			intent.setComponent(new ComponentName(_package, _class));
//			intent.putExtra("Tag", "校历表");
//			intent.putExtra("padLeft", left);
//			intent.putExtra("padTop", top);
//			intent.putExtra("W_width", width);
//			intent.putExtra("W_height", height);
//			ctrlContext.startService(intent);
//		} else if (_name.contains("考勤区域")) {
//			mainActivity.enableReadCard();
//			intent.setAction(_action);
//			intent.setComponent(new ComponentName(_package, _class));
//			intent.putExtra("Tag", "考勤区域");
//			intent.putExtra("padLeft", left);
//			intent.putExtra("padTop", top);
//			intent.putExtra("W_width", width);
//			intent.putExtra("W_height", height);
//			ctrlContext.startService(intent);
//		}
		// else if (_name.contains("教室课表展示")) {
		// intent.setAction("com.itsync.campusDemo");
		// intent.setComponent(new ComponentName("com.itsync.CoreService",
		// "com.itsync.CoreService.CampusService"));
		// intent.putExtra("Tag", "教室课表展示");
		// intent.putExtra("padLeft", left);
		// intent.putExtra("padTop", top);
		// intent.putExtra("W_width", width);
		// intent.putExtra("W_height", height);
		// ctrlContext.startService(intent);
		// } else if (_name.contains("学生课表展示")) {
		// mainActivity._canReadCard = true;
		// intent.setAction("com.itsync.campusDemo");
		// intent.setComponent(new ComponentName("com.itsync.CoreService",
		// "com.itsync.CoreService.CampusService"));
		// intent.putExtra("Tag", "学生课表展示");
		// intent.putExtra("padLeft", left);
		// intent.putExtra("padTop", top);
		// intent.putExtra("W_width", width);
		// intent.putExtra("W_height", height);
		// ctrlContext.startService(intent);
		// }
//		else {
//			mainActivity._canReadCard = false;
//		}
		// if (_name.equals("学生课表")) {
		// intent = new Intent();
		// intent.setAction("com.itsync.mService");
		// intent.setComponent(new ComponentName("com.itsync.cardService",
		// "com.itsync.cardService.mService"));
		// _mContext. bindService(intent,connection, Context.BIND_AUTO_CREATE);
		// _isStartService = true;
		//
		// }
	}

	@Override
	public void uninitialize() {
		// if (_isStartService) {
		// _mContext.unbindService(connection);
		// }
		// _mContext.stopService(intent);
		_isStartService = false;
		_name = null;
		// _name = "";
		left = 0;
		top = 0;
		width = 0;
		height = 0;

	}

	// ServiceConnection connection = new ServiceConnection() {
	//
	// @Override
	// public void onServiceDisconnected(ComponentName name) {
	// // iService = null;
	// }
	//
	// // @Override
	// // public void onServiceConnected(ComponentName name, IBinder service) {
	// // iService = IService.Stub.asInterface(service);
	// // if (iService == null) {
	// // } else {
	// // try {
	// //
	// // String value = iService.SerData2(_screenSize.y * top,_screenSize.x *
	// left,_screenSize.x * width,_screenSize.y * height);
	// // Toast.makeText(_mContext, value, 1).show();
	// // } catch (RemoteException e) {
	// // e.printStackTrace();
	// // }
	// // }
	// // }
	// };

	public static DisplayObject Load(String basePath, XmlPullParser xpp) throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;
		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG || !xpp.getName().equalsIgnoreCase("data_image_object"))
			return null;

		DataImage retValue = new DataImage();
		retValue.LoadBaseInfo(xpp);

		retValue._name = xpp.getAttributeValue(null, "Name");
		return retValue;
	}

	@Override
	public void stopWork() {
	}

}
