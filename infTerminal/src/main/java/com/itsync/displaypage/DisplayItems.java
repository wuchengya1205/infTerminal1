package com.itsync.displaypage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


public class DisplayItems {
	private String _displayObjectName;
	public String getDisplayObjectName() {
		return _displayObjectName;
	}
	
	private ArrayList<DisplayItem> _items;
	public int getItemNum() {
		return _items.size();
	}
	public DisplayItem getItem(int index) {
		return _items.get(index);
	}
	public int findItemIndex(DisplayItem item) {
		return _items.indexOf(item);
	}
	
	public void addHead(DisplayItem item){
		if (item != null) 
			_items.add(0, item);
	}
	public DisplayItem removeTail() {
		if (_items.size() > 0) {
			return	_items.remove(_items.size()-1);
		}else {
		    return null;	
		}
		 
	}
	
	public void addTail(DisplayItem item){
		if (item != null) 
			_items.add(item);
	}
	
	public DisplayItem removeHead(){
		if (_items.size() > 0)
			return _items.remove(0);
		else 
			return null;
	}
	public DisplayItem getTail() {
		if (_items.size()>0) {
			return _items.get(_items.size()-1);
		}
		return null;
	}
	
	public void removeDisplayItem(UUID deliverItemID) {
		for (int i = _items.size() - 1; i >= 0; i--) {
			DisplayItem item = _items.get(i);
			if (item.isDeliverItem(deliverItemID))
				_items.remove(i);
		}
	}

	public DisplayItems() {
		this._displayObjectName = "";
		this._items = new ArrayList<DisplayItem>();			
	}
	
	
	static public DisplayItems load(String basePath, XmlPullParser xpp) throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;
		
		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
		 || !xpp.getName().equalsIgnoreCase("DisplayItems"))
			return null;
		
		DisplayItems retValue = new DisplayItems();
		retValue._displayObjectName = xpp.getAttributeValue(null, "DisplayObjectName");
		
		while (true){
			eventType = xpp.next();

			if (eventType == XmlPullParser.START_TAG) {
				DisplayItem item = DisplayItem.load(basePath, xpp);
				if (null != item)
					retValue._items.add(item);
				
				
				while (true){
					eventType = xpp.next();
					if (eventType == XmlPullParser.END_TAG || eventType == XmlPullParser.END_DOCUMENT) {
						break;
					}
				}

				
			} else if (eventType == XmlPullParser.END_TAG || eventType == XmlPullParser.END_DOCUMENT) {
				break;
			}
		}
				
		return retValue;
	}
}
