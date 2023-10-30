package com.concerto.searching.filter.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.concerto.searching.filter.bean.Category;
import com.concerto.searching.filter.bean.Item;

@Repository
public interface ItemRepository extends GenericRepository<Item, Long> {
	List<Item> findByCategory(Category category);

}