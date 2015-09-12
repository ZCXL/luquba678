/**
 *
 */
package cn.luquba678.ui;

import internal.org.apache.http.entity.mime.MultipartEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpUtil {
	// 创建HttpClient对象
	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String BASE_URL = "http://120.26.112.250/api/sendword/add";

	/**
	 *
	 * @param url
	 *            发送请求的URL
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public static String getRequest(final String url) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// 创建HttpGet对象。
						HttpGet get = new HttpGet(url);
						// 发送GET请求
						HttpResponse httpResponse = httpClient.execute(get);
						// 如果服务器成功地返回响应
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							// 获取服务器响应字符串
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							return result;
						}
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}

	/**
	 * @param url
	 *            发送请求的URL
	 * @param rawParams
	 *            请求参数
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public static String postRequest(final String url,
			final Map<String, String> rawParams) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// 创建HttpPost对象。
						HttpPost post = new HttpPost(url);
						// 如果传递参数个数比较多的话可以对传递的参数进行封装
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						JSONObject jsonObject = new JSONObject();
						for (String key : rawParams.keySet()) {
							// 封装请求参数
							// params.add(new BasicNameValuePair(key
							// , rawParams.get(key)));
							jsonObject.put(key, rawParams.get(key));
						}
						StringEntity se = new StringEntity(jsonObject
								.toString(), HTTP.UTF_8);

						// 设置请求参数
						// post.setEntity(new UrlEncodedFormEntity(params,
						// "gbk"));
						post.setEntity(se);

						// 发送POST请求
						HttpResponse httpResponse = httpClient.execute(post);
						// 如果服务器成功地返回响应
						if (httpResponse.getStatusLine().getStatusCode() == 200)

						{
							// 获取服务器响应字符串
							String result = EntityUtils.toString(httpResponse.getEntity());
							return result;
						}
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}

	public static com.alibaba.fastjson.JSONObject getRequestJson(final String url, final MultipartEntity entity) throws Exception {
		return com.alibaba.fastjson.JSONObject.parseObject(HttpUtil.postRequestEntity(url, entity));
	}
	public static void getRequestJsonRunnable(final String url, final MultipartEntity entity,final Handler handler) throws Exception {
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				
				try {
					Message msg = new Message();
					String json = postRequestEntity(url, entity);
					msg.obj=json;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static String postRequestEntity(final String path, final MultipartEntity entity) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// 创建HttpPost对象。
						HttpClient client = new DefaultHttpClient();
						client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 12000);
						// 请求超时
						client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 12000);
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
								reader = new BufferedReader(
										new InputStreamReader(response
												.getEntity().getContent(),
												Charset.forName("UTF-8")));
								for (String s = reader.readLine(); s != null; s = reader
										.readLine()) {
									builder.append(s);
								}
								String builder_BOM = builder.toString();
								Log.i("GetJson", builder_BOM);
								return builder_BOM;
							}
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
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
						return "{\"errorcode\":500}";
					}
				});
		new Thread(task).start();
		return task.get();
	}
}