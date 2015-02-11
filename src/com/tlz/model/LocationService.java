package com.tlz.model;

import org.json.JSONArray;
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
import com.tlz.utils.AndroidUtils;
import com.tlz.utils.DeviceUtils;
import com.tlz.utils.Flog;
import com.tlz.utils.PrefsUtils;
import com.tlz.utils.TimeUtils;

public class LocationService extends Service {
	private CountDownTimer mCutdownTimer = null;
	private int mRemainderTime = 60;
	private int count = 0;
	private int body = 0;
	private double Longitude, Latitude;
	private boolean isFirst = true;

	/**
	 * 
	 * @param isClear 为false表示写缓存，为true表示判断缓存，返回缓存数据
	 * @return
	 */
	private String writeCache(boolean isClear) {

		
		JSONObject Record = new JSONObject();
		JSONObject Source = new JSONObject();
		try {
			Source.put("Cellphone", Myself.UserName);
			Source.put("Id", "233455");
			Source.put("DeviceCode",
					DeviceUtils.getIMEI(getApplicationContext()));
			JSONObject Point = new JSONObject();
			Point.put("Longitude", Longitude);
			Point.put("Latitude", Latitude);
			Record.put("Source", Source);
			Record.put("Point", Point);
			Record.put("DeviceTime", TimeUtils.getCurrentDateTime());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		String result = AndroidUtils.readFileData("LocationService", this);
		JSONArray array =null;
		if (result == null)// 表示没有这个文件
		{
			//为true表示不写缓存 只是判断有无缓存。
			if (isClear) {return null;}
			array = new JSONArray();
			array.put(Record);
			AndroidUtils.writeFileData("LocationService", array.toString(),
					this);
		} else {
			
			try {
				JSONArray temp =null;
				array = new JSONArray(result);
				array.put(Record);
				if (array.length() > 50) {
					 temp = new JSONArray();
					for (int i = 1; i < array.length(); i++) {
						temp.put(array.get(i));
					}
				}
				//为true表示不写缓存 只是判断有无缓存。这里表示有缓存，返回缓存内容  这里要先判断 返回出去 然后才能执行下面的写代码
				if (isClear) {
					JSONObject obj=new JSONObject();
					try {
						obj.put("Positions", temp==null?array:temp);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return obj.toString();
				}
				//如果不是判断缓存，就可以写缓存
				AndroidUtils.writeFileData("LocationService", array.toString(),
						this);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}

	private void handlerResult(String jsonString) {
		
		
		if (jsonString == null) {
			mRemainderTime = 300;
			if(Longitude!=0||Latitude!=0) 
			writeCache(false);
		} else if(jsonString.length()>0) {//空串表示定位失败 要做判断处理
			try {
				JSONObject objResult = new JSONObject(jsonString);
				if (objResult.getString("Code").equals("0000")) {
					body = objResult.getInt("Body");
					count++;
					//如果上传数据成功   删除缓存文件
					deleteFile("LocationService");
				} else {
					body = 0;
					if(Longitude!=0||Latitude!=0) 
					writeCache(false);
				}
			} catch (JSONException e) {
				body = 0;
				if(Longitude!=0||Latitude!=0) 
				writeCache(false);
			}

			if (body != 0) {
				mRemainderTime = body;
			} else {
				mRemainderTime = 300;
			}
		}
		else
		{
			Longitude=0;Latitude=0;
		}
//		mRemainderTime = 20;
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
					postData();
				}
			};
			mCutdownTimer.start();
		}

	}

	// {
	// Positions:
	// [
	// {
	// Source:
	// {
	// Cellphone:"13319514511",
	// Id:"233455",
	// DeviceCode:"AEBBF994333"
	// },
	// Point:
	// {
	// Longitude:116.403857,
	// Latitude:39.915285
	// },
	// DeviceTime:'2015-2-6 9:00'
	// },
	// {
	// Source:
	// {
	// Cellphone:"13319514511",
	// Id:"233455",
	// DeviceCode:"AEBBF994333"
	// },
	// Point:
	// {
	// Longitude:116.403857,
	// Latitude:39.915285
	// },
	// DeviceTime:'2015-2-6 9:00'
	// },
	// ]
	// }
	private String jsonInstall() {
		try {
			JSONObject obj = new JSONObject();
			JSONArray array = new JSONArray();
			JSONObject Record = new JSONObject();
			JSONObject Source = new JSONObject();
			Source.put("Cellphone", Myself.UserName);
			Source.put("Id", "233455");
			Source.put("DeviceCode",
					DeviceUtils.getIMEI(getApplicationContext()));
			JSONObject Point = new JSONObject();
			Point.put("Longitude", Longitude);
			Point.put("Latitude", Latitude);

			Record.put("Source", Source);
			Record.put("Point", Point);
			Record.put("DeviceTime", TimeUtils.getCurrentDateTime());
			array.put(Record);
			obj.put("Positions", array);
			return obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void postData() {
		String result =null;
		if(Longitude!=0||Latitude!=0) 
		{result = jsonInstall();
		//发送数据前 判断是否有缓存内容，如果有就取出缓存内容，没缓存则为null
		String temp=writeCache(true);
		if(temp!=null) //有缓存内容
			result=temp;
		}
		Flog.e(result);
		AndroidHttp.getInstance().doRequest(
				"http://120.24.234.112/service/gps/batch", result,
				new HttpCallback() {
					@Override
					public void onRequestSuccess(String tag, String jsonString) {
						handlerResult(jsonString);
					}

					@Override
					public void onRequestFinish(String tag) {
					}

					@Override
					public void onRequestFailure(String tag, int errorCode) {
						handlerResult(null);
					}
				});
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
				Longitude = location.getLongitude();
				Latitude = location.getLatitude();
				Flog.e(Longitude + "," + Latitude);
				if (isFirst) {
					isFirst = false;
					postData();
				}
			}

			@Override
			public void onLocationFailure(int arg0) {
				handlerResult("");
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
		// if (intent == null)
		// return super.onStartCommand(intent, flags, startId);
		if (intent != null) {
			Myself.UserName = intent.getStringExtra("username");
			if(Myself.UserName!=null&&(!Myself.UserName.equals("null")))
			{PrefsUtils.putValue(this, "config", "username", Myself.UserName);}
		} else {
			Myself.UserName = PrefsUtils.getString(this, "config", "username",
					null);
		}
		Flog.e("onStartCommand:" + Myself.UserName);
		return START_STICKY;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Flog.e("onDestroy");
	}
}