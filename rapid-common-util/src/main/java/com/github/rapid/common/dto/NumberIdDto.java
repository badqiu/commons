package com.github.rapid.common.dto;

/**
 * controller,按ID删除数据,ID查找数据,作为@RequestBody
 */
public class NumberIdDto {
    
    private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
    

}
