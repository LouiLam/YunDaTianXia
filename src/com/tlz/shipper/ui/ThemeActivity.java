package com.tlz.shipper.ui;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tlz.model.User;
import com.tlz.utils.Flog;

public class ThemeActivity extends BaseActivity {

	private static final boolean DEBUG = false;

	private BaseApplication mApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Application app = getApplication();
		if (app instanceof BaseApplication) {
			mApplication = (BaseApplication) app;
		} else {
			if (DEBUG)
				Flog.e("Must extends KApplication or register in AndroidManifest.xml Application android:name!");
		}

		initDefaultActionBar();
	}

	protected void initDefaultActionBar() {
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	protected void initActionBarNoBackAndTwoPress() {
		mActionBar.setDisplayHomeAsUpEnabled(false);
		setPressTwoExitEnable();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// if (!Config.DEBUG) {
		// MobclickAgent.onResume(this);
		// }
		super.onResume();
	}

	@Override
	protected void onPause() {
		// if (!Config.DEBUG) {
		// MobclickAgent.onPause(this);
		// }
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public PendingIntent getDefalutIntent(int flags, String ssid) {
		Intent intent = new Intent();
		intent.putExtra("ssid", ssid);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
				intent, flags);
		return pendingIntent;
	}

	public User getUser() {
		if (mApplication != null) {
			return mApplication.getUser();
		}
		return new User();
	}

	public void setUser(User user) {
		if (mApplication != null) {
			mApplication.setUser(user);
		}
	}
}
