package cn.luquba678.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EntityBase<T> {
	public ArrayList<T> getListFromJson(String jsonData) {

		Class c = this.getClass();
		Type listType = new TypeToken<ArrayList<T>>() {
		}.getType();
		Gson gson = new Gson();

		ArrayList<T> queryList = gson.fromJson(jsonData, listType);
		return queryList;
	}

	public List<T> JsonStrToObjectList(T jsonStr, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonStr.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(JsonStrToObject((T) jsonArray.get(i), clazz));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public T JsonStrToObject(T jsonStr, Class<T> clazz) {
		T t = null;

		return t;
	}

}
