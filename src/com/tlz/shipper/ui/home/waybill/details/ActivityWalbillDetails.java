package com.tlz.shipper.ui.home.waybill.details;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;

@SuppressLint("HandlerLeak")
public class ActivityWalbillDetails extends ThemeActivity implements OnClickListener
		  {




	TextView barTitleView;
	int actionBarHeight;
	public int operateFlag = -1;

	// Knet mKnet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waybill_details);
		init();
		// setVisible(false);
	}





	private void init() {
	}

	






	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.waybill_details, menu);
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

//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		MenuItem item;
//
//		// item = menu.findItem(R.id.action_overflow);
//		// item.setIcon(R.drawable.ic_menu_more);
//		item = menu.findItem(R.id.action_new_yundan);
//		item.setVisible(true);
//		item = menu.findItem(R.id.action_add_driver);
//		item.setVisible(true);
//		item = menu.findItem(R.id.action_scan);
//		item.setVisible(true);
//		item = menu.findItem(R.id.action_feedback);
//		item.setVisible(true);
//
//		super.onPrepareOptionsMenu(menu);
//		return true;
//	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_waybill_modify:
			// startShare();
			break;
		case R.id.menu_waybill_location:
			// startRegiterActivity();
			break;
		case R.id.menu_waybill_damaged:
			// startLoginActivity();
			break;
		case R.id.menu_waybill_del:
			// startActivity(new Intent(this, AboutActivity.class));
			break;
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


	





}
