package com.tlz.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

/**
 * 加密工具类
 * @description 
 * @author Fang Yucun
 * @created 2013年10月31日
 */
public class EncryptUtils {
	
	private static final String TAG = EncryptUtils.class.getSimpleName();
	
	public static String encryptSHA1(String srcString) {
		MessageDigest md = null;
		try {
		    md = MessageDigest.getInstance("sha1");
		    md.update(srcString.getBytes());
		    return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return null;
    }

	public static String encryptMD5(String srcString) {
		
		if (TextUtils.isEmpty(srcString)) return "";
		
		MessageDigest md = null;
		try {
		    md = MessageDigest.getInstance("MD5");
		    md.update(srcString.getBytes("UTF-8"));
		    byte[] bytes = md.digest();
		    int i;
		    StringBuffer buffer = new StringBuffer("");
		    for (int offset = 0; offset < bytes.length; offset++) {
		    	i = bytes[offset];
			    if (i < 0) {
			    	i += 256;
			    }
			    if (i < 16) {
			    	buffer.append("0");
			    }
			    buffer.append(Integer.toHexString(i));
		    }
		    return buffer.toString();
		} catch (Exception e) {
			Flog.e(e.getMessage());
		}
		return "";
	}
	
	public static String encryptBASE64(String srcString) {
		if (!TextUtils.isEmpty(srcString)) {
			try {
	            byte[] encode = srcString.getBytes("UTF-8");
	            return new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	        	Log.e(TAG, e.getMessage());
	        }
        }
        return null;
	}

	public static String decryptBASE64(String destString) {
		if (!TextUtils.isEmpty(destString)) {
			try {
	            byte[] encode = destString.getBytes("UTF-8");
	            return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	        	Log.e(TAG, e.getMessage());
	        }
        }
        return null;
	}
	
	public static  byte[] encryptGZIP(String srcString) {  
        if (!TextUtils.isEmpty(srcString)) {
        	try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzip = new GZIPOutputStream(baos);
                gzip.write(srcString.getBytes("UTF-8"));
                gzip.close();
                byte[] encode = baos.toByteArray();
                baos.flush();
                baos.close();
                return encode;
            } catch (Exception e) {
            	Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }
	
	public static String decryptGZIP(String destString) {  
		if (!TextUtils.isEmpty(destString)) {
			try {
				byte[] decode = destString.getBytes("UTF-8");  
	            ByteArrayInputStream baInStream = new ByteArrayInputStream(decode);  
	            GZIPInputStream gzipInStream = new GZIPInputStream(baInStream);
	            int BUFFER_SIZE = 8 * 1024;
	            byte[] buf = new byte[BUFFER_SIZE];
	            int len = 0;
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            while((len=gzipInStream.read(buf, 0, BUFFER_SIZE)) != -1){
	                 baos.write(buf, 0, len);
	            }
	            gzipInStream.close();
	            baos.flush();
	            decode = baos.toByteArray();
	            baos.close();
	            return new String(decode, "UTF-8");
	        } catch (Exception e) {
	            Log.e(TAG, e.getMessage());
	        }
		}
		return null;
	}
	
	public static String encrypt(String key, Map<String, String> map) {
		String[] keys = new String[map.keySet().size()];
		keys = map.keySet().toArray(keys);
		Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (String s : keys) {
			sb.append(s);
			sb.append(map.get(s));
		}
		sb.append(key);
		return sb.toString();
	}
	
	private EncryptUtils() {/*Do not new me*/};
}
