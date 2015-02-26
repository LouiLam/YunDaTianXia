package com.tlz.shipper.ui.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.net.AppConfig;
import com.net.NetAsyncFactory;
import com.net.NetAsyncFactory.ResultCodeSucListener;
import com.net.ShipperAccountApi;
import com.net.Urls;
import com.tlz.admin.ImageLoaderAdmin;
import com.tlz.model.Myself;
import com.tlz.shipper.R;
import com.tlz.shipper.ui.ThemeActivity;
import com.tlz.utils.FileUtils;
import com.tlz.utils.Flog;
import com.tlz.utils.ToastUtils;

public class ActivityImageGridPicker extends ThemeActivity {
	private GridView mGrid;
	private ArrayList<String> urlList;
	private TextView save_btn;
	public static final byte UploadHead = 1;
	public static final byte UploadBusinessLicence = 2;
	public static final byte UploadOrganizationNo = 3;
	public static final byte UploadTaxRegistNo = 4;
	private byte curCategory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar.setTitle(R.string.common_details_image_grid_title);
		setContentView(R.layout.activity_common_details_image_grid);
		curCategory = getIntent().getByteExtra("category", (byte) 0);
		Flog.e("curCategory:"+curCategory);
		initView();

	}

	View lastItemView;

	@Override
	protected void initView() {
		super.initView();
		LayoutParams params = new ActionBar.LayoutParams((int) getResources()
				.getDimension(R.dimen.custom_actionbar_icon_width),
				(int) getResources().getDimension(
						R.dimen.custom_actionbar_icon_height), Gravity.RIGHT);
		params.rightMargin = (int) getResources().getDimension(
				R.dimen.custom_actionbar_icon_margin);
		save_btn = (TextView) LayoutInflater.from(this).inflate(
				R.layout.actionbar_custom_item, null);
		save_btn.setText(getString(R.string.register_save));
		save_btn.setEnabled(false);
		save_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!AppConfig.DEBUG) {
					String path = (String) lastItemView
							.findViewById(R.id.thumb).getTag();
					Flog.e(path);
					NetAsyncFactory.createUploadTask(
							ActivityImageGridPicker.this,
							new ResultCodeSucListener<Object>() {

								@Override
								public void suc(JSONObject obj)
										throws JSONException {
									switch (curCategory) {
									case UploadHead:
										uploadHead(obj.getString("Body"));
										break;
									case UploadBusinessLicence:
										uploadBusinessLicence(obj
												.getString("Body"));
										break;
									case UploadOrganizationNo:
										uploadOrganizationNo(obj
												.getString("Body"));
										break;
									case UploadTaxRegistNo:
										uploadTaxRegistNo(obj
												.getString("Body"));
										break;
									default:
										break;
									}

									ToastUtils.showCrouton(
											ActivityImageGridPicker.this,
											getString(R.string.upload_ok));
								}

								@Override
								public String handler(Object api) {
									return null;
								}
							}).execute(new File(path));
				} else {
					setResult(RESULT_OK);
					finish();
				}
			}
		});
		mActionBar.setCustomView(save_btn, params);
		mActionBar.setDisplayShowCustomEnabled(true);

		mGrid = (GridView) findViewById(R.id.grid);
		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {// 表示拍照
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, 1);
				} else {
					if (lastItemView != null) {
						lastItemView.findViewById(R.id.selected).setVisibility(
								View.GONE);
					}

					save_btn.setEnabled(true);
					view.findViewById(R.id.selected)
							.setVisibility(View.VISIBLE);
					lastItemView = view;
				}

			}
		});
		urlList = new ArrayList<>();
		Resources r = getResources();
		// String image = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
		// + r.getResourcePackageName(R.drawable.icon_photograph) + "/"
		// + r.getResourceTypeName(R.drawable.icon_photograph) + "/"
		// + r.getResourceEntryName(R.drawable.icon_photograph);
		String image = "drawable://" + R.drawable.icon_photograph;

		urlList.add(image);
		new LoadAsyncTask().execute(0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				ToastUtils.show(this, "SD卡不可用");
				return;
			}
			String name = DateFormat.format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))
					+ ".jpg";
			// Toast.makeText(this, name, Toast.LENGTH_LONG).show();
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			FileOutputStream b = null;

			String path = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + "运达天下" + File.separator + name;
			FileUtils.createFile(path);
			// Uri uri = Uri.fromFile(file);
			try {
				b = new FileOutputStream(path);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			data.putExtra("bitmap", bitmap);
			setResult(RESULT_OK, data);
			finish();
			// mGrid.setBackgroundDrawable(new BitmapDrawable(getResources(),
			// bitmap));
			// ((ImageView)
			// findViewById(R.id.imageView)).setImageBitmap(bitmap);//
			// 将图片显示在ImageView里
		}
	}

//	private void skipUI() {
//
//		Intent data = new Intent();
//		ImageView view = (ImageView) lastItemView.findViewById(R.id.thumb);
//		BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
//		Bitmap bitmap = drawable.getBitmap();
//		// Bitmap bitmap = Bitmap.createBitmap(
//		//
//		// drawable.getIntrinsicWidth(),
//		//
//		// drawable.getIntrinsicHeight(),
//		//
//		// drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//		//
//		// : Bitmap.Config.RGB_565);
//		//
//		// Canvas canvas = new Canvas(bitmap);
//		// // canvas.setBitmap(bitmap);
//		// drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
//		// drawable.getIntrinsicHeight());
//		// drawable.draw(canvas);
//		data.putExtra("bitmap", bitmap);
//		setResult(RESULT_OK, data);
//		finish();
//	}

	private void uploadHead(final String url) {
		NetAsyncFactory.createShipperTask(this,
				new ResultCodeSucListener<ShipperAccountApi>() {

					@Override
					public void suc(JSONObject obj) throws JSONException {
						setResult(RESULT_OK);
						finish();

					}

					@Override
					public String handler(ShipperAccountApi api) {
						return api.uploadHead(Myself.ShipperId, url);
					}
				}).execute(Urls.REGEDIT);

	}

	private void uploadBusinessLicence(final String url) {
		NetAsyncFactory.createShipperTask3(this,
				new ResultCodeSucListener<ShipperAccountApi>() {

					@Override
					public void suc(JSONObject obj) throws JSONException {
						Myself.Businesslicence=url;
						setResult(RESULT_OK);
						finish();
					}

					@Override
					public String handler(ShipperAccountApi api) {
						return api.uploadBusinessLicence(Myself.ShipperId, url);
					}
				}).execute(Urls.REGEDIT);
	}

	private void uploadOrganizationNo(final String url) {
		NetAsyncFactory.createShipperTask3(this,
				new ResultCodeSucListener<ShipperAccountApi>() {

					@Override
					public void suc(JSONObject obj) throws JSONException {
						Myself.Organization=url;
						setResult(RESULT_OK);
						finish();
					}

					@Override
					public String handler(ShipperAccountApi api) {
						return api.uploadOrganizationNo(Myself.ShipperId, url);
					}
				}).execute(Urls.REGEDIT);
	}
	private void uploadTaxRegistNo(final String url) {
		NetAsyncFactory.createShipperTask3(this,
				new ResultCodeSucListener<ShipperAccountApi>() {

					@Override
					public void suc(JSONObject obj) throws JSONException {
						Myself.Taxregist=url;
						setResult(RESULT_OK);
						finish();
					}

					@Override
					public String handler(ShipperAccountApi api) {
						return api.uploadTaxRegistNo(Myself.ShipperId, url);
					}
				}).execute(Urls.REGEDIT);
	}
	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	class LoadAsyncTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {

			Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			ContentResolver mContentResolver = ActivityImageGridPicker.this
					.getContentResolver();

			// 只查询jpeg和png的图片
			Cursor mCursor = mContentResolver.query(mImageUri, null,
					MediaStore.Images.Media.MIME_TYPE + "=? or "
							+ MediaStore.Images.Media.MIME_TYPE + "=?",
					new String[] { "image/jpeg", "image/png" },
					MediaStore.Images.Media.DATE_MODIFIED);

			if (mCursor == null) {
				return null;
			}

			while (mCursor.moveToNext()) {
				// 获取图片的路径
				String path = mCursor.getString(mCursor
						.getColumnIndex(MediaStore.Images.Media.DATA));
				urlList.add("file:///" + path);
			}
			mCursor.close();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mGrid.setAdapter(new ImageAdapter());

		}
	}

	public class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		ImageAdapter() {
			inflater = LayoutInflater.from(ActivityImageGridPicker.this);
		}

		@Override
		public int getCount() {
			return urlList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.grid_item, parent, false);
				holder = new ViewHolder();
				holder.imageView = (ImageView) view.findViewById(R.id.thumb);
				holder.imageView.setTag(Uri.parse(urlList.get(position)).getPath());
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			ImageLoaderAdmin.getInstance().displayImage(urlList.get(position),
					holder.imageView);

			return view;
		}
	}

	static class ViewHolder {
		ImageView imageView;
	}
}
