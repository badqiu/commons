package com.github.rapid.common.dto;

import java.util.List;

/**
 * controller,按ID删除数据,ID查找数据,作为@RequestBody
 */
public class BatchNumberIdDto {
    
    private List<Long> ids;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
    
    
}
