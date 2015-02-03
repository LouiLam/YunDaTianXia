package com.tlz.shipper.ui.register_login;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.baidu.location.BDLocation;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.TextViewBarIcon;
import com.tlz.utils.DBHelper;
import com.tlz.utils.HandlerMsg;
import com.tlz.utils.ToastUtils;

public class LocationActivity extends ThemeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_location);
		mActionBar.setTitle(R.string.register_area_title);
		initView();
	}

	TextViewBarIcon iconTextBar;
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case HandlerMsg.LocationSuc:
			BDLocation location=(BDLocation) msg.obj;
			iconTextBar.setTBLeftText(String.format("%s %s %s", location.getProvince(),location.getCity(),location.getDistrict()));
			break;

		default:
			break;
		}
		return super.handleMessage(msg);
	}
	@Override
	protected void initView() {
		super.initView();
		Intent intent=getIntent();
		iconTextBar =(TextViewBarIcon) findViewById(R.id.cur_locationing);
		iconTextBar.setTBLeftText(intent.getStringExtra("myLocation"));
		iconTextBar.setTBIconLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iconTextBar.setTBLeftText(getString(R.string.locationing_error));
				LocationActivity.this.initLocation();
			}
		});
		final ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new SimpleAdapter(this, getAllProvincesData(), R.layout.list_item_location, new String[] { "content" }, new int[]{R.id.tb_left}));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				ToastUtils.show(LocationActivity.this, "onItemClick:"+curProvinces);
				if(curProvinces==null)
				{
					curProvinces=allProvinces[position];
					iconTextBar.setTBLeftText(curProvinces);
					listView.setAdapter(new SimpleAdapter(LocationActivity.this, getAllCitysData(position), R.layout.list_item_location, new String[] { "content" }, new int[]{R.id.tb_left}));
				}
				else
				{
					if(curCity==null)
					{
						curCity=allCitys[position];
						String temp=String.format("%s %s", curProvinces,curCity);
						iconTextBar.setTBLeftText(temp);
						Intent intent = new Intent();
						intent.putExtra("myLocation", temp);
						setResult(RESULT_OK, intent);
						finish();
					}
				}
				
			}
		});
	}
	
	String allProvinces[],allCitys[],curProvinces,curCity;
	private ArrayList<HashMap<String, Object>> getAllProvincesData() {
		DBHelper dbHelper = new DBHelper(getApplicationContext(),
				"db_weather.db");
		allProvinces = dbHelper.getAllProvinces();
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		/* 为动态数组添加数据 */
		for (int i = 0; i < allProvinces.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("content", allProvinces[i]);
			listItem.add(map);
		}
		return listItem;
	}
	private ArrayList<HashMap<String, Object>> getAllCitysData(int position) {
		DBHelper dbHelper = new DBHelper(getApplicationContext(),
				"db_weather.db");
		 allCitys= dbHelper.getAllCityByProvinceID(position);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		/* 为动态数组添加数据 */
		for (int i = 0; i < allCitys.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("content", allCitys[i]);
			listItem.add(map);
		}
		return listItem;

	}

}
