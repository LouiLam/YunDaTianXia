package com.tlz.shipper.ui.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tlz.admin.ImageLoaderAdmin;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.TextViewBarIcon;
import com.tlz.shipper.ui.widget.TextViewBarPure;
import com.tlz.shipper.ui.widget.ViewBar.TBBarOnClickListener;
import com.tlz.utils.AndroidTextUtils;
import com.tlz.utils.QRCodeUtils;

public class ActivityCompleteEnterpriseInfo extends ThemeActivity  {
	TextViewBarIcon bar_logo,qrcode;
	TextViewBarPure fullName, contactName ,location,detailAddress,cargoType,enterpriseIntroduction,enterpriseQualifications;
	public static final int REQUEST_CODE_IMAGE_SELECT=0;
	public static final int REQUEST_CODE_ITEM_RETURN=1;
	public static final int REQUEST_CODE_AREA=2;
	public static final int REQUEST_CODE_GOODS=3;
	
	
	public static final int IEMT_TYPE_FULL_NAME=0;
	public static final int IEMT_TYPE_CONTACT_NAME=1;
	public static final int IEMT_TYPE_DETAIL_ADDRESS=2;
	public static final int IEMT_TYPE_ENTERPRISE_INTRODUCTION=3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar.setTitle(R.string.complete_enterprise_info_title);
		setContentView(R.layout.activity_common_complete_enterprise_info);
		initView();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_GOODS && resultCode == RESULT_OK)
		{
			cargoType.setTBTextRight(getCurCargoTypeString());
		}
		else if (requestCode == REQUEST_CODE_IMAGE_SELECT && resultCode == RESULT_OK) {
//			Bitmap bm = data.getParcelableExtra("bitmap");
			ImageLoaderAdmin.getInstance().displayImage(Myself.HeadIconUrl, (ImageView)bar_logo.findViewById(R.id.tb_icon_right));
//			bar_logo.setIconRightBack(bm);

		}
		if (data == null)
			return;
	
		else if(requestCode == REQUEST_CODE_ITEM_RETURN && resultCode == RESULT_OK)
		{
			Bundle bundle=data.getExtras();
			int curType=bundle.getInt("item_type");
			switch (curType) {
			case  ActivityCompleteEnterpriseInfo.IEMT_TYPE_FULL_NAME:
				fullName.setTBTextRight(bundle.getString("item_type_value"));
				break;
			case   ActivityCompleteEnterpriseInfo.IEMT_TYPE_CONTACT_NAME:
				contactName.setTBTextRight(bundle.getString("item_type_value"));
				
				break;
			case  ActivityCompleteEnterpriseInfo.IEMT_TYPE_DETAIL_ADDRESS:
				detailAddress.setTBTextRight(bundle.getString("item_type_value"));
			break;
			case ActivityCompleteEnterpriseInfo.IEMT_TYPE_ENTERPRISE_INTRODUCTION:
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
	
		
		// else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
		// Uri uri = data.getData();
		// // to do find the path of pic
		// }
	}

	@Override
	protected void initView() {
		super.initView();
		bar_logo = (TextViewBarIcon) findViewById(R.id.complete_enterprise_info_logo);
		bar_logo.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCompleteEnterpriseInfo.this,
						ActivityImageGridPicker.class);
				intent.putExtra("category", ActivityImageGridPicker.UploadHead);
				startActivityForResult(intent, REQUEST_CODE_IMAGE_SELECT);
			}
		});
		
		TextViewBarPure simpleName = (TextViewBarPure) findViewById(R.id.complete_enterprise_info_simple_name);
		TextViewBarPure phoneNumber = (TextViewBarPure) findViewById(R.id.complete_enterprise_info_phone_number);
		
		simpleName.setTBTextRight(Myself.UserName);
		phoneNumber.setTBTextRight(Myself.PhoneNumber);
		
		
		fullName = (TextViewBarPure) findViewById(R.id.complete_enterprise_info_full_name);
		fullName.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				putContent(getString(R.string.register_details_item_full_name_hint_top), 
						getString(R.string.register_details_item_full_name_hint_top),
						getString(R.string.register_details_item_full_name_hint_bottom), IEMT_TYPE_FULL_NAME);
			}
		});
		contactName = (TextViewBarPure) findViewById(R.id.complete_enterprise_info_contact);
		contactName.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				putContent(getString(R.string.register_details_item_contact_name_hint_top), 
						getString(R.string.register_details_item_contact_name_hint_top),
						getString(R.string.register_details_item_contact_name_hint_bottom), IEMT_TYPE_CONTACT_NAME);
			}
		});
		location = (TextViewBarPure) findViewById(R.id.complete_enterprise_info_location);
		location.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCompleteEnterpriseInfo.this,
						ActivityLocation.class);
				intent.putExtra("myLocation", Myself.Location);
				startActivityForResult(intent, REQUEST_CODE_AREA);
			}
		});
	
		detailAddress = (TextViewBarPure) findViewById(R.id.complete_enterprise_info_detail_address);
		detailAddress.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				putContent(getString(R.string.register_details_item_location_detail_hint_top), 
						getString(R.string.register_details_item_location_detail_hint_top),
						"", IEMT_TYPE_DETAIL_ADDRESS);
			}
		});
		cargoType=(TextViewBarPure) findViewById(R.id.complete_enterprise_info_cargo_type);
		cargoType.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCompleteEnterpriseInfo.this,
						ActivityCargoType.class);
				startActivityForResult(intent, REQUEST_CODE_GOODS);
			}
		});
		enterpriseIntroduction=(TextViewBarPure) findViewById(R.id.complete_enterprise_info_introduction);
		enterpriseIntroduction.setTBBarOnClickListener(new TBBarOnClickListener() {
			@Override
			public void onTBClick(View v) {
				putContent(getString(R.string.register_details_item_enterprise_introduction_hint_top), 
						getString(R.string.register_details_item_enterprise_introduction_hint_top),
						"", IEMT_TYPE_ENTERPRISE_INTRODUCTION);
			}
		});
		qrcode=(TextViewBarIcon) findViewById(R.id.complete_enterprise_info_qr_code);
		qrcode.setTBBarOnClickListener(new TBBarOnClickListener() {
			
			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCompleteEnterpriseInfo.this,
						ActivityBigPicture.class);
				intent.putExtra("isDelete", false);
				intent.putExtra("bitmap", QRCodeUtils.generateQRCode(Myself.UserName));
				intent.putExtra("count", (Integer)v.getTag());
				startActivity(intent);
			}
		});
		enterpriseQualifications=(TextViewBarPure) findViewById(R.id.complete_enterprise_info_qualifications);
		enterpriseQualifications.setTBBarOnClickListener(new TBBarOnClickListener() {
			
			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCompleteEnterpriseInfo.this,
						ActivityEnterpriseQualifications.class);
				startActivity(intent);
			}
		});
		setDefault(Myself.FullName, fullName);
		setDefault(Myself.ContactName, contactName);
		setDefault(Myself.Location, location);
		setDefault(Myself.DetailAddress, detailAddress);
		setDefault(Myself.Introduction, enterpriseIntroduction);
		setDefault(Myself.Businesslicence, enterpriseQualifications);
		cargoType.setTBTextRight(getCurCargoTypeString()==null?getString(R.string.complete_enterprise_info_default):getCurCargoTypeString());
		ImageLoaderAdmin.getInstance().displayImage(Myself.HeadIconUrl,
				(ImageView)bar_logo.findViewById(R.id.tb_icon_right));
	}
	private void setDefault(String data,TextViewBarPure view){
		if(!AndroidTextUtils.isEmpty(data))
		{
			view.setTBTextRight(data);
		}
		else
		{
			view.setTBTextRight(getString(R.string.complete_enterprise_info_default));
		}
	}
	private void putContent(String title,String hint_top,String hint_bottom,int item_type)
	{
		Intent intent = new Intent(ActivityCompleteEnterpriseInfo.this,
				ActivityCompleteEnterpriseInfoItem.class);
		Bundle bundle=new Bundle();
		bundle.putString("title", title);
		bundle.putString("hint_top", hint_top);
		bundle.putInt("item_type", item_type);
		bundle.putString("hint_bottom", hint_bottom);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQUEST_CODE_ITEM_RETURN);
	}

}

	