package com.github.rapid.common.jdbc.sqlgenerator.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Ref;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.util.Assert;


public class MetadataCreateUtilsTest {
    @Test
    public void isIncludeJavaType() {
        assertTrue(MetadataCreateUtils.isIncludeJavaType(int.class));
        assertTrue(MetadataCreateUtils.isIncludeJavaType(Integer.class));
        assertTrue(MetadataCreateUtils.isIncludeJavaType(Date.class));
        assertTrue(MetadataCreateUtils.isIncludeJavaType(Timestamp.class));
        assertTrue(MetadataCreateUtils.isIncludeJavaType(LocalTime.class));
        assertTrue(MetadataCreateUtils.isIncludeJavaType(Ref.class));
        assertFalse(MetadataCreateUtils.isIncludeJavaType(null));
        assertFalse(MetadataCreateUtils.isIncludeJavaType(AAA.class));
        assertFalse(MetadataCreateUtils.isIncludeJavaType(List.class));
        assertFalse(MetadataCreateUtils.isIncludeJavaType(Map.class));
        assertFalse(MetadataCreateUtils.isIncludeJavaType(Collection.class));
        assertFalse(MetadataCreateUtils.isIncludeJavaType(new Integer[]{}.getClass()));
        assertFalse(MetadataCreateUtils.isIncludeJavaType(new int[]{}.getClass()));
        System.out.println(Integer.class.isPrimitive());
        System.out.println(new Integer[]{}.getClass().getName());
    }
    
    @Test
    public void testCreate() {
    	Table table = MetadataCreateUtils.createTable(CommentUserInfoBean.class);
    	assertEquals(table.getTableName(),"comment_user_info_bean");
    	
    	vefiryColumn(table.getPrimaryKeyColumns().get(0),"user_id",false,false,true,false);
    	
    	vefiryColumn(table.getColumnBySqlName("dept_big_name"),"dept_big_name",true,false,true,false);
    	vefiryColumn(table.getColumnBySqlName("username"),"username",true,false,true,false);
    	vefiryColumn(table.getColumnBySqlName("version"),"version",true,false,true,true);
    }
    
    private void vefiryColumn(Column column, String sqlName, boolean inserable,boolean unique,boolean updatable,boolean version) {
		Assert.notNull(column,"not found column for sqlName:"+sqlName);
    	assertEquals(column.getSqlName(),sqlName);
		assertEquals(column.isInsertable(),inserable);
		assertEquals(column.isUnique(),unique);
		assertEquals(column.isUpdatable(),updatable);
		assertEquals(column.isVersion(),version);
	}

	private static class AAA {}
}
