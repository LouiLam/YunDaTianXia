package com.tlz.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class ManifestUtils {

	private static final String TAG = ManifestUtils.class.getSimpleName();
	private static final boolean DEBUG = false;


	public static int getAppVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			if (DEBUG) Log.e(TAG, "NameNotFoundException");
		}
		return info.versionCode;
	}
	
	public static String getAppVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			Log.e(TAG, "NameNotFoundException");
		}
		return info.versionName;
	}
	
	/**
	 * 获取<meta-data>的值
	 * @param context
	 * @param metaKey
	 * @return
	 */
    public static String getMetaValue(Context context, Class<?> cls, String metaKey) {
        Bundle metaData = null;
        String value = null;
        if (context == null || TextUtils.isEmpty(metaKey)) {
        	return null;
        }
        try {
        	PackageItemInfo pii = null;
        	if (Application.class.isAssignableFrom(cls)) {
        		pii = context.getPackageManager()
        				.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        	} else if (Activity.class.isAssignableFrom(cls)) {
        		pii = context.getPackageManager()
                        .getActivityInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
        	} else if (Service.class.isAssignableFrom(cls)) {
        		pii = context.getPackageManager()
        				.getServiceInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
        	} else if (BroadcastReceiver.class.isAssignableFrom(cls)) {
        		pii = context.getPackageManager()
        				.getReceiverInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
        	}
        	if (pii != null) {
        		metaData = pii.metaData;
        	}
            if (metaData != null) {
            	value = String.valueOf(metaData.get(metaKey));
            }
        } catch (NameNotFoundException e) {
        	if (DEBUG) 
        		Log.e(TAG, Flog.getCurrentMethodName() + " NameNotFoundException", e);
        }
        return value;
    }
    
    public static int getAppIconId(Context context) {
    	try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
			return info.icon;
		} catch (NameNotFoundException e) {
			Flog.e(e);
		}
    	return 0;
    }
    
    private ManifestUtils() {/*Do not new me*/}
}
