package com.tlz.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;

public class RomUtils {
	
	public static final String ROM_ANDROID = "Android";
	
	public static final String ROM_MIUI_V5 = "V5";
	public static final String ROM_MIUI_V6 = "V6";
	
	public static final String ROM_COLOROS = "corloros";
	
	public static final String PERMISSION_READ_SETTINGS = "com.android.launcher.permission.READ_SETTINGS";
	
	public static String getRom() {
		
		if ("V5".equalsIgnoreCase(SysUtils.getSystemProperty("ro.miui.ui.version.name"))) {
			return ROM_MIUI_V5;
		}
		
		if ("V6".equalsIgnoreCase(SysUtils.getSystemProperty("ro.miui.ui.version.name"))) {
			return ROM_MIUI_V6;
		}
		
		return ROM_ANDROID;
	}
	
	public static boolean isMIUI() {
		if (ROM_MIUI_V5.equals(getRom()) || ROM_MIUI_V6.equals(getRom())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static String getAuthorityFromPermission(Context context) {
		List<PackageInfo> piList = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (piList != null) {
			for (PackageInfo pi : piList) {
				ProviderInfo[] infos = pi.providers;
				if (infos != null) {
					for (ProviderInfo info : infos) {
						if (PERMISSION_READ_SETTINGS.equals(info.readPermission)) {
							return info.authority;
						}
					}
				}
			}
		}
		return "";
	}

	private RomUtils() {/*Do not new me!*/};
}
