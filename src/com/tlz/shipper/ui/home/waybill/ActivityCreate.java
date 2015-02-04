package com.tlz.shipper.ui.home.waybill;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.common.GoodsActivity;
import com.tlz.shipper.ui.common.ImageGridPickerActivity;
import com.tlz.shipper.ui.common.QRCodeScanningActivity;
import com.tlz.shipper.ui.home.waybill.my_waybill.ActivityMyWalbill;
import com.tlz.shipper.ui.widget.EditTextBarIconTitleBtn;
import com.tlz.shipper.ui.widget.EditTextBarIconTitleClearText;
import com.tlz.shipper.ui.widget.EditTextBarIconTitleClearTextAndRemark;
import com.tlz.shipper.ui.widget.EditTextBarIconTitleClearTextAndRemark.TBOnPhotographClickListener;
import com.tlz.shipper.ui.widget.ViewBar.TBBarOnClickListener;
import com.tlz.utils.ResIdentifier;
import com.tlz.utils.ToastUtils;

public class ActivityCreate extends ThemeActivity {
	public static final int REQUEST_CODE_LOADING = 0;
	public static final int REQUEST_CODE_UNLOADING = 1;
	public static final int REQUEST_CODE_WEIGHT = 2;
	public static final int REQUEST_CODE_DATE_SEND = 3;
	public static final int REQUEST_CODE_DATE_ARRIVE = 4;
	public static final int REQUEST_CODE_GOODS = 5;
	public static final int REQUEST_CODE_IMAGE_SELECT_TOP = 6;
	public static final int REQUEST_CODE_IMAGE_SELECT_BOTTOM = 7;
	public static final int REQUEST_CODE_QR_CODE = 8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waybill_create);
		mActionBar.setTitle(R.string.waybill_create_title);
		initView();
	}
	EditTextBarIconTitleBtn loading, unloading, weight, date_send, date_arrive,
			goods_type;
	EditTextBarIconTitleClearTextAndRemark remarks, remarks1;
	EditTextBarIconTitleClearText goods_insurance, goods_receive;
	LinearLayout waybill_create_image_bar_top, waybill_create_image_bar_bottom;
	ImageView imgsTop[] = new ImageView[5];
	ImageView imgsBottom[] = new ImageView[5];
	private int countTop = 0, countBottom = 0;

	@Override
	protected void initView() {
		super.initView();
		waybill_create_image_bar_top = (LinearLayout) findViewById(R.id.waybill_create_image_bar_top);
		waybill_create_image_bar_top.setVisibility(View.GONE);
		waybill_create_image_bar_bottom = (LinearLayout) findViewById(R.id.waybill_create_image_bar_bottom);
		waybill_create_image_bar_bottom.setVisibility(View.GONE);
		for (int i = 0; i < 5; i++) {
			imgsTop[i] = (ImageView) waybill_create_image_bar_top
					.findViewById(ResIdentifier.getIDByName(this, "image" + i));
			imgsBottom[i] = (ImageView) waybill_create_image_bar_bottom
					.findViewById(ResIdentifier.getIDByName(this, "image" + i));
		}
		loading = (EditTextBarIconTitleBtn) findViewById(R.id.waybill_create_loading);
		loading.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCreate.this,
						ActivityLoadingGoods.class);
				intent.putExtra("isLoading", true);
				startActivityForResult(intent, REQUEST_CODE_LOADING);

			}
		});
		unloading = (EditTextBarIconTitleBtn) findViewById(R.id.waybill_create_unloading);
		unloading.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCreate.this,
						ActivityLoadingGoods.class);
				intent.putExtra("isLoading", false);
				startActivityForResult(intent, REQUEST_CODE_UNLOADING);

			}
		});
		weight = (EditTextBarIconTitleBtn) findViewById(R.id.waybill_create_weight);
		weight.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCreate.this,
						ActivityWeight.class);
				startActivityForResult(intent, REQUEST_CODE_WEIGHT);

			}
		});
		date_send = (EditTextBarIconTitleBtn) findViewById(R.id.waybill_create_date_send);
		date_send.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCreate.this,
						ActivityCalendar.class);
				intent.putExtra("isSend", true);
				startActivityForResult(intent, REQUEST_CODE_DATE_SEND);
			}
		});
		date_arrive = (EditTextBarIconTitleBtn) findViewById(R.id.waybill_create_date_arrive);
		date_arrive.setTBBarOnClickListener(new TBBarOnClickListener() {

			@Override
			public void onTBClick(View v) {
				Intent intent = new Intent(ActivityCreate.this,
						ActivityCalendar.class);
				intent.putExtra("isSend", false);
				startActivityForResult(intent, REQUEST_CODE_DATE_ARRIVE);
			}
		});
		goods_type = (EditTextBarIconTitleBtn) findViewById(R.id.waybill_create_more_goods_type);
		goods_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityCreate.this,
						GoodsActivity.class);
				startActivityForResult(intent, REQUEST_CODE_GOODS);
			}
		});
		remarks = (EditTextBarIconTitleClearTextAndRemark) findViewById(R.id.waybill_create_remarks);
		remarks1 = (EditTextBarIconTitleClearTextAndRemark) findViewById(R.id.waybill_create_remarks1);
		remarks1.setTBOnPhotographClickListener(new TBOnPhotographClickListener() {

			@Override
			public void onClick() {
				if (countBottom > 4)
					return;
				Intent intent = new Intent(ActivityCreate.this,
						ImageGridPickerActivity.class);
				startActivityForResult(intent, REQUEST_CODE_IMAGE_SELECT_BOTTOM);

			}
		});
		remarks.setTBOnPhotographClickListener(new TBOnPhotographClickListener() {

			@Override
			public void onClick() {
				if (countTop > 4)
					return;
				Intent intent = new Intent(ActivityCreate.this,
						ImageGridPickerActivity.class);
				startActivityForResult(intent, REQUEST_CODE_IMAGE_SELECT_TOP);

			}
		});
		goods_insurance = (EditTextBarIconTitleClearText) findViewById(R.id.waybill_create_more_goods_insurance);
		goods_receive = (EditTextBarIconTitleClearText) findViewById(R.id.waybill_create_more_goods_receive);
		moreGoodsInfo(View.GONE);
		findViewById(R.id.waybill_btn_save).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(ActivityCreate.this,
								ActivityMyWalbill.class));
					}
				});
		findViewById(R.id.waybill_create_more).setOnClickListener(this);
		findViewById(R.id.tb_qr).setOnClickListener(this);
		notNullValue();
	}

	@Override
	public void onClick(int viewId) {
		switch (viewId) {
		case R.id.tb_qr:
			Intent intent = new Intent();
			intent.setClass(ActivityCreate.this, QRCodeScanningActivity.class);
			startActivityForResult(intent, REQUEST_CODE_QR_CODE);
			break;
		case R.id.waybill_create_more:
			moreGoodsInfo(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	private void moreGoodsInfo(int visibility) {
		goods_type.setVisibility(visibility);
		goods_insurance.setVisibility(visibility);
		goods_receive.setVisibility(visibility);
	}

	private void notNullValue() {
		if (Myself.Loading != null) {
			loading.setTBRightText(Myself.Loading);
		}
		if (Myself.Unloading != null) {
			unloading.setTBRightText(Myself.Unloading);
		}
		if (Myself.WeightString != null) {
			weight.setTBRightText(Myself.WeightString);
		}
		if (Myself.SendString != null) {
			date_send.setTBRightText(Myself.SendString);
		}
		if (Myself.ArriveString != null) {
			date_arrive.setTBRightText(Myself.ArriveString);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;
		if (requestCode == REQUEST_CODE_LOADING && resultCode == RESULT_OK) {
			loading.setTBRightText(data.getStringExtra("myLocation"));
		} else if (requestCode == REQUEST_CODE_UNLOADING
				&& resultCode == RESULT_OK) {
			unloading.setTBRightText(data.getStringExtra("myLocation"));
		} else if (requestCode == REQUEST_CODE_WEIGHT
				&& resultCode == RESULT_OK) {
			weight.setTBRightText(data.getStringExtra("content"));
		} else if (requestCode == REQUEST_CODE_DATE_SEND
				&& resultCode == RESULT_OK) {
			date_send.setTBRightText(data.getStringExtra("content"));
		} else if (requestCode == REQUEST_CODE_DATE_ARRIVE
				&& resultCode == RESULT_OK) {
			date_arrive.setTBRightText(data.getStringExtra("content"));
		} else if (requestCode == REQUEST_CODE_GOODS && resultCode == RESULT_OK) {
			Myself.Goods = data.getStringExtra("goods");
			goods_type.setTBRightText(Myself.Goods);
		} else if (requestCode == REQUEST_CODE_IMAGE_SELECT_TOP
				&& resultCode == RESULT_OK) {
			waybill_create_image_bar_top.setVisibility(View.VISIBLE);
			Bitmap bm = data.getParcelableExtra("bitmap");
			imgsTop[countTop].setImageBitmap(bm);
			countTop++;
		} else if (requestCode == REQUEST_CODE_QR_CODE
				&& resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			ToastUtils.show(this, "二维码内容为:" + bundle.getString("result"));
			// mTextView.setText(bundle.getString("result"));
			// mImageView.setImageBitmap((Bitmap)
			// data.getParcelableExtra("bitmap"));
		} else if (requestCode == REQUEST_CODE_IMAGE_SELECT_BOTTOM
				&& resultCode == RESULT_OK) {
			waybill_create_image_bar_bottom.setVisibility(View.VISIBLE);
			Bitmap bm = data.getParcelableExtra("bitmap");
			imgsBottom[countBottom].setImageBitmap(bm);
			countBottom++;
		}
	}

}
