package com.tlz.shipper.ui.home.waybill;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.utils.DBHelper;
import com.tlz.utils.ToastUtils;

public class ActivityLoadingGoods extends ThemeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waybill_loadling);
		mActionBar.setTitle(R.string.waybill_loading_title);
		initView();
	}

	EditText loading,unloading;
	@Override
	protected void initView() {
		super.initView();
		Intent intent=getIntent();
		
		final boolean isLoading=intent.getBooleanExtra("isLoading", false);
		loading =(EditText) findViewById(R.id.loading);
		unloading =(EditText) findViewById(R.id.unloading);
		if(Myself.Loading!=null)
		{
			loading.setText(Myself.Loading);
		}
		if(Myself.Unloading!=null)
		{
			unloading.setText(Myself.Unloading);
		}
		if(isLoading)
		{
			unloading.setEnabled(false);
		}
		else
		{
			unloading.requestFocus();
			loading.setEnabled(false);
		}
		final ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new SimpleAdapter(this, getAllProvincesData(), R.layout.list_item_location, new String[] { "content" }, new int[]{R.id.tb_left}));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				ToastUtils.show(ActivityLoadingGoods.this, "onItemClick:"+curProvinces);
				if(curProvinces==null)
				{
					curProvinces=allProvinces[position];
					if(isLoading)
					{
						loading.setText(curProvinces);
					}
					else
					{
						unloading.setText(curProvinces);
					}
					listView.setAdapter(new SimpleAdapter(ActivityLoadingGoods.this, getAllCitysData(position), R.layout.list_item_location, new String[] { "content" }, new int[]{R.id.tb_left}));
				}
				else
				{
					if(curCity==null)
					{
						curCity=allCitys[position];
						String temp=String.format("%s %s", curProvinces,curCity);
						if(isLoading)
						{
							Myself.Loading=temp;
						}
						else
						{
							Myself.Unloading=temp;
						}
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
