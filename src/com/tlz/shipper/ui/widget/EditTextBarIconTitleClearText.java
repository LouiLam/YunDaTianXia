package com.tlz.shipper.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tlz.shipper.R;

public class EditTextBarIconTitleClearText extends ViewBar {
	EditText et_left,et_right;
	View iconRight;

	@SuppressWarnings("deprecation")
	public EditTextBarIconTitleClearText(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EditTextBarIconTitleClearText, 0, 0);
		LayoutInflater.from(getContext()).inflate(
				R.layout.item_title_edittext_bar, this, true);

		final float default_icon_margin = getResources().getDimension(
				R.dimen.default_icon_margin);
		int default_maxLength = getResources().getInteger(R.integer.maxLength);
		final String strLeft = a.getString(R.styleable.EditTextBarIconTitleClearText_left_text);
		final String strRight = a.getString(R.styleable.EditTextBarIconTitleClearText_right_text);
		final String strRightHint = a.getString(R.styleable.EditTextBarIconTitleClearText_right_hint);
		final String  strUnitRightText=a.getString(R.styleable.EditTextBarIconTitleClearText_unit_right_text);
		boolean isGone=a.getBoolean(R.styleable.EditTextBarIconTitleClearText_title_gone, false);
		int rightMaxLength = a.getInt(
				R.styleable.EditTextBarIconTitleClearText_right_maxLength,
				default_maxLength);
		int icon_margin = (int) a.getDimension(
				R.styleable.EditTextBarIconTitleClearText_icon_margin,
				default_icon_margin);
		boolean phone = a.getBoolean(
				R.styleable.EditTextBarIconTitleClearText_phoneNumber,
				false);
		Drawable background = a
				.getDrawable(R.styleable.EditTextBarIconTitleClearText_icon_right_background);
		a.recycle();
		
		et_left = (EditText) findViewById(R.id.tb_left);
		((TextView) findViewById(R.id.tb_unit_right)).setText(strUnitRightText);
		et_left.setText(strLeft);
		if(isGone)
		{
			et_left.setVisibility(View.GONE);
		}
		et_right = (EditText) findViewById(R.id.tb_right);
		et_right.setText(strRight);
		et_right.setHint(strRightHint);
		et_right.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				rightMaxLength) });
		if (phone) {
//			et_right.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
			et_right.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		}
		et_right.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					et_left.setBackgroundResource(R.drawable.input_selected);
				} else {
					et_left.setBackgroundResource(R.drawable.input_normal);
				}
				if (verifyListener == null)
					return;
				//verify success
				if (verifyListener.verify(et_right.getText().toString(), hasFocus))
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
				} else// verify failed
				{
					et_left.setBackgroundResource(R.drawable.input_error);
					et_right.setBackgroundResource(R.drawable.input_error);
				}
			}
		});
		if (background != null) {
			iconRight = findViewById(R.id.tb_icon_right);
			iconRight.setBackgroundDrawable(background);
			setMargins(iconRight, icon_margin, 0, icon_margin, 0);
			iconRight.setVisibility(View.INVISIBLE);
			et_right.removeTextChangedListener(watcher);
			et_right.addTextChangedListener(watcher);
			iconRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					et_right.setText("");
					iconRight.setVisibility(View.INVISIBLE);
				}
			});
		}

	}
	public String getTBTextRight()
	{
		return et_right.getText().toString();
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
			if (s.length() > 0 && iconRight.getVisibility() == View.INVISIBLE) {
				iconRight.setVisibility(View.VISIBLE);
			} else if (s.length() == 0) {
				iconRight.setVisibility(View.INVISIBLE);
			}
			if(textChangedListener==null)return;
			textChangedListener.afterTextChanged(s);
		}
	};


}
