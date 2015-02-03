package com.tlz.utils;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.webkit.URLUtil;

public class IntentUtils {
	
	private static final String SETTINGS_PACKAGE_NAME = "com.android.settings";
	private static final String SETTINGS_APPDETAILS_CLASS_NAME_B21 = "com.android.settings.ApplicationPkgName";
	private static final String SETTINGS_APPDETAILS_CLASS_NAME_22 = "pkg";
	private static final String SETTINGS_APPDETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	
	private static final String SCHEME_PACKAGE = "package";
	
	public static final String ACTIVITY_MIUI_OPEN_WIFI_LOGIN = "com.android.settings.openwifi.OpenWifiLogin";

	public static final String PACKAGE_COLOROS_PURE_BACKGROUND = "com.oppo.purebackground";
	public static final String ACTIVITY_COLOROS_PURE_BACKGROUND = "com.oppo.purebackground.PurebackgroundTopActivity";
	
	public static final String PACKAGE_MIUI_PERMISSION_MANAGER = "com.android.settings";
	public static final String ACTIVITY_MIUI_PERMISSION_MANAGER = "com.miui.securitycenter.permission.PermMainActivity";
	
	public static final String PACKAGE_BROWSER_DEFAULT = "com.android.browser";
	public static final String ACTIVITY_BROWSER_DEFAULT = "com.android.browser.BrowserActivity";
	
	public static final String PACKAGE_BROWSER_LIEBAO = "com.ijinshan.browser";
	public static final String ACTIVITY_BROWSER_LIEBAO = "com.ijinshan.browser.screen.BrowserActivity";
	
	public static final String PACKAGE_BROWSER_UC = "com.UCMobile";
	public static final String ACTIVITY_BROWSER_UC = "com.UCMobile.main.UCMobile";
	
	public static final String PACKAGE_BROWSER_QQ = "com.tencent.mtt";
	public static final String ACTIVITY_BROWSER_QQ = "com.tencent.mtt.MainActivity";
	
	public static final String PACKAGE_BROWSER_360 = "com.qihoo.browser";
	public static final String ACTIVITY_BROWSER_360 = "com.qihoo.browser.BrowserActivity";
	
	public static final String PACKAGE_BROWSER_CHROME = "com.android.chrome";
	public static final String ACTIVITY_BROWSER_CHROME = "com.google.android.apps.chrome.Main";
    
    public static void configureCropPhoto(Intent intent, int outputX, int outputY) {
    	intent.putExtra("crop", "true");
		//裁剪框比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		
		intent.putExtra("outputX", outputX);  
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputFormat", "JPEG");
		//人脸识别
		intent.putExtra("noFaceDetection", false);
		intent.putExtra("return-data", true);
    }
    
    public static void configureCropPhoto(Intent intent, Bitmap data, int outputX, int outputY) {
    	intent.setAction("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", data);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1); 
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
    }
    
    /**
     * 打开网页搜索
     * @param context
     * @param intent
     * @param text
     */
    public static void openWebSearchActivity(Context context, String text) {
    	Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
    	intent.putExtra(SearchManager.QUERY, text);
    	context.startActivity(intent);
    }
    
	@TargetApi(9)
	public static void openAppDetailActivity(Context context, String packageName) {
		Intent intent = null;
		if (Build.VERSION.SDK_INT >= 9) {
			intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts(SCHEME_PACKAGE, packageName, null);
			intent.setData(uri);
		} else {
			final String className = Build.VERSION.SDK_INT == 8 ? 
					SETTINGS_APPDETAILS_CLASS_NAME_22 : SETTINGS_APPDETAILS_CLASS_NAME_B21;
			intent = new Intent(Intent.ACTION_VIEW);
		    intent.setClassName(SETTINGS_PACKAGE_NAME, SETTINGS_APPDETAILS_CLASS_NAME);
		    intent.putExtra(className, packageName);
		}
		if (isIntentAvailable(context, intent)) {
			context.startActivity(intent);
		} else {
			Flog.e("intent is not available!");
		}
	}
	
	/**
	 * 打开激活设备管理器
	 * @param context
	 * @param clazz Receiver
	 */
	public static void openActiveDeviceAdminActivity(Context context, Class<?> clazz) {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		ComponentName  mDeviceComponentName = new ComponentName(context, clazz); 
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceComponentName);
		context.startActivity(intent);
	}
	
	/**
	 * 打开分享
	 * @param context
	 * @param title
	 * @param text
	 */
	public static void openShareActivity(Context context, String title, String text) {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
		shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		context.startActivity(shareIntent);
	}
	
	/**
	 * 启动默认的Activity
	 * @param context
	 * @param packageName
	 */
	public static void startMainActivity(final Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(packageName, 0);
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(pi.packageName);

            List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String className = ri.activityInfo.name;
                intent.setComponent(new ComponentName(packageName, className));
                context.startActivity(intent);
            }
        } catch (NameNotFoundException e) {
        	
        }
    }

	/**
	 * 打开浏览器
	 * @param context
	 * @param info
	 */
	public static void openUrlInBrowser(Context context, String urlString) {
		if (URLUtil.isValidUrl(urlString)) {
			Intent intent = new Intent();
			if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_LIEBAO)) {
				intent.setClassName(PACKAGE_BROWSER_LIEBAO, ACTIVITY_BROWSER_LIEBAO);
			} else if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_UC)) {
				intent.setClassName(PACKAGE_BROWSER_UC, ACTIVITY_BROWSER_UC);
			} else if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_QQ)) {
				intent.setClassName(PACKAGE_BROWSER_QQ, ACTIVITY_BROWSER_QQ);
			} else if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_360)) {
				intent.setClassName(PACKAGE_BROWSER_360, ACTIVITY_BROWSER_360);
			} else if (PackageUtils.isApplicationInstalled(context, PACKAGE_BROWSER_CHROME)) {
				intent.setClassName(PACKAGE_BROWSER_CHROME, ACTIVITY_BROWSER_CHROME);
			} else {
				intent.setClassName(PACKAGE_BROWSER_DEFAULT, ACTIVITY_BROWSER_DEFAULT);
			}
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(urlString));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
	
	/**
	 * 打开ColorOS纯净后台
	 * @param context
	 */
	public static void openColorOsPureBackground(Context context) {
		try {
			Intent intent = new Intent();
			intent.setClassName(PACKAGE_COLOROS_PURE_BACKGROUND, ACTIVITY_COLOROS_PURE_BACKGROUND);
			context.startActivity(intent);
		} catch (Exception e) {
			Flog.e("activity not found!");
		}
	}
	
	/**
	 * 打开MIUI授权管理页面
	 * @param context
	 */
	public static void openMiuiPermissionManagerActivity (Context context) {
		Intent intent = new Intent();
		intent.setClassName(PACKAGE_MIUI_PERMISSION_MANAGER, ACTIVITY_MIUI_PERMISSION_MANAGER);
		
		if (isIntentAvailable(context, intent)) {
			context.startActivity(intent);
		} else {
			Flog.e("Intent is not available!");
		}
	}
	
	/**
	 * 打开自身权限管理界面
	 * @param context
	 */
	public static void OpenMiuiPermissionActivity(Context context) {
		openMiuiPermissionActivity(context, context.getPackageName());
	}
	
	/**
	 * 打开MIUI权限管理界面(MIUI v5, v6)
	 * @param context
	 */
	public static void openMiuiPermissionActivity(Context context, String packageName) {
		Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
		String rom = RomUtils.getRom();
		
		if (RomUtils.ROM_MIUI_V5.equals(rom)) {
			PackageInfo pInfo = null;
			try {
				pInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			} catch (NameNotFoundException e) {
				Flog.e(e);
			}
		    intent.setClassName(SETTINGS_PACKAGE_NAME, "com.miui.securitycenter.permission.AppPermissionsEditor");
		    intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
		    
		} else if (RomUtils.ROM_MIUI_V6.equals(rom)) {
			intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
			intent.putExtra("extra_pkgname", packageName);
		}
		
		if (isIntentAvailable(context, intent)) {
	    	if (context instanceof Activity) {
	    		Activity a = (Activity) context;
	    		a.startActivityForResult(intent, 2);
	    	}
	    } else {
	    	Flog.e("Intent is not available!");
	    }
	}
	
	/**
	 * 判断是否有可以接受的Activity
	 * @param context
	 * @param action
	 * @return
	 */
	public static boolean isIntentAvailable(Context context, Intent intent) {
		if (intent == null) return false;
		return context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_ACTIVITIES).size() > 0;
	}
	
	public static void closeMiuiOpenWifiLoginActivity(Context context) {
		SysUtils.killProcess(context, SETTINGS_PACKAGE_NAME);
	}
}
