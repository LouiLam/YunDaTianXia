package com.tlz.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class MeasureUtils {

	/**
	 * 获取屏幕宽度像素
	 * @param a
	 * @return
	 */
	public static int getScreenWidth(Activity a) {
		if (a != null) {
			return a.getResources().getDisplayMetrics().widthPixels;
		}
		return 0;
	}

	/**
	 * 获取屏幕高度像素
	 * 
	 * @param a
	 * @return
	 */
	public static int getScreenHeight(Activity a) {
		if (a != null) {
			return a.getResources().getDisplayMetrics().heightPixels;
		}
		return 0;
	}
	
	@TargetApi(11)
	public static int getActionBarHeight(Context context) {
        TypedValue typedValue = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        return TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
	}
	
	public static int getStatusBarHeight(Activity a) {
		if (a == null) {
			return 0;
		}
		Rect rect = new Rect();
		Window win = a.getWindow();
		win.getDecorView().getWindowVisibleDisplayFrame(rect);
		return rect.top;
	}
	
	/**
	 * 获取状态栏(通知栏)高度
	 * 
	 * @param a
	 * @return
	 */
//	public static int getStatusBarHeight(Activity a) {
//		try {
//			Object o = Class.forName("com.android.internal.R$dimen").newInstance();
//			Field field = Class.forName("com.android.internal.R$dimen").getField("status_bar_height");
//			int x = Integer.parseInt(field.get(o).toString());  
//			int y = a.getResources().getDimensionPixelSize(x);  
//			return y;
//		} catch (Exception e) {
//			if (DEBUG) Flog.e(TAG, e);
//		}
//		return 0;
//	}
	
	/**
	 * 获取标题栏高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getTitleBarHeight(Activity a) {
		if (a == null) {
			return 0;
		}
		Rect rect = new Rect();
		Window win = a.getWindow();
		win.getDecorView().getWindowVisibleDisplayFrame(rect);
		int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT)
				.getTop();
		return contentViewTop;
	}
	
	/**
	 * 获取屏幕宽高度
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(13)
	public static Point getScreenSize(Context context) {
	    Display localDisplay = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    Point localPoint = new Point();
	    if (Build.VERSION.SDK_INT >= 13) {
	    	localDisplay.getSize(localPoint);
	    	return localPoint;
	    }
	    localPoint.x = localDisplay.getWidth();
	    localPoint.y = localDisplay.getHeight();
	    return localPoint;
	}
	
	/**
	 * 获取字宽
	 * @param text
	 * @param Size
	 * @return
	 */
	public static float getTextWidth(String text, float Size) {
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(Size);
        return FontPaint.measureText(text);
    }
	/**
	 * DP转像素
	 * @param dip
	 * @return
	 */
	public static int dipToPixels(int dip,Resources r) {
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}
	private MeasureUtils() {/*Do Not New Me!*/}
}
