package com.tlz.utils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;

public class AndroidHttp {

	private static final String TAG = AndroidHttp.class.getSimpleName();
	private static final boolean DEBUG = true;
	
	private static AndroidHttp sHttpRequest;
	private ExecutorService mExecutor = Executors.newFixedThreadPool(15);

	public static AndroidHttp getInstance() {
		if (sHttpRequest == null) {
			sHttpRequest = new AndroidHttp();
		}
		return sHttpRequest;
	}
	
//	public void doPost(String urlString, HttpCallback callback) {
//		doRequest(Method.POST, urlString, null, null, callback);
//	}
//	
//	public void doPost(String urlString, Map<String, String> params, HttpCallback callback) {
//		doRequest(Method.POST, urlString, null, params, callback);
//	}
//	public void doPostString(String urlString, Map<String, String> params, HttpCallback callback) {
//		doRequest(Method.POST, urlString, null, params, callback);
//	}
//	
//	public void doPost(String urlString, String tag, HttpCallback callback) {
//		doRequest(Method.POST, urlString, tag, null, callback);
//	}
//	
//	public void doPost(String urlString, String tag, Map<String, String> params, HttpCallback callback) {
//		doRequest(Method.POST, urlString, tag, params, callback);
//	}
//	
//	public void doGet(String urlString, HttpCallback callback) {
//		doRequest(Method.GET, urlString, null, null, callback);
//	}
//	
//	public void doGet(String urlString, String tag, HttpCallback callback) {
//		doRequest(Method.GET, urlString, tag, null, callback);
//	}
	
	public void doRequest(String urlString , String params, HttpCallback callback) {
		mExecutor.execute(new LoadInfoThread( urlString,  params, callback));
	}
	
	public void uploadFile(String urlString, Map<String, String> params, File file, HttpCallback callback) {
		ArrayMap<String, File> fileParams = new ArrayMap<String, File>();
		fileParams.put("file", file);
		uploadFiles(urlString, params, fileParams, callback);
	}
	
	public void uploadFiles(String urlString, Map<String, String> params, Map<String, File> fileParams, HttpCallback callback) {
		mExecutor.execute(new UploadFileThread(urlString, params, fileParams, callback));
	}
	
	private static Handler sMainHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			HttpResult result = (HttpResult) msg.obj;
			String jsonString = result.mJsonString;
			
			if (result.mHttpCallback == null) {
				Flog.e(TAG, "HttpCallback can not be null!");
				return;
			}
			
			result.mHttpCallback.onRequestFinish(result.mTag);
			
			if (jsonString != null) {
				
				if (DEBUG) Flog.i("jsonString:" + jsonString);
				
				result.mHttpCallback.onRequestSuccess(result.mTag, jsonString);
				
			} else {
				result.mHttpCallback.onRequestFailure(result.mTag, ErrorUtils.ERROR_CODE_NETWORK);
			}
			super.handleMessage(msg);
		}
	};
	
	private class LoadInfoThread implements Runnable {

		private int mMethod;
		private String mUrlString;
		private  String mParams;
		private String mTag;
		private HttpCallback mCallback;
		
		public LoadInfoThread( String urlString, String params, HttpCallback callback) {
			mUrlString = urlString;
			mParams = params;
			mCallback = callback;
		}
		
		@Override
		public void run() {
			Message msg = sMainHandler.obtainMessage();
//			String jsonString = HttpUtils.doRequest(mMethod, mUrlString, mParams);
			
			String jsonString = HttpUtils.doPostString( mUrlString, mParams);
			msg.obj = new HttpResult(mTag, jsonString, mCallback);
			msg.sendToTarget();
		}
		
	}
	
	private class UploadFileThread implements Runnable {

		private String mUrlString;
		private Map<String, String> mParams;
		private Map<String, File> mFileParams;
		private HttpCallback mCallback;
		
		public UploadFileThread(String urlString, Map<String, String> params, Map<String, File> fileParams, HttpCallback callback) {
			mUrlString = urlString;
			mParams = params;
			mFileParams = fileParams;
			mCallback = callback;
		}
		
		@Override
		public void run() {
			Message msg = sMainHandler.obtainMessage();
			String jsonString = HttpUtils.uploadFile(mUrlString, mParams, mFileParams.get("file"));
			msg.obj = new HttpResult(null, jsonString, mCallback);
			msg.sendToTarget();
		}
		
	}
	
	private class HttpResult {
		
		final String mJsonString;
		final String mTag;
		final HttpCallback mHttpCallback;
		
		public HttpResult(String tag, String jsonString, HttpCallback callback) {
			mJsonString = jsonString;
			mTag = tag;
			mHttpCallback = callback;
		}
	}
	
	public static abstract class SimpleHttpCallback implements HttpCallback {

		@Override
		public void onRequestSuccess(String tag, String jsonString) {
		}

		@Override
		public void onRequestFailure(String tag, int errorCode) {
		}

		@Override
		public void onRequestFinish(String tag) {
		}
		
	}
	
	public interface HttpCallback {
		public void onRequestSuccess(String tag, String jsonString);
		public void onRequestFailure(String tag, int errorCode);
		public void onRequestFinish(String tag);
	}
	
	private AndroidHttp(){/*Do not new me!*/};
}

