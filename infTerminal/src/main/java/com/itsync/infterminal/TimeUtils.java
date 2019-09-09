package com.itsync.infterminal;

import android.annotation.SuppressLint;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	@SuppressLint("SimpleDateFormat")
	public static String getDetailTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH-mm-ss");
		Date date = new Date(System.currentTimeMillis());
		return simpleDateFormat.format(date);
	}
}
