package com.tlz.shipper.ui.home;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
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
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
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

import com.baidu.location.BDLocation;
import com.tlz.model.Myself;
import com.tlz.model.User;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.BaseLoadingDialog;
import com.tlz.shipper.ui.FullScreenDialog;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.ViewVisibleListener;
import com.tlz.shipper.ui.home.waybill.ActivityCreate;
import com.tlz.utils.AndroidHttp;
import com.tlz.utils.AndroidHttp.HttpCallback;
import com.tlz.utils.CrashHandler;
import com.tlz.utils.DeviceUtils;
import com.tlz.utils.FaceImageUtils.FaceImageChangeListener;
import com.tlz.utils.Flog;
import com.tlz.utils.HandlerMsg;
import com.tlz.utils.ToastUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

@SuppressLint("HandlerLeak")
public class ActivityHome extends ThemeActivity implements OnClickListener,
		 FaceImageChangeListener {
	private TextView mErrorNetTips,mErrorNetTips1;
	public static final int REQUEST_CODE_QR_CODE = 0;
	private static final int MAXQUERYSERVERCOUNT = 30;

	protected static final int HAS_NEW = 0;
	protected static final int NO_HAS_NEW = 1;
	protected static final int TIPS_MSG = 2;
	protected static final int MSG_UPDATE_TRAFFIC = 3;
	protected static final int AUTO_LOGIN = 4;
	protected static final int BING_SUCCESS = 5;

	public static final int REQUESTCODE = 22;

	// private AppUpdateInfo appUpdateInfo;
	private Context mContext;

	public View bottomView;

	private FragmentPagerAdapterToHome mViewPagerAdapter;
	private ViewPager mViewPager;
	public int offset;
	public int bmpW;

	private CountDownTimer mObtrainTrafficTimer;
	private int queryServerCount = 0;

	// private KnetListener mAbKnetListener;
	// private final static String KUAISHANGAPPKEY = "CWX0000ASWERFVUOHSQ";

	// private WifiManager mWifiMgr;

	private BaseLoadingDialog mLoadingDialog;

	private UMSocialService mController;

	private static final int MAIN_TAB = 0;// 主要显示界面
	TextView barTitleView;
	int actionBarHeight;
	public int operateFlag = -1;
	public static int count111=0;
	// Knet mKnet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initActionBarNoBackAndTwoPress();
		count111++;
		init();
		boolean isComeFromReg = getIntent().getBooleanExtra("isComeFromReg",
				false);
		if (isComeFromReg) {
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
		mErrorNetTips = (TextView) findViewById(R.id.tv_error_net_tips);
		mErrorNetTips1= (TextView) findViewById(R.id.tv_error_net_tips1);
		mErrorNetTips.setOnClickListener(this);
		mErrorNetTips.setVisibility(View.GONE);
		mErrorNetTips1.setVisibility(View.VISIBLE);
		mErrorNetTips1.setText("顶部条通过init不可见count次数："+count111);
		rGroup_Controls = (RadioGroup) findViewById(R.id.rdgr_controls);
		RadioButton rLinkRadioButton = (RadioButton) rGroup_Controls
				.getChildAt(MAIN_TAB);
		rLinkRadioButton.setChecked(true);
//		if (rLinkRadioButton != null) {
//			rLinkRadioButton.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					if (currTab == MAIN_TAB && !tabChanged) {
//						if (viewVisibleListener != null)
//							viewVisibleListener.viewVisible();
//					}
//					if (currTab == MAIN_TAB)
//						tabChanged = false;
//
//				}
//			});
//			// rLinkRadioButton.setOnLongClickListener(new OnLongClickListener()
//			// {
//			//
//			// @Override
//			// public boolean onLongClick(View v) {
//			// if (currTab == MAIN_TAB && !tabChanged) {
//			// Toast.makeText(mContext, "智能联网启动", Toast.LENGTH_SHORT)
//			// .show();
//			// }
//			// return true;
//			// }
//			// });
//		}
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
//						if (currTab == tabIndex) {
//							if (currTab == MAIN_TAB) {
//								tabChanged = false;
//							}
//							return;
//						} else {
//							tabChanged = true;
//						}

						mViewPager.setCurrentItem(tabIndex, false);

					}
				});
		autoLogin(ActivityHome.this);
		mContext = this;
		// PreferenceUtils.init(HomeActivity.this);
		// mWifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);

		// initYouzu();

		// mAbKnetListener = new KnetListener() {
		// @Override
		// public void onOpenWifiSuccess() {
		// InternetFragment temp = getInternetFragment();
		// if (temp != null) {
		// temp.scanNearbyWifi();
		// }
		// super.onOpenWifiSuccess();
		// }
		// @Override
		// public void onBindSuccess(List<KScanResult> arg0) {
		// mHandler.sendEmptyMessage(BING_SUCCESS);
		// super.onBindSuccess(arg0);
		// }
		//
		// @Override
		// public void onBindFailure(int errorCode) {
		// ToastUtils.show(HomeActivity.this, "非法用户，请与客服联�?);
		// super.onBindFailure(errorCode);
		// }
		// };

		// if (PhoneUtils.isNetworkAvailable(HomeActivity.this)) {
		// checkUpdate(this);
		// }

		// initShare();
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

		mViewPagerAdapter = new FragmentPagerAdapterToHome(
				getSupportFragmentManager());
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setCurrentItem(MAIN_TAB);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
//				if (arg0 != MAIN_TAB)
//					barTitleVisible(false);
//				if (arg0 == MAIN_TAB)
//					currTab = arg0;
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

	// private InternetFragment getInternetFragment() {
	// return (InternetFragment) mViewPagerAdapter.getFragmentByTag(MAIN_TAB);
	// }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		if (requestCode == REQUEST_CODE_QR_CODE && resultCode == RESULT_OK) {
		}
	}

	/**
	 * 自动登录
	 * 
	 * @param context
	 */
	public void autoLogin(final Context context) {
		User user = getUser();
		boolean bNeedAutoLogin = true;
		if (user == null) {
			bNeedAutoLogin = true;
		} else if (!user.isValideUser()) {
			bNeedAutoLogin = true;
		}
		if (bNeedAutoLogin) {

			// Callback<String> pAutoLoginCallback = new Callback<String>() {
			//
			// @Override
			// public void onSuccess(String status, String msg, String result) {
			// if (status.equals("1"))// 1成功�?参数错误�?已存�?
			// {
			// try {
			// JSONObject jo = new JSONObject(result);
			//
			// String userId = jo.optString("userId");
			// User currentUser = getUser();
			// currentUser.setId(userId);
			// currentUser.setUserValide(true);
			// String number =
			// SharePreferenceUtils.getValue(HomeActivity.this,SharePreferenceUtils.KEY_USER_NAME,
			// "");
			//
			// if (number != null && !TextUtils.isEmpty(number)) {
			// currentUser.setPhoneNumber(number);
			// mHandler.sendEmptyMessage(AUTO_LOGIN);
			// lightFace();
			// } else {
			// greyFace();
			// currentUser.setUserValide(false);
			// }
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// } else {
			// if (mLoadingDialog != null) {
			// mLoadingDialog.dismiss();
			// }
			// }
			// }
			//
			// @Override
			// public void onFailure(String statue, String message, Exception e)
			// {
			// }
			// };
			//
			// String userName =
			// SharePreferenceUtils.getValue(HomeActivity.this,
			// SharePreferenceUtils.KEY_USER_NAME, "");
			// String password =
			// SharePreferenceUtils.getValue(HomeActivity.this,
			// SharePreferenceUtils.KEY_USER_PWD, "");
			//
			// if (userName.length() > 0 && password.length() >= 6) {
			// ServerHelper.login(context, userName, password,
			// com.kkg6.kuaishang.util.DeviceUtils
			// .getDeviceUniqueId(context), DeviceUtils
			// .getIMEI(context), DeviceUtils.getOsVersion(),
			// DeviceUtils.getMAC(context), DeviceUtils.getOsModel(),
			// pAutoLoginCallback);
			// }
		}
	}

	private void invalidateMenu() {
		supportInvalidateOptionsMenu();
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
						logout();
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
	 *            onMenuOpened方法中调
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
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_new_yundan:
			startActivity(new Intent(this, ActivityCreate.class));
			break;
		case R.id.action_feedback:
			startTimer();
//			startActivity(new Intent(this, FeedbackActivity.class));
			break;
		case R.id.action_scan:
			count = 0;
			mErrorNetTips.setVisibility(View.GONE);
			mErrorNetTips1.setVisibility(View.VISIBLE);
			mErrorNetTips1.setText("顶部条通过点击停止定位不可见");
			stopTimer();
//			Intent intent = new Intent();
//			intent.setClass(this, QRCodeScanningActivity.class);
//			startActivityForResult(intent, REQUEST_CODE_QR_CODE);
			break;
		case R.id.action_add_driver:
			
			break;

		// case R.id.action_login:
		// // startLoginActivity();
		// break;
		// case R.id.action_about:
		// // startActivity(new Intent(this, AboutActivity.class));
		// break;
		// case R.id.action_feedback:
		// // startUserCommentActivity();
		// // showUserCommentDialog();
		// // startTrafficAccountActivity();
		// break;
		// case R.id.action_logout:
		// showLogoutAlert(HomeActivity.this);
		// break;
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

	private void startShare() {
		mController.openShare(this, new SnsPostListener() {

			public void onStart() {

			}

			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {

			}
		});
	}

	private void initShare() {
		com.umeng.socialize.utils.Log.LOG = true;
		// 微信
		String appIdWX = "wx81c8289aa5318526";
		String appSecret = "defd1ef4f88e189a91fb4c255205a219";
		// QQ
		String appIdQQ = "1103078627";
		String appKey = "pb3T51d6h4VxCcVJ";

		String sinaStr = "免费上网，就用快上！注册快上就送20小时免费上网时长！官网下载地址http://android.kuaishang100.com/download/kuaishang.apk";

		String smsStr = "免费上网软件首选快上，现在下载就送20小时免费时长，快来下载吧！点击链接直接下载http://android.kuaishang100.com/download/kuaishang.apk";

		String tarGetUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.kkg6.kuaishang";
		String title = "体验新版快上，领取免费上网时长！";
		String weixinStr = "【20小时免费上网】年终红包派送中，没钱也任性，你敢来我就敢送！";

		UMImage shareImageWeiXin = new UMImage(this, R.drawable.banner_weixin);
		UMImage shareImageSina = new UMImage(this, R.drawable.banner_sina);

		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);

		mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.SMS);
		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appIdWX, appSecret);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appIdWX, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 添加QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appIdQQ, appKey);
		qqSsoHandler.addToSocialSDK();
		// 添加QZone
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appIdQQ,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent(sinaStr);
		// 设置点击消息的跳转URL
		qzone.setTargetUrl(tarGetUrl);
		// 设置分享内容的标题
		qzone.setTitle(title);
		// 设置分享图片
		qzone.setShareImage(shareImageSina);
		mController.setShareMedia(qzone);

		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent(weixinStr);
		// 设置分享title
		qqShareContent.setTitle(title);
		// 设置分享图片
		qqShareContent.setShareImage(shareImageWeiXin);
		// 设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(tarGetUrl);
		mController.setShareMedia(qqShareContent);

		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareContent(sinaStr);
		sinaContent.setShareImage(shareImageSina);
		mController.setShareMedia(sinaContent);

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(weixinStr);
		// 设置title
		weixinContent.setTitle(title);
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(tarGetUrl);
		// 设置分享图片
		weixinContent.setShareImage(shareImageWeiXin);
		mController.setShareMedia(weixinContent);

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(weixinStr);
		// 设置朋友圈title
		circleMedia.setTitle(title);
		circleMedia.setShareImage(shareImageWeiXin);
		circleMedia.setTargetUrl(tarGetUrl);
		mController.setShareMedia(circleMedia);

		// 设置分享内容
		mController.setShareContent(smsStr);

	}

	private CountDownTimer mCutdownTimer = null;
	private int mRemainderTime = 60;
	private int count = 0;
	private int body = 0;
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case HandlerMsg.LocationSuc:
			final BDLocation location = (BDLocation) msg.obj;
			JSONObject obj = null;
			try {
				obj = new JSONObject();
				obj.put("Cellphone", Myself.UserName);
				obj.put("SourceId", "233455");
				obj.put("DeviceCode",DeviceUtils.getIMEI(ActivityHome.this));
				obj.put("Longitude", location.getLongitude());
				obj.put("Latitude", location.getLatitude());
			} catch (Exception e) {
				mErrorNetTips1.setVisibility(View.VISIBLE);
				mErrorNetTips1.setText(CrashHandler.getErrorMsg(e));
			}
			Flog.e(obj.toString());
			AndroidHttp.getInstance().doRequest("http://120.24.234.112/service/gps", obj.toString(), new HttpCallback() {
				@Override
				public void onRequestSuccess(String tag, String jsonString) {handlerResult(jsonString);}
				@Override
				public void onRequestFinish(String tag) {}
				@Override
				public void onRequestFailure(String tag, int errorCode) {handlerResult(null);}
			});
			break;
		case HandlerMsg.LocationFailed:
			handlerResult(null);
			break;
		default:
			break;
		}
		return super.handleMessage(msg);
	}

	private void startTimer() {
		initLocation();
	}
	private void handlerResult(String jsonString)
	{
		mErrorNetTips1.setVisibility(View.GONE);
		if(jsonString==null)
		{
			mRemainderTime = 300;
		}
		else
		{
			try {
				JSONObject  objResult=new JSONObject(jsonString);
				if(objResult.getString("Code").equals("0000"))
				{
					body=objResult.getInt("Body");
				}
				else
				{body=0;}
			} catch (JSONException e) {
				mErrorNetTips1.setVisibility(View.VISIBLE);
				mErrorNetTips1.setText(CrashHandler.getErrorMsg(e));
				body=0;
			}
			Flog.e(jsonString);
			count++;
			if(body!=0)
			mRemainderTime = body;
			else
			mRemainderTime = 300;
		}
		mErrorNetTips.setVisibility(View.VISIBLE);
			
		if (mCutdownTimer == null) {
			mCutdownTimer = new CountDownTimer(mRemainderTime * 1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					mRemainderTime--;
					mErrorNetTips.setText("离下次获取经纬度的时间还有，" + mRemainderTime
							+ "秒" + ",定位并且发送数据成功次数:" + count);
					Flog.e(mRemainderTime + "秒");
				}

				@Override
				public void onFinish() {
					mRemainderTime = 0;
					stopTimer();
					startTimer();
				}
			};
			mCutdownTimer.start();
		}
	
	}
	private void stopTimer() {

		if (mCutdownTimer != null) {
			mCutdownTimer.cancel();
			mCutdownTimer = null;
		}
	}



	// private void startUserCommentActivity() {
	// Intent intent = new Intent();
	// intent.setClass(HomeActivity.this, FeedbackActivity.class);
	// this.startActivity(intent);
	// }
	//
	// public void startLoginActivity() {
	// startActivity(new Intent(this, LoginActivity.class));
	// }
	//
	// public void startRegiterActivity() {
	// Intent intent = new Intent();
	// intent.setClass(this, RegisterActivity.class);
	// intent.putExtra("target", RegisterActivity.REGISTER);
	// startActivity(intent);
	// }

	/**
	 * 注销
	 */
	private void logout() {
		ToastUtils.show(ActivityHome.this, "注销成功");
		getUser().setUserValide(false);
		getUser().setTotalTime(0);
		getUser().setPhoneNumber("");
		// SharePreferenceUtils.setValue(HomeActivity.this,SharePreferenceUtils.KEY_USER_PWD,
		// "");
		//
		// mKnet.disconnectWifi(mAbKnetListener);
		//
		// InternetFragment temp = getInternetFragment();
		greyFace();
		// if (temp != null) {
		// temp.updateTraffic(0);
		// temp.refreshScanResultList();
		// }

		// 注销鉴权
		// mKnet.unAuthentication();
		// Intent nofityIntent = new
		// Intent(LoginActivity.LOGOUT_SUCCESS_ACTION);
		// HomeActivity.this.sendBroadcast(nofityIntent);
		invalidateMenu();

	}

	// private void initYouzu() {
	//
	// // 初始化游族sdk
	// String channelId = "3b151b6fe72cfcb2512334130f7d70b8";
	//
	// // �?��要在打开app的时候调�?
	// ChannelApi.getInstance().init(HomeActivity.this, channelId, "kuaishang");
	// }

	@Override
	protected void onDestroy() {
		// unRegisterReceiver();
		// if (mKnet != null) {
		// mKnet.destroy();
		// }
		super.onDestroy();
	}

	// private Handler mHandler = new Handler() {
	// InternetFragment temp;
	//
	// public void handleMessage(android.os.Message msg) {
	// switch (msg.what) {
	// case HAS_NEW:
	// ParamsConstant.DOWN_LOAD_NEW_URL = appUpdateInfo.getUrl();
	// doNewVersionUpdateDialog(mContext);
	// break;
	// case NO_HAS_NEW:
	// // Utils.show(getBaseContext(),
	// // getResources().getString(R.string.version_isnew));
	// break;
	// case TIPS_MSG:
	// // showTipsDialog("赠�?流量提示", msg.obj.toString());
	// // ((InternetFragment) mInternetFragment)
	// // .upDateTraffic((int) (getUser().getTotalTime()));
	// updateUserTraffic(HomeActivity.this);
	// temp = getInternetFragment();
	// if (temp != null) {
	// temp.scanNearbyWifi();
	// }
	// ToastUtils.showCrouton(HomeActivity.this, msg.obj.toString());
	// break;
	// case MSG_UPDATE_TRAFFIC:
	// temp = getInternetFragment();
	// if (temp != null) {
	// temp.updateTraffic((int) (getUser().getRemainingTime()));
	// }
	// break;
	// case AUTO_LOGIN:
	// temp = getInternetFragment();
	// if (temp != null) {
	// temp.closeUsefulNetAlertDiaolog();
	// }
	// invalidateMenu();
	// updateUserTraffic(HomeActivity.this);
	// bindingToSdk();
	// break;
	// case BING_SUCCESS:
	// // updateUserTraffic(HomeActivity.this);
	// temp = getInternetFragment();
	// if (temp != null) {
	// temp.scanNearbyWifi();
	// temp.informUserUsefulWifi();
	// }
	// break;
	// default:
	// break;
	// }
	// };
	// };

	// private void showTipsDialog(final String title, final String content) {
	// final Dialog dialog = new Dialog(HomeActivity.this,
	// R.style.Jason_Dialog_None);
	// dialog.setContentView(R.layout.item_protocol_dialog);
	// dialog.findViewById(R.id.btn_close).setOnClickListener(
	// new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// }
	// });
	// ((TextView) dialog.findViewById(R.id.tv_tips_title)).setText(title);
	//
	// ((TextView) dialog.findViewById(R.id.tv_tips_content)).setText(content);
	//
	// WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
	// params.width = DensityUtil.dip2px(HomeActivity.this, 300);
	// // params.height = DensityUtil.dip2px(HomeActivity.this, 400);
	// dialog.getWindow().setAttributes(params);
	// dialog.show();
	// }

	// private void checkUpdate(final Context context, final JSONObject result)
	// {
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// appUpdateInfo = new AppUpdateInfo();
	//
	// JSONObject jo = new JSONObject(result.toString());
	// if (jo.optString("update").equals("0")) {
	// mHandler.sendEmptyMessage(NO_HAS_NEW);
	// } else {
	// appUpdateInfo = appUpdateInfo.parse(result);
	// MLog.d("appUpdateInfo====" + appUpdateInfo);
	// if (0 < appUpdateInfo.getCode()) {
	// if
	// (isNew(appUpdateInfo.getVersionCode(),Integer.valueOf(DeviceUtils.getCurrentVersionCode(context))))
	// {
	// mHandler.sendEmptyMessage(HAS_NEW);
	// } else {
	// mHandler.sendEmptyMessage(NO_HAS_NEW);
	// }
	// } else {
	// mHandler.sendEmptyMessage(NO_HAS_NEW);
	// }
	// }
	// if (mLoadingDialog != null) {
	// mLoadingDialog.dismiss();
	// }
	// } catch (Exception e) {
	// if (mLoadingDialog != null) {
	// mLoadingDialog.dismiss();
	// }
	// MLog.d("e", e);
	// mHandler.sendEmptyMessage(NO_HAS_NEW);
	// }
	// }
	// }).start();
	// }

	// private void checkUpdate(final Context context) {
	// Callback<String> getVersionDateCallBack = new Callback<String>() {
	//
	// @Override
	// public void onSuccess(String status, String msg, String result) {
	// try {
	// JSONObject jo = new JSONObject(result);
	// checkUpdate(context, jo);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onFailure(String status, String msg, Exception e) {
	//
	// }
	//
	// };
	// ServerHelper.checkUpdateApp(context,
	// getPackageName(),DeviceUtils.getCurrentVersionName(context),DeviceUtils.getCurrentVersionCode(context),getVersionDateCallBack);
	// }

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

	// /**
	// * 升级对话框
	// */
	// private void doNewVersionUpdateDialog(final Context context) {
	// StringBuffer sb = new StringBuffer();
	// sb.append(getResources().getString(R.string.find_new_version));
	// sb.append(appUpdateInfo.getVersion()); // 新版本版�?
	// sb.append(getResources().getString(R.string.is_no_update));
	// sb.append(getResources().getString(R.string.update_content));
	// sb.append(appUpdateInfo.getDescription()); // 新版本特征描�?
	// Dialog dialog = new AlertDialog.Builder(context)
	// .setTitle(getResources().getString(R.string.syb_manage_up))
	// .setMessage(sb.toString())
	// .setPositiveButton(getResources().getString(R.string.update_new),
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,int which) {
	// if
	// (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
	// {
	// startUpdateApp();
	// } else {
	// Utils.show(context, getResources().getString(R.string.no_storage));
	// }
	// if (appUpdateInfo.isForceUpdate()) {
	// ((Activity) context).finish();
	// }
	// }
	// })
	// .setNegativeButton(
	// getResources().getString(R.string.alipay_cancel),
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	// if (appUpdateInfo.isForceUpdate()) {
	// ((Activity) context).finish();
	// }
	// dialog.cancel();
	// }
	// }).create();
	// dialog.show();
	// }

	// private void startUpdateApp() {
	// Intent intent = new Intent(this, AppVersionUpdateService.class);
	// this.startService(intent);
	//
	// }

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

	// private BroadcastReceiver mLoginRegisterReceiver = new
	// BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String action = intent.getAction();
	// InternetFragment temp = getInternetFragment();
	// if (action.equals(LoginActivity.LOGIN_SUCCESS_ACTION)) {
	// invalidateMenu();
	// updateUserTraffic(HomeActivity.this);
	// lightFace();
	// bindingToSdk();
	// if (temp != null) {
	// temp.closeUsefulNetAlertDiaolog();
	// }
	// } else if (action.equals(RegisterActivity.REGISTER_SUCCESS_ACTION)||
	// action.equals(RegisterActivity.BINDING_SUCCESS_ACTION)) {
	//
	// if (action.equals(RegisterActivity.BINDING_SUCCESS_ACTION))
	// showTipsDialog("绑定提示",getString(R.string.banding_success_tips));
	// invalidateMenu();
	// userRegistered();
	//
	// }
	// }
	// };

	// public void bindingToSdk() {
	// mKnet.authentication(false, getUser().getPhoneNumber(),KUAISHANGAPPKEY,
	// mAbKnetListener);
	// }

	// public void updateUserTraffic(final Context context) {
	// Callback<String> pQueryTrafficCallback = new Callback<String>() {
	//
	// @Override
	// public void onSuccess(String statue, String message, String result) {
	// try {
	// JSONObject tmpData = new JSONObject(result);
	// long dataSpare = tmpData.optLong("dataSpare");
	// long dataHava = tmpData.optLong("dataHava");
	// int measurementUnit = tmpData.optInt("unitId");
	// if (measurementUnit == 100) {// 100 kb,101秒，102 �?
	// dataSpare = dataSpare / 1024;
	// dataHava = dataHava / 1024;
	// } else if (measurementUnit == 101) {
	// dataSpare = dataSpare / 3600;
	// dataHava = dataHava / 3600;
	// } else if (measurementUnit == 102) {
	//
	// } else {
	// dataSpare = dataSpare / 3600;
	// dataHava = dataHava / 3600;
	// }
	// getUser().setRemainingTime(dataSpare);
	// getUser().setTotalTime(dataHava);
	// mHandler.sendEmptyMessage(MSG_UPDATE_TRAFFIC);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onFailure(String statue, String message, Exception e) {
	// }
	// };
	//
	// ServerHelper.queryUserTraffic(context,
	// getUser().getId(),pQueryTrafficCallback);
	// }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (MyOntouchListener listener : touchListeners) {
			listener.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	private ArrayList<MyOntouchListener> touchListeners = new ArrayList<ActivityHome.MyOntouchListener>();

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
