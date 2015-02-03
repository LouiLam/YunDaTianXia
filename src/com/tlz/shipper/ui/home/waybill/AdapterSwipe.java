
package com.tlz.shipper.ui.home.waybill;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tlz.model.WaybillNews;
import com.tlz.shipper.R;

public class AdapterSwipe extends BaseAdapter {
	private List<WaybillNews> data = new ArrayList<WaybillNews>();
    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 
     */
    private int mRightWidth = 0;

    /**
     * 单击事件监听器
     */
    private IOnItemRightClickListener mListener = null;

    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }

    /**
     * @param mainActivity
     */
    public AdapterSwipe(Context ctx, int rightWidth, IOnItemRightClickListener l,List<WaybillNews>data) {
        mContext = ctx;
        mRightWidth = rightWidth;
        mListener = l;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final int thisPosition = position;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_news, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.name);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.content);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.time);
			viewHolder.target = (TextView) convertView
					.findViewById(R.id.target);
			viewHolder.right = (View)convertView.findViewById(R.id.item_right);
			viewHolder.left=(View)convertView.findViewById(R.id.item_left);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		WaybillNews ksScanResult = data.get(position);
		viewHolder.name.setText(ksScanResult.name);
		viewHolder.content.setText(ksScanResult.content);
		viewHolder.time.setText(ksScanResult.time);
		viewHolder.target.setText(ksScanResult.target);
        LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        viewHolder.left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        viewHolder.right.setLayoutParams(lp2);
        viewHolder.right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightClick(v, thisPosition);
                }
            }
        });
        return convertView;
    }
	static class ViewHolder {
		TextView name;
		TextView content;
		TextView time;
		TextView target;
		View right,left;
	}

 
}
