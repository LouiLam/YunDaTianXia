package com.tlz.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;


public class CrashHandler implements UncaughtExceptionHandler {

	private static final boolean DEBUG = true;
	
	private Context mContext;
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	private static CrashHandler mInstance;
	private Map<String, String> mInfos = new HashMap<String, String>();
	
	private CrashHandler(){/*Do not new me*/};
	
	private CrashHandler(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		if (!Config.DEBUG) {
			uploadLog();
		}
	}
	
	public static CrashHandler startMonitor(Context context) {
		if (mInstance == null) {
			mInstance = new CrashHandler(context);
		}
		Thread.setDefaultUncaughtExceptionHandler(mInstance);
		return mInstance;
	}
	
	private void uploadLog() {
		Flog.getInstance(mContext).uploadCrashLogByFtp(null);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Flog.e(ex);
		if (mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}
		if (Config.DEBUG) return;
		handleException(ex);
		exit();
	}
	
	private void handleException(Throwable ex) {
		if (ex == null || TextUtils.isEmpty(ex.toString()));
		collectDeviceInfo();
		saveCrashInfoToFile(ex);
	}
	
	private void collectDeviceInfo() {
		try {
			mInfos.put("versionName", ManifestUtils.getAppVersionName(mContext));
			mInfos.put("versionCode", ManifestUtils.getAppVersionCode(mContext) + "");
		} catch (Exception e) {
			Flog.e(e);
		}
		Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Flog.e(e);
            }
        }
	}
	
	private void saveCrashInfoToFile(Throwable ex) {
		StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
  
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        sb.append(writer.toString());
        try {
            String fileName = "crash-" + TimeUtils.getCurrentDateTimeFullInString() + ".log";
            File dir = new File(mContext.getCacheDir(), "logs");
            FileUtils.createDir(dir);
            FileUtils.writeString(sb.toString(), dir, fileName);
        } catch (Exception e) {
            Flog.e(e);
        }
	}
	public static String getErrorMsg(Exception e)
	{
		  Writer writer = new StringWriter();  
	      PrintWriter printWriter = new PrintWriter(writer);  
	      e.printStackTrace(printWriter);  
	      Throwable cause = e.getCause();  
	      while (cause != null) {  
	          cause.printStackTrace(printWriter);  
	          cause = cause.getCause();  
	      }  
	      printWriter.close();  
	      String result = writer.toString();  
	      return result;
	}
	
	private void exit() {
		if (DEBUG) Flog.i("exit");
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}
}
