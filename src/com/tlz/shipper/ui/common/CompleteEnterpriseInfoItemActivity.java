package com.tlz.shipper.ui.common;

import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.net.AppConfig;
import com.net.NetShipperMsgAsyncTask;
import com.net.ShipperAccountApi;
import com.net.Urls;
import com.net.NetShipperMsgAsyncTask.APIListener;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.EditTextBarPureClearText;
import com.tlz.shipper.ui.widget.ViewBar.TBOnTextChangedListener;
import com.tlz.utils.Flog;
import com.tlz.utils.ToastUtils;

public class CompleteEnterpriseInfoItemActivity extends ThemeActivity {
	EditTextBarPureClearText itemBar;
	private TextView save_btn;
	private int curItemType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_enterprise_info_item);
		initView();
	}

	int maxLengths[] = new int[10];

	@Override
	protected void initView() {
		super.initView();
		Resources res = getResources();
		maxLengths[CompleteEnterpriseInfoActivity.IEMT_TYPE_FULL_NAME] = res
				.getInteger(R.integer.enterprise_full_name);
		maxLengths[CompleteEnterpriseInfoActivity.IEMT_TYPE_CONTACT_NAME] = res
				.getInteger(R.integer.location_detail);
		Bundle bundle = getIntent().getExtras();
		LayoutParams params = new ActionBar.LayoutParams((int) getResources()
				.getDimension(R.dimen.custom_actionbar_icon_width),
				(int) getResources().getDimension(
						R.dimen.custom_actionbar_icon_height), Gravity.RIGHT);
		params.rightMargin = (int) getResources().getDimension(
				R.dimen.custom_actionbar_icon_margin);
		mActionBar.setTitle(bundle.getString("title"));
		itemBar = (EditTextBarPureClearText) findViewById(R.id.register_details_item);
		itemBar.setTBTextTopHint(bundle.getString("hint_top"));
		itemBar.setTBTextBottom(bundle.getString("hint_bottom"));
		itemBar.setMaxLengthTop(maxLengths[curItemType]);
		itemBar.setTBOnTextChangedListener(new TBOnTextChangedListener() {
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 0)
				{
					save_btn.setEnabled(true);
				}
			}
		});
		curItemType = bundle.getInt("item_type");
		save_btn = (TextView) LayoutInflater.from(this).inflate(
				R.layout.actionbar_custom_item, null);
		save_btn.setText(getString(R.string.register_save));
		save_btn.setEnabled(false);
		save_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("item_type", curItemType);
				bundle.putString("item_type_value", itemBar.getTBText());
				data.putExtras(bundle);
				switch (curItemType) {
				case CompleteEnterpriseInfoActivity.IEMT_TYPE_FULL_NAME:
					Myself.FullName = itemBar.getTBText();
					break;
				case  CompleteEnterpriseInfoActivity.IEMT_TYPE_CONTACT_NAME:
					Myself.ContactName = itemBar.getTBText();
					break;
				case CompleteEnterpriseInfoActivity.IEMT_TYPE_DETAIL_ADDRESS:
					Myself.DetailAddress = itemBar.getTBText();
					break;
				default:
					break;
				}
				netComm(data);
			}
		});
		mActionBar.setCustomView(save_btn, params);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

private void  netComm(final Intent data)
{
	if (AppConfig.DEBUG) {
		setResult(RESULT_OK, data);
		finish();
	} else {
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
				return api.completeData(Myself.ShipperId, Myself.FullName, Myself.DetailAddress, Myself.ContactName,  Myself.Introduction,  Myself.CargoType);
			}

			@Override
			public void finish(String json) {
				Flog.e(json);
				try {
					JSONObject obj = new JSONObject(json);
					if (obj.getInt("resultCode") == 1) {
						setResult(RESULT_OK, data);
						CompleteEnterpriseInfoItemActivity.this.finish();
					}
					else {
						try {
							String error=obj.getString("error");
							ToastUtils.showCrouton(CompleteEnterpriseInfoItemActivity.this,
									error+":"+obj.getInt("resultCode"));
						} catch (Exception e) {
							ToastUtils.showCrouton(CompleteEnterpriseInfoItemActivity.this,
									getString(R.string.error)+obj.getInt("resultCode"));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showCrouton(
							CompleteEnterpriseInfoItemActivity.this,
							getString(R.string.exception)
									+ e.getMessage());
				}

			}
		}, this).execute(Urls.REGEDIT);
	}}
}
