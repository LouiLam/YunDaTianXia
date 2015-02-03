package com.tlz.shipper.ui;

import com.tlz.model.User;


public class ThemeFragment extends BaseFragment {

	protected User getUser() {
		if (mActivity == null) return new User();
		return ((ThemeActivity)mActivity).getUser();
	}
	
	protected void setUser(User user) {
		((ThemeActivity)mActivity).setUser(user);
	}

}
