package com.tlz.shipper.ui.register_login;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.viewpagerindicator.CirclePageIndicator;

public class ActivityGuide extends ThemeActivity {

	
	private JasonPagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		init();
	}
	
	private void init() {
		mActionBar.hide();
		initViewPager();
//		new Thread(){
//			public void run() {
//				FileUtils.copyWeatherDb(GuideActivity.this);
//			};
//		}.start();
	}
	
	private void initViewPager() {

		ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
		List<View> viewList = new ArrayList<View>();
		View view1 = View.inflate(this, R.layout.layout_pager1, null);
		Button btnSkip1 = (Button)view1.findViewById(R.id.btn_skip1);
		
		View view2 = View.inflate(this, R.layout.layout_pager2, null);
		Button btnSkip2 = (Button)view2.findViewById(R.id.btn_skip2);
		
		View view3 = View.inflate(this, R.layout.layout_pager3, null);
		TextView mBtnEntry = (TextView)view3.findViewById(R.id.guide_btn_want_reg);
		
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		
		mPagerAdapter = new JasonPagerAdapter(viewList);
		viewPager.setAdapter(mPagerAdapter);
		
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(viewPager);
		//indicator.setOnPageChangeListener(new JasonPageChangeListener());
		
		setViewsClickListener(btnSkip1, btnSkip2, mBtnEntry);
	}
	
	@Override
	public void onClick(int viewId) {
		switch (viewId) {
			case R.id.btn_skip1:
			case R.id.btn_skip2:
			case R.id.guide_btn_want_reg:
				startActivity(new Intent(this, ActivityRegister.class));
				finish();
				break;
		}
	}
	
//	class JasonPageChangeListener extends SimpleOnPageChangeListener {
//		Button mBtnEntry;
//		@Override
//		public void onPageSelected(int position) {
//			
//			if (position == mPagerAdapter.getCount() - 1) {
//				mBtnEntry.setVisibility(View.VISIBLE);
//				TranslateAnimation animation = new TranslateAnimation(
//						-mBtnEntry.getLeft() - mBtnEntry.getWidth(), 0, 
//						mScreenHeight - mBtnEntry.getHeight() / 2 - mBtnEntry.getTop(), 0);
//				animation.setDuration(500);
//				mBtnEntry.startAnimation(animation);
//			} else {
//				mBtnEntry.setVisibility(View.INVISIBLE);
//			}
//		}
//		
//	}
//	
	class JasonPagerAdapter extends PagerAdapter {

		private List<View> mData;
		
		public JasonPagerAdapter(List<View> data) {
			mData = data;
		}
		
		@Override
		public int getCount() {
			return mData == null ? 0 : mData.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			if (container instanceof ViewPager) {
				ViewPager pager = (ViewPager)container;
				pager.addView(mData.get(position));
			}
			return mData.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (container instanceof ViewPager) {
				((ViewPager)container).removeView(mData.get(position));
			}
		}
	}

}
