package com.tlz.shipper.ui.register_login;

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

import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.EditTextBarPureClearText;
import com.tlz.shipper.ui.widget.ViewBar.TBOnTextChangedListener;

public class RegisterDetailsItemActivity extends ThemeActivity {
	EditTextBarPureClearText itemBar;
	private TextView save_btn;
	private int curItemType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_details_item);
		initView();
	}

	int maxLengths[] = new int[10];

	@Override
	protected void initView() {
		super.initView();
		Resources res = getResources();
		maxLengths[RegisterDetailsActivity.IEMT_TYPE_FULL_NAME] = res
				.getInteger(R.integer.enterprise_full_name);
		maxLengths[RegisterDetailsActivity.IEMT_TYPE_CONTACT_NAME] = res
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
				case RegisterDetailsActivity.IEMT_TYPE_FULL_NAME:
					Myself.FullName = itemBar.getTBText();
					break;
				case  RegisterDetailsActivity.IEMT_TYPE_CONTACT_NAME:
					Myself.ContactName = itemBar.getTBText();
					break;
				case RegisterDetailsActivity.IEMT_TYPE_LOCATION_DETAIL:
					Myself.LocationDetail = itemBar.getTBText();
					break;
				default:
					break;
				}
				setResult(RESULT_OK, data);
				finish();
			}
		});
		mActionBar.setCustomView(save_btn, params);
		mActionBar.setDisplayShowCustomEnabled(true);
	}


}
