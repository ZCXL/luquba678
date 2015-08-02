package cn.luquba678.activity;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import cn.luquba678.R;
import cn.luquba678.activity.adapter.CommentAdapter;
import cn.luquba678.activity.adapter.StoryAdapter;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.utils.ImageLoader;
import cn.luquba678.utils.ImageUtil;
import cn.luquba678.utils.ScreenUtils;
import cn.luquba678.view.ImgScrollViewPager;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;

public class FunnyDetailActivity extends CommonActivity implements
		OnClickListener, OnGlobalLayoutListener, OnRefreshListener<ListView> {
	cn.luquba678.view.PullToRefreshListView ptrlv;
	private ListView listview_stories;
	private ArrayList<View> listViews;
	private ImageLoader mImageLoader;
	private ImgScrollViewPager mmPager;
	private LinearLayout ovalLayout;
	private int type;
	private int id;
	private EditText comment_text;
	private InputMethodManager imm;
	private View buttom_btns;
	private View comment_input;
	private View activityRootView;
	private View comment_container;

	public String getType(int i) {
		String type = "";
		switch (i) {
		case 4:
			type = "校园趣事";
			break;
		case 5:
			type = "搞笑段子";
			break;
		case 6:
			type = "内涵图";
			break;
		}
		return type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_detail_news_page);

		ptrlv = getView(R.id.comment_scroll_view);
		// 设置当前上拉加载不可用
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(false);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		commentList = ptrlv.getRefreshableView();
		ptrlv.setOnRefreshListener(this);
		LinearLayout container = (LinearLayout) View.inflate(self,
				R.layout.detail_funny, null);
		commentList.addHeaderView(container);
		comment_container = container.findViewById(R.id.comment_container);
		buttom_btns = findViewById(R.id.buttom_btns);
		comment_input = findViewById(R.id.comment_input);
		// comment_container = findViewById(R.id.comment_container);
		collection = getView(R.id.ic_collect);
		praise = getView(R.id.ic_praise);
		setOnClickLinstener(R.id.share, R.id.collection, R.id.good,
				R.id.comment, R.id.send_comment, R.id.back_button);
		Intent intent = getIntent();
		String content = intent.getStringExtra("content");
		id = intent.getIntExtra("id", 0);
		final String oncreatetime = intent.getStringExtra("oncreatetime");
		final String origin = intent.getStringExtra("origin");
		type = intent.getIntExtra("type", 4);
		final String title = intent.getStringExtra("title");
		ImageView iv = (ImageView) container.findViewById(R.id.image);
		try {
			final String image = intent.getStringExtra("image");
			if (StringUtils.isNotEmpty(image)) {
				Bitmap bm = ImageLoader.getBitmap(image);
				bm = ImageUtil.zoomBitmapWith(bm,
						ScreenUtils.getScreenWidth(self));
				iv.setOnClickListener(this);
				iv.setImageBitmap(bm);
			}
		} catch (Exception e) {
		}
		// getImageLoader().DisplayImage(image, iv, false);
		((TextView) container.findViewById(R.id.story_title)).setText(title);
		((TextView) container.findViewById(R.id.story_content))
				.setText(content);
		((TextView) container.findViewById(R.id.leibie)).setText(getType(type));
		((TextView) container.findViewById(R.id.origin)).setText(origin);
		((TextView) container.findViewById(R.id.createtime))
				.setText(oncreatetime);
		getMSG();
		activityRootView = findViewById(R.id.root_view);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
		// commentList = getView(R.id.commentlistview);
		getcomentlist(1, 0);
		// ObjectAnimator.ofFloat(listview_stories,
		// "x",0f,200f).setDuration(1000l).start();
	}

	private void getcomentlist(int page, final int action) {
		String comment_list_url = String.format(Const.COMMENT_LIST_URL,
				User.getUID(self), User.getLoginToken(self), id, type, 1);
		try {
			HttpUtil.getRequestJsonRunnable(comment_list_url, null,
					new Handler() {

						public void handleMessage(Message msg) {
							JSONObject json = JSONObject.parseObject(msg.obj
									.toString());
							int errcode = json.getIntValue("errcode");
							if (errcode == 0) {
								JSONArray arry = json.getJSONArray("data");
								ArrayList<News> arryList = News
										.getListFromJson(arry.toJSONString());
								switch (action) {
								case CHANGE:
									newsList = arryList;
									break;

								default:
									if (newsList != null)
										newsList.addAll(arryList);
									else
										newsList = arryList;
									break;
								}
								if (adapter == null) {
									adapter = new CommentAdapter(self,
											arryList, R.layout.comment_cell);
									commentList.setAdapter(adapter);
								} else {
									adapter.changeDateInThread(arryList);
								}
								comment_container.setVisibility(View.VISIBLE);

							} else {
								comment_container.setVisibility(View.GONE);
							}
							ptrlv.onPullDownRefreshComplete();
							ptrlv.onPullUpRefreshComplete();
							ptrlv.setHasMoreData(true);
						}

					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	ArrayList<News> newsList;
	CommentAdapter adapter;
	ListView commentList;

	@Override
	public void onGlobalLayout() {

		// 比较Activity根布局与当前布局的大小
		int heightDiff = activityRootView.getRootView().getHeight()
				- activityRootView.getHeight();
		if (heightDiff > 200) {

			buttom_btns.setVisibility(View.GONE);
			comment_input.setVisibility(View.VISIBLE);
		} else {
			buttom_btns.setVisibility(View.VISIBLE);
			comment_input.setVisibility(View.GONE);
			// 大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			this.finish();
			break;

		case R.id.share:
			toast("分享");
			break;
		case R.id.collection:
			String add_collection_url = String.format(Const.ADD_COLLECTION_URL,
					User.getUID(self), User.getLoginToken(self), id, type);
			try {
				HttpUtil.getRequestJsonRunnable(add_collection_url, null,
						new Handler() {
							public void handleMessage(Message msg) {
								JSONObject obj = JSONObject.parseObject(msg.obj
										.toString());
								int errcode = obj.getIntValue("errcode");
								if (errcode == 0) {
									getMSG();
								}
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.good:
			String praise_url = String.format(Const.PRAISE_URL,
					User.getUID(self), User.getLoginToken(self), id, type);
			try {
				HttpUtil.getRequestJsonRunnable(praise_url, null,
						new Handler() {
							public void handleMessage(Message msg) {
								JSONObject obj = JSONObject.parseObject(msg.obj
										.toString());
								int errcode = obj.getIntValue("errcode");
								if (errcode == 0) {
									getMSG();
								}
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.comment:

			buttom_btns.setVisibility(View.GONE);
			comment_input.setVisibility(View.VISIBLE);
			comment_text = getView(R.id.comment_text);
			comment_text.setFocusable(true);
			comment_text.setFocusableInTouchMode(true);
			comment_text.requestFocus();
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(comment_text, InputMethodManager.RESULT_SHOWN);
			break;
		case R.id.send_comment:
			sendComment();
			break;
		}
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_ENTER:
			sendComment();

			break;

		default:
			break;
		}
		return super.dispatchKeyEvent(event);
	}

	private void sendComment() {
		String comment = comment_text.getText().toString();
		if (StringUtils.isEmpty(comment)) {
			toast("评论不能为空！");
			return;
		}
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
		buttom_btns.setVisibility(View.VISIBLE);
		comment_input.setVisibility(View.GONE);
		String comment_url = String.format(Const.COMMENT_URL,
				User.getUID(self), User.getLoginToken(self), id, type);
		try {
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("content",
					new StringBody(comment, Charset.forName("utf-8")));
			HttpUtil.getRequestJsonRunnable(comment_url, entity, new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					int code = JSONObject.parseObject(msg.obj.toString())
							.getIntValue("errcode");
					if (code == 0) {
						toast("评论成功！");
						getMSG();
						getcomentlist(1, 0);
					} else {
						toast("评论失败!");
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getMSG() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					String url = String.format(Const.GET_DETAIL_MSG_URL,
							User.getUID(self), User.getLoginToken(self), id,
							type);
					Log.i("url", url);
					JSONObject obj = HttpUtil.getRequestJson(url, null);
					handler.sendMessage(handler.obtainMessage(0, obj));
					/*
					 * if (errcode == 0) { }
					 */
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	private ImageView praise;
	private ImageView collection;
	private Handler handler = new Handler() {

		private TextView praise_time_tv;
		private TextView comment_time_tv;

		@Override
		public void handleMessage(Message msg) {
			try {
				String json = msg.obj.toString();
				JSONObject obj = JSONObject.parseObject(json);
				int code = obj.getIntValue("errcode");

				if (code == 0) {

					JSONObject date = obj.getJSONObject("data");

					String praise_time = date.getString("praise_time");
					String comment_time = date.getString("comment_time");
					int is_praise = date.getIntValue("is_praise");
					int is_collect = date.getIntValue("is_collect");
					praise_time_tv = getView(R.id.praise_time);
					comment_time_tv = getView(R.id.comment_time);
					praise_time_tv.setText(praise_time);
					comment_time_tv.setText(comment_time);
					if (is_praise != 0) {
						praise.setImageResource(R.drawable.ic_praised);
					} else {
						praise.setImageResource(R.drawable.ic_praise);

					}
					if (is_collect != 0) {
						collection.setImageResource(R.drawable.ic_collected);
					} else {
						collection.setImageResource(R.drawable.ic_collection);
					}
				}
			} catch (Exception e) {
			}
		}
	};

	public static void intentToDetailNews(News news, Context context, int type) {
		Intent intent = new Intent(context, FunnyDetailActivity.class);
		intent.putExtra("title", news.getTitle());
		intent.putExtra("content", news.getContent());
		intent.putExtra("oncreatetime", news.getCreatetime());
		intent.putExtra("origin", news.getOrigin());
		intent.putExtra("id", news.getId());
		intent.putExtra("image", news.getPic());
		intent.putExtra("type", type);
		context.startActivity(intent);

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	private int page = 1;

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		getcomentlist(++page, ADD);

	}
}
