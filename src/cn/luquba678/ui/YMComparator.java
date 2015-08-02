package cn.luquba678.ui;

import java.util.Comparator;

import cn.luquba678.entity.GridItem;

public class YMComparator implements Comparator<GridItem> {

	public int compare(GridItem o1, GridItem o2) {
		return -o1.getTime().compareTo(o2.getTime());
	}
}
