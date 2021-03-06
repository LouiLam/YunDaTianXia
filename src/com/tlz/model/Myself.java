package com.tlz.model;

import java.util.Date;

public class Myself {
	public static  String UserName,Password;
	public static String  Location;
	/**
	 * 登录，注册成功返回的Token
	 */
	public static String  Token;
	public static String  PhoneNumber;
	public static String  FullName;
	public static String  ContactName;
	public static String  DetailAddress;
	/**
	 * 主要运输货物类型
	 */
	public static byte  CargoType;
	/**
	 * 营业执照
	 */
	public static String Businesslicence;
	/**
	 * 组织结构代码证
	 */
	public static String Organization;
	/**
	 * 税务登记证
	 */
	public static String Taxregist;
	/**
	 * 头像
	 */
	public static String  HeadIconUrl;
	/**
	 * 二维码URL
	 */
	public static String  QRUrl;
	/**
	 * 企业简介
	 */
	public static String  Introduction;
	/**
	 * 会员ID
	 */
	public static int MemberId;
	/**
	 * 信用等级
	 */
	public static int CreditGrad;
	/**
	 * 账户余额
	 */
	public static double Balance;
	/**
	 * 货主ID
	 */
	public static int ShipperId;
	/**
	 * 认证状态
	 * @see STATUS_NOACTIVED=0  未激活
	 * @see STATUS_ACTIVED=1 已激法 
	 * @see  STATUS_LOCKED=2  锁定
	 */
	public static int AuditStatus;
	
	/**
	 * 装货地
	 */
	public static String Loading;
	/**
	 * 卸货地
	 */
	public static String Unloading;
	
	/**
	 * 重量/体积/件数
	 */
	public static int Weight,Volume,Quantity;
	public static String WeightString;
	/**
	 * 发货日期，到达日期
	 */
	public static Date Send,Arrive;
	public static String SendString,ArriveString;
	
	
}
