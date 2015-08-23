package cn.luquba678.activity;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.zhuchao.bean.Wish;
import com.zhuchao.http.Network;
import com.zhuchao.http.NetworkFunction;
import com.zhuchao.utils.ImageProcess;
import com.zhuchao.utils.LayoutUtils;
import com.zhuchao.view_rewrite.ChangImageStyle;
import com.zhuchao.view_rewrite.ChangeTextFontDialog;
import com.zhuchao.view_rewrite.ChangeTextSizeDialog;
import com.zhuchao.view_rewrite.ChangeTextStyleWindow;
import com.zhuchao.view_rewrite.ColorSelector;
import com.zhuchao.view_rewrite.LoadingDialog;

import cn.luquba678.R;
import cn.luquba678.activity.fragment.TabMyStoryFragment;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.ui.RoundImageView;
import cn.luquba678.utils.BitmapUtil;
import cn.luquba678.utils.SPUtils;
import cn.luquba678.view.MoveTextView;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WdjySaveActivity extends CommonActivity implements OnClickListener,ColorSelector.OnColorSelectedListener,ChangeTextFontDialog.OnFontChangeListener,ChangeTextSizeDialog.OnSizeChangeListener,ChangImageStyle.OnImageChangeStyleListener{

	private ImageView showImgView = null;

	private FrameLayout fl;
	private LinearLayout openAlbumTextView = null;
	private LinearLayout switchImgView = null;
	private LinearLayout switchFontTextView = null;
    private RelativeLayout bg_gallery;
	private MoveTextView textView;

    private LoadingDialog loadingDialog;
	private String myWishString;

	private Integer[] mImageIds = {R.drawable.wish_1,R.drawable.wish_2,R.drawable.wish_3,R.drawable.wish_4,R.drawable.wish_5,R.drawable.wish_6,R.drawable.wish_7,R.drawable.wish_8,R.drawable.wish_9,R.drawable.wish_10,R.drawable.wish_11,R.drawable.wish_12,R.drawable.wish_13,R.drawable.wish_14,R.drawable.wish_15,R.drawable.wish_16,R.drawable.wish_17,R.drawable.wish_18,R.drawable.wish_19,R.drawable.wish_20};

	private Gallery mGallery = null;
	private Button nextButton;
    private Wish w;


    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_CAMERA= 2;
    private AlertDialog photoDialog;


    /**
     * view of changing text style
     */
    private ChangeTextStyleWindow changeTextStyleWindow;

	private ColorSelector colorSelector;

    private ChangeTextFontDialog changeTextFontDialog;

    private ChangeTextSizeDialog changeTextSizeWindow;

    private ChangImageStyle changImageStyle;

    private Bitmap bitmap;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String url=String.valueOf(msg.obj);
                    uploadWish(url,myWishString);
                    break;
                case 1:
                    toast("保存成功！");
                    Intent intent=new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("wish",w);
                    intent.putExtras(bundle);
                    setResult(2,intent);
                    finish();
                    if(TabMyStoryFragment.activity!=null){
                        TabMyStoryFragment.activity.dismiss();
                    }
                    loadingDialog.stopProgressDialog();
                    break;
                case 2:
                    toast("保存寄语失败，请重试。。。");
                    loadingDialog.stopProgressDialog();
                    nextButton.setEnabled(true);
                    break;
            }
        }

    };


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.wdjy_save_activity);
		Intent intent = super.getIntent();
		myWishString = intent.getStringExtra("MyWish");

		initView();
	}

    /**
     * init view
     */
	private void initView() {
        /**
         * upload animation
         */
        loadingDialog=new LoadingDialog(this);

        /**
         * view of change text's style
         */
        changeTextStyleWindow=new ChangeTextStyleWindow(this,this);
        /**
         * change text font dialog
         */
        changeTextFontDialog=new ChangeTextFontDialog(this,R.style.dialog);
        changeTextFontDialog.setOnFontChangeListener(this);

        /**
         * the interface containing of text view
         */
		fl = (FrameLayout) findViewById(R.id.id_framelayout_show_image);
		fl.setDrawingCacheEnabled(true);
        textView = new MoveTextView(this, myWishString, 0xFF000000, 0xFFFFFFFF);
        colorSelector=new ColorSelector(this,textView.getSolidColor(),this,myWishString.substring(0, 1));
		fl.addView(textView);

        /**
         * change text size window
         */
        changeTextSizeWindow=new ChangeTextSizeDialog(this,R.style.dialog,textView.getTextSize());
        changeTextSizeWindow.setOnSizeChangeListener(this);
        /**
         * background
         */
		showImgView = (ImageView) findViewById(R.id.id_imgview_pic);
		showImgView.setImageResource(mImageIds[7]);

        /**
         * button of save
         */
		findViewById(R.id.top_back).setOnClickListener(this);
		nextButton = (Button) findViewById(R.id.top_submit);
		nextButton.setText("保存");
		nextButton.setOnClickListener(this);
		nextButton.setEnabled(true);
        /**
         * change default images
         */
		mGallery = (Gallery) findViewById(R.id.id_gallery);
		mGallery.setAdapter(new ImageAdapter(this));
		mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                fl.addView(textView);
                showImgView.setImageResource(mImageIds[position]);
                bitmap = BitmapFactory.decodeResource(getResources(), mImageIds[position]);
                bg_gallery.setVisibility(View.INVISIBLE);
            }
        });
        bg_gallery=(RelativeLayout)findViewById(R.id.id_bg_gallery);
        /**
         * change image by using gallery
         */
		switchImgView = (LinearLayout) findViewById(R.id.id_tab_bottom_switch_line);
		switchImgView.setOnClickListener(this);

        /**
         * change text style
         */
		switchFontTextView = (LinearLayout) findViewById(R.id.id_tab_bottom_font_line);
		switchFontTextView.setOnClickListener(this);

        changImageStyle=new ChangImageStyle(this,R.style.dialog);
        changImageStyle.setOnImageChangeStyleListener(this);
        /**
         * open album
         */
		openAlbumTextView = (LinearLayout) findViewById(R.id.id_tab_bottom_album_line);
		openAlbumTextView.setOnClickListener(this);
        /**
         * image's bitmap
         */
        bitmap=((BitmapDrawable)showImgView.getDrawable()).getBitmap();
	}

	@Override
	public void onColorSelected(int color) {
		textView.setTextColor(color);
	}

    @Override
    public void onFontChange(Typeface typeface) {
        textView.setType(typeface);
    }
    @Override
    public void onSizeChange(int value) {
        textView.setTextSize(value);
    }

    @Override
    public void onImageChangeStyle(Bitmap bitmap) {
        showImgView.setImageBitmap(bitmap);
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
        /**
         * get result from picture album
         */
        if (requestCode == SELECT_PICTURE) {
            String imgLocalPath = (String) SPUtils.get(this, "head_img", "nopath");
            if (!imgLocalPath.equals("nopath")) {
                Bitmap bm = BitmapUtil.getLoacalBitmap(imgLocalPath);
                bitmap=bm;
                showImgView.setImageBitmap(bm);
            }
        }
        /**
         * select photo from camera
         */
        else if (requestCode == SELECT_CAMERA){
            Uri uri = Uri.fromFile(tempFile);
            Bitmap bm = BitmapUtil.getLoacalBitmap(uri.getPath());
            bitmap=bm;
            showImgView.setImageBitmap(bm);
        }

	}
	@Override
	public void onClick(View v) {
        /**
         * ensure that switch button is visible
         */
        if(v.getId()==R.id.id_tab_bottom_switch_line){
            if (bg_gallery.getVisibility() == View.INVISIBLE) {
                bg_gallery.setVisibility(View.VISIBLE);
                fl.removeView(textView);
            } else {
                fl.addView(textView);
                bg_gallery.setVisibility(View.INVISIBLE);
            }
            return;
        }
        if(bg_gallery.getVisibility()==View.VISIBLE){
            bg_gallery.setVisibility(View.INVISIBLE);
            fl.addView(textView);
        }
		switch (v.getId()) {
            case R.id.top_back:
                this.finish();
                break;
            case R.id.rl_photo_album:
                getImageFromGallery();
                photoDialog.dismiss();
                break;
            case R.id.rl_cancle:
                photoDialog.dismiss();
                break;
            case R.id.rl_camer:
                getImageFromCamera();
                photoDialog.dismiss();
                break;
            case R.id.id_tab_bottom_album_line:
                showSelectPhotoDialog();
                break;
            case R.id.id_tab_bottom_font_line:
                changeTextStyleWindow.showAtLocation(findViewById(R.id.id_tab_bottom_font_line), Gravity.BOTTOM|Gravity.RIGHT,LayoutUtils.dpToPx(10,getResources()), LayoutUtils.dpToPx(60, getResources()));
                break;
            case R.id.top_submit:
                /**
                 * upload image to server
                 */
                uploadImage();
                break;

            /**
             * view in window
             */
            case R.id.text_style:
                changImageStyle.show(bitmap);
                changeTextStyleWindow.dismiss();
                break;
            case R.id.text_font:
                changeTextFontDialog.show();
                changeTextStyleWindow.dismiss();
                break;
            case R.id.text_color:
                colorSelector.show();
                changeTextStyleWindow.dismiss();
                break;
            case R.id.text_size:
                changeTextSizeWindow.setSize(textView.getTextSize());
                changeTextSizeWindow.show();
                changeTextStyleWindow.dismiss();
                break;
            case R.id.text_orientation:
                if(textView.getOrientation()==LinearLayout.VERTICAL) {
                    textView.setOrientation(LinearLayout.HORIZONTAL);
                    changeTextStyleWindow.setText_oritention("水平");
                }else{
                    textView.setOrientation(LinearLayout.VERTICAL);
                    changeTextStyleWindow.setText_oritention("垂直");
                }
                changeTextStyleWindow.dismiss();
                break;
            default:
                break;
		}

	}

    /**
     * upload image to server
     */
    private void uploadImage(){
        nextButton.setEnabled(false);
        /**
         * save image into memory
         */
        final Bitmap resultBitmap = getScreenShot(getView(R.id.id_framelayout_show_image));
        Date d=new Date(System.currentTimeMillis());
        final String path=String.valueOf(d.getYear())+String.valueOf(d.getMonth())+String.valueOf(d.getDay())+ String.valueOf(d.getHours()) + String.valueOf(d.getMinutes()) + String.valueOf(d.getSeconds()) + ".jpg";

        /**
         * upload image
         */
        if(Network.checkNetWorkState(this)&&ImageProcess.InputImage(resultBitmap,ImageProcess.FileType_Image.StoryImage,path)){
            loadingDialog.startProgressDialog();
            new Thread(new Runnable() {
                @SuppressLint("SdCardPath")
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    String result= NetworkFunction.UploadHeadImage(WdjySaveActivity.this, path, "/sdcard/luquba/StoryImages/" + path);
                    Log.d("zhuchao", result);
                    if(result.contains("http:")&&result.contains(".jpg")){
                        JSONObject obj=JSONObject.parseObject(result);
                        Message message=new Message();
                        message.obj=obj.getString("data");
                        message.what=0;
                        handler.sendMessage(message);
                    }else{
                        handler.sendEmptyMessage(1);
                    }
                }
            }).start();
        }else{
            Toast.makeText(this, "未连接网络", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * select photo from album
     */
	public void showSelectPhotoDialog() {
		photoDialog = new AlertDialog.Builder(this).create();
		photoDialog.show();
		photoDialog.getWindow().setContentView(R.layout.person_select_photo);
		RelativeLayout rl_camera, rl_photo_album, rl_cancel;
		rl_camera = (RelativeLayout) photoDialog.findViewById(R.id.rl_camer);
		rl_photo_album = (RelativeLayout) photoDialog.findViewById(R.id.rl_photo_album);
		rl_cancel = (RelativeLayout) photoDialog.findViewById(R.id.rl_cancle);
		rl_camera.setOnClickListener(this);
		rl_photo_album.setOnClickListener(this);
		rl_cancel.setOnClickListener(this);
	}
    /**
     * upload wish
     */
    private void uploadWish(String url,String wish) {
        String link = String.format(Const.SEND_WORD, User.getUID(this), User.getLoginToken(this));
        MultipartEntity entity = new MultipartEntity();
        try {
            entity.addPart("pic", new StringBody(url, Charset.forName("utf-8")));
            entity.addPart("content", new StringBody(wish, Charset.forName("utf-8")));
            JSONObject obj = HttpUtil.getRequestJson(link, entity);
            int code = obj.getInteger("errcode");
            if (code == 0) {
                w=new Wish(obj.getJSONObject("data").toString());
                handler.sendEmptyMessage(1);
            }else
                handler.sendEmptyMessage(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定Activity的截屏，保存到png文件
     * @param view
     * @return
     */
    private Bitmap getScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        int width = view.getWidth()-2* LayoutUtils.dpToPx(36.66f,getResources());
        int height = view.getHeight()-LayoutUtils.dpToPx(21.33f, getResources());
        Bitmap b = Bitmap.createBitmap(bitmap, LayoutUtils.dpToPx(36.66f,getResources()), LayoutUtils.dpToPx(8.33f,getResources()), width, height);
        view.destroyDrawingCache();
        return b;
    }
    /**
     * get image from gallery
     */
    private void getImageFromGallery(){
        startActivityForResult(new Intent(self, SelectPhotosActivity.class),SELECT_PICTURE);
    }

    File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
    /**
     * get image from camera
     */
    private void getImageFromCamera(){
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
        startActivityForResult(intent, SELECT_CAMERA);
    }
    /**
     * set name of temporary file
     * @return
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }




    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RoundImageView i = new RoundImageView(mContext);
            i.setImageResource(mImageIds[position]);
            i.setAdjustViewBounds(true);
            i.setScaleType(ScaleType.FIT_XY);
            int width = getWindowManager().getDefaultDisplay().getWidth();
            int height = getWindowManager().getDefaultDisplay().getHeight();
            i.setLayoutParams(new Gallery.LayoutParams(width - 200, height - 500));
            return i;
        }

        private Context mContext;
    }
}