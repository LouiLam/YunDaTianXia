package com.net;

import java.io.File;

import android.content.Context;
import android.os.AsyncTask;

import com.tlz.shipper.R;
import com.tlz.shipper.ui.BaseLoadingDialog;
import com.tlz.shipper.ui.BaseLoadingDialog.OnTimeoutListener;
import com.tlz.utils.HttpUtils;
import com.tlz.utils.ToastUtils;
/**
 * 上传参数:无
 * 返回格式
 *{"Code":"0000","Message":"执行成功","Body":"http://quickload.oss-cn-shenzhen.aliyuncs.com/5bfb071ebdd94481ae9aee1cb630c34e.png"}
 */
public class NetUploadAsyncTask extends AsyncTask<File, Integer, Integer> {
	private APIListener listener;
	BaseLoadingDialog dia;
	private final Context context;
	String json;
	public NetUploadAsyncTask(APIListener listener, Context context) {
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
	protected Integer doInBackground(File... params) {
		File file = params[0];
		try {
			json= HttpUtils.uploadFile(Urls.UPLOAD, null, file);
			// api.regist(sid,"000000","123456")
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static interface APIListener {
		public void finish(String json);
	}
}
