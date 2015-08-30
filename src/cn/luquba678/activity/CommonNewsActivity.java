package cn.luquba678.activity;

import cn.luquba678.entity.Comment;
import cn.luquba678.view.PullToRefreshListView;
import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;
import com.zhuchao.adapter.CommentAdapter;
import com.zhuchao.http.Network;
import com.zhuchao.share.ShareInit;
import com.zhuchao.view_rewrite.LoadingDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.luquba678.R;
import cn.luquba678.entity.Const;
import cn.luquba678.entity.News;
import cn.luquba678.entity.User;
import cn.luquba678.ui.HttpUtil;
import cn.luquba678.view.PullToRefreshBase;
import cn.luquba678.view.PullToRefreshBase.OnRefreshListener;

public class CommonNewsActivity extends CommonActivity implements OnClickListener, OnRefreshListener<ListView> {

	private WebView story_content_web;
	private View comment_input;
	private View buttom_btns;
	private View comment_container;
	private PullToRefreshListView ptrlv;

    private ArrayList<Comment> commentArrayList;
    private CommentAdapter adapter;
    private ImageButton collection,comment,praise,share;
    private ListView commentList;

    private Boolean isCollect;
	private News news;
	private LoadingDialog loadingDialog;
    private TextView comment_number,praise_number;

    private EditText comment_text;
    private InputMethodManager imm;
    private int page=1;
    private RelativeLayout error_layout;
    private Button refresh_button;
    /**
     * check page
     */
    private boolean isLoad=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_detail_news_page);
		ShareInit.initSDK(this);
		initView();

		loadData();
	}
    /**
     * init view
     */
    private void initView(){

		loadingDialog=new LoadingDialog(CommonNewsActivity.this);

        /**
         * network error bg
         */
        error_layout=(RelativeLayout)findViewById(R.id.network_error);
        refresh_button=(Button)findViewById(R.id.network_error_button);
        refresh_button.setOnClickListener(this);
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
        findViewById(R.id.back_button).setOnClickListener(this);
        collection =(ImageButton)findViewById(R.id.ic_collect);
        praise =(ImageButton)findViewById(R.id.ic_praise);
        comment=(ImageButton)findViewById(R.id.comment_icon);
        share=(ImageButton)findViewById(R.id.ic_share);
        collection.setOnClickListener(this);
        praise.setOnClickListener(this);
        comment.setOnClickListener(this);
        share.setOnClickListener(this);
		/**
		 * comment number and praise number
		 */
		comment_number=(TextView)findViewById(R.id.comment_number);
        praise_number=(TextView)findViewById(R.id.praise_number);
        /**
         * load content of article
         */
		news=getIntent().getExtras().getParcelable("news");

        story_content_web = (WebView) container.findViewById(R.id.story_content_web);
        story_content_web.getSettings().setJavaScriptEnabled(true);
        story_content_web.setWebViewClient(new StoryWebView());
        // 加载需要显示的网页

        container.setFocusable(false);
        container.setClickable(false);


        setOnClickLinstener(R.id.share, R.id.collection, R.id.good, R.id.comment, R.id.send_comment);
        buttom_btns = findViewById(R.id.buttom_btns);
        comment_input = findViewById(R.id.comment_input);

        /**
         * init list view of comment
         */
        commentArrayList=new ArrayList<Comment>();
        adapter=new CommentAdapter(commentArrayList,this,container);
        commentList.setAdapter(adapter);
    }


    private void loadData(){
        if(Network.checkNetWorkState(this)){
            if(error_layout.getVisibility()==View.VISIBLE)
                error_layout.setVisibility(View.INVISIBLE);
            loadingDialog.startProgressDialog();
            story_content_web.loadUrl(news.getUrl());
            getCommentList(page);
            getMSG();
        }else{
            Toast.makeText(CommonNewsActivity.this,"未连接网络",Toast.LENGTH_SHORT).show();
            if(error_layout.getVisibility()==View.INVISIBLE)
                error_layout.setVisibility(View.VISIBLE);
        }
    }
    /**
     * get comment list
     * @param page
     */
	private void getCommentList(int page) {
		String comment_list_url = String.format(Const.COMMENT_LIST_URL, User.getUID(self), User.getLoginToken(self),news.getId(), page);
		try {
			HttpUtil.getRequestJsonRunnable(comment_list_url, null,
					new Handler() {
						public void handleMessage(Message msg) {
							JSONObject json = JSONObject.parseObject(msg.obj.toString());
							int errcode = json.getIntValue("errcode");
							if (errcode == 0) {
								JSONArray array = json.getJSONArray("data");
								ArrayList<Comment> arrayList = new ArrayList<Comment>();
                                for(int i=0;i<array.size();i++)
                                    arrayList.add(new Comment(array.get(i).toString()));
                                if(arrayList!=null){
                                    commentArrayList.addAll(arrayList);
                                    adapter.notifyDataSetChanged();
                                    comment_container.setVisibility(View.VISIBLE);
                                }
							}else{
                                if(commentArrayList.size()>0)
                                    Toast.makeText(CommonNewsActivity.this,"亲,没有可刷新的了!",Toast.LENGTH_SHORT).show();
                            }
							ptrlv.onPullDownRefreshComplete();
							ptrlv.onPullUpRefreshComplete();
							ptrlv.setHasMoreData(true);
                            if(isLoad)
                                loadingDialog.stopProgressDialog();
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
					String url = String.format(Const.GET_DETAIL_MSG_URL, User.getUID(self), User.getLoginToken(self),news.getId());
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
					comment_number.setText(comment_time);
                    praise_number.setText(praise_time);
					if (is_praise != 0) {
						praise.setImageResource(R.drawable.new_good_checked);
					} else {
						praise.setImageResource(R.drawable.new_good_unchecked);
					}
					if (is_collect != 0) {
                        isCollect=true;
						collection.setImageResource(R.drawable.new_collect_checked);
					} else {
                        isCollect=false;
						collection.setImageResource(R.drawable.new_collect_unchecked);
					}
				}
			} catch (Exception e) {
			}
		}
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.network_error_button:
                loadData();
                break;
            case R.id.back_button:
                this.finish();
                break;
            case R.id.share:
                ShareInit.showShare(false,null,CommonNewsActivity.this,news.getTitle(),news.getUrl(),"这里有好文一篇["+news.getTitle()+"]别人我可不告诉哟!"+"\n"+news.getUrl(),news.getPic());
                break;
            case R.id.collection:
                collectOrNot();
                break;
            case R.id.good:
                recognise();
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
            case R.id.ic_share:
                ShareInit.showShare(false,null,CommonNewsActivity.this,news.getTitle(),news.getUrl(),"这里有好文一篇["+news.getTitle()+"]别人我可不告诉哟!"+"\n"+news.getUrl(),news.getPic());
                break;
            case R.id.ic_collect:
                collectOrNot();
                break;
            case R.id.ic_praise:
                recognise();
                break;
            case R.id.comment_icon:
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
		String comment_url = String.format(Const.COMMENT_URL, User.getUID(self), User.getLoginToken(self),news.getId());
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

                            Comment comment_result=new Comment();
                            JSONObject jsonObject= object.getJSONObject("data");
                            comment_result.setContent(jsonObject.getString("content"));
                            comment_result.setComment_time(jsonObject.getString("createtime"));
                            comment_result.setHeadpic(jsonObject.getString("headpic"));
                            comment_result.setNickname(jsonObject.getString("nickname"));

                            commentArrayList.add(0, comment_result);
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

    /**
     * good
     */
    private void recognise(){
        String praise_url = String.format(Const.PRAISE_URL,
                User.getUID(self), User.getLoginToken(self),news.getId());
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
    }
    /**
     * collect or cancel
     */
    private void collectOrNot(){
        if(!isCollect) {
            String add_collection_url = String.format(Const.ADD_COLLECTION_URL, User.getUID(self), User.getLoginToken(self), news.getId());
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
        }else{
            String deleteCollectionUrl = String.format(Const.DELETECOLLECTION, User.getUID(self), User.getLoginToken(self));
            MultipartEntity entity = new MultipartEntity();
            try {
                entity.addPart("list",new StringBody("[\""+news.getId()+"\"]"));
                JSONObject obj= HttpUtil.getRequestJson(deleteCollectionUrl, entity);
                int err_code=obj.getIntValue("errcode");
                if(err_code==0)
                    getMSG();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
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
		Bundle bundle=new Bundle();
		bundle.putParcelable("news",news);
        intent.putExtras(bundle);
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
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            isLoad=true;
            loadingDialog.stopProgressDialog();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
	}
}
