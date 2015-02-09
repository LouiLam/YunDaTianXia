package com.tlz.shipper.ui.home.waybill.my_waybill;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;

@SuppressLint("HandlerLeak")
public class ActivityMyWaybill extends ThemeActivity implements OnClickListener {

	private FragmentPagerAdapterToMyWaybill mViewPagerAdapter;
	private ViewPager mViewPager;

	private static final int MAIN_TAB = 0;// 主要显示界面

	// Knet mKnet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waybill_my_waybill);
		mActionBar.setTitle(R.string.waybill_myWaybill_title);
		
		init();
	}

	RadioGroup rGroup_Controls;

	private void init() {
		initViewPager();
		rGroup_Controls = (RadioGroup) findViewById(R.id.rdgr_controls);
		RadioButton rLinkRadioButton = (RadioButton) rGroup_Controls
				.getChildAt(MAIN_TAB);
		rLinkRadioButton.setChecked(true);
		rGroup_Controls
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					int tabIndex = MAIN_TAB;

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rdi_yundan:
							tabIndex = 0;
							break;
						case R.id.rdi_guanche:
							tabIndex = 1;
							break;
						case R.id.rdi_fahuo:
							tabIndex = 2;
							break;
						case R.id.rdi_yingyong:
							tabIndex = 3;
							break;
						case R.id.rdi_wo:
							tabIndex = 4;
							break;
						default:
							break;
						}
						mViewPager.setCurrentItem(tabIndex, false);

					}
				});
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.tab_pager);

		mViewPagerAdapter = new FragmentPagerAdapterToMyWaybill(
				getSupportFragmentManager());
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setCurrentItem(MAIN_TAB);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				((RadioButton) rGroup_Controls.getChildAt(arg0))
						.setChecked(true);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

}
