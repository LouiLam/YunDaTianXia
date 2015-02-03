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

import com.tlz.shipper.R;

public class EditTextBarTitleClearTextAndLeftClick extends ViewBar {

	EditText et_right,et_left;
	View iconRight;
//	boolean mChecked;
	@SuppressWarnings("deprecation")
	public EditTextBarTitleClearTextAndLeftClick(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EditTextBarTitleClearTextAndLeftClick, 0, 0);
		LayoutInflater.from(getContext()).inflate(
				R.layout.item_title_edittext_bar, this, true);

		final float default_icon_margin = getResources().getDimension(
				R.dimen.default_icon_margin);
		int default_maxLength = getResources().getInteger(R.integer.maxLength);
		String strTextLeft = a
				.getString(R.styleable.EditTextBarTitleClearTextAndLeftClick_left_text);
		String strHintRight = a
				.getString(R.styleable.EditTextBarTitleClearTextAndLeftClick_right_hint);
		int maxLength = a.getInt(
				R.styleable.EditTextBarTitleClearTextAndLeftClick_right_maxLength,
				default_maxLength);
		int icon_margin = (int) a.getDimension(
				R.styleable.EditTextBarTitleClearTextAndLeftClick_icon_margin,
				default_icon_margin);
		boolean phone = a.getBoolean(
				R.styleable.EditTextBarTitleClearTextAndLeftClick_phoneNumber,
				false);
		Drawable iconBackground = a
				.getDrawable(R.styleable.EditTextBarTitleClearTextAndLeftClick_icon_right_background);
		a.recycle();

		et_left = (EditText) findViewById(R.id.tb_left);
		et_left.setText(strTextLeft);
		et_right = (EditText) findViewById(R.id.tb_right);
		et_right.setHint(strHintRight);
		et_right
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						maxLength) });
		if (phone) {
			et_right.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		}
		et_left.setTextColor(getResources().getColor(android.R.color.white));
		et_left.setBackgroundColor(getResources().getColor(R.color.default_bule));
		et_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				et_left.setEnabled(false);
				et_left.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
				if (clickListener == null)
					return;
				clickListener.onClick();
			}
		});
		if (iconBackground != null) {
			iconRight = findViewById(R.id.tb_icon_right);
			iconRight.setBackgroundDrawable(iconBackground);
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
	public void setTBTitleEnable()
	{
		et_left.setEnabled(true);
		et_left.setBackgroundColor(getResources().getColor(R.color.default_bule));
	}
	public void setTBRightText(String text)
	{
		et_right.setText(text);
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

		}
	};


}
