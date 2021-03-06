package com.tlz.shipper.ui.register_login;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.net.AppConfig;
import com.net.NetAsyncFactory;
import com.net.NetAsyncFactory.ResultCodeSucListener;
import com.net.ShipperAccountApi;
import com.net.Urls;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.home.ActivityHome;
import com.tlz.shipper.ui.widget.EditTextBarIconTitleClearText;
import com.tlz.shipper.ui.widget.EditTextBarIconTitlePassword;
import com.tlz.shipper.ui.widget.TextViewBarIcon;
import com.tlz.utils.VerifyUtils;

public class ActivityLogin extends ThemeActivity {
	EditText username, pwd;
	View login_view_btn;
	private TextView mErrorNetTips;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mActionBar.setTitle(R.string.login_title);
		initView();
	}

	TextViewBarIcon iconTextBar;

	@Override
	protected void initView() {
		super.initView();
		EditTextBarIconTitleClearText usernameBar = (EditTextBarIconTitleClearText) findViewById(R.id.login_username);
		EditTextBarIconTitlePassword loginPwdBar = (EditTextBarIconTitlePassword) findViewById(R.id.login_pwd);
		username = (EditText) usernameBar.findViewById(R.id.tb_right);
		username.removeTextChangedListener(watcher);
		username.addTextChangedListener(watcher);
		pwd = (EditText) loginPwdBar.findViewById(R.id.tb_right);
		pwd.removeTextChangedListener(watcher);
		pwd.addTextChangedListener(watcher);
		login_view_btn = findViewById(R.id.login_btn);
		login_view_btn.setOnClickListener(this);
		login_view_btn.setEnabled(false);
		TextView tip = (TextView) this.findViewById(R.id.login_tip_bottom);
		tip.setText(Html.fromHtml(getString(R.string.login_tip_bottom0)
				+ String.format("<font color='#09a6ca'>%s</font>",
						getString(R.string.login_tip_bottom1))));
		tip.setOnClickListener(this);
		mErrorNetTips = (TextView) findViewById(R.id.tv_error_tips);
		mErrorNetTips.setOnClickListener(this);
		mErrorNetTips.setVisibility(View.GONE);
	}
	private void skipUI()
	{
		Intent  intent=new Intent(ActivityLogin.this,ActivityHome.class);
		intent.putExtra("isComeFromReg", false);
		startActivity(intent);
	}
	public void onClick(int viewId) {

		switch (viewId) {
		case R.id.tv_error_tips:
			skipUI();
			break;
		case R.id.login_tip_bottom:
			startActivity(new Intent(ActivityLogin.this, ActivityRegister.class));
			break;
		case R.id.login_btn:
			Myself.UserName = username.getText().toString();
			Myself.Password = pwd.getText().toString();
			if (AppConfig.DEBUG) {
				skipUI();
			} else {
				NetAsyncFactory.createShipperTask(this, new ResultCodeSucListener<ShipperAccountApi>() {
					
					@Override
					public void suc(JSONObject obj) throws JSONException {

						JSONObject data = obj.getJSONObject("data");
						Myself.Token = data.getString("token");
						JSONObject memeber = data.getJSONObject("member");
						JSONObject shipper = data
								.getJSONObject("shipper");
						Myself.MemberId = memeber.getInt("memberId");
						Myself.PhoneNumber = memeber.getString("phone");
						Myself.UserName = memeber
								.getString("loginName");
						Myself.CreditGrad = memeber
								.getInt("creditGrad");
						Myself.Balance = memeber.getDouble("balance");

						Myself.ShipperId = shipper.getInt("shipperId");
						Myself.HeadIconUrl = shipper.getString("head");
						Myself.AuditStatus = shipper
								.getInt("auditStatus");
						Myself.Location = shipper
								.getString("locationCode");
						Myself.UserName = shipper
								.getString("simpleName");
						Myself.FullName = shipper.getString("fullName");
						Myself.DetailAddress = shipper
								.getString("detailAddress");
						Myself.ContactName = shipper
								.getString("contact");
						Myself.PhoneNumber = shipper.getString("phone");
						Myself.Introduction = shipper
								.getString("introduce");
						Myself.CargoType = (byte) shipper.getInt("cargoType");
						Myself.QRUrl = shipper.getString("qrCode");
						skipUI();
					
					}
					
					@Override
					public String handler(ShipperAccountApi api) {
						return api.login(Myself.UserName, Myself.Password,
								 AppConfig.TYPE_ANDROID);
					}
				}).execute(Urls.LOGIN);
				
			}

			break;
		default:
			break;
		}

	};

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
			if(mErrorNetTips.getVisibility()==View.VISIBLE)
			{mErrorNetTips.setVisibility(View.GONE);}
			if (VerifyUtils.isPwd(pwd.getText().toString())
					&& pwd.getText().length() > 0
					&& username.getText().length() > 0) {
				login_view_btn.setEnabled(true);
			} else {
				login_view_btn.setEnabled(false);
			}
		}
	};
}
