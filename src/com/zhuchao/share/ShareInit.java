package com.zhuchao.share;

import android.content.Context;

import cn.luquba678.R;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

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

        oks.setTitle(title);
        oks.setTitleUrl(url);
        if (text != null) {
            oks.setText(text);
        }  else {
            oks.setText(context.getString(R.string.app_name));
        }


        oks.setUrl(url);
        oks.setComment(text);
        oks.setSite(text);
        oks.setSiteUrl(url);
        oks.setVenueName("Luquba");
        oks.setVenueDescription(text);
        oks.setLatitude(23.056081f);
        oks.setLongitude(113.385708f);
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
