package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class KeyValueTest {

	@Test
	public void test() {
		assertNull(KeyValue.parse(null));
		assertNull(KeyValue.parse(" "));
		assertNull(KeyValue.parse(""));
		assertNull(KeyValue.parse(" \t \n"));

		assertEquals("name=123",KeyValue.parse(" name = 123").toString());
		assertEquals("name=",KeyValue.parse(" name = ").toString());
		assertEquals("name=diy",KeyValue.parse(" name : diy ",":").toString());
		assertEquals("name=diy \nhibernate",KeyValue.parse(" name : diy \nhibernate",":").toString());
		assertEquals(null,KeyValue.parse("  = "));
		assertEquals("name=",KeyValue.parse("name= ").toString());
		assertEquals(null,KeyValue.parse("name"));
		assertEquals(null,KeyValue.parse("="));
		assertEquals(null,KeyValue.parse("= "));
		assertEquals(null,KeyValue.parse(" = "));
		assertEquals(null,KeyValue.parse("=123"));
	}
	
	
	
	// ========== parse 默认分隔符 ==========
    @Test
    public void parse_defaultSeparator_normal() {
        KeyValue<String, String> kv = KeyValue.parse("name=zhangsan");
        assertNotNull(kv);
        assertEquals("name", kv.getKey());
        assertEquals("zhangsan", kv.getValue());
    }

    @Test
    public void parse_defaultSeparator_trimKeyAndValue() {
        KeyValue<String, String> kv = KeyValue.parse("  age  =  18  ");
        assertNotNull(kv);
        assertEquals("age", kv.getKey());
        assertEquals("18", kv.getValue());
    }

    @Test
    public void parse_defaultSeparator_valueEmpty() {
        KeyValue<String, String> kv = KeyValue.parse("remark=");
        assertNotNull(kv);
        assertEquals("remark", kv.getKey());
        assertEquals("", kv.getValue());
    }

    @Test
    public void parse_defaultSeparator_multiSeparatorTakeFirst() {
        KeyValue<String, String> kv = KeyValue.parse("a=b=c=d");
        assertNotNull(kv);
        assertEquals("a", kv.getKey());
        assertEquals("b=c=d", kv.getValue());
    }

    @Test
    public void parse_defaultSeparator_textNull() {
        KeyValue<String, String> kv = KeyValue.parse(null);
        assertNull(kv);
    }

    @Test
    public void parse_defaultSeparator_textEmpty() {
        KeyValue<String, String> kv = KeyValue.parse("");
        assertNull(kv);
    }

    @Test
    public void parse_defaultSeparator_textAllBlank() {
        KeyValue<String, String> kv = KeyValue.parse("    ");
        assertNull(kv);
    }

    @Test
    public void parse_defaultSeparator_separatorAtStart() {
        KeyValue<String, String> kv = KeyValue.parse("=abc");
        assertNull(kv);
    }

    @Test
    public void parse_defaultSeparator_noSeparator() {
        KeyValue<String, String> kv = KeyValue.parse("helloWorld");
        assertNull(kv);
    }

    @Test
    public void parse_defaultSeparator_keyBlankAfterTrim() {
        KeyValue<String, String> kv = KeyValue.parse("   =123");
        assertNull(kv);
    }

    // ========== parse 自定义分隔符 ==========
    @Test
    public void parse_customSeparator_colon() {
        KeyValue<String, String> kv = KeyValue.parse("sex:male", ":");
        assertNotNull(kv);
        assertEquals("sex", kv.getKey());
        assertEquals("male", kv.getValue());
    }

    @Test
    public void parse_customSeparator_multiCharSep() {
        KeyValue<String, String> kv = KeyValue.parse("type=>json", "=>");
        assertNotNull(kv);
        assertEquals("type", kv.getKey());
        assertEquals("json", kv.getValue());
    }

    @Test
    public void parse_defaultSeparator_valueContainsSpecialSymbol() {
        KeyValue<String, String> kv = KeyValue.parse("filter=id>1 and name like '%a'");
        assertNotNull(kv);
        assertEquals("filter", kv.getKey());
        assertEquals("id>1 and name like '%a'", kv.getValue());
    }

}
