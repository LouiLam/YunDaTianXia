package com.tlz.shipper.ui.register_login;

import android.content.Intent;
import android.os.Bundle;

import com.tlz.part0.other.AcitivityLocationDemo;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.utils.Flog;

public class ActivityIndex extends ThemeActivity {

	private static final boolean DEBUG = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		initActionBarNoBackAndTwoPress();
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
		findViewById(R.id.index_btn_reg).setOnClickListener(this);
		findViewById(R.id.index_btn_login).setOnClickListener(this);
		if (DEBUG)
			Flog.e("onAttachedToWindow()->init()");
	}

	@Override
	public void onClick(int viewId) {
		switch (viewId) {
		case R.id.index_btn_reg:
			startActivity(new Intent(this, ActivityRegister.class));
//			startActivity(new Intent(this, AcitivityLocationDemo.class));
//			startActivity(new Intent(this, ActivityWalbillDetails.class));
			
			break;
		case R.id.index_btn_login:
			startActivity(new Intent(this, ActivityLogin.class));
			break;
		default:
			break;
		}
		super.onClick(viewId);
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
