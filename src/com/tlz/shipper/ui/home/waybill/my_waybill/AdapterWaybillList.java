package com.tlz.shipper.ui.home.waybill.my_waybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tlz.model.MyWaybill;
import com.tlz.shipper.R;
import com.tlz.utils.Flog;

/**
 * 运单列表
 * 
 */
public class AdapterWaybillList extends BaseAdapter {
	private List<MyWaybill> dataPrimitive;
	private List<MyWaybill> dataCombined = new ArrayList<MyWaybill>();

	HashMap<Integer, List<MyWaybill>> dataYears = new HashMap<Integer, List<MyWaybill>>();
	HashMap<String, List<MyWaybill>> dataDays = new HashMap<String, List<MyWaybill>>();
	/**
	 * 上下文对象
	 */
	private Context mContext = null;

	/**
     * 
     */

	public interface IOnItemRightClickListener {
		void onRightClick(View v, int position);
	}

	HashSet<Integer> setYear = new HashSet<Integer>();
	HashSet<String> setYearMonthDay = new HashSet<String>();

	public AdapterWaybillList(Context ctx, List<MyWaybill> data) {
		mContext = ctx;
		this.dataPrimitive = data;
		// 检索 同一年的数据
		for (MyWaybill myWaybill : data) {
			if (!setYear.contains(myWaybill.year)) {
				setYear.add(myWaybill.year);
			}
		}
		Iterator<Integer> iteratorYear = setYear.iterator();
		while (iteratorYear.hasNext()) {
			int year = iteratorYear.next();
			List<MyWaybill> list = new ArrayList<MyWaybill>();
			for (MyWaybill myWaybill : data) {
				if (myWaybill.year == year) {
					list.add(myWaybill);
				}
			}
			dataYears.put(year, list);
		}
		// 检索同年同月同日的数据
		// 检索 同一年的数据
		for (MyWaybill myWaybill : data) {
			if (!setYearMonthDay.contains(myWaybill.date)) {
				setYearMonthDay.add(myWaybill.date);
			}
		}
		Iterator<String> iteratorDay = setYearMonthDay.iterator();
		while (iteratorDay.hasNext()) {
			String yearMonthDay = iteratorDay.next();
			List<MyWaybill> list = new ArrayList<MyWaybill>();
			for (MyWaybill myWaybill : data) {
				if (myWaybill.date.equals(yearMonthDay)) {
					list.add(myWaybill);
				}
			}
			dataDays.put(yearMonthDay, list);
		}

		// 年 title
		Iterator<Integer> iterYears = dataYears.keySet().iterator();
		while (iterYears.hasNext()) {
			Integer keyYears = iterYears.next();
			MyWaybill bill = dataYears.get(keyYears).get(0);
			dataCombined.add(new MyWaybill(bill.time, MyWaybill.TYPE_DATE_YEAR));
			// 年月日 title

			Iterator<String> iterDay = dataDays.keySet().iterator();
			while (iterDay.hasNext()) {

				String keyDay = iterDay.next();
				List<MyWaybill> valDay = dataDays.get(keyDay);

				dataCombined.add( new MyWaybill(valDay.get(0).time,
						MyWaybill.TYPE_DATE_SAME_DAY));
				// 真实数据
				for (MyWaybill myWaybill : valDay) {
					dataCombined.add(myWaybill);
				}
			}
		}

		// Calendar d=Calendar.getInstance();
		// d.setTimeInMillis(myWaybill.time*1000);
	}

	@Override
	public int getItemViewType(int position) {
		return dataCombined.get(position).curType;
	}

	@Override
	public int getCount() {
		return dataCombined.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder viewHolder = null;
		// Flog.e(data.get(position).date);
		int type = getItemViewType(position);
		MyWaybill obj = dataCombined.get(position);
		switch (type) {
		case MyWaybill.TYPE_DATE_SAME_DAY: {
			ViewHolderDateText holder = null;
			if (convertView == null) {
				holder = new ViewHolderDateText();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_waybill_list_year_month_day, parent,
						false);
				holder.text = (TextView) convertView
						.findViewById(R.id.yearMonthDay);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderDateText) convertView.getTag();
			}
			holder.text.setText(String
					.format("%d日%d月 装车发货", obj.day, obj.month));

		}
			break;
		case MyWaybill.TYPE_DATE_YEAR: {
			ViewHolderDateText holder = null;
			if (convertView == null) {
				holder = new ViewHolderDateText();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_waybill_list_year, parent, false);
				holder.text = (TextView) convertView.findViewById(R.id.year);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderDateText) convertView.getTag();
			}
			holder.text.setText(String.format("%d年", obj.year));

		}
			break;
		case MyWaybill.TYPE_CONTENT: {
			ViewHolderContent holder = null;
			if (convertView == null) {
				holder = new ViewHolderContent();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_waybill_list, parent, false);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.licensePlate = (TextView) convertView
						.findViewById(R.id.licensePlate);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				holder.toProvinceCityDistrict = (TextView) convertView
						.findViewById(R.id.toProvinceCityDistrict);
				holder.fromCity = (TextView) convertView
						.findViewById(R.id.fromCity);
				holder.toCity = (TextView) convertView
						.findViewById(R.id.toCity);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderContent) convertView.getTag();
			}
		}
			break;
		default:
			break;
		}
		return convertView;
	}

	static class ViewHolderContent {
		TextView name;
		TextView licensePlate;
		TextView content;
		TextView toProvinceCityDistrict;
		TextView fromCity;
		TextView toCity;
	}

	static class ViewHolderDateText {
		TextView text;
	}
}
