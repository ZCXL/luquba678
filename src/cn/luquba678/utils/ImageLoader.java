package cn.luquba678.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	static OnBackOutStream back;
	public static MemoryCache memoryCache = new MemoryCache();
	private static FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	private ExecutorService executorService;

	public ImageLoader(Context context) {
		fileCache = new FileCache();
		executorService = Executors.newFixedThreadPool(5);
	}

	/**
	 * 加载图片
	 */
	public void DisplayImage(String url, ImageView imageView,
			boolean isLoadOnlyFromCache) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else if (!isLoadOnlyFromCache) {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
		}
	}

	/**
	 * 按指定大小加载图片
	 */
	public void DisplayImage(String url, ImageView imageView,
			boolean isLoadOnlyFromCache, int width, int height) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(ImageUtil
					.zoomBitmap(bitmap, width, height));
		else if (!isLoadOnlyFromCache) {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView, width, height);
		}
	}

	public void DisplayImage(String url, ImageView imageView,
			boolean isLoadOnlyFromCache, int width) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(ImageUtil.centerSquareScaleBitmap(bitmap,
					width));
		else if (!isLoadOnlyFromCache) {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView, width);

		}
	}

	/**
	 * 将缓存图片伸缩到指定大小
	 */
	private void queuePhoto(String url, ImageView imageView, int width,
			int height) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, width, height);
		executorService.submit(new PhotosLoader(p));
	}

	private void queuePhoto(String url, ImageView imageView, int width) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, width);
		executorService.submit(new PhotosLoader(p));
	}

	/**
	 * 加载图片
	 */
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	/**
	 * 传入url获取bitmap
	 */
	public static Bitmap getBitmap(String url) {
		File f = FileCache.getFile(url);
		// 先从文件缓存中查找是否有
		Bitmap b = null;
		if (f != null && f.exists()) {
			b = decodeFile(f);
		}
		if (b != null) {
			return b;
		}

		// 最后从指定的url中下载图片
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os, url);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			Log.e("",
					"getBitmap catch Exception...\nmessage = "
							+ ex.getMessage());
			return null;
		}
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private static Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public int height;
		public int width;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}

		public PhotoToLoad(String url, ImageView imageView, int width,
				int height) {
			this.url = url;
			this.imageView = imageView;
			this.width = width;
			this.height = height;
		}

		public PhotoToLoad(String url, ImageView imageView, int width) {
			super();
			this.url = url;
			this.imageView = imageView;
			this.width = width;
		}

	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			if (photoToLoad.width > 0 && photoToLoad.height > 0)
				bmp = ImageUtil.zoomBitmap(bmp, photoToLoad.width,
						photoToLoad.height);
			else if (photoToLoad.width > 0 && photoToLoad.height <= 0)
				bmp = ImageUtil.centerSquareScaleBitmap(bmp, photoToLoad.width);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * 防止图片错位
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);

		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os, String path) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			Log.e("", "CopyStream catch Exception...");
		}
		back.OnBackStream(is, path);
	}

	public interface OnBackOutStream {
		void OnBackStream(InputStream os, String path);

	}

	public void setOnBackStream(OnBackOutStream back) {
		ImageLoader.back = back;
	}
}