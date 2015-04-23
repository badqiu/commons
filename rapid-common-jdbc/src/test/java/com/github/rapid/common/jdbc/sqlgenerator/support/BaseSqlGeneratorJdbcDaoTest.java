package com.github.rapid.common.jdbc.sqlgenerator.support;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


public class BaseSqlGeneratorJdbcDaoTest extends Assert {
	
	RssInfoDao dao = new RssInfoDao();
	
	@Before
	public void setUp() {
		DataSource ds = getDataSource();
		JdbcTemplate template = new JdbcTemplate(ds);
		template.execute("create table rss_info(rss_id bigint,rss_title varchar(30),rss_content varchar(50) )");
		template.execute("insert into rss_info values(1,'title1','content1')");
		
		dao.setDataSource(ds);
		dao.afterPropertiesSet();
	}
	
	private DataSource getDataSource(){
		String jdbcDriver = "org.h2.Driver";
		String jdbcUsername = "sa";
		String jdbcPassword = "";
		String jdbcUrl = "jdbc:h2:mem:testDb;DB_CLOSE_DELAY=-1";
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(jdbcDriver);
		
		ds.setUsername(jdbcUsername);
		ds.setPassword(jdbcPassword);
		ds.setUrl(jdbcUrl);
		return ds;
	}
	
	@Test
	public void test() throws SQLException {
		RssInfo info = new RssInfo();
		info.setRssId(100);
		info.setRssTitle("title1");
		info.setRssContent("content1");
		dao.insert(info);
		dao.update(info);
		assertNotNull(dao.getById(info.getRssId()));
		dao.deleteById(info.getRssId());
		assertNull(dao.getById(info.getRssId()));
	}
	
}
