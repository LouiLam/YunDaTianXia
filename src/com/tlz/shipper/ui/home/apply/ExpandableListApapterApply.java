package com.tlz.shipper.ui.home.apply;

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
import com.tlz.shipper.R;
import com.tlz.shipper.ui.home.ActivityHome;
import com.tlz.shipper.ui.widget.BadgeView;
import com.tlz.utils.CollectionUtils;
import com.tlz.utils.ResIdentifier;

public class ExpandableListApapterApply extends BaseExpandableListAdapter {

	private ActivityHome mContext;
	private List<WaybillNews> mChildData1 = new ArrayList<WaybillNews>();


	public ExpandableListApapterApply(ActivityHome context, List<WaybillNews> datas) {
		mContext = context;
		splitData(datas, true);
	
	}


	private void splitData(List<WaybillNews> data, boolean isClear) {
		if (CollectionUtils.isEmpty(data))
			return;
		if (isClear) {
			mChildData1.clear();
		}
		for (WaybillNews result : data) {
			mChildData1.add(result);
		}
	}

	public boolean haveKsAround() {
		return mChildData1.size() == 1 ? false : true;
	}


	public void update(List<WaybillNews> datas) {
		splitData(datas, true);
		notifyDataSetChanged();
	}

	// public void addItem(YunDanNews item) {
	// if (item.isKsWifi) {
	// mChildData1.add(item);
	// } else {
	// mChildData2.add(item);
	// }
	// notifyDataSetChanged();
	// }
	//
	// public void addItems(List<YunDanNews> items) {
	// splitData(items, false);
	// notifyDataSetChanged();
	// }

	@Override
	public int getGroupCount() {
		return 5;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition == 0) {
			return mChildData1.size();
		}
		// else if (groupPosition == 1) {
		// return mChildData2.size();
		// }
		else {
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public WaybillNews getChild(int groupPosition, int childPosition) {
//		if (groupPosition == 0) {
//			return mChildData1.get(childPosition);
//		}
		// else if (groupPosition == 1) {
		// return mChildData2.get(childPosition);
		// }
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
		String titles[]=mContext.getResources().getStringArray(R.array.apply_titles);
		title.setText(titles[groupPosition]);
		icon.setBackgroundResource(ResIdentifier.getDrawbleIDByName(mContext, "icon_home_yundan_"+groupPosition));
    	
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
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		WaybillNews ksScanResult = getChild(groupPosition, childPosition);
		viewHolder.name.setText(ksScanResult.name);
		viewHolder.content.setText(ksScanResult.content);
		viewHolder.time.setText(ksScanResult.time);
		viewHolder.target.setText(ksScanResult.target);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	static class ViewHolder {
		TextView name;
		TextView content;
		TextView time;
		TextView target;
	}

}
