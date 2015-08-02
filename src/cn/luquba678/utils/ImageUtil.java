package cn.luquba678.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html.ImageGetter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Collin 一些图像处理的工具
 */
public class ImageUtil {

	public static ImageGetter getImageGetterFromUrl(ImageLoader ima,
			Context context) {
		return new Getter(ima, context);
	}
	

	/**
	 * 获取图片路径
	 */
	public static String getPathByUri(Uri uri, Context context) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Log.i("info", filePathColumn[0] + "");
		Cursor cursor = context.getContentResolver().query(uri, filePathColumn,
				null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String uriPath = cursor.getString(columnIndex);
		cursor.close();
		return uriPath;
	}

	/**
	 * 选择文件夹中文件
	 */
	public static void chooseImage(Activity c) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		c.startActivityForResult(intent, 1);
	}

	/**
	 * 拍照
	 * 
	 * @param picturePath
	 * @param context
	 */
	public static void camara(String picturePath, Context context) {
		Intent intent = new Intent();
		intent.setAction("android.media.action.IMAGE_CAPTURE");
		intent.addCategory("android.intent.category.DEFAULT");
		// 创建照片存储路径，方便调用
		picturePath = "/mnt/sdcard/DCIM/pic"
				+ DateFormat.format("kkmmss", new Date()).toString() + ".jpg";
		File file = new File(picturePath);
		file.mkdirs();
		Uri uri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		context.startActivity(intent);
	}

	/**
	 * 销毁图片
	 * 
	 * @param photo
	 */
	public static void destoryBimap(Bitmap photo) {
		if (photo != null && !photo.isRecycled()) {
			photo.recycle();
			photo = null;
		}
	}

	public static int imgLength;
	public static int screamdp;

	/**
	 * 给imageview设置drawable
	 */
	public static void setDraById(ImageView iv, int id, Context c) {
		iv.setImageDrawable(c.getResources().getDrawable(id));
	}

	public static Bitmap centerSquareScaleBitmap1(Bitmap bitmap, int edgeLength) {
		return centerSquareScaleBitmap(bitmap, 0);
	}

	/**
	 * 剪裁图片中间正方形
	 */
	public static Bitmap centerSquareBitmap(Bitmap bitmap, int edgeLength) {
		if (null == bitmap || edgeLength <= 0)
			return null;
		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();
		if (widthOrg > edgeLength && heightOrg > edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math
					.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;
			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				return null;
			}
			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
						edgeLength, edgeLength);
				scaledBitmap.recycle();
			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}

	/**
	 * 剪裁图片中间最大正方形,并指定边长。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int leng) {
		if (null == bitmap)
			return null;
		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();
		Matrix matrix = null;
		if (leng != 0) {
			matrix = new Matrix();
			float scaleWidht = ((float) leng / widthOrg);
			matrix.postScale(scaleWidht, scaleWidht);
		}
		if (widthOrg == heightOrg) {
			return Bitmap.createBitmap(result, 0, 0, widthOrg, heightOrg,
					matrix, true);
		}
		int edgeLength = Math.min(widthOrg, heightOrg);
		int scaledWidth = widthOrg * leng / edgeLength;
		int scaledHeight = heightOrg * leng / edgeLength;
		Bitmap scaledBitmap;
		try {
			scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
					scaledHeight, true);
			// 从图中截取正中间的正方形部分。
			int xTopLeft = ((scaledWidth - leng) / 2);
			int yTopLeft = ((scaledHeight - leng) / 2);
			result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
					leng, leng);
			scaledBitmap.recycle();
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	// 放大缩小图片

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

	}
	
	public static Bitmap zoomBitmapWith(Bitmap bitmap, int w) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		matrix.postScale(scaleWidht, scaleWidht);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		
	}
	
	public static Bitmap zoomBitmapHeight(Bitmap bitmap, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) h / height);
		matrix.postScale(scaleWidht, scaleWidht);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		
	}

	/**
	 * 获取资源文件
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	// 获得圆角图片的方法

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		// 保证是方形，并且从中心画
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int w;
		int deltaX = 0;
		int deltaY = 0;
		if (width <= height) {
			w = width;
			deltaY = height - w;
		} else {
			w = height;
			deltaX = width - w;
		}
		final Rect rect = new Rect(deltaX, deltaY, w, w);
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		// 圆形，所有只用一个

		int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
		canvas.drawRoundRect(rectF, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 计算切图大小
	 */
	public static int getLength(int w, Context context) {
		return (w - DensityUtils.dp2px(context, 30)) / 3;
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] buffer = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			baos = null;
		}
		return buffer;
	}

	public static void writeImage(File f, Bitmap bm) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			fos.write(Bitmap2Bytes(bm));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				fos = null;
			}
		}

	}

}

class Getter implements ImageGetter {
	private ImageLoader ima;
	private Context context;

	public Getter(ImageLoader ima, Context context) {
		this.ima = ima;
		this.context = context;
	}

	@Override
	public Drawable getDrawable(String source) {
		Bitmap bmp = ima.getBitmap(source);
		Drawable drawable = new BitmapDrawable(context.getResources(), bmp);
		return drawable;
	}

}
