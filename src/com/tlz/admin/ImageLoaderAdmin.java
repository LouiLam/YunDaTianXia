package com.tlz.admin;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tlz.shipper.R;

public class ImageLoaderAdmin {
	private static ImageLoaderAdmin mInstance;
	DisplayImageOptions options;
	private ImageLoaderAdmin() {
	
	}
	public void init(Context context)
	{
		 options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)// 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.ic_empty)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.ic_error)// 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)// 是否緩存都內存中
			.cacheOnDisk(true)// 是否緩存到sd卡上
			.considerExifParams(true)// 设置imageloader是否会考虑参数 EXIF
										// JPEG图像（旋转，翻转）
			.displayer(new RoundedBitmapDisplayer(20)).build();
	initImageLoader(context);
	}
	public static ImageLoaderAdmin getInstance() {
		if (mInstance == null) {
			mInstance = new ImageLoaderAdmin();
		}
		return mInstance;
	}
	public void displayImage(String url,ImageView view)
	{
		ImageLoader.getInstance().displayImage(url,
				view, options,animateFirstListener);
	}
	public ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	 static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	private void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级
				.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
				.diskCacheSize(50 * 1024 * 1024) // 缓存文件的占用空间的大小 1024=1KB
													// 1024*1024=1000KB
													// 50*1024*1024=50M
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
				// .imageDownloader(new MY(context)) //
				// 自定义支持android.resrouce://com.tlz.shipper/drawable/icon_photograph格式
				// 默认支持格式
				// http://site.com/image.png", "file:///mnt/sdcard/image.png
				// .writeDebugLogs() // Remove for release app // 是否调试写LOG
				.build();
		ImageLoader.getInstance().init(config);
	}
	// class MYBaseImageDownloader extends BaseImageDownloader {
	//
	// public MYBaseImageDownloader(Context context) {
	// super(context);
	// }
	// @Override
	// public InputStream getStream(String imageUri, Object extra)
	// throws IOException {
	// // TODO Auto-generated method stub
	// return super.getStream(imageUri, extra);
	// }
	// }
}
