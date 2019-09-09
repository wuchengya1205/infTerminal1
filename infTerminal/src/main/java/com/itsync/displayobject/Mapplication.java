package com.itsync.displayobject;

import android.app.Application;

public class Mapplication extends Application {
	// private Context context;

	@Override
	public void onCreate() {
//		Thread.currentThread().setUncaughtExceptionHandler(
//				new MyExceptionHander());
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		// context = getApplicationContext();
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				Mapplication.this)
//				.memoryCacheExtraOptions(3000, 3000)
//				// max width, max height，即保存的每个缓存文件的最大长宽
//				.threadPoolSize(1)
//				// 线程池内加载的数量
//				.threadPriority(Thread.NORM_PRIORITY - 2)
//				.denyCacheImageMultipleSizesInMemory()
//				.memoryCache(
//						new UsingFreqLimitedMemoryCache(2 * 102400 * 102400))
//				// You can pass your own memory cache
//				// implementation/你可以通过自己的内存缓存实现
//				.memoryCacheSize(2 * 1024 * 1024)
//				.discCacheSize(50 * 1024 * 1024)
//				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.discCacheFileCount(100)
//				// 缓存的文件数量
//				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//				.writeDebugLogs() // Remove for release app
//				.build();// 开始构建
//
//		ImageLoader.getInstance().init(config);
	}

//	private class MyExceptionHander implements UncaughtExceptionHandler {
//		@Override
//		public void uncaughtException(Thread thread, Throwable ex) {
//
//			
//			Process process = null;
//			DataOutputStream os = null;
//			String command1 = "am broadcast -a android.intent.action.BOOT_COMPLETED -n com.itsync.infterminal/.BootBroadcastReceiver";
//			String command2 = "am broadcast -a itsync.intent.action.reload -n com.itsync.infterminal/.BootBroadcastReceiver";
//			try {
//
//				process = Runtime.getRuntime().exec("su");
//				os = new DataOutputStream(process.getOutputStream());
//				os.write(command2.getBytes());
//				os.writeBytes("\n");
//				os.writeBytes("sleep 5\n");
//				os.write(command1.getBytes());
//				os.writeBytes("\n");
//
//				os.writeBytes("exit\n");
//				os.flush();
//				os.close();
//				process.getOutputStream().close();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			try {
//
//				StringWriter wr = new StringWriter();
//				PrintWriter pw = new PrintWriter(wr);
//				ex.printStackTrace(pw);
//				File file = new File(Environment.getExternalStorageDirectory(),
//						"error.log");
//				FileOutputStream fos = new FileOutputStream(file);
//				fos.write(wr.toString().getBytes());
//				fos.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			android.os.Process.killProcess(android.os.Process.myPid());
//		}
//	}


}
