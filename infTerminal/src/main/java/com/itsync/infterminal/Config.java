package com.itsync.infterminal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Environment;

public class Config {
	public String ServerIP;
	public String TerminalName;
	public String UpdateIP;
	public boolean IsAnima;
	public boolean IsAutoUpdate;
	public String tempString;
	public String IP;
	public boolean isActive;
	public String LastTemplate;
	public boolean openWatchDog;
	public boolean fastCapScreen;
	public String mCurrentArea;

	public String action;
	public String packageName;
	public String className;
	public String[] dataTagList = { "学生课表", "教室课表", "作息时间表", "校历表" };

	public Config() {
		this.ServerIP = "192.168.1.100";
		this.TerminalName = "";
		this.UpdateIP = "192.168.1.93";
		this.IsAnima = true;
		this.IsAutoUpdate = true;
		this.isActive = true;
		this.LastTemplate = "";
		this.openWatchDog = false;
		this.mCurrentArea = "";
		this.fastCapScreen = true;
	}

	private final static String c_configFileName = Environment.getExternalStorageDirectory()
			+ "/MagicInfData/config.txt";

	private final static String c_ServerIP_Tag = "ServerIp:";
	private final static String c_TerminalName_Tag = "TerminalName:";
	private final static String c_UpdateIP_Tag = "UpdateIp:";
	private final static String c_IsAnima_Tag = "IsAnima:";
	private final static String c_IsAutoUpdate_Tag = "IsAutoUpdate:";
	private final static String c_IP = "IP:";
	private final static String c_isActive = "isActive:";
	private final static String c_lastTemplate = "LastTemplateName:";
	private final static String c_openWatchDog = "OpenWatchDog:";
	private final static String c_current_aera = "CurrentAera:";
	private final static String c_fastCapScreen = "FastCapScreen:";

	private final static String ACTION = "ACTION:";
	private final static String PACKAGE = "PACKAGE:";
	private final static String CLASS = "CLASS:";
	private final static String DATA_TAG = "DATA_TAG:";

	private static final String PATH = Environment.getExternalStorageDirectory()
			+ "/MagicInfData/ItsyncCampus/campusConfig.txt";

	public static synchronized Config load() {

		Config retValue = new Config();

		boolean readOK = false;
		File file = new File(c_configFileName);
//		System.out.println("Loading " + c_configFileName);
		if (file.exists()) {
			FileInputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
				String str = null;
				while ((str = reader.readLine()) != null) {
					if (str.startsWith(c_ServerIP_Tag))
						retValue.ServerIP = str.substring(c_ServerIP_Tag.length());
					else if (str.startsWith(c_TerminalName_Tag))
						retValue.TerminalName = str.substring(c_TerminalName_Tag.length());
					else if (str.startsWith(c_UpdateIP_Tag))
						retValue.UpdateIP = str.substring(c_UpdateIP_Tag.length());
					else if (str.startsWith(c_IP))
						retValue.IP = str.substring(c_IP.length());
					else if (str.startsWith(c_IsAnima_Tag))
						retValue.IsAnima = !str.substring(c_IsAnima_Tag.length()).equals("0");
					else if (str.startsWith(c_IsAutoUpdate_Tag))
						retValue.IsAutoUpdate = !str.substring(c_IsAutoUpdate_Tag.length()).equals("0");
					else if (str.startsWith(c_isActive))
						retValue.isActive = !str.substring(c_isActive.length()).equals("0");
					else if (str.startsWith(c_lastTemplate))
						retValue.LastTemplate = str.substring(c_lastTemplate.length());
					else if (str.startsWith(c_openWatchDog))
						retValue.openWatchDog = !str.substring(c_openWatchDog.length()).equals("0");
					else if (str.startsWith(c_current_aera))
						retValue.mCurrentArea = str.substring(c_current_aera.length());
					else if (str.startsWith(c_fastCapScreen))
						retValue.fastCapScreen = !str.substring(c_fastCapScreen.length()).equals("0");

				}
				fileInputStream.close();
				readOK = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File campusFile = new File(PATH);
		if (campusFile.exists()) {
			FileInputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(campusFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
				String str = null;
				while ((str = reader.readLine()) != null) {
					if (str.startsWith(ACTION))
						retValue.action = str.substring(ACTION.length());
					else if (str.startsWith(PACKAGE))
						retValue.packageName = str.substring(PACKAGE.length());
					else if (str.startsWith(CLASS))
						retValue.className = str.substring(CLASS.length());
					else if (str.startsWith(DATA_TAG))
						retValue.dataTagList = str.substring(DATA_TAG.length()).split(";");
				}
				fileInputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!readOK)
			retValue.save();
		return retValue;

	}

	public boolean save() {
		File file = new File(c_configFileName);

		File parentDir = file.getParentFile();
		if (!parentDir.exists()) {
			try {
				// 按照指定的路径创建文件夹
				parentDir.mkdirs();
			} catch (Exception e) {
			}
		}

		try {
			FileOutputStream fOutputStream = new FileOutputStream(file);
			fOutputStream.write(String.format("%s%s\n", c_ServerIP_Tag, this.ServerIP).getBytes());
			fOutputStream.write(String.format("%s%s\n", c_UpdateIP_Tag, "http://" + this.IP + ":8080/config/config.xml")
					.getBytes());
			fOutputStream.write(String.format("%s%s\n", c_TerminalName_Tag, this.TerminalName).getBytes());
			fOutputStream.write(String.format("%s%d\n", c_IsAnima_Tag, this.IsAnima ? 1 : 0).getBytes());
			fOutputStream.write(String.format("%s%d\n", c_IsAutoUpdate_Tag, this.IsAutoUpdate ? 1 : 0).getBytes());
			fOutputStream.write(String.format("%s%s\n", c_IP, this.IP).getBytes());
			fOutputStream.write(String.format("%s%d\n", c_isActive, this.isActive ? 1 : 0).getBytes());
			fOutputStream.write(String.format("%s%s\n", c_lastTemplate, this.LastTemplate).getBytes());
			fOutputStream.write(String.format("%s%d\n", c_openWatchDog, this.openWatchDog ? 1 : 0).getBytes());
			fOutputStream.write(String.format("%s%s\n", c_current_aera, this.mCurrentArea).getBytes());
			fOutputStream.write(String.format("%s%d\n", c_fastCapScreen, this.fastCapScreen ? 1 : 0).getBytes());
			fOutputStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
