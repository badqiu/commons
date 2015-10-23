package com.github.rapid.common.jdbc.dao.support;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.github.rapid.common.util.MapUtil;

public class FreemarkerJdbcTemplateTest {

	DataSource ds = mock(DataSource.class);
	Connection conn = mock(Connection.class);
	
	@Test
	public void test() throws Exception {
		when(ds.getConnection()).thenReturn(conn);
		when(conn.prepareStatement(any(String.class))).thenReturn(mock(PreparedStatement.class));
		
		
		FreemarkerJdbcTemplate template = new FreemarkerJdbcTemplate();
		template.setLocation(new ClassPathResource("fortest_freemarkerjdbctemplate/App.sql.xml"));
		template.setNamedParameterJdbcTemplate(new NamedParameterJdbcTemplate(ds));
		template.afterPropertiesSet();
		Map paramMap = MapUtil.newMap("appId","1983","appName","1983","appDesc","appDesc","appLogo","100","version","1.0");
		template.execute("app.insert", paramMap,new PreparedStatementCallback(){
			@Override
			public Object doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.execute();
				return null;
			}
		});
	}

}
