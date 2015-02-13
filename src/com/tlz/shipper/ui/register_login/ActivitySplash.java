package com.tlz.shipper.ui.register_login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.utils.FileUtils;
import com.tlz.utils.Flog;
import com.tlz.utils.ManifestUtils;
import com.tlz.utils.PrefsUtils;

public class ActivitySplash extends ThemeActivity {

	 private static final boolean DEBUG = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		
	}

	private void init() {
		mActionBar.hide();
		new SplashAsyncTask(this).execute(0);
		if (DEBUG) Flog.e("onAttachedToWindow()->init()");
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
	}
}

class SplashAsyncTask extends AsyncTask<Integer, Integer, String> {
	Activity act;

	private boolean isFirstRun() {
		int versionCode = PrefsUtils.getInt(act, "config", "version_code");
		if (versionCode != ManifestUtils.getAppVersionCode(act)) {
			PrefsUtils.removeKey(act, "config", "is_first_run");
		}

		boolean isFirstRun = PrefsUtils.getBoolean(act, "config",
				"is_first_run", true);
		if (isFirstRun) {
			return true;
		}
		return false;
	}

	public SplashAsyncTask(Activity act) {
		this.act = act;
	}

	@Override
	protected String doInBackground(Integer... params) {
		try {
			FileUtils.copyWeatherDb(act.getApplicationContext());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (isFirstRun()) {

			act.startActivity(new Intent(act, ActivityGuide.class));
			act.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);

			PrefsUtils.putValue(act, "config", "is_first_run", false);
			PrefsUtils.putValue(act, "config", "version_code",
					ManifestUtils.getAppVersionCode(act));
			act.finish();

		} else {
			act.startActivity(new Intent(act, ActivityIndex.class));
			act.finish();
		}
	}
}
