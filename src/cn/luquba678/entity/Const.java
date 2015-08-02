package cn.luquba678.entity;

public class Const {
	// 主页
	public final static String BASE_URL = "http://120.26.112.250";
	// 短信验证
	public final static String GET_VRIFY_URL = BASE_URL + "/api/common/sendsms";
	// 注册
	public final static String REGIST_URL = BASE_URL + "/api/common/register";
	// 登录
	public final static String LOGIN_URL = BASE_URL + "/api/common/login";

	// 忘记密码
	public final static String FORGETPASS_URL = BASE_URL
			+ "/api/common/findPassword";
	// 查找广告
	public final static String QUERY_ADS_URL = BASE_URL
			+ "/api/ads/query";
	// 详情
	public final static String DETAIL_URL = BASE_URL
			+ "/home/detail?uid=UID&login_token=LOGIN_TOKEN&type=TYPE";
	// 评论 post:content
	public final static String COMMENT_URL = BASE_URL
			+ "/api/comment/add?uid=%s&login_token=%s&id=%d&type=%d";
	// 评论列表
	public final static String COMMENT_LIST_URL = BASE_URL
			+ "/api/comment/query?uid=%s&login_token=%s&id=%d&type=%d&page=%d";
	// 点赞
	public final static String PRAISE_URL = BASE_URL
			+ "/api/praise?uid=%s&login_token=%s&id=%d&type=%d";
	// 添加收藏 post ：type,id
	public final static String ADD_COLLECTION_URL = BASE_URL
			+ "/api/collection/add?uid=%s&login_token=%s&id=%d&type=%d";
	// 查看详情页的点赞，评论等数据信息
	public final static String GET_DETAIL_MSG_URL = BASE_URL
			+ "/api/detail?uid=%s&login_token=%s&id=%d&type=%d";
	// 获取是否点赞，收藏
	public final static String DETAILMSG = BASE_URL
			+ "/api/story/detail?uid=%s&id=%d";
	// 删除，收藏
	public final static String DELETECOLLECTION = BASE_URL
			+ "/api/collection/delete?uid=%s&login_token=%s";
	// 查询，收藏
	public final static String QUERYCOLLECTION = BASE_URL
			+ "/api/collection/query?uid=%s&login_token=%s&page=%s";
	// 忘记密码
	public final static String FORGETPASS_GETMSG_URL = BASE_URL
			+ "/api/common/forgetPassword";
	// 4.6.7.查看详情页html
	//home/detail?uid=UID&login_token=LOGIN_TOKEN&type=TYPE
	public final static String STORY_DETAIL = BASE_URL + "/home/detail?id=%d&type=%d";

	// 查找名言
	public final static String QUERY_FAMOUS_SAYS = BASE_URL
			+ "/api/logion/query";
	// 励志故事
	public final static String STORY_QUERY = BASE_URL
			+ "/api/story/query?page=";
	// 状元心得
	public final static String CHAMPION_EXPERIENCE = BASE_URL
			+ "/api/toptips/query?page=";
	// 校花校草
	public final static String PRETTY_QUERY = BASE_URL
			+ "/api/schoolbeauty/query?page=%d&type=%d";
	// 轻松一刻
	public final static String FUNNY_QUERY = BASE_URL
			+ "/api/relaxtime/query?page=%d&type=%d";
	// 大学排名
	public final static String SCHOOL_QUERY = BASE_URL
			+ "/api/schoolrange/query?page=";
	// 发送寄语
	public final static String SEND_WORD = BASE_URL
			+ "/api/sendword/add?uid=%s&login_token=%s";
	// 删除寄语
	public final static String DELETE_WORD = BASE_URL + "/api/sendword/delete";
	// 查询寄语
	public final static String QUERY_PAGE_WORD = BASE_URL
			+ "/api/story/query?page=";
	// 查找寄语
	public final static String QUERY_WORD = BASE_URL
			+ "/api/sendword/queryAll?uid=%s&login_token=%s";
	// 确认登录
	public final static String CHECK_LOGIN = BASE_URL
			+ "/api/user/checklogin?uid=%s&login_token=%s";
	// 查询学校列表
	public final static String QUERY_SCHOOL = BASE_URL
			+ "/api/gradeline/querySchool?page=";
	// 查询学校详细信息
	public final static String QUERY_SCHOOL_DETAIL = BASE_URL
			+ "/api/gradeline/querySchoolDetail";
	// 查询相关专业
	public final static String QUERY_MAJOR = BASE_URL
			+ "/api/gradeline/queryMajor";
	// 修改用户信息
	public final static String CHANGE_USER_INFO = BASE_URL
			+ "/api/user/changeUserInfo?uid=%s&login_token=%s";
	// 常见问题
	public final static String GET_HELP = BASE_URL + "/api/user/getHelp";
	// 意见反馈
	public final static String FEED_BACK = BASE_URL + "/api/user/feedback";

	// 定位KEY
	public final static String LOCAT_KEY = "F9da85afead8b6e9c4738e5e5b79eb97";
	// 定位
	public final static String LOCAT_URL = "http://api.map.baidu.com/geocoder?";

	public final static String Test_GET_VRIFY_URL = BASE_URL
			+ "auth/getApiToken?token=luquba678";
	public final static String VRIFY_KEY = "e8c97fa226cff8c46c3f82ed1ebeb1ba";
	// http://120.26.112.250/api/auth/getApiToken?token=luquba678
}
