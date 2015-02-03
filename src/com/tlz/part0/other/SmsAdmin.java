package com.tlz.part0.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsAdmin {
	
	public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	private static SmsAdmin sInstance;
	private Context mContext;
	private OnSmsListener mOnSmsListener;
	
	private String mLongNumber;
	private String mKeyword;
	
	private SmsReceivedReceiver mSmsRecevedReceiver;
	
	private SmsAdmin(Context context) {
		mContext = context;
	}
	
	public static SmsAdmin getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SmsAdmin(context);
		}
		return sInstance;
	}
	
	public void startMonitor(OnSmsListener listener) {
		startMonitor("", "", listener);
	}
	
	/**
	 * 开始监听
	 * @param longNumber
	 * @param listener
	 */
	public void startMonitor(String longNumber, String keyword, OnSmsListener listener) {
		mLongNumber = longNumber == null ? "" : longNumber;
		mKeyword = keyword == null ? "" : keyword;
		mOnSmsListener = listener;
		
		if (mSmsRecevedReceiver == null) {
			mSmsRecevedReceiver = new SmsReceivedReceiver();
		}
		
		IntentFilter filter = new IntentFilter(ACTION_SMS_RECEIVED);
		filter.setPriority(Integer.MAX_VALUE);
		mContext.registerReceiver(mSmsRecevedReceiver, filter);
	}
	
	public void stopMonitor() {
		try {
			mContext.unregisterReceiver(mSmsRecevedReceiver);
		} catch (Exception e) {
		}
	}
	
	class SmsReceivedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_SMS_RECEIVED.equals(intent.getAction())) {
				Bundle bundle = intent.getExtras();
				if (bundle == null) {
					return;
				}
				
				Object[] pdus = (Object[])bundle.get("pdus");
				SmsMessage[] smsMessage = new SmsMessage[pdus.length];
				for (int i=0; i<smsMessage.length; i++) {
					byte[] pdu = (byte[])pdus[i];
					smsMessage[i] = SmsMessage.createFromPdu(pdu);
				}
				
				for (SmsMessage msg : smsMessage) {
					String srcAddress = msg.getOriginatingAddress();
					String message = msg.getMessageBody();
					if (srcAddress.startsWith(mLongNumber == null ? "" : mLongNumber) && message.contains(mKeyword)) {
						if (mOnSmsListener != null) {
							mOnSmsListener.onSmsReceived(msg.getMessageBody());
						}
						break;
					}
				}
			}
		}

	}
	
	public interface OnSmsListener {
		public void onSmsReceived(String text);
	}
}
