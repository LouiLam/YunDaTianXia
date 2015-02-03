package com.tlz.shipper.ui.home.cargo;

import java.util.ArrayList;

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
import com.tlz.shipper.ui.home.apply.ExpandableListApapterApply;
import com.tlz.utils.CollectionUtils;
import com.tlz.utils.ToastUtils;

public class CargoFragment extends ThemeFragment implements
		OnGroupCollapseListener, OnChildClickListener, OnGroupExpandListener,
		OnGroupClickListener {
	private ExpandableListView listView = null;
	private ExpandableListApapterApply adapter;
	private ActivityHome homeActivity;
	private ArrayList<WaybillNews> list;

	public static CargoFragment newInstance() {
		return new CargoFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		homeActivity = (ActivityHome) mActivity;
		list = new ArrayList<WaybillNews>();
		list.add(new WaybillNews());
		list.add(new WaybillNews());
		adapter = new ExpandableListApapterApply(homeActivity, list);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_cargo, container, false);
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
//		if(groupPosition==0)
//		{skipUI();}
//		if (listView.isGroupExpanded(groupPosition)) {
//			listView.collapseGroup(groupPosition);
//		} else {
//			listView.expandGroup(groupPosition);
//		}
		return true;
	}
	private void skipUI()
	{
//		startActivity(new Intent(getActivity(),NewsActivity.class));
		
	}
}
