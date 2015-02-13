package com.tlz.shipper.ui.common;

import java.util.Map;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.View.OnClickListener;

import com.tlz.shipper.R;
import com.tlz.shipper.ui.BaseLoadingDialog;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.shipper.ui.widget.EditTextBarPureClearText;

public class ActivityFeedback extends ThemeActivity {

	EditTextBarPureClearText itemBar;
	private BaseLoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_feedback);
		mActionBar.setTitle(R.string.feedback_title);
		itemBar = (EditTextBarPureClearText) findViewById(R.id.register_details_item);
		itemBar.setTBTextTopHint(getString(R.string.feedback_hint_contact));
		itemBar.setTBTextBottom(getString(R.string.feedback_hint_content));
		init();
	}

	private void init() {
		findViewById(R.id.feedback_btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
//	class MyTextWatcher implements TextWatcher {
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before,
//				int count) {
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			if (TextUtils.isEmpty(mInputContent.getText()) || TextUtils.isEmpty(mInputContact.getText())) {
//				mBtnSubmit.setEnabled(false);
//			} else {
//				mBtnSubmit.setEnabled(true);
//			}
//		}
//		
//	}

	private void submitFeedback(String userConnectType, String userComment) {
		mLoadingDialog = BaseLoadingDialog.show(this, "提交中...");
		
		String userId = getUser().getId();
		if (userId == null) {
			userId = "userunlogin";
		}
		
		Map<String, String> tmpMap = new ArrayMap<String, String>();
		tmpMap.put("userId", userId);
		tmpMap.put("connecType", userConnectType);
		tmpMap.put("context", userComment);
		
		
//		AndroidHttp.getInstance().doPost(Urls.FEEDBACK_URL,  tmpMap, new AndroidHttp.SimpleHttpCallback() {
//
//			
////				@Override
////				public void onRequestSuccess(String tag, String jsonString) {
////					String srcString = SecurityUtils.DESedeDecrypt(jsonString);
////					if (JsonUtils.getStatusValue(srcString) == 1) {
////						setMainTips("感谢您的宝贵意见!");
////						mEtFeedback.setText("");
////					} else {
////						setMainTips("网络失败!");
////					}
////				}
//
//
//			@Override
//			public void onRequestFinish(String tag) {
//				super.onRequestFinish(tag);
//				mLoadingDialog.dismiss();
//			}
//
//			@Override
//			public void onRequestSuccess(String tag, String jsonString) {
//				Log.e("ZCL", "FeedbackActivity"+jsonString);
//				ToastUtils.showCrouton(FeedbackActivity.this, getString(R.string.feedback_submit_success));
////				mInputContact.setText("");
////				mInputContent.setText("");
//			}
//
//			@Override
//			public void onRequestFailure(String tag, int errorCode) {
//				ToastUtils.showCrouton(FeedbackActivity.this, getString(R.string.feedback_submit_fail));
//			}
//			
//		});
//		
	}

}
