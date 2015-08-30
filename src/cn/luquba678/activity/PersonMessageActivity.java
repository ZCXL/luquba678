package cn.luquba678.activity;

import cn.luquba678.utils.ToolUtils;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.zhuchao.http.Network;
import com.zhuchao.http.NetworkFunction;
import com.zhuchao.utils.ImageLoaderTask;
import com.zhuchao.utils.ImageProcess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.luquba678.R;
import cn.luquba678.activity.person.PersonDetailEditDialog;
import cn.luquba678.activity.person.PersonNameEditDialog;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.BitmapUtil;
import cn.luquba678.utils.SPUtils;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class PersonMessageActivity extends Activity implements OnClickListener {
    private LinearLayout back;
	private ImageView headImage, top_back;
	private TextView name, sex, date, province, detail, score, type, year;
	private RelativeLayout rl_head_img, rl_name, rl_sex, rl_date, rl_province, rl_detail, rl_score, rl_type, rl_year;
	private PersonDetailEditDialog pDetailEditDialog;
	private PersonNameEditDialog pNameEditDialog;
	private static final int SELECT_PICTURE = 10;
	private static final int SELECT_CAMERA = 20;
	private static final int CROP_IMAGE=30;
	private String provinceName = null;
	private String mYear, mMonth, mDay;
	private final int img_head = 1, nick_name = 2, user_sex = 3, birth = 4, address = 5, intro = 6, user_year = 7,examinatio_type = 8, grade = 9;

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			String changeBody = (String) msg.obj;

			if(msg.what==0){
				ToolUtils.showShortToast(PersonMessageActivity.this, "修改失败");
				return;
			}
			switch (msg.what) {
				case img_head:
					break;
				case 20:
					uploadChange(img_head, changeBody);
					SPUtils.put(PersonMessageActivity.this, "headpic",changeBody);
					/**
					 * update head
					 */
					new ImageLoaderTask(headImage,PersonMessageActivity.this, ImageProcess.FileType_Image.HeadImage).execute(changeBody);
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
					SPUtils.put(PersonMessageActivity.this, "intro",changeBody);
					if(changeBody.length()>8)
						changeBody=changeBody.substring(0,8)+"...";
					detail.setText(changeBody);
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
			ToolUtils.showShortToast(PersonMessageActivity.this, "修改成功");
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
		DisplayMetrics metric = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		headImage = (ImageView) findViewById(R.id.head_img);
		if(Network.checkNetWorkState(this))
			new DownloadImageTask().execute(SPUtils.get(this, "headpic", "sss").toString());
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
        back=(LinearLayout)findViewById(R.id.top_back);
        back.setOnClickListener(this);
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
		provinceName = SPUtils.get(this, "address", "湖北").toString();
		detail = (TextView) findViewById(R.id.detail);
		String detail_string=SPUtils.get(this, "intro", "***").toString();
		if(detail_string.length()>8)
			detail_string=detail_string.substring(0,8)+"...";
		detail.setText(detail_string);
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
            case R.id.top_back:
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/**
		 * get result from picture album
		 */
		if (requestCode == SELECT_PICTURE) {
            if(data!=null){
                Uri uri=data.getData();
                startPhotoZoom(uri);
            }else{
                Toast.makeText(this, "未选择图片", Toast.LENGTH_LONG).show();
            }
		}
		/**
		 * upload data
		 */
        else if(requestCode==CROP_IMAGE){
            getHead(data);
        }
		/**
		 * get location info
		 */
		else if (resultCode == 5) {
			provinceName = data.getStringExtra("proviceName");
			String cityName = data.getStringExtra("cityName");
			String areaName = "";
			if (data.getStringExtra("areaName") != null) {
				areaName = data.getStringExtra("areaName");
			}
			uploadChange(address, provinceName + " " + cityName + " " + areaName);
		}
		/**
		 * get birth
		 */
		else if (resultCode == 6) {
			mYear = data.getStringExtra("year");
			mMonth = data.getStringExtra("month");
			mDay = data.getStringExtra("day");
			String mDate = mYear + "-" + mMonth + "-" + mDay;
			uploadChange(birth, mDate);
		}
		/**
		 * select photo from camera
		 */
		else if (requestCode == SELECT_CAMERA) {
            startPhotoZoom(Uri.fromFile(tempFile));
		}
		//super.onActivityResult(requestCode, resultCode, data);
	}

    /**
     * select photo dialog
     */
	public void showSelectPhotoDialog() {
		final AlertDialog photoDialog;
		photoDialog = new AlertDialog.Builder(this).create();
		photoDialog.show();
		photoDialog.getWindow().setContentView(R.layout.person_select_photo);
		RelativeLayout rl_camera, rl_photo_album, rl_cancel;
		rl_camera = (RelativeLayout) photoDialog.findViewById(R.id.rl_camer);
		rl_photo_album = (RelativeLayout) photoDialog.findViewById(R.id.rl_photo_album);
		rl_cancel = (RelativeLayout) photoDialog.findViewById(R.id.rl_cancle);
		rl_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), SELECT_CAMER);
                getImageFromCamera();
				photoDialog.dismiss();
			}
		});
		rl_photo_album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//startActivityForResult(new Intent(PersonMessageActivity.this,SelectPhotosActivity.class), SELECT_PICTURE);
				getImageFromGallery();
                photoDialog.dismiss();
			}
		});
		rl_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photoDialog.dismiss();
			}
		});
	}

    /**
     * select sex
     */
	private void showSelectSexDialog() {
		final AlertDialog sexDialog;
		sexDialog = new AlertDialog.Builder(this).create();
		sexDialog.show();
		sexDialog.getWindow().setContentView(R.layout.person_select_sex_dialog);
		final Button rl_boy, rl_girl, rl_cancle;
		rl_boy = (Button) sexDialog.findViewById(R.id.female);
		rl_girl = (Button) sexDialog.findViewById(R.id.male);
		if(SPUtils.get(this, "sex", "男").toString().equals("1")){
			rl_boy.setBackgroundResource(R.drawable.new_sex_checked);
			rl_girl.setBackgroundResource(R.drawable.new_sex_unchecked);
		}else {
			rl_boy.setBackgroundResource(R.drawable.new_sex_unchecked);
			rl_girl.setBackgroundResource(R.drawable.new_sex_checked);
		}
		rl_boy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadChange(user_sex, "1");
				rl_boy.setBackgroundResource(R.drawable.new_sex_checked);
				rl_girl.setBackgroundResource(R.drawable.new_sex_unchecked);
				sexDialog.dismiss();
			}
		});
		rl_girl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadChange(user_sex, "2");
				rl_boy.setBackgroundResource(R.drawable.new_sex_unchecked);
				rl_girl.setBackgroundResource(R.drawable.new_sex_checked);
				sexDialog.dismiss();
			}
		});
		rl_cancle = (Button) sexDialog.findViewById(R.id.rl_cancel);
		rl_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sexDialog.dismiss();
			}
		});
	}

    /**
     * select subject
     */
	private void showSelectSubjectDialog() {
		final AlertDialog subjectDialog;
		subjectDialog = new AlertDialog.Builder(this).create();
		subjectDialog.show();
		subjectDialog.getWindow().setContentView(R.layout.person_select_sciences);
		final Button rl_liberal_arts,rl_science_subject,rl_cancle;
		rl_liberal_arts = (Button) subjectDialog.findViewById(R.id.messy);
		rl_science_subject = (Button) subjectDialog.findViewById(R.id.math);
		rl_cancle = (Button) subjectDialog.findViewById(R.id.rl_cancel);
		if(SPUtils.get(this, "type", "理科").toString().equals("1")){
			rl_science_subject.setBackgroundResource(R.drawable.new_sex_checked);
			rl_liberal_arts.setBackgroundResource(R.drawable.new_sex_unchecked);
		}else {
			rl_science_subject.setBackgroundResource(R.drawable.new_sex_unchecked);
			rl_liberal_arts.setBackgroundResource(R.drawable.new_sex_checked);
		}
		rl_liberal_arts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rl_science_subject.setBackgroundResource(R.drawable.new_sex_unchecked);
				rl_liberal_arts.setBackgroundResource(R.drawable.new_sex_checked);
				uploadChange(examinatio_type, "文科");
				subjectDialog.dismiss();
			}
		});
		rl_science_subject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rl_science_subject.setBackgroundResource(R.drawable.new_sex_checked);
				rl_liberal_arts.setBackgroundResource(R.drawable.new_sex_unchecked);
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

    /**
     * select score
     */
	private void showSelectScore() {
		final AlertDialog scoreDialog;
		scoreDialog = new AlertDialog.Builder(this).create();
		scoreDialog.setView(new EditText(this));
		scoreDialog.show();
		scoreDialog.getWindow().setContentView(R.layout.person_score_dialog);
		final TextView input_error_hint = (TextView) scoreDialog.findViewById(R.id.input_error_hint);
		final EditText ed_score = (EditText) scoreDialog.findViewById(R.id.ed_score);
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
				if (checkInputScoreIsValid(editScore, provinceName, input_error_hint)) {
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

    /**
     * select year
     */
	private void showSelectYearDialog() {
		final AlertDialog scoreDialog;
		scoreDialog = new AlertDialog.Builder(this).create();
		scoreDialog.show();
		scoreDialog.getWindow().setContentView(R.layout.person_select_examinaton_year_dialog);
		Button confirm,cancel;
        final WheelView year;
        confirm=(Button)scoreDialog.findViewById(R.id.btn_ok);
        cancel=(Button)scoreDialog.findViewById(R.id.btn_cancel);
        year=(WheelView)scoreDialog.findViewById(R.id.id_year);

        final String years[]={"2016","2017","2018"};
        year.setViewAdapter(new ArrayWheelAdapter<String>(this, years));
        year.setCurrentItem(1);

        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadChange(user_year, years[year.getCurrentItem()]);
                scoreDialog.dismiss();
            }
        });
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				scoreDialog.dismiss();
			}
		});
	}

    /**
     * check value whether is valid
     * @param score
     * @param whichProvince
     * @param hint
     * @return
     */
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

    /**
     * upload change of user's info
     * @param whichChange
     * @param changeBody
     */
	public void uploadChange(final int whichChange, final String changeBody) {
		Log.d("zhuchao",String.valueOf(whichChange));
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					String body = changeBody;
					String which = null;
					switch (whichChange) {
						case img_head:
							which="headpic";
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
					String changeUserInfoUrl = String.format(Const.CHANGE_USER_INFO, User.getUID(PersonMessageActivity.this), User.getLoginToken(PersonMessageActivity.this));
					JSONObject obj = HttpUtil.getRequestJson(changeUserInfoUrl, entity);
					Integer errcode = obj.getInteger("errcode");
                    Message msg = handler.obtainMessage();
					msg.what = whichChange;
					msg.obj = changeBody;
					if (errcode == 0) {
						handler.sendMessage(msg);
					}else{
						handler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(0);
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


    /**
     * get image from gallery
     */
    private void getImageFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, SELECT_PICTURE);
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
        startActivityForResult(intent,SELECT_CAMERA);
    }
    // 使用系统当前日期加以调整作为照片的名称

    /**
     * set name of temporary file
     * @return
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * zoom image from gallery and camera
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
        startActivityForResult(intent, CROP_IMAGE);
    }

    /**
     * upload file to server
     * @param date
     */
    private void getHead(Intent date){
        if(date!=null){
            Bundle extras = date.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                if(photo!=null){
                    Date d=new Date(System.currentTimeMillis());
                    @SuppressWarnings("deprecation")
                    final String path=String.valueOf(d.getYear())+String.valueOf(d.getMonth())+String.valueOf(d.getDay())+String.valueOf(d.getHours())+String.valueOf(d.getMinutes())+String.valueOf(d.getSeconds())+".jpg";
                    ImageProcess.InputImage(photo, ImageProcess.FileType_Image.HeadImage, path);
                    if(Network.checkNetWorkState(this)){
                        headImage.setImageBitmap(photo);
                        new Thread(new Runnable() {
                            @SuppressLint("SdCardPath")
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                String result= NetworkFunction.UploadHeadImage(PersonMessageActivity.this, path, "/sdcard/luquba/HeadImages/" + path);
                                Log.d("zhuchao",result);
                                if(result.contains("http:")&&result.contains(".jpg")){
									JSONObject obj=JSONObject.parseObject(result);
                                    Message message=new Message();
                                    message.obj=obj.getString("data");
                                    message.what=20;
                                    handler.sendMessage(message);
                                }

                            }
                        }).start();
                    }else{
                        Toast.makeText(this, "网络出错", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "未选择图片", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "未选择图片", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(this, "未选择图片", Toast.LENGTH_LONG).show();
        }
    }
}
