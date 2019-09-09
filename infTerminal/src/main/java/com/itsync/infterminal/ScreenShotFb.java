/**
 * ScreenShotFb.java
 * 版权所有(C) 2014
 * 创建者:cuiran 2014-4-3 下午4:55:23
 */
package com.itsync.infterminal;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.itsync.infterminal.ShellUtils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.graphics.Point;

public class ScreenShotFb {

	final static String FB0FILE1 = "/dev/graphics/fb0";

	static File fbFile;

	static int rotation;
	static int widthPixels;
	static int heightPixels;
	static boolean isFirst = true;

	public static ByteArrayOutputStream shoot(Activity activity) {

		if (isFirst) {
			ShellUtils.execCommand("chmod 777 /dev/graphics/fb0", true);
			isFirst = false;
		}
		Bitmap bitmap = null;
//		bitmap = getScreenShotBitmap();

		View dView = activity.getWindow().getDecorView();//.getRootView();
		dView.setDrawingCacheEnabled(true);
	    dView.buildDrawingCache();
	    bitmap = dView.getDrawingCache();
	    
		if (bitmap == null)
			return null;
//		savePic(bitmap);
		
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int bmpDensity = 1;
		while (w > 640 * bmpDensity || h > 640 * bmpDensity)
			bmpDensity++;

		Matrix matrix = new Matrix();
		matrix.postScale(1.0f / bmpDensity, 1.0f / bmpDensity); // 长和宽放大缩小的比例
		switch (rotation) {
		case Surface.ROTATION_0:
			// matrix.postRotate(0);
			System.out.println("方向" + 0);
			break;
		case Surface.ROTATION_90:
//			matrix.postRotate(270);
			System.out.println("方向" + 90);
			break;
		case Surface.ROTATION_180:
//			matrix.postRotate(180);
			System.out.println("方向" + 180);
			break;
		case Surface.ROTATION_270:
//			matrix.postRotate(90);
			System.out.println("方向" + 270);
			break;
		}

		Bitmap resizedBmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
				true);
//		bitmap.recycle();

		ByteArrayOutputStream retStream = new ByteArrayOutputStream();
		resizedBmp.compress(Bitmap.CompressFormat.JPEG, 50, retStream);
		resizedBmp.recycle();
		System.gc();
		// cv.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		return retStream;
		// return getScreenShotBitmap();
		// try {
		// /************ 创建锁对象 ************/
		// final Object lock = new Object();
		//
		// synchronized (lock) {
		// long start = System.currentTimeMillis();
		// Bitmap bitmap = getScreenShotBitmap();
		// long end = System.currentTimeMillis();
		// return bitmap;
		// // String filePath = "/sdcard/s.png";
		// // String filePath= ConstantValue.ROOT_SDCARD_DIR+"/s.png";
		// // String filePath=
		// //
		// ConstantValue.ROOT_SDCARD_DIR+"/screens/"+System.currentTimeMillis()+".png";
		// // ScreenShotFb.savePic(bitmap, filePath);
		// }
		// } catch (Exception e) {
		// }
		// return null;

	}

	public static Bitmap shootHandWrite(Activity activity, int x, int y,
			int width, int height) {

		Bitmap retValue = null;
		View view = activity.getWindow().getDecorView();
		try {
			if (!view.isDrawingCacheEnabled())
				view.setDrawingCacheEnabled(true);
			view.buildDrawingCache(false);
			retValue = Bitmap.createBitmap(view.getDrawingCache());
			view.setDrawingCacheEnabled(false);

		} catch (Exception e) {
			e.printStackTrace();
			retValue = null;
		}
		view.destroyDrawingCache();
		Bitmap resizedBmp = Bitmap.createBitmap(retValue, x, y, width, height);
		retValue.recycle();
		return resizedBmp;
	}

	 // 保存到sdcard
	 static Date startTime = Calendar.getInstance().getTime();  

	static void savePic(Bitmap b) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "/sdcard/cap/" + fmt.format(startTime) + ".jpg";

		File file = new File(fileName);
		if (!file.exists()) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(fileName);
				if (null != fos) {
					b.compress(Bitmap.CompressFormat.JPEG, 50, fos);
					fos.flush();
					fos.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void init(Activity activity) {

		try {

			WindowManager w = activity.getWindowManager();
			Display d = w.getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			d.getMetrics(metrics);
			// since SDK_INT = 1;
			widthPixels = metrics.widthPixels;
			heightPixels = metrics.heightPixels;
//			System.out.println("000:  widthPixels=" + widthPixels + "    heightPixels=" + heightPixels);
			// includes window decorations (statusbar bar/menu bar)
			if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
				try {
					widthPixels = (Integer) Display.class.getMethod(
							"getRawWidth").invoke(d);
					heightPixels = (Integer) Display.class.getMethod(
							"getRawHeight").invoke(d);
//					System.out.println("111:  widthPixels=" + widthPixels + "    heightPixels=" + heightPixels);
				} catch (Exception ignored) {

				}
			// includes window decorations (statusbar bar/menu bar)
			// if (Build.VERSION.SDK_INT
			// >= 17) try { Point realSize = new
			// Point();
			// Display.class.getMethod("getRealSize",
			// Point.class).invoke(d, realSize);
			// widthPixels = realSize.x; heightPixels =
			// realSize.y; } catch (Exception ignored) {
			// }
			if (Build.VERSION.SDK_INT >= 17)
				try {
					Point realSize = new Point();
					Display.class.getMethod("getRealSize", Point.class).invoke(
							d, realSize);
					widthPixels = realSize.x;
					heightPixels = realSize.y;
//					System.out.println("222:  widthPixels=" + widthPixels + "    heightPixels=" + heightPixels);
				} catch (Exception ignored) {

				}

			rotation = activity.getWindowManager().getDefaultDisplay()
					.getRotation();

			DisplayMetrics dm = new DisplayMetrics();
			Display display = activity.getWindowManager().getDefaultDisplay();
			display.getMetrics(dm);
			widthPixels = dm.widthPixels; // 屏幕宽（像素，如：480px）
			heightPixels = dm.heightPixels; // 屏幕高（像素，如：800p）
			@SuppressWarnings("deprecation")
			int pixelformat = display.getPixelFormat();
			PixelFormat localPixelFormat1 = new PixelFormat();
			PixelFormat.getPixelFormatInfo(pixelformat, localPixelFormat1);
			int deepth = localPixelFormat1.bytesPerPixel;// 位深
			piex = new byte[widthPixels * heightPixels * deepth];// 像素
			colors = new int[widthPixels * heightPixels];
			System.out.println("色深" + localPixelFormat1.bytesPerPixel + "像素"
					+ widthPixels + "   " + heightPixels);

		} catch (Exception e) {
		}
	}

	static DataInputStream dStream = null;
	static byte[] piex = null;
	static int[] colors = null;

	// static int screenWidth;
	// static int screenHeight;

	static int ABGR(byte A, byte R, byte G, byte B) {
		return (int) ((int) (A << 24) & 0xFF000000)
				| ((int) (B << 16) & 0x00FF0000)
				| ((int) (G << 8) & 0x0000FF00) | ((int) (R) & 0xFF);
	}

	static int ARGB(byte A, byte R, byte G, byte B) {
		return (int) ((int) (A << 24) & 0xFF000000)
				| ((int) (R << 16) & 0x00FF0000)
				| ((int) (G << 8) & 0x0000FF00) | ((int) (B) & 0xFF);
	}

	public static synchronized Bitmap getScreenShotBitmap() {
		FileInputStream buf = null;
		try {
			fbFile = new File(FB0FILE1);
			buf = new FileInputStream(fbFile);// 读取文件内容
			dStream = new DataInputStream(buf);
			dStream.readFully(piex);
			dStream.close();
			// 将rgb转为色值
			// for (int i = 0; i < piex.length; i += 2) {
			// colors[i / 2] = (int) 0xff000000
			// + (int) (((piex[i + 1]) << (16)) & 0x00f80000)
			// + (int) (((piex[i + 1]) << 13) & 0x0000e000)
			// + (int) (((piex[i]) << 5) & 0x00001A00)
			// + (int) (((piex[i]) << 3) & 0x000000f8);
			// }

			for (int i = 0; i < colors.length; i++) {
				colors[i] = ABGR(piex[i * 4 + 3], piex[i * 4 + 2],
						piex[i * 4 + 1], piex[i * 4 + 0]);
				// colors[i] = ARGB(piex[i * 4 + 3], piex[i * 4 + 2],
				// piex[i * 4 + 1], piex[i * 4 + 0]);
			}
			// 得到屏幕bitmap
			if (rotation == Surface.ROTATION_90
					|| rotation == Surface.ROTATION_270) {
				return Bitmap.createBitmap(colors, heightPixels, widthPixels,
						Bitmap.Config.RGB_565);
			}
			return Bitmap.createBitmap(colors, widthPixels, heightPixels,
					Bitmap.Config.RGB_565);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (buf != null) {
				try {
					buf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
