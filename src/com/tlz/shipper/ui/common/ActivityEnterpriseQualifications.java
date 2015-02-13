package com.tlz.shipper.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tlz.admin.ImageLoaderAdmin;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.TextViewBarIcon;
import com.tlz.shipper.ui.widget.ViewBar.TBBarOnClickListener;
import com.tlz.utils.AndroidTextUtils;
/**
 * 企业资质
 *
 */
public class ActivityEnterpriseQualifications extends ThemeActivity {
	private TextViewBarIcon businesslicence,organization,taxregist;
	public static final int REQUEST_CODE_BUSINESS_LICENCE=0;
	public static final int REQUEST_CODE_ORGANIZATION=1;
	public static final int REQUEST_CODE_TAX_REG_NUM=2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_enterprise_qualifications);
		initView();
	}


	@Override
	protected void initView() {
		super.initView();
		businesslicence=(TextViewBarIcon) findViewById(R.id.eq_businesslicence);
		organization=(TextViewBarIcon) findViewById(R.id.eq_organization);
		taxregist=(TextViewBarIcon) findViewById(R.id.eq_taxregist);
		businesslicence.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				if(AndroidTextUtils.isEmpty(Myself.Businesslicence))
				{Intent intent = new Intent(ActivityEnterpriseQualifications.this,
						ActivityImageGridPicker.class);
				intent.putExtra("category", ActivityImageGridPicker.UploadBusinessLicence);
				startActivityForResult(intent, REQUEST_CODE_BUSINESS_LICENCE);
				}
			}
		});
		organization.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				if(AndroidTextUtils.isEmpty(Myself.Organization))
				{Intent intent = new Intent(ActivityEnterpriseQualifications.this,
						ActivityImageGridPicker.class);
				intent.putExtra("category", ActivityImageGridPicker.UploadOrganizationNo);
				startActivityForResult(intent, REQUEST_CODE_ORGANIZATION);
				}
			}
		});
		taxregist.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				if(AndroidTextUtils.isEmpty(Myself.Taxregist))
				{Intent intent = new Intent(ActivityEnterpriseQualifications.this,
						ActivityImageGridPicker.class);
				intent.putExtra("category", ActivityImageGridPicker.UploadTaxRegistNo);
				startActivityForResult(intent, REQUEST_CODE_TAX_REG_NUM);
				
				}
			}
		});
		ImageLoaderAdmin.getInstance().displayImage(Myself.Businesslicence, (ImageView)businesslicence.findViewById(R.id.tb_icon_right));
		ImageLoaderAdmin.getInstance().displayImage(Myself.Organization, (ImageView)organization.findViewById(R.id.tb_icon_right));
		ImageLoaderAdmin.getInstance().displayImage(Myself.Taxregist, (ImageView)taxregist.findViewById(R.id.tb_icon_right));
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_BUSINESS_LICENCE && resultCode == RESULT_OK)
		{
			if(!AndroidTextUtils.isEmpty(Myself.Businesslicence))
			{
				ImageLoaderAdmin.getInstance().displayImage(Myself.Businesslicence, (ImageView)businesslicence.findViewById(R.id.tb_icon_right));
			}
		}
		if(requestCode == REQUEST_CODE_ORGANIZATION && resultCode == RESULT_OK)
		{
			if(!AndroidTextUtils.isEmpty(Myself.Organization))
			{
				ImageLoaderAdmin.getInstance().displayImage(Myself.Organization, (ImageView)organization.findViewById(R.id.tb_icon_right));
			}
		}
		if(requestCode == REQUEST_CODE_TAX_REG_NUM && resultCode == RESULT_OK)
		{
			if(!AndroidTextUtils.isEmpty(Myself.Taxregist))
			{
				ImageLoaderAdmin.getInstance().displayImage(Myself.Taxregist, (ImageView)taxregist.findViewById(R.id.tb_icon_right));
			}
		}
	
	}
}
