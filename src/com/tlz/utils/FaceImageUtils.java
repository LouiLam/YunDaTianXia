package com.tlz.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;

import com.tlz.shipper.R;

public class FaceImageUtils {
	private  static String FACE_IMAGE_FILEPATH = Environment
			.getExternalStorageDirectory() + "/kuaishang/";

	public static void getImageToView(ImageView faceImage, Resources rs,String sUserName) {
		Bitmap photo = BitmapFactory.decodeFile(FACE_IMAGE_FILEPATH+sUserName);
		if (photo == null) {
			faceImage.setImageResource(R.drawable.ic_user_face_default);
			return;
		}
		Drawable drawable = new BitmapDrawable(rs, photo);
		faceImage.setImageDrawable(drawable);

	}
	public static boolean faceExists(String sUserName){
		File f = new File(FACE_IMAGE_FILEPATH+sUserName);
		return f.exists();
	}
	public static void getImageToView(Bitmap photo, ImageView faceImage,
			Resources rs,String sUserName) {
		if (photo == null)
			return;
		saveMyBitmap(photo,sUserName);
		Drawable drawable = new BitmapDrawable(rs, photo);
		faceImage.setImageDrawable(drawable);
	}

	/**
	 * 保存裁剪之后的图片数据
	 */
	private static void saveMyBitmap(Bitmap mBitmap,String sUserName) {
		File f = new File(FACE_IMAGE_FILEPATH+sUserName);
		try {
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public interface FaceImageChangeListener{
		public void lightFace();
		public void greyFace();
	}
}
