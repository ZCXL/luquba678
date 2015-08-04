package com.zhuchao.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuchao.utils.ImageLoader;

import java.util.ArrayList;

import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;

/**
 * Created by zhuchao on 8/4/15.
 */
public class CommentAdapter extends BaseAdapter {
    private ArrayList<News> newses;
    private Context context;
    private LinearLayout layout;

    private ImageLoader loader;
    public CommentAdapter(ArrayList<News>newses,Context context,LinearLayout layout){
        this.newses=newses;
        this.context=context;
        this.layout=layout;
        loader=new ImageLoader(context);
    }
    @Override
    public int getCount() {
        if(newses==null)
            return 0;
        return newses.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if(newses==null)
            return null;
        return newses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(position==0){
            return layout;
        }

        if(convertView==null||convertView.getTag()==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.comment_cell,null);
            holder=new ViewHolder();
            holder.nick_name=(TextView)convertView.findViewById(R.id.nick_name);
            holder.comment_content=(TextView)convertView.findViewById(R.id.comment_content);
            holder.head_img=(ImageView)convertView.findViewById(R.id.head_img);
            holder.create_time=(TextView)convertView.findViewById(R.id.comment_create_time);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        News news=newses.get(position-1);
        holder.comment_content.setText(news.getContent());
        holder.nick_name.setText(news.getNickname());
        holder.create_time.setText(news.getCreatetime());
        holder.head_img.setTag(news.getHeadpic());

        loader.DisplayImage(news.getHeadpic(), holder.head_img);
        return convertView;
    }

    static class ViewHolder{
        TextView nick_name;
        TextView comment_content;
        ImageView head_img;
        TextView create_time;
    }
}
