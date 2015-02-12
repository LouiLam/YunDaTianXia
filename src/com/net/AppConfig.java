package com.net;

public class AppConfig {
	public static final byte TYPE_PC = 0;
	public static final byte TYPE_ANDROID = 1;
	public static final byte TYPE_IOS = 2;
	public static final byte TYPE_WINPHONE = 3;
	public static final byte TYPE_OTHER = 4;
	/** 未激活 */
	public static final byte STATUS_NOACTIVED = 0;
	/** 已激法 */
	public static final byte STATUS_ACTIVED = 1;
	/** 锁定 */
	public static final byte STATUS_LOCKED = 2;
	public static boolean DEBUG = false;// test
}
