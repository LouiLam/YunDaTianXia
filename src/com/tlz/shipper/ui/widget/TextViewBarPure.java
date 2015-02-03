package com.tlz.shipper.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tlz.shipper.R;

public class TextViewBarPure extends ViewBar {
	TextView tv_left,tv_right;
	public TextViewBarPure(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.TextViewBarPure, 0, 0);
		LayoutInflater.from(getContext()).inflate(R.layout.item_pure_textview_bar,this,true);
		findViewById(R.id.tb_group).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(barListener==null) return;
				barListener.onTBClick(v);
			}
		});
		final float default_padding0=getResources().getDimension(R.dimen.default_padding0);
		final String strLeft = a.getString(R.styleable.TextViewBarPure_left_text);
		final String strRight = a.getString(R.styleable.TextViewBarPure_right_text);
		int padding=(int) a.getDimension(R.styleable.TextViewBarPure_padding, default_padding0);
		a.recycle();
		tv_left = (TextView)findViewById(R.id.tb_left); 
		tv_left.setText(strLeft);
		tv_right=(TextView)findViewById(R.id.tb_right); 
		tv_right.setText(strRight);
		findViewById(R.id.tb_group).setPadding(padding, padding, padding, padding);
		
	}
	public void setTBTextLeft(String str)
	{
		tv_left.setText(str);
	}
	public void setTBTextRight(String str)
	{
		tv_right.setText(str);
	}
}
