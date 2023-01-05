package com.github.rapid.common.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FileCommentUtil {

	private static String XML_COMMENT = "<!--(.*?)-->";
	private static String C_STYLE_COMMENT = "/*(.*?)*/";
	private static String C_STYLE_LINE_COMMENT = "//(.*?)";
	private static String SHELL_COMMENT = "#(.*?)";
	private static String SQL_LINE_COMMENT = "--(.*?)";
	private static String SQL_COMMENT = C_STYLE_COMMENT;
	
	public static String getComments(String content) {
		List<String> all = RegexUtil.findAllByRegexGroup(content, XML_COMMENT, 1);
		return StringUtils.join(all,"\n");
	}
	
}
