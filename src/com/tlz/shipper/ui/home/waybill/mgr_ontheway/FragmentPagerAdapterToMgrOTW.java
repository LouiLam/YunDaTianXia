package com.tlz.shipper.ui.home.waybill.mgr_ontheway;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.tlz.shipper.ui.home.waybill.FragmentWaybill;
import com.tlz.shipper.ui.home.waybill.my_waybill.FragmentWait;
/**
 * 我的运单 
 *
 */
public class FragmentPagerAdapterToMgrOTW extends FragmentPagerAdapter {

	private FragmentManager mFragmentManager;
	private int mContainer1Id;
	private int mContainer2Id;
	private int mContainer3Id;

	public FragmentPagerAdapterToMgrOTW(FragmentManager fm) {
		super(fm);
		mFragmentManager = fm;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return FragmentOTW.newInstance();
		case 1:
			return FragmentArrive.newInstance();
		}
		return FragmentOTW.newInstance();
	}

	@Override
	public int getCount() {
		return 2;
	}

//	public Fragment getFragmentByTag(int tag) {
//		switch (tag) {
//		case 0:
//			return mFragmentManager.findFragmentByTag("android:switcher:"
//					+ mContainer1Id + ":" + tag);
//		case 1:
//			return mFragmentManager.findFragmentByTag("android:switcher:"
//					+ mContainer2Id + ":" + tag);
//		case 2:
//			return mFragmentManager.findFragmentByTag("android:switcher:"
//					+ mContainer3Id + ":" + tag);
//
//		}
//		return null;
//	}
//
//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//		switch (position) {
//		case 0:
//			mContainer1Id = container.getId();
//			break;
//		case 1:
//			mContainer2Id = container.getId();
//		case 2:
//			mContainer3Id = container.getId();
//
//		}
//		return super.instantiateItem(container, position);
//	}
}
