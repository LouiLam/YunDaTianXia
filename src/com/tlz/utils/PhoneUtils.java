package com.tlz.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.text.format.Formatter;

public class PhoneUtils {
	
	private static final boolean DEBUG = false;
	
	public static final int FLAG_SYSTEM = 1;
	public static final int FLAG_NOT_SYSTEM = 2;
	
	public static final String CHINA_MOBILE = "中国移动";
	public static final String CHINA_UNICOM = "中国联通";
	public static final String CHINA_TELECOM = "中国电信";
	
	/**
	 * 获取网络运营商名称
	 * @link #permission:android.permission.READ_PHONE_STATE
	 * @param
	 * @return
	 */
	public static String getOperatorName(Context context) {
		
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		
		try {
			if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
				return CHINA_TELECOM;
			} else {
				String str = tm.getNetworkOperator();
				if ("46000".equals(str)
						|| "46002".equals(str)
						|| "46007".equals(str)) {
					return CHINA_MOBILE;
				} else if ("46001".equals(str)) {
					return CHINA_UNICOM;
				} else if ("46003".equals(str)) {
					return CHINA_TELECOM;
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取运营商编号
	 * @param context
	 * @return
	 */
	public static String getOperatorId(Context context) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
				return "cdma";
			} else {
				return tm.getNetworkOperator();
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获取ICCID
	 * 
	 * 中国移动编码格式:89 86 00 M F SS YY G XXXXXX P
	 * 89:国际编号
	 * 86:国家编号
	 * 00:运营商编号		00代表中国移动, 01代表中国联通, 03代表中国电信
	 * M:号段对应号码前三位		0:159	1:158	2:150	3:151	4-9:134-139   A:157	  B:188	  C:152   D:147   E:187   F:187
	 * F:用户号码第四位
	 * SS:省编号			01:北京   02:天津   03:河北   04:山西   05:内蒙古   06:辽宁   07:吉林   08:黑龙江   09:上海   10:江苏
	 * 					11:浙江   12:安徽   13:福建   1
	 * 4:江西   15:山东   16:河南   17:湖北   18:湖南   19:广东   20:广西
	 * 					21:海南   22:四川   23:贵州   24:云南   25:西藏   26:陕西   27:甘肃   28:青海   29:宁夏   30:新疆   
	 * 					31:重庆
	 * YY:编制ICCID时年号的后两位
	 * G:SIM卡供应商代码
	 * XXXXXX:用户识别码
	 * P:校验码
	 * 
	 * 中国联通编码格式:89 86 01 YY M SS XXXXXXXX P
	 * SS:省编号			10:内蒙古   11:北京   13:天津   17:山东   18:河北   19:山西   30:安徽   31:上海   34:江苏   36:浙江  
	 * 					38:福建   50:海南   51:广东   59:广西   70:青海   71:湖北   74:湖南   75:江西   76:河南   79:西藏    
	 * 					81:四川   83:重庆   84:陕西   85:贵州    86:云南    87:甘肃   88:宁夏   89:新疆   90:吉林   91:辽宁   97:黑龙江
	 * M:默认为8
	 * XXXXXXXX:卡商生产的顺序编码
	 * 
	 * 中国电信编码格式:89 86 03 M YY HHH XXXXXXXX
	 * M:保留位 默认为0
	 * HHH:省份区号
	 * XXXXXXXX:流水号
	 * @param context
	 * @return
	 */
	public static String getICCID(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getSimSerialNumber();
		} catch (Exception e) {
			Flog.e(e);
		}
		return "";
	}

	/**
	 * 获取IMSI
	 * Requires Permission:
	 * {@link android.Manifest.permission#READ_PHONE_STATE}
	 * @param
	 * @return
	 */
	public static String getIMSI(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getSubscriberId() == null ? "" : tm.getSubscriberId();
		} catch (SecurityException e) {
			
		}
		return "";
	}
	
	/**
	 * 获取IMEI
	 * Requires Permission:
	 * {@link android.Manifest.permission#READ_PHONE_STATE}
	 * @param
	 * @return
	 */
	public static String getIMEI(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getDeviceId() == null ? "" : tm.getDeviceId();
		} catch (SecurityException e) {
			Flog.e("Requires android.Manifest.permission#READ_PHONE_STATE");
		}
		return "";
	}

	/**
	 * 判断sim卡是否准备好
	 * @param tm
	 * @return
	 */
	public static boolean isSimReady(Context context) {
		TelephonyManager tm = (TelephonyManager)context
				.getSystemService(Context.TELEPHONY_SERVICE); 
		if (!(tm.getSimState() == TelephonyManager.SIM_STATE_READY)) {
			return false;
		}
		return true;
	}

    /*
     * 获取当前的手机号
     */
    public static String getPhoneNumber(Context context) {
    	try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String phoneNumber = tm.getLine1Number();
			if (!TextUtils.isEmpty(phoneNumber)) {
				if (phoneNumber.startsWith("+")) {
					phoneNumber = tm.getLine1Number().substring(3);
				}
				if (VerifyUtils.isPhoneNumber(phoneNumber)) {
					return phoneNumber;
				}
			}
		} catch (SecurityException e) {
			if (DEBUG) Flog.e(e);
		} catch (Exception e) {
			if (DEBUG) Flog.e(e);
		}
		return "";
    }
	
    public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED
							|| info[i].getState() == NetworkInfo.State.CONNECTING) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取IPv4
	 * @param useIPv4
	 * @return
	 */
	public static String getLocalIpAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> niList = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : niList) {
                List<InetAddress> iaList = Collections.list(intf.getInetAddresses());
                for (InetAddress ia : iaList) {
                    if (!ia.isLoopbackAddress()) {
                        String sAddr = ia.getHostAddress().toUpperCase(Locale.CHINESE);
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (useIPv4) {
                            if (isIPv4) {
                                return sAddr;
                            }
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%');
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { 
        }
        return "";
    }
	
	/**
	 * 获取蓝牙地址
	 * @param context
	 * @return
	 */
	public static String getBluetoothAddress(Context context) {
		String address = "";
		try {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if (adapter != null) {
				address = adapter.getAddress() == null ? "" : adapter.getAddress();
			}
		} catch (SecurityException e) {
			
		} catch (Exception e) {
			
		}
		return address;
	}

	/**
	 * 获取MAC地址(未申请相应的权限,wifi未打开状态下获取不到)!
	 * @param
	 * @return
	 */
	public static String getMacAddress() {
		String macAddress = "";
        String str = "";
        try {
        	Process process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
        	InputStreamReader ir = new InputStreamReader(process.getInputStream());
        	LineNumberReader input = new LineNumberReader(ir);
        	for (; str != null; ) {
        		str = input.readLine();
        		if (str != null) {
        			macAddress = str.trim();
        			break;
        		}
        	}
        } catch (IOException ex) {
        	
        }
        return macAddress;
	}
	
	/**
	 * 获取MAC地址
	 * @param
	 * @return
	 */
	public static String getMacAddress(Context context) {
		try {
			WifiManager wManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wManager.getConnectionInfo();
			if (info != null) {
				return info.getMacAddress() == null ? "" : info.getMacAddress();
			}
		} catch (SecurityException e) {
			return getMacAddress();
		}
		return "";
	}
	

    /** 
     * 判断当前网络是否是wifi网络
     * @param context
     * @return boolean
     */  
    public static boolean isWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
    
    
    /** 
     * 判断当前网络是否3G网络
     * @param context
     * @return boolean
     */  
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {  
            return true;
        }
        return false;
    }

    
    @TargetApi(9)
	public static String getSerial() {
    	return Build.VERSION.SDK_INT >=9 ? Build.SERIAL : "0";
    }
    
    /**
     * 卸载apk
     * @param context
     * @param packageName
     */
	public static void uninstallApk(Context context, String packageName) {
		Uri uri = Uri.parse("package:" + packageName);  
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);  
        context.startActivity(intent);
	}
    
	/**
	 * 获取系统app运行内存
	 * @param context
	 */
    public static void getSystemMemory(Context context) {
    	ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);    
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();   
        activityManager.getMemoryInfo(info);
     
        Flog.i("系统剩余内存:" + Formatter.formatFileSize(context, info.availMem));   
        Flog.i("系统是否处于低内存运行：" + info.lowMemory);
        Flog.i("当系统剩余内存低于" + Formatter.formatFileSize(context, info.threshold) + "时就看成低内存运行");
    }
    
	/**
	 * 获取网络类型
	 * @param tm
	 * @return
	 */
	public static int getNetworkType(Context context) {
		TelephonyManager tManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getNetworkType();
	}
	
	public static String getNetworkTypeName(int type) {
		switch (type) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return "GPRS";
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return "EDGE";
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return "HSPA";
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return "HSPA+";
		case TelephonyManager.NETWORK_TYPE_LTE:
			return "LTE";
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return "CDMA 1xRTT";
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return "CDMA EHRPD";
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return "CDMA EVDO-0";
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return "CDMA EVDO-A";
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return "CDMA EVDO-B";
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return "IDEN";
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return "HSDPA";
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return "HSUPA";
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return "UMTS";
		default:
			return "UNKNOWN";
		}
	}
	
	/**
	 * 获取基站编号
	 * User Permission:
	 * 				{@link android.Manifest.permission#ACCESS_COARSE_LOCATION}
	 * @param context
	 */
	public static int getCid(Context context) {
		TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		CellLocation location = tManager.getCellLocation();
		if(location==null)return -1;
		if (location instanceof GsmCellLocation) {
			return ((GsmCellLocation)location).getCid();
		} else {
			return ((CdmaCellLocation)location).getBaseStationId();
		}
	}
	
	/**
	 * 获取位置区域码
	 * User Permission:
	 * 				{@link android.Manifest.permission#ACCESS_COARSE_LOCATION}
	 * @param context
	 */
	public static int getLac(Context context) {
		TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		CellLocation location = tManager.getCellLocation();
		if (location instanceof GsmCellLocation) {
			return ((GsmCellLocation)location).getLac();
		} else {
			return ((CdmaCellLocation)location).getNetworkId();
		}
	}
	
	/**
	 * 
	 * 获取相邻基站信息
	 * User Permission:
	 * 				{@link android.Manifest.permission#ACCESS_COARSE_LOCATION} 
	 * @param context
	 * @return
	 */
	public static List<NeighboringCellInfo> getNeighboringCellInfo(Context context) {
		TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getNeighboringCellInfo();
	}
	
	private PhoneUtils(){/*Do not new me*/};
	
}
