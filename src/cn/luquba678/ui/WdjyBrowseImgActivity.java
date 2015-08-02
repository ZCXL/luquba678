package cn.luquba678.ui;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import cn.luquba678.R;
import cn.luquba678.entity.GridItem;
import cn.luquba678.ui.DiskLruCache.Snapshot;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class WdjyBrowseImgActivity extends Activity {

	private Gallery mGallery;

	private List<GridItem> mGridList;

	private int mPos = 0;

	private DiskLruCache mDiskLruCache;

	private File mFileImages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.wdjy_gallery_imgs);

		mPos = getIntent().getIntExtra("ImgIdx", 0);
		mGridList = (List<GridItem>) getIntent()
				.getSerializableExtra("ImgList");

		mGallery = (Gallery) findViewById(R.id.gallery_wdjy_imgs);
		mGallery.setAdapter(new ImageAdapter(this));
		mGallery.setSelection(mPos);
		mFileImages = getDiskCacheDir(this, "images");

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

	public Bitmap getBitmapFromImgUrl(String imgUrl) {
		// ����ͼƬURL��Ӧ��key
		final String key = hashKeyForDisk(imgUrl);
		String strFileNameString = mFileImages.getPath() + File.separator + key
				+ ".jpg";
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeFile(strFileNameString);
		return bitmap;
	}

	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mGridList.size();
		}

		public Object getItem(int position) {
			return mGridList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			Bitmap bm = getBitmapFromImgUrl(mGridList.get(position).getPath());
			if (bm == null) {
				i.setImageResource(R.drawable.empty_photo);
			} else {
				i.setImageBitmap(bm);
			}

			i.setAdjustViewBounds(true);
			i.setScaleType(ScaleType.FIT_XY);
			i.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			return i;
		}

		private Context mContext;

	}
}
