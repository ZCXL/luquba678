package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;

import cn.luquba678.R;
import cn.luquba678.activity.fragment.TabMyStoryFragment;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.Base64;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.ui.RoundImageView;
import cn.luquba678.utils.BitmapUtil;
import cn.luquba678.utils.SPUtils;
import cn.luquba678.view.MoveTextView;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WdjySaveActivity extends CommonActivity implements OnClickListener {
	private ImageView showImgView = null;
	private Button saveBtn = null;

	private FrameLayout fl;
	private LinearLayout openAlbumTextView = null;
	private LinearLayout switchImgView = null;
	private LinearLayout switchFontTextView = null;
	private ImageButton switchButton;
	private ImageButton albunButton;
	private ImageButton fontButton;
	private TextView switchText;
	private TextView albunText;
	private TextView fontText;
	private MoveTextView textView;

	private boolean bSwitchFont = true;

	private String myWishString;

	private static final int REQUEST_CODE_PICK_IMAGE = 1;

	private ProgressDialog mProgressDialog;

	private Integer[] mImageIds = { R.drawable.a, R.drawable.b, R.drawable.c,
			R.drawable.d, R.drawable.e };

	private int nClickCount = 0;

	Handler mHandler = new Handler();

	private Gallery mGallery = null;

	private Bitmap mAlbumBitmap = null;
	private Button nextButton;
	private View view;

	// 获取指定Activity的截屏，保存到png文件
	private Bitmap getScreenShot(View view) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		int width = view.getWidth();
		int height = view.getHeight();
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, width, height);
		view.destroyDrawingCache();
		return b;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		view = getView(R.id.id_bg_gallery);
		setContentView(R.layout.wdjy_save_activity);

		Intent intent = super.getIntent();
		myWishString = intent.getStringExtra("MyWish");

		initView();

	}

	private void initView() {
		fl = (FrameLayout) findViewById(R.id.id_framelayout_show_image);
		fl.setDrawingCacheEnabled(true);
		textView = new MoveTextView(this, myWishString, 0xFF000000, 0xFFFFFFFF);
		fl.addView(textView);
		showImgView = (ImageView) findViewById(R.id.id_imgview_pic);
		showImgView.setImageResource(mImageIds[4]);

		findViewById(R.id.top_back).setOnClickListener(this);
		findViewById(R.id.top_text).setVisibility(View.INVISIBLE);
		nextButton = (Button) findViewById(R.id.top_submit);
		nextButton.setText("保存");
		nextButton.setOnClickListener(this);
		nextButton.setEnabled(true);
		switchButton = (ImageButton) findViewById(R.id.btn_tab_bottom_switch);
		albunButton = (ImageButton) findViewById(R.id.btn_tab_bottom_album);
		fontButton = (ImageButton) findViewById(R.id.btn_tab_bottom_font);

		switchText = (TextView) findViewById(R.id.tv_tab_bottom_switch_text);
		albunText = (TextView) findViewById(R.id.tv_tab_bottom_album_text);
		fontText = (TextView) findViewById(R.id.tv_tab_bottom_font_text);

		mGallery = (Gallery) findViewById(R.id.id_bg_gallery);
		mGallery.setAdapter(new ImageAdapter(this));
		mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				fl.addView(textView);
				showImgView.setImageResource(mImageIds[position]);
				mGallery.setVisibility(View.INVISIBLE);
			}
		});
		switchImgView = (LinearLayout) findViewById(R.id.id_tab_bottom_switch_line);
		switchImgView.setOnClickListener(this);

		switchFontTextView = (LinearLayout) findViewById(R.id.id_tab_bottom_font_line);
		switchFontTextView.setOnClickListener(this);

		openAlbumTextView = (LinearLayout) findViewById(R.id.id_tab_bottom_album_line);
		openAlbumTextView.setOnClickListener(this);
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
			int width = getWindowManager().getDefaultDisplay().getWidth();// 鑾峰彇灞忓箷楂樺害
			int height = getWindowManager().getDefaultDisplay().getHeight();// 鑾峰彇灞忓箷楂樺害
			i.setLayoutParams(new Gallery.LayoutParams(width - 200,
					height - 500));
			// i.setBackgroundResource(R.drawable.e);
			return i;
		}

		private Context mContext;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SELECT_PICTURE) {
			String imgLocalPath = (String) SPUtils.get(this, "head_img", "nopath");
			if (!imgLocalPath.equals("nopath")) {
				Bitmap bm = BitmapUtil.getLoacalBitmap(imgLocalPath);
				showImgView.setImageBitmap(bm);
			}
		}

		if (requestCode == SELECT_CAMER) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				return;
			}
			String name = new DateFormat().format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))
					+ ".jpg";
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");
			FileOutputStream b = null;
			File file = new File("/sdcard/luquba_Image/");
			file.mkdirs();
			String fileName = "/sdcard/luquba_Image/" + name;
			SPUtils.put(this, "head_img", fileName);
			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			showImgView.setImageBitmap(bitmap);
		}

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				toast("保存成功！");
				WdjySaveActivity.this.finish();
				dialog.dismiss();
				//Intent intent = new Intent(self, MainFragmentActivity.class);
				WdjySaveActivity.this.finish();
				if(TabMyStoryFragment.activity!=null){
					TabMyStoryFragment.activity.dismiss();
				}
				break;

			default:
				toast("保存寄语失败，请重试。。。");
				dialog.dismiss();

				break;
			}
			nextButton.setEnabled(true);
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			this.finish();
			break;
		case R.id.rl_photo_album:
			startActivityForResult(
					new Intent(self, SelectPhotosActivity.class),
					SELECT_PICTURE);
			photoDialog.dismiss();
			break;
		case R.id.rl_cancle:
			photoDialog.dismiss();
			break;
		case R.id.rl_camer:
			startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
					SELECT_CAMER);
			photoDialog.dismiss();
			break;
		case R.id.id_tab_bottom_switch_line:
			if (mGallery.getVisibility() == View.INVISIBLE) {
				mGallery.setVisibility(View.VISIBLE);
				fl.removeView(textView);
				// saveBtn.setEnabled(false);
				// saveBtn.setTextColor(0xFFCFCBCB);
			} else {

				mGallery.setVisibility(View.INVISIBLE);
			}

			break;
		case R.id.id_tab_bottom_album_line:
			if (mGallery.getVisibility() == View.VISIBLE) {

				mGallery.setVisibility(View.INVISIBLE);
				// saveBtn.setEnabled(true);
				// saveBtn.setTextColor(0xFFFDFDFE);
			}
			showSelectPhotoDialog();
			/*
			 * Intent intent = new Intent(Intent.ACTION_PICK);
			 * intent.setType("image/*"); startActivityForResult(intent,
			 * REQUEST_CODE_PICK_IMAGE);
			 */
			break;
		case R.id.id_tab_bottom_font_line:
			if (bSwitchFont) {
				// Typeface tf = Typeface.create("sans", Typeface.ITALIC);
				textView.setType(Typeface.DEFAULT_BOLD);
				textView.setTextColor(0xFFFFFFFF, 0xFF000000);
				bSwitchFont = false;
			} else {
				// Typeface tf = Typeface.create("serif", Typeface.SANS_SERIF);
				textView.setTextColor(0xFF000000, 0xFFFFFFFF);
				textView.setType(Typeface.DEFAULT);
				bSwitchFont = true;
			}
			break;

		case R.id.top_submit:
			dialog = new ProgressDialog(this);
			dialog.setMessage("正在保存...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.show();
			nextButton.setEnabled(false);
			final Bitmap resultBitmap = getScreenShot(getView(R.id.id_framelayout_show_image));
			/*
			 * ImageView imageView = new ImageView(self);
			 * imageView.setImageBitmap(resultBitmap); AlertDialog alertDialog =
			 * new AlertDialog.Builder(this).create();
			 * alertDialog.setView(imageView); alertDialog.show();
			 */

			Runnable run = new Runnable() {

				@Override
				public void run() {
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					resultBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);

					byte[] ba = bao.toByteArray();
					String baEncode = Base64.encodeBytes(ba);
					MultipartEntity entity = new MultipartEntity();
					try {
						entity.addPart("pic_base64", new StringBody(baEncode));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					try {
						String url = String.format(Const.SEND_WORD,
								User.getUID(self), User.getLoginToken(self));
						JSONObject obj = JSONObject.parseObject(HttpUtil
								.postRequestEntity(url, entity));
						Integer errcode = obj.getInteger("errcode");
						handler.sendEmptyMessage(errcode);
						/*
						 * if (errcode == 0) { toast("保存成功！");
						 * WdjySaveActivity.this.finish(); dialog.dismiss(); }
						 * else { toast("保存寄语失败，请重试。。。"); dialog.dismiss(); }
						 * nextButton.setEnabled(true);
						 */
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			};

			Executors.newSingleThreadExecutor().execute(run);

			/*
			 * new Thread() { public void run() { if (SaveWdjy("1",
			 * myWishString)) { mHandler.post(new Runnable() {
			 * 
			 * @Override public void run() {
			 * Toast.makeText(WdjySaveActivity.this, "保存成功！", 0).show(); } });
			 * Intent intent = new Intent(WdjySaveActivity.this,
			 * MainFragmentActivity.class);
			 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			 * startActivity(intent); } else { //
			 * Toast.makeText(WdjySaveActivity.this, // "瀵勮淇濆瓨澶辫触锛岃绋嶅悗鍐嶈瘯锛�,
			 * Toast.LENGTH_LONG).show(); } }; }.start();
			 */
			break;

		default:
			break;
		}

	}

	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_CAMER = 2;
	AlertDialog photoDialog;

	public void showSelectPhotoDialog() {
		photoDialog = new AlertDialog.Builder(this).create();
		photoDialog.show();
		photoDialog.getWindow().setContentView(R.layout.person_select_photo);
		RelativeLayout rl_camer, rl_photo_album, rl_cancle;
		rl_camer = (RelativeLayout) photoDialog.findViewById(R.id.rl_camer);
		rl_photo_album = (RelativeLayout) photoDialog
				.findViewById(R.id.rl_photo_album);
		rl_cancle = (RelativeLayout) photoDialog.findViewById(R.id.rl_cancle);
		rl_camer.setOnClickListener(this);
		rl_photo_album.setOnClickListener(this);
		rl_cancle.setOnClickListener(this);
	}
}