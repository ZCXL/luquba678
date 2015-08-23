package com.zhuchao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuchao.function.GetFont;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/14/15.
 */
public class FontAdapter extends BaseAdapter{

    private Context context;
    public FontAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return GetFont.getFontNum();
    }

    @Override
    public Object getItem(int position) {
        return GetFont.getFontList(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.font_item,null);
            holder=new ViewHolder();
            holder.font=(TextView)convertView.findViewById(R.id.font_item);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.font.setText(GetFont.getFontList(position));
        return convertView;
    }

    static class ViewHolder{
        TextView font;
    }
}
