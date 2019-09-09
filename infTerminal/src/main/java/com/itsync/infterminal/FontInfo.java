package com.itsync.infterminal;

import java.io.File;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Typeface;

public class FontInfo {
	public String FontName;
	public String FileName;

	protected Typeface _typeface;
	public synchronized Typeface GetTypeface()
	{
		if (_typeface == null && new File(this.FileName).exists()){
			_typeface = Typeface.createFromFile(this.FileName);
		}
		return _typeface;
	}		
	
	static public FontInfo load(String basePath, XmlPullParser xpp) throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;
		
		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
		 || !xpp.getName().equalsIgnoreCase("FontInfo"))
			return null;
		
		FontInfo retValue = new FontInfo();
			retValue.FontName = xpp.getAttributeValue(null, "FontName");
			retValue.FileName = xpp.getAttributeValue(null, "FileName");
			
		// 将 相对路径 修改为 绝对路径
		if (retValue.FileName != null && !retValue.FileName.isEmpty())
			retValue.FileName = basePath + retValue.FileName;
		
		return retValue;
	}
}
