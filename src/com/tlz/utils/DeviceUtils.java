package com.tlz.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class DeviceUtils {

	/**
	 * 判断是否是平板
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isEmulator() {
		return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
	}
	
	public static boolean isGenymotion() {
		return Build.DEVICE.startsWith("vbox86");
	}
	
	public static long getAvailableMemorySize(Context context) {
		ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo info = new MemoryInfo();
		aManager.getMemoryInfo(info);
		return info.availMem;
	}
	
	public static int getHeapSize(Context context) {
		ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return aManager.getMemoryClass();
	}
	
	@TargetApi(9)
	public static String getSerial() {
    	return Build.VERSION.SDK_INT >=9 ? Build.SERIAL : "0";
    }
	
	/**
	 * 获取MAC地址(无需申请相应的权限,wifi未打开状态下获取不到)!
	 * @param
	 * @return
	 */
	public static String getMacAddress() {
		String macAddress = "";
        String str = "";
        try {
        	Process process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
        	InputStreamReader ir = new InputStreamReader(process.getInputStream());
        	LineNumberReader input = new LineNumberReader(ir);
        	for (; str != null; ) {
        		str = input.readLine();
        		if (str != null) {
        			macAddress = str.trim();
        			break;
        		}
        	}
        } catch (IOException ex) {
        	
        }
        return macAddress;
	}
	
	/**
	 * 获取MAC地址
	 * @param
	 * @return
	 */
	public static String getMacAddress(Context context) {
		try {
			WifiManager wManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wManager.getConnectionInfo();
			if (info != null) {
				return info.getMacAddress() == null ? "" : info.getMacAddress();
			}
		} catch (SecurityException e) {
			return getMacAddress();
		}
		return "";
	}
	
	/**
	 * 获取蓝牙地址
	 * @param context
	 * @return
	 */
	public static String getBluetoothAddress(Context context) {
		String address = "";
		try {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if (adapter != null) {
				address = adapter.getAddress() == null ? "" : adapter.getAddress();
			}
		} catch (SecurityException e) {
			
		} catch (Exception e) {
			
		}
		return address;
	}
	
	/**
	 * 获取IMEI
	 * Requires Permission:
	 * {@link android.Manifest.permission#READ_PHONE_STATE}
	 * @param
	 * @return
	 */
	public static String getIMEI(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getDeviceId() == null ? "" : tm.getDeviceId();
		} catch (SecurityException e) {
			Flog.e("Requires android.Manifest.permission#READ_PHONE_STATE");
		}
		return "";
	}
	
	public static String getDeviceUniqueId(Context context) {
		String imei = getIMEI(context);
		String macAddress = getMacAddress();
		String serial = getSerial();
		return EncryptUtils.encryptMD5(imei + macAddress + serial);
	}
	
	/**
	 * 获取app可占用的内存大小
	 * @param context
	 * @return MB
	 */
	public static int getMemoryClass (Context context) {
		return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}
	
	private DeviceUtils(){/*Do not new me!*/}
}
