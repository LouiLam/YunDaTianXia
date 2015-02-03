package com.tlz.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeUtils {
	   
	   
	   public static  Bitmap generateQRCode(String content) {  
	        try {  
	            QRCodeWriter writer = new QRCodeWriter();  
	            // MultiFormatWriter writer = new MultiFormatWriter();  
	            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 500, 500);  
	            return bitMatrix2Bitmap(matrix);  
	        } catch (WriterException e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
	   private static Bitmap bitMatrix2Bitmap(BitMatrix matrix) {  
	        int w = matrix.getWidth();  
	        int h = matrix.getHeight();  
	        int[] rawData = new int[w * h];  
	        for (int i = 0; i < w; i++) {  
	            for (int j = 0; j < h; j++) {  
	                int color = Color.WHITE;  
	                if (matrix.get(i, j)) {  
	                    color = Color.BLACK;  
	                }  
	                rawData[i + (j * w)] = color;  
	            }  
	        }  
	  
	        Bitmap bitmap = Bitmap.createBitmap(w, h, Config.RGB_565);  
	        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);  
	        return bitmap;  
	    }  
//	   public static void writeToFile(BitMatrix matrix, String format, File file)  
//	       throws IOException {  
//	     BufferedImage image = toBufferedImage(matrix);  
//	     if (!ImageIO.write(image, format, file)) {  
//	       throw new IOException("Could not write an image of format " + format + " to " + file);  
//	     }  
//	   }  
//	   
//	     
//	   public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)  
//	       throws IOException {  
//	     BufferedImage image = toBufferedImage(matrix);  
//	     if (!ImageIO.write(image, format, stream)) {  
//	       throw new IOException("Could not write an image of format " + format);  
//	     }  
//	   }  
}
