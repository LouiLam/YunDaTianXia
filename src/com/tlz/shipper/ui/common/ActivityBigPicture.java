package com.tlz.shipper.ui.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;

public class ActivityBigPicture extends ThemeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_picture);
		mActionBar.setTitle(R.string.big_picture_title);
		initView();
	}

	@Override
	protected void initView() {
		super.initView();
		ImageView big = (ImageView) findViewById(R.id.big_picture_img);
		TextView delete = (TextView) findViewById(R.id.big_picture_btn_delete);

		final Intent intent = getIntent();
		Bitmap bm=intent.getParcelableExtra("bitmap");
		final boolean isDelete = intent.getBooleanExtra("isDelete", false);
		if (isDelete) {
			delete.setVisibility(View.VISIBLE);
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		} else {
			delete.setVisibility(View.GONE);
		}
		big.setImageBitmap(bm);
		
	}

}
