package com.concerto.searching.filter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concerto.searching.filter.bean.Item;
import com.concerto.searching.filter.repository.ItemRepository;
import com.concerto.searching.filter.mode.ApiResponse;

import java.util.List;

@Service
public class ItemService {
	

	@Autowired
	  private ItemRepository itemRepository;



	 public ApiResponse<List<Item>> searchAndFilter() {
		 List<Item> items = itemRepository.findAll();
	        return new ApiResponse<>(items, items.size());
	    }
}
