package com.zhuchao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuchao.utils.ImageLoader;
import java.util.ArrayList;

import cn.luquba678.R;
import cn.luquba678.entity.School;

/**
 * Created by zhuchao on 8/30/15.
 */
public class SchoolAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<School>schools;
    private ImageLoader ima;
    public SchoolAdapter(Context context,ArrayList<School>schools){
        super();
        this.context=context;
        this.schools=schools;
        ima=new ImageLoader(context);
        ima.setStub_id(R.drawable.new_city_default);
    }
    @Override
    public int getCount() {
        return schools.size();
    }

    @Override
    public Object getItem(int position) {
        return schools.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.list_university_item,null);
            holder=new ViewHolder();
            holder.count=(TextView)convertView.findViewById(R.id.grid_university_count);
            holder.university_name=(TextView)convertView.findViewById(R.id.university_name);
            holder.university_area=(TextView)convertView.findViewById(R.id.university_area);
            holder.logo=(ImageView)convertView.findViewById(R.id.university_logo);
            holder.mark_211=convertView.findViewById(R.id.is_211);
            holder.mark_985=convertView.findViewById(R.id.is_985);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        School t=schools.get(position);
        holder.count.setText("排名:" + t.getRank());
        holder.university_name.setText(t.getSchool_name() + "");
        holder.university_area.setText("地区:" + t.getAreaName());
        ima.DisplayImage(t.getLogo(),holder.logo);
        if (t.getIs_211() == 1) {
            holder.mark_211.setVisibility(View.VISIBLE);
        } else
            holder.mark_211.setVisibility(View.GONE);
        if (t.getIs_985() == 1)
            holder.mark_985.setVisibility(View.VISIBLE);
        else
            holder.mark_985.setVisibility(View.GONE);

        return convertView;
    }
    private static class ViewHolder{
        TextView count;
        TextView university_name;
        TextView university_area ;
        ImageView logo;
        View mark_211 ;
        View mark_985 ;
    }
}
