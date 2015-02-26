package com.tlz.shipper.ui.home.waybill;

import java.util.ArrayList;
import java.util.List;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlz.model.WaybillNews;
import com.tlz.model.WaybillNewsGroup;
import com.tlz.model.WaybillNewsWarp;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.home.ActivityHome;
import com.tlz.shipper.ui.widget.BadgeView;
import com.tlz.utils.CollectionUtils;
import com.tlz.utils.ResIdentifier;
/**
 * 运单Adapter
 *
 */
public class ExpandableListApapterWaybill extends BaseExpandableListAdapter {

	private ActivityHome mContext;
	private List<WaybillNewsGroup> mChildData1 = new ArrayList<WaybillNewsGroup>();
	WaybillNewsWarp warp;

	public ExpandableListApapterWaybill(ActivityHome context,WaybillNewsWarp warp) {
		mContext = context;
		this.warp=warp;
		splitData(warp.list, true);
	
	}
	public void setWarp(WaybillNewsWarp warp)
	{
		this.warp=warp;
	}

	private void splitData(List<WaybillNewsGroup> list, boolean isClear) {
		if (CollectionUtils.isEmpty(list))
			return;
		if (isClear) {
			mChildData1.clear();
		}
		for (WaybillNewsGroup result : list) {
			mChildData1.add(result);
		}
	}

	public boolean haveKsAround() {
		return mChildData1.size() == 1 ? false : true;
	}


	public void update(List<WaybillNewsGroup> datas) {
		splitData(datas, true);
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		return 5;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition == 0) {
			return mChildData1.size();
		}
		else {
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public WaybillNewsGroup getChild(int groupPosition, int childPosition) {
		return mChildData1.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.list_expandable_group, parent, false);
		View icon = (ImageView) view.findViewById(R.id.icon);
		TextView title = (TextView) view.findViewById(R.id.title);
		String titles[]=mContext.getResources().getStringArray(R.array.yundan_titles);
		title.setText(titles[groupPosition]);
		icon.setBackgroundResource(ResIdentifier.getDrawbleIDByName(mContext, "icon_home_yundan_"+groupPosition));
		if(groupPosition==0)
		{BadgeView badge = new BadgeView(mContext, icon);
		//大与两位数只显示原点，不显示文字
		badge.setText(warp.totalCount+"");
		badge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
		badge.setBackgroundResource(R.drawable.icon_circle);
    	badge.setBadgeMargin(0, 0);
    	badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
    	badge.toggle(true);}
    	
		return view;
		
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_expandable_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.name);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.content);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.time);
			viewHolder.target = (TextView) convertView
					.findViewById(R.id.target);
			viewHolder.icon= (ImageView) convertView
					.findViewById(R.id.icon);
			viewHolder.badge=new BadgeView(mContext, viewHolder.icon);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		WaybillNewsGroup group = getChild(groupPosition, childPosition);
		viewHolder.name.setText(group.name);
		viewHolder.content.setText(group.description);
		viewHolder.time.setText("");
		viewHolder.target.setText("");
		if(group.type==WaybillNews.TYPE_SYS)
		viewHolder.icon.setBackgroundResource(R.drawable.icon_sys_msg);
		
		//大与两位数只显示原点，不显示文字
		viewHolder.badge.setText(group.count+"");
		viewHolder.badge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
		viewHolder.badge.setBackgroundResource(R.drawable.icon_circle);
		viewHolder.badge.setBadgeMargin(0, 0);
		viewHolder.badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		viewHolder.badge.toggle(true);
	
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	static class ViewHolder {
		public BadgeView badge;
		TextView name;
		TextView content;
		TextView time;
		TextView target;
		ImageView icon;
	}

}
