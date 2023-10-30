package com.concerto.searching.filter.repository;

import com.concerto.searching.filter.bean.Category;

public interface CategoryRepository extends GenericRepository<Category, Long> {

	Category findByCategoryName(String categoryName);
	
}
