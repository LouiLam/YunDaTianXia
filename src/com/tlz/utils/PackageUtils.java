package com.tlz.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.tlz.utils.ShellUtils.CommandResult;


public class PackageUtils {

    public static final String TAG = PackageUtils.class.getSimpleName();
    public static final boolean DEBUG = false;
    
    /**
     * 获取App权限
     * @param context
     * @param packageName
     * @return
     */
    public static String[] getAppPermissions(Context context, String packageName) {
    	if (context == null) return new String[0];
    	
    	PackageManager pManager = (PackageManager) context.getPackageManager();
    	try {
			return pManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions;
		} catch (NameNotFoundException e) {
			Flog.e(e);
		}
    	return new String[0];
    }
    
    /**
     * 获取安装的App
     * @param context
     * @param isIncludeSystemApp
     * @return
     */
    public static List<PackageInfo> getInstalledApps(Context context, boolean isIncludeSystemApp) {
    	if (context == null) return null;
    	
    	PackageManager pManager = (PackageManager) context.getPackageManager();
    	List<PackageInfo> appInfoList = pManager.getInstalledPackages(0);
    	if (!isIncludeSystemApp) {
    		for (Iterator<PackageInfo> iter = appInfoList.iterator(); iter.hasNext();) {
    			PackageInfo info = iter.next();
    			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
	    			iter.remove();
	    		}
    		}
    	}
    	return appInfoList;
    }

    public static boolean installNormal(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    public static int installSilent(Context context, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return INSTALL_FAILED_INVALID_URI;
        }

        File file = new File(filePath);
        if (file == null || file.length() <= 0 || !file.exists() || !file.isFile()) {
            return INSTALL_FAILED_INVALID_URI;
        }

        /**
         * if context is system app, don't need root permission, but should add <uses-permission
         * android:name="android.permission.INSTALL_PACKAGES" /> in mainfest
         **/
        StringBuilder command = new StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r ")
                                                   .append(filePath.replace(" ", "\\ "));
        CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSystemApplication(context), true);
        if (commandResult.successMsg != null
            && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
            return INSTALL_SUCCEEDED;
        }

        Log.e(TAG,
              new StringBuilder().append("installSilent successMsg:").append(commandResult.successMsg)
                                 .append(", ErrorMsg:").append(commandResult.errorMsg).toString());
        if (commandResult.errorMsg == null) {
            return INSTALL_FAILED_OTHER;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_ALREADY_EXISTS")) {
            return INSTALL_FAILED_ALREADY_EXISTS;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_APK")) {
            return INSTALL_FAILED_INVALID_APK;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_URI")) {
            return INSTALL_FAILED_INVALID_URI;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
            return INSTALL_FAILED_INSUFFICIENT_STORAGE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DUPLICATE_PACKAGE")) {
            return INSTALL_FAILED_DUPLICATE_PACKAGE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NO_SHARED_USER")) {
            return INSTALL_FAILED_NO_SHARED_USER;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")) {
            return INSTALL_FAILED_UPDATE_INCOMPATIBLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE")) {
            return INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY")) {
            return INSTALL_FAILED_MISSING_SHARED_LIBRARY;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE")) {
            return INSTALL_FAILED_REPLACE_COULDNT_DELETE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DEXOPT")) {
            return INSTALL_FAILED_DEXOPT;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_OLDER_SDK")) {
            return INSTALL_FAILED_OLDER_SDK;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONFLICTING_PROVIDER")) {
            return INSTALL_FAILED_CONFLICTING_PROVIDER;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NEWER_SDK")) {
            return INSTALL_FAILED_NEWER_SDK;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_TEST_ONLY")) {
            return INSTALL_FAILED_TEST_ONLY;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE")) {
            return INSTALL_FAILED_CPU_ABI_INCOMPATIBLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_FEATURE")) {
            return INSTALL_FAILED_MISSING_FEATURE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONTAINER_ERROR")) {
            return INSTALL_FAILED_CONTAINER_ERROR;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION")) {
            return INSTALL_FAILED_INVALID_INSTALL_LOCATION;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE")) {
            return INSTALL_FAILED_MEDIA_UNAVAILABLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT")) {
            return INSTALL_FAILED_VERIFICATION_TIMEOUT;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_FAILURE")) {
            return INSTALL_FAILED_VERIFICATION_FAILURE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_PACKAGE_CHANGED")) {
            return INSTALL_FAILED_PACKAGE_CHANGED;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UID_CHANGED")) {
            return INSTALL_FAILED_UID_CHANGED;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NOT_APK")) {
            return INSTALL_PARSE_FAILED_NOT_APK;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_MANIFEST")) {
            return INSTALL_PARSE_FAILED_BAD_MANIFEST;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION")) {
            return INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_NO_CERTIFICATES;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING")) {
            return INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME")) {
            return INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID")) {
            return INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_MALFORMED")) {
            return INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_EMPTY")) {
            return INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INTERNAL_ERROR")) {
            return INSTALL_FAILED_INTERNAL_ERROR;
        }
        return INSTALL_FAILED_OTHER;
    }

    public static final int uninstall(Context context, String packageName) {
        if (PackageUtils.isSystemApplication(context) || ShellUtils.checkRootPermission()) {
            return uninstallSilent(context, packageName);
        }
        return uninstallNormal(context, packageName) ? DELETE_SUCCEEDED : DELETE_FAILED_INVALID_PACKAGE;
    }

    public static boolean uninstallNormal(Context context, String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return false;
        }

        Intent i = new Intent(Intent.ACTION_DELETE, 
        		Uri.parse(new StringBuilder(32).append("package:").append(packageName).toString()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    public static int uninstallSilent(Context context, String packageName) {
        return uninstallSilent(context, packageName, true);
    }

    /**
     * @param context file path of package
     * @param packageName package name of app
     * @param isKeepData whether keep the data and cache directories around after package removal
     * @return <ul>
     * <li>{@link #DELETE_SUCCEEDED} means uninstall success</li>
     * <li>{@link #DELETE_FAILED_INTERNAL_ERROR} means internal error</li>
     * <li>{@link #DELETE_FAILED_INVALID_PACKAGE} means package name error</li>
     * <li>{@link #DELETE_FAILED_PERMISSION_DENIED} means permission denied</li>
     */
    public static int uninstallSilent(Context context, String packageName, boolean isKeepData) {
        if (packageName == null || packageName.length() == 0) {
            return DELETE_FAILED_INVALID_PACKAGE;
        }

        /**
         * if context is system app, don't need root permission, but should add <uses-permission
         * android:name="android.permission.DELETE_PACKAGES" /> in mainfest
         **/
        StringBuilder command = new StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall")
                                                   .append(isKeepData ? " -k " : " ")
                                                   .append(packageName.replace(" ", "\\ "));
        CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSystemApplication(context), true);
        if (commandResult.successMsg != null
            && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
            return DELETE_SUCCEEDED;
        }
        Log.e(TAG,
              new StringBuilder().append("uninstallSilent successMsg:").append(commandResult.successMsg)
                                 .append(", ErrorMsg:").append(commandResult.errorMsg).toString());
        if (commandResult.errorMsg == null) {
            return DELETE_FAILED_INTERNAL_ERROR;
        }
        if (commandResult.errorMsg.contains("Permission denied")) {
            return DELETE_FAILED_PERMISSION_DENIED;
        }
        return DELETE_FAILED_INTERNAL_ERROR;
    }

    public static boolean isSystemApplication(Context context) {
        return isSystemApplication(context, context.getPackageName());
    }

    public static boolean isSystemApplication(Context context, String packageName) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context.getPackageManager(), packageName);
    }

    public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
        if (packageManager == null || TextUtils.isEmpty(packageName)) {
            return false;
        }

        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (NameNotFoundException e) {
            Flog.e(e);
        }
        return false;
    }

    /**
     * whether the app whost package's name is packageName is on the top of the stack
     * <ul>
     * <strong>Attentions:</strong>
     * <li>You should add <strong>android.permission.GET_TASKS</strong> in manifest</li>
     * </ul>
     * 
     * @param context
     * @param packageName
     * @return if params error or task stack is null, return null, otherwise retun whether the app is on the top of
     * stack
     */
    public static Boolean isTopActivity(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return null;
        }

        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        
        if (tasksInfo == null || tasksInfo.size() == 0) {
            return null;
        }
        
        try {
            return packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
        } catch (Exception e) {
        }
        
        return false;
    }
    
	public static boolean isApplicationInstalled(Context context, String packageName) {
		List<PackageInfo> pakageinfos = context.getPackageManager().
        		getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo pi : pakageinfos) {
            String pi_packageName = pi.packageName;
            if(packageName.endsWith(pi_packageName)){
                return true;
            }
        }
        return false;
	}

    /** 
     * 从APK中读取签名 
     * @param file 
     * @return 
     */  
    public static List<String> getSignaturesFromApk(File file) {
        List<String> signatures = new ArrayList<String>();
		try {
			JarFile jarFile = new JarFile(file);
			JarEntry je = jarFile.getJarEntry("AndroidManifest.xml");
            byte[] buffer = new byte[8 * 1024];
            Certificate[] certs = loadCertificates(jarFile, je, buffer);
            if(certs != null) {
                for(Certificate c: certs) {
                    String sig = toCharsString(c.getEncoded());
                    signatures.add(sig);
                }
            }
		} catch (Exception e) {
			Log.e(TAG, Flog.getCurrentMethodName() + " Exception", e);
		}
        return signatures;
    }
    
    /** 
     * 加载签名 
     * @param jarFile 
     * @param je 
     * @param buffer 
     * @return 
     */  
    private static Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] buffer) 
    		throws IOException {
    	InputStream is = jarFile.getInputStream(je);
    	try {
            while(is.read(buffer) != -1) {
            }
            return je != null ? je.getCertificates() : null;
        } finally {
        	IOUtils.close(is);
        }
    }
    
    /**
     * 获取签名信息
     * @param context
     * @return
     */
    public static Map<String, String> getSingnatureInfo(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signatures = packageInfo.signatures;
			Signature sign = signatures[0];
			return parseSignature(sign.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			Flog.e(TAG, e);
		}
		return null;
	}

    /**
     * 签名信息的转换
     * @param signature
     * @return
     * @throws CertificateException
     */
	private static Map<String, String> parseSignature(byte[] signature) throws CertificateException {
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
		String pubKey = cert.getPublicKey().toString();
		String signNumber = cert.getSerialNumber().toString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("public_key", pubKey);
		map.put("serial_number", signNumber);
		return map;
	}
    
    
    /** 
     * 将签名转成转成可见字符串 
     * @param sigBytes 
     * @return 
     */  
    private static String toCharsString(byte[] sigBytes) {
        byte[] sig = sigBytes;
        final int N = sig.length;
        final int N2 = N * 2;
        char[] text = new char[N2];
        for(int j=0; j < N; j++) {
            byte v = sig[j];
            int d = (v >> 4) & 0xf;
            text[j * 2] = (char)(d >= 10 ? ('a' + d - 10) : ('0' + d));
            d = v & 0xf;
            text[j * 2 + 1] = (char)(d >= 10 ? ('a' + d - 10) : ('0' + d));
        }
        return new String(text);
    }
    
	public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Flog.e("ApkIconLoader", e);
            }
        }
        return null;
    }

    /**
     * Installation return code<br/>
     * install success.
     */
    public static final int INSTALL_SUCCEEDED                              = 1;
    /**
     * Installation return code<br/>
     * the package is already installed.
     */
    public static final int INSTALL_FAILED_ALREADY_EXISTS                  = -1;

    /**
     * Installation return code<br/>
     * the package archive file is invalid.
     */
    public static final int INSTALL_FAILED_INVALID_APK                     = -2;

    /**
     * Installation return code<br/>
     * the URI passed in is invalid.
     */
    public static final int INSTALL_FAILED_INVALID_URI                     = -3;

    /**
     * Installation return code<br/>
     * the package manager service found that the device didn't have enough storage space to install the app.
     */
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE            = -4;

    /**
     * Installation return code<br/>
     * a package is already installed with the same name.
     */
    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE               = -5;

    /**
     * Installation return code<br/>
     * the requested shared user does not exist.
     */
    public static final int INSTALL_FAILED_NO_SHARED_USER                  = -6;

    /**
     * Installation return code<br/>
     * a previously installed package of the same name has a different signature than the new package (and the old
     * package's data was not removed).
     */
    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE             = -7;

    /**
     * Installation return code<br/>
     * the new package is requested a shared user which is already installed on the device and does not have matching
     * signature.
     */
    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE        = -8;

    /**
     * Installation return code<br/>
     * the new package uses a shared library that is not available.
     */
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY          = -9;

    /**
     * Installation return code<br/>
     * the new package uses a shared library that is not available.
     */
    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE          = -10;

    /**
     * Installation return code<br/>
     * the new package failed while optimizing and validating its dex files, either because there was not enough storage
     * or the validation failed.
     */
    public static final int INSTALL_FAILED_DEXOPT                          = -11;

    /**
     * Installation return code<br/>
     * the new package failed because the current SDK version is older than that required by the package.
     */
    public static final int INSTALL_FAILED_OLDER_SDK                       = -12;

    /**
     * Installation return code<br/>
     * the new package failed because it contains a content provider with the same authority as a provider already
     * installed in the system.
     */
    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER            = -13;

    /**
     * Installation return code<br/>
     * the new package failed because the current SDK version is newer than that required by the package.
     */
    public static final int INSTALL_FAILED_NEWER_SDK                       = -14;

    /**
     * Installation return code<br/>
     * the new package failed because it has specified that it is a test-only package and the caller has not supplied
     * the {@link #INSTALL_ALLOW_TEST} flag.
     */
    public static final int INSTALL_FAILED_TEST_ONLY                       = -15;

    /**
     * Installation return code<br/>
     * the package being installed contains native code, but none that is compatible with the the device's CPU_ABI.
     */
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE            = -16;

    /**
     * Installation return code<br/>
     * the new package uses a feature that is not available.
     */
    public static final int INSTALL_FAILED_MISSING_FEATURE                 = -17;

    /**
     * Installation return code<br/>
     * a secure container mount point couldn't be accessed on external media.
     */
    public static final int INSTALL_FAILED_CONTAINER_ERROR                 = -18;

    /**
     * Installation return code<br/>
     * the new package couldn't be installed in the specified install location.
     */
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION        = -19;

    /**
     * Installation return code<br/>
     * the new package couldn't be installed in the specified install location because the media is not available.
     */
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE               = -20;

    /**
     * Installation return code<br/>
     * the new package couldn't be installed because the verification timed out.
     */
    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT            = -21;

    /**
     * Installation return code<br/>
     * the new package couldn't be installed because the verification did not succeed.
     */
    public static final int INSTALL_FAILED_VERIFICATION_FAILURE            = -22;

    /**
     * Installation return code<br/>
     * the package changed from what the calling program expected.
     */
    public static final int INSTALL_FAILED_PACKAGE_CHANGED                 = -23;

    /**
     * Installation return code<br/>
     * the new package is assigned a different UID than it previously held.
     */
    public static final int INSTALL_FAILED_UID_CHANGED                     = -24;

    /**
     * Installation return code<br/>
     * if the parser was given a path that is not a file, or does not end with the expected '.apk' extension.
     */
    public static final int INSTALL_PARSE_FAILED_NOT_APK                   = -100;

    /**
     * Installation return code<br/>
     * if the parser was unable to retrieve the AndroidManifest.xml file.
     */
    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST              = -101;

    /**
     * Installation return code<br/>
     * if the parser encountered an unexpected exception.
     */
    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION      = -102;

    /**
     * Installation return code<br/>
     * if the parser did not find any certificates in the .apk.
     */
    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES           = -103;

    /**
     * Installation return code<br/>
     * if the parser found inconsistent certificates on the files in the .apk.
     */
    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;

    /**
     * Installation return code<br/>
     * if the parser encountered a CertificateEncodingException in one of the files in the .apk.
     */
    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING      = -105;

    /**
     * Installation return code<br/>
     * if the parser encountered a bad or missing package name in the manifest.
     */
    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME          = -106;

    /**
     * Installation return code<br/>
     * if the parser encountered a bad shared user id name in the manifest.
     */
    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID        = -107;

    /**
     * Installation return code<br/>
     * if the parser encountered some structural problem in the manifest.
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED        = -108;

    /**
     * Installation return code<br/>
     * if the parser did not find any actionable tags (instrumentation or application) in the manifest.
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY            = -109;

    /**
     * Installation return code<br/>
     * if the system failed to install the package because of system issues.
     */
    public static final int INSTALL_FAILED_INTERNAL_ERROR                  = -110;
    /**
     * Installation return code<br/>
     * other reason
     */
    public static final int INSTALL_FAILED_OTHER                           = -1000000;

    /**
     * Uninstall return code<br/>
     * uninstall success.
     */
    public static final int DELETE_SUCCEEDED                               = 1;

    /**
     * Uninstall return code<br/>
     * uninstall fail if the system failed to delete the package for an unspecified reason.
     */
    public static final int DELETE_FAILED_INTERNAL_ERROR                   = -1;

    /**
     * Uninstall return code<br/>
     * uninstall fail if the system failed to delete the package because it is the active DevicePolicy manager.
     */
    public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER            = -2;

    /**
     * Uninstall return code<br/>
     * uninstall fail if pcakge name is invalid
     */
    public static final int DELETE_FAILED_INVALID_PACKAGE                  = -3;

    /**
     * Uninstall return code<br/>
     * uninstall fail if permission denied
     */
    public static final int DELETE_FAILED_PERMISSION_DENIED                = -4;
    
    
    private PackageUtils() {/*Do not new me*/}
}
