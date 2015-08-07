package cn.luquba678.utils;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.PlatformListFakeActivity.OnShareButtonClickListener;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class Until {
	public static void showShare(final Context context, final Handler mHandler) {
		// ShareSDK.initSDK(context);
		final OnekeyShare oks = new OnekeyShare();
		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("录取吧");
		oks.setTitleUrl("http://mob.com");
		// if(imgurlStrings != null){
		// oks.setImageArray(imgurlStrings);
		// imgurlStrings = null;
		// }

		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://www.baidu.com");
		// text是分享文本，所有平台都需要这个字段
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/storage/emulated/0/MIUI/wallpaper/大白 (3)_&_e06b62cf-e12a-4096-9393-efd200e4165d.jpg");//
		// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		// oks.setUrl("http://lkgn.com");

		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("录取吧");
		// site是分享此内容的网站名称，仅在QQ空间使用
		// oks.setSite(context.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		// oks.setSiteUrl("http://lkgn.com");
		//
		oks.setOnShareButtonClickListener(new OnShareButtonClickListener() {

			@Override
			public void onClick(View v, List<Object> checkPlatforms) {
				String name = ((Platform) checkPlatforms.get(0)).getName();

				if (name.equals(Wechat.NAME)) {
				} else if (name.equals(WechatMoments.NAME)) {
				} else {
				}
				oks.setText("录取吧！！！");
			}
		});
		oks.setCallback(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				Log.i("wyb_test", "onError");
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {

			}
		});
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 启动分享GUI
		oks.show(context);
	}
	public static void showShare(final Context context, final Handler mHandler, final String title, final String link, final String imageUrl){
        ShareSDK.initSDK(context);
		final OnekeyShare oks = new OnekeyShare();
		oks.setTitle("录取吧");
		oks.setComment(title);
        oks.setTitleUrl(link);
        oks.setText(title);
        if(imageUrl!=null)
            oks.setImageUrl(imageUrl);

		oks.setOnShareButtonClickListener(new OnShareButtonClickListener() {

			@Override
			public void onClick(View v, List<Object> checkPlatforms) {
                Platform platform=(Platform) checkPlatforms.get(0);
				String name = platform.getName();
				if (name.equals(Wechat.NAME)) {

				} else if (name.equals(WechatMoments.NAME)) {

				} else{

				}
			}
		});
		oks.setCallback(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				Log.i("wyb_test", "onError");
			}

			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {

			}
		});
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 启动分享GUI
		oks.show(context);
	}
}