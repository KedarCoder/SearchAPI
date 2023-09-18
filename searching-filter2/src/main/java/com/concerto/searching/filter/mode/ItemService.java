package com.concerto.searching.filter.mode;

import org.springframework.stereotype.Service;
import com.concerto.searching.filter.bean.Item;
import com.concerto.searching.filter.bean.Pen;
import com.concerto.searching.filter.functions.Search;
import com.concerto.searching.filter.functions.Filter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

	public ApiResponse<List<Item>> searchAndFilter(Filter searchRequest) {
		List<Item> items = GenerateMockData(searchRequest);

		return new ApiResponse<>(items, items.size());
	}

		private List<Item> GenerateMockData(Filter searchRequest) {
		List<Item> mockItems = new ArrayList<>();
		mockItems.add(new Item(1L, "Pen", 10.0, new Pen(1L, "ExamTime", "Cello")));
		mockItems.add(new Item(2L, "Pencile", 20.0, new Pen(2L, "Extra Dark", "Lexuy")));
		mockItems.add(new Item(3L, "Pencile", 30.0, new Pen(3L, "Light", "Sonata")));
		mockItems.add(new Item(4L, "Chair", 15.0, new Pen(4L, "Flash", "Fotrt")));
		mockItems.add(new Item(5L, "Bottel", 25.0, new Pen(5L, "Caption", "Camelin")));

		List<Item> filteredItems = new ArrayList<>();
		for (Item item : mockItems) {

			boolean passesFilters = true;
			for (Search filter : searchRequest.getFilters()) {
				String fieldName = filter.getFieldName();
				String operator = filter.getOperator();
				Object value = filter.getValue();

				if ("name".equalsIgnoreCase(fieldName) && "like".equalsIgnoreCase(operator)) {
					String itemName = item.getName().toLowerCase(); // Convert to lowercase
					String searchValue = ((String) value).toLowerCase(); // Convert to lowercase
					if (!itemName.contains(searchValue)) {
						passesFilters = false;
						break;
					}
				} else if ("price".equalsIgnoreCase(fieldName)) {
					Double itemPrice = item.getPrice();
					Double filterValue = (Double) value;
					if ("ge".equalsIgnoreCase(operator) && itemPrice < filterValue) {
						passesFilters = false;
						break;
					} else if ("le".equalsIgnoreCase(operator) && itemPrice > filterValue) {
						passesFilters = false;
						break;
					} else if ("eq".equalsIgnoreCase(operator) && !itemPrice.equals(filterValue)) {
						passesFilters = false;
						break;
					}
				} else if ("brand".equalsIgnoreCase(fieldName) && "like".equalsIgnoreCase(operator)) {
					String brandName = item.getPen().getBrand().toLowerCase(); // Convert to lowercase
					String searchValue = ((String) value).toLowerCase(); // Convert to lowercase
					if (!brandName.contains(searchValue)) {
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
