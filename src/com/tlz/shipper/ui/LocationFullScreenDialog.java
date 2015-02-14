package com.tlz.shipper.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClientOption.LocationMode;
import com.tlz.admin.LocationAdmin;
import com.tlz.model.LocationService;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.utils.ToastUtils;

public class LocationFullScreenDialog extends Dialog {

	// private AnimationDrawable mLoadingAnim;

	public LocationFullScreenDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
private LocationMode Modes[]={LocationMode.Hight_Accuracy,LocationMode.Battery_Saving,LocationMode.Device_Sensors};
private LocationMode cur=LocationMode.Hight_Accuracy;
	public LocationFullScreenDialog(Context context, int theme) {
		super(context, theme);
		getWindow().setGravity(Gravity.FILL);
		this.setContentView(R.layout.dialog_location);
		this.setCanceledOnTouchOutside(false);
		Spinner mSpinner = (Spinner) findViewById(R.id.spinner1);
		// 建立数据源
		String[] mItems = context.getResources().getStringArray(R.array.test);
		// 建立Adapter并且绑定数据源
//		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, mItems);
		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(context,R.layout._test_item, mItems);
		//绑定 Adapter到控件
		mSpinner.setAdapter(_Adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view,
		            int position, long id) {
//		        String str=parent.getItemAtPosition(position).toString();
		        cur= Modes[position];
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		        // TODO Auto-generated method stub
		    }
		});
		final TextView message=(TextView) findViewById(R.id.message);
		findViewById(android.R.id.text1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
						try {
							Integer.parseInt(message.getText().toString());
						} catch (Exception e) {
							return;
						}
						LocationAdmin.getInstance(getContext()).setScanSpan(Integer.parseInt(message.getText().toString()));
						LocationAdmin.getInstance(getContext()).setMode(cur);
						Intent intent=new Intent(getContext(), LocationService.class);
						intent.putExtra("username", Myself.UserName);
						getContext().startService(intent);
						ToastUtils.show(getContext(), "定位服务开启成功");
					}
				});

	}

	public LocationFullScreenDialog(Context context) {
		super(context);
	}

	public static LocationFullScreenDialog show(Context context) {
		LocationFullScreenDialog dialog = new LocationFullScreenDialog(context,
				R.style.Lam_Dialog_FullScreen);
		dialog.show();
		return dialog;
	}

	// public void setMessage(String msg) {
	// mMessage = msg;
	// }

	@Override
	public void dismiss() {
		// setMessage("");
		// if (mLoadingAnim != null) {
		// mLoadingAnim.stop();
		// }
		super.dismiss();
	}

}
