package com.tlz.shipper.ui.home.waybill.my_waybill;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tlz.model.MyWaybill;
import com.tlz.model.WaybillNews;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeFragment;
/**
 * 待发车
 *
 */
public class FragmentAlready extends ThemeFragment  {
	private ListView listView = null;
	private ActivityMyWalbill activity;
	private ArrayList<MyWaybill> list;
	AdapterWaybillList  adapter;
	public static FragmentAlready newInstance() {
		return new FragmentAlready();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = (ActivityMyWalbill) mActivity;
		list = new ArrayList<MyWaybill>();
		list.add(new MyWaybill(0,MyWaybill.TYPE_CONTENT));
		list.add(new MyWaybill(0,MyWaybill.TYPE_CONTENT));
		adapter=new AdapterWaybillList(activity,  list);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_waybill_mywaybill_list, container, false);
		initView(v);
		return v;
	}


	private void initView(View v) {
		listView = (ListView) v.findViewById(R.id.listView);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//
		// case R.id.tv_find_groupbuy:
		// startWebActivity("团购",
		// "http://ai.m.taobao.com/bu.html?id=2&pid=mm_98998373_7704481_27676357");
		// break;
		// case R.id.tv_find_tour:
		// startWebActivity("旅游",
		// "http://ai.m.taobao.com/bu.html?id=3&pid=mm_98998373_7704481_27678414");
		// break;
		// case R.id.tv_find_souyou:
		// startActivity(new Intent(mActivity, SouYouActivity.class));// 搜游
		// break;
		// case R.id.tv_find_jobwanted:
		// startWebActivity("求职",
		// "http://jump.luna.58.com/s?spm=s-26247596029447-ms-f-kuaishang&ch=zhaopin");
		// break;
		//
		// case R.id.tv_find_housing:
		// startWebActivity("租房",
		// "http://jump.luna.58.com/s?spm=s-26247596029447-ms-f-kuaishang&ch=fangchan");
		// break;
		// case R.id.tv_find_housekeeping:
		// startWebActivity("家政",
		// "http://jump.luna.58.com/s?spm=s-26247596029447-ms-f-kuaishang&ch=huangye");
		// break;
		default:
			break;
		}

		super.onClick(v);
	}


	private void skipUI(int position) {
//		if (position == 0)
//			startActivity(new Intent(getActivity(), NewsActivity.class));
//		if (position == 1)
//			startActivity(new Intent(getActivity(), CreateActivity.class));
	}
}
