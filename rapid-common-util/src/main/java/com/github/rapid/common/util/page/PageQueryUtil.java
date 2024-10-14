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
		
		long page = Math.max(1, query.getPage());
		long offset = (page - 1) * query.getPageSize();
		return offset+","+query.getPageSize();
	}
	
	/**
	 * 遍历分页查询
	 * 
	 * @param pageSize	分页大小
	 * @param queryAndProcessListFunction 生成查询结果的处理Function
	 * 
	 */
	public static <T> void forEachPageQuery(long pageSize,Function<PageQuery,List<T>> queryAndProcessListFunction) {
		forEachPageQuery(pageSize,queryAndProcessListFunction,null);
	}
	
	/**
	 * 遍历分页查询
	 * 
	 * @param pageSize	分页大小
	 * @param queryListFunction			生成查询结果的处理Function
	 * @param queryResultProcessFunction 处理查询结果的Function
	 * 
	 */
	public static <T> void forEachPageQuery(long pageSize,Function<PageQuery,List<T>> queryListFunction,Consumer<List<T>> queryResultProcessFunction) {
		long page = 1;
		PageQuery pageQuery = new PageQuery(page,pageSize);
		List<T> list = null;
		do {
			pageQuery.setPage(page);
			list = queryListFunction.apply(pageQuery);
			
			if(queryResultProcessFunction != null) {
				queryResultProcessFunction.accept(list);
			}
			
			page++;
		}while(CollectionUtils.isNotEmpty(list) && !(list.size() < pageSize));
	}
	
	/**
	 * 遍历分页查询
	 * 
	 * @param pageSize	分页大小
	 * @param queryAndProcessListFunction 生成查询结果的处理Function
	 * 
	 */	
	public static <T> void forEachPage(long pageSize,Function<Long,List<T>> queryAndProcessListFunction) {
		forEachPage(pageSize,queryAndProcessListFunction,null);
	}
	
	/**
	 * 遍历分页查询
	 * 
	 * @param pageSize	分页大小
	 * @param queryListFunction			生成查询结果的处理Function
	 * @param queryResultProcessFunction 处理查询结果的Function
	 * 
	 */
	public static <T> void forEachPage(long pageSize,Function<Long,List<T>> queryListFunction,Consumer<List<T>> queryResultProcessFunction) {
		long page = 1;
		List<T> list = null;
		do {
			list = queryListFunction.apply(page);
			
			if(queryResultProcessFunction != null) {
				queryResultProcessFunction.accept(list);
			}
			
			page++;
		}while(isEndPage(pageSize, list));
	}

	private static <T> boolean isEndPage(long pageSize, List<T> list) {
		return CollectionUtils.isNotEmpty(list) && !(list.size() < pageSize);
	}
}
