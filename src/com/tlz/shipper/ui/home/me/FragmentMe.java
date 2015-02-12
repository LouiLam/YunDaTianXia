package com.tlz.shipper.ui.home.me;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;

import com.net.AppConfig;
import com.net.NetShipperMsgAsyncTask;
import com.net.ShipperAccountApi;
import com.net.Urls;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tlz.admin.ImageLoaderAdmin;
import com.tlz.model.Myself;
import com.tlz.model.WaybillNews;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.ThemeFragment;
import com.tlz.shipper.ui.common.CompleteEnterpriseInfoActivity;
import com.tlz.shipper.ui.home.ActivityHome;
import com.tlz.shipper.ui.widget.TextViewBarIcon;
import com.tlz.shipper.ui.widget.ViewBar.TBBarOnClickListener;
import com.tlz.utils.CollectionUtils;
import com.tlz.utils.Flog;
import com.tlz.utils.ToastUtils;

public class FragmentMe extends ThemeFragment implements
		OnGroupCollapseListener, OnChildClickListener, OnGroupExpandListener,
		OnGroupClickListener {
	private ExpandableListView listView = null;
	private ExpandableListApapterMe adapter;
	private ActivityHome homeActivity;
	private ArrayList<WaybillNews> list;
	
	public static FragmentMe newInstance() {
		return new FragmentMe();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		homeActivity = (ActivityHome) mActivity;
		list = new ArrayList<WaybillNews>();
		list.add(new WaybillNews());
		list.add(new WaybillNews());
		adapter = new ExpandableListApapterMe(homeActivity, list);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_me, container, false);
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
	
		
		TextViewBarIcon bar=(TextViewBarIcon) v.findViewById(R.id.me_details_icon);
		bar.setTBBarOnClickListener(new TBBarOnClickListener() {
			
			@Override
			public void onTBClick(View v) {
				if(AppConfig.DEBUG)
				{
					startActivity(new Intent(getActivity(), CompleteEnterpriseInfoActivity.class));
				}
				else
				{new NetShipperMsgAsyncTask(new NetShipperMsgAsyncTask.APIListener() {

					@Override
					public String handler(ShipperAccountApi api) {
						return api.getShipper(Myself.ShipperId);

					}

					@Override
					public void finish(String json) {
						Flog.e(json);
						try {
							JSONObject obj = new JSONObject(json);
						
							if (obj.getInt("resultCode") == 1) {
								JSONObject data=obj.getJSONObject("data");
								String website = data
										.getString("website"); //暂无用
								String taxregistno=data
										.getString("taxregistno");//营业执照URL
								String introduce=data
										.getString("introduce");//
								String qrCode=data.getString("qrCode");
								Myself.ContactName = data.getString("contact");
								Myself.DetailAddress=data.getString("detailAddress");
								Myself.Location=data.getString("locationCode");
								int auditStatus=data.getInt("auditStatus");
								int cargoType=data.getInt("cargoType");
								int serialVersionUID=data.getInt("serialVersionUID");
								Myself.FullName=data.getString("fullName");
								String organizationno=data.getString("organizationno");//组织机构代码证的URL
								String head=data.getString("head");
								String businesslicence=data.getString("businesslicence");
								startActivity(new Intent(getActivity(), CompleteEnterpriseInfoActivity.class));
							} else {
								try {
									String error=obj.getString("error");
									ToastUtils.showCrouton(getActivity(),
											error+":"+obj.getInt("resultCode"));
								} catch (Exception e) {
									ToastUtils.showCrouton(getActivity(),
											getString(R.string.error)+obj.getInt("resultCode"));
								}
								
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.showCrouton(getActivity(),
									getString(R.string.exception));
						}

					}
				}, getActivity()).execute(Urls.REGEDIT);}
				
				
			}
		});
		bar.setTBLeftText(Myself.UserName);
		listView = (ExpandableListView) v.findViewById(R.id.listView);
		listView.setAdapter(adapter);
		listView.setOnGroupCollapseListener(this);
		listView.setOnGroupExpandListener(this);
		listView.setOnGroupClickListener(this);
		listView.setOnChildClickListener(this);
		ImageLoaderAdmin.getInstance().displayImage(Myself.HeadIconUrl,
				(ImageView)bar.findViewById(R.id.tb_icon_left));
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
