package com.tlz.utils;

public class AndroidTextUtils {
	public static boolean isEmpty(CharSequence str) {
		return android.text.TextUtils.isEmpty(str) || str.equals("null");
	}
}
