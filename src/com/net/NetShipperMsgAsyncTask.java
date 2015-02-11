package com.net;

import android.content.Context;
import android.os.AsyncTask;

import com.caucho.hessian.client.HessianProxyFactory;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.BaseLoadingDialog;
import com.tlz.shipper.ui.BaseLoadingDialog.OnTimeoutListener;
import com.tlz.utils.ToastUtils;

public class NetShipperMsgAsyncTask extends AsyncTask<String, Integer, Integer> {
	private APIListener listener;
	BaseLoadingDialog dia;
	private final Context context;
	String json;
	public NetShipperMsgAsyncTask(APIListener listener, Context context) {
		this.listener = listener;
		this.context = context;
	}

	@Override
	protected void onPostExecute(Integer result) {
		dia.dismiss();
		if(listener==null)return;
		listener.finish(json);
	}

	@Override
	protected void onPreExecute() {
		dia = BaseLoadingDialog.show(context, "", 30, new OnTimeoutListener() {

			@Override
			public void onTimeout() {
				ToastUtils.show(context,
						context.getString(R.string.connect_not_available));

			}
		});
	}

	@Override
	protected Integer doInBackground(String... params) {
		String url = params[0];
		try {
			
			HessianProxyFactory factory = new HessianProxyFactory();
			ShipperAccountApi api = (ShipperAccountApi) factory.create(
					ShipperAccountApi.class, url);
			if(listener==null)return -1;
			json=listener.handler(api);
			// api.regist(sid,"000000","123456")
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static interface APIListener {
		public String handler(ShipperAccountApi api);
		public void finish(String json);
	}
}
