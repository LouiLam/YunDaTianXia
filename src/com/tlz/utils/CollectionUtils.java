package com.tlz.utils;

import java.util.List;
import java.util.Map;

import android.text.TextUtils;

public class CollectionUtils {

	public static boolean isContained(List<String> list, String string) {
		return isContainedIgnoreCase(list, string, true);
	}
	
	public static boolean isContainedIgnoreCase(List<String> list, String string, boolean isIgnoreCase) {
		if (isEmpty(list)) return false;
		
		for (String s : list) {
			if (isIgnoreCase ? s.equalsIgnoreCase(string) : s.equals(string)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEmpty(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(Map<?, ?> map) {
		if (map == null || map.size() == 0) return true;
		return false;
	}
	
	public static boolean isContained(Map<String, ?> map, String str) {
		if (isEmpty(map)) return false;
		if (TextUtils.isEmpty(str)) return false;
		
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			if (entry.getKey().equals(str)) {
				return true;
			}
		}
		return false;
		
	}
}
