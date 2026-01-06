package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public void underscoreName() {
		assertEquals("test_name", StringUtil.underscore("TestName"));
		assertEquals("test_name123_sex", StringUtil.underscore("testName123Sex"));
	}

	@Test
	public void camelCaseName() {
		assertEquals("testName", StringUtil.camelCase("test_name"));
		assertEquals("testName123Sex", StringUtil.camelCase("test_name123_sex"));

		assertEquals("testNameAbc", StringUtil.camelCase("testNameAbc"));
		assertEquals("testNameAbc", StringUtil.camelCase("test_Name_Abc"));
	}

	@Test
	public void testSplitByCommonSeparators_NullInput() {
		// 测试空输入
		assertNull(StringUtil.splitByCommonSeparators(null));

		// 测试空白字符串
		String[] result1 = StringUtil.splitByCommonSeparators("");
		assertNotNull(result1);
		assertEquals(0, result1.length);

		// 测试全空白字符串
		String[] result2 = StringUtil.splitByCommonSeparators("   \t\n  ");
		assertNotNull(result2);
		assertEquals(0, result2.length);
	}

	@Test
	public void testSplitByCommonSeparators_SingleElement() {
		// 测试单个元素
		String[] result = StringUtil.splitByCommonSeparators("hello");
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals("hello", result[0]);
	}

	@Test
	public void testSplitByCommonSeparators_EnglishComma() {
		// 测试英文逗号分隔
		String[] result = StringUtil.splitByCommonSeparators("a, b,c ");
		assertNotNull(result);
		assertEquals(3, result.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_ChineseComma() {
		// 测试中文逗号分隔
		String[] result = StringUtil.splitByCommonSeparators("a，b，\nc");
		assertNotNull(result);
		assertEquals(3, result.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_SpaceSeparated() {
		// 测试空格分隔
		String[] result = StringUtil.splitByCommonSeparators("a b c");
		assertNotNull(result);
		assertEquals(3, result.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result);

		// 测试多个空格
		String[] result2 = StringUtil.splitByCommonSeparators("a  b   c");
		assertNotNull(result2);
		assertEquals(3, result2.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result2);
	}

	@Test
	public void testSplitByCommonSeparators_TabSeparated() {
		// 测试制表符分隔
		String[] result = StringUtil.splitByCommonSeparators("a\tb\tc");
		assertNotNull(result);
		assertEquals(3, result.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_NewlineSeparated() {
		// 测试换行符分隔
		String[] result = StringUtil.splitByCommonSeparators("a\nb\nc");
		assertNotNull(result);
		assertEquals(3, result.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_MixedSeparators() {
		// 测试混合分隔符
		String[] result = StringUtil.splitByCommonSeparators("a,b c，d\te\nf");
		assertNotNull(result);
		assertEquals(6, result.length);
		assertArrayEquals(new String[] { "a", "b", "c", "d", "e", "f" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_ComplexExample() {
		// 测试注释中的例子
		String[] result = StringUtil.splitByCommonSeparators("a,b c");
		assertNotNull(result);
		assertEquals(3, result.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_WithExtraSpaces() {
		// 测试包含多余空格的场景
		String[] result = StringUtil.splitByCommonSeparators("  a , b  , c ");
		assertNotNull(result);
		assertEquals(3, result.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_RepeatedSeparators() {
		// 测试重复分隔符
		String[] result = StringUtil.splitByCommonSeparators("a,,,b");
		assertNotNull(result);
		assertEquals(2, result.length);
		assertArrayEquals(new String[] { "a", "b" }, result);

		String[] result2 = StringUtil.splitByCommonSeparators(",a,b,c,");
		assertNotNull(result2);
		assertEquals(3, result2.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result2);
	}

	@Test
	public void testSplitByCommonSeparators_RealWorldExample() {
		// 测试真实场景
		String input = "java,spring，hibernate\tmybatis\nredis";
		String[] result = StringUtil.splitByCommonSeparators(input);
		assertNotNull(result);
		assertEquals(5, result.length);
		assertArrayEquals(new String[] { "java", "spring", "hibernate", "mybatis", "redis" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_ChineseAndEnglishMixed() {
		// 测试中英文混合
		String input = "北京,上海，广州\t深圳\n杭州";
		String[] result = StringUtil.splitByCommonSeparators(input);
		assertNotNull(result);
		assertEquals(5, result.length);
		assertArrayEquals(new String[] { "北京", "上海", "广州", "深圳", "杭州" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_WithTrim() {
		// 测试分隔符周围的空格被trim
		String input = "  apple ,  banana ，  cherry  ";
		String[] result = StringUtil.splitByCommonSeparators(input);
		assertNotNull(result);
		assertEquals(3, result.length);
		assertArrayEquals(new String[] { "apple", "banana", "cherry" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_EmptyElements() {
		// 测试会忽略空元素
		String input = "a,,b,,,c";
		String[] result = StringUtil.splitByCommonSeparators(input);
		assertNotNull(result);
		assertEquals(3, result.length);
		assertArrayEquals(new String[] { "a", "b", "c" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_AllSeparatorsCombined() {
		// 测试所有分隔符组合
		String input = "a,b，c d\te\nf";
		String[] result = StringUtil.splitByCommonSeparators(input);
		assertNotNull(result);
		assertEquals(6, result.length);
		assertArrayEquals(new String[] { "a", "b", "c", "d", "e", "f" }, result);
	}

	@Test
	public void testSplitByCommonSeparators_SpecialCharacters() {
		// 测试特殊字符
		String input = "a-b,c_d，e@f";
		String[] result = StringUtil.splitByCommonSeparators(input);
		assertNotNull(result);
		assertEquals(3, result.length);
		// 注意：-、_、@不会被分割
		assertArrayEquals(new String[] { "a-b", "c_d", "e@f" }, result);
	}
	
	@Test
	public void testSplitByCommonSeparators_SpecialCharacters2() {
		// 测试特殊字符
		String input = "[\"helloworld\",\"my-name\",'my.name','my_name',123,999.99,0x111]";
		String[] result = StringUtil.splitByCommonSeparators(input);
		assertNotNull(result);
//		assertEquals(6, result.length);
		
		for(Object item : result) {
			System.out.println(item);
		}
		// 注意：-、_、@不会被分割
		assertArrayEquals(new String[] { "helloworld", "my-name", "my.name","my_name","123","999.99","0x111" }, result);
	}

}
