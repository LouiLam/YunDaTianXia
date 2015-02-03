package com.tlz.utils;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

public class StringUtils {
	
//	private static final String TAG = StringUtils.class.getSimpleName();
//	private static final boolean DEBUG = true;
	
	public static final String FILE_EXTENSION_APK = "apk";
	public static final String FILE_EXTENSION_DOC = "doc";
	public static final String FILE_EXTENSION_DOCX = "docx";
	public static final String FILE_EXTENSION_PDF = "pdf";
	public static final String FILE_EXTENSION_PPT = "ppt";
	public static final String FILE_EXTENSION_PPTX = "pptx";
	public static final String FILE_EXTENSION_XLS = "xls";
	public static final String FILE_EXTENSION_XLSX = "xlsx";
	public static final String FILE_EXTENSION_TXT = "txt";
	public static final String FILE_EXTENSION_XML = "xml";
	public static final String FILE_EXTENSION_LOG = "log";
	public static final String FILE_EXTENSION_ZIP = "zip";
	
	public static String formatPlayTime(long milliseconds) {
		if (milliseconds < 0) {
			return "00:00";
		}
		int totalSeconds = (int)(milliseconds / 1000);
		int seconds = totalSeconds % 60;
		int minutes = totalSeconds / 60;
		int hours = minutes / 60;
		return hours == 0 ? String.format(Locale.SIMPLIFIED_CHINESE, "%02d:%02d", minutes, seconds) : 
			String.format(Locale.SIMPLIFIED_CHINESE, "%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	public static String getFileExtension(String fileName) {
		if (fileName != null && fileName.length() > 0) {
			int start = fileName.lastIndexOf('.');
			if (start > -1 && start < fileName.length() -1) {
				return fileName.substring(start + 1).toLowerCase(Locale.getDefault());
			}
		}
		return "";
	}
	
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i=0; i<strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 通过扩展名获取MIME类型
	 * @param extension
	 * @return
	 */
	public static String getMimeTypeByExtension(String extension) {
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
	}
	
	/**
	 * 通过文件名获取MIME类型
	 * @param extension
	 * @return
	 */
	public static String getMimeTypeByFileName(String name) {
		return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtension(name));
	}
	
	/**
	 * 通过MIME获取扩展名
	 * @param mimeType
	 * @return
	 */
	public static String getExtensionByMimeType(String mimeType) {
		return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
	}
	
	/**
	 * 通过Url获取扩展名
	 * @param url
	 * @return
	 */
	public static String getExtensionFromUrl(String url) {
		return MimeTypeMap.getFileExtensionFromUrl(url);
	}
	
	public static String getFileNameFromPath(String path) {
		if (TextUtils.isEmpty(path)) return "noname";
		
		if (path.contains("\\")) {
			return path.substring(path.lastIndexOf('\\') + 1);
		} else {
			return path.substring(path.lastIndexOf('/') + 1);
		}
	}
	
	public static String getFileNameFromUrl(String urlString) {
		if (TextUtils.isEmpty(urlString)) return "noname";
		
		return urlString.substring(urlString.lastIndexOf('/') + 1);
	}
	
	/**
	 * 通过全路径报名获取简单包名
	 * @param className
	 * @return
	 */
	public static String getSimpleNameFromClassName(String className) {
		int start = className.lastIndexOf(".") + 1;
		int end = className.indexOf("$");
		return className.substring(start, end == -1 ? className.length() : end);
	}
	
	public static String getSimpleNameFromFullName(String fullName) {
		return getSimpleNameFromClassName(fullName);
	}
	
	public static String formatValue(double number, int precision) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(precision);
		return format.format(number);
	}
	
	/**
	 * 将Map转化为Get请求的参数
	 * @param paramsMap
	 * @return
	 */
	public static String formatMapToRequestParams(Map<String, String> paramsMap, boolean isFirstParams) {
		if (paramsMap == null) return "";
		
		StringBuffer result = new StringBuffer();
		int count = 0;
		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
			if (entry.getKey() == null) continue;
			if (count == 0) {
				if (isFirstParams) {
					result.append("?");
				}
			} else {
				result.append("&");
			}
			count++;
			
			result.append(entry.getKey());
			result.append("=");
			result.append(entry.getValue() == null ? "" : EncodeUtils.encode(entry.getValue()));
		}
		return result.toString();
	}
	
	public static byte[] getByte(String text) {
		try {
			return text.getBytes(EncodeUtils.getDefultCharset());
		} catch (UnsupportedEncodingException e) {
			Flog.e(e);
		}
		return null;
	}
	
	public static String getJsonString(String jsonString) {
		jsonString.replaceAll("'", "&apos;");
		jsonString.replaceAll("\"", "&quot;");
		jsonString.replaceAll(">", "&gt;");
		jsonString.replaceAll("<", "&lt;");
		jsonString.replaceAll("&", "&amp;");
		return jsonString.replaceAll(" ", "\u0020");
	}
	
	private StringUtils(){/*Do not new me*/};
}
