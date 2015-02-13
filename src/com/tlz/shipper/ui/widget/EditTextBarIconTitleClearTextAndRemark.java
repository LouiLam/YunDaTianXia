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
import android.widget.ImageView;

import com.tlz.shipper.R;

public class EditTextBarIconTitleClearTextAndRemark extends ViewBar {
	EditText et_right;
	ImageView iconRight_clear,iconRight_photograph;

	@SuppressWarnings("deprecation")
	public EditTextBarIconTitleClearTextAndRemark(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EditTextBarIconTitleClearTextAndRemark, 0, 0);
		LayoutInflater.from(getContext()).inflate(
				R.layout.item_title_edittext_bar_remark, this, true);

		final float default_icon_margin = getResources().getDimension(
				R.dimen.default_icon_margin);
		int default_maxLength = getResources().getInteger(R.integer.maxLength);
		final String strRight = a.getString(R.styleable.EditTextBarIconTitleClearTextAndRemark_right_text);
		final String strRightHint = a.getString(R.styleable.EditTextBarIconTitleClearTextAndRemark_right_hint);
		int rightMaxLength = a.getInt(
				R.styleable.EditTextBarIconTitleClearTextAndRemark_right_maxLength,
				default_maxLength);
		int icon_margin = (int) a.getDimension(
				R.styleable.EditTextBarIconTitleClearTextAndRemark_icon_margin,
				default_icon_margin);
		boolean phone = a.getBoolean(
				R.styleable.EditTextBarIconTitleClearTextAndRemark_phoneNumber,
				false);
		Drawable drawable = a
				.getDrawable(R.styleable.EditTextBarIconTitleClearTextAndRemark_icon_right_drawable);
		a.recycle();
		
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
				
				if (verifyListener == null)
					return;
				//verify success
				if (verifyListener.verify(et_right.getText().toString(), hasFocus))
				{
					
					et_right
							.setBackgroundResource(R.drawable.tb_selector_textfield);
				} else// verify failed
				{
					et_right.setBackgroundResource(R.drawable.input_error);
				}
			}
		});
		if (drawable != null) {
			iconRight_clear = (ImageView) findViewById(R.id.tb_icon_right_clear);
			iconRight_clear.setImageDrawable(drawable);
			setMargins(iconRight_clear, icon_margin, 0, icon_margin, 0);
			iconRight_clear.setVisibility(View.INVISIBLE);
			et_right.removeTextChangedListener(watcher);
			et_right.addTextChangedListener(watcher);
			iconRight_clear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					et_right.setText("");
					iconRight_clear.setVisibility(View.INVISIBLE);
				}
			});
		}
		iconRight_photograph= (ImageView) findViewById(R.id.tb_icon_right_photograph);
		iconRight_photograph.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(photographListener==null)return ;
				photographListener.onClick();
			}
		});

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
			if (s.length() > 0 && iconRight_clear.getVisibility() == View.INVISIBLE) {
				iconRight_clear.setVisibility(View.VISIBLE);
			} else if (s.length() == 0) {
				iconRight_clear.setVisibility(View.INVISIBLE);
			}

		}
	};
	TBOnPhotographClickListener photographListener;
	public static interface TBOnPhotographClickListener {
		void onClick();
	}
	public void setTBOnPhotographClickListener(TBOnPhotographClickListener listener) {
		this.photographListener = listener;
	}
}
