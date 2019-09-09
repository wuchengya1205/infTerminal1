package com.itsync.infterminal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import android.os.Environment;

public class FileUtils {

	private final static String FILE_PATH = Environment
			.getExternalStorageDirectory() + "/ItsyncLog/log.txt";
	private final static String PATH = Environment
			.getExternalStorageDirectory() + "/ItsyncLog";

	public static void addTxtToFileBuffered(String content) {
		BufferedWriter out = null;
		content =content+ "         当前时间:"+TimeUtils.getDetailTime();
		try {
			File file = new File(PATH);
			if (!file.exists()) {
				file.mkdirs();
			}
			File file2 = new File(FILE_PATH);
			if (!file2.exists()) {
				file2.createNewFile();
			}
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file2, true)));
			out.newLine();// 换行
			out.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
