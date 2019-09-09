package com.itsync.infterminal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.itsync.infterminal.ShellUtils;
import com.itsync.infterminal.ShellUtils.CommandResult;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateManager {
	private String UpdateIP;
	public static String url;
	PackageManager pm;

	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 下载出错 */
	private static final int DOWNLOAD_ERROR = 3;
	/* 区分警告类型 1为服务器地址出错 2为已是最新版本 */
	private int WARNING;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case DOWNLOAD:

				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:

				installApk();
				// installAndStartApk(mContext,mSavePath +"/"+
				// mHashMap.get("name"));
				break;
			case DOWNLOAD_ERROR:

				Toast.makeText(mContext, "没有找到服务器中的新版本文件，请检查文件是否存在",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context, String IP,
			PackageManager packageManager) {
		this.mContext = context;
		this.UpdateIP = IP;
		this.pm = packageManager;
	}

	public void checkUpdate() {
		if (isUpdate()) {

			showNoticeDialog();
		} else {
			if (WARNING == 1) {
				Toast.makeText(mContext, "软件更新服务器地址错误，请在config程序中检查配置是否正确",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(mContext, "已经是最新版本了", Toast.LENGTH_LONG).show();
			}

		}
	}

	
	private boolean isNetworkConnected() { 
		if (mContext != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext 
		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null) { 
		return mNetworkInfo.isAvailable(); 
		} 
		} 
		return false; 
	}
	
	private boolean isUpdate() {
		long startWaitTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startWaitTime < 30000 && !isNetworkConnected()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		int versionCode = getVersionCode(mContext);
		float serviceCode = 0;
		if (!(UpdateManager.getXML(UpdateIP) == null)) {
			WARNING = 2;
			InputStream inStream = UpdateManager.getXML(UpdateIP);

			ParseXmlService service = new ParseXmlService();

			try {
				mHashMap = service.parseXml(inStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null != mHashMap) {
				serviceCode = Float.valueOf(mHashMap.get("version"));
				if (serviceCode > versionCode) {
					return true;
				}
			}
		} else {

			WARNING = 1;
		}
		return false;
	}

	public static InputStream getXML(String path) {
		try {
			URL url = new URL(path);
			if (url != null) {
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.setConnectTimeout(3000);
				connection.setRequestMethod("GET");
				int requesetCode = connection.getResponseCode();
				if (requesetCode == 200) {

					return connection.getInputStream();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {

			versionCode = context.getPackageManager().getPackageInfo(
					"com.itsync.infterminal", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	private void showNoticeDialog() {
		showDownloadDialog();
		// AlertDialog.Builder builder = new Builder(mContext);
		// builder.setTitle("软件更新");
		// builder.setMessage("检测到新版本，现在更新？");
		//
		// builder.setPositiveButton("好的，更新", new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// showDownloadDialog();
		// }
		// });
		//
		// builder.setNegativeButton("一会儿再说", new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		// Dialog noticeDialog = builder.create();
		// noticeDialog.show();
	}

	@SuppressLint("InflateParams")
	private void showDownloadDialog() {

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在更新中loading....");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		builder.setNegativeButton("取消更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		downloadApk();
	}

	private void downloadApk() {

		new downloadApkThread().start();
	}

	private class downloadApkThread extends Thread {
		InputStream is;
		boolean work = true;

		@Override
		public void run() {
			try {

				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {

					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "Download";
					URL url = new URL(mHashMap.get("url"));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					int length = conn.getContentLength();

					// if (length <= 0) {
					// mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
					//
					// }
					try {
						is = conn.getInputStream();
					} catch (FileNotFoundException e) {

						mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
						work = false;
					}

					File file = new File(mSavePath);
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					if (work) {

						do {
							int numread = is.read(buf);
							count += numread;
							progress = (int) (((float) count / length) * 100);

							mHandler.sendEmptyMessage(DOWNLOAD);
							if (numread <= 0) {

								mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
								break;
							}

							fos.write(buf, 0, numread);
						} while (!cancelUpdate);
						fos.close();
						is.close();
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				FileUtils.addTxtToFileBuffered("Itsync异常" + e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				FileUtils.addTxtToFileBuffered("Itsync异常" + e.toString());
			}

			mDownloadDialog.dismiss();
		}
	};

	private void installApk() {

		// PackageInstaller piInstaller = new PackageInstaller(mContext);
		// piInstaller.instatnewapk(new File(mSavePath,
		// mHashMap.get("name")),"com.itsync.infterminal");
//		FileUtils.addTxtToFileBuffered("Itsync断点0");
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
//		FileUtils.addTxtToFileBuffered("Itsync断点1");
		// String cmd = "pm install -r /mnt/sdcard/test.apk";
		// String cmd = "pm install -r " + "/sdcard/Download/InfTerminal.apk";
		String cm = "am force-stop com.itsync.infterminal";
		String cmd = "pm install -r " + mSavePath + "/" + mHashMap.get("name");
		String cmd2 = "am start -n" + " " + "com.itsync.infterminal" + "/"
				+ "com.itsync.infterminal.MainActivity";
//		String cmd3 = "sleep 5";
		String[] strings = new String[]{cm,cmd,cmd2};
//		ShellUtils.execCommand(cm, true);
//		ShellUtils.execCommand(cmd, true);
//		ShellUtils.execCommand(cmd3, true);
//		ShellUtils.execCommand(cmd2, true);
		ShellUtils.execCommand(strings, true);
		// FileUtils.addTxtToFileBuffered("等待失败"+ShellUtils.execCommand(cmd3,
		// true).errorMsg);
		// FileUtils.addTxtToFileBuffered("启动失败"+ShellUtils.execCommand(cmd2,
		// true).errorMsg);

		// CommandResult commandResult = ShellUtils.execCommand(new
		// String[]{cmd,"sleep 5",cmd2}, true);
		// //
		// System.out.println("当前执行成功"+commandResult.successMsg+
		// "当前执行失败"+commandResult.errorMsg);
		// ShellUtils.execCommand("sleep 10", true);
		// CommandResult commandResult2 = ShellUtils.execCommand(cmd2, true);
		// CommandResult commandResult2 = ShellUtils.execCommand(cmd2,true);
		// System.out.println("当前执行成功2"+commandResult2.successMsg+
		// "当前执行失败2"+commandResult2.errorMsg);
		// android.os.Process.killProcess(android.os.Process.myPid());
		// ShellUtils.execCommand(new
		// String[]{cmd,"\n","sleep 5\n",cmd2,"\n","exit\n"}, false);
		Process process = null;
		DataOutputStream os = null;
		// DataOutputStream os1 = null;
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder successMsg = null;
		StringBuilder errorMsg = null;

		// try {
		//
		// // boolean isSuccess = false;
		// process = Runtime.getRuntime().exec("su");
		// os = new DataOutputStream(process.getOutputStream());
		// os.write(cmd.getBytes());
		// os.writeBytes("\n");
		//
		// os.writeBytes("sleep 5\n");
		//
		// os.write(cmd2.getBytes());
		// os.writeBytes("\n");
		// // os.writeBytes("reboot\n");
		// os.writeBytes("exit\n");
		// os.flush(); // 提交命令
		// os.close(); // 关闭流操作
		// process.getOutputStream().close();
		// // android.os.Process.killProcess(android.os.Process.myPid());
		// // 获取返回结果
		// successMsg = new StringBuilder();
		// errorMsg = new StringBuilder();
		// successResult = new BufferedReader(new InputStreamReader(
		// process.getInputStream()));
		// errorResult = new BufferedReader(new InputStreamReader(
		// process.getErrorStream()));
		// String s;
		//
		// while ((s = successResult.readLine()) != null) {
		// successMsg.append(s);
		// }
		// while ((s = errorResult.readLine()) != null) {
		// errorMsg.append(s);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// if (os != null) {
		// os.close();
		// }
		// if (process != null) {
		// process.destroy();
		// }
		// if (successResult != null) {
		// successResult.close();
		// }
		// if (errorResult != null) {
		// errorResult.close();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

	}

}
