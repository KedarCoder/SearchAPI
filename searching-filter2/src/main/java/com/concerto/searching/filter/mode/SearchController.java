package com.concerto.searching.filter.mode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.concerto.searching.filter.bean.Item;
import com.concerto.searching.filter.bean.Pen;
import com.concerto.searching.filter.functions.Filter;

@RestController
public class SearchController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private PenService penService;

	@PostMapping("/searchItem")
	public ResponseEntity<ApiResponse<List<Item>>> searchAndFilterItems(@RequestBody Filter searchRequest) {
		ApiResponse<List<Item>> response = itemService.searchAndFilter(searchRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/searchPen")
	public ResponseEntity<ApiResponse<List<Pen>>> searchAndFilterPen(@RequestBody Filter searchRequest)throws Exception {
		ApiResponse<List<Pen>> response = penService.searchAndFilter(searchRequest);
		return ResponseEntity.ok(response);
	}
}
