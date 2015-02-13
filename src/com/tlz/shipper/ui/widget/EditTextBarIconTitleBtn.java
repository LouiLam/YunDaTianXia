package com.tlz.shipper.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tlz.shipper.R;

public class EditTextBarIconTitleBtn extends ViewBar {
	EditText et_left,et_right;
	ImageView iconRight;

	@SuppressWarnings("deprecation")
	public EditTextBarIconTitleBtn(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.EditTextBarIconTitleBtn, 0, 0);
		LayoutInflater.from(getContext()).inflate(
				R.layout.item_title_edittext_bar, this, true);

		final float default_icon_margin = getResources().getDimension(
				R.dimen.default_icon_margin);
		int default_maxLength = getResources().getInteger(R.integer.maxLength);
		final String strLeft = a.getString(R.styleable.EditTextBarIconTitleBtn_left_text);
		final String strRight = a.getString(R.styleable.EditTextBarIconTitleBtn_right_text);
		final String strRightHint = a.getString(R.styleable.EditTextBarIconTitleBtn_right_hint);
		int rightMaxLength = a.getInt(
				R.styleable.EditTextBarIconTitleBtn_right_maxLength,
				default_maxLength);
		int icon_margin = (int) a.getDimension(
				R.styleable.EditTextBarIconTitleBtn_icon_margin,
				default_icon_margin);
		Drawable drawable = a
				.getDrawable(R.styleable.EditTextBarIconTitleBtn_icon_right_drawable);
		a.recycle();

		et_left = (EditText) findViewById(R.id.tb_left);
		et_left.setText(strLeft);
		et_right = (EditText) findViewById(R.id.tb_right);
		et_right.setText(strRight);
		et_right.setHint(strRightHint);
		et_right.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				rightMaxLength) });
		et_right.setFocusable(false);
		et_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(barListener==null) return;
				barListener.onTBClick(v);
			}
		});
		if (drawable != null) {
			iconRight = (ImageView) findViewById(R.id.tb_icon_right);
			iconRight.setImageDrawable(drawable);
			setMargins(iconRight, icon_margin, 0, icon_margin, 0);
		}

	}
	public void setTBRightText(String text)
	{
		et_right.setText(text);
	}

}
