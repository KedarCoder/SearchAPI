package com.concerto.searching.filter.mode;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.concerto.searching.filter.bean.Item;
import com.concerto.searching.filter.functions.Search;
import com.concerto.searching.filter.functions.Filter;

//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import javax.persistence.criteria.Path;
import java.util.ArrayList;
import java.util.List;
//import org.springframework.stereotype.Service;
//import java.util.ArrayList;
//import java.util.List;

@Service
public class ItemService {

	public ApiResponse<List<Item>> searchAndFilter(Filter searchRequest) {
		List<Item> items = GenerateMockData(searchRequest);

		return new ApiResponse<>(items, items.size());
	}

	private List<Item> GenerateMockData(Filter searchRequest) {
		List<Item> mockItems = new ArrayList<>();
		mockItems.add(new Item(1L, "Pen", 10.0));
		mockItems.add(new Item(2L, "Pencile", 20.0));
		mockItems.add(new Item(3L, "Black Board", 30.0));
		mockItems.add(new Item(4L, "Chair", 15.0));
		mockItems.add(new Item(5L, "Bottel", 25.0));

		List<Item> filteredItems = new ArrayList<>();
		for (Item item : mockItems) {
			// Simulate applying filters based on search criteria
			// (searchRequest.getFilters())
			boolean passesFilters = true;
			for (Search filter : searchRequest.getFilters()) {
				String fieldName = filter.getFieldName();
				String operator = filter.getOperator();
				Object value = filter.getValue();

				// if ("name".equalsIgnoreCase(fieldName) && "like".equalsIgnoreCase(operator))
				// {
				// String itemName = item.getName().toLowerCase();
				// if (!itemName.contains((String) value)) {
				// passesFilters = false;
				// break;
				// }
				if ("name".equalsIgnoreCase(fieldName) && "like".equalsIgnoreCase(operator)) {
					String itemName = item.getName().toLowerCase(); // Convert to lowercase
					String searchValue = ((String) value).toLowerCase(); // Convert to lowercase
					if (!itemName.contains(searchValue)) {
						passesFilters = false;
						break;
					}
				} else if ("price".equalsIgnoreCase(fieldName) && "eq".equalsIgnoreCase(operator)) {
					Double itemPrice = item.getPrice();
					if (!itemPrice.equals(value)) {
						passesFilters = false;
						break;
					}
				}
			}
			if (passesFilters) {
				filteredItems.add(item);
			}
		}

		return filteredItems;
	}
}

//
// @Service
// public class ItemService {
//
// public ApiResponse<List<Item>> searchAndFilter(SearchRequest searchRequest) {
// List<Item> items = generateMockData(searchRequest);
// long totalItems = items.size();
//
// return new ApiResponse<>(items, totalItems);
// }
//
// private List<Item> generateMockData(SearchRequest searchRequest) {
// List<Item> mockItems = new ArrayList<>();
// mockItems.add(new Item(1L, "Mock Item 1", 10.0));
// mockItems.add(new Item(2L, "Mock Item 2", 20.0));
// mockItems.add(new Item(3L, "Mock Item 3", 30.0));
// mockItems.add(new Item(4L, "Another Mock Item", 15.0));
// mockItems.add(new Item(5L, "Yet Another Item", 25.0));
// // ... Add more mock items based on search criteria
//
// // Apply filters based on search criteria (searchRequest.getFilters())
// // Here, we're simulating filtering by name and price
// List<Item> filteredItems = new ArrayList<>();
// for (Item item : mockItems) {
// boolean passesFilters = true;
// for (SearchFilter filter : searchRequest.getFilters()) {
// String fieldName = filter.getFieldName();
// String operator = filter.getOperator();
// Object value = filter.getValue();
//
// if ("name".equalsIgnoreCase(fieldName) && "like".equalsIgnoreCase(operator))
// {
// String itemName = item.getName();
// if (!itemName.contains((String) value)) {
// passesFilters = false;
// break;
// }
// } else if ("price".equalsIgnoreCase(fieldName) &&
// "eq".equalsIgnoreCase(operator)) {
// Double itemPrice = item.getPrice();
// if (!itemPrice.equals(value)) {
// passesFilters = false;
// break;
// }
// }
// }
// if (passesFilters) {
// filteredItems.add(item);
// }
// }
//
// return filteredItems;
// }
// }

//
// @Service
// public class ItemService {
//
// @Autowired
// private ItemRepository itemRepository;
//
//
// public ApiResponse<List<Item>> searchAndFilter(SearchRequest searchRequest) {
// Specification<Item> specification =
// createSpecification(searchRequest.getFilters());
// Pageable pageable = PageRequest.of(searchRequest.getPage(),
// searchRequest.getSize());
//
// Page<Item> page = itemRepository.findAll(specification, pageable);
//
// // Populate ApiResponse with data from the database
// List<Item> items = page.getContent();
// long totalItems = page.getTotalElements();
// return new ApiResponse<List<Item>>(items, totalItems);
// }
//
//
// private Specification<Item> createSpecification(List<SearchFilter> filters) {
// return (root, query, criteriaBuilder) -> {
// List<Predicate> predicates = new ArrayList<>();
//
// for (SearchFilter filter : filters) {
// String fieldName = filter.getFieldName();
// String operator = filter.getOperator();
// Object value = filter.getValue();
//
// Path<String> fieldPath = root.get(fieldName);
//
// if ("eq".equalsIgnoreCase(operator)) {
// predicates.add(criteriaBuilder.equal(fieldPath, value));
// } else if ("like".equalsIgnoreCase(operator)) {
// predicates.add(criteriaBuilder.like(fieldPath, "%" + value + "%"));
// } // Add more operators as needed
//
// }
//
// return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
// };
// }
// }
