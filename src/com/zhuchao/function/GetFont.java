package com.zhuchao.function;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by zhuchao on 8/14/15.
 */
public class GetFont {
    public final static String fonts[]={"Cute","少女字体","行楷","古隶","启体简体","流行体简体","瘦金体","诺基亚古印"};

    public final static String fontPaths[]={"cute","hksn","hwxk","fzgl","fzqt","fzlx","sjt","nokia"};
    public static String getFontList(int position){
        if(position>=0&&position<fonts.length)
            return fonts[position];
        else
            return null;
    }

    public static int getFontNum(){
        return fonts.length;
    }

    public static Typeface getFont(int position,Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/"+fontPaths[position]+".ttf");
    }
}
