package com.tlz.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.support.v4.util.ArrayMap;


public class FtpUtils {
	
	private static final boolean DEBUG = false;
	
	public static final String USERNAME = "kslogs";
	public static final String PASSWORD = "kkg6@123";
	public static final String URL = "121.199.63.147";
	public static final String REMOTE_PATH = "android/logs/";
	public static final int PORT = 21;
	
	public static boolean upload(String remoteDir, String uploadFilePath) {
		return upload(remoteDir, new File(uploadFilePath));
	}
	
	public static boolean upload(String remoteDir, File uploadFile) {
		return upload(remoteDir, Arrays.asList(new File[]{uploadFile})).get(uploadFile.getPath());
	}
	
//	public static boolean upload(String remotePath, List<String> uploadFilePathList) {
//		return upload(URL, PORT, USERNAME, PASSWORD, REMOTE_PATH + remotePath, uploadFilePathList);
//	}
	
	public static Map<String, Boolean> upload(String remoteDir, List<File> uploadFiles) {
		return upload(URL, PORT, USERNAME, PASSWORD, REMOTE_PATH + remoteDir, uploadFiles);
	}
	
	public static Map<String, Boolean> upload(String url, int port, String username, String password, String remotePath, List<File> uploadFiles) { 
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		try {
			ftpClient.connect(url, port);
			boolean loginResult = ftpClient.login(username, password);
			int returnCode = ftpClient.getReplyCode();
			if (returnCode == FTPReply.NOT_LOGGED_IN) {
				Flog.e("UserName Or Password Is Wrong!");
				return null;
			}
			
			if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {
				ftpClient.makeDirectory(remotePath);
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.enterLocalPassiveMode();
				return storeFiles(ftpClient, fis, uploadFiles);
			} else {
				return null;
			}

		} catch (Exception e) {
//			Flog.e(e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				Flog.e(e);
			}
		}
		return null;
	}
	
	private static Map<String, Boolean> storeFiles(FTPClient ftpClient, FileInputStream fis, List<File> files) {
		if (files == null || files.size() == 0) return null;
		
		Map<String, Boolean> tempMap = new ArrayMap<String, Boolean>();
		for (File file : files) {
			try {
				fis = new FileInputStream(file);
				if (ftpClient.storeFile(StringUtils.getFileNameFromPath(file.getPath()), fis)) {
					tempMap.put(file.getPath(), true);
				} else {
					tempMap.put(file.getPath(), false);
				}
			} catch (IOException e) {
				if (DEBUG) Flog.e(e);
			}
		}
		return tempMap;
	}
}
