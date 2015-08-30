package com.zhuchao.share;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.List;

import cn.luquba678.R;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.PlatformListFakeActivity;

/**
 * Created by zhuchao on 8/9/15.
 */
public class ShareInit {
    /**
     * init share sdk
     * @param context
     */
    public static void initSDK(Context context){
        ShareSDK.initSDK(context);
        ShareSDK.registerPlatform(CustomePlatform.class);
        ShareSDK.setConnTimeout(2000);
        ShareSDK.setReadTimeout(2000);
    }
    public static void showShare(boolean silent, String platform,final Context context,String title,String url,String text,String imageUrl) {
        final OnekeyShare oks = new OnekeyShare();

        Log.d("zhuchao",text);
        oks.setTitle(title);
        oks.setTitleUrl(url);
        oks.setText(text);

        oks.setUrl(url);
        oks.setComment(text);
        oks.setSite("录取吧");
        oks.setSiteUrl(url);
        oks.setVenueName("Luquba");
        oks.setVenueDescription(text);
        oks.setSilent(silent);
        oks.setShareFromQQAuthSupport(false);
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        oks.setImageUrl(imageUrl);
        if (platform != null) {
            oks.setPlatform(platform);
        }
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();

        // 在自动授权时可以禁用SSO方式
        //if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
        oks.disableSSOWhenAuthorize();


        oks.show(context);
    }
}
