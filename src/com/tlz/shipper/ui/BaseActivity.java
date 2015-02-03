package com.tlz.shipper.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;

import com.baidu.location.BDLocation;
import com.tlz.admin.LocationAdmin;
import com.tlz.admin.LocationAdmin.OnLocationListener;
import com.tlz.shipper.R;
import com.tlz.utils.AndroidUtils;
import com.tlz.utils.Flog;
import com.tlz.utils.HandlerMsg;
import com.tlz.utils.ReflectUtils;
import com.tlz.utils.ToastUtils;

public class BaseActivity extends ActionBarActivity implements OnClickListener, Callback {
	
	protected BaseApplication mApplication;
	
	public static int mScreenWidth;
	public static int mScreenHeight;
	public float mDensity;
	
	public int mAndroidVersion;
	
	private boolean mPressTwoExit = false;
	private long mPressTime = 0;
	
	protected ActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	protected void initView()
	{
		
	}
	private void init() {
//		mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
//		mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
		mScreenWidth = getResources().getDisplayMetrics().widthPixels;
		mScreenHeight = getResources().getDisplayMetrics().heightPixels;
		mDensity = getResources().getDisplayMetrics().density;
		mAndroidVersion = Build.VERSION.SDK_INT;
		
		if (getApplication() instanceof BaseApplication) {
			mApplication = (BaseApplication) getApplication();
			mApplication.addActivity(this);
		}
		
		mActionBar = getSupportActionBar();
	}
	Handler  locationHandler;
	public void  sendLocSucMessage(BDLocation location)
	{
		if(locationHandler==null)return;
		Message msg=Message.obtain();
		msg.obj=location;
		msg.what=HandlerMsg.LocationSuc;
		locationHandler.sendMessage(msg);
	}
	public void  sendLocFailedMessage()
	{
		if(locationHandler==null)return;
		Message msg=Message.obtain();
		msg.obj=getString(R.string.locationing_error);
		msg.what=HandlerMsg.LocationFailed;
		locationHandler.sendMessage(msg);
	}
	protected void initLocation() {
		if(locationHandler==null)
		{locationHandler=new Handler(this);}
		LocationAdmin.getInstance(this).startLocation(new OnLocationListener() {

			@Override
			public void onLocation(BDLocation location) {
//				mLocation = location;
				if(location.getProvince()==null)
				{
					sendLocFailedMessage();
				}
				else
				{
					sendLocSucMessage(location);
//					myLocation=String.format("%s %s %s", location.getProvince(),location.getCity(),location.getDistrict());
				}
				
			}

			@Override
			public void onLocationFailure(int arg0) {
				sendLocFailedMessage();
			}
			
		});
	}
	public void setViewsClickListener(View...views) {
		for (View v : views) {
			if (v != null) {
				v.setOnClickListener(this);
			} else {
				Flog.e("Some Views are null! cannot setOnClickListener!");
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.f_right_enter, R.anim.f_fade_slow_exit);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.f_right_enter, R.anim.f_fade_slow_exit);
	}

	@Override
	public void finish() {
		if (mApplication != null) {
			mApplication.removeActivity(this);
		}
		super.finish();
		overridePendingTransition(R.anim.f_fade_fast_enter, R.anim.f_right_exit);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void unbindService(ServiceConnection conn) {
		try {
			super.unbindService(conn);
		} catch (IllegalArgumentException e) {
			Flog.e("Service not registered!");
		}
	}

	@Override
	public boolean stopService(Intent name) {
		if (name == null) {
			Flog.e("intent cannot be null!");
			return true;
		}
		return super.stopService(name);
	}
	
	@TargetApi(14)
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
	}

	/**
	 * 禁止截屏
	 * @param enable
	 */
	protected void setWindowSecure(boolean enable) {
		AndroidUtils.setWindowSecure(this, enable);
	}
	
	/**
	 * 设置全屏
	 * @param on
	 */
	protected void setFullScreen(boolean on) {
		AndroidUtils.setFullScreen(this, on);
	}
	
	protected void setPressTwoExitEnable() {
		mPressTwoExit = true;
	}
	
	protected int getColor(int resId) {
		return getResources().getColor(resId);
	}
	
	protected String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}
	
	protected DisplayMetrics getDisplayMetrics() {
		return getResources().getDisplayMetrics();
	}
	
	/**
	 * 设置ActionBar的按钮永远显示
	 */
	protected void setActionBarMenuVisible() {
		ReflectUtils.setBoolean(ViewConfiguration.get(this), "sHasPermanentMenuKey", false);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (mPressTwoExit) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
				if (System.currentTimeMillis() - mPressTime > 2000) {
					ToastUtils.show(this, "再按一次退出" + getPackageManager().getApplicationLabel(getApplicationInfo()) + "!");
					mPressTime = System.currentTimeMillis();
				} else {
					mApplication.exit();
				}
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		onClick(id);
	}
	
	public void onClick(int viewId) {
	}
	
	protected void showInputMethod(boolean visibility) {
		View focusView = getCurrentFocus();
		
		if (focusView != null) {
			InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (visibility) {
				manager.showSoftInput(focusView, InputMethodManager.SHOW_IMPLICIT);
			} else {
				manager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
