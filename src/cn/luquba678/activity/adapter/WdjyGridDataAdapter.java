package cn.luquba678.activity.adapter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.GridItem;
import cn.luquba678.ui.DiskLruCache;
import cn.luquba678.ui.WdjyGridAdapter.HeaderViewHolder;
import cn.luquba678.ui.WdjyImageView;
import cn.luquba678.ui.WdjyImageView.OnMeasureListener;
import cn.luquba678.utils.FileCache;
import cn.luquba678.utils.ImageLoader;
import cn.luquba678.utils.ImageLoader.OnBackOutStream;
import cn.luquba678.utils.MD5;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapterWrapper;

public class WdjyGridDataAdapter extends CommonAdapter<GridItem> implements
		StickyGridHeadersSimpleAdapter {
	private LayoutInflater Inflater;
	private StickyGridHeadersGridView mGridView;
	private Point mPoint = new Point(0, 0);
	private StickyGridHeadersSimpleAdapterWrapper mWrap;
	FileCache fileCache;
	ImageLoader loader;

	public WdjyGridDataAdapter(Context context, List<GridItem> list,
			StickyGridHeadersGridView mGridView) {
		super(context, list, R.layout.wdjy_grid_item);
		this.context = context;
		this.dates = list;
		this.mGridView = mGridView;
		initAdapter();
		loader = new cn.luquba678.utils.ImageLoader(context);
		loader.setOnBackStream(new OnBackOutStream() {
			@Override
			public void OnBackStream(InputStream is, String path) {
				FileInputStream fileInputStream = (FileInputStream) is;

				try {
					FileDescriptor fileDescriptor = fileInputStream.getFD();
					if (fileDescriptor != null) {
						Bitmap bitmap = BitmapFactory
								.decodeFileDescriptor(fileDescriptor);
						String key = MD5.MD5Encode(path);
						copmassIPEG(bitmap, key, 75);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}

	private void copmassIPEG(Bitmap bitmap, String key, int i) {
		File caFile = fileCache.getFile("images");
		if (caFile.exists()) {
			caFile.mkdirs();
		}
		String fileName = caFile.getPath() + File.separator + key + ".jpg";
		File saveFile = new File(fileName);
		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
				FileOutputStream outputStream = new FileOutputStream(saveFile);
				if (outputStream != null) {
					bitmap.compress(CompressFormat.JPEG, i, outputStream);
				}
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 初始化Adapter
	 */
	private void initAdapter() {
		Inflater = LayoutInflater.from(context);
		mWrap = new StickyGridHeadersSimpleAdapterWrapper(this);
	}

	@Override
	public long getHeaderId(int position) {
		return dates.get(position).getSection();
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder mHeaderHolder;
		if (convertView == null) {
			mHeaderHolder = new HeaderViewHolder();
			convertView = Inflater.inflate(R.layout.wdjy_grid_header, parent,
					false);
			mHeaderHolder.mTextView = (TextView) convertView
					.findViewById(R.id.header);
			convertView.setTag(mHeaderHolder);
		} else {
			mHeaderHolder = (HeaderViewHolder) convertView.getTag();
		}

		/*
		 * String strDate = dateTimeToStr(strToDateTime(dates.get(position)
		 * .getTime()));
		 */
		int nCount = mWrap.getCountForHeader((int) getHeaderId(position));
		try {
			String strHeader = String.format("%s (%d)", dates.get(position)
					.getTime(), nCount);
			mHeaderHolder.mTextView.setText(strHeader);
		} catch (Exception e) {
		}
		return convertView;
	}

	class ViewHodler {
		public WdjyImageView imageView;
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

	@Override
	public void setViews(ViewHolder holder, GridItem t, int position) {
		WdjyImageView imageView = holder.getView(R.id.grid_item);
		imageView.setOnMeasureListener(new OnMeasureListener() {
			@Override
			public void onMeasureSize(int width, int height) {
				mPoint.set(width, height);
			}
		});
		String path = dates.get(position).getPath();
		imageView.setTag(position);
		imageView.setImageResource(R.drawable.empty_photo);
		loader.DisplayImage(path, imageView, false, 300);

	}
}
