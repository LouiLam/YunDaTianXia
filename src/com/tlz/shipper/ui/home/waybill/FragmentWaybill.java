package com.tlz.shipper.ui.home.waybill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.net.AppConfig;
import com.net.MessageApi;
import com.net.NetAsyncFactory;
import com.net.NetAsyncFactory.ResultCodeSucListener;
import com.net.Urls;
import com.tlz.model.Myself;
import com.tlz.model.WaybillNews;
import com.tlz.model.WaybillNewsGroup;
import com.tlz.model.WaybillNewsWarp;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeFragment;
import com.tlz.shipper.ui.home.ActivityHome;
import com.tlz.shipper.ui.home.waybill.create.ActivityCreate;
import com.tlz.shipper.ui.home.waybill.mgr_ontheway.ActivityMgrOTW;
import com.tlz.shipper.ui.home.waybill.my_waybill.ActivityMyWaybill;
import com.tlz.shipper.ui.home.waybill.news.ActivityNewMsg;
import com.tlz.utils.ToastUtils;

public class FragmentWaybill extends ThemeFragment implements
		OnGroupCollapseListener, OnChildClickListener, OnGroupExpandListener,
		OnGroupClickListener {
	private ExpandableListView listView = null;
	private ExpandableListApapterWaybill adapter;
	private ActivityHome homeActivity;
	WaybillNewsWarp warp;
	public static FragmentWaybill newInstance() {
		return new FragmentWaybill();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		homeActivity = (ActivityHome) mActivity;
		if(!AppConfig.DEBUG)
		{
			
			NetAsyncFactory.createMsgTask(getActivity(), new ResultCodeSucListener<MessageApi>() {

			@Override
			public String handler(MessageApi api) {
				return api.getMyUnreadCountBySender(1, 10);
			}

			@Override
			public void suc(JSONObject obj) throws JSONException {
				JSONArray array=obj.getJSONArray("data");
				//最后一条记录是特殊的格式，来读取数量
				int totalCount=array.getJSONObject(array.length()-1).getInt("totalCount");
				warp=new WaybillNewsWarp();
				if(totalCount>0)
				{
					//最后一条记录不读，因为前面已读过了
					for (int i = 0; i < array.length()-1; i++) {
						JSONObject msg=array.getJSONObject(i);
						int messageCreatorId=msg.getInt("messageCreatorId");
						String description=msg.getString("description");
						String name=msg.getString("name");
						String head=msg.getString("head");
						int unreadCount=msg.getInt("unreadCount");
						warp.totalCount+=unreadCount;
						WaybillNewsGroup group=new WaybillNewsGroup();
						if(messageCreatorId==-1)
						{group.name=name;group.count=unreadCount;group.type=WaybillNews.TYPE_SYS;}
						else
						{group.name=name;group.head=head;group.description=description;group.count=unreadCount;}
						warp.list.add(group);
					}
					adapter.setWarp(warp);
					refreshList();
				}
				
				
			}
		}).execute(Urls.MSGAPI+";jsessionid="+Myself.Token+"?");}
		adapter = new ExpandableListApapterWaybill(homeActivity, new WaybillNewsWarp());
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
		if (adapter == null || warp == null)
			return;
		adapter.update(warp.list);
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
