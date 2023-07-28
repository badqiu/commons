package com.github.rapid.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ArgsUtil {

	/**
	 * Get the key from the given args. Keys have to start with '-' or '--'. For example, --key1 value1 -key2 value2.
	 * @param args all given args.
	 * @param index the index of args to be parsed.
	 * @return the key of the given arg.
	 */
	public static String getKeyFromArgs(String[] args, int index) {
		String key;
		if (args[index].startsWith("--")) {
			key = args[index].substring(2);
		} else if (args[index].startsWith("-")) {
			key = args[index].substring(1);
		} else {
//			throw new IllegalArgumentException(
//				String.format("Error parsing arguments '%s' on '%s'. Please prefix keys with -- or -",
//					Arrays.toString(args), args[index]));
			return null;
		}

//		if (key.isEmpty()) {
//			throw new IllegalArgumentException(
//				"The input " + Arrays.toString(args) + " contains an empty argument");
//		}

		return key;
	}
	protected static final String NO_VALUE_KEY = "__NO_VALUE_KEY";
	public static Map<String,String> fromArgs(String argsStr) {
		String[] args = argsStr.trim().split("\\s+");
		return fromArgs(args);
	}


	public static String getValue(String argsStr, String key) {
		String[] args = argsStr.trim().split("\\s+");
		return fromArgs(args).get(key);
	}
	
	public static Map<String,String> fromArgs(String[] args) {
		final Map<String, String> map = new HashMap<>(args.length / 2);

		int i = 0;
		while (i < args.length) {
			final String key = getKeyFromArgs(args, i);

			if (key == null || key.isEmpty()) {
//				throw new IllegalArgumentException(
//					"The input " + Arrays.toString(args) + " contains an empty argument");
				i+=1;
				continue;
			}

			i += 1; // try to find the value

			if (i >= args.length) {
				map.put(key, NO_VALUE_KEY);
			} else if (NumberUtils.isNumber(args[i])) {
				map.put(key, args[i]);
				i += 1;
			} else if (args[i].startsWith("--") || args[i].startsWith("-") || args[i].startsWith("-D")) {
				// the argument cannot be a negative number because we checked earlier
				// -> the next argument is a parameter name
				map.put(key, NO_VALUE_KEY);
			} else {
				map.put(key, args[i]);
				i += 1;
			}
		}

		return map;
	}
	
}
