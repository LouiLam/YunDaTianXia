package com.tlz.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


public class HttpUtils {

	private static final String TAG = HttpUtils.class.getSimpleName();
	private static final boolean DEBUG = true;

	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HEADER_CHARSET = "Charset";
	private static final String HEADER_CONNECTION = "connection";

	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";

	private static final int CONNECT_TIME_OUT = 30 * 1000;
	private static final int READ_TIME_OUT = 30 * 1000;

	private static String BOUNDARY = UUID.randomUUID().toString();

	public static String doGet(String urlString) {
		return doRequest(Method.GET, urlString, null);
	}

	public static String doPost(String urlString, Map<String, String> params) {
		return doRequest(Method.POST, urlString, params);
	}
	public static String doPostString(String urlString, String json) {
		return doRequestString(Method.POST, urlString, json);
	}
	
	public static String doRequest(int method, String urlString,
			Map<String, String> params) {
		HttpURLConnection connection = null;
		try {
			connection = openConnection(new URL(urlString));
			setConnectionParametersForRequest(method, connection, params);
			int responseCode = connection.getResponseCode();
			if (responseCode == -1) {
				throw new IOException(
						"Could not retrieve response code from HttpURLConnection.");
			}
			return readStream(connection.getInputStream());
		} catch (MalformedURLException e) {
			if (DEBUG)
				Flog.e(TAG, "MalformedURLException", e);
		} catch (IOException e) {
			if (DEBUG)
				Flog.e(TAG, "IOException", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
	public static String doRequestString(int method, String urlString,
			String json) {
		HttpURLConnection connection = null;
		try {
			connection = openConnection(new URL(urlString));
			setConnectionPStringForRequest(method, connection, json);
			int responseCode = connection.getResponseCode();
			if (responseCode == -1) {
				throw new IOException(
						"Could not retrieve response code from HttpURLConnection.");
			}
			return readStream(connection.getInputStream());
		} catch (MalformedURLException e) {
			if (DEBUG)
				Flog.e(TAG, "MalformedURLException", e);
		} catch (IOException e) {
			if (DEBUG)
				Flog.e(TAG, "IOException", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	private static HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIME_OUT);
		return new DefaultHttpClient(params);
	}

	/**
	 * 上传文件
	 * 
	 * @param urlString
	 * @param params
	 * @param file
	 * @return
	 */
	public static String uploadFile(String urlString,
			Map<String, String> params, File file) {

		if (file == null || !file.exists()) {
			return null;
		}

		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(urlString);

		MultipartEntity entity = new MultipartEntity();

		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				try {
					entity.addPart(entry.getKey(),
							new StringBody(entry.getValue()));
				} catch (UnsupportedEncodingException e) {
					if (DEBUG)
						Flog.e(TAG, e);
				}
			}
		}

		entity.addPart("file", new FileBody(file));
		httpPost.setEntity(entity);

		try {
			HttpResponse response = httpClient.execute(httpPost);
			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			if (DEBUG)
				Flog.e(TAG, e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		httpPost.abort();
		return null;
	}

	public static String uploadFile2(String urlString,
			Map<String, String> stringParams, Map<String, File> fileParams) {
		HttpURLConnection connection = null;
		InputStream is = null;

		try {
			connection = openConnection(new URL(urlString));
			connection.setDoOutput(true);

			DataOutputStream dos = new DataOutputStream(
					connection.getOutputStream());

			addStringParams(dos, stringParams);
			addFileParams(dos, fileParams);
			addEndParams(dos);

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return readStream(connection.getInputStream());
			}
		} catch (Exception e) {
			if (DEBUG)
				Flog.e(TAG, e);
		} finally {
			IOUtils.close(is);
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	public static String readStream(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			IOUtils.close(br);
		}
		return sb.toString();
	}

	private static void setConnectionParametersForRequest(int method,
			HttpURLConnection connection, Map<String, String> paramsMap)
			throws IOException {
		switch (method) {
		case Method.GET:
			connection.setRequestMethod("GET");
			break;
		case Method.POST:
			connection.setRequestMethod("POST");
			addBodyIfExists(connection, paramsMap);
			break;
		default:
			throw new IllegalStateException("Unknown method type.");
		}
	}
	private static void setConnectionPStringForRequest(int method,
			HttpURLConnection connection, String json)
			throws IOException {
		switch (method) {
		case Method.GET:
			connection.setRequestMethod("GET");
			break;
		case Method.POST:
			connection.setRequestMethod("POST");
			addBodyIfExists(connection, json);
			break;
		default:
			throw new IllegalStateException("Unknown method type.");
		}
	}
	/**
	 * 配置基本的HttpURLConnection
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection openConnection(URL url) throws IOException {
		HttpURLConnection connection = createConnection(url);
		connection.setConnectTimeout(CONNECT_TIME_OUT);
		connection.setReadTimeout(READ_TIME_OUT);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setRequestProperty(HEADER_CHARSET,
				EncodeUtils.getDefultCharset());
		connection.setRequestProperty(HEADER_CONNECTION, "keep-alive");
		return connection;
	}

	/**
	 * 创建HttpURLConnection
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection createConnection(URL url)
			throws IOException {
		return (HttpURLConnection) url.openConnection();
	}

	private static void addBodyIfExists(HttpURLConnection connection,
			Map<String, String> params) throws IOException {

		if (params == null || params.size() == 0)
			return;

		byte[] body = encodeParameters(params);

		if (body != null) {
			connection.setDoOutput(true);
			connection.addRequestProperty(HEADER_CONTENT_TYPE,
					getBodyContentType());
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			out.write(body);
			out.close();
		}
	}
	private static void addBodyIfExists(HttpURLConnection connection,
			String json) throws IOException {

		if (json == null || json.length() == 0)
			return;

		byte[] body =json.getBytes(EncodeUtils.getDefultCharset());;

		if (body != null) {
			connection.setDoOutput(true);
			connection.addRequestProperty(HEADER_CONTENT_TYPE,
					getJsonBodyContentType());
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			out.write(body);
			out.close();
		}
	}
	private static String getBodyContentType() {
		return "application/x-www-form-urlencoded; charset="
				+ EncodeUtils.getDefultCharset();
	}
	private static String getJsonBodyContentType() {
		return "application/json; charset="
				+ EncodeUtils.getDefultCharset();
	}
	/**
	 * 添加字符
	 * 
	 * @param dos
	 * @param stringParamsMap
	 * @throws IOException
	 */
	private static void addStringParams(DataOutputStream dos,
			Map<String, String> stringParamsMap) throws IOException {
		for (Map.Entry<String, String> entry : stringParamsMap.entrySet()) {
			dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
			dos.writeBytes("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINE_END);
			dos.writeBytes(LINE_END);
			dos.writeBytes(EncodeUtils.encode(entry.getValue()) + "\r\n");
		}
	}

	/**
	 * 添加文件
	 * 
	 * @param dos
	 * @param fileParamsMap
	 * @throws Exception
	 */
	private static void addFileParams(DataOutputStream dos,
			Map<String, File> fileParamsMap) throws Exception {
		for (Map.Entry<String, File> entry : fileParamsMap.entrySet()) {

			if (entry.getValue() == null || !entry.getValue().exists())
				continue;

			dos.writeBytes(PREFIX + BOUNDARY + LINE_END);
			dos.writeBytes("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"; filename=\""
					+ EncodeUtils.encode(entry.getValue().getName()) + "\""
					+ LINE_END);
			dos.writeBytes("Content-Type: application/octet-stream" + LINE_END);
			dos.writeBytes(LINE_END);
			dos.write(FileUtils.getBytes(entry.getValue()));
			dos.writeBytes(LINE_END);

		}
	}

	/**
	 * 添加Http尾部
	 * 
	 * @param dos
	 * @throws IOException
	 */
	private static void addEndParams(DataOutputStream dos) throws IOException {
		dos.writeBytes(PREFIX + BOUNDARY + PREFIX + LINE_END);
		dos.writeBytes(LINE_END);
	}

	private static byte[] encodeParameters(Map<String, String> params) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			int size = params.entrySet().size();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				size--;
				encodedParams.append(EncodeUtils.encode(entry.getKey()));
				encodedParams.append('=');
				encodedParams
						.append(EncodeUtils.encode(entry.getValue() == null ? ""
								: entry.getValue()));

				if (size != 0) {
					encodedParams.append('&');
				}
			}
			return encodedParams.toString().getBytes(
					EncodeUtils.getDefultCharset());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding not supported: "
					+ EncodeUtils.getDefultCharset(), e);
		}
	}

	public static interface Method {
		public static int GET = 1;
		public static int POST = 2;
	}

	private HttpUtils() {
	};

}
