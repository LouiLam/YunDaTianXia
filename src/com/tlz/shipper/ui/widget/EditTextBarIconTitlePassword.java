package com.tlz.shipper.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.tlz.shipper.R;

public class EditTextBarIconTitlePassword extends ViewBar {
	EditText et_right,et_left;
	View iconRight;
	Drawable background_invisible, background_visible;
	boolean mChecked;

	@SuppressWarnings("deprecation")
	public EditTextBarIconTitlePassword(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EditTextBarIconTitlePassword, 0, 0);
		LayoutInflater.from(getContext()).inflate(
				R.layout.item_title_edittext_bar, this, true);

		final float default_icon_margin = getResources().getDimension(
				R.dimen.default_icon_margin);
		int default_maxLength = getResources().getInteger(R.integer.maxLength);
		String strLeft = a
				.getString(R.styleable.EditTextBarIconTitlePassword_left_text);
		String strRightHint = a
				.getString(R.styleable.EditTextBarIconTitlePassword_right_hint);
		int maxLength = a.getInt(
				R.styleable.EditTextBarIconTitlePassword_right_maxLength,
				default_maxLength);
		int icon_margin = (int) a.getDimension(
				R.styleable.EditTextBarIconTitlePassword_icon_margin,
				default_icon_margin);
		boolean isGone=a.getBoolean(R.styleable.EditTextBarIconTitlePassword_title_gone, false);
		background_invisible = a
				.getDrawable(R.styleable.EditTextBarIconTitlePassword_icon_background_invisible);
		background_visible = a
				.getDrawable(R.styleable.EditTextBarIconTitlePassword_icon_background_visible);
		a.recycle();
		
		et_left = (EditText) findViewById(R.id.tb_left);
		et_left.setText(strLeft);
		if(isGone)
		{
			et_left.setVisibility(View.GONE);
		}
		et_right = (EditText) findViewById(R.id.tb_right);
		et_right.setHint(strRightHint);
		et_right
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						maxLength) });
		et_right.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (verifyListener == null)
					return;
				if (verifyListener.verify(et_right.getText().toString(), hasFocus))// ͨ����֤
				{
					if (hasFocus) {
						et_left
								.setBackgroundResource(R.drawable.input_selected);

					} else {
						et_left
								.setBackgroundResource(R.drawable.input_normal);
					}
					et_right
							.setBackgroundResource(R.drawable.tb_selector_textfield);
				} else
				{
					et_left.setBackgroundResource(R.drawable.input_error);
					et_right.setBackgroundResource(R.drawable.input_error);
				}

			}
		});
		if (background_invisible != null) {
			iconRight = findViewById(R.id.tb_icon_right);
			iconRight.setBackgroundDrawable(background_invisible);
			setMargins(iconRight, icon_margin, 0, icon_margin, 0);
			et_right.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			iconRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mChecked = !mChecked;
					if (mChecked) {
						et_right.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
						iconRight.setBackgroundDrawable(background_visible);
					} else {
						et_right.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						iconRight.setBackgroundDrawable(background_invisible);

					}
					et_right.setSelection(et_right.length());
				}
			});
		}

	}
	

	

	public void setTBTextLeft(String str) {
		et_left.setText(str);
	}


	
}
