package cn.luquba678.service;

import internal.org.apache.http.entity.mime.MultipartEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LoadDataFromServer {
	String path;
	MultipartEntity entity;
	boolean isGet = false;

	public LoadDataFromServer(String path, MultipartEntity entity) {
		this.path = path;
		this.entity = entity;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public MultipartEntity getEntity() {
		return entity;
	}

	public void setEntity(MultipartEntity entity) {
		this.entity = entity;
	}

	public boolean isGet() {
		return isGet;
	}

	public void setGet(boolean isGet) {
		this.isGet = isGet;
	}

	public LoadDataFromServer(String path, boolean isGet) {
		this.path = path;
		this.isGet = isGet;
	}

	static String json;

	public static String getJson(final MultipartEntity entity, final String path) {
		getDataString(entity, path);
		return json;

	}

	public static void getDataString(final MultipartEntity entity,
			final String path) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 200) {
					json = msg.obj.toString();
				}
			}
		};

		Thread thread = new Thread() {
			public void run() {
				Message msg = new Message();
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 3000000);
				// 请求超时
				client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 300000);
				StringBuilder builder = new StringBuilder();
				HttpResponse response = null;
				BufferedReader reader = null;
				try {
					if (entity == null) {
						HttpGet get = new HttpGet(path);
						Log.i("get请求", path);
						response = client.execute(get);
					} else {
						HttpPost post = new HttpPost(path);
						post.setEntity(entity);
						response = client.execute(post);
					}
					if (response.getStatusLine().getStatusCode() == 200) {
						reader = new BufferedReader(new InputStreamReader(
								response.getEntity().getContent(),
								Charset.forName("UTF-8")));
						for (String s = reader.readLine(); s != null; s = reader
								.readLine()) {
							builder.append(s);
						}
						String builder_BOM = jsonTokener(builder.toString());
						Log.i("GetJson", builder_BOM);
						msg.what = response.getStatusLine().getStatusCode();
						msg.obj = builder_BOM;
						handler.sendMessage(msg);
					} else {
						msg.what = response.getStatusLine().getStatusCode();
						msg.obj = "网络错误...";
						handler.sendMessage(msg);
						Log.e("GetJson", msg.obj.toString());
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
					msg.what = 211;
					msg.obj = "发送请求异常！";
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = 212;
					msg.obj = "IO异常";
					handler.sendMessage(msg);
				} finally {
					try {
						if (reader != null) {
							reader.close();
							reader = null;
						}
					} catch (IOException e) {
						Log.e("Json", e.getMessage());
					}

				}

			}
		};
		Executors.newSingleThreadExecutor().execute(thread);
	}

	public void getData(final DataCallBack dataCallBack) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (dataCallBack != null) {
					dataCallBack.onDataCallBack(msg.what, msg.obj);
				}
			}
		};

		Thread thread = new Thread() {
			public void run() {
				Message msg = new Message();
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 3000000);
				// 请求超时
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 300000);
				StringBuilder builder = new StringBuilder();
				HttpResponse response = null;
				BufferedReader reader = null;
				try {
					if (isGet) {
						HttpGet get = new HttpGet(path);
						Log.i("get请求", path);
						response = client.execute(get);
					} else {
						HttpPost post = new HttpPost(path);
						post.setEntity(entity);
						response = client.execute(post);
					}
					if (response.getStatusLine().getStatusCode() == 200) {
						reader = new BufferedReader(new InputStreamReader(
								response.getEntity().getContent(),
								Charset.forName("UTF-8")));
						for (String s = reader.readLine(); s != null; s = reader
								.readLine()) {
							builder.append(s);
						}
						String builder_BOM = jsonTokener(builder.toString());
						Log.i("GetJson", builder_BOM);
						msg.what = response.getStatusLine().getStatusCode();
						msg.obj = builder_BOM;
						handler.sendMessage(msg);
					} else {
						msg.what = response.getStatusLine().getStatusCode();
						msg.obj = "网络错误...";
						handler.sendMessage(msg);
						Log.e("GetJson", msg.obj.toString());
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
					msg.what = 211;
					msg.obj = "发送请求异常！";
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = 212;
					msg.obj = "IO异常";
					handler.sendMessage(msg);
				} finally {
					try {
						if (reader != null) {
							reader.close();
							reader = null;
						}
					} catch (IOException e) {
						Log.e("Json", e.getMessage());
					}

				}

			}
		};
		Executors.newSingleThreadExecutor().execute(thread);
	}

	public interface DataCallBack {
		void onDataCallBack(int what, Object data);
	}

	public class Entity extends MultipartEntity {

	}

	private static String jsonTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}
}
