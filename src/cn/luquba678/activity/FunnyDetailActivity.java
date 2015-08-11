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
import com.zhuchao.share.ShareInit;
import com.zhuchao.utils.ImageLoader;
import com.zhuchao.view_rewrite.LoadingDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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

public class FunnyDetailActivity extends CommonActivity implements OnClickListener,OnRefreshListener<ListView> {
	private PullToRefreshListView ptrlv;
	private EditText comment_text;
	private InputMethodManager imm;
	private View bottom_btns;
	private View comment_input;
	private View comment_container;

    private ArrayList<Comment> commentArrayList;
    private CommentAdapter adapter;
    private ListView commentList;
	private int page=1;

	private boolean isCollect;

	private LoadingDialog loadingDialog;
	private News news;

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

		ShareInit.initSDK(FunnyDetailActivity.this);
		loadingDialog=new LoadingDialog(FunnyDetailActivity.this);
		ptrlv = getView(R.id.comment_scroll_view);
		// 设置当前上拉加载不可用
		// 设置下拉刷新可用
		ptrlv.setPullRefreshEnabled(false);
		// 设置上拉加载可用
		ptrlv.setPullLoadEnabled(true);
		// 滑到底部是否自动加载数据，这句话一定要加要不然"已经到底啦"显示不出来
		commentList = ptrlv.getRefreshableView();
		ptrlv.setOnRefreshListener(this);
		LinearLayout container = (LinearLayout) View.inflate(self, R.layout.detail_funny, null);
		comment_container = container.findViewById(R.id.comment_container);

        /**
         * comment block
         */
		bottom_btns = findViewById(R.id.buttom_btns);
		comment_input = findViewById(R.id.comment_input);
		collection = getView(R.id.ic_collect);
		praise = getView(R.id.ic_praise);
		setOnClickLinstener(R.id.share, R.id.collection, R.id.good, R.id.comment, R.id.send_comment, R.id.back_button);

        /**
         * get data from last activity
         */
		news=getIntent().getExtras().getParcelable("news");
        /**
         * set value
         */
        ((TextView) container.findViewById(R.id.story_title)).setText(news.getTitle());
        ((TextView) container.findViewById(R.id.story_content)).setText(Html.fromHtml(Html.fromHtml(news.getIntro()).toString()));
        //((TextView) container.findViewById(R.id.leibie)).setText(getType(type));
        ((TextView) container.findViewById(R.id.origin)).setText(news.getOrigin());
        ((TextView) container.findViewById(R.id.createtime)).setText(news.getCreatetime());

        /**
         * download image of picture
         */
		ImageView iv = (ImageView) container.findViewById(R.id.image);
		try {
			String imageUrl=news.getPic();
			if (StringUtils.isNotEmpty(imageUrl)&&imageUrl.contains("Upload")) {
                container.findViewById(R.id.story_content).setVisibility(View.GONE);
                new ImageLoader(this).DisplayImage(imageUrl,iv);
			}else{
                iv.setVisibility(View.GONE);
            }
		} catch (Exception e) {

		}

        /**
         * get info of comment number,collection number and so on.
         */
		getMSG();

        /**
         * init list view of comment
         */
        commentArrayList=new ArrayList<Comment>();
        adapter=new CommentAdapter(commentArrayList,this,container);
        commentList.setAdapter(adapter);
		getCommentList(page);
	}

    /**
     * get comment list
     * @param page
     */
    private void getCommentList(int page) {
        String comment_list_url = String.format(Const.COMMENT_LIST_URL, User.getUID(self), User.getLoginToken(self),news.getId(), page);
        try {
			loadingDialog.startProgressDialog();
            HttpUtil.getRequestJsonRunnable(comment_list_url, null, new Handler() {
                        public void handleMessage(Message msg) {
                            JSONObject json = JSONObject.parseObject(msg.obj.toString());
                            int errcode = json.getIntValue("errcode");
                            if (errcode == 0) {
                                JSONArray array = json.getJSONArray("data");
                                ArrayList<Comment> arrayList = Comment.getListFromJson(array.toJSONString());
                                if(arrayList!=null){
                                    commentArrayList.addAll(arrayList);
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


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			this.finish();
			break;
		case R.id.share:
			ShareInit.showShare(false,null,FunnyDetailActivity.this,news.getTitle(),news.getUrl(),Html.fromHtml(Html.fromHtml(news.getIntro()).toString()).toString(),news.getPic());
			break;
		case R.id.collection:
			collectOrNot();
			break;
		case R.id.good:
			recognise();
			break;
		case R.id.comment:

			bottom_btns.setVisibility(View.GONE);
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

	/**
	 * good
	 */
	private void recognise(){
		String praise_url = String.format(Const.PRAISE_URL, User.getUID(self), User.getLoginToken(self), news.getId());
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
			String add_collection_url = String.format(Const.ADD_COLLECTION_URL, User.getUID(self), User.getLoginToken(self),news.getId());
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
        bottom_btns.setVisibility(View.VISIBLE);
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

                            /**
                             * get new comment
                             */
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
						isCollect=true;
						collection.setImageResource(R.drawable.ic_collected);
					} else {
						isCollect=false;
						collection.setImageResource(R.drawable.ic_collection);
					}
				}
			} catch (Exception e) {
			}
		}
	};

	public static void intentToDetailNews(News news, Context context, int type) {
		Intent intent = new Intent(context, FunnyDetailActivity.class);
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
}
