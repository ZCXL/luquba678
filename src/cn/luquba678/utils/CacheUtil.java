package cn.luquba678.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.android.volley.toolbox.JsonObjectRequest;

import android.R.bool;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CacheUtil {
	public static File getCitiesInfo() {
		File file = new File(CacheUtil.getCache(), "cities.log");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return file;
	}

	/**
	 * 写入文件
	 * 
	 * @param file
	 * @param str
	 */
	public static boolean writeText(File file, String str) {
		BufferedWriter bw = null;
		try {
			if (!file.exists())
				file.createNewFile();
			// 字符缓冲流
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(str);
			bw.flush();
			return true;
		} catch (Exception e) {
			Log.e("用户文件写入失败！", e.getMessage());
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;

	}

	/**
	 * 读取文档文件
	 */
	public static String readFile(File file) {
		BufferedReader br = null;
		StringBuilder sb = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String msg = null;
			sb = new StringBuilder();
			while ((msg = br.readLine()) != null) {
				sb.append(msg);
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			Log.e("File", "没有找到文件");
			return null;
		} catch (IOException e) {
			Log.e("IO", "读取文件失败！");
			return null;
		} finally {
			try {
				// 关闭资源
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static File getUserInfo() {
		File file = new File(CacheUtil.getCache(), "user.log");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return file;
	}

	public static String getUserInfoJson() {
		return CacheUtil.readFile(CacheUtil.getCitiesInfo());
	}

	// private LinkedList<ThreadPoolTask> asyncTasks;
	final static public File CACHE = new File(
			Environment.getExternalStorageDirectory(), "cache");

	public static Runnable cacheImgLoad(final ImageView imageView,
			final String path) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// Uri uri = (Uri) msg.obj;
				Bitmap bitmap1 = (Bitmap) msg.obj;
				if (bitmap1 != null && imageView != null) {
					Bitmap bitmap = ImageUtil.zoomBitmap(bitmap1, 210, 210);

					imageView.setImageBitmap(bitmap);
					// imageView.setImageURI(uri);
				}
			}
		};
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					// CacheUtil.getImgBm(path, CACHE);
					// Uri uri = CacheUtil.getImage(path, CACHE);
					handler.sendMessage(handler.obtainMessage(10,
							CacheUtil.getImgBm(path, CACHE)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		return runnable;

		// new Thread(runnable).start();
	}

	/**
	 * 删除文件夹
	 */
	public static void deleteDir(File file) {
		for (File f : file.listFiles()) {
			f.delete();
		}
		file.delete();
	}

	public static Bitmap getImgBm(String path, File cacheDir) {
		try {
			return BitmapFactory.decodeStream(getImageIs(path, cacheDir));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getImageIs(String path, File cacheDir)
			throws Exception {
		File f = new File(cacheDir, MD5.MD5Encode(path)
				+ path.substring(path.lastIndexOf(".")));
		if (f.exists()) {
			return new FileInputStream(f);
		} else {
			HttpURLConnection conn = (HttpURLConnection) new URL(path)
					.openConnection();
			conn.setConnectTimeout(50000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200)
				return conn.getInputStream();
			return null;
		}
	}

	/**
	 * 缓存网络上加载的图片
	 * 
	 * @param path
	 *            - 图片地址
	 * @param cacheDir
	 *            - 缓存目录
	 * @return
	 * @throws Exception
	 */
	public static Uri getImage(String path, File cacheDir) throws Exception {
		File f = new File(cacheDir, MD5.MD5Encode(path)
				+ path.substring(path.lastIndexOf(".")));
		if (f.exists()) {
			return Uri.fromFile(f);
		} else {
			HttpURLConnection conn = (HttpURLConnection) new URL(path)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() == 200) {
				FileOutputStream fos = new FileOutputStream(f);
				InputStream is = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				for (; (len = is.read(buffer)) != -1;) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
				return Uri.fromFile(f);
			}
		}
		return null;
	}

	/**
	 * 创建缓存目录
	 */
	public static File getCache() {
		if (!CACHE.exists())
			CACHE.mkdirs();
		return CACHE;
	}

	public static Bitmap getImgBm(String path) {
		return getImgBm(path, CACHE);
	}

	public static InputStream getImageIs(String path) throws Exception {
		return getImageIs(path, CACHE);
	}

	/**
	 * 图片地址转成 Drawable
	 */
	public static Drawable loadImageFromNetwork(String urladdr) {
		Drawable drawable = null;
		try {
			// judge if has picture locate or not according to filename
			drawable = Drawable.createFromStream(new URL(urladdr).openStream(),
					"asadsdasdfjdgzfgsfmage.jpg");
		} catch (IOException e) {
			Log.d("test", e.getMessage());
		}
		if (drawable == null) {
			Log.d("test", "null drawable");
		} else {
			Log.d("test", "not null drawable");
		}
		return drawable;
	}

	/**
	 * uri获取地址
	 * 
	 * @param fileUrl
	 * @param c
	 * @return
	 */
	public static String getImagePathFromUri(Uri fileUrl, Context c) {
		String fileName = null;
		Uri filePathUri = fileUrl;
		if (fileUrl != null) {
			if (fileUrl.getScheme().toString().compareTo("content") == 0) {
				// content://开头的uri
				Cursor cursor = c.getContentResolver().query(fileUrl, null,
						null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					fileName = cursor.getString(column_index); // 取出文件路径

					// Android 4.1 更改了SD的目录，sdcard映射到/storage/sdcard0
					if (!fileName.startsWith("/storage")
							&& !fileName.startsWith("/mnt")) {
						// 检查是否有”/mnt“前缀
						fileName = "/mnt" + fileName;
					}
					cursor.close();
				}
			} else if (fileUrl.getScheme().compareTo("file") == 0) {
				fileName = filePathUri.toString();// 替换file://
				fileName = filePathUri.toString().replace("file://", "");
				int index = fileName.indexOf("/sdcard");
				fileName = index == -1 ? fileName : fileName.substring(index);

				if (!fileName.startsWith("/mnt")) {
					// 加上"/mnt"头
					fileName += "/mnt";
				}
			}
		}
		return fileName;
	}

}
