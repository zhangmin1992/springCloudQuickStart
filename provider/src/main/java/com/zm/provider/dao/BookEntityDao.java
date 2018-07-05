package com.zm.provider.dao;

import java.util.ArrayList;

import com.zm.provider.entity.Book;
import com.zm.provider.vo.BookInfoVO;

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
	
	/**
	 * 插入数据
	 * @param book
	 */
	void insertBook(Book book);
	
	ArrayList<String> getBookInfo();
	
	void batchUpdateBookInfo(ArrayList<BookInfoVO> list);
	
	void batchDelete(ArrayList<String> list);
}
