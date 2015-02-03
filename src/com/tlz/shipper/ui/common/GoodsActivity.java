package com.tlz.shipper.ui.common;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.TextViewBarIcon;

public class GoodsActivity extends ThemeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_goods);
		mActionBar.setTitle(R.string.register_goods_title);
		initView();
	}

	TextViewBarIcon iconTextBar;
	@Override
	protected void initView() {
		super.initView();
		iconTextBar =(TextViewBarIcon) findViewById(R.id.cur_locationing);
		if(Myself.Goods!=null)
		{iconTextBar.setTBLeftText(Myself.Goods);}
		final ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new SimpleAdapter(this, getAllGoods(), R.layout.list_item_location, new String[] { "content" }, new int[]{R.id.tb_left}));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
					Myself.Goods=goods[position];
					iconTextBar.setTBLeftText(Myself.Goods);
					Intent intent = new Intent();
					intent.putExtra("goods", Myself.Goods);
					setResult(RESULT_OK, intent);
					finish();
		
				
			}
		});
	}
	 String[] goods;
	private ArrayList<HashMap<String, Object>> getAllGoods() {
		
		 Resources res =getResources();
		  goods=res.getStringArray(R.array.goods);
		 
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
