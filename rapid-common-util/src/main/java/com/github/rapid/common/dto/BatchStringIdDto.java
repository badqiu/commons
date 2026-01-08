package com.github.rapid.common.dto;

import java.util.List;

/**
 * controller,按ID删除数据,ID查找数据,作为@RequestBody
 */
public class BatchStringIdDto {
    
    private List<String> ids;

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
    
    
}
