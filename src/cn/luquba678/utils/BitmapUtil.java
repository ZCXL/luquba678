package cn.luquba678.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class BitmapUtil {

	public static Bitmap getLoacalBitmap(String url) {
		try {
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 4;
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis,null,options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * uri->bitmap最大正方形
	 */
	public static Bitmap getBitmapByUri(Uri uri, Context context, int length) {
		try {
			return ImageUtil.centerSquareScaleBitmap(
					MediaStore.Images.Media.getBitmap(
							context.getContentResolver(), uri), length);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Resuose->Bitmap
	 */
	public static Bitmap getBitmapByResuose(int id, Context context, int length) {
		return getBitmapByUri(getUri(context, id), context, length);
	}

	public static Uri getUri(Context context, int id) {
		return Uri.parse("android.resource://" + context.getPackageName() + "/"
				+ id);
	}

	/**
	 * Bitmap->file
	 * 
	 * @return
	 */
	public static File bitmapToPic(Bitmap bitmap, String str) {
		File file = new File(CacheUtil.getCache(), str);// 将要保存图片的路径
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
			bos.flush();
			bos.close();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	public static Drawable loadImageFromNetwork(String imageUrl)  
	{  
	    Drawable drawable = null;  
	    try {  
	        // 可以在这里通过文件名来判断，是否本地有此图片  
	        drawable = Drawable.createFromStream(  
	                new URL(imageUrl).openStream(), "image.jpg");  
	    } catch (IOException e) {  
	        Log.d("test", e.getMessage());  
	    }  
	    if (drawable == null) {  
	        Log.d("test", "null drawable");  
	    } else {  
	        Log.d("test", "not null drawable");  
	    }  
	      
	    return drawable ;  
	}  
}
