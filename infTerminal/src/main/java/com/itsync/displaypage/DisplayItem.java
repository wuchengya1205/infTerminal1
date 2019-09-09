package com.itsync.displaypage;

import java.io.IOException;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.itsync.infbase.DeliverItem;

public class DisplayItem {
	static public final int DataType_Unknown = 0;
	static public final int DataType_Image = 1;      // 图片
	static public final int DataType_Video = 2;      // 视频
	static public final int DataType_Text = 3;       // 文字
	static public final int DataType_PlayVideo = 4;  // 直播   添加直播视频的功能
	static public final int DataType_WebPage = 5;    // 网页
	static public final int DataType_CiteVideo = 6;  // 视频引用
	private int _dataType;
	
	String _url;
	public String getUrl(){
		return _url;
	}
	public int getDataType() {
		return _dataType;
	}
	
	private String _text;
	public String getText() {
		return _text;
	}

	private String _fileName;
	 
	public String getFileName() {
		return _fileName;
	}
	private int _duration;
	public int getDuration() {
		
		return _duration;
		
	}

	private String[] _fileList; 
	public String[] getFileList() {
		return _fileList;
	}

	private int _showSeconds;
	public int getShowSeconds() {
		return _showSeconds;
	}

	private boolean _isCrawlImage;
	public boolean isCrawlImage() {
		return _isCrawlImage;
	}

	private int _crawlSpeed;
	public int getCrawlSpeed() {
		return _crawlSpeed;
	}

	private int _effectID;
	public int getEffectID() {
		return _effectID;
	}

	private int _effectDuration;
	public int getEffectDuration() {
		return _effectDuration;
	}

	private int _markIn;
	public int getMarkIn() {
		return _markIn;
	}
	
	private UUID _deliverItemID;
	public Boolean isDeliverItem(UUID itemID) {
		if (null == _deliverItemID)
			return false;
		
		return _deliverItemID.equals(itemID);
	}

	public DisplayItem() {
		this._dataType = DataType_Unknown;
		this._text = "";
		this._fileName = "";
		this._fileList = new String[0];
		this._showSeconds = 5;
		this._isCrawlImage = false;
		this._crawlSpeed = 5;
		this._effectID = 0;
		this._effectDuration = 0;
		this._markIn = 0;
		this._deliverItemID = null;
	}
	
	public DisplayItem (DeliverItem deliveritem){
		if (deliveritem.FileName == null || deliveritem.FileName.isEmpty()) {
			// 文字
			this._dataType = DataType_Text;
			this._text = deliveritem.Text;
			this._fileList = new String[0];
			this._fileName = "";
			this._deliverItemID = deliveritem.ItemGuid;
		} else {
			// 图片
			this._dataType = DataType_Image;
			this._text = deliveritem.Text;
			this._fileName = deliveritem.FileName;
			this._fileList = new String[1];
			this._fileList[0] = deliveritem.FileName;
			this._deliverItemID = deliveritem.ItemGuid;
		}
		
		this._showSeconds = 5;
		this._isCrawlImage = false;
		this._crawlSpeed = 5;
		this._effectID = 0;
		this._effectDuration = 0;
		this._markIn = 0;
	}


	static public DisplayItem load(String basePath, XmlPullParser xpp) throws XmlPullParserException, IOException {
		if (xpp == null)
			return null;
		
		int eventType = xpp.getEventType();
		if (eventType != XmlPullParser.START_TAG
		 || !xpp.getName().equalsIgnoreCase("DisplayItem"))
			return null;
		
		DisplayItem retValue = new DisplayItem();
		retValue._dataType = Integer.parseInt(xpp.getAttributeValue(null, "DataType"));
		
		switch(retValue._dataType) {
		case DataType_Image:      // 图片
			retValue._text = xpp.getAttributeValue(null, "Text");
			retValue._fileName = xpp.getAttributeValue(null, "FileName");
			
			retValue._fileList = xpp.getAttributeValue(null, "FileList").split(";");
			retValue._effectID = Integer.parseInt(xpp.getAttributeValue(null, "EffectID"));
			retValue._effectDuration = Integer.parseInt(xpp.getAttributeValue(null, "EffectDuration"));
			retValue._showSeconds = Integer.parseInt(xpp.getAttributeValue(null, "ShowSeconds"));


			retValue._isCrawlImage = Boolean.parseBoolean(xpp.getAttributeValue(null, "IsCrawlImage"));
			retValue._crawlSpeed = Integer.parseInt(xpp.getAttributeValue(null, "CrawlSpeed"));
			break;

		case DataType_Video:      // 视频
			retValue._text = xpp.getAttributeValue(null, "Text");
			retValue._fileName = xpp.getAttributeValue(null, "FileName");
			
//		
			
			retValue._markIn = Integer.parseInt(xpp.getAttributeValue(null, "MarkIn"));
			retValue._showSeconds = Integer.parseInt(xpp.getAttributeValue(null, "Duration"));
			break;

		case DataType_Text:       // 文字
			retValue._text = xpp.getAttributeValue(null, "Text");
			
			break;

		case DataType_PlayVideo:  // 直播   添加直播视频的功能
			retValue._text = xpp.getAttributeValue(null, "PlayName");
			retValue._fileName = xpp.getAttributeValue(null, "PlayUrl");
			retValue._showSeconds = Integer.parseInt(xpp.getAttributeValue(null, "TimeSlot"));
			break;

		case DataType_WebPage:    // 网页
			retValue._text = xpp.getAttributeValue(null, "WebName");
			retValue._fileName = xpp.getAttributeValue(null, "WebPath");
			retValue._showSeconds = Integer.parseInt(xpp.getAttributeValue(null, "WebTimeSlot"));
			break;

		case DataType_CiteVideo:   // 视频引用
			retValue._text = xpp.getAttributeValue(null, "PlayName");
			retValue._fileName = xpp.getAttributeValue(null, "PlayUrl");
			retValue._showSeconds = Integer.parseInt(xpp.getAttributeValue(null, "Duration"));
			break;
			
		default:
			return null;
		}
		
		// 将 相对路径 修改为 绝对路径
		if (retValue._fileName != null && !retValue._fileName.isEmpty())
			if (!(retValue._dataType == DataType_WebPage || retValue._dataType == DataType_PlayVideo)) {
				retValue._fileName = basePath + retValue._fileName;
			}
		
		if (retValue._fileList != null) {
			for (int i = 0; i < retValue._fileList.length; i++)
				retValue._fileList[i] = basePath + retValue._fileList[i];
		}
		return retValue;
	}
//	public static boolean isWebFile() {
//		if (getDataType() == DisplayItem.DataType_WebPage) {
//			return false;
//		}
//		return true;
//	}
}
