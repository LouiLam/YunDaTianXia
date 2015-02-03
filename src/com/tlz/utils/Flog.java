package com.tlz.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;


public class Flog {
	
	private static final String UPLOAD_URL = "";
	
	private static final int MSG_WHAT_WRITE = 1;
	private static final int MSG_WHAT_UPLOAD = 2;
	private static final int MSG_WHAT_UPLOAD_BY_FTP = 3;
	
	private static Flog mInstance;
	private Context mContext;
	private Executor mThreadPool = Executors.newFixedThreadPool(4);
	private MainHandler mHandler = new MainHandler(Looper.getMainLooper());
	private File mDefaultDir;
	private String mDefaultFileName;
	
	private Flog(Context context) {
		mContext = context;
		mDefaultDir = FileUtils.getDirectory(StorageUtils.getExternalStorageRootDir().toString() + "/" + 
					StringUtils.getSimpleNameFromFullName(mContext.getPackageName()));
		mDefaultFileName = "log-" + TimeUtils.getCurrentDateInString() + ".log";
	}
	
	public static Flog getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new Flog(context);
		}
		return mInstance;
	}
	
	public static void e(Object obj) {
		android.util.Log.e(getInvokerClassName(), String.valueOf(obj));
	}
	
	public static void i(Object obj) {
		android.util.Log.i(getInvokerClassName(), String.valueOf(obj));
	}
	
	public static void e(Throwable tr) {
		android.util.Log.e(getInvokerClassName(), "", tr);
	}
	
	public static void i(String tag, Object obj) {
		android.util.Log.i(tag, String.valueOf(obj));
	}

	public static void i(String tag, String format, Object... args) {
		android.util.Log.i(tag, String.format(format, args));
	}
	
	public static void e(String tag, Throwable tr) {
		android.util.Log.e(tag, "", tr);
	}
	
	public static void e(String tag, String msg, Throwable tr) {
		android.util.Log.e(tag, msg, tr);
	}
	
	public static void e(String tag, String format, Object... args) {
		android.util.Log.e(tag, String.format(format, args));
	}
	
	public static String getCurrentMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
	public static void p(Object o) {
		System.out.println(o);
	}
	
	public static void p(String TAG, Object obj) {
		System.out.println(TextUtils.isEmpty(TAG) ? obj : TAG + ":" + obj);
	}
	
	public static void err(Object o) {
		System.err.println(o);
	}
	
	public static String getInvokerClassName() {
		StackTraceElement[] elements = (new Throwable()).getStackTrace();
		return StringUtils.getSimpleNameFromFullName(elements[2].getClassName());
	}
	
	public static String getInvokerMethodName() {
		StackTraceElement[] elements = (new Throwable()).getStackTrace();
		return elements[2].getMethodName();
	}
	
	public void writeLog(String text) {
		writeLog(text, null);
	}
	
	public void writeLog(String text, OnWriteListener listener) {
		writeLog(mDefaultDir, mDefaultFileName, text, listener);
	}
	
	public void writeLog(File dir, String fileName, String text, OnWriteListener listener) {
		mThreadPool.execute(new WriteThread(dir, fileName, text, listener));
	}
	
	public void uploadCrashLogByFtp(OnUploadListener listener) {
		mThreadPool.execute(new UploadCrashLogByFtpThread(listener));
	}
	
	/**
	 * 上传日志
	 * @param id 可以指定上传的目录ID
	 * @param fileList 文件列表 为空时取默认
	 * @param listener 回调
	 */
	public void uploadLog(@Nullable String id, @Nullable List<File> fileList, @Nullable OnUploadListener listener) {
		mThreadPool.execute(new UploadThread(id, fileList, listener));
	}
	
	static class MainHandler extends Handler {

		public MainHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			HandlerResult result = (HandlerResult) msg.obj;
			if (msg.what == MSG_WHAT_WRITE) {
				OnWriteListener onWriteListener = null;
				if (result.mOnOpListener instanceof OnWriteListener) {
					onWriteListener = (OnWriteListener)result.mOnOpListener;
				}
				
				if (result.mIsSuccess) {
					if (onWriteListener != null) {
						onWriteListener.onWriteSuccess();
					}
				} else {
					if (onWriteListener != null) {
						onWriteListener.onWriteFailure(msg.what);
					}
				}
			} else if (msg.what == MSG_WHAT_UPLOAD || msg.what == MSG_WHAT_UPLOAD_BY_FTP) {
				OnUploadListener onUploadListener = null;
				if (result.mOnOpListener instanceof OnUploadListener) {
					onUploadListener = (OnUploadListener)result.mOnOpListener;
				}
				
				if (onUploadListener != null) {
					onUploadListener.onUploadResult(result.mResultMap);
				}
			}
			super.handleMessage(msg);
		}
		
	}
	
	static class HandlerResult {
		OnOpListener mOnOpListener;
		boolean mIsSuccess;
		Map<String, Boolean> mResultMap;
		
		public HandlerResult(OnOpListener listener) {
			mOnOpListener = listener;
		}
		
		public void setResult(boolean isSuccess) {
			mIsSuccess = isSuccess;
		}
		
		public void setResultMap(Map<String, Boolean> map) {
			mResultMap = map;
		}
		
	}
	
	/**
	 * 日志线程
	 * @author Yucun
	 *
	 */
	class WriteThread implements Runnable {

		private File mTargetDir;
		private String mFileName;
		private String mText;
		private OnWriteListener mOnWriteListener;
		
		public WriteThread(File dir, String fileName, String text, OnWriteListener listener) {
			mTargetDir = dir;
			mFileName = fileName;
			mText = text;
			mOnWriteListener = listener;
		}
		
		@Override
		public void run() {
			if (mContext == null) {
				Flog.e("mContext can not be null!");
				return;
			}
			
			Message msg = mHandler.obtainMessage(MSG_WHAT_WRITE);
			HandlerResult result = new HandlerResult(mOnWriteListener);
			
			if (StorageUtils.isExternalStorageReady()) {
				File file = FileUtils.writeString(mText, mTargetDir, mFileName);
				if (file == null) {
					result.setResult(false);
				} else {
					result.setResult(true);
				}
			} else {
				result.setResult(false);
			}
			msg.obj = result;
			
			if (mOnWriteListener != null) {
				mHandler.sendMessage(msg);
			}
		}
		
	}

	/**
	 * 上传线程
	 * @author Yucun
	 *
	 */
	class UploadThread implements Runnable {

		private String mId;
		private List<File> mFileList;
		private OnUploadListener mOnUploadListener;
		
		public UploadThread(String id, List<File> fileList, OnUploadListener listener) {
			mId = id;
			mFileList = fileList;
			mOnUploadListener = listener;
		}
		
		@Override
		public void run() {
			if (mContext == null) {
				Flog.e("mContext can not be null!");
				return;
			}
			
			Message msg = mHandler.obtainMessage(MSG_WHAT_UPLOAD);
			HandlerResult result = new HandlerResult(mOnUploadListener);
			Map<String, String> params = new ArrayMap<String, String>();
			params.put("id", mId);
			
			if (CollectionUtils.isEmpty(mFileList)) {
				mFileList = FileUtils.getSubFiles(mDefaultDir);
			}
			
			Map<String, Boolean> resultMap = new ArrayMap<String, Boolean>();
			for (File file : mFileList) {
				String resultString = HttpUtils.uploadFile(UPLOAD_URL, params, file);
				if (!TextUtils.isEmpty(resultString)) {
					FileUtils.deleteFile(file);
					resultMap.put(file.getPath(), true);
				} else {
					resultMap.put(file.getPath(), false);
				}
			}
			result.setResultMap(resultMap);
			msg.obj = result;
			
			if (mOnUploadListener != null) {
				mHandler.sendMessage(msg);
			}
		}
		
	}
	
	class UploadCrashLogByFtpThread implements Runnable {

		private OnUploadListener mOnUploadListener;
		
		public UploadCrashLogByFtpThread(OnUploadListener listener) {
			mOnUploadListener = listener;
		}
		
		@Override
		public void run() {
			if (mContext == null) {
				Flog.e("mContext can not be null!");
				return;
			}
			
			Message msg = mHandler.obtainMessage(MSG_WHAT_UPLOAD_BY_FTP);
			HandlerResult result = new HandlerResult(mOnUploadListener);
			
			Map<String, Boolean> resultMap = new ArrayMap<String, Boolean>();
			
			File crashDir = new File(mContext.getCacheDir(), "logs");
			File[] files = crashDir.listFiles();
			if (files != null && files.length != 0) {
				resultMap = FtpUtils.upload(TimeUtils.getCurrentDate(), Arrays.asList(files));
			}
			
			if (resultMap != null) {
				for (Map.Entry<String, Boolean> entry : resultMap.entrySet()) {
					if (entry.getValue()) FileUtils.deleteFile(entry.getKey());
				}
			}
			
			result.setResultMap(resultMap);
			msg.obj = result;
			
			if (mOnUploadListener != null) {
				mHandler.sendMessage(msg);
			}
		}
		
	}
	
	public interface OnWriteListener extends OnOpListener {
		public void onWriteSuccess();
		public void onWriteFailure(int errorCode);
	}
	
	public interface OnUploadListener extends OnOpListener {
		public void onUploadResult(Map<String, Boolean> filePathMap);
	}
	
	interface OnOpListener {
	}
	
	private Flog(){/*Do not new me*/};
}
