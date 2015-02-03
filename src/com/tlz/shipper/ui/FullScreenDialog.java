package com.tlz.shipper.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.tlz.shipper.R;
import com.tlz.shipper.ui.register_login.RegisterDetailsActivity;


public class FullScreenDialog extends Dialog {

//	private AnimationDrawable mLoadingAnim;

	public FullScreenDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public FullScreenDialog(Context context, int theme) {
		super(context, theme);
		getWindow().setGravity(Gravity.FILL);
		this.setContentView(R.layout.dialog_fullscreen);
		this.setCanceledOnTouchOutside(false);
		findViewById(android.R.id.text2).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		findViewById(android.R.id.text1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				getContext().startActivity(new Intent(getContext(), RegisterDetailsActivity.class));
			}
		});
		
	}

	public FullScreenDialog(Context context) {
		super(context);
	}


	public static FullScreenDialog  show(Context context) {
		FullScreenDialog dialog = new FullScreenDialog(context,
				R.style.Lam_Dialog_FullScreen);
		dialog.show();
		return dialog;
	}


//	public void setMessage(String msg) {
//		mMessage = msg;
//	}


	@Override
	public void dismiss() {
//		setMessage("");
//		if (mLoadingAnim != null) {
//			mLoadingAnim.stop();
//		}
		super.dismiss();
	}


}
