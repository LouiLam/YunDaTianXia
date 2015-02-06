package com.tlz.shipper.ui;

import java.util.List;
import java.util.Stack;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.baidu.mapapi.SDKInitializer;
import com.tlz.model.User;
import com.tlz.utils.CrashHandler;
import com.tlz.utils.Flog;

public class BaseApplication extends Application {

	private static final boolean DEBUG = true;

	public static BaseApplication mApplication;

	private Stack<BaseActivity> mActivityStack = new Stack<BaseActivity>();

	public int mScreenWidth;
	public int mScreenHeight;
	public float mDensity;
	public int mAndroidVersion;
	private JasonActivityLifecycleCallbacks mActivityLifecycleCallbacks;
	private boolean mIsMonitorAppRunningBackground = false;
	private boolean mIsAppRunningForground = false;

	// public BDLocation mLocation;
	private User mUser;

	@Override
	public void onCreate() {
		super.onCreate();
		if (DEBUG)
			Flog.e("BaseApplication onCreate");
		init();
		SDKInitializer.initialize(this);
	}

	@TargetApi(14)
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
	}

	@TargetApi(14)
	private void init() {
		mScreenWidth = getResources().getDisplayMetrics().widthPixels;
		mScreenHeight = getResources().getDisplayMetrics().heightPixels;
		mDensity = getResources().getDisplayMetrics().density;
		mAndroidVersion = Build.VERSION.SDK_INT;

		mApplication = this;
		CrashHandler.startMonitor(getApplicationContext());

		if (mAndroidVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (mActivityLifecycleCallbacks == null) {
				mActivityLifecycleCallbacks = new JasonActivityLifecycleCallbacks();
			}
			registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
		}
	}

	public void addActivity(BaseActivity a) {
		if (a == null)
			return;
		mActivityStack.add(a);
	}

	public void removeActivity(BaseActivity a) {
		if (a == null)
			return;

		if (mActivityStack.contains(a)) {
			mActivityStack.remove(a);
		}
	}

	/**
	 * 退出应用
	 */
	public void exit() {
		closeAllActivities();
		// Process.killProcess(Process.myPid());
		unregisterActivityLifecycleCallbacks();
		System.exit(0);
	}

	@TargetApi(14)
	private void unregisterActivityLifecycleCallbacks() {
		if (mActivityLifecycleCallbacks != null) {
			unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
			mActivityLifecycleCallbacks = null;
		}
	}


	private void closeAllActivities() {
		if (mActivityStack.empty())
			return;
		BaseActivity s[]=mActivityStack.toArray(new BaseActivity[0]);
		for (BaseActivity a : s) {
			if (a == null || a.isFinishing())
				continue;
			ActivityCompat.finishAffinity(a);
		}
		mActivityStack.clear();
	}

	/**
	 * 设置是否监听App运行在后台
	 * 
	 * @param enabled
	 */
	protected void setMonitorAppRunningBackgroundEnabled(boolean enabled) {
		mIsMonitorAppRunningBackground = enabled;
	}

	@TargetApi(14)
	class JasonActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

		@Override
		public void onActivityCreated(Activity activity,
				Bundle savedInstanceState) {
		}

		@Override
		public void onActivityStarted(Activity activity) {
		}

		@Override
		public void onActivityResumed(Activity activity) {
			if (!mIsMonitorAppRunningBackground)
				return;
			if (isAppRunningForeground() && !mIsAppRunningForground) {
				mIsAppRunningForground = true;
				onAppRunningForground();
			}
		}

		@Override
		public void onActivityPaused(Activity activity) {
		}

		@Override
		public void onActivityStopped(Activity activity) {
			if (!mIsMonitorAppRunningBackground)
				return;
			if (!isAppRunningForeground()) {
				mIsAppRunningForground = false;
				onAppRunningBackground();
			}
		}

		@Override
		public void onActivitySaveInstanceState(Activity activity,
				Bundle outState) {
		}

		@Override
		public void onActivityDestroyed(Activity activity) {
		}

	}

	private boolean isAppRunningForeground() {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getPackageName();

		List<RunningAppProcessInfo> processList = activityManager
				.getRunningAppProcesses();
		if (processList == null)
			return false;

		for (RunningAppProcessInfo info : processList) {
			if (info.processName.equals(packageName)
					&& info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	protected void onAppRunningBackground() {
	}

	protected void onAppRunningForground() {
	}

	public User getUser() {
		return mUser == null ? new User() : mUser;
	}

	public void setUser(User user) {
		mUser = user;
	}
}
