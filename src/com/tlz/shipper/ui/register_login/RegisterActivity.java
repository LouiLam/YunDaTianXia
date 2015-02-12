package com.tlz.shipper.ui.register_login;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.net.AppConfig;
import com.net.NetShipperMsgAsyncTask;
import com.net.NetShipperMsgAsyncTask.APIListener;
import com.net.ShipperAccountApi;
import com.net.Urls;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.WebContentActivity;
import com.tlz.shipper.ui.widget.EditTextBarIconTitleBtn;
import com.tlz.shipper.ui.widget.EditTextBarIconTitleClearText;
import com.tlz.shipper.ui.widget.EditTextBarIconTitlePassword;
import com.tlz.shipper.ui.widget.ViewBar.TBBarOnClickListener;
import com.tlz.shipper.ui.widget.ViewBar.TBFocusChangeVerifyListener;
import com.tlz.utils.Flog;
import com.tlz.utils.HandlerMsg;
import com.tlz.utils.IntentUtils;
import com.tlz.utils.PhoneUtils;
import com.tlz.utils.ToastUtils;
import com.tlz.utils.VerifyUtils;

public class RegisterActivity extends ThemeActivity {

	private TextView mErrorNetTips;
	private CountDownTimer mCheckNetTimer;
	EditText enterprise_et, location_et, pwd_et, pwd_conf_et;
	View reg_view_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar.setTitle(R.string.register_title);
		setContentView(R.layout.activity_register);
		initView();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case HandlerMsg.LocationSuc:
			BDLocation location = (BDLocation) msg.obj;

			Myself.Location = String.format("%s %s %s", location.getProvince(),
					location.getCity(), location.getDistrict());
			location_et.setText(Myself.Location);
			break;

		default:
			break;
		}
		return super.handleMessage(msg);
	}

	@Override
	protected void initView() {
		super.initView();
		EditTextBarIconTitleBtn view = (EditTextBarIconTitleBtn) findViewById(R.id.register_area);
		location_et = (EditText) view.findViewById(R.id.tb_right);
		EditTextBarIconTitleClearText viewbar_enterprise = (EditTextBarIconTitleClearText) findViewById(R.id.register_enterprise_simple_name);
		initLocation();
		view.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(RegisterActivity.this,
						LocationActivity.class);
				intent.putExtra("myLocation", location_et.getText().toString());
				startActivityForResult(intent, 0);

			}
		});
		EditTextBarIconTitlePassword passwordView = (EditTextBarIconTitlePassword) findViewById(R.id.register_pwd);
		passwordView
				.setTBFocusChangeVerifyListener(new TBFocusChangeVerifyListener() {

					@Override
					public boolean verify(String text, boolean hasFocus) {
						return (VerifyUtils.isPwd(text) || text.length() == 0);
					}
				});
		EditTextBarIconTitlePassword passwordConfView = (EditTextBarIconTitlePassword) findViewById(R.id.register_conf_pwd);
		passwordConfView
				.setTBFocusChangeVerifyListener(new TBFocusChangeVerifyListener() {

					@Override
					public boolean verify(String text, boolean hasFocus) {
						return (VerifyUtils.isPwd(text) || text.length() == 0);
					}
				});
		enterprise_et = (EditText) viewbar_enterprise
				.findViewById(R.id.tb_right);
		pwd_et = (EditText) passwordView.findViewById(R.id.tb_right);
		pwd_conf_et = (EditText) passwordConfView.findViewById(R.id.tb_right);
		pwd_et.addTextChangedListener(watcher);
		pwd_conf_et.addTextChangedListener(watcher);
		enterprise_et.addTextChangedListener(watcher);
		TextView protocol = (TextView) this
				.findViewById(R.id.register_btn_agree_protocol);
		protocol.setText(Html
				.fromHtml(getString(R.string.register_agree_protocol1)
						+ String.format("<font color='#09a6ca'>%s</font>",
								getString(R.string.register_agree_protocol2))));
		protocol.setOnClickListener(this);
		mErrorNetTips = (TextView) findViewById(R.id.tv_error_net_tips);
		mErrorNetTips.setOnClickListener(this);
		reg_view_btn = findViewById(R.id.register_btn_reg);
		reg_view_btn.setOnClickListener(this);
		reg_view_btn.setEnabled(false);
	}

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (pwd_et.getText().toString()
					.equals(pwd_conf_et.getText().toString())
					&& enterprise_et.getText().length() > 0) {
				if (VerifyUtils.isPwd(pwd_et.getText().toString())
						&& enterprise_et.getText().length() > 0) {
					reg_view_btn.setEnabled(true);
				}
			} else {
				reg_view_btn.setEnabled(false);
			}
		}
	};

	@Override
	public void onClick(int viewId) {
		super.onClick(viewId);
		switch (viewId) {
		case R.id.tv_error_net_tips:
			IntentUtils.startMainActivity(this, "com.android.settings");
			break;
		case R.id.register_btn_agree_protocol:
			Intent intent = new Intent(RegisterActivity.this,
					WebContentActivity.class);
			intent.putExtra("title",
					getString(R.string.register_agree_protocol2));
			intent.putExtra("url", Urls.USER_SERVICE_PROTOCOL_URL);
			startActivity(intent);
			break;
		case R.id.register_btn_reg:
			Myself.UserName = enterprise_et.getText().toString();
			Myself.Location = location_et.getText().toString();
			Myself.Password = pwd_et.getText().toString();
			if (AppConfig.DEBUG) {
				startActivity(new Intent(RegisterActivity.this,
						RegisterPhoneNumberActivity.class));
			} else {
				new NetShipperMsgAsyncTask(new APIListener() {

					@Override
					public String handler(ShipperAccountApi api) {
						return api.regist(Myself.UserName, Myself.Location,
								Myself.Password);

					}

					@Override
					public void finish(String json) {
						Flog.e(json);
						try {
							JSONObject obj = new JSONObject(json);
							JSONObject data=obj.getJSONObject("data");
							if (obj.getInt("resultCode") == 1) {
								Myself.Token = data
										.getString("token");
								Myself.MemberId=data
										.getInt("memberId");
								startActivity(new Intent(RegisterActivity.this,
										RegisterPhoneNumberActivity.class));
							} else {
								try {
									String error=obj.getString("error");
									ToastUtils.showCrouton(RegisterActivity.this,
											error+":"+obj.getInt("resultCode"));
								} catch (Exception e) {
									ToastUtils.showCrouton(RegisterActivity.this,
											getString(R.string.error)+obj.getInt("resultCode"));
								}
								
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCrouton(RegisterActivity.this,
									getString(R.string.exception));
						}

					}
				}, this).execute(Urls.REGEDIT);

			
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 0) {
			location_et.setText(data.getStringExtra("myLocation"));
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onResume() {
		if (PhoneUtils.isNetworkAvailable(this)) {
			mErrorNetTips.setVisibility(View.GONE);
		} else {
			mErrorNetTips.setVisibility(View.VISIBLE);
			startCheckNetTimer();
		}
		super.onResume();
	}

	private void startCheckNetTimer() {
		mCheckNetTimer = new CountDownTimer(1000 * 5, 1000 * 5) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				if (mErrorNetTips.getVisibility() == View.GONE) {
					cancelCheckNetTimer();
				}
				if (PhoneUtils.isNetworkAvailable(RegisterActivity.this)) {
					mErrorNetTips.setVisibility(View.GONE);
				} else {
					startCheckNetTimer();
					// mErrorNetTips.setVisibility(View.VISIBLE);
				}
			}
		};
		mCheckNetTimer.start();
	}

	private void cancelCheckNetTimer() {
		if (mCheckNetTimer != null)
			mCheckNetTimer.cancel();
		mCheckNetTimer = null;
	}

}
