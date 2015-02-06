package com.tlz.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class AndroidUtils {

	private static final String TAG = AndroidUtils.class.getSimpleName();
	
	public static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
	
	/**
	 * 模拟按键
	 * 
	 * @param keyCode
	 */
	public static void sendKeyCode(final int keyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(keyCode);
				} catch (Exception e) {
					Flog.e(TAG, e);
				}
			}
		}.start();
	}

	/**
	 * 模拟鼠标按键
	 */
//	public static void sendMouseEvent() {
//		new Thread() {
//			public void run() {
//				try {
//					Instrumentation inst = new Instrumentation();
//					inst.sendPointerSync(MotionEvent.obtain(
//											SystemClock.uptimeMillis(), 
//											SystemClock.uptimeMillis(),	
//											MotionEvent.ACTION_DOWN, 240, 400, 0));
//					inst.sendPointerSync(MotionEvent.obtain(
//											SystemClock.uptimeMillis(),	
//											SystemClock.uptimeMillis(),	
//											MotionEvent.ACTION_UP, 240, 400, 0));
//				} catch (Exception e) {
//					LogUtils.e(TAG, e);
//				}
//			}
//		}.start();
//	}
	
	public static String getMarketAppLink(String packageName) {
	    return "market://details?id=" + packageName;
	}
	
	/**
	 * 发送短信
	 * @param receiverPhoneNumber
	 * @param message
	 * @param sendIntent
	 * @param deliverIntent
	 */
	public static void sendSms(String receiverPhoneNumber, 
			String message, PendingIntent sendIntent, PendingIntent deliverIntent) {
		
		if (TextUtils.isEmpty(receiverPhoneNumber) || TextUtils.isEmpty(message)) {
			return;
		}
		
		SmsManager sm = SmsManager.getDefault();
		if (message.length() > 70) {
			ArrayList<String> messages = sm.divideMessage(message);
			ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
			ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
			for (int i=0; i<messages.size(); i++) {
				sentIntents.add(sendIntent);
				deliveryIntents.add(deliverIntent);
			}
			sm.sendMultipartTextMessage(receiverPhoneNumber, null, messages, sentIntents, deliveryIntents);
		} else {
			sm.sendTextMessage(receiverPhoneNumber, null, message, sendIntent, deliverIntent);
		}
	}
	
	
	/**
	 * 禁用组件
	 * @param context
	 * @param cls
	 */
	public static void setComponentEnabledSetting(Context context, Class<?> cls) {
		PackageManager pm = context.getPackageManager();
		ComponentName cn = new ComponentName(context, cls);
		pm.setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}
	
	/**
	 * 创建快捷方式
	 * require permission
	 * {@link #com.android.launcher.permission.INSTALL_SHORTCUT}
	 */
	public static void installShortcut(Context context, String name, int iconResId, Intent targetIntent) {
		Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, iconResId));
		shortcutIntent.putExtra("duplicate", false);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
		context.sendBroadcast(shortcutIntent);
	}
	
	/**
	 * 移除快捷方式
	 * require permission
	 * {@link #com.android.launcher.permission.UNINSTALL_SHORTCUT}
	 */
	public static void uninstallShortcut(Context context, String name, Intent targetIntent) {
		Intent shortcutIntent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
		context.sendBroadcast(shortcutIntent);
	}
	
	/**
	 * {@link #com.android.launcher.permission.READ_SETTINGS}
	 * @param context
	 * @return
	 */
	public static boolean isShortcutInstalled(Context context, String name) {
		ContentResolver cr = context.getContentResolver();
		final String AUTHORITY = RomUtils.getAuthorityFromPermission(context);
		final Uri contentUri = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
		Cursor c = null;
		try {
			c = cr.query(contentUri, new String[]{"title", "iconPackage"}, "title=? AND iconPackage=?", new String[]{name, context.getPackageName()}, null);
			if (c != null && c.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
		} finally {
			IOUtils.close(c);
		}
		return false;
	}
	
	public static void showInputMethod(Context context) {
		if (context instanceof Activity) {
			((Activity)context).getWindow()
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}
	
	public static void hideInputMethod(EditText editText) {
		InputMethodManager imManager = ((InputMethodManager) editText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE));
		imManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	/**
	 * 设置密码可见
	 * @param input
	 * @param visibility
	 */
	public static void setPasswordVisibility(EditText input, boolean visibility) {
		if (visibility) {
			input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			input.setSelection(input.length());
		} else {
			input.setTransformationMethod(PasswordTransformationMethod.getInstance());
			input.setSelection(input.length());
		}
	}
	
	/**
	 * 清除应用默认设置
	 * @param context
	 * @param paceageName
	 */
	public static void clearDefaultSettings(Context context, String paceageName) {
		Intent intent = new Intent();
		intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uriAppSettings = Uri.fromParts("package", paceageName, null);
		intent.setData(uriAppSettings);
		context.startActivity(intent);
	}
	
	/**
	 * 设置代理
	 * @param context
	 * @param proxy
	 */
	public static void setProxy(Context context, String proxy) {
		Settings.System.putString(context.getContentResolver(), 
				"http_proxy", proxy + ":8080");
	}
	

	public static String takeScreenShot(Activity activity, String dirPath) throws RuntimeException {
		File file = null;
		String name = TimeUtils.getCurrentDateTime() + ".png";
		
		if (TextUtils.isEmpty(dirPath)) {
			file = new File(StorageUtils.getExternalStorageRootDir(), name);
		} else {
			file = new File(dirPath, name);
		}
		
		View view = activity.getWindow().getDecorView();
		Bitmap bitmap = Bitmap.createBitmap(MeasureUtils.getScreenWidth(activity), MeasureUtils.getScreenHeight(activity), Bitmap.Config.ARGB_8888);
		view.draw(new Canvas(bitmap));
		
		try {
			bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return file.getPath();
	}
	
	public static String takeScreenShot2(Activity activity, String path) throws RuntimeException {
		if (TextUtils.isEmpty(path)) path = StorageUtils.getExternalStorageRootDir().getPath();
		
		View decorView = activity.getWindow().getDecorView();
		decorView.setDrawingCacheEnabled(true);
		Bitmap bitmap = decorView.getDrawingCache();
		
		File file = new File(path);
		try {
			bitmap.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return file.getPath();
	}
	
	/**
	 * 设置全屏
	 * @param activity
	 * @param enable
	 */	
	public static void setFullScreen(Activity a, boolean on) {
		Window win = a.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (on) {
            params.flags |=  bits;
        } else {
            params.flags &= ~bits;
        }
        win.setAttributes(params);
	}
	
	/**
	 * 禁止截屏
	 * @param enable
	 */
	public static void setWindowSecure(Activity a, boolean enable) {
		Window win = a.getWindow();
		WindowManager.LayoutParams params = win.getAttributes();
		final int flag = WindowManager.LayoutParams.FLAG_SECURE;
		if (enable) {
			params.flags |= flag;
		} else {
			params.flags &= ~flag;
		}
		win.setAttributes(params);
	}
	
	/**
	 * 判断当前是否是横屏
	 * @param a
	 * @return
	 */
	public static boolean isScreenLandscape(Activity a) {
		return a.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}
	
	/**
	 * 导航栏透明
	 * @param a
	 * @param on
	 */
    @TargetApi(Build.VERSION_CODES.KITKAT)
	public static void setTranslucentNavigation(Activity a, boolean on) {
        Window win = a.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |=  bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    
    /**
     * Requires Permission:
     * {@link android.Manifest.permission#GET_TASKS}
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
	public static ComponentName getTopComponentName(Context context) {
	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    ComponentName cm = null;
	    try {
			List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
			cm = taskInfo.get(0).topActivity;
	    } catch (SecurityException e) {
	    	Flog.e("Requires Permission: android.Manifest.permission#GET_TASKS");
	    }
		return cm;
    }
    
    /**
     * 获取桌面信息
     * @param context
     * @return
     */
    public static List<String> getLauncherInfo(Context context) {
    	if (context == null) {
    		Flog.e("context cannot be null!");
    		throw new IllegalArgumentException("context cannot be null!");
    	}
    	
    	List<String> names = new ArrayList<String>();
		Intent intent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = context.getPackageManager()
				.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}
    
    public static boolean isLauncherTop(Context context) {
    	if (context == null) {
    		Flog.e("context cannot be null!");
    		throw new IllegalArgumentException("context cannot be null!");
    	}
    	
    	List<String> strList = getLauncherInfo(context);
    	if(CollectionUtils.isEmpty(strList)) return false;
    	
    	ComponentName cm = getTopComponentName(context);
    	if (cm == null) return false;
    	
    	if (strList.contains(cm.getPackageName())) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * 获取签名密钥
     * @param context
     * @param packageName
     * @return
     */
    public static String getSignaturePublicKey(Context context, String packageName) {
    	X509Certificate cert = getX509Certificate(context, packageName);
    	String publicKey = cert.getPublicKey().toString();
    	int start = publicKey.indexOf("modulus=") + 8;
    	int end = publicKey.indexOf(",");
    	return publicKey.substring(start, end);
    }
    
    /**
     * 获取X509证书
     * @param context
     * @param packageName
     * @return
     */
    public static X509Certificate getX509Certificate(Context context, String packageName) {
    	List<PackageInfo> packageInfoList = context.getPackageManager()
        		.getInstalledPackages(PackageManager.GET_SIGNATURES);
        
        byte[] signatureByteArray = null;
        for (PackageInfo info : packageInfoList) {
        	if (info.packageName.equals(packageName)) {
        		signatureByteArray = info.signatures[0].toByteArray();
        	}
        }
    	
		try {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			return (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signatureByteArray));
		} catch (CertificateException e) {
			Flog.e(e);
		}
		return null;
    }
    //向指定的文件中写入指定的数据
    public static void writeFileData(String filename, String message,Context context){
        try {
           FileOutputStream fout = context.openFileOutput(filename, Context.MODE_PRIVATE);//获得FileOutputStream
           //将要写入的字符串转换为byte数组
           byte[]  bytes = message.getBytes();
           fout.write(bytes);//将byte数组写入文件
           IOUtils.close(fout);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
                       
    //打开指定文件，读取其数据，返回字符串对象
    public static String readFileData(String fileName,Context context){
        String result="";
        try {
           FileInputStream fin = context.openFileInput(fileName);
           //获取文件长度
           int length = fin.available();
           byte[] buffer = new byte[length];
           fin.read(buffer);
           //将byte数组转换成指定格式的字符串
           result = EncodingUtils.getString(buffer, "UTF-8");
       } catch (Exception e) {
          return null;
       }
       return result;
    }
	private AndroidUtils() {/* Do not new me */}
}
