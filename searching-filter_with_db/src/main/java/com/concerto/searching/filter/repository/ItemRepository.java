package com.concerto.searching.filter.repository;

import org.springframework.stereotype.Repository;

import com.concerto.searching.filter.bean.Item;

@Repository
public interface ItemRepository extends GenericRepository<Item, Long> {
}