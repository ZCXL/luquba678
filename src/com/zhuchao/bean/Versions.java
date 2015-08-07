package com.zhuchao.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by zhuchao on 7/13/15.
 */
public class Versions extends BaseObjects{
    private static String TAG="VersionProcessJson";
    private ArrayList<BaseObject> versions;
    public Versions(String c){
        versions=getObjects(c);
    }
    public ArrayList<BaseObject> getObjects(String c) {
        /**
         * deal with String c for version
         * Created by LMZ on 7/13/15
         * modified by LMZ on 7/14/15
         */
        ArrayList<BaseObject> list = new ArrayList<BaseObject>();
        Version version;
        try {
            JSONObject object = new JSONObject(c);

            JSONArray jsonArray=object.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                version = new Version();
                version.setVersionId(jsonObject.getString("version"));
                version.setVersionDescription(jsonObject.getString("description"));
                version.setVersionUrl(jsonObject.getString("downloadurl"));
                String available=jsonObject.getString("available");
                if(available.equals("0")){
                    version.setAvailable(false);
                }else{
                    version.setAvailable(true);
                }
                list.add(version);
            }
            return list;
        } catch (JSONException e) {
            Log.d(TAG, e.toString() + "version of json error");
            return  null;
        }
    }
    /**
     * get versions' counts
     * @return int
     * Created by LMZ on 7/13/15
     */
    @Override
    public int getCount() {
        if (versions == null)
            return -1;
        else
            return versions.size();
    }
    /**
     * get instant version's item
     * @param index
     * @return
     * Created by LMZ on 7/13/15
     */
    @Override
    public BaseObject getItem(int index) {
        if (versions == null)
            return null;
        else
            return versions.get(index);
    }
}
