package com.tianer.ch.utils;

public class ParseUtil {
	/**
	 * String-->int
	 * 
	 * @param s
	 * @return
	 */
	public static int parseInt(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		int t = 0;
		try {

			t = Integer.parseInt(s);
		} catch (Exception e) {
			t = 0;
		}
		return t;
	}

	/**
	 * String-->double
	 * 
	 * @param s
	 * @return
	 */
	public static double parseDouble(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		double t = 0;
		try {

			t = Double.parseDouble(s);
		} catch (Exception e) {
			t = 0;
		}
		return t;
	}

	/**
	 * String-->double
	 *
	 * @param s
	 * @return
	 */
	public static float parseFloat(String s) {
		if (s == null || s.equals("")) {
			return 0;
		}
		float t = 0;
		try {

			t = Float.parseFloat(s);
		} catch (Exception e) {
			t = 0;
		}
		return t;
	}

	// 手机号码中间四位修改为*
	public static String getPhone(String phone) {
		return phone.replaceAll("(?<=[\\d]{3})\\d(?=[\\d]{4})", "*");
	}
}
