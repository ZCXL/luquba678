package cn.luquba678.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.json.JSONObject;

import android.util.Log;

public class JsonUtil {

	/**
	 * 实体的值转成JSON对象的值
	 * 
	 * @param source
	 * @param dest
	 */
	public static void entityToJSON(Object source, JSONObject dest) {

		Class clzss = source.getClass();

		Field[] fields = clzss.getDeclaredFields();

		try {
			for (Field field : fields) {
				dest.put(field.getName(),
						getFieldValue(source, field.getName()));
			}
		} catch (Exception e) {
			Log.e("json", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取字段的值
	 * 
	 * @param data
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object data, String fieldName) {

		StringBuilder sb = new StringBuilder();

		Class clzss = data.getClass();

		// 将字段首字母大写
		String firstWord = fieldName.substring(0, 1).toUpperCase();
		sb.append(firstWord);
		sb.append(fieldName.substring(1, fieldName.length()));

		final String methodName = "get" + sb.toString();

		Method[] methods = clzss.getDeclaredMethods();
		try {
			for (Method method : methods) {
				// 调用对应的方法
				if (methodName.equals(method.getName())) {
					return method.invoke(data, new Object[] {});
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return null;

	}
}
