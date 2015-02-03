package com.tlz.shipper.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tlz.shipper.R;

public class WebContentActivity extends ThemeActivity {

	private WebView mWebView;
	private String mTitle;
	private String mUrl;
	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_content);
		init();
	}

	private void init() {

		Intent intent = getIntent();
		mTitle = intent.getStringExtra("title");
		mUrl = intent.getStringExtra("url");
		mActionBar.setTitle(mTitle);

		mWebView = (WebView) findViewById(R.id.webView);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		initWebSettings();
//		mWebView.addJavascriptInterface(new Contact(), "contact");
		mWebView.loadUrl(mUrl);
		//mWebView.loadUrl("file:///android_asset/index.html");
		//mWebView.loadUrl("http://www.baidu.com");
		mWebView.setWebChromeClient(new LamWebChromeClient());
		mWebView.setWebViewClient(new LamWebViewClient());
		mWebView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		mProgressBar.setProgress(1);
		
	}

//	@SuppressLint("NewApi")
//	private final class Contact {
//		// JavaScript调用此方法拨打电话
//		@JavascriptInterface
//		public void call(String phone) {
//			Log.e("ZCL", "js回调call");
//			if (getUser().isValideUser()) {
//				String json = "[{\"name\":\"zxx\", \"amount\":\"9999999\", \"phone\":\"18600012345\"}]";
//				// 调用JS中的方法
//				Log.e("ZCL", "js回调登录了");
//				mWebView.loadUrl("javascript:show('" + json + "')");
//			} else {
//				startActivity(new Intent(WebContentActivity.this,LoginActivity.class));
//			}
//		}
//
//		// Html调用此方法传递数据
//		@JavascriptInterface
//		public void showcontacts() {
//			String json = "[{\"name\":\"zxx\", \"amount\":\"9999999\", \"phone\":\"18600012345\"}]";
//			// 调用JS中的方法
//			Log.e("ZCL", "js回调showcontacts");
//			if (Build.VERSION.SDK_INT < 19) {
//				mWebView.loadUrl("javascript:show('" + json + "')");
//			} else {
//				String script = String.format("javascript:show('" + json + "')");
//				mWebView.evaluateJavascript(script,
//						new ValueCallback<String>() {
//							@Override
//							public void onReceiveValue(String value) {
//								Log.e("ZCL", "返回值" + value);
//							}
//						});
//			}
//		}
//
//	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void initWebSettings() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setAppCacheEnabled(true);
		webSettings.setAppCachePath(getCacheDir().getAbsolutePath());
		webSettings.setJavaScriptEnabled(true);

		// 单行显示
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);

		// 支持缩放
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		if (mAndroidVersion >= 11) {
			webSettings.setDisplayZoomControls(false);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	class LamWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			mWebView.loadUrl(url);
			return true;
		}

	}

	class LamWebChromeClient extends WebChromeClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);// 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
			return true;
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				mProgressBar.setVisibility(View.GONE);
			} else {
				mProgressBar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

}
