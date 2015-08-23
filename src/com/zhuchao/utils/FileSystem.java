package com.zhuchao.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by zhuchao on 8/2/15.
 */
public class FileSystem {
    public static long getFolderSize(){
        String movieImage="/sdcard/luquba/HeadImages";
        String movie="/sdcard/luquba/StoryImages";
        if(isSDExit()){
            File file=new File(movie);
            File file1=new File(movieImage);
            return getFolderSize(file)+getFolderSize(file1);
        }
        return 0;
    }
    public static long getFolderSize(File file){
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);
                }else{
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return size/1048576;
    }
    public static boolean isSDExit(){
        String state= Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }
    public static boolean saveFile(String content,String name){
        try
        {
            if(FileSystem.isSDExit()) {
                String fileFolderPath = "/sdcard/luquba/tmp/";
                String parentPath = "/sdcard/luquba/";
                File parentFile = new File(parentPath);
                File fileFolder = new File(fileFolderPath);
                File file = new File(fileFolderPath + name+".txt");
                if (!parentFile.exists())
                    parentFile.mkdir();
                if (!fileFolder.exists()) {
                    fileFolder.mkdir();
                }
                if (!file.exists()) {
                    file.createNewFile();
                } else {
                    return true;
                }
                FileOutputStream outStream = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(outStream,"utf-8");
                writer.write(content);
                writer.write("\n");
                writer.flush();
                writer.close();//记得关闭
                outStream.close();
            }
        }catch (Exception e) {
            Log.e("m", "file write error");
        }
        return false;
    }
}
