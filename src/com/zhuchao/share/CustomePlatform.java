package com.zhuchao.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;

/**
 * Created by zhuchao on 8/9/15.
 */
public class CustomePlatform extends CustomPlatform {
    public final static String NAME=CustomePlatform.class.getName();


    public CustomePlatform(Context context){
        super(context);
    }
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected boolean checkAuthorize(int i, Object o) {
        return isValid();
    }

    /**
     * check valid
     * @return
     */
    public boolean isValid() {
        return isClientInstalled();
    }
    private boolean isClientInstalled() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setPackage("com.alibaba.android.babylon");
        i.setType("image/*");
        PackageManager pm = getContext().getPackageManager();
        List<?> ris = pm.queryIntentActivities(i, PackageManager.GET_ACTIVITIES);
        return ris != null && ris.size() > 0;
    }

    @Override
    protected void doShare(Platform.ShareParams params) {
        ShareParams sp = new ShareParams(params.toMap());
        Intent i = new Intent(Intent.ACTION_SEND);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage("com.alibaba.android.babylon");
        String imagePath = sp.getImagePath();
        String text = sp.getText();
        if (!TextUtils.isEmpty(imagePath)
                && (new File(imagePath).exists())) {
            Uri uri = Uri.fromFile(new File(imagePath));
            i.putExtra(Intent.EXTRA_STREAM, uri);
            i.setType("image/*");
            try {
                getContext().startActivity(i);
                if (listener != null) {
                    HashMap<String, Object> res = new HashMap<String, Object>();
                    res.put("ShareParams", params);
                    listener.onComplete(this, ACTION_SHARE, res);
                }
            } catch (Throwable t) {
                listener.onError(this, ACTION_SHARE, t);
            }
        } else if (!TextUtils.isEmpty(text)) {
            i.putExtra(Intent.EXTRA_TEXT, text);
            i.setType("text/plain");
            try {
                getContext().startActivity(i);
                if (listener != null) {
                    HashMap<String, Object> res = new HashMap<String, Object>();
                    res.put("ShareParams", params);
                    listener.onComplete(this, ACTION_SHARE, res);
                }
            } catch (Throwable t) {
                listener.onError(this, ACTION_SHARE, t);
            }
        } else if (listener != null) {
            listener.onError(this, ACTION_SHARE, new Throwable("Share content is empty!"));
        }
    }
    public static class ShareParams extends Platform.ShareParams {

        public ShareParams() {
            super();
        }

        public ShareParams(HashMap<String, Object> params) {
            super(params);
        }

        public ShareParams(String jsonParams) {
            super(jsonParams);
        }

        public void setText(String text) {
            set(ShareParams.TEXT, text);
        }

        public String getText() {
            return get(ShareParams.TEXT, String.class);
        }

        public void setImagePath(String imagePath) {
            set(ShareParams.IMAGE_PATH, imagePath);
        }

        public String getImagePath() {
            return get(ShareParams.IMAGE_PATH, String.class);
        }

    }
}
