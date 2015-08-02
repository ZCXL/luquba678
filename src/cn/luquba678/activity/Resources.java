package cn.luquba678.activity;

import cn.luquba678.R;
import cn.luquba678.activity.fragment.HomeFragment;
import cn.luquba678.activity.fragment.PersonalFragment;
import cn.luquba678.activity.fragment.QueryFragment;
import cn.luquba678.activity.fragment.TabMyStoryFragment;
import android.support.v4.app.Fragment;

/**
 * @author Collin 添加导航条目
 */
public class Resources {

	/**
	 * 设置ImageViewID的数组
	 */
	public static int[] navigationImgViewIds = { R.id.img_home,
			R.id.img_search, R.id.img_blog, R.id.img_personal };
	/**
	 * 设置未选中的图像
	 */
	public static int[] iconsNormal = { R.drawable.ic_home,
			R.drawable.ic_search, R.drawable.ic_msg,
			R.drawable.ic_personal_info };
	/**
	 * 设置选中后的图像
	 */
	public static int[] iconsChoosed = { R.drawable.ic_home1,
			R.drawable.ic_search1, R.drawable.ic_msg1,
			R.drawable.ic_personal_info1 };
	/**
	 * 点击区域
	 */
	public static int[] navigationLayoutIds = { R.id.navigation_home,
			R.id.navigation_search, R.id.navigation_blog,
			R.id.navigation_personal };
	/**
	 * 文字TextViewIds
	 */
	public static int[] navigationText = { R.id.text_home, R.id.text_search,
			R.id.text_blog, R.id.text_personal };
	/**
	 * 选中和未选中时文字的颜色
	 */
	public static int[] textcolors = { R.color.normal_text_color,
			R.color.main_color };
	/**
	 * 对应的fragment<? extends Fragment>
	 */
	public static Class<? extends Fragment>[] classes = new Class[] {
			HomeFragment.class, QueryFragment.class, TabMyStoryFragment.class,
			PersonalFragment.class };
}
