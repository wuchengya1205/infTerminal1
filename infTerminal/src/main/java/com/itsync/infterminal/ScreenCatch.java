package com.itsync.infterminal;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.View;

import com.itsync.displayobject.VideoList;

public class ScreenCatch {

	static VideoList _mVideoList = new VideoList();;
	static Canvas cv;

	//
	// public ScreenCatch() {
	//
	// _mVideoList = new VideoList();
	// cv = new Canvas();
	// }

	// public static void shoot(Activity activity, final File filePath) {
	// if (filePath == null) {
	// return;
	// }
	// if (!filePath.getParentFile().exists()) {
	// filePath.getParentFile().mkdirs();
	// }
	// FileOutputStream fos = null;
	// try {
	// fos = new FileOutputStream(filePath);
	// if (null != fos) {
	// takeScreenShot(activity).compress(Bitmap.CompressFormat.PNG, 100, fos);
	// }
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } finally {
	// if (fos != null) {
	// try {
	// fos.flush();
	// fos.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	static class CapEvent {
		public boolean IsFinished;
		public Bitmap ScreenBitmap;

		public CapEvent() {
			this.IsFinished = false;
			this.ScreenBitmap = null;
		}
	}

	public static ByteArrayOutputStream shoot(final Activity activity,
			final View view) {
		System.out.println("shooting....................");
		if (null == view)
			return null;

		final int c_outSize = 640;
		final CapEvent capEvent = new CapEvent();
		view.post(new Runnable() { // 更新UI，需要在 UI 线程中处理
			@Override
			public void run() {
				capEvent.ScreenBitmap = takeScreenShot(view);
				// cv.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
				// bitmap.recycle();
				// capEvent.ScreenBitmap = getScreenShotBitmap();
				// capEvent.ScreenBitmap = ini();
				capEvent.IsFinished = true;
			}
		});

		while (!capEvent.IsFinished) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (capEvent.ScreenBitmap != null) {
			System.out.println("shooting.......111111111111111111111");

			int w = capEvent.ScreenBitmap.getWidth();
			int h = capEvent.ScreenBitmap.getHeight();
			int bmpDensity = 1;
			while (w > c_outSize * bmpDensity || h > c_outSize * bmpDensity)
				bmpDensity++;

			Matrix matrix = new Matrix();
			matrix.postScale(1.0f / bmpDensity, 1.0f / bmpDensity); // 长和宽放大缩小的比例
			// matrix.postRotate(90);
			Bitmap resizedBmp = Bitmap.createBitmap(capEvent.ScreenBitmap, 0,
					0, w, h, matrix, true);
			capEvent.ScreenBitmap.recycle();

			ByteArrayOutputStream retStream = new ByteArrayOutputStream();
			resizedBmp.compress(Bitmap.CompressFormat.JPEG, 30, retStream);
			//释放
			resizedBmp.recycle();
			//回收
			System.gc();
			// cv.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			return retStream;
		}
		System.out.println("shooting...NNNNNNNNNNNNNNNNNNN");
		return null;
	}

	public static Bitmap takeScreenShot(View view) {
		Bitmap retValue = null;
		try {
			if (!view.isDrawingCacheEnabled())
				view.setDrawingCacheEnabled(true);
			view.buildDrawingCache(false);
			retValue = Bitmap.createBitmap(view.getDrawingCache());
			view.setDrawingCacheEnabled(false);
			cv = new Canvas(retValue);
			// Canvas cv = new Canvas(retValue);
			// cv.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			cv.setBitmap(retValue);
			// _mVideoList.VideoViewImage();
			Bitmap tempBitmap = _mVideoList.VideoViewImage();
			if (tempBitmap != null) {
				cv.drawBitmap(tempBitmap, null,
						new Rect(VideoList._padLeft, VideoList._padTop,
								VideoList._padLeft + tempBitmap.getWidth(),
								VideoList._padTop + tempBitmap.getHeight()),
						null);
				tempBitmap.recycle();
			}

		} catch (Exception e) {
			e.printStackTrace();
			retValue = null;
		}
		view.destroyDrawingCache();
		return retValue;
	}

}
