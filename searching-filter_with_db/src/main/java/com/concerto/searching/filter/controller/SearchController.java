package com.concerto.searching.filter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.concerto.searching.filter.bean.Item;
import com.concerto.searching.filter.functions.Filter;
import com.concerto.searching.filter.mode.ApiResponse;
import com.concerto.searching.filter.service.ItemService;

@RestController
@RequestMapping("/restC")
public class SearchController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Item>>> searchAndFilterItems() {
        ApiResponse<List<Item>> response = itemService.search();
        return ResponseEntity.ok(response);
    }
  
    @PostMapping("/searchItem")
    public ResponseEntity<ApiResponse<List<Item>>> searchAndFilterItems(@RequestBody Filter searchRequest) {
        ApiResponse<List<Item>> response = itemService.searchAndFilter(searchRequest);
        return ResponseEntity.ok(response);
    }
   }


//{
//	  "filters": [
//	    {
//	      "fieldName": "name",
//	      "operator": "CONTAINS",
//	      "value": "Item"
//	    }
//	  ],
//	  "page": 1,
//	  "size": 10
//	}
