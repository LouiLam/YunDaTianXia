package com.tlz.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

public class SysUtils {
	
    public static String getSystemProperty(String propertyName) {
        String line = null;
        BufferedReader reader = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propertyName);
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = reader.readLine();
            return line;
        } catch (IOException e) {
        	Flog.e(e);
        } finally {
            IOUtils.close(reader);
        }
        return "UNKNOWN";
    }

    /**
     * 杀进程
     * Requires Permission:
     * 	System Level {@link android.Manifest.permission#FORCE_STOP_PACKAGES}
     * @param context
     * @param packageName
     */
    public static void killProcess(Context context, String packageName){
    	ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		try {
			ReflectUtils.invokeMethod(aManager, "forceStopPackage", packageName);
		} catch (Exception e) {
			Flog.e(e);
		}
    }
    
    /**
     * 获取可用的内存大小
     * @param context
     * @return
     */
	public static long getAvailableMemorySize(Context context) {
		ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo info = new MemoryInfo();
		aManager.getMemoryInfo(info);
		return info.availMem;
	}

	/**
	 * 获取堆大小
	 * @param context
	 * @return
	 */
	public static int getHeapSize(Context context) {
		ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return aManager.getMemoryClass();
	}
	
	/**
	 * 获取app可占用的内存大小
	 * @param context
	 * @return MB
	 */
	public static int getMemoryClass (Context context) {
		return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}

	private SysUtils(){/*Do not new me!*/}
}
