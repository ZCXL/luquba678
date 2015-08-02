package cn.luquba678.entity;

import cn.luquba678.utils.DateUtils;

public class CollectItem {
	public String collect_imgUrl;
	public String collect_title;
	public String collect_lable;
	public String collect_date;
    public String collect_id;
    public String collect_type;

	public String getCollect_id() {
		return collect_id;
	}

	public void setCollect_id(String collect_id) {
		this.collect_id = collect_id;
	}

	public String getCollect_type() {
		return collect_type;
	}

	public void setCollect_type(String collect_type) {
		this.collect_type = collect_type;
	}

	public String getCollect_imgUrl() {
		return collect_imgUrl;
	}

	public void setCollect_imgUrl(String collect_imgUrl) {
		this.collect_imgUrl = collect_imgUrl;
	}

	public String getCollect_title() {
		return collect_title;
	}

	public void setCollect_title(String collect_title) {
		this.collect_title = collect_title;
	}

	public String getCollect_date() {
		return DateUtils.getCreatetime(collect_date);
	}

	public void setCollect_date(String collect_date) {
		this.collect_date = collect_date;
	}

}
