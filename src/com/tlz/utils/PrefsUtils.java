package com.tlz.utils;

import java.util.Map;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

public class PrefsUtils {
	
	private static final String DEFAULT_NAME = "_preferences";
	
	public static boolean putValue(Context cotnext, String key, Object value) {
		return putValue(cotnext, "", key, value);
	}
	
	public static boolean putValue(Context context, String name, String key, Object value) {
		return putValue(context, name, key, value, Context.MODE_PRIVATE);
	}
	
	public static boolean putValue(Context context, String name, Map<String, Object> map) {
		return putValue(context, name, map, Context.MODE_PRIVATE);
	}
	
	public static boolean putValue(Context context, String name, String key, Object value, int mode) {
		Map<String, Object> map = new ArrayMap<String, Object>();
		map.put(key, value);
		return putValue(context, name, map, mode);
	}

	@TargetApi(11)
	@SuppressWarnings("unchecked")
	public static boolean putValue(Context context, String name, Map<String, Object> map, int mode) {
		if (TextUtils.isEmpty(name)) name = context.getPackageName() + DEFAULT_NAME;
		
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		Editor editor = preferences.edit();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Boolean) {
				editor.putBoolean(key, Boolean.parseBoolean(String.valueOf(value)));
			} else if (value instanceof Float) {
				editor.putFloat(key, Float.parseFloat(String.valueOf(value)));
			} else if (value instanceof Integer) {
				editor.putInt(key, Integer.parseInt(String.valueOf(value)));
			} else if (value instanceof Long) {
				editor.putLong(key, Long.parseLong(String.valueOf(value)));
			} else if (value instanceof String) {
				editor.putString(key, String.valueOf(value));
			} else if (value instanceof Set) {
				editor.putStringSet(key, (Set<String>)value);
			} else {
				editor.putString(key, String.valueOf(value));
			}
		}
		return editor.commit();
	}
	
	/**
	 * 移除Key
	 * @param context
	 * @param key
	 */
	public static boolean removeKey(Context context, String key) {
		return removeKey(context, context.getPackageName() + DEFAULT_NAME, key);
	}
	
	public static boolean removeKey(Context context, String name, String key) {
		return removeKey(context, name, key, Context.MODE_PRIVATE);
	}
	
	public static boolean removeKey(Context context, String name, String key, int mode) {
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		Editor editor = preferences.edit();
		editor.remove(key);
		return editor.commit();
	}
	
	public static String getString(Context context, String key) {
		return getString(context, context.getPackageName() + DEFAULT_NAME, key, "", Context.MODE_PRIVATE);
	}
	
	public static String getString(Context context, String key, String defValue) {
		return getString(context, context.getPackageName() + DEFAULT_NAME, key, defValue, Context.MODE_PRIVATE);
	}
	
	public static String getString(Context context, String name, String key, String defValue) {
		return getString(context, name, key, defValue, Context.MODE_PRIVATE);
	}
	
	public static String getString(Context context, String name, String key, String defValue, int mode) {
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		return preferences.getString(key, defValue);
	}
	
	public static int getInt(Context context, String name, String key) {
		return getInt(context, name, key, -1, Context.MODE_PRIVATE);
	}
	
	public static int getInt(Context context, String name, String key, int defValue) {
		return getInt(context, name, key, defValue, Context.MODE_PRIVATE);
	}

	public static int getInt(Context context, String name, String key, int defValue, int mode) {
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		return preferences.getInt(key, defValue);
	}
	
	public static long getLong(Context context, String name, String key) {
		return getLong(context, name, key, -1, Context.MODE_PRIVATE);
	}
	
	public static long getLong(Context context, String name, String key, long defValue) {
		return getLong(context, name, key, defValue, Context.MODE_PRIVATE);
	}
	
	public static long getLong(Context context, String name, String key, long defValue, int mode) {
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		return preferences.getLong(key, defValue);
	}
	
	public static float getFloat(Context context, String name, String key) {
		return getFloat(context, name, key, -1, Context.MODE_PRIVATE);
	}
	
	public static float getFloat(Context context, String name, String key, float defValue) {
		return getFloat(context, name, key, defValue, Context.MODE_PRIVATE);
	}
	
	public static float getFloat(Context context, String name, String key, float defValue, int mode) {
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		return preferences.getFloat(key, defValue);
	}
	
	public static boolean getBoolean(Context context, String name, String key) {
		return getBoolean(context, name, key, false, Context.MODE_PRIVATE);
	}
	
	public static boolean getBoolean(Context context, String name, String key, boolean defValue) {
		return getBoolean(context, name, key, defValue, Context.MODE_PRIVATE);
	}
	
	public static boolean getBoolean(Context context, String name, String key, boolean defValue, int mode) {
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		return preferences.getBoolean(key, defValue);
	}
	
	public static Set<String> getStringSet(Context context, String name, String key) {
		return getStringSet(context, name, key, null, Context.MODE_PRIVATE);
	}
	
	public static Set<String> getStringSet(Context context, String name, String key, Set<String> defValues) {
		return getStringSet(context, name, key, defValues, Context.MODE_PRIVATE);
	}
	
	@TargetApi(11)
	public static Set<String> getStringSet(Context context, String name, String key, Set<String> defValues, int mode) {
		SharedPreferences preferences = context.getSharedPreferences(name, mode);
		return preferences.getStringSet(key, defValues);
	}
	
	private PrefsUtils(){/*Do not new me*/};
}
