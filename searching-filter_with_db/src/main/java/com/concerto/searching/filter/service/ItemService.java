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

	// @Autowired
	// private CategoryRepository categoryRepository;

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

				if (fieldName.equals("name") && operator.equals("like")) {
					String partialName = value.toString().toLowerCase();
					return item.getName().toLowerCase().contains(partialName);
				} else {
					return true;
				}
			}).collect(Collectors.toList());
		}

		return filteredItems;
	}

	// private List<Item> applyFilter(List<Item> items, List<Search> filters) {
	// // Modify this method based on your filtering requirements
	// // Here, we'll demonstrate filtering by category name
	// for (Search filter : filters) {
	// if ("categoryName".equalsIgnoreCase(filter.getFieldName())) {
	// String categoryName = (String) filter.getValue();
	// Category category = categoryRepository.findByCategoryName(categoryName);
	// if (category != null) {
	// return itemRepository.findByCategory(category);
	// } else {
	// return new ArrayList<>(); // Return an empty list if category not found
	// }
	// }
	// }
	//
	// return items;
	// }

	private List<Item> paginate(List<Item> items, int page, int size) {
		
		   if (page <= 0) {
		        // Return a message or throw an exception indicating that the page parameter is invalid
		        throw new IllegalArgumentException("Invalid page value. Page cannot be 0 or negative.");
		    }

		int start = (page - 1) * size;
		int end = Math.min(start + size, items.size());
		return items.subList(start, end);
	}
}
