package com.itsync.helper;

public class DateFormatMap {
	private String CFormat;
	private String JavaFormat;

	private DateFormatMap(String cFormat, String javaFormat) {
		this.CFormat = cFormat;
		this.JavaFormat = javaFormat;
	}

	final static DateFormatMap[] c_FormatMaps = {
			new DateFormatMap("%Y", "yyyy"), new DateFormatMap("%y", "yy"),
			new DateFormatMap("%m", "MM"), new DateFormatMap("%d", "dd"),
			new DateFormatMap("%H", "HH"), new DateFormatMap("%M", "mm"),
			new DateFormatMap("%S", "ss"), new DateFormatMap("%#m", "M"),
			new DateFormatMap("%#d", "d"), new DateFormatMap("%#H", "H"), };

	public static String TranslateDateFormat(String cDateFormat) {
		String retValue = cDateFormat;
		for (DateFormatMap item : c_FormatMaps) {
			retValue = retValue.replace(item.CFormat, item.JavaFormat);
		}
		return retValue;
	}
}
