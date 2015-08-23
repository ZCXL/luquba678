package com.zhuchao.view_rewrite;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuchao.adapter.FontAdapter;
import com.zhuchao.function.GetFont;

import cn.luquba678.R;

/**
 * Created by zhuchao on 8/14/15.
 */
public class ChangeTextFontDialog extends Dialog implements AdapterView.OnItemClickListener{
    private OnFontChangeListener onFontChangeListener;

    private ListView listView;
    private FontAdapter adapter;
    private View backView,view;
    public OnFontChangeListener getOnFontChangeListener() {
        return onFontChangeListener;
    }

    public void setOnFontChangeListener(OnFontChangeListener onFontChangeListener) {
        this.onFontChangeListener = onFontChangeListener;
    }

    public ChangeTextFontDialog(Context context,int theme) {
        super(context, android.R.style.Theme_Translucent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.font_list);

        listView = (ListView)findViewById(R.id.font_list);
        adapter = new FontAdapter(getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        backView=findViewById(R.id.backView);
        view=findViewById(R.id.rootSelector);

        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < backView.getLeft() || event.getX() > backView.getRight() || event.getY() >backView.getBottom() || event.getY() < backView.getTop()) {
                    dismiss();
                }
                return false;
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(onFontChangeListener!=null)
            onFontChangeListener.onFontChange(GetFont.getFont(position,getContext()));
        dismiss();
    }
    public interface OnFontChangeListener{
        void onFontChange(Typeface typeface);
    }

}
