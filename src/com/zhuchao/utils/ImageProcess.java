package com.zhuchao.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressLint("SdCardPath")
public class ImageProcess {
   public enum FileType_Image{StoryImage,HeadImage}//Type of image
   public static String TAG="ImageProcess";
   @SuppressLint("SdCardPath")
public static boolean InputImage(Bitmap bitmap,FileType_Image fImage,String filename){
   try{
	   if(FileSystem.isSDExit()){
	        String fileFolderPath="";
	        String parentPath="/sdcard/luquba/";
	        if(fImage== FileType_Image.StoryImage){
		      fileFolderPath=parentPath+"StoryImages/";
	          }else if (fImage== FileType_Image.HeadImage) {
		      fileFolderPath=parentPath+"HeadImages/";
	           }
	            File parentFile=new File(parentPath);
	            File fileFolder=new File(fileFolderPath);
	            File file=new File(fileFolderPath+filename);
	            if(!parentFile.exists())
	            	parentFile.mkdir();
	            if(!fileFolder.exists()){
		           fileFolder.mkdir();
	            }
	            if(!file.exists()){
	            	file.createNewFile();
	            }else{
	            	return true;
	            }
	            FileOutputStream fOutputStream=new FileOutputStream(file);
	            InputStream iStream=Bitmap2IS(bitmap);
	            int temp = 0;
				byte[] data = new byte[1024];
				while ((temp = iStream.read(data)) != -1) {
					fOutputStream.write(data, 0, temp);
				}
				fOutputStream.close();
				return true;
	      }else{
	    	  return false;
	      }
	   }catch(IOException exception){
		   Log.d(TAG, exception.toString() + "");
		   return false;
	   }
   }
   public static Bitmap OutputImage(FileType_Image fImage,String filename){
	   String fileFolderPath="";
	   Bitmap bitmap;
	   String parentPath="/sdcard/luquba/";
	   if(fImage== FileType_Image.StoryImage){
		   fileFolderPath=parentPath+"StoryImages/";
	   }else if (fImage== FileType_Image.HeadImage) {
		   fileFolderPath=parentPath+"HeadImages/";
	   }
	    try {
		  if(FileSystem.isSDExit()){
			  File parentFile=new File(parentPath);
			  File fileFolder=new File(fileFolderPath);
			  File file=new File(fileFolderPath+filename);
			  if(!parentFile.exists())
	            	parentFile.mkdir();
			  if(!fileFolder.exists()){
				  fileFolder.mkdir();
			  }
			  if(!file.exists()){
				  return null;
			  }
			  FileInputStream oStream=new FileInputStream(file);
			  bitmap=getBitmap(oStream);//ת��
			  oStream.close();
			  return bitmap;
		  }
	    } catch (Exception e) {
	    	Log.d(TAG, e.toString() + "");
			   return null;
	    }
	   return null;
   }
	public static File saveTempFile(String file_name){
		String parentPath="/sdcard/luquba/";
		String folderPath=parentPath+"image_tmp/";
		if(FileSystem.isSDExit()){
			File parent=new File(parentPath);
			File folder=new File(folderPath);
			if(!parent.exists())
				parent.mkdir();
			if(!folder.exists())
				folder.mkdir();
			File file=new File(folderPath+file_name);
			return file;
		}
		return null;
	}
   public static boolean SearchImage(FileType_Image fImage,String filename){
	   String fileFolderPath="";
	   String parentPath="/sdcard/luquba/";
	   if(fImage== FileType_Image.StoryImage){
		   fileFolderPath=parentPath+"StoryImages/";
	   }else if (fImage== FileType_Image.HeadImage) {
		   fileFolderPath=parentPath+"HeadImages/";
	   }
	   if(FileSystem.isSDExit()){
		   File file=new File(fileFolderPath+filename);
		   if(file.exists()){
				  return true;
		  }
	   }
	   return false;
   }
   public static boolean DeleteImage(){
	   DeleteImage("/sdcard/luquba/StoryImages");
	   DeleteImage("/sdcard/luquba/HeadImages");
	   DeleteImage("/sdcard/luquba/image_tmp");
	   return true;
   }
	public static boolean DeleteImage(String url){
		String parentPathString=url;
		if(FileSystem.isSDExit()){
			File file=new File(parentPathString);
			if(file.exists()){
				File[]files=file.listFiles();
				if(files!=null||files.length!=0){
					for(File f:files)
						f.delete();
					return true;
				}
			}
		}
		return false;
	}

   private static InputStream Bitmap2IS(Bitmap bm){
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
           InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
           return sbs;  
       }
   public static Bitmap getBitmap(InputStream in){
		Bitmap bitmap=null;
		if(in!=null){
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ALPHA_8;
	    options.inPurgeable = true;  
		options.inInputShareable = true; 
		options.inJustDecodeBounds=false;
		bitmap= BitmapFactory.decodeStream(in, null, options);
		return bitmap;
		}else{
		return null;
		}
	}
   public static Bitmap zoomImage(Bitmap bitmap,int nwidth,int nheight){
	   float bili=((float)nwidth)/nheight;
	   int width=bitmap.getWidth();
	   int height=bitmap.getHeight();
	   float afterwidth=height*bili;
	   float afterheight=width/bili;
	   if(afterheight<=height)
		   height=(int) afterheight;
	   if(afterwidth<=width)
		   width=(int) afterwidth;  
	   //Bitmap bitmap1=Bitmap.createBitmap(bitmap,(bitmap.getWidth()-width)/2,(bitmap.getHeight()-height)/2, width, height);
	   float scaleWidth=((float)nwidth)/width;
	   float scaleHeight=((float)nheight)/height;
	   Matrix matrix=new Matrix();
	   matrix.postScale(scaleWidth,scaleHeight);
       return Bitmap.createBitmap(bitmap, (bitmap.getWidth() - width) / 2, (bitmap.getHeight() - height) / 2, width, height, matrix, true);
   }
	public static Bitmap compressImage(Bitmap image,int size) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>size) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            Log.d("length", String.valueOf(baos.toByteArray().length / 1024) + "k");
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
        Log.d("length", String.valueOf(baos.toByteArray().length / 1024) + "k");
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	public static Bitmap getBitmapFromUri(Uri uri,Context context)
	{
		try
		{
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
			return bitmap;
		}
		catch (Exception e)
		{
			Log.e("[Android]", e.getMessage());
			Log.e("[Android]", "目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * get bitmap by uri
	 * @param uri
	 * @return
	 */
	public static Bitmap getBitmapFromPath(Uri uri){
		String path=uri.getPath();
		Log.d("zhuchao",path);
		//先解析图片边框的大小
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inJustDecodeBounds = true;
		Bitmap bm ;
		bm= BitmapFactory.decodeFile(path, ops);
		ops.inSampleSize = 1;
		int oHeight = ops.outHeight;
		int oWidth = ops.outWidth;
		int contentHeight = 0;
		int contentWidth = 0;
		if(((float)oHeight/contentHeight) < ((float)oWidth/contentWidth)){
			ops.inSampleSize = (int) Math.ceil((float)oWidth/contentWidth);
		}else{
			ops.inSampleSize = (int) Math.ceil((float)oHeight/contentHeight);
		}
		ops.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, ops);
		return bm;
	}
}
