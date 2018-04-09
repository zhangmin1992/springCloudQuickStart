package com.zm.provider.dao;

import com.zm.provider.entity.Book;

public interface BookEntityDao {

	/**
	 * 根据主键查找Book信息
	 * @param id
	 * @return
	 */
	Book getBook(Long id);
	
	/**
	 * 根据name查询
	 * @param name
	 * @return
	 */
	Book getBookByName(String name);
	
	void insertBook(Book book);
}
