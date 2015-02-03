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
import com.net.Urls;
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


public class RegisterPhoneNumberActivity extends ThemeActivity implements OnSmsListener {




	private TextView  getVerifyCodeBtn = null;

	private CountDownTimer mCutdownTimer = null;
	private int mRemainderTime = 60;
	View btn_verify;
	EditText et_pn,et_verify_code;
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
		textBar_pn=(EditTextBarIconTitleClearText) findViewById(R.id.register_phone_number_pn);
		textBar=(EditTextBarTitleClearTextAndLeftClick) findViewById(R.id.register_phone_number_verify_code);
		textBar.setTBOnClickListener(new TBOnClickListener() {
			
			@Override
			public void onClick() {
				new NetCommonMsgAsyncTask(new NetCommonMsgAsyncTask.APIListener() {
					
					@Override
					public String handler(CommonApi api) {
						return api.sendCaptcha(et_pn.getText().toString());
					}
					
					@Override
					public void finish(String json) {
						Flog.e(json);
						try {
							JSONObject obj = new JSONObject(json);
							if (obj.getInt("resultCode") == 1) {
								ToastUtils.show(RegisterPhoneNumberActivity.this, "服务测试阶段，验证码直接发送过来,您的验证码为:"+obj.getString("data"));
								textBar.setTBRightText(obj.getString("data"));
								stopTimer();
							} else {
								ToastUtils.show(RegisterPhoneNumberActivity.this,
										getString(R.string.register_error));
								
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.show(RegisterPhoneNumberActivity.this,
									getString(R.string.register_exception));
						}
						
						
					}
				}, RegisterPhoneNumberActivity.this).execute(Urls.COMMON);
				startTimer();
			}
		});
		getVerifyCodeBtn=(TextView) textBar.findViewById(R.id.tb_left);
		textBar_pn.setTBFocusChangeVerifyListener(new TBFocusChangeVerifyListener() {
			
			@Override
			public boolean verify(String text, boolean hasFocus) {
				return (VerifyUtils.isPhoneNumber(text)||text.length()==0);
			}
		});
		et_pn=(EditText) textBar_pn.findViewById(R.id.tb_right);
		et_verify_code=(EditText) textBar.findViewById(R.id.tb_right);
		et_pn.addTextChangedListener(watcher);
		et_verify_code.addTextChangedListener(watcher);
		btn_verify=findViewById(R.id.register_phone_number_btn_verify);
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
			if(VerifyUtils.isPhoneNumber(et_pn.getText().toString())&&et_verify_code.getText().toString().length()>0)
			{
				btn_verify.setEnabled(true);
			}
			else
			{
				btn_verify.setEnabled(false);
			}
		}
	};
	@Override
	public void onClick(int viewId) {
		super.onClick(viewId);
		switch (viewId) {
		case R.id.register_phone_number_btn_verify:
			Myself.PhoneNumber=textBar_pn.getTBTextRight();
			if(AppConfig.DEBUG)
			{
				Intent  intent=new Intent(RegisterPhoneNumberActivity.this,ActivityHome.class);
				intent.putExtra("isComeFromReg", true);
				startActivity(intent);
				
			}
			else
			{
				Intent  intent=new Intent(RegisterPhoneNumberActivity.this,ActivityHome.class);
				intent.putExtra("isComeFromReg", true);
				startActivity(intent);
			}
//			Intent intent = new Intent(RegisterPhoneNumberActivity.this,
//					WebContentActivity.class);
//			intent.putExtra("title",
//					getString(R.string.register_view_agree_protocol2));
//			intent.putExtra("url", Urls.USER_SERVICE_PROTOCOL_URL);
//			startActivity(intent);
			break;
		default:
			break;
		}
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_OK && requestCode == 0) {
//			location_et.setText(data.getStringExtra("myLocation"));
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//
//	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		SmsAdmin.getInstance(this).stopMonitor();
		super.onPause();
	}

	@Override
	protected void onResume() {
		SmsAdmin.getInstance(this).startMonitor("10", "验证码", this);
		super.onResume();
	}




	private void startTimer() {
		mRemainderTime = 60;
		if (mCutdownTimer == null) {
			mCutdownTimer = new CountDownTimer(60* 1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					mRemainderTime--;
					getVerifyCodeBtn.setText(mRemainderTime+"秒");
//					int remainTime = (int) (millisUntilFinished / 1000 - 540);
//					if (remainTime > 0) {
//						mBtnReSend.setText("重新发送 "
//								+ (millisUntilFinished / 1000 - 540));
//						mBtnReSend.setEnabled(false);
//					} else {
//						mBtnReSend.setText("重新发送 ");
//						mBtnReSend.setEnabled(true);
//					}
				}

				@Override
				public void onFinish() {
					mRemainderTime = 0;
					getVerifyCodeBtn.setText("重新发送 ");
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
		ToastUtils.show(this, "收到短信验证："+verificationCode);
//		mInputVerificationCode.setText(verificationCode);
	}


}
