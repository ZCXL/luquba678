package cn.luquba678.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.zhuchao.view_rewrite.SearchRotateView;

import cn.luquba678.R;

public class Test extends Activity {

    private SearchRotateView searchRotateView;
    private boolean isRotate=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        searchRotateView=(SearchRotateView)findViewById(R.id.search);

        searchRotateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRotate) {
                    searchRotateView.startSearch();
                    isRotate=true;
                }else {
                    searchRotateView.endSearch();
                    isRotate=false;
                }
            }
        });
    }
}
