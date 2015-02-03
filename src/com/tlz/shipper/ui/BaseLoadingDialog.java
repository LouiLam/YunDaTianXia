package com.tlz.shipper.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlz.shipper.R;


public class BaseLoadingDialog extends Dialog {

	private AnimationDrawable mLoadingAnim;
	private CountDownTimer mCountDownTimer;
	private TextView mTvMessage;
	private String mMessage;
	private long mTimeoutInMilli;
	private OnTimeoutListener mOnTimeoutListener;
	private long mMillisUntilFinished;
	private boolean showTime = true;

	public BaseLoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public BaseLoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public BaseLoadingDialog(Context context) {
		super(context);
	}

	public static BaseLoadingDialog show(Context context, String msg) {
		return show(context, msg, 0, null);
	}

	public static BaseLoadingDialog show(Context context, String msg,
			int timeoutSecond, OnTimeoutListener listener) {
		BaseLoadingDialog dialog = new BaseLoadingDialog(context,
				R.style.Lam_Dialog_None);
		dialog.setContentView(R.layout.dialog_loading);
		dialog.setCanceledOnTouchOutside(false);
		dialog.mMessage = msg;
		dialog.mTimeoutInMilli = timeoutSecond * 1000;
		dialog.mOnTimeoutListener = listener;
		dialog.mTvMessage = ((TextView) dialog.findViewById(R.id.message));
		dialog.mTvMessage.setText(msg);
		dialog.show();
		return dialog;
	}

	@Override
	public void show() {
		mLoadingAnim = (AnimationDrawable) ((ImageView) findViewById(R.id.iv_loading)).getDrawable();
		mLoadingAnim.start();
		setupCountDownTimer();
		super.show();
	}

	public void setMessage(String msg) {
		mMessage = msg;
		if (showTime)
			mTvMessage.setText(mMessage
					+ (mMillisUntilFinished == 0 ? "" : "("
							+ mMillisUntilFinished / 1000 + ")"));
		else
			mTvMessage.setText(mMessage);
	}

	private void setupCountDownTimer() {
		if (mTimeoutInMilli == 0)
			return;

		mCountDownTimer = new CountDownTimer(mTimeoutInMilli, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				mMillisUntilFinished = millisUntilFinished;
				setMessage(mMessage);
			}

			@Override
			public void onFinish() {
				dismiss();
				if (mOnTimeoutListener != null) {
					mOnTimeoutListener.onTimeout();
				}
			}

		};
		mCountDownTimer.start();
	}

	@Override
	public void dismiss() {
		setMessage("");
		if (mLoadingAnim != null) {
			mLoadingAnim.stop();
		}
		if (mTimeoutInMilli != 0) {
			if (mCountDownTimer != null) {
				mCountDownTimer.cancel();
			}
		}
		super.dismiss();
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}

	public interface OnTimeoutListener {
		public void onTimeout();
	}

}
