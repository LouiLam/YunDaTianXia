package com.tlz.shipper.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.tlz.shipper.R;

public class EditTextBarPureClearText extends ViewBar {
	EditText bottom_et;
	EditText top_et;
	View iconRight;

	@SuppressWarnings("deprecation")
	public EditTextBarPureClearText(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EditTextBarPureClearText, 0, 0);
		LayoutInflater.from(getContext()).inflate(
				R.layout.item_pure_edittext_bar, this, true);

		final float default_icon_margin = getResources().getDimension(
				R.dimen.default_icon_margin);
		String topHint = a
				.getString(R.styleable.EditTextBarPureClearText_top_hint);
		String bottomText = a
				.getString(R.styleable.EditTextBarPureClearText_bottom_text);
		int icon_margin = (int) a.getDimension(
				R.styleable.EditTextBarPureClearText_icon_margin,
				default_icon_margin);
		boolean phone = a.getBoolean(
				R.styleable.EditTextBarPureClearText_phoneNumber, false);
		Drawable background = a
				.getDrawable(R.styleable.EditTextBarPureClearText_icon_right_background);
		a.recycle();

		top_et = (EditText) findViewById(R.id.tb_top);
		top_et.setHint(topHint);
		bottom_et = (EditText) findViewById(R.id.tb_bottom);
		bottom_et.setText(bottomText);
		if (phone) {
			top_et.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
			top_et.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		}

		if (background != null) {
			iconRight = findViewById(R.id.tb_icon_right);
			iconRight.setBackgroundDrawable(background);
			setMargins(iconRight, icon_margin, 0, icon_margin, 0);
			iconRight.setVisibility(View.INVISIBLE);
			top_et.removeTextChangedListener(watcher);
			top_et.addTextChangedListener(watcher);
			iconRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					top_et.setText("");
					iconRight.setVisibility(View.INVISIBLE);
				}
			});
		}

	}
	public void setMaxLengthTop(int MaxLengthTop)
	{
		top_et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				MaxLengthTop) });
	}

	public void setTBTextTop(String text) {
		top_et.setText(text);
	}

	public void setTBTextTopHint(String text) {
		top_et.setHint(text);
	}

	public void setTBTextBottom(String text) {

		bottom_et.setText(text);
	}

	public String getTBText() {
		return top_et.getText().toString();
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
