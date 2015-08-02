package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Selection;
import android.text.Spannable;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.activity.person.PersonDetailEditDialog;
import cn.luquba678.activity.person.PersonNameEditDialog;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.photo.Bimp;
import cn.luquba678.ui.Base64;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.BitmapUtil;
import cn.luquba678.utils.ImageLoader;
import cn.luquba678.utils.SPUtils;

public class PersonMessageActivity extends Activity implements OnClickListener {
	private ImageView headImage, top_back;
	private TextView name, sex, date, province, detail, score, type, year,
			top_text;
	private RelativeLayout rl_head_img, rl_name, rl_sex, rl_date, rl_province,
			rl_detail, rl_score, rl_type, rl_year;
	private PersonDetailEditDialog pDetailEditDialog;
	private PersonNameEditDialog pNameEditDialog;
	private ImageLoader imgLoader;
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_CAMER = 2;
	private String proviceName = null;
	private String mYear, mMonth, mDay;
	private final int img_head = 1, nick_name = 2, user_sex = 3, birth = 4,
			address = 5, intro = 6, user_year = 7,examinatio_type = 8, grade = 9;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			String changeBody = (String) msg.obj;

			switch (msg.what) {
			case img_head:
				headImage.setImageBitmap(bitmap);
				SPUtils.put(PersonMessageActivity.this, "headpic",changeBody);
				break;
			case nick_name:
				name.setText(changeBody);
				SPUtils.put(PersonMessageActivity.this, "nickname",changeBody);
				break;
			case user_sex:
				if(changeBody.equals("1")){
					changeBody = "男";
				}else {
					changeBody = "女";
				}
				sex.setText(changeBody);
				SPUtils.put(PersonMessageActivity.this, "sex",changeBody);
				break;
			case birth:
				date.setText(changeBody);
				SPUtils.put(PersonMessageActivity.this, "birth",changeBody);
				break;
			case address:
				province.setText(changeBody);
				SPUtils.put(PersonMessageActivity.this, "address",changeBody);
				break;
			case intro:
				detail.setText(changeBody);
				SPUtils.put(PersonMessageActivity.this, "intro",changeBody);
				break;
			case examinatio_type:
				type.setText(changeBody);
				SPUtils.put(PersonMessageActivity.this, "type",changeBody);
				break;
			case user_year:
				year.setText(changeBody);
				SPUtils.put(PersonMessageActivity.this, "year",changeBody);
				break;
			case grade:
				score.setText(changeBody);
				SPUtils.put(PersonMessageActivity.this, "grade",changeBody);
				break;
			default:
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_detail);
		initPrams();
	}

	@SuppressLint("NewApi")
	private void initPrams() {
		imgLoader = new ImageLoader(this);
		DisplayMetrics metric = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		headImage = (ImageView) findViewById(R.id.head_img);
//		imgLoader.DisplayImage(Const.BASE_URL+"/"+SPUtils.get(this, "headpic", "sss").toString(), headImage, false);
		new DownloadImageTask().execute(Const.BASE_URL+"/"+SPUtils.get(this, "headpic", "sss").toString());  
		rl_head_img = (RelativeLayout) findViewById(R.id.rl_head_img);
		rl_head_img.setOnClickListener(this);
		rl_name = (RelativeLayout) findViewById(R.id.rl_name);
		rl_name.setOnClickListener(this);
		rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
		rl_sex.setOnClickListener(this);
		rl_date = (RelativeLayout) findViewById(R.id.rl_date);
		rl_date.setOnClickListener(this);
		rl_province = (RelativeLayout) findViewById(R.id.rl_province);
		rl_province.setOnClickListener(this);
		rl_detail = (RelativeLayout) findViewById(R.id.rl_detail);
		rl_detail.setOnClickListener(this);
		rl_score = (RelativeLayout) findViewById(R.id.rl_score);
		rl_score.setOnClickListener(this);
		rl_type = (RelativeLayout) findViewById(R.id.rl_type);
		rl_type.setOnClickListener(this);
		rl_year = (RelativeLayout) findViewById(R.id.rl_year);
		rl_year.setOnClickListener(this);

		top_back = (ImageView) findViewById(R.id.title_top_image);
		top_back.setOnClickListener(this);
		top_text = (TextView) findViewById(R.id.top_text);
		top_text.setText("个人信息");
		name = (TextView) findViewById(R.id.name);
		name.setText(SPUtils.get(this, "nickname", "ss").toString());
		sex = (TextView) findViewById(R.id.sex);
		sex.setText(SPUtils.get(this, "sex", "男").toString());
		if(SPUtils.get(this, "sex", "男").toString().equals("1")){
			sex.setText("男");
		}else {
			sex.setText("女");
		}
		date = (TextView) findViewById(R.id.date);
		date.setText(SPUtils.get(this, "birth", "1999-9-9").toString());
		province = (TextView) findViewById(R.id.province);
		province.setText(SPUtils.get(this, "address", "湖北").toString());
		proviceName = SPUtils.get(this, "address", "湖北").toString();
		detail = (TextView) findViewById(R.id.detail);
		detail.setText(SPUtils.get(this, "intro", "***").toString());
		score = (TextView) findViewById(R.id.score);
		score.setText(SPUtils.get(this, "grade", "0").toString());
		type = (TextView) findViewById(R.id.type);
		if(SPUtils.get(this, "type", "理科").toString().equals("1")){
			type.setText("理科");
		}else {
			type.setText("文科");
		}
		year = (TextView) findViewById(R.id.year);
		year.setText(SPUtils.get(this, "year", "0").toString());
		RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) headImage
				.getLayoutParams();
		linearParams.setMarginStart(width / 2);
		RelativeLayout.LayoutParams linearParams_name = (RelativeLayout.LayoutParams) name
				.getLayoutParams();
		linearParams_name.setMarginStart(width / 2);
		RelativeLayout.LayoutParams linearParams_sex = (RelativeLayout.LayoutParams) sex
				.getLayoutParams();
		linearParams_sex.setMarginStart(width / 2);
		RelativeLayout.LayoutParams linearParams_date = (RelativeLayout.LayoutParams) date
				.getLayoutParams();
		linearParams_date.setMarginStart(width / 2);
		RelativeLayout.LayoutParams linearParams_province = (RelativeLayout.LayoutParams) province
				.getLayoutParams();
		linearParams_province.setMarginStart(width / 2);
		RelativeLayout.LayoutParams linearParams_detail = (RelativeLayout.LayoutParams) detail
				.getLayoutParams();
		linearParams_detail.setMarginStart(width / 2);
		RelativeLayout.LayoutParams linearParams_score = (RelativeLayout.LayoutParams) score
				.getLayoutParams();
		linearParams_score.setMarginStart(width / 2);
		RelativeLayout.LayoutParams linearParams_type = (RelativeLayout.LayoutParams) type
				.getLayoutParams();
		linearParams_type.setMarginStart(width / 2);
		RelativeLayout.LayoutParams linearParams_year = (RelativeLayout.LayoutParams) year
				.getLayoutParams();
		linearParams_year.setMarginStart(width / 2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_name:
			pNameEditDialog = new PersonNameEditDialog(this, name);
			pNameEditDialog.show();
			break;
		case R.id.rl_detail:
			pDetailEditDialog = new PersonDetailEditDialog(this, detail);
			pDetailEditDialog.show();
			break;
		case R.id.rl_head_img:
			showSelectPhotoDialog();
			break;
		case R.id.title_top_image:
			finish();
			break;
		case R.id.rl_sex:
			showSelectSexDialog();
			break;
		case R.id.rl_date:
			startActivityForResult(new Intent(this, BirthdayActivity.class), 5);
			break;
		case R.id.rl_province:
			startActivityForResult(new Intent(this, CitiesActivity.class), 4);
			break;
		case R.id.rl_score:
			showSelectScore();
			break;
		case R.id.rl_type:
			showSelectSubjectDialog();
			break;
		case R.id.rl_year:
			showSelectYearDialog();
			break;
		default:
			break;
		}
	}

	Bitmap bitmap;

	@SuppressWarnings("static-access")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_PICTURE) {
			if (Bimp.drr.get(0) != null) {
				bitmap = getLoacalBitmap(Bimp.drr.get(0));
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
				byte[] ba = bao.toByteArray();
				String baEncode = Base64.encodeBytes(ba);
				uploadChange(img_head, baEncode);
			}
		}
		if (resultCode == 5) {
			proviceName = data.getStringExtra("proviceName");
			String cityName = data.getStringExtra("cityName");
			String areaName = "";
			if (data.getStringExtra("areaName") != null) {
				areaName = data.getStringExtra("areaName");
			}
			uploadChange(address, proviceName + " " + cityName + " " + areaName);

		}
		if (resultCode == 6) {
			mYear = data.getStringExtra("year");
			mMonth = data.getStringExtra("month");
			mDay = data.getStringExtra("day");
			String mDate = mYear + "-" + mMonth + "-" + mDay;
			uploadChange(birth, mDate);
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
			bitmap = (Bitmap) bundle.get("data");
			FileOutputStream b = null;
			File file = new File("/sdcard/luquba_Image/");
			file.mkdirs();
			String fileName = "/sdcard/luquba_Image/" + name;
			try {
				b = new FileOutputStream(fileName);
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
				byte[] ba = bao.toByteArray();
				String baEncode = Base64.encodeBytes(ba);
				uploadChange(img_head, baEncode);
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
		}
	}

	public void showSelectPhotoDialog() {
		final AlertDialog photoDialog;
		photoDialog = new AlertDialog.Builder(this).create();
		photoDialog.show();
		photoDialog.getWindow().setContentView(R.layout.person_select_photo);
		RelativeLayout rl_camer, rl_photo_album, rl_cancle;
		rl_camer = (RelativeLayout) photoDialog.findViewById(R.id.rl_camer);
		rl_photo_album = (RelativeLayout) photoDialog
				.findViewById(R.id.rl_photo_album);
		rl_cancle = (RelativeLayout) photoDialog.findViewById(R.id.rl_cancle);
		rl_camer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE), SELECT_CAMER);
				photoDialog.dismiss();
			}
		});
		rl_photo_album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(PersonMessageActivity.this,
						SelectPhotosActivity.class), SELECT_PICTURE);
				photoDialog.dismiss();
			}
		});
		rl_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photoDialog.dismiss();
			}
		});
	}

	private void showSelectSexDialog() {
		final AlertDialog sexDialog;
		sexDialog = new AlertDialog.Builder(this).create();
		sexDialog.show();
		sexDialog.getWindow().setContentView(R.layout.person_select_sex_dialog);
		RelativeLayout rl_boy, rl_girl, rl_cancle;
		rl_boy = (RelativeLayout) sexDialog.findViewById(R.id.rl_boy);
		rl_boy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadChange(user_sex, "1");
				sexDialog.dismiss();
			}
		});
		rl_girl = (RelativeLayout) sexDialog.findViewById(R.id.rl_girl);
		rl_girl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadChange(user_sex, "2");
				sexDialog.dismiss();
			}
		});
		rl_cancle = (RelativeLayout) sexDialog.findViewById(R.id.rl_cancle);
		rl_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sexDialog.dismiss();
			}
		});
	}

	private void showSelectSubjectDialog() {
		final AlertDialog subjectDialog;
		subjectDialog = new AlertDialog.Builder(this).create();
		subjectDialog.show();
		subjectDialog.getWindow().setContentView(
				R.layout.person_select_sciences);
		RelativeLayout rl_liberal_arts, rl_science_subject, rl_cancle;
		rl_liberal_arts = (RelativeLayout) subjectDialog
				.findViewById(R.id.rl_liberal_arts);
		rl_science_subject = (RelativeLayout) subjectDialog
				.findViewById(R.id.rl_science_subject);
		rl_cancle = (RelativeLayout) subjectDialog.findViewById(R.id.rl_cancle);
		rl_liberal_arts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadChange(examinatio_type, "文科");
				subjectDialog.dismiss();
			}
		});
		rl_science_subject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadChange(examinatio_type, "理科");
				subjectDialog.dismiss();
			}
		});
		rl_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				subjectDialog.dismiss();
			}
		});
	}

	private void showSelectScore() {
		final AlertDialog scoreDialog;
		scoreDialog = new AlertDialog.Builder(this).create();
		scoreDialog.setView(new EditText(this));
		scoreDialog.show();
		scoreDialog.getWindow().setContentView(R.layout.person_score_dialog);
		final TextView input_error_hint = (TextView) scoreDialog
				.findViewById(R.id.input_error_hint);
		final EditText ed_score = (EditText) scoreDialog
				.findViewById(R.id.ed_score);
		CharSequence text = ed_score.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
		Button btn_ok, btn_cancle;
		btn_ok = (Button) scoreDialog.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String editScore = ed_score.getText().toString();
				if (checkInputScoreIsValid(editScore, proviceName,
						input_error_hint)) {
					uploadChange(grade, editScore);
					scoreDialog.dismiss();
				}
			}
		});
		btn_cancle = (Button) scoreDialog.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				scoreDialog.dismiss();
			}
		});
	}

	private void showSelectYearDialog() {
		final AlertDialog scoreDialog;
		scoreDialog = new AlertDialog.Builder(this).create();
		scoreDialog.show();
		scoreDialog.getWindow().setContentView(
				R.layout.person_select_examinaton_year_dialog);
		RelativeLayout rl_year, rl_after_year, rl_after_next_year, rl_cancle;
		rl_year = (RelativeLayout) scoreDialog.findViewById(R.id.rl_year);
		rl_after_year = (RelativeLayout) scoreDialog
				.findViewById(R.id.rl_after_year);
		rl_after_next_year = (RelativeLayout) scoreDialog
				.findViewById(R.id.rl_after_next_year);
		rl_cancle = (RelativeLayout) scoreDialog.findViewById(R.id.rl_cancle);
		rl_year.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadChange(user_year, "2016");
				scoreDialog.dismiss();
			}
		});

		rl_after_year.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadChange(user_year, "2017");
				scoreDialog.dismiss();
			}
		});

		rl_after_next_year.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadChange(user_year, "2018");
				scoreDialog.dismiss();
			}
		});

		rl_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				scoreDialog.dismiss();
			}
		});
	}

	public Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean checkInputScoreIsValid(String score, String whichProvince,
			TextView hint) {
		if (score.trim().isEmpty()) {
			hint.setText("请输入分数");
			return false;
		}
		if (!isInteger(score) || score.substring(0, 1).equals("0")) {
			hint.setText("请输入正确的分数");
			return false;
		}
		if (whichProvince.equals("上海")) {
			if (Integer.parseInt(score) > 630 || Integer.parseInt(score) < 1) {
				hint.setText("您输入的分数超出了范围");
				return false;
			}
		} else if (whichProvince.equals("江苏")) {
			if (Integer.parseInt(score) > 485 || Integer.parseInt(score) < 1) {
				hint.setText("您输入的分数超出了范围");
				return false;
			}
		} else if (whichProvince.equals("浙江")) {
			if (Integer.parseInt(score) > 810 || Integer.parseInt(score) < 1) {
				hint.setText("您输入的分数超出了范围");
				return false;
			}
		} else if (whichProvince.equals("海南")) {
			if (Integer.parseInt(score) > 900 || Integer.parseInt(score) < 1) {
				hint.setText("您输入的分数超出了范围");
				return false;
			}
		} else {
			if (Integer.parseInt(score) > 750 || Integer.parseInt(score) < 1) {
				hint.setText("您输入的分数超出了范围");
				return false;
			}
		}
		return true;
	}

	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public void uploadChange(final int whichChange, final String changeBody) {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					String body = changeBody;
					String which = null;
					switch (whichChange) {
					case img_head:
						which = "headpic_base64";
						break;
					case nick_name:
						which = "nickname";
						break;
					case user_sex:
						which = "sex";
						break;
					case birth:
						which = "birth";
						break;
					case address:
						which = "address";
						break;
					case intro:
						which = "intro";
						break;
					case examinatio_type:
						which = "type";
						break;
					case user_year:
						which = "year";
						break;
					case grade:
						which = "grade";
						break;
					default:
						break;
					}
					MultipartEntity entity = new MultipartEntity();
					entity.addPart(which, new StringBody(body,Charset.forName("utf-8")));
					String changeUserInfoUrl = String.format(
							Const.CHANGE_USER_INFO,
							User.getUID(PersonMessageActivity.this),
							User.getLoginToken(PersonMessageActivity.this));
					JSONObject obj = HttpUtil.getRequestJson(changeUserInfoUrl,
							entity);
					Integer errcode = obj.getInteger("errcode");
					Message msg = handler.obtainMessage();
					msg.what = whichChange;
					msg.obj = changeBody;
					if (errcode == 0) {
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Drawable>   
	{  
	        protected Drawable doInBackground(String... urls) {  
	            return BitmapUtil.loadImageFromNetwork(urls[0]);  
	        }  
	        
	        protected void onPostExecute(Drawable result) {  
	        	headImage.setImageDrawable(result);  
	        }  
	} 
	
}
