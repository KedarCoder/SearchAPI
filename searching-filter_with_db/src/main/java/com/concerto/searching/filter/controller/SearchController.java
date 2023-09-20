package com.concerto.searching.filter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concerto.searching.filter.bean.Item;
import com.concerto.searching.filter.mode.ApiResponse;
import com.concerto.searching.filter.service.ItemService;

@RestController
@RequestMapping("/restC")
public class SearchController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/searchItem")
    public ResponseEntity<ApiResponse<List<Item>>> searchAndFilterItems() {
        ApiResponse<List<Item>> response = itemService.searchAndFilter();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getString")
    public String searchAndFilterItemss() {
        return "King is back";
    }
}

