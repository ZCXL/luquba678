package cn.luquba678.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private static final String TAG = FileUtil.class.getSimpleName();
	private String local_image_path;

	public FileUtil(Context context, String local_image_path) {
		this.local_image_path = local_image_path;
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * 保存图片到制定路径
	 * 
	 * @param filepath
	 * @param bitmap
	 */
	public void saveBitmap(String filename, Bitmap bitmap) {
		if (!isExternalStorageWritable()) {
			Log.i(TAG, "SD卡不可用，保存失败");
			return;
		}

		if (bitmap == null) {
			return;
		}

		try {

			File file = new File(local_image_path, filename);
			FileOutputStream outputstream = new FileOutputStream(file);
			if ((filename.indexOf("png") != -1)
					|| (filename.indexOf("PNG") != -1)) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputstream);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputstream);
			}

			outputstream.flush();
			outputstream.close();

		} catch (FileNotFoundException e) {
			Log.i(TAG, e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, e.getMessage());
		}
	}

	public static String saveFile(Context c, String fileName, Bitmap bitmap) {
		return saveFile(c, "", fileName, bitmap);
	}

	public static String saveFile(Context c, String filePath, String fileName,
			Bitmap bitmap) {
		byte[] bytes = bitmapToBytes(bitmap);
		return saveFile(c, filePath, fileName, bytes);
	}

	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	public static String saveFile(Context c, String filePath, String fileName,
			byte[] bytes) {
		String fileFullName = "";
		FileOutputStream fos = null;
		String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
				.format(new Date());
		try {
			String suffix = "";
			if (filePath == null || filePath.trim().length() == 0) {
				filePath = Environment.getExternalStorageDirectory()
						+ "/JiaXT/" + dateFolder + "/";
			}
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			File fullFile = new File(filePath, fileName + suffix);
			fileFullName = fullFile.getPath();
			fos = new FileOutputStream(new File(filePath, fileName + suffix));
			fos.write(bytes);
		} catch (Exception e) {
			fileFullName = "";
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					fileFullName = "";
				}
			}
		}
		return fileFullName;
	}

	/**
	 * 返回当前应用 SD 卡的绝对路径 like
	 * /storage/sdcard0/Android/data/com.example.test/files
	 */
	@SuppressLint("SdCardPath")
	public String getAbsolutePath() {
		File root = new File(local_image_path);
		if (!root.exists()) {
			root.mkdirs();

		}

		return local_image_path;

	}

	@SuppressLint("SdCardPath")
	public boolean isBitmapExists(String filename) {
		File dir = new File(local_image_path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// context.getExternalFilesDir(null);
		File file = new File(dir, filename);

		return file.exists();
	}

}
