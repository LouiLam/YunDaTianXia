package com.tlz.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.SparseArray;


public class ErrorUtils {

	public static final int CONNECT_STATUS_CONNECTING = 1;
	public static final int CONNECT_STATUS_AUTHENTICATING = 2;
	public static final int CONNECT_STATUS_OBTAINING_IPADDR = 3;
	public static final int CONNECT_STATUS_GS = 4;
	public static final int CONNECT_STATUS_STARTREQ = 5;
	public static final int CONNECT_STATUS_ENDREQ = 6;
	public static final int CONNECT_STATUS_LOGIN = 7;
	public static final int CONNECT_STATUS_PREPARE = 8;
	
	/**
	 *  成功
	 */
	public static final int SUCCESS 						= 0;//成功
	/**
	 * 未知错误
	 */
	public static final int ERROR_CODE_NONE 				= -1;
	/**
	 * 网络错误!请确认是否联网或者登陆
	 */
	public static final int ERROR_CODE_NETWORK 				= -2;
	/**
	 * 服务器错误!
	 */
	public static final int ERROR_CODE_SERVER				= -3;
	/**
	 * 未发现网络!
	 */
	public static final int ERROR_CODE_NETWORK_NOT_FOUND 	= -4;
	
	/**
	 * 开卡失败
	 */
	public static final int ERROR_CODE_OPEN_CARD 			= -5;
	/**
	 * 打开WIFI失败!
	 */
	public static final int ERROR_CODE_OPEN_WIFI 			= -6;
	/**
	 * 关闭WIFI失败!
	 */
	public static final int ERROR_CODE_CLOSE_WIFI 			= -7;
	
	/**
	 * 登录异常,请手动断开
	 */
	public static final int ERROR_CODE_NOLOGIN 				= -8;
	/**
	 * 请勿进行重复连接!
	 */
	public static final int ERROR_CODE_CONNECT_REPEAT 		= -9;
	/**
	 * 账户无流量
	 */
	public static final int ERROR_CODE_ACCOUNT_NO_TRAFFIC 	= -10;
	/**
	 * 账户无时长!
	 */
	public static final int ERROR_CODE_ACCOUNT_NO_TIME 		= -11;
	
	/**
	 * SDK错误!
	 */
	public static final int ERROR_CODE_SDK 					= -12;

	/**
	 * 信号太弱!
	 */
	public static final int ERROR_CODE_WIFI_SINGAL 			= -14;
	/**
	 * WIFI连接断开失败!
	 */
	public static final int ERROR_CODE_WIFI_DISCONNECT 		= -15;
	/**
	 * 扫描认证网络错误
	 */
	public static final int ERROR_CODE_SCAN_REQ 			= -16;
	/**
	 * 帐号绑定错误!
	 */
	public static final int ERROR_CODE_LOGIN_REQ 			= -17;
	/**
	 * 没有找到认证网络,请尝试手动连接!
	 */
	public static final int ERROR_CODE_AUTH_NOT 			= -18;
	/**
	 * 获取不到凭证，请先进行登陆鉴权
	 */
	public static final int ERROR_CODE_AUTH_NULL 			= -19;
	/**
	 * 获取不到当前位置!
	 */
	public static final int ERROR_CODE_LOCATION 			= -20;
	/**
	 * 连接成功,但无法访问网络
	 */
	public static final int ERROR_CODE_NOTINTER 			= -21;
	/**
	 * 强制断开!
	 */
	public static final int ERROR_CODE_SDK_DISCONNECT 		= -22;
	/**
	 * 运营商连接凭证错误!
	 */
	public static final int ERROR_CODE_TELECOM_CONNECT 		= -23;
	/**
	 * 网络不存在!
	 */
	public static final int ERROR_CODE_WIFI_NOTEXIST 		= -24;
	/**
	 * session过期!
	 */
	public static final int ERROR_CODE_SESSION_OUTTIME 		= -25;
	/**
	 * 加密模式不匹配
	 */
	public static final int ERROR_CODE_SECURITY_NULL 		= -26;
	/**
	 * 连接超时!
	 */
	public static final int ERROR_CODE_CONNECT_TIMEOUT 		= -31;
	/**
	 * 密码验证错误!
	 */
	public static final int ERROR_CODE_CONNECT_AUTHENTICATE = -32;
	/**
	 * 无效连接!
	 */
	public static final int ERROR_CODE_CONNECT_INVALID 		= -33;
	/**
	 * WIFI未打开!
	 */
	public static final int ERROR_CODE_WIFI_NOT_OPEN 		= -34;
	/**
	 * 无用的WIFI!
	 */
	public static final int ERROR_CODE_WIFI_NOT_FOUND 		= -35;
	/**
	 * 认证网络已不存在，请重新刷新网络
	 */
	public static final int ERROR_CODE_NO_WIFI_CATCH 		= -36;
	/**
	 * 用户信息失效
	 */
	public static final int ERROR_CODE_SESSION_ERROR 		= -37;
	
	private static SparseArray<String> sErrorArray = new SparseArray<String>() {
		{
			put(SUCCESS, "成功");
			put(ERROR_CODE_NONE, "未知错误!");
			put(ERROR_CODE_NETWORK, "网络错误!请确认是否联网或者登陆");
			put(ERROR_CODE_SERVER, "服务器错误!");
			put(ERROR_CODE_NETWORK_NOT_FOUND, "未发现网络!");
			put(ERROR_CODE_OPEN_CARD, "开卡失败!");
			put(ERROR_CODE_OPEN_WIFI, "打开WIFI失败!");
			put(ERROR_CODE_CLOSE_WIFI, "关闭WIFI失败!");
			put(ERROR_CODE_NOLOGIN, "登录异常,请手动断开!");
			put(ERROR_CODE_CONNECT_REPEAT, "请勿进行重复连接!");
			put(ERROR_CODE_ACCOUNT_NO_TRAFFIC, "账户无流量");
			put(ERROR_CODE_ACCOUNT_NO_TIME, "账户无时长!");
			put(ERROR_CODE_SDK, "SDK错误!");
			put(ERROR_CODE_NOTINTER, "连接成功,但无法访问网络");
			put(ERROR_CODE_WIFI_SINGAL, "信号太弱!");
			put(ERROR_CODE_WIFI_DISCONNECT, "WIFI连接断开失败!");
			put(ERROR_CODE_SDK_DISCONNECT, "第三方SDK断开失败,强制断开!");
			put(ERROR_CODE_SCAN_REQ, "扫描认证网络错误!");
			put(ERROR_CODE_LOGIN_REQ, "帐号绑定错误!");
			put(ERROR_CODE_AUTH_NOT, "没有找到认证网络,请尝试手动连接!");
			put(ERROR_CODE_AUTH_NULL, "获取不到凭证，请先进行帐号绑定!");
			put(ERROR_CODE_LOCATION, "获取不到当前位置!");
			put(ERROR_CODE_TELECOM_CONNECT, "运营商连接凭证错误!");
			put(ERROR_CODE_WIFI_NOTEXIST, "网络无效或不存在!");
			put(ERROR_CODE_SESSION_OUTTIME, "session过期!");
			put(ERROR_CODE_SECURITY_NULL, "加密模式不匹配");
			put(ERROR_CODE_SESSION_ERROR, "无效的session");
			
			put(ERROR_CODE_CONNECT_TIMEOUT,"连接超时!");
			put(ERROR_CODE_CONNECT_AUTHENTICATE,"密码验证错误!");
			put(ERROR_CODE_CONNECT_INVALID,"无效连接!");
			put(ERROR_CODE_WIFI_NOT_OPEN,"WIFI未打开!");
			put(ERROR_CODE_WIFI_NOT_FOUND,"无用的WIFI!");
			put(ERROR_CODE_NO_WIFI_CATCH,"认证网络已不存在，请重新刷新网络");

			put(2002,"当前wifi不可用");
			put(2003,"热点不存在");
			put(2004,"绑定失败");
			put(2005,"服务器繁忙");
			put(2006,"设备不支持");
			put(2008,"请稍后重试");
			put(2009,"操作失败");
			put(2010,"当前人数太多");
			put(2011,"网络不可用");
			put(2013,"未绑定，请绑定后重试");
			put(2014,"网络繁忙");
			put(2015,"服务器错误");
			put(2016,"无法获取ip地址");
			put(2018,"没有上网剩余时长");
		}
	};

	public static String getMsg(int errorCode) {
		if (errorCode < 0 || errorCode >2000) {
//			if(errorCode>50000)
//				return ConServerErr.getMsg(errorCode);
			return sErrorArray.get(errorCode) == null ? sErrorArray.get(ERROR_CODE_NONE) : sErrorArray.get(errorCode);
		} 
//		else if (errorCode >0 && errorCode < 100){
//			return ChinaNetWifi.getInstance().codeMessage(errorCode);
//		} 
	else if (errorCode == 0){
			return "SUCCESS";
		} else {
			return "UNKNOWN!";
		}
		
	}
	
	public static String getConnectingStatusMsg(int statusCode) {
		switch (statusCode) {
		case CONNECT_STATUS_AUTHENTICATING:
			return "验证中...";
		case CONNECT_STATUS_CONNECTING:
			return "连接中...";
		case CONNECT_STATUS_OBTAINING_IPADDR:
			return "获取IP中...";
		case CONNECT_STATUS_GS:
			return "获取位置信息中...";
		case CONNECT_STATUS_STARTREQ:
			return "开始查询免费可用网络...";
		case CONNECT_STATUS_ENDREQ:
			return "查询免费可用网络结束...";
		case CONNECT_STATUS_LOGIN:
			return "开始登录,请稍等...";
		case CONNECT_STATUS_PREPARE:
			return "准备连接,请稍等...";
		}
		return "WIFI 连接中,请稍等!";
	}
	
	/**
	 * 打印异常堆栈
	 * @param ex
	 * @return
	 */
	public static String getErrorInfoFromException(Exception ex){
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}
