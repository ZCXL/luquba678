package cn.luquba678.entity;

import java.util.ArrayList;
import java.util.List;

public class TestDB {

	public static List<People> getPeople() {
		List<People> list = new ArrayList<People>();
		for (int i = 0; i < 20; i++) {
			list.add(new People(
					"http://img0.imgtn.bdimg.com/it/u=2727444260,2713392284&fm=21&gp=0.jpg",
					"校花" + i, "简介" + i));
		}
		return list;
	}

	public static List<People> getBoys() {
		List<People> list = new ArrayList<People>();
		for (int i = 0; i < 20; i++) {
			list.add(new People(
					"http://pic.baike.soso.com/p/20140422/20140422143648-982261245.jpg",
					"校草" + i, "简介" + i));
		}
		return list;
	}

}
