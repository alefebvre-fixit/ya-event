package com.ya.yaevent.util;

import java.util.Collection;

public class YaUtil {

	public static boolean isEmpty(Collection c) {
		if (c != null && c.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isNotEmpty(Collection c) {
		return !isEmpty(c);
	}

	public static boolean isEmpty(String s) {
		if (s == null)
			return true;
		if ("".equals(s))
			return true;
		return false;
	}

	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

}
