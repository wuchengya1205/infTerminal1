package com.itsync.displayobject;







import java.io.File;

import com.itsync.infterminal.FontInfo;
import com.itsync.infterminal.FontList;

import android.graphics.Typeface;
import android.os.Environment;

public class TextFont {
	private static Typeface _defaultTypeface;
	
	public static synchronized Typeface getFontType(String fontName) {
//		typeface = Typeface.SERIF;
		FontList fontList = FontList.GetInstance();
		for (int i = 0; i < fontList.size(); i++) {
			FontInfo fontInfo = fontList.get(i);
			if (fontInfo.FontName.equals(fontName)) {
				Typeface typeface = fontInfo.GetTypeface();
				if (typeface != null) {
					return fontInfo.GetTypeface();
				}
			} 
		}
		
		if (_defaultTypeface == null) {
			String path = Environment.getExternalStorageDirectory()+"/MagicInfData/Fonts/msyh.ttf";
			if (new File(path).exists()) {
				_defaultTypeface = Typeface.createFromFile(path);
			}
			else {
				_defaultTypeface = Typeface.SERIF;
			}
		}
		
		return _defaultTypeface;
	}
}
