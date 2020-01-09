package com.github.rapid.common.util.page;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import com.github.rapid.common.util.page.PageQuery;

public class PageQueryUtil {

	public static String toOffsetLimit(PageQuery query) {
		Assert.isTrue(query.getPageSize() >= 1,"pageSize >= 1 must be true");
		
		int page = Math.max(1, query.getPage());
		int offset = (page - 1) * query.getPageSize();
		return offset+","+query.getPageSize();
	}
	
	/**
	 * 遍历分页查询
	 * 
	 * @param pageSize	分页大小
	 * @param queryListFunction			生成查询结果的处理Function
	 * @param queryResultProcessFunction 处理查询结果的Function
	 * 
	 */
	public static  void forEachPageQuery(int pageSize,Function<PageQuery,List> queryListFunction,Consumer<List> queryResultProcessFunction) {
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageSize(pageSize);
		pageQuery.setPage(1);
		
		List list = null;
		do {
			list = queryListFunction.apply(pageQuery);
			queryResultProcessFunction.accept(list);
			pageQuery.setPage(pageQuery.getPage() + 1);
		}while(CollectionUtils.isNotEmpty(list) && !(list.size() < pageSize));
	}
	
}
