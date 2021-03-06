package com.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.tlz.shipper.R;
import com.tlz.utils.Flog;
import com.tlz.utils.ToastUtils;


public class NetAsyncFactory {
	public static interface APIListener<T> {
		
		public String handler(T api);
		public void finish(String json);
	}
	public static interface ResultCodeSucListener<T> {
		public String handler(T api);
		public void suc(JSONObject obj) throws JSONException;
	}
	/**
	 * 创建ShipperAccountApi相关接口
	 * 创建完后 需要调用execute()来执行  参数为Url
	 * @param context
	 * @param resultCodeListener
	 * @return
	 */
	public static NetShipperAsyncTask createShipperTask(final Context context,final ResultCodeSucListener<ShipperAccountApi> resultCodeListener)
	{
		
		
		return new NetShipperAsyncTask(new APIListener<ShipperAccountApi>() {

			@Override
			public String handler(ShipperAccountApi api) {
				return resultCodeListener.handler(api);

			}

			@Override
			public void finish(String json) {
				Flog.e(json);
		
				try {
					JSONObject obj = new JSONObject(json);
					if (obj.getInt("resultCode") == 1) {
						resultCodeListener.suc(obj);
					} else {
						try {
							String error=obj.getString("error");
							ToastUtils.showCrouton(context,
									error+":"+obj.getInt("resultCode"));
						} catch (Exception e) {
							ToastUtils.showCrouton(context,
									context.getString(R.string.error)+obj.getInt("resultCode"));
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showCrouton(context,
							context.getString(R.string.exception));
				}

			}
		}, context);
	}
	/**
	 * 创建ShipperAccountApi中三证首次上传的相关接口，这个返回值比较特殊 
	 * 创建完后 需要调用execute()来执行  参数为Url
	 * @param context
	 * @param resultCodeListener
	 * @return
	 */
	public static NetShipperAsyncTask createShipperTask3(final Context context,final ResultCodeSucListener<ShipperAccountApi> resultCodeListener)
	{
		
		
		return new NetShipperAsyncTask(new APIListener<ShipperAccountApi>() {

			@Override
			public String handler(ShipperAccountApi api) {
				return resultCodeListener.handler(api);

			}

			@Override
			public void finish(String json) {
				Flog.e(json);
				try {
					JSONObject obj = new JSONObject(json);
					if (obj.getInt("resultCode") >0) {
						resultCodeListener.suc(obj);
					} else {
						try {
							String error=obj.getString("error");
							ToastUtils.showCrouton(context,
									error+":"+obj.getInt("resultCode"));
						} catch (Exception e) {
							ToastUtils.showCrouton(context,
									context.getString(R.string.error)+obj.getInt("resultCode"));
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showCrouton(context,
							context.getString(R.string.exception));
				}

			}
		}, context);
	}
	/**
	 * 消息服务的调用，比如运单->新消息
	 * 创建完后 需要调用execute()来执行  里面参数名为Url
	 * @param context
	 * @param resultCodeListener
	 * @return
	 */
	public static NetMsgAsyncTask createMsgTask(final Context context,final ResultCodeSucListener<MessageApi> resultCodeListener)
	{
		
		
		return new NetMsgAsyncTask(new APIListener<MessageApi>() {

			@Override
			public String handler(MessageApi api) {
				return resultCodeListener.handler(api);

			}

			@Override
			public void finish(String json) {
				Flog.e(json);
		
				try {
					JSONObject obj = new JSONObject(json);
					if (obj.getInt("resultCode") == 1) {
						resultCodeListener.suc(obj);
					} else {
						try {
							String error=obj.getString("error");
							ToastUtils.showCrouton(context,
									error+":"+obj.getInt("resultCode"));
						} catch (Exception e) {
							ToastUtils.showCrouton(context,
									context.getString(R.string.error)+obj.getInt("resultCode"));
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showCrouton(context,
							context.getString(R.string.exception));
				}

			}
		}, context);
	}
	/**
	 * 基础服务的调用，比如手机验证码
	 * 创建完后 需要调用execute()来执行  里面参数名为Url
	 * @param context
	 * @param resultCodeListener
	 * @return
	 */
	public static NetCommonAsyncTask createCommonTask(final Context context,final ResultCodeSucListener<CommonApi> resultCodeListener)
	{
		
		
		return new NetCommonAsyncTask(new APIListener<CommonApi>() {

			@Override
			public String handler(CommonApi api) {
				return resultCodeListener.handler(api);

			}

			@Override
			public void finish(String json) {
				Flog.e(json);
		
				try {
					JSONObject obj = new JSONObject(json);
					if (obj.getInt("resultCode") == 1) {
						resultCodeListener.suc(obj);
					} else {
						try {
							String error=obj.getString("error");
							ToastUtils.showCrouton(context,
									error+":"+obj.getInt("resultCode"));
						} catch (Exception e) {
							ToastUtils.showCrouton(context,
									context.getString(R.string.error)+obj.getInt("resultCode"));
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showCrouton(context,
							context.getString(R.string.exception));
				}

			}
		}, context);
	}
	/**
	 * 上传到基础服务
	 * @param context
	 * @param resultCodeListener
	 * @return
	 */
	public static NetUploadAsyncTask createUploadTask(final Context context,final ResultCodeSucListener<Object> resultCodeListener)
	{
		
		return new NetUploadAsyncTask(new APIListener<Object>() {

			@Override
			public String handler(Object api) {
				return null;
			}

			@Override
			public void finish(String json) {
				Flog.e(json);
				
				try {
					JSONObject obj = new JSONObject(json);
					if (obj.getString("Code").equals("0000")) {
						resultCodeListener.suc(obj);
					} else {
						try {
							String error=obj.getString("Message");
							ToastUtils.showCrouton(context,
									error+":"+obj.getInt("Code"));
						} catch (Exception e) {
							ToastUtils.showCrouton(context,
									context.getString(R.string.error)+obj.getInt("Code"));
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.showCrouton(context,
							context.getString(R.string.exception));
				}
				
			}
		}, context);
	}
}
