package com.tlz.shipper.ui.common;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.net.NetAsyncFactory;
import com.net.NetAsyncFactory.ResultCodeSucListener;
import com.net.ShipperAccountApi;
import com.net.Urls;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.TextViewBarIcon;

public class ActivityCargoType extends ThemeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_cargo_type);
		mActionBar.setTitle(R.string.cargo_type_title);
		initView();
		
	}

	TextViewBarIcon iconTextBar;
	@Override
	protected void initView() {
		super.initView();
		iconTextBar =(TextViewBarIcon) findViewById(R.id.cur_locationing);
		{iconTextBar.setTBLeftText(getCurCargoTypeString());}
		final ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new SimpleAdapter(this, getAllGoods(), R.layout.list_item_location, new String[] { "content" }, new int[]{R.id.tb_left}));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Myself.CargoType=(byte) (position+1);
				iconTextBar.setTBLeftText(getCurCargoTypeString());
				NetAsyncFactory.createShipperTask(ActivityCargoType.this,
						new ResultCodeSucListener<ShipperAccountApi>() {

							@Override
							public void suc(JSONObject obj) throws JSONException {
								
								setResult(RESULT_OK);
								finish();
								// {
								// resultCode:1 :正确|-1:操作失败,0:用户名或密码有误
								// data:
								// {
								// token:令牌
								// memeber:
								// { memberId:12, phone:"会员手机号" loginName:"登陆名"
								// creditGrad:(int)信誉等级 balance:(float)帐户余额 },
								// shipper:
								// { shipperId:(int)主键 head:"头像url"
								// auditStatus:(int)认证状态
								// locationCode :"所在地CODE" simpleName:"企业简称"
								// fullName:"企业全称"
								// detailAddress:"详细地址" contact:"联系人" phone:"联系电话"
								// introduce:"企业简介" cargoType:"主要运送货品"
								// qrCode:"二维码明片url" }
								// }
								// }

							}

							@Override
							public String handler(ShipperAccountApi api) {
								return api.completeData(Myself.ShipperId,
										Myself.FullName, Myself.DetailAddress,
										Myself.ContactName, Myself.Introduction,
										Myself.CargoType);
							}
						}).execute(Urls.REGEDIT);
				
		
				
			}
		});
	}
	 
	private ArrayList<HashMap<String, Object>> getAllGoods() {
		
		String[] goods=getCargoTypeStringArray();
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		/* 为动态数组添加数据 */
		for (int i = 0; i < goods.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("content", goods[i]);
			listItem.add(map);
		}
		return listItem;

	}

}
