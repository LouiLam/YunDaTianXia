package com.tlz.shipper.ui.register_login;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.net.CommonApi;
import com.net.AppConfig;
import com.net.NetCommonMsgAsyncTask;
import com.net.NetShipperMsgAsyncTask;
import com.net.ShipperAccountApi;
import com.net.Urls;
import com.net.NetShipperMsgAsyncTask.APIListener;
import com.tlz.model.Myself;
import com.tlz.part0.other.SmsAdmin;
import com.tlz.part0.other.SmsAdmin.OnSmsListener;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.home.ActivityHome;
import com.tlz.shipper.ui.widget.EditTextBarIconTitleClearText;
import com.tlz.shipper.ui.widget.EditTextBarTitleClearTextAndLeftClick;
import com.tlz.shipper.ui.widget.ViewBar.TBFocusChangeVerifyListener;
import com.tlz.shipper.ui.widget.ViewBar.TBOnClickListener;
import com.tlz.utils.Flog;
import com.tlz.utils.ToastUtils;
import com.tlz.utils.VerifyUtils;

public class RegisterPhoneNumberActivity extends ThemeActivity implements
		OnSmsListener {

	private TextView getVerifyCodeBtn = null;

	private CountDownTimer mCutdownTimer = null;
	private int mRemainderTime = 60;
	View btn_verify;
	EditText et_pn, et_verify_code;
	private String code;
	String TokenCaptcha;
	EditTextBarTitleClearTextAndLeftClick textBar;
	EditTextBarIconTitleClearText textBar_pn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar.setTitle(R.string.register_phone_number_title);
		setContentView(R.layout.activity_register_phone_number);
		initView();
	}

	@Override
	protected void initView() {
		super.initView();
		textBar_pn = (EditTextBarIconTitleClearText) findViewById(R.id.register_phone_number_pn);
		textBar = (EditTextBarTitleClearTextAndLeftClick) findViewById(R.id.register_phone_number_verify_code);
		textBar.setTBOnClickListener(new TBOnClickListener() {

			@Override
			public void onClick() {
				new NetCommonMsgAsyncTask(
						new NetCommonMsgAsyncTask.APIListener() {

							@Override
							public String handler(CommonApi api) {
								return api.sendCaptcha(et_pn.getText()
										.toString());
							}

							@Override
							public void finish(String json) {
								Flog.e(json);
								try {
									JSONObject obj = new JSONObject(json);
									if (obj.getInt("resultCode") == 1) {
										JSONObject data = obj
												.getJSONObject("data");
										// ToastUtils.showCrouton(RegisterPhoneNumberActivity.this,
										// "服务测试阶段，验证码直接发送过来,您的验证码为:"+data.getString("code"));
										TokenCaptcha = data.getString("token");
										code = data.getString("code");
										textBar.setTBRightText(code);
										getVerifyCodeBtn
												.setText(getString(R.string.register_phone_number_verify_code_repeat));
										stopTimer();
									} else {
										ToastUtils
												.showCrouton(RegisterPhoneNumberActivity.this,
														getString(R.string.error)
																+ obj.getInt("resultCode"));

									}
								} catch (Exception e) {
									e.printStackTrace();
									ToastUtils
											.showCrouton(RegisterPhoneNumberActivity.this,
													getString(R.string.exception));
								}

							}
						}, RegisterPhoneNumberActivity.this)
						.execute(Urls.COMMON);
				startTimer();
			}
		});
		getVerifyCodeBtn = (TextView) textBar.findViewById(R.id.tb_left);
		textBar_pn
				.setTBFocusChangeVerifyListener(new TBFocusChangeVerifyListener() {

					@Override
					public boolean verify(String text, boolean hasFocus) {
						return (VerifyUtils.isPhoneNumber(text) || text
								.length() == 0);
					}
				});
		et_pn = (EditText) textBar_pn.findViewById(R.id.tb_right);
		et_verify_code = (EditText) textBar.findViewById(R.id.tb_right);
		et_pn.addTextChangedListener(watcher);
		et_verify_code.addTextChangedListener(watcher);
		btn_verify = findViewById(R.id.register_phone_number_btn_verify);
		btn_verify.setOnClickListener(this);
		btn_verify.setEnabled(false);
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
			if (VerifyUtils.isPhoneNumber(et_pn.getText().toString())
					&& et_verify_code.getText().toString().length() > 0) {
				btn_verify.setEnabled(true);
			} else {
				btn_verify.setEnabled(false);
			}
		}
	};

	@Override
	public void onClick(int viewId) {
		super.onClick(viewId);
		switch (viewId) {
		case R.id.register_phone_number_btn_verify:
			Myself.PhoneNumber = textBar_pn.getTBTextRight();
			if (AppConfig.DEBUG) {
				Intent intent = new Intent(RegisterPhoneNumberActivity.this,
						ActivityHome.class);
				intent.putExtra("isComeFromReg", true);
				startActivity(intent);

			} else {
				new NetShipperMsgAsyncTask(
						new NetShipperMsgAsyncTask.APIListener() {

							@Override
							public String handler(ShipperAccountApi api) {
								return api.checkShipperPhonePass(
										Myself.MemberId, et_pn.getText()
												.toString(), code);
							}

							@Override
							public void finish(String json) {
								Flog.e(json);
								try {
									JSONObject obj = new JSONObject(json);
									if (obj.getInt("resultCode") == 1) {
										login();
										// Intent intent = new Intent(
										// RegisterPhoneNumberActivity.this,
										// ActivityHome.class);
										// intent.putExtra("isComeFromReg",
										// true);
										// startActivity(intent);
									} else {
										try {
											String error = obj.getString("error");
											ToastUtils
													.showCrouton(
															RegisterPhoneNumberActivity.this,
															error
																	+ ":"
																	+ obj.getInt("resultCode"));
										} catch (Exception e) {
											ToastUtils
													.showCrouton(
															RegisterPhoneNumberActivity.this,
															getString(R.string.verify_error)
																	+ obj.getInt("resultCode"));
										}

									}
								} catch (Exception e) {
									e.printStackTrace();
									ToastUtils
											.showCrouton(
													RegisterPhoneNumberActivity.this,
													getString(R.string.verify_exception));
								}

							}
						}, RegisterPhoneNumberActivity.this)
						.execute(Urls.REGEDIT + ";jsessionid=" + TokenCaptcha);

			}
			break;
		default:
			break;
		}
	}

	// @Override
	// public void onDestroy() {
	// super.onDestroy();
	// }
	//
	// @Override
	// protected void onPause() {
	// SmsAdmin.getInstance(this).stopMonitor();
	// super.onPause();
	// }
	//
	// @Override
	// protected void onResume() {
	// SmsAdmin.getInstance(this).startMonitor("10", "验证码", this);
	// super.onResume();
	// }

	private void startTimer() {
		mRemainderTime = 60;
		if (mCutdownTimer == null) {
			mCutdownTimer = new CountDownTimer(60 * 1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					mRemainderTime--;
					getVerifyCodeBtn.setText(mRemainderTime + "秒");
				}

				@Override
				public void onFinish() {
					mRemainderTime = 0;
					getVerifyCodeBtn
							.setText(getString(R.string.register_phone_number_verify_code_repeat));
					textBar.setTBTitleEnable();
					stopTimer();
				}
			};
			mCutdownTimer.start();
		}
	}

	private void stopTimer() {
		if (mCutdownTimer != null) {
			mCutdownTimer.cancel();
			mCutdownTimer = null;
		}
	}

	@Override
	public void onSmsReceived(String text) {
		if (TextUtils.isEmpty(text))
			return;

		int index = text.indexOf("验证码:");
		String verificationCode = text.substring(index + 4, index + 4 + 6);
		ToastUtils.show(this, "收到短信验证：" + verificationCode);
		// textBar.setTBRightText(verificationCode);
	}

	private void login() {
		new NetShipperMsgAsyncTask(new APIListener() {
			// {
			// resultCode:1 :正确|-1:操作失败,0:用户名或密码有误
			// data:
			// {
			// token:令牌
			// memeber:
			// { memberId:12, phone:"会员手机号" loginName:"登陆名"
			// creditGrad:(int)信誉等级 balance:(float)帐户余额 },
			// shipper:
			// { shipperId:(int)主键 head:"头像url" auditStatus:(int)认证状态
			// locationCode :"所在地CODE" simpleName:"企业简称" fullName:"企业全称"
			// detailAddress:"详细地址" contact:"联系人" phone:"联系电话"
			// introduce:"企业简介" cargoType:"主要运送货品" qrCode:"二维码明片url" }
			// }
			// }
			@Override
			public String handler(ShipperAccountApi api) {
				return api.login(Myself.UserName, Myself.Password,
						AppConfig.TYPE_ANDROID);
			}

			@Override
			public void finish(String json) {
				Flog.e(json);
				try {
					JSONObject obj = new JSONObject(json);
					if (obj.getInt("resultCode") == 1) {
						JSONObject data = obj.getJSONObject("data");
						Myself.Token = data.getString("token");
						JSONObject memeber = data.getJSONObject("member");
						JSONObject shipper = data.getJSONObject("shipper");
						Myself.MemberId = memeber.getInt("memberId");
						Myself.PhoneNumber = memeber.getString("phone");
						Myself.UserName = memeber.getString("loginName");
						Myself.CreditGrad = memeber.getInt("creditGrad");
						Myself.Balance = memeber.getDouble("balance");

						Myself.ShipperId = shipper.getInt("shipperId");
						Myself.HeadIconUrl = shipper.getString("head");
						Myself.AuditStatus = shipper.getInt("auditStatus");
						Myself.Location = shipper.getString("locationCode");
						Myself.UserName = shipper.getString("simpleName");
						Myself.FullName = shipper.getString("fullName");
						Myself.DetailAddress = shipper.getString("detailAddress");
						Myself.ContactName = shipper.getString("contact");
						Myself.PhoneNumber = shipper.getString("phone");
						Myself.Introduction = shipper.getString("introduce");
						Myself.CargoType = (byte) shipper.getInt("cargoType");
						Myself.QRUrl = shipper.getString("qrCode");
						Intent intent = new Intent(
								RegisterPhoneNumberActivity.this,
								ActivityHome.class);
						intent.putExtra("isComeFromReg", true);
						startActivity(intent);
					} else {

						try {
							String error = obj.getString("error");
							ToastUtils.showCrouton(
									RegisterPhoneNumberActivity.this, error
											+ ":" + obj.getInt("resultCode"));
						} catch (Exception e) {
							ToastUtils.showCrouton(
									RegisterPhoneNumberActivity.this,
									getString(R.string.error)
											+ obj.getInt("resultCode"));
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showCrouton(
							RegisterPhoneNumberActivity.this,
							getString(R.string.exception)
									+ e.getMessage());
				}

			}
		}, this).execute(Urls.LOGIN);
	}
}
