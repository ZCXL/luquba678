package cn.luquba678.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.baidu.navisdk.util.common.StringUtils;

import android.R.integer;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class DateUtils {
	private final static int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23,
			23, 23, 24, 23, 22 };
	private final static String[] constellationArr = new String[] { "摩羯座",
			"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
			"天蝎座", "射手座", "摩羯座" };

	public final static long SECOND = 1000;
	public final static long MINUT = SECOND * 60;
	public final static long HOUR = MINUT * 60;
	public final static long DAY = HOUR * 24;

	public static String getCreatetime(String createtime) {
		if (StringUtils.isNotEmpty(createtime))
			return DateUtils.timeHint(Long.parseLong(createtime) * 1000,
					"yyyy年MM月dd日");
		else
			return "";
	}

	
	/** 传入月份和日期获取星座 */
	public static String getConstellation(int month, int day) {
		return day < dayArr[month - 1] ? constellationArr[month - 1]
				: constellationArr[month];
	}

	/** 传入时间毫秒数获取星座 */
	public static String getConstellationByTimeMillis(Long time) {
		String[] mmdd = formatDate("MM-dd", time).split("[^0-9]");
		return getConstellation(Integer.parseInt(mmdd[0]),
				Integer.parseInt(mmdd[1]));
	}

	/** 传入时间和对应时间格式获取星座 */
	public static String getConstellationByDate(String pattern, String timeStr) {
		return getConstellationByTimeMillis(getTimeMillis(pattern, timeStr));
	}

	/** 传入时间(Timestamp)星座 */
	public static String getConstellationByTimestamp(Timestamp s) {
		return getConstellationByTimeMillis(s.getTime());
	}

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

	/** 根据日期毫秒数获取距今多少年的工具(获取年龄) */
	public static int getAge(Long time) {
		Calendar b = Calendar.getInstance();
		int yy = b.get(Calendar.YEAR);
		int mm = b.get(Calendar.MONTH);
		int dd = b.get(Calendar.DATE);
		b.setTimeInMillis(time);
		int age = yy - b.get(Calendar.YEAR);
		if (b.get(Calendar.MONTH) > mm
				|| (b.get(Calendar.MONTH) == mm && b.get(Calendar.DATE) > dd)) {
			age = age - 1;
		}
		return age;
	}

	/** 传入两个时间计算相差多少天 */
	public static int timeBefor(Long befor, Long after) {
		long a = after - befor;
		int c = (int) (a / DAY);
		return c;
	}

	/**
	 * 时间提示
	 * 
	 * @param after
	 * @return
	 */
	public static String timeHint(Long befor, String format) {
		long a = System.currentTimeMillis() - befor;
		int c = (int) (a / DAY);
		String result = "";
		if (a < DAY && a > HOUR) {
			result = a / HOUR + "小时前";
		} else if (a < HOUR && a > MINUT) {
			result = a / MINUT + "分钟前";
		} else if (a < MINUT && a > 0) {
			result = a / SECOND + "秒前";
		} else if (c <= 3) {
			result = c + "天前";
		} else {
			result = formatDate(format, befor);
		}
		return result;
	}

	/** 传入生日毫秒数，获取还有多少天过生日 */
	public static int getBirthDayBefore(Long birth) {
		long a = getMillisBefore(birth);
		int c = (int) (a / 1000 / 60 / 60 / 24);
		return c;
	}

	/** 传入生日毫秒数，获取还有多少毫秒 */
	public static long getMillisBefore(Long birth) {
		Calendar b = Calendar.getInstance();
		int age = getAge(birth);
		b.setTimeInMillis(birth);
		b.set(b.get(Calendar.YEAR) + 1 + age, b.get(Calendar.MONTH),
				b.get(Calendar.DATE));
		return b.getTimeInMillis() - System.currentTimeMillis();
	}

	public static long getMillisBefore(int year) {
		return getTimeMillis("yyyy-MM-dd HH", year + "-06-07 09")
				- System.currentTimeMillis();
	}

	public static String getStringFromMil(long mil) {
		long day = mil / 1000 / 3600 / 24;
		long hour = mil / 1000 / 3600 - day * 24;
		long min = mil / 1000 / 60 - day * 24 * 60 - hour * 60;
		long ss = mil / 1000 - (day * 24 * 60 + hour * 60) * 60 - min * 60;
		return String.format("%d天%d小时%d分钟%d秒", day, hour, min, ss);
	}

	public static String countNationalHigherEducationEntranceExamination(
			int year) {
		return getStringFromMil(getMillisBefore(year));
	}

	public static String countNationalHigherEducationEntranceExamination(
			String year) {
		return getStringFromMil(getMillisBefore(Integer.valueOf(year)));
	}

	/** 今天是否是生日 */
	public static boolean isBirthDay(Long time) {
		return getBirthDayBefore(time) == 0;
	}

	/**
	 * 获取当前应用程序的版本号。
	 */
	public int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

}
