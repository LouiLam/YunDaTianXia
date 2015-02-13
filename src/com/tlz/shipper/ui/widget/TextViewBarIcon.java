package com.tlz.shipper.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlz.shipper.R;
import com.tlz.utils.Flog;

public class TextViewBarIcon extends ViewBar {
	TextView tv;
	ImageView iconLeft,iconRight;
	final Resources res;
	@SuppressWarnings("deprecation")
	public TextViewBarIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		res=context.getResources();
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.TextViewBarIcon, 0, 0);
		LayoutInflater.from(getContext()).inflate(R.layout.item_icon_textview_bar,this,true);
		Drawable background = a
				.getDrawable(R.styleable.TextViewBarIcon_bar_background);
		if (background != null) {
			findViewById(R.id.tb_group).setBackgroundDrawable(background);
		}
		findViewById(R.id.tb_group).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(barListener==null) return;
				barListener.onTBClick(v);
			}
		});
		final float default_padding0=getResources().getDimension(R.dimen.default_padding0);
		final float default_drawable_padding0=getResources().getDimension(R.dimen.default_drawable_padding0);
		final String strLeft = a.getString(R.styleable.TextViewBarIcon_left_text);
		int padding = (int)a.getDimension(R.styleable.TextViewBarIcon_icon_padding, default_padding0);
		int icon_padding = (int)a.getDimension(R.styleable.TextViewBarIcon_icon_padding, default_drawable_padding0);
		int icon_left_height = (int)a.getDimension(R.styleable.TextViewBarIcon_icon_left_height, LayoutParams.WRAP_CONTENT);
		int icon_left_width = (int)a.getDimension(R.styleable.TextViewBarIcon_icon_left_width, LayoutParams.WRAP_CONTENT);
		int icon_right_height = (int)a.getDimension(R.styleable.TextViewBarIcon_icon_right_height, LayoutParams.WRAP_CONTENT);
		int icon_right_width = (int)a.getDimension(R.styleable.TextViewBarIcon_icon_right_width, LayoutParams.WRAP_CONTENT);
		Drawable drawableLeft = a
				.getDrawable(R.styleable.TextViewBarIcon_icon_left_drawable);
		Drawable drawableRight = a
				.getDrawable(R.styleable.TextViewBarIcon_icon_right_drawable);
		a.recycle();
		if(isInEditMode()){return;}
		tv = (TextView)findViewById(R.id.tb_left); 
		tv.setText(strLeft);
		findViewById(R.id.tb_group).setPadding(padding, padding, padding, padding);
		if(icon_left_height>0||icon_left_width>0)
		{
			iconLeft=(ImageView) findViewById(R.id.tb_icon_left);
			iconLeft.getLayoutParams().height=icon_left_height;
			iconLeft.getLayoutParams().width=icon_left_width;
			setMargins(iconLeft, 0, 0, icon_padding, 0);
		}
		if(icon_right_height>0||icon_right_width>0)
		{
			iconRight=(ImageView) findViewById(R.id.tb_icon_right);
			iconRight.getLayoutParams().height=icon_right_height;
			iconRight.getLayoutParams().width=icon_right_width;
			setMargins(iconRight, 0, 0, icon_padding, 0);
		}
		
		if (drawableLeft != null) {
			iconLeft=(ImageView) findViewById(R.id.tb_icon_left);
			iconLeft.setImageDrawable(drawableLeft);
		
			
			
//			iconLeft.setPadding(0, 0, drawable_all, 0);
//			drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(),
//					drawableLeft.getMinimumHeight());
//			tv.setCompoundDrawables(drawableLeft, null, null, null);
//			if(drawable_all>0)
//			{tv.setCompoundDrawablePadding(drawable_all);
//			}
		}
		if(drawableRight!=null){
			iconRight=(ImageView) findViewById(R.id.tb_icon_right);
			iconRight.setImageDrawable(drawableRight);
			setMargins(iconRight, 0, 0, 0, 0);
		
		}
	}
	public void setTBTextOnClickListener(OnClickListener l)
	{
		tv.setOnClickListener(l);
	}
	public void setTBIconLeftOnClickListener(OnClickListener l)
	{
		if(iconLeft==null) return;
		iconLeft.setOnClickListener(l);
	}
	public void setTBLeftText(String str)
	{
		tv.setText(str);
	}
	public void setIconRightBack(Bitmap bm)
	{
		iconRight.setBackgroundDrawable(new BitmapDrawable(res, bm));
	}
	public void setIconRightBack(int resid)
	{
		iconRight.setBackgroundResource(resid);
	}
}
