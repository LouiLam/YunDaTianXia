package com.tlz.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tlz.shipper.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class ToastUtils {

	private static final String TAG = ToastUtils.class.getSimpleName();
	
	private static Toast mCustomToast;
	private static Toast mToast;
	private static TextView mText;
	
	public static void showCustom(Context context, String text) {
		Resources resources = context.getResources();
		int iconId = 0;
		if (text.contains("失败")) {
			iconId = resources.getIdentifier("ic_toast_error", "drawable", context.getPackageName());
		} else {
			iconId = resources.getIdentifier("ic_tick", "drawable", context.getPackageName());
		}
		showCustom(context, iconId, text);
	}
	
	public static void showCustom(Context context, int iconId, String text) {
		Resources resources = context.getResources();
		if(mCustomToast == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			int resId = resources.getIdentifier("toast_view", "layout", context.getPackageName());
			if (resId == 0) {
				Log.e(TAG, "cannot find layout toast_view!");
			}
			View view = inflater.inflate(resId, null);
			int textId = resources.getIdentifier("text", "id", context.getPackageName());
			mText = (TextView)view.findViewById(textId);
			mCustomToast = new Toast(context);
			mCustomToast.setGravity(Gravity.CENTER, 0, 0);
			mCustomToast.setView(view);
		}
		mText.setCompoundDrawablesWithIntrinsicBounds(0, iconId, 0, 0);
		mText.setText(text);
		mCustomToast.show();
	}
	
	public static void showCrouton(Context context, Object obj) {
		if (!(context instanceof Activity)) return;
 		
		View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, (ViewGroup)null, false);
		TextView tv = (TextView) view.findViewById(R.id.content);
		tv.setText(String.valueOf(obj));
		Crouton.make((Activity)context, view).show();
	}
	
	public static void show(Context context, int errorCode) {
		show(context, ErrorUtils.getMsg(errorCode));// + "(" + errorCode + ")");
	}
	
	public static void show(Context context, Object obj) {
		show(context, obj, false);
	}
	
	public static void show(final Context context, final Object obj, final boolean isLongTime) {
		
		if (Looper.myLooper() != Looper.getMainLooper()) {
			if (context instanceof Activity) {
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						show(context, obj == null ? "" : obj.toString(), isLongTime);
					}
					
				});
			}
			return;
		}
		
		show(context, obj == null ? "" : obj.toString(), isLongTime);
	}
	
	private static void show(Context context, String text, boolean isLongTime) {
		if (context == null) return;
		
		if(mToast == null) {
			mToast = Toast.makeText(context, text, isLongTime == true ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(isLongTime == true ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
	
	private ToastUtils() {/*Do not new me*/};
}
