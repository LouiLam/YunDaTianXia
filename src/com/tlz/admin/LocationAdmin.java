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

	// public String getCurrentLocation() {
	// Criteria c = new Criteria();
	// c.setAccuracy(Criteria.ACCURACY_FINE);
	// c.setBearingRequired(false);
	// c.setAltitudeRequired(false);
	// c.setCostAllowed(true);
	// c.setPowerRequirement(Criteria.POWER_LOW);
	//
	// String provider = mLocationManager.getBestProvider(c, true);
	// Location location = mLocationManager.getLastKnownLocation(provider);
	// if (location == null) {
	// return "无法获取当前位置!";
	// } else {
	// return location.getLatitude() + "," + location.getLongitude();
	// }
	// }

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

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

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
		case BDLocation.TypeCacheLocation:
		case BDLocation.TypeOffLineLocation:
		case BDLocation.TypeNetWorkLocation:
		case BDLocation.TypeOffLineLocationNetworkFail:
			if (mOnLocationListener != null) {
				mOnLocationListener.onLocation(location);
			}
			break;
		default:
			if (mOnLocationListener != null) {
				mOnLocationListener.onLocationFailure(type);
			}
		}
		stopLocation();
	}
}
