package cn.luquba678.activity;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuchao.bean.Wish;
import com.zhuchao.view_rewrite.DeleteWishDialog;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.ImageAdapter;
import cn.luquba678.ui.FullScreenDialog;
import cn.luquba678.utils.DateUtils;

public class BrowseImageDialog extends FullScreenDialog implements android.view.View.OnClickListener,AdapterView.OnItemSelectedListener,DeleteWishDialog.OnDeleteListener{
	private Gallery mGallery;
	private Context context;
	private ArrayList<Wish>wishs;
	private int mPos = 0;
	private TextView top_text;
	private ImageView back_image;
	private LinearLayout back;
	private ImageAdapter imageAdapter;

	private ImageView delete;
    private OnChangeListener onChangeListener;
	private DeleteWishDialog deleteWishDialog;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					break;
			}
		}

	};

	public BrowseImageDialog(Activity context, ArrayList<Wish> wishs, int position) {
		super(context);
		this.context = context;
		this.wishs=wishs;
		mPos = position;
		deleteWishDialog=new DeleteWishDialog(context);
		deleteWishDialog.setOnDeleteListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wdjy_gallery_imgs);
		initView();

	}

	private void initView() {

		delete=(ImageView)findViewById(R.id.delete);
		delete.setOnClickListener(this);
		back_image=(ImageView)findViewById(R.id.title_top_image);
		back_image.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.top_back);
		back.setOnClickListener(this);
		top_text=(TextView)findViewById(R.id.top_text);
		mGallery = (Gallery) findViewById(R.id.gallery_wdjy_imgs);
		imageAdapter=new ImageAdapter(context, wishs, R.layout.image_view, this);
		mGallery.setAdapter(imageAdapter);
		mGallery.setSelection(mPos);
		mGallery.setOnItemSelectedListener(this);
        handler.sendEmptyMessageAtTime(0,3000);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(onChangeListener!=null)
				onChangeListener.onChange(wishs);
			dismiss();
            return true;
		}
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction()==2){

		}
		return false;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.top_back:
                if(onChangeListener!=null)
                    onChangeListener.onChange(wishs);
				this.dismiss();
				break;
			case R.id.title_top_image:
                if(onChangeListener!=null)
                    onChangeListener.onChange(wishs);
				dismiss();
				break;
			case R.id.delete:
				/**
				 * add remove code
				 */
				deleteWishDialog.show(wishs.get(mPos),mPos);
				break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		mPos=position;
		top_text.setText(DateUtils.timeHint(Long.parseLong(wishs.get(position).getCreate_time())* 1000, "yyyy年MM月dd日"));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

    public OnChangeListener getOnChangeListener() {
        return onChangeListener;
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

	@Override
	public void onSuccess(int position) {
		wishs.remove(position);
		imageAdapter.notifyDataSetChanged();
		if(wishs.size()==0) {
			if (onChangeListener != null)
				onChangeListener.onChange(wishs);
			dismiss();
		}
	}

	@Override
	public void onFail() {
		Toast.makeText(context,"Delete failed",Toast.LENGTH_SHORT).show();
	}

	/**
     * delete rollback
     */
    public interface OnChangeListener{
        void onChange(ArrayList<Wish>wishs);
    }
}
