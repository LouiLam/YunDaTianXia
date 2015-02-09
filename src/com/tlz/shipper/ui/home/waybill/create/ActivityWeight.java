package com.tlz.shipper.ui.home.waybill.create;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.EditTextBarIconTitleClearText;
import com.tlz.shipper.ui.widget.ViewBar.TBFocusChangeVerifyListener;
import com.tlz.shipper.ui.widget.ViewBar.TBOnTextChangedListener;
import com.tlz.utils.VerifyUtils;

public class ActivityWeight extends ThemeActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waybill_weight);
		mActionBar.setTitle(R.string.waybill_weight_title);
		initView();
	}

	EditTextBarIconTitleClearText weight, volume, quantity;
	TextView tv;
	TBFocusChangeVerifyListener listener = new TBFocusChangeVerifyListener() {

		@Override
		public boolean verify(String text, boolean hasFocus) {
			return (VerifyUtils.isNumber(text) || text.length() == 0);
		}
	};
	TBOnTextChangedListener listener1 = new TBOnTextChangedListener() {

		@Override
		public void afterTextChanged(Editable s) {
			if (VerifyUtils.isNumber(weight.getTBTextRight())
					&& VerifyUtils.isNumber(volume.getTBTextRight())
					&& VerifyUtils.isNumber(quantity.getTBTextRight())) {
				tv.setEnabled(true);
			}
			else
			{
				tv.setEnabled(false);
			}
		}
	};

	@Override
	protected void initView() {
		super.initView();
		weight = (EditTextBarIconTitleClearText) findViewById(R.id.waybill_weight);
		weight.setTBFocusChangeVerifyListener(listener);
		 weight.setTBOnTextChangedListener(listener1);
		volume = (EditTextBarIconTitleClearText) findViewById(R.id.waybill_volume);
		volume.setTBFocusChangeVerifyListener(listener);
		 volume.setTBOnTextChangedListener(listener1);
		quantity = (EditTextBarIconTitleClearText) findViewById(R.id.waybill_quantity);
		quantity.setTBFocusChangeVerifyListener(listener);
		 quantity.setTBOnTextChangedListener(listener1);
		tv = (TextView) findViewById(R.id.waybill_btn_save);
		tv.setEnabled(false);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Myself.Weight = Integer.parseInt(weight.getTBTextRight());
				Myself.Volume = Integer.parseInt(volume.getTBTextRight());
				Myself.Quantity = Integer.parseInt(quantity.getTBTextRight());
				Intent intent = new Intent();
				Myself.WeightString= String.format("%d吨/%d立方/%d件",
						Myself.Weight, Myself.Volume, Myself.Quantity);
				intent.putExtra("content",Myself.WeightString);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}
