package com.tlz.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class VerifyUtils {

	public static boolean isEmail(String text) {

		if (TextUtils.isEmpty(text)) return false;
		
		Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher m = p.matcher(text);
		return m.matches();
	}
	public static boolean isPwd(CharSequence text) {

		if (TextUtils.isEmpty(text)) return false;
		Pattern p = Pattern.compile("[a-zA-Z0-9]{6,12}");
		Matcher m = p.matcher(text);
		return m.matches();
	}
	public static boolean isPhoneNumber(String text) {

		if (TextUtils.isEmpty(text)) return false;
		
//		Pattern p = Pattern.compile("([+]?[0-9]{2,})?1[3-8]\\d{9}");
//		1[3|5|7|8|][0-9]{9}
		Pattern p = Pattern.compile("1[0-9]{10}");
		 
		Matcher m = p.matcher(text);
		return m.matches();
	}
	/**
	 * 非0正整数
	 * @param text
	 * @return
	 */
	public static boolean isNumber(String text) {
		System.err.println("cccccccccccccccc");
		if (TextUtils.isEmpty(text)) return false;
		System.err.println("dddddddddddddddddddd");
		Pattern p = Pattern.compile("^[1-9][0-9]*$");
		System.err.println("eeeeeeeeeeeeeeeeeeeeeee");
		Matcher m = p.matcher(text);
		System.err.println("ffffffffffffffffffffff");
		boolean result=m.matches();
		System.err.println("gggggggggggggggggggggg");
		return result;
	}
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(Map<?, ?> paramsMap){
		if (paramsMap == null || paramsMap.size() == 0) {
			return true;
		}
		return false;
	}
	
	private VerifyUtils(){/*Do not new me!*/};
}
