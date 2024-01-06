package com.github.rapid.common.util.page;

/**
 * 分页器，根据page,pageSize,totalItem用于页面上分页显示多项内容，计算页码和当前页的偏移量，方便页面分页使用.
 * 
 * @author badqiu
 * @version $Id: Paginator.java,v 0.1 2010-11-29 下午05:35:58 badqiu Exp $
 */
public class Paginator implements java.io.Serializable {
	private static final long serialVersionUID = 6089482156906595931L;
	
	private static final long DEFAULT_SLIDERS_COUNT = 7;
	
    /** 分页大小 */
    private long               pageSize;
    /** 页数  */
    private long               page;
    /** 总记录数 */
    private long               totalItems;
	
    public Paginator() {
    }
    
	public Paginator(long page, long pageSize, long totalItems) {
		super();
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.page = computePageNo(page);
	}

	public long getPageSize() {
		return pageSize;
	}
	
    public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public void setPage(long page) {
		this.page = page;
	}

	/**
     * 取得当前页。
     */
	public long getPage() {
		return page;
	}
	
	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

    /**
     * 取得总项数。
     *
     * @return 总项数
     */
	public long getTotalItems() {
		return totalItems;
	}

    /**
     * 是否是首页（第一页），第一页页码为1
     *
     * @return 首页标识
     */
	public boolean isFirstPage() {
		return page <= 1;
	}

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
	public boolean isLastPage() {
		return page >= getTotalPages();
	}
	
	public long getPrePage() {
		if (isHasPrePage()) {
			return page - 1;
		} else {
			return page;
		}
	}
	
	public long getNextPage() {
		if (isHasNextPage()) {
			return page + 1;
		} else {
			return page;
		}
	}
	
    /**
     * 判断指定页码是否被禁止，也就是说指定页码超出了范围或等于当前页码。
     *
     * @param page 页码
     *
     * @return boolean  是否为禁止的页码
     */
    public boolean isDisabledPage(long page) {
        return ((page < 1) || (page > getTotalPages()) || (page == this.page));
    }
    
    /**
     * 是否有上一页
     *
     * @return 上一页标识
     */
	public boolean isHasPrePage() {
		return (page - 1 >= 1);
	}	
    /**
     * 是否有下一页
     *
     * @return 下一页标识
     */
	public boolean isHasNextPage() {
		return (page + 1 <= getTotalPages());
	}
	
	/**
	 * 开始行，可以用于oracle分页使用 (1-based)。
	 **/
	public long getStartRow() {
		if(getPageSize() <= 0 || totalItems <= 0) return 0;
		return page > 0 ? (page - 1) * getPageSize() + 1 : 0;
	}
	
	/**
     * 结束行，可以用于oracle分页使用 (1-based)。
     **/
	public long getEndRow() {
	    return page > 0 ? Math.min(pageSize * page, getTotalItems()) : 0; 
	}
	
    /**
     * offset，计数从0开始，可以用于mysql分页使用(0-based)
     **/	
	public long getOffset() {
		return page > 0 ? (page - 1) * getPageSize() : 0;
	}
	
	/**
     * limit，可以用于mysql分页使用(0-based)
     **/
    public long getLimit() {
        if (page > 0) {
            return Math.min(pageSize * page, getTotalItems()) - (pageSize * (page - 1));
        } else {
            return 0;
        }
    }
    
    /**
     * 结束offset，(0-based)
     **/
    public long getEndOffset() {
        return getOffset() + getLimit();
    }
    
	/**
	 * 得到 总页数
	 * @return
	 */
	public long getTotalPages() {
		if (totalItems <= 0) {
			return 0;
		}
		if (pageSize <= 0) {
			return 0;
		}

		long count = totalItems / pageSize;
		if (totalItems % pageSize > 0) {
			count++;
		}
		return count;
	}

    protected long computePageNo(long page) {
        return computePageNumber(page,pageSize,totalItems);
    }
    /**
     * 页码滑动窗口，并将当前页尽可能地放在滑动窗口的中间部位。
     * @return
     */
    public Long[] getSlider() {
    	return slider(DEFAULT_SLIDERS_COUNT);
    }
    /**
     * 页码滑动窗口，并将当前页尽可能地放在滑动窗口的中间部位。
     * 注意:不可以使用 getSlider(1)方法名称，因为在JSP中会与 getSlider()方法冲突，报exception
     * @return
     */
    public Long[] slider(long slidersCount) {
    	return generateLinkPageNumbers(getPage(),(int)getTotalPages(), slidersCount);
    }
    
    private static long computeLastPageNumber(long totalItems,long pageSize) {
    	if(pageSize <= 0) return 1;
        long result = (int)(totalItems % pageSize == 0 ? 
                totalItems / pageSize 
                : totalItems / pageSize + 1);
        if(result <= 1)
            result = 1;
        return result;
    }
    
    private static long computePageNumber(long page, long pageSize,long totalItems) {
        if(page <= 1) {
            return 1;
        }
        if (Integer.MAX_VALUE == page
                || page > computeLastPageNumber(totalItems,pageSize)) { //last page
            return computeLastPageNumber(totalItems,pageSize);
        }
        return page;
    }
    
    private static Long[] generateLinkPageNumbers(long currentPageNumber,long lastPageNumber,long count) {
        long avg = count / 2;
        
        long startPageNumber = currentPageNumber - avg;
        if(startPageNumber <= 0) {
            startPageNumber = 1;
        }
        
        long endPageNumber = startPageNumber + count - 1;
        if(endPageNumber > lastPageNumber) {
            endPageNumber = lastPageNumber;
        }
        
        if(endPageNumber - startPageNumber < count) {
            startPageNumber = endPageNumber - count;
            if(startPageNumber <= 0 ) {
                startPageNumber = 1;
            }
        }
        
        java.util.List<Long> result = new java.util.ArrayList<Long>();
        for(long i = startPageNumber; i <= endPageNumber; i++) {
            result.add(new Long(i));
        }
        return result.toArray(new Long[result.size()]);
    }
    
    public String toString() {
        return "page:"+page+" pageSize:"+pageSize+" totalItems:"+totalItems;
    }
    
}
