package com.tlz.shipper.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tlz.model.User;

public class ThemeActivity extends BaseActivity {

	public String getCurGoodsString()
	{
		return mApplication.getCurGoodsString();
	}
	public String[] getGoodsStringArray()
	{
		return mApplication.getGoodsStringArray();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


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
