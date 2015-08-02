package cn.luquba678.service;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.luquba678.entity.Const;
import cn.luquba678.entity.MatriculateMsg;

/**
 * 
 * @author Collin
 *
 */
public class MatriculateMsgService {

	public static LoadDataFromServer getMatriculateMsgListLoadDataFromServer(
			String stu_area_id, String school_area_id, String kelei,
			String grade) {
		MultipartEntity entity = new MultipartEntity();
		try {
			entity.addPart("stu_area_id",
					new StringBody(stu_area_id, Charset.forName("UTF-8")));
			entity.addPart("school_area_id", new StringBody(school_area_id,
					Charset.forName("UTF-8")));
			entity.addPart("kelei",
					new StringBody(kelei, Charset.forName("UTF-8")));
			entity.addPart("grade",
					new StringBody(grade, Charset.forName("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new LoadDataFromServer(Const.QUERY_SCHOOL, entity);
	}
}
