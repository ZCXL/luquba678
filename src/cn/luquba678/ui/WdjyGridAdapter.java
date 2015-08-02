package cn.luquba678.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.luquba678.R;
import cn.luquba678.entity.GridItem;
import cn.luquba678.ui.DiskLruCache.Snapshot;
import cn.luquba678.utils.FileCache;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapterWrapper;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class WdjyGridAdapter extends BaseAdapter implements
		StickyGridHeadersSimpleAdapter {

	private List<GridItem> list;
	private LayoutInflater mInflater;
	private StickyGridHeadersGridView mGridView;
	private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象

	StickyGridHeadersSimpleAdapterWrapper mWrapper;
	/**
	 * 记录所有正在下载或等待下载的任务。
	 */
	private Set<BitmapWorkerTask> taskCollection;

	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private LruCache<String, Bitmap> mMemoryCache;
	FileCache fileCache;
	/**
	 * 图片硬盘缓存核心类。
	 */
	private DiskLruCache mDiskLruCache;

	private Context mContext;

	public WdjyGridAdapter(Context context, List<GridItem> list,
			StickyGridHeadersGridView mGridView) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
		this.mGridView = mGridView;
		mContext = context;

		taskCollection = new HashSet<BitmapWorkerTask>();
		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		// 设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};

		try {
			// 获取图片缓存路径
			File cacheDir = getDiskCacheDir(context, "thumb");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			// 创建DiskLruCache实例，初始化缓存数据
			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context),
					1, 10 * 1024 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}

		mWrapper = new StickyGridHeadersSimpleAdapterWrapper(this);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.wdjy_grid_item, parent,
					false);
			mViewHolder.mImageView = (WdjyImageView) convertView
					.findViewById(R.id.grid_item);
			convertView.setTag(mViewHolder);

			// 用来监听ImageView的宽和高
			mViewHolder.mImageView
					.setOnMeasureListener(new WdjyImageView.OnMeasureListener() {
						@Override
						public void onMeasureSize(int width, int height) {
							mPoint.set(width, height);
						}
					});

		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		String path = list.get(position).getPath();
		System.out.println("-----调用------->      " + path);
		mViewHolder.mImageView.setTag(path);
		mViewHolder.mImageView.setImageResource(R.drawable.empty_photo);
		loadBitmaps(mViewHolder.mImageView, path);

		return convertView;
	}

	public static class ViewHolder {
		public WdjyImageView mImageView;
	}

	public static class HeaderViewHolder {
		public TextView mTextView;
	}

	public static Date strToDateTime(String arg0) {
		Date resultDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			resultDate = simpleDateFormat.parse(arg0);
			return resultDate;
		} catch (Exception e) {
			return resultDate;
		}
	}

	public static String dateTimeToStr(Date arg0) {
		String resultStr;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M月");
		resultStr = simpleDateFormat.format(arg0);
		return resultStr;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HeaderViewHolder mHeaderHolder;
		if (convertView == null) {
			mHeaderHolder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.wdjy_grid_header, parent,
					false);
			mHeaderHolder.mTextView = (TextView) convertView
					.findViewById(R.id.header);
			convertView.setTag(mHeaderHolder);
		} else {
			mHeaderHolder = (HeaderViewHolder) convertView.getTag();
		}

		String strDate = dateTimeToStr(strToDateTime(list.get(position)
				.getTime()));
		int nCount = mWrapper.getCountForHeader((int) getHeaderId(position));
		String strHeader = String.format("%s (%d)", strDate, nCount);

		mHeaderHolder.mTextView.setText(strHeader);

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return list.get(position).getSection();
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	// 压缩且保存图片到SDCard
	private void compressAndSaveBitmap(Bitmap rawBitmap, String fileName,
			int quality) {
		File appDir = getDiskCacheDir(mContext, "images");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String strSaveFileName = appDir.getPath() + File.separator + fileName
				+ ".jpg";
		File saveFile = new File(strSaveFileName);
		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(
						saveFile);
				if (fileOutputStream != null) {
					rawBitmap.compress(Bitmap.CompressFormat.JPEG, quality,
							fileOutputStream);
				}
				fileOutputStream.flush();
				fileOutputStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}

	public void loadBitmaps(ImageView imageView, String imageUrl) {
		try {
			Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
			System.out.println("bitmap-------> " + bitmap);
			if (bitmap == null) {
				BitmapWorkerTask task = new BitmapWorkerTask();
				taskCollection.add(task);
				task.execute(imageUrl);
			} else {
				if (imageView != null && bitmap != null) {

					imageView.setImageBitmap(bitmap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelAllTasks() {
		if (taskCollection != null) {
			for (BitmapWorkerTask task : taskCollection) {
				task.cancel(false);
			}
		}
	}

	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * 获取当前应用程序的版本号。
	 */
	public int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * 使用MD5算法对传入的key进行加密并返回。
	 */
	public String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	/**
	 * 将缓存记录同步到journal文件中。
	 */
	public void fluchCache() {
		if (mDiskLruCache != null) {
			try {
				mDiskLruCache.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 异步下载图片的任务。
	 * 
	 * @author guolin
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * 图片的URL地址
		 */
		private String imageUrl;

		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			FileDescriptor fileDescriptor = null;
			FileInputStream fileInputStream = null;
			Snapshot snapShot = null;
			try {
				// 生成图片URL对应的key
				final String key = hashKeyForDisk(imageUrl);
				// 查找key对应的缓存
				snapShot = mDiskLruCache.get(key);

				if (snapShot == null) {
					// 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
					DiskLruCache.Editor editor = mDiskLruCache.edit(key);
					if (editor != null) {
						OutputStream outputStream = editor.newOutputStream(0);
						if (downloadUrlToStream(imageUrl, outputStream)) {
							editor.commit();
						} else {
							editor.abort();
						}
					}
					// 缓存被写入后，再次查找key对应的缓存
					snapShot = mDiskLruCache.get(key);
					System.out.println("----------snapot _     " + snapShot);
				}
				if (snapShot != null) {
					fileInputStream = (FileInputStream) snapShot
							.getInputStream(0);
					fileDescriptor = fileInputStream.getFD();
				}
				// 将缓存数据解析成Bitmap对象
				Bitmap bitmap = null;
				if (fileDescriptor != null) {
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}
				System.out.println("----doInBackground-下载中的图片---------"
						+ bitmap);
				if (bitmap != null) {
					// 将Bitmap对象添加到内存缓存当中
					addBitmapToMemoryCache(params[0], bitmap);
					compressAndSaveBitmap(bitmap, key, 90);
				}
				return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileDescriptor == null && fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			// 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
			ImageView imageView = (ImageView) mGridView
					.findViewWithTag(imageUrl);
			System.out.println("----onPostExecute-下载好的图片---------" + bitmap);
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
			taskCollection.remove(this);
		}

		/**
		 * 建立HTTP请求，并获取Bitmap对象。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 解析后的Bitmap对象
		 */
		private boolean downloadUrlToStream(String urlString,
				OutputStream outputStream) {
			HttpURLConnection urlConnection = null;
			BufferedOutputStream out = null;
			BufferedInputStream in = null;
			try {
				final URL url = new URL(urlString);
				urlConnection = (HttpURLConnection) url.openConnection();
				in = new BufferedInputStream(urlConnection.getInputStream(),
						8 * 1024);
				out = new BufferedOutputStream(outputStream, 8 * 1024);
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				return true;
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

	}

}