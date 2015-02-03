package com.tlz.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

import android.database.Cursor;
import android.util.Log;

public class IOUtils {
	
	private static final String TAG = IOUtils.class.getSimpleName();
	
	public static void close(Object... objs) {
		for (Object obj : objs) {
			close(obj);
		}
	}
	
	public static void closeOS(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
			os = null;
		}
	}
	
	public static void closeIS(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				Flog.e(TAG, e.getMessage());
			}
			is = null;
		}
	}
	
	public static void closeReader(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
			reader = null;
		}
	}
	
	public static void closeWriter(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
			writer = null;
		}
	}
	
	public static void closeFile(RandomAccessFile file) {
		if (file != null) {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
			file = null;
		}
	}
	
	public static void closeSocket(Socket socket) {
		if (socket != null) {
			if (socket.isConnected()) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, e.getMessage());
				}
			}
			socket = null;
		}
	}
	
	public static void closeServerSocket(ServerSocket socket) {
		if (socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
			socket = null;
		}
	}
	
	public static void closeProcess(Process process) {
		if (process != null) {
			process.destroy();
		}
		process = null;
	}
	
	public static void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		cursor = null;
	}
	
	private static void close(Object obj) {
		if (obj == null) return;
		
		if (obj instanceof InputStream) {
			closeIS((InputStream)obj);
		} else if (obj instanceof OutputStream) {
			closeOS((OutputStream)obj);
		} else if (obj instanceof Writer) {
			closeWriter((Writer)obj);
		} else if (obj instanceof Reader) {
			closeReader((Reader)obj);
		} else if (obj instanceof RandomAccessFile) {
			closeFile((RandomAccessFile)obj);
		} else if (obj instanceof Socket) {
			closeSocket((Socket)obj);
		} else if (obj instanceof ServerSocket) {
			closeServerSocket((ServerSocket)obj);
		} else if (obj instanceof Process) {
			closeProcess((Process)obj);
		} else if (obj instanceof Cursor) {
			closeCursor((Cursor)obj);
		} else {
			Log.e(TAG, "不支持的关闭!");
			throw new RuntimeException("不支持的关闭!");
		}
	}
	
	public static InputStream getPhoneLogs() throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder("logcat", "-d");
		builder.redirectErrorStream(true);
		Process process = builder.start();
		//process.waitFor();
		return process.getInputStream();
	}
	
	public static void copyFile(File src, File dest) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
		   out.write(buf, 0, len);
		}
		in.close();
		out.close();

	}
	
	public static void writeByteArrayToFile(File file, byte[] bytes) throws IOException {
		FileOutputStream fout = new FileOutputStream(file);
		try {
			fout.write(bytes);
		} finally {
			fout.close();
		}
	}
	
	
	private IOUtils(){/*Do not new me*/};
}
