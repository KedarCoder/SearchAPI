package com.concerto.searching.filter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concerto.searching.filter.bean.Item;
import com.concerto.searching.filter.functions.Filter;
import com.concerto.searching.filter.functions.Search;
import com.concerto.searching.filter.repository.ItemRepository;
import com.concerto.searching.filter.mode.ApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	public ApiResponse<List<Item>> search() {
		List<Item> items = itemRepository.findAll();
		return new ApiResponse<>(items, items.size());
	}

	public ApiResponse<List<Item>> searchAndFilter(Filter searchRequest) {
		List<Search> filters = searchRequest.getFilters();
		int page = searchRequest.getPage();
		int size = searchRequest.getSize();

		// Fetch all items from the repository
		List<Item> allItems = itemRepository.findAll();

		// Apply filters
		List<Item> filteredItems = applyFilters(allItems, filters);

		// Paginate the result
		List<Item> paginatedItems = paginate(filteredItems, page, size);

		return new ApiResponse<>(paginatedItems, filteredItems.size());
	}

	private List<Item> applyFilters(List<Item> items, List<Search> filters) {
		List<Item> filteredItems = new ArrayList<>(items);

		for (Search filter : filters) {
			String fieldName = filter.getFieldName();
			String operator = filter.getOperator();
			Object value = filter.getValue();

			filteredItems = filteredItems.stream().filter(item -> {
				if ("equals".equalsIgnoreCase(operator)) {
					return item.getName().equals(value);
				} else {
					// Handle other operators as needed
					return true; // No filter applied for unknown operators
				}
			}).collect(Collectors.toList());
		}

		return filteredItems;
	}

	private List<Item> paginate(List<Item> items, int page, int size) {
		int start = (page - 1) * size;
		int end = Math.min(start + size, items.size());
		return items.subList(start, end);
	}
}
