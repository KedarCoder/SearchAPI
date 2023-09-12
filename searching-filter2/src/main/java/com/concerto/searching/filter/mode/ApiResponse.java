package com.concerto.searching.filter.mode;


public class ApiResponse<T> {
	private T data;
	private long totalItems;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

	public ApiResponse(T data, long totalItems) {

		this.data = data;
		this.totalItems = totalItems;
	}

	@Override
	public String toString() {
		return "ApiResponse [data=" + data + ", totalItems=" + totalItems + "]";
	}

}
