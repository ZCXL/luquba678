package com.zhuchao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuchao.bean.Wish;
import com.zhuchao.utils.ImageLoader;

import java.util.ArrayList;

import cn.luquba678.R;
import cn.luquba678.utils.DateUtils;

/**
 * Created by zhuchao on 8/12/15.
 */
public class WishAdapter extends BaseAdapter {
    private ArrayList<Wish>wishs;
    private Context context;
    private ImageLoader imageLoader;

    public WishAdapter(ArrayList<Wish>wishs,Context context){
        this.wishs=wishs;
        this.context=context;
        imageLoader=new ImageLoader(context);
        imageLoader.setStub_id(R.drawable.new_wish_default_1);
    }
    @Override
    public int getCount() {
        return wishs.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return wishs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.wish_item,null);
            holder=new ViewHolder();
            holder.image=(ImageView)convertView.findViewById(R.id.image_1);
            holder.time=(TextView)convertView.findViewById(R.id.show_time);
            holder.wish_word=(TextView)convertView.findViewById(R.id.title);
            holder.wish_layout=(RelativeLayout)convertView.findViewById(R.id.wish_time_layout);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        if(position==0){
            holder.image.setImageResource(R.drawable.new_wish_default_2);
            holder.wish_word.setVisibility(View.INVISIBLE);
            holder.wish_layout.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);
            return convertView;
        }else{
            holder.wish_word.setVisibility(View.VISIBLE);
            holder.wish_layout.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
        }
        Wish wish=wishs.get(position-1);
        holder.time.setText(DateUtils.timeHint(Long.parseLong(wish.getCreate_time())* 1000, "yyyy年MM月dd日"));
        holder.wish_word.setText(DateUtils.getDay(Long.parseLong(wish.getCreate_time())*1000));
        holder.image.setTag(wish.getPic());

        imageLoader.DisplayImage(wish.getPic(),holder.image);
        return convertView;
    }

    static class ViewHolder{
        public TextView time;
        public ImageView image;
        public TextView wish_word;
        public RelativeLayout wish_layout;
    }
}
