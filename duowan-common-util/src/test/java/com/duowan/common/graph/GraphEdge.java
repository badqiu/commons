package com.duowan.common.graph;


/**
 * 代表图的边,有方向
 * 
 * @author badqiu
 *
 */
public class GraphEdge {

	private String begin;
	private String end;

	public GraphEdge(){
	}
	
	public GraphEdge(String begin, String end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
