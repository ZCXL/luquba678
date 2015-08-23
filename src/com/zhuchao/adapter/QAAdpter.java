package com.zhuchao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuchao.bean.QA;

import java.util.ArrayList;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/21/15.
 */
public class QAAdpter extends BaseAdapter {
    private ArrayList<QA> qas;
    private Context context;
    public QAAdpter(ArrayList<QA>qas,Context context){
        this.qas=qas;
        this.context=context;
    }
    @Override
    public int getCount() {
        return qas.size();
    }

    @Override
    public Object getItem(int position) {
        return qas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.qa_item,null);
            holder=new ViewHolder();
            holder.question=(TextView)convertView.findViewById(R.id.question);
            holder.answer=(TextView)convertView.findViewById(R.id.answer);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        QA qa=qas.get(position);
        holder.question.setText(qa.getQuestion());
        holder.answer.setText(qa.getAnswer());

        return convertView;
    }
    public static class ViewHolder{
        public TextView question;
        public TextView answer;
    }
}
