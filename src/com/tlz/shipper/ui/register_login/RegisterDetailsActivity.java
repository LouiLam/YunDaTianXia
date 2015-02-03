package com.tlz.shipper.ui.register_login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.common.GoodsActivity;
import com.tlz.shipper.ui.common.ImageGridPickerActivity;
import com.tlz.shipper.ui.widget.TextViewBarIcon;
import com.tlz.shipper.ui.widget.TextViewBarPure;
import com.tlz.shipper.ui.widget.ViewBar.TBBarOnClickListener;

public class RegisterDetailsActivity extends ThemeActivity  {
	TextViewBarIcon bar_logo,qrcode;
	TextViewBarPure fullName, contactName ,location,locationDetail,transportGoods,enterpriseIntroduction;
	public static final int REQUEST_CODE_IMAGE_SELECT=0;
	public static final int REQUEST_CODE_ITEM_RETURN=1;
	public static final int REQUEST_CODE_AREA=2;
	public static final int REQUEST_CODE_GOODS=3;
	
	
	public static final int IEMT_TYPE_FULL_NAME=0;
	public static final int IEMT_TYPE_CONTACT_NAME=1;
	public static final int IEMT_TYPE_LOCATION_DETAIL=2;
	public static final int IEMT_TYPE_ENTERPRISE_INTRODUCTION=3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar.setTitle(R.string.register_details_title);
		setContentView(R.layout.activity_register_details);
		initView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		if (requestCode == REQUEST_CODE_IMAGE_SELECT && resultCode == RESULT_OK) {
			Bitmap bm = data.getParcelableExtra("bitmap");
			bar_logo.setIconRightBack(bm);
			// ToastUtils.show(RegisterDetailsActivity.this, uri.toString());

		}
		else if(requestCode == REQUEST_CODE_ITEM_RETURN && resultCode == RESULT_OK)
		{
			Bundle bundle=data.getExtras();
			int curType=bundle.getInt("item_type");
			switch (curType) {
			case  RegisterDetailsActivity.IEMT_TYPE_FULL_NAME:
				fullName.setTBTextRight(bundle.getString("item_type_value"));
				break;
			case   RegisterDetailsActivity.IEMT_TYPE_CONTACT_NAME:
				contactName.setTBTextRight(bundle.getString("item_type_value"));
				
				break;
			case  RegisterDetailsActivity.IEMT_TYPE_LOCATION_DETAIL:
				locationDetail.setTBTextRight(bundle.getString("item_type_value"));
			break;
			case RegisterDetailsActivity.IEMT_TYPE_ENTERPRISE_INTRODUCTION:
				enterpriseIntroduction.setTBTextRight(bundle.getString("item_type_value"));
				break;
			default:
				break;
			}
		}
		else if(requestCode == REQUEST_CODE_AREA && resultCode == RESULT_OK)
		{
			Myself.Location=data.getStringExtra("myLocation");
			location.setTBTextRight(Myself.Location);
		}
		else if(requestCode == REQUEST_CODE_GOODS && resultCode == RESULT_OK)
		{
			Myself.Goods=data.getStringExtra("goods");
			transportGoods.setTBTextRight(Myself.Goods);
		}
		
		// else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
		// Uri uri = data.getData();
		// // to do find the path of pic
		// }
	}

	@Override
	protected void initView() {
		super.initView();
		bar_logo = (TextViewBarIcon) findViewById(R.id.register_details_logo);
		bar_logo.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(RegisterDetailsActivity.this,
						ImageGridPickerActivity.class);
				startActivityForResult(intent, REQUEST_CODE_IMAGE_SELECT);
			}
		});
		TextViewBarPure simpleName = (TextViewBarPure) findViewById(R.id.register_details_enterprise_simple_name);
		TextViewBarPure phoneNumber = (TextViewBarPure) findViewById(R.id.register_details_phone_number);
		
		simpleName.setTBTextRight(Myself.UserName);
		phoneNumber.setTBTextRight(Myself.PhoneNumber);
		
		
		fullName = (TextViewBarPure) findViewById(R.id.register_details_enterprise_full_name);
		fullName.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				putContent(getString(R.string.register_details_item_full_name_hint_top), 
						getString(R.string.register_details_item_full_name_hint_top),
						getString(R.string.register_details_item_full_name_hint_bottom), IEMT_TYPE_FULL_NAME);
			}
		});
		contactName = (TextViewBarPure) findViewById(R.id.register_details_contact_name);
		contactName.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				putContent(getString(R.string.register_details_item_contact_name_hint_top), 
						getString(R.string.register_details_item_contact_name_hint_top),
						getString(R.string.register_details_item_contact_name_hint_bottom), IEMT_TYPE_CONTACT_NAME);
			}
		});
		location = (TextViewBarPure) findViewById(R.id.register_details_location);
		location.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(RegisterDetailsActivity.this,
						LocationActivity.class);
				intent.putExtra("myLocation", Myself.Location);
				startActivityForResult(intent, REQUEST_CODE_AREA);
			}
		});
	
		locationDetail = (TextViewBarPure) findViewById(R.id.register_details_location_detail);
		locationDetail.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				putContent(getString(R.string.register_details_item_location_detail_hint_top), 
						getString(R.string.register_details_item_location_detail_hint_top),
						"", IEMT_TYPE_LOCATION_DETAIL);
			}
		});
		transportGoods=(TextViewBarPure) findViewById(R.id.register_details_transport_goods);
		transportGoods.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(RegisterDetailsActivity.this,
						GoodsActivity.class);
				startActivityForResult(intent, REQUEST_CODE_GOODS);
			}
		});
		enterpriseIntroduction=(TextViewBarPure) findViewById(R.id.register_details_enterprise_introduction);
		enterpriseIntroduction.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				putContent(getString(R.string.register_details_item_enterprise_introduction_hint_top), 
						getString(R.string.register_details_item_enterprise_introduction_hint_top),
						"", IEMT_TYPE_ENTERPRISE_INTRODUCTION);
			}
		});
		qrcode=(TextViewBarIcon) findViewById(R.id.register_details_qr_code);
		qrcode.setTBBarOnClickListener(new TBBarOnClickListener() {
			
			@Override
			public void onTBClick(View v) {
				startActivity(new Intent(RegisterDetailsActivity.this,QRCodeActivity.class));
			}
		});
		if (Myself.FullName!=null){
			fullName.setTBTextRight(Myself.FullName);
		}
		if(Myself.ContactName!=null)
		{
			contactName.setTBTextRight(Myself.ContactName);
		}
		if(Myself.Location!=null)
		{
			location.setTBTextRight(Myself.Location);
		}
		
	}
	private void putContent(String title,String hint_top,String hint_bottom,int item_type)
	{
		Intent intent = new Intent(RegisterDetailsActivity.this,
				RegisterDetailsItemActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("title", title);
		bundle.putString("hint_top", hint_top);
		bundle.putInt("item_type", item_type);
		bundle.putString("hint_bottom", hint_bottom);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQUEST_CODE_ITEM_RETURN);
	}

}

	