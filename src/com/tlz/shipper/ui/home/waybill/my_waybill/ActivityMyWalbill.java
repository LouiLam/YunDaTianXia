package com.tlz.shipper.ui.home.waybill.my_waybill;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.tlz.model.User;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.BaseLoadingDialog;
import com.tlz.shipper.ui.FullScreenDialog;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.ViewVisibleListener;
import com.tlz.utils.FaceImageUtils.FaceImageChangeListener;

@SuppressLint("HandlerLeak")
public class ActivityMyWalbill extends ThemeActivity implements OnClickListener,
		 FaceImageChangeListener {

	private FragmentPagerAdapterToMyWaybill mViewPagerAdapter;
	private ViewPager mViewPager;


	private BaseLoadingDialog mLoadingDialog;


	private static final int MAIN_TAB = 0;// 主要显示界面
	TextView barTitleView;
	int actionBarHeight;
	public int operateFlag = -1;

	// Knet mKnet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waybill_my_waybill);
		init();
		boolean isComeFromReg=getIntent().getBooleanExtra("isComeFromReg", false);
		if(isComeFromReg)
		{
			FullScreenDialog.show(this);
		}
		User currentUser = new User();
		setUser(currentUser);
		// setVisible(false);
	}

	RadioGroup rGroup_Controls;
	int mControlsHeight;
	int mShortAnimTime = 200;



	ViewVisibleListener viewVisibleListener;

	public void setViewVisibleListener(ViewVisibleListener viewVisibleListener) {
		this.viewVisibleListener = viewVisibleListener;
	}

	public void setCurrTab(int tabIndex) {
		if (mViewPager != null) {

			mViewPager.setCurrentItem(tabIndex, false);

			if (operateFlag == 0) {
				// MineFragment mf = (MineFragment)
				// mViewPagerAdapter.getFragmentByTag(tabIndex);
				// mf.openDialog();
				operateFlag = -1;
			}
		}
	}

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
		barTitleView = new TextView(this);

		LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

		barTitleView.setVisibility(View.GONE);
		mActionBar.setCustomView(barTitleView, params);
		mActionBar.setDisplayShowCustomEnabled(true);
		actionBarHeight = mActionBar.getHeight();
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.tab_pager);

		mViewPagerAdapter = new FragmentPagerAdapterToMyWaybill(getSupportFragmentManager());
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





	private void showLogoutAlert(final Context context) {
		StringBuffer sb = new StringBuffer();
		sb.append("您将注销当前用户，是否继续？");

		Dialog dialog = new AlertDialog.Builder(context).setTitle("注销提示")
				.setMessage(sb.toString())
				.setPositiveButton("注销", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create();
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		int size = menu.size();
		for (int i = 0; i < size; i++) {
			MenuItemCompat.setShowAsAction(menu.getItem(i),
					MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		}
		return true;

	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		setActionBarMenuIconVisible(featureId, menu);
		return super.onMenuOpened(featureId, menu);
	}

	/**
	 * 利用反射让隐藏在Overflow中的MenuItem显示Icon图标
	 * 
	 * @param featureId
	 * @param menu
	 *            onMenuOpened方法中调�?
	 */
	public static void setActionBarMenuIconVisible(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item;

		// item = menu.findItem(R.id.action_overflow);
		// item.setIcon(R.drawable.ic_menu_more);
		item = menu.findItem(R.id.action_new_yundan);
		item.setVisible(true);
		item = menu.findItem(R.id.action_add_driver);
		item.setVisible(true);
		item = menu.findItem(R.id.action_scan);
		item.setVisible(true);
		item = menu.findItem(R.id.action_feedback);
		item.setVisible(true);

		super.onPrepareOptionsMenu(menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
//		case R.id.action_share:
//			// startShare();
//			break;
//		case R.id.action_register:
//			// startRegiterActivity();
//			break;
//		case R.id.action_login:
//			// startLoginActivity();
//			break;
//		case R.id.action_about:
//			// startActivity(new Intent(this, AboutActivity.class));
//			break;
//		case R.id.action_feedback:
//			// startUserCommentActivity();
//			// showUserCommentDialog();
//			// startTrafficAccountActivity();
//			break;
//		case R.id.action_logout:
//			showLogoutAlert(HomeActivity.this);
//			break;
		// case R.id.action_help:
		// Intent intent = new Intent(this, WebContentActivity.class);
		// intent.putExtra("title", getString(R.string.title_help));
		// intent.putExtra("url", Urls.HELP_URL);
		// startActivity(intent);
		// break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}




	/**
	 * 
	 * @param newVersion
	 *            新版本号
	 * @param oldVersion
	 *            旧版本号
	 * @return true 有新版本 false 无新版本
	 */
	public static boolean isNew(int newVersion, int oldVersion) {
		boolean result = false;

		if (newVersion > oldVersion) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}


	public JSONArray getInstalledApp() {
		PackageManager pManager = this.getPackageManager();
		List<PackageInfo> appList = pManager.getInstalledPackages(0);
		int appCount = appList.size();
		ApplicationInfo appInfo;
		PackageInfo appPInfo;
		JSONObject mkeys = null;
		JSONArray arrayList = new JSONArray();
		try {
			for (int i = 0; i < appCount; ++i) {
				try {
					appPInfo = appList.get(i);
					if ((appPInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
						//
						// String strAppInfo = "";
						appInfo = pManager.getApplicationInfo(
								appPInfo.packageName, 0);
						String appName = pManager.getApplicationLabel(appInfo)
								.toString();
						String packageName = appPInfo.packageName;
						String versionName = appPInfo.versionName;
						String versionCode = "" + appPInfo.versionCode;

						mkeys = new JSONObject();
						mkeys.put("appName", appName);
						mkeys.put("packageName", packageName);
						mkeys.put("versionName", versionName);
						mkeys.put("versionCode", versionCode);
						arrayList.put(mkeys);
					}
				} catch (NameNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrayList;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (MyOntouchListener listener : touchListeners) {
			listener.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	private ArrayList<MyOntouchListener> touchListeners = new ArrayList<ActivityMyWalbill.MyOntouchListener>();

	public void registerListener(MyOntouchListener listener) {
		touchListeners.add(listener);
	}

	public void unRegisterListener(MyOntouchListener listener) {
		touchListeners.remove(listener);
	}

	public interface MyOntouchListener {
		public void onTouchEvent(MotionEvent event);
	}

	public void setNetName(String netName) {
		barTitleView.setText(netName);
	}



	@Override
	public void lightFace() {
		for (int i = 1; i < 3; i++) {
			Fragment mf = (Fragment) mViewPagerAdapter.getFragmentByTag(i);
			if (mf instanceof FaceImageChangeListener) {
				((FaceImageChangeListener) mf).lightFace();
			}
		}

	}

	@Override
	public void greyFace() {
		for (int i = 1; i < 3; i++) {
			Fragment mf = (Fragment) mViewPagerAdapter.getFragmentByTag(i);
			if (mf instanceof FaceImageChangeListener) {
				((FaceImageChangeListener) mf).greyFace();
			}
		}

	}

}
