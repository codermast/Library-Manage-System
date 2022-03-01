package com.bite.mybook.biz;

import com.bite.mybook.bean.Book;
import com.bite.mybook.dao.BookDao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BookBiz {
    // 构建 BookDao 对象
    BookDao bookDao = new BookDao();

    // 根据类型查书籍
    public List<Book> getBookByTypeId(long typeId){
        List<Book> books = new LinkedList<>();

        try {
            books = bookDao.getBookByTypeId(typeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
}
