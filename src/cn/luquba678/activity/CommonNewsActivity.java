package cn.luquba678.activity;

import cn.luquba678.utils.Until;
import cn.luquba678.view.PullToRefreshListView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;
import com.zhuchao.adapter.CommentAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;

public class CommonNewsActivity extends CommonActivity implements OnClickListener, OnRefreshListener<ListView> {

	private TextView praise_time_tv, comment_time_tv;
	private WebView story_content_web;
	private View comment_input;
	private View buttom_btns;
	private int type;
	private int id;
	private View activityRootView;
	private View comment_container;
	private PullToRefreshListView ptrlv;


    ArrayList<News> newsList;
    CommentAdapter adapter;
    private ImageView collection;
    private ImageView praise;
    ListView commentList;

	private String url,title,imageUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_detail_news_page);
		
		initView();

		getMSG();
		getCommentList(page);

	}
    /**
     * init view
     */
    private void initView(){

        ptrlv= getView(R.id.comment_scroll_view);
        // 设置下拉刷新可用
        ptrlv.setPullRefreshEnabled(false);
        // 设置上拉加载可用
        ptrlv.setPullLoadEnabled(true);
        // 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
        commentList = ptrlv.getRefreshableView();
        ptrlv.setOnRefreshListener(this);

        LinearLayout container = (LinearLayout) View.inflate(self, R.layout.detail_container, null);

        comment_container = container.findViewById(R.id.comment_container);
        activityRootView = findViewById(R.id.root_view);
        //activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);
        collection = getView(R.id.ic_collect);
        praise = getView(R.id.ic_praise);
        /**
         * load content of article
         */
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 1);
        type = intent.getIntExtra("type", 0);
		title=intent.getStringExtra("title");
		imageUrl=intent.getStringExtra("imageUrl");

        story_content_web = (WebView) container.findViewById(R.id.story_content_web);
        url = String.format(Const.STORY_DETAIL, id, type);
        story_content_web.getSettings().setJavaScriptEnabled(true);
        story_content_web.setWebViewClient(new StoryWebView());
        // 加载需要显示的网页
        story_content_web.loadUrl(url);
        container.setFocusable(false);
        container.setClickable(false);


        setOnClickLinstener(R.id.share, R.id.collection, R.id.good, R.id.comment, R.id.send_comment);
        buttom_btns = findViewById(R.id.buttom_btns);
        comment_input = findViewById(R.id.comment_input);

        /**
         * init list view of comment
         */
        newsList=new ArrayList<News>();
        adapter=new CommentAdapter(newsList,this,container);
        commentList.setAdapter(adapter);
    }


    /**
     * get comment list
     * @param page
     */
	private void getCommentList(int page) {
		String comment_list_url = String.format(Const.COMMENT_LIST_URL, User.getUID(self), User.getLoginToken(self), id, type, page);
		try {
			HttpUtil.getRequestJsonRunnable(comment_list_url, null,
					new Handler() {
						public void handleMessage(Message msg) {
							JSONObject json = JSONObject.parseObject(msg.obj.toString());
							int errcode = json.getIntValue("errcode");
							if (errcode == 0) {
								JSONArray array = json.getJSONArray("data");
								ArrayList<News> arrayList = News.getListFromJson(array.toJSONString());
                                if(arrayList!=null){
                                    newsList.addAll(arrayList);
                                    adapter.notifyDataSetChanged();
                                    comment_container.setVisibility(View.VISIBLE);
                                }
							} else if(errcode==40004){
                                toast("No more data");
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

    /**
     * get message
     */
	public void getMSG() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					String url = String.format(Const.GET_DETAIL_MSG_URL, User.getUID(self), User.getLoginToken(self), id, type);
					JSONObject obj = HttpUtil.getRequestJson(url, null);
					handler.sendMessage(handler.obtainMessage(0, obj));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

    /**
     * process current data
     */
	private Handler handler = new Handler() {

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
	EditText comment_text;
	private InputMethodManager imm;
	private int page=1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			this.finish();
			break;

		case R.id.share:
			Until.showShare(CommonNewsActivity.this,handler,title,url,imageUrl);
            //showShare();
			break;
		case R.id.collection:
			String add_collection_url = String.format(Const.ADD_COLLECTION_URL,
					User.getUID(self), User.getLoginToken(self), id, type);
			try {
				HttpUtil.getRequestJsonRunnable(add_collection_url, null,
						new Handler() {
							public void handleMessage(Message msg) {
								JSONObject obj = JSONObject.parseObject(msg.obj.toString());
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
								JSONObject obj = JSONObject.parseObject(msg.obj.toString());
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
            comment_text.setText("");
			comment_text.setFocusable(true);
			comment_text.setFocusableInTouchMode(true);
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			comment_text.requestFocus();
			imm.showSoftInput(comment_text, InputMethodManager.RESULT_SHOWN);
			break;
		case R.id.send_comment:
			sendComment();
			break;
		}
	}

	/**
	 * send comment to server
	 */
	private void sendComment() {
		String comment = comment_text.getText().toString();
		if(StringUtils.isEmpty(comment))
		{
			toast("评论不能为空！");
			return;
		}
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
		buttom_btns.setVisibility(View.VISIBLE);
		comment_input.setVisibility(View.GONE);
		String comment_url = String.format(Const.COMMENT_URL, User.getUID(self), User.getLoginToken(self), id, type);
		try {
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("content", new StringBody(comment, Charset.forName("utf-8")));
			HttpUtil.getRequestJsonRunnable(comment_url, entity, new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					try {

                        String result=msg.obj.toString();
                        JSONObject object=JSONObject.parseObject(result);
						int code = object.getIntValue("errcode");
						if(code==0){
							toast("评论成功！");
                            //refresh comment number;
							getMSG();

                            News news=new News();
                            JSONObject jsonObject= object.getJSONObject("data");
                            news.setContent(jsonObject.getString("content"));
                            news.setCreatetime(jsonObject.getString("createtime"));
                            news.setHeadpic(jsonObject.getString("headpic"));
                            news.setNickname(jsonObject.getString("nickname"));

                            newsList.add(0, news);
                            adapter.notifyDataSetChanged();
						}else{
							toast("评论失败!");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					}});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public static void intentToDetailNews(News news, Context context, int type) {
		Intent intent = new Intent(context, CommonNewsActivity.class);
		intent.putExtra("title", news.getTitle());
		intent.putExtra("imageUrl",news.getPic());
		intent.putExtra("content", news.getUrl());
		intent.putExtra("oncreatetime", news.getCreatetime());
		intent.putExtra("origin", news.getOrigin());
		intent.putExtra("id", news.getId());
		intent.putExtra("type", type);
		context.startActivity(intent);
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

        getCommentList(++page);
		
	}

	class StoryWebView extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

    /**
     * share to other app
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
