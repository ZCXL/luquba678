package cn.luquba678.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WDJYitem {
	private String date;
	private ArrayList<GridItem> gridItems;

	public HashMap<String, ArrayList<GridItem>> getMap(
			ArrayList<GridItem> gridItems) {
		HashMap<String, ArrayList<GridItem>> map = getMap(null, gridItems);
		return map;
	}

	public HashMap<String, ArrayList<GridItem>> getMap(
			HashMap<String, ArrayList<GridItem>> map,
			ArrayList<GridItem> gridItems) {
		if (map == null) {
			map = new HashMap<String, ArrayList<GridItem>>();
		}
		for (GridItem g : gridItems) {
			String time = g.getTime("yyMM");
			if (map.get(time) == null) {
				ArrayList<GridItem> grids = new ArrayList<GridItem>();
				map.put(time, grids);
			} else {
				map.get(time).add(g);
			}
		}
		
		return map;
	}
}
