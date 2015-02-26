package com.tlz.admin;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationAdmin implements BDLocationListener {

	private static LocationAdmin mInstance;
	private LocationClient mLocationClient;
	private OnLocationListener mOnLocationListener;

	private LocationAdmin(Context context) {
		mLocationClient = new LocationClient(context.getApplicationContext());
	}

	public static LocationAdmin getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new LocationAdmin(context);
		}
		return mInstance;
	}


	public void startLocation(OnLocationListener listener) {
		mOnLocationListener = listener;
		mLocationClient.unRegisterLocationListener(this);
		initLocation();
		mLocationClient.registerLocationListener(this);
		mLocationClient.start();
	}

	public void stopLocation() {
		mLocationClient.stop();
		mLocationClient.unRegisterLocationListener(this);
	}

	public int getScanSpan() {
		return scanSpan;
	}

	public void setScanSpan(int scanSpan) {
		this.scanSpan = scanSpan*1000;
	}

	public LocationMode getMode() {
		return mode;
	}

	public void setMode(LocationMode mode) {
		this.mode = mode;
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(mode);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(scanSpan);// 当<1000(1s)时，定时定位无效,为主动单次请求
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		mLocationClient.setLocOption(option);
	}
	private int scanSpan=60000;//扫描间隔 当<1000(1s)时，定时定位无效,为主动单次请求
	/**
	 * 定位模式 分为高精度定位模式 低功耗定位模式 仅设备定位模式 
	 * 高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果； 
	 * 低功耗定位模式：这种定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位） 
	 * 仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
	 */
	private  LocationMode mode=LocationMode.Hight_Accuracy;//定位模式
	public interface OnLocationListener {
		public void onLocation(BDLocation location);

		public void onLocationFailure(int errorCode);
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;
		int type = location.getLocType();
		switch (type) {
		case BDLocation.TypeGpsLocation:
//		case BDLocation.TypeCacheLocation:
//		case BDLocation.TypeOffLineLocation:
		case BDLocation.TypeNetWorkLocation:
//		case BDLocation.TypeOffLineLocationNetworkFail:
			if (mOnLocationListener != null) {
				mOnLocationListener.onLocation(location);
			}
			break;
		default:
			if (mOnLocationListener != null) {
				mOnLocationListener.onLocationFailure(type);
			}
		}
//		stopLocation();
	}
}
