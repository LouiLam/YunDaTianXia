package com.tlz.shipper.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ViewBar extends LinearLayout{
	public ViewBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	TBBarOnClickListener barListener;

	public static interface TBBarOnClickListener {
		void onTBClick(View v);
	}

	public void setTBBarOnClickListener(TBBarOnClickListener listener) {
		this.barListener = listener;
	}
	void setMargins(View v, int l, int t, int r, int b) {
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v
					.getLayoutParams();
			p.setMargins(l, t, r, b);
			v.requestLayout();
		}
	}
	TBFocusChangeVerifyListener verifyListener;
	TBOnClickListener clickListener;
	TBOnTextChangedListener textChangedListener;
	public void setTBOnClickListener(TBOnClickListener listener) {
		this.clickListener = listener;
	}
	public void setTBOnTextChangedListener(TBOnTextChangedListener listener) {
		this.textChangedListener = listener;
	}
	public void setTBFocusChangeVerifyListener(TBFocusChangeVerifyListener listener) {
		this.verifyListener = listener;
	}
	public static interface TBFocusChangeVerifyListener {
		boolean verify(String text, boolean hasFocus);
	}
	public static interface TBOnClickListener {
		void onClick();
	}
	public static interface TBOnTextChangedListener {
		void afterTextChanged(Editable s) ;
	}
}
