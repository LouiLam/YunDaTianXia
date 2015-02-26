package com.net;

import android.content.Context;
import android.os.AsyncTask;

import com.caucho.hessian.client.HessianProxyFactory;
import com.net.NetAsyncFactory.APIListener;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.BaseLoadingDialog;
import com.tlz.shipper.ui.BaseLoadingDialog.OnTimeoutListener;
import com.tlz.utils.ToastUtils;

public class NetMsgAsyncTask extends AsyncTask<String, Integer, Integer> {
	private APIListener<MessageApi> listener;
	BaseLoadingDialog dia;
	private final Context context;
	String json;
	public NetMsgAsyncTask(APIListener<MessageApi> listener, Context context) {
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
			MessageApi api = (MessageApi) factory.create(
					MessageApi.class, url);
			if(listener==null)return -1;
			json=listener.handler(api);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
