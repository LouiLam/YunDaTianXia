package com.tlz.shipper.ui.home.waybill;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.tlz.model.WaybillNews;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeFragment;
import com.tlz.shipper.ui.home.ActivityHome;
import com.tlz.shipper.ui.home.waybill.create.ActivityCreate;
import com.tlz.shipper.ui.home.waybill.mgr_ontheway.ActivityMgrOTW;
import com.tlz.shipper.ui.home.waybill.my_waybill.ActivityMyWaybill;
import com.tlz.shipper.ui.home.waybill.news.ActivityNewMsg;
import com.tlz.utils.CollectionUtils;
import com.tlz.utils.ToastUtils;

public class FragmentWaybill extends ThemeFragment implements
		OnGroupCollapseListener, OnChildClickListener, OnGroupExpandListener,
		OnGroupClickListener {
	private ExpandableListView listView = null;
	private ExpandableListApapterWaybill adapter;
	private ActivityHome homeActivity;
	private ArrayList<WaybillNews> list;

	public static FragmentWaybill newInstance() {
		return new FragmentWaybill();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		homeActivity = (ActivityHome) mActivity;
		list = new ArrayList<WaybillNews>();
		list.add(new WaybillNews());
		list.add(new WaybillNews());
		adapter = new ExpandableListApapterWaybill(homeActivity, list);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_waybill, container, false);
		initView(v);
		return v;
	}

	public void refreshList() {
		if (adapter == null || list == null)
			return;
		adapter.update(list);
		if (CollectionUtils.isEmpty(list)) {

		} else {

		}

	}

	private void initView(View v) {
		listView = (ExpandableListView) v.findViewById(R.id.listView);
		listView.setAdapter(adapter);
		listView.setOnGroupCollapseListener(this);
		listView.setOnGroupExpandListener(this);
		listView.setOnGroupClickListener(this);
		listView.setOnChildClickListener(this);
		listView.expandGroup(0);
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

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		ToastUtils.show(getActivity(), groupPosition + "," + childPosition);
		// connectToWifi(adapter.getChild(groupPosition, childPosition));
		return false;
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		// listView.collapseGroup(groupPosition);
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		// listView.expandGroup(groupPosition);
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {

		skipUI(groupPosition);

		// if (listView.isGroupExpanded(groupPosition)) {
		// listView.collapseGroup(groupPosition);
		// } else {
		// listView.expandGroup(groupPosition);
		// }
		return true;
	}

	private void skipUI(int position) {
		if (position == 0)
			startActivity(new Intent(getActivity(), ActivityNewMsg.class));
		if (position == 1)
			startActivity(new Intent(getActivity(), ActivityCreate.class));
		if (position == 2)
			startActivity(new Intent(getActivity(), ActivityMgrOTW.class));
		if (position == 3)
			startActivity(new Intent(getActivity(), ActivityMyWaybill.class));
	}
}
