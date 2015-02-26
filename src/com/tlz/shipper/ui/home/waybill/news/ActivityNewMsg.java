package com.tlz.shipper.ui.home.waybill.news;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tlz.model.WaybillNews;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.SwipeListView;
import com.tlz.shipper.ui.widget.TextViewBarIcon;

public class ActivityNewMsg extends ThemeActivity {
	SwipeListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waybill_news);
		mActionBar.setTitle(R.string.waybill_news_title);
		initView();
	}

	TextViewBarIcon iconTextBar;
	@Override
	protected void initView() {
		super.initView();
		ArrayList<WaybillNews> list = new ArrayList<WaybillNews>();
		list.add(new WaybillNews());
		list.add(new WaybillNews());
        mListView = (SwipeListView)findViewById(R.id.listView);
        AdapterSwipe adapter = new AdapterSwipe(this, mListView.getRightViewWidth(),
                new AdapterSwipe.IOnItemRightClickListener() {
                    @Override
                    public void onRightClick(View v, int position) {
                        Toast.makeText(ActivityNewMsg.this, "right onclick " + position,
                                Toast.LENGTH_SHORT).show();
                    }
                },list);
        mListView.setAdapter(adapter);
        mListView.setRightViewWidth((int)getResources().getDimension(R.dimen.custom_actionbar_icon_height));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActivityNewMsg.this, "item onclick " + position, Toast.LENGTH_SHORT)
                        .show();
//                Myself.Goods=goods[position];
//				iconTextBar.setText(Myself.Goods);
//				Intent intent = new Intent();
//				intent.putExtra("goods", Myself.Goods);
//				setResult(RESULT_OK, intent);
//				finish();
            }
        });
		
		
	
	}


}
