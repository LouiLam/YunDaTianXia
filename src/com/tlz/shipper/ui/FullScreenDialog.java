package com.tlz.shipper.ui;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.net.AppConfig;
import com.net.NetShipperMsgAsyncTask;
import com.net.ShipperAccountApi;
import com.net.Urls;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.common.CompleteEnterpriseInfoActivity;
import com.tlz.utils.Flog;
import com.tlz.utils.ToastUtils;


public class FullScreenDialog extends Dialog {

//	private AnimationDrawable mLoadingAnim;

	public FullScreenDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public FullScreenDialog(Context context, int theme) {
		super(context, theme);
		getWindow().setGravity(Gravity.FILL);
		this.setContentView(R.layout.dialog_fullscreen);
		this.setCanceledOnTouchOutside(false);
		findViewById(android.R.id.text2).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		findViewById(android.R.id.text1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				if(AppConfig.DEBUG)
				{getContext().startActivity(new Intent(getContext(), CompleteEnterpriseInfoActivity.class));}
				else
				{
					new NetShipperMsgAsyncTask(new NetShipperMsgAsyncTask.APIListener() {

						@Override
						public String handler(ShipperAccountApi api) {
							return api.getShipper(Myself.ShipperId);

						}

						@Override
						public void finish(String json) {
							Flog.e(json);
					
							try {
								JSONObject obj = new JSONObject(json);
							
								if (obj.getInt("resultCode") == 1) {
									JSONObject data=obj.getJSONObject("data");
									String website = data
											.getString("website"); //暂无用
									String taxregistno=data
											.getString("taxregistno");//营业执照URL
									String introduce=data
											.getString("introduce");//
									String qrCode=data.getString("qrCode");
									Myself.ContactName = data.getString("contact");
									Myself.DetailAddress=data.getString("detailAddress");
									Myself.Location=data.getString("locationCode");
									int auditStatus=data.getInt("auditStatus");
									int cargoType=data.getInt("cargoType");
									int serialVersionUID=data.getInt("serialVersionUID");
									Myself.FullName=data.getString("fullName");
									String organizationno=data.getString("organizationno");//组织机构代码证的URL
									String head=data.getString("head");
									String businesslicence=data.getString("businesslicence");
									getContext().startActivity(new Intent(getContext(), CompleteEnterpriseInfoActivity.class));
								} else {
									try {
										String error=obj.getString("error");
										ToastUtils.showCrouton(getContext(),
												error+":"+obj.getInt("resultCode"));
									} catch (Exception e) {
										ToastUtils.showCrouton(getContext(),
												getContext().getString(R.string.error)+obj.getInt("resultCode"));
									}
									
								}
							} catch (Exception e) {
								e.printStackTrace();
								ToastUtils.showCrouton(getContext(),
										getContext().getString(R.string.exception));
							}

						}
					}, getContext()).execute(Urls.REGEDIT);
				}
			
				
				
			
			}
		});
		
	}

	public FullScreenDialog(Context context) {
		super(context);
	}


	public static FullScreenDialog  show(Context context) {
		FullScreenDialog dialog = new FullScreenDialog(context,
				R.style.Lam_Dialog_FullScreen);
		dialog.show();
		return dialog;
	}


//	public void setMessage(String msg) {
//		mMessage = msg;
//	}


	@Override
	public void dismiss() {
//		setMessage("");
//		if (mLoadingAnim != null) {
//			mLoadingAnim.stop();
//		}
		super.dismiss();
	}


}
