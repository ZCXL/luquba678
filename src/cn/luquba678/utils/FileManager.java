package cn.luquba678.utils;

public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.luquba/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.luquba/files/";
		}
	}
}
