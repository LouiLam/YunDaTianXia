package com.tlz.model;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import com.baidu.location.BDLocation;
import com.tlz.admin.LocationAdmin;
import com.tlz.admin.LocationAdmin.OnLocationListener;
import com.tlz.utils.AndroidHttp;
import com.tlz.utils.AndroidHttp.HttpCallback;
import com.tlz.utils.DeviceUtils;
import com.tlz.utils.Flog;

public class LocationService extends Service {
	private CountDownTimer mCutdownTimer = null;
	private int mRemainderTime = 60;
	private int count = 0;
	private int body = 0;

	private void handlerResult(String jsonString) {
		if (jsonString == null) {
			mRemainderTime = 300;
		} else {
			try {
				JSONObject objResult = new JSONObject(jsonString);
				if (objResult.getString("Code").equals("0000")) {
					body = objResult.getInt("Body");
				} else {
					body = 0;
				}
			} catch (JSONException e) {
				body = 0;
			}
			Flog.e(jsonString);
			count++;
			if (body != 0)
				mRemainderTime = body;
			else
				mRemainderTime = 300;
		}

		if (mCutdownTimer == null) {
			mCutdownTimer = new CountDownTimer(mRemainderTime * 1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					mRemainderTime--;
					Intent intent = new Intent("ticker.broadcast");
					intent.putExtra("mRemainderTime", mRemainderTime);
					intent.putExtra("count", count);
					sendBroadcast(intent);
					// mErrorNetTips.setText("离下次获取经纬度的时间还有，" + mRemainderTime
					// + "秒" + ",定位并且发送数据成功次数:" + count);
					Flog.e(mRemainderTime + "秒");
				}

				@Override
				public void onFinish() {
					mRemainderTime = 0;
					stopTimer();
					initLocation();
				}
			};
			mCutdownTimer.start();
		}

	}

	private void stopTimer() {

		if (mCutdownTimer != null) {
			mCutdownTimer.cancel();
			mCutdownTimer = null;
		}
	}

	private void initLocation() {
		LocationAdmin.getInstance(this).startLocation(new OnLocationListener() {

			@Override
			public void onLocation(BDLocation location) {
				// mLocation = location;
				if (location.getProvince() == null) {
					handlerResult(null);
				} else {
					JSONObject obj = null;
					try {
						obj = new JSONObject();
						obj.put("Cellphone", Myself.UserName);
						obj.put("SourceId", "233455");
						obj.put("DeviceCode",
								DeviceUtils.getIMEI(getApplicationContext()));
						obj.put("Longitude", location.getLongitude());
						obj.put("Latitude", location.getLatitude());
					} catch (Exception e) {
					}
					Flog.e(obj.toString());
					AndroidHttp.getInstance().doRequest(
							"http://120.24.234.112/service/gps",
							obj.toString(), new HttpCallback() {
								@Override
								public void onRequestSuccess(String tag,
										String jsonString) {
									handlerResult(jsonString);
								}

								@Override
								public void onRequestFinish(String tag) {
								}

								@Override
								public void onRequestFailure(String tag,
										int errorCode) {
									handlerResult(null);
								}
							});
					// sendLocSucMessage(location);
					// myLocation=String.format("%s %s %s",
					// location.getProvince(),location.getCity(),location.getDistrict());
				}

			}

			@Override
			public void onLocationFailure(int arg0) {
				handlerResult(null);
			}

		});
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Flog.e("onCreate");
		initLocation();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null)
			return super.onStartCommand(intent, flags, startId);
		Flog.e("onStartCommand");
		return START_NOT_STICKY;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Flog.e("onDestroy");
	}
}