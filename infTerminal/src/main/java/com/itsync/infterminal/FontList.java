package com.itsync.infterminal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.os.Environment;

@SuppressWarnings("serial")
public class FontList extends ArrayList<FontInfo> {
	private FontList() {
		_initialized = false;
	}
	
	private static FontList s_singleInstance = new FontList();
	public static FontList GetInstance() {
		synchronized (s_singleInstance) {
			if (!s_singleInstance._initialized)
				s_singleInstance.load(Environment.getExternalStorageDirectory() + "/MagicInfData/Fonts/FontInfo.xml");
		}
		return s_singleInstance;
	}
	
	private boolean _initialized;
	private void load(String xmlFileName) {
		File xmlFile = new File(xmlFileName);
		if (!xmlFile.exists())
			return;
		
		String basePath = xmlFile.getParent() + "/";
		
		FileInputStream IS = null;
		try {
			IS = new FileInputStream(xmlFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}

		_initialized = true;
		this.clear();
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(IS, "gb2312");
			
			int eventType = xpp.getEventType();
			
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {
					FontInfo item = FontInfo.load(basePath, xpp);
					if (item != null) 
						add(item);
				}

				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (IS != null) {
			try {
				IS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			IS = null;
		}
	}
	
}
