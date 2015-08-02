package cn.luquba678.utils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.util.Log;

public class FileCache {
	private String dirString;

	public FileCache() {
		super();
		dirString = getCacheDir();
		boolean ret = FileHelper.createDirectory(dirString);
		Log.e("", "FileHelper.createDirectory:" + dirString + ", ret = " + ret);
	}

	public static File getFile(String url) {
		File f = new File(getSavePath(url));
		return f;
	}

	public void clear() {
		FileHelper.deleteDirectory(dirString);
	}

	public static String getSavePath(String url) {
		String filename = hashKeyForDisk(url) + ".jpg";
		return getCacheDir() + filename;
	}

	public static String getCacheDir() {

		return FileManager.getSaveFilePath();
	}

	/**
	 * 使用MD5算法对传入的key进行加密并返回。
	 */
	public static String hashKeyForDisk(String key) {
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

	private static String bytesToHexString(byte[] bytes) {
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
}
