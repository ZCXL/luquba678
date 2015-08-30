package cn.luquba678.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.baidu.navisdk.util.common.StringUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class DateUtils {
	private final static int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23,
			23, 23, 24, 23, 22 };
	private final static String[] constellationArr = new String[] { "摩羯座",
			"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
			"天蝎座", "射手座", "摩羯座" };

	public final static long SECOND = 1000;
	public final static long MINUTE = SECOND * 60;
	public final static long HOUR = MINUTE * 60;
	public final static long DAY = HOUR * 24;

	/**
	 * 将毫秒数转换成对应时间格式
	 * 
	 * @param time
	 * @return
	 */
	public static String formatDate(String pattern, long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(time);
	}

	/**
	 * 将时间转换成毫秒
	 * 
	 * @param pattern
	 * @param time
	 * @return
	 */
	public static long getTimeMillis(String pattern, String time) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date d = null;
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (d == null) {
			return 0;
		} else {
			return d.getTime();
		}
	}
	/**
	 * 时间提示
	 * 
	 * @param before
	 * @return
	 */
	public static String timeHint(Long before, String format) {
		long a = System.currentTimeMillis() - before;
		int c = (int) (a / DAY);
		String result = "";
		if (a < DAY && a > HOUR) {
			result = a / HOUR + "小时前";
		} else if (a < HOUR && a > MINUTE) {
			result = a / MINUTE + "分钟前";
		} else if (a < MINUTE && a >=0) {
			result = a / SECOND + "秒前";
			if(a<1000)
				result="刚刚";
		} else if (c>0&&c <= 3) {
			result = c + "天前";
		} else {
			result = formatDate(format, before);
		}
		return result;
	}

	public static long getMillisBefore(int year) {
		return getTimeMillis("yyyy-MM-dd HH", year + "-06-07 09") - System.currentTimeMillis();
	}

	public static String getStringFromMil(long mil) {
		long day = mil / 1000 / 3600 / 24;
		long hour = mil / 1000 / 3600 - day * 24;
		long min = mil / 1000 / 60 - day * 24 * 60 - hour * 60;
		long ss = mil / 1000 - (day * 24 * 60 + hour * 60) * 60 - min * 60;
		return String.format("%d天%d小时%d分钟%d秒", day, hour, min, ss);
	}

	public static String countNationalHigherEducationEntranceExamination(
			String year) {
		return getStringFromMil(getMillisBefore(Integer.valueOf(year)));
	}
	public static String getDay(long time){
		SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
		String d = format.format(time);
		return d;
	}

}
