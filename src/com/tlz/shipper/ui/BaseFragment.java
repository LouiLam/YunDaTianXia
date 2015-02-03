package com.tlz.shipper.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.tlz.utils.Flog;

public class BaseFragment extends Fragment implements OnClickListener {

	protected Activity mActivity;
	protected Intent mIntent;
	
	protected int mScreenWidth = 0;
	protected int mScreenHeight = 0;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mScreenWidth = ((BaseActivity)getActivity()).mScreenWidth;
		mScreenHeight = ((BaseActivity)getActivity()).mScreenHeight;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	public void setViewsClickListener(View... views) {
		for (View v : views) {
			if (v != null) {
				v.setOnClickListener(this);
			} else {
				Flog.e("Some Views are null! cannot setOnClickListener!");
			}
		}
	}
	
	protected Intent getIntent() {
		return mActivity.getIntent();
	}
	
	protected String[] getStringArray(int resId) {
		return mActivity.getResources().getStringArray(resId);
	}

	protected int getColor(int resId) {
		return mActivity.getResources().getColor(resId);
	}
	
	@Override
	public void onClick(View v) {
	}

}
