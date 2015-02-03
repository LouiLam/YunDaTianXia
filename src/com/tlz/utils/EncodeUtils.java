package com.tlz.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.text.TextUtils;

public class EncodeUtils {
	
	private static final String TAG = EncodeUtils.class.getSimpleName();

	private static final String CHARSET_UFT_8 = "UTF-8";
	
    public static String encode(String text) {
    	if (TextUtils.isEmpty(text)) return "";
    	
		try {
			return URLEncoder.encode(text, getDefultCharset());
		} catch (UnsupportedEncodingException e) {
			Flog.e(e);
		}
		
		return "";
    }
    
    public static String getDefultCharset() {
    	return CHARSET_UFT_8;
    }
	
	/**
	 * 判断文件的编码格式
	 * 
	 * @param filePath
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String getTextCodeFormat(String filePath) {
		String code = null;
		BufferedInputStream bis = null;
		int p = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			p = (bis.read() << 8) + bis.read();
		} catch (Exception e) {
			Flog.e(TAG, e);
		} finally {
			IOUtils.close(bis);
		}
		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		default:
			code = "GBK";
		}
		return code;
	}

	private EncodeUtils() {/*Do not new me!*/}
}
