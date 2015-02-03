package com.tlz.shipper.ui.register_login;

import android.os.Bundle;
import android.widget.ImageView;

import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.utils.QRCodeUtils;

public class QRCodeActivity extends ThemeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_qrcode);
		init();
	}

	private void init() {
		ImageView view = (ImageView) findViewById(R.id.qrcode);
		view.setImageBitmap(QRCodeUtils.generateQRCode(Myself.UserName));
	}

}
