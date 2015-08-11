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
import cn.luquba678.entity.Comment;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;

/**
 * Created by zhuchao on 8/4/15.
 */
public class CommentAdapter extends BaseAdapter {
    private ArrayList<Comment> comments;
    private Context context;
    private LinearLayout layout;

    private ImageLoader loader;
    public CommentAdapter(ArrayList<Comment>comments,Context context,LinearLayout layout){
        this.comments = comments;
        this.context=context;
        this.layout=layout;
        loader=new ImageLoader(context);
    }
    @Override
    public int getCount() {
        if(comments==null)
            return 0;
        return comments.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if(comments==null)
            return null;
        return comments.get(position);
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

        Comment comment=comments.get(position-1);
        holder.comment_content.setText(comment.getContent());
        holder.nick_name.setText(comment.getNickname());
        holder.create_time.setText(comment.getComment_time());
        holder.head_img.setTag(comment.getHeadpic());

        loader.DisplayImage(comment.getHeadpic(), holder.head_img);
        return convertView;
    }

    static class ViewHolder{
        TextView nick_name;
        TextView comment_content;
        ImageView head_img;
        TextView create_time;
    }
}
