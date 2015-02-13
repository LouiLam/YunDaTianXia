package com.tlz.shipper.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.net.AppConfig;
import com.net.NetAsyncFactory;
import com.net.NetAsyncFactory.ResultCodeSucListener;
import com.net.NetShipperMsgAsyncTask;
import com.net.ShipperAccountApi;
import com.net.Urls;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.common.ActivityCompleteEnterpriseInfo;
import com.tlz.utils.AndroidTextUtils;
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
				{getContext().startActivity(new Intent(getContext(), ActivityCompleteEnterpriseInfo.class));}
				else
				{
					
					NetAsyncFactory.createShipperTask(getContext(), new ResultCodeSucListener<ShipperAccountApi>() {
						
						@Override
						public void suc(JSONObject obj) throws JSONException{
							JSONObject data=obj.getJSONObject("data");
							Myself.Businesslicence=data.getString("businesslicence");
							Myself.Organization=data.getString("organizationno");
							Myself.Taxregist=data.getString("taxregistno");
							Myself.ContactName = data.getString("contact");
							if(!AndroidTextUtils.isEmpty(Myself.Businesslicence))
							{
								JSONObject Businesslicence=new JSONObject(Myself.Businesslicence);
								Myself.Businesslicence=Businesslicence.getString("url");
							}
							if(!AndroidTextUtils.isEmpty(Myself.Organization))
							{
								JSONObject Organization=new JSONObject(Myself.Organization);
								Myself.Organization=Organization.getString("url");
							}
							if(!AndroidTextUtils.isEmpty(Myself.Taxregist))
							{
								JSONObject Taxregist=new JSONObject(Myself.Taxregist);
								Myself.Taxregist=Taxregist.getString("url");
							}
//							String website = data
//							.getString("website"); //暂无用
//							String introduce=data
//									.getString("introduce");
//							String qrCode=data.getString("qrCode");
//							Myself.DetailAddress=data.getString("detailAddress");
//							Myself.Location=data.getString("locationCode");
//							int auditStatus=data.getInt("auditStatus");//激活状态
//							Myself.CargoType=(byte) data.getInt("cargoType");
//							int serialVersionUID=data.getInt("serialVersionUID");
//							Myself.FullName=data.getString("fullName");
//							Myself.HeadIconUrl=data.getString("head");
							getContext().startActivity(new Intent(getContext(), ActivityCompleteEnterpriseInfo.class));
						}
						
						@Override
						public String handler(ShipperAccountApi api) {
							return  api.getShipper(Myself.ShipperId);
						}
					}).execute(Urls.REGEDIT); 
				
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
