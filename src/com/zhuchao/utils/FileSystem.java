package com.zhuchao.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhuchao on 8/2/15.
 */
public class FileSystem {
    public static long getFolderSize(){
        String movieImage="/sdcard/luquba/HeadImages";
       // String movie="/sdcard/luquba/Movies";
        if(isSDExit()){
            //File file=new File(movie);
            File file1=new File(movieImage);
            //return getFolderSize(file)+getFolderSize(file1);
            return getFolderSize(file1);
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
}
