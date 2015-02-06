package com.tlz.utils;

import java.io.File;
import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;


public class StorageUtils {

	/**
	 * 判断SD卡是否挂起
	 * @return
	 */
	public static boolean isExternalStorageReady() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取存储卡根目录
	 * @return
	 */
	public static File getExternalStorageRootDir()  {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory();
		}
		return null;
	}
	
	public static File getExternalStorageDCIMDir() throws RuntimeException {
		if (!isExternalStorageReady()) {
			throw new RuntimeException("外部存储器未知!");
		}
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	}
	
	/**
	 * 获取根目录
	 * @return
	 */
	public static File getRootDir() {
		return Environment.getRootDirectory();
	}
	

	/**
	 * 获取存储卡可用空间
	 * @return
	 */
    @SuppressWarnings("deprecation")
	@TargetApi(18)
	public static long getExternalStorageAvailableSpace() {
        try {
            StatFs stat = new StatFs(getExternalStorageRootDir().getAbsolutePath());
            long avaliableSize = 0;
            if (Build.VERSION.SDK_INT >= 18) {
            	avaliableSize = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
            } else {
            	avaliableSize = ((long)stat.getAvailableBlocks() * (long)stat.getBlockSize());
            }
            return avaliableSize;
        } catch (Exception e) {
        	Flog.e(e);
        }
        return 0;
    }
    
    @SuppressWarnings("deprecation")
	@TargetApi(18)
	public static long getExternalStorageTotalSize(Context context) {
		File dir = Environment.getExternalStorageDirectory();
		StatFs statFs = new StatFs(dir.getPath());
		long blockSize = 0;
		long blockCount = 0;
		if (Build.VERSION.SDK_INT < 18) {
			blockSize = statFs.getBlockSize();
			blockCount = statFs.getBlockCount();
		} else {
			blockSize = statFs.getBlockSizeLong();
			blockCount = statFs.getBlockCountLong();
		}
		return blockSize * blockCount;
	}
    
    /**
     * 创建文件夹
     * @param path
     * @return
     */
    public static boolean createDir(String path) {
    	File file = new File(path);
    	if (!file.exists()) {
    		return file.mkdirs();
    	} else if (file.isFile()){
    		return false;
    	} else {
    		return true;
    	}
    }
    
    /**
     * 创建文件
     * @param path
     * @return
     */
    public static boolean createFile(String path) {
    	File file = new File(path);
    	
    	if (file.exists() && file.isFile()) {
    		return true;
    	} else if (file.exists() && file.isDirectory()) {
    		return false;
    	}
    	
		createDir(file.getParent());
		try {
			return file.createNewFile();
		} catch (IOException e) {
			Flog.e(e);
		}
		return false;
    	
    }
    
    /**
     * 文件删除
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
    	File file = new File(path);
    	if (!file.exists()) return true;
    	if (file.isFile()) {
    		return file.delete();
    	} else {
    		File[] files = file.listFiles();
    		for (File f : files) {
    			deleteFile(f.getPath());
    		}
    		return file.delete();
    	}
    	
    }
    
    
    private StorageUtils(){/*Do not new me*/};
}
