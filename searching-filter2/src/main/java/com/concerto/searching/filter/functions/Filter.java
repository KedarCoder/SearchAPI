package com.concerto.searching.filter.functions;

import java.util.List;

public class Filter {
    private List<Search> filters;
    private int page;
    private int size;
    
    
	public List<Search> getFilters() {
		return filters;
	}
	public void setFilters(List<Search> filters) {
		this.filters = filters;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "SearchRequest [filters=" + filters + ", page=" + page + ", size=" + size + "]";
	}
    
    
}
