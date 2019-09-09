package com.itsync.displaypage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class DisplayPage {
	private String _name;
	public String getName() {
		return _name;
	}
	
	private String _staticOriginalFolderNameString;
	public String  getstaticOriginalFileNameString() {
		return _staticOriginalFolderNameString;
	}

	private String _templateFileName;
	public String getTemplateFileName() {
		return _templateFileName;
	}
	
	private ArrayList<DisplayItems> _items;
	public int getItemNum() {
		return _items.size();
	}
	public DisplayItems getItem(int index) {
		return _items.get(index);
	}
	
	public DisplayPage() {
		this._name = "";
		this._templateFileName = "";
		this._items = new ArrayList<DisplayItems>();
	}

	
	static public DisplayPage load(String xmlFileName) {
		File xmlFile = new File(xmlFileName);
		if (!xmlFile.exists())
			return null;
		
		String basePath = xmlFile.getParentFile().getParent() + "/";
		
		FileInputStream IS = null;
		try {
			IS = new FileInputStream(xmlFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}

		DisplayPage retValue = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(IS, "utf-8");
			
			int eventType = xpp.getEventType();
			
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {
					if (xpp.getName().equalsIgnoreCase("DisplayPage")) {
						retValue = new DisplayPage();						
						retValue._name = xpp.getAttributeValue(null, "Name");
						String templateName = xpp.getAttributeValue(null, "TemplateName");
						if (!templateName.isEmpty())
							retValue._templateFileName = basePath + "Templates/" + templateName + "/OpenShow.xml";
						    retValue._staticOriginalFolderNameString = basePath + "Templates/" + templateName + "/";
					} else if (null != retValue) {
						DisplayItems item = DisplayItems.load(basePath, xpp);
						if (item != null) 
							retValue._items.add(item);
					}
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
		return retValue;
	}
}
