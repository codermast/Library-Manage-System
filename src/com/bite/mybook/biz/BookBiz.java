package com.bite.mybook.biz;

import com.bite.mybook.bean.Book;
import com.bite.mybook.bean.Type;
import com.bite.mybook.dao.BookDao;
import com.bite.mybook.dao.TypeDao;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BookBiz {
    // 构建 BookDao 对象
    BookDao bookDao = new BookDao();

    // 查询同一类型的所有书籍
    public List<Book> getBookByTypeId(long typeId){
        List<Book> books = new LinkedList<>();
        try {
            books = bookDao.getBookByTypeId(typeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // 根据书籍id获取book对象
    public Book getBookByBookId(long bookId){
        Book book = null;
        try {
            book = bookDao.getBookByBookId(bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    // 添加一个书籍
    public boolean add(Book book){
        boolean isAdd = false;
        try {
            isAdd = bookDao.add(book.getTypeId(), book.getName(),  book.getPrice(),  book.getDesc(), book.getPic(),  book.getPublish(),  book.getAuthor(),  book.getStock(),  book.getAddress());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAdd;
    }

    // 删除一个书籍
    public boolean remove(long bookId){
        boolean isTrue = false;
        try {
            isTrue = bookDao.remove(bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isTrue;
    }

    // 修改一个书籍
    public boolean modify(long bookId,long typeId, String name, double price, String desc, String pic, String publish, String author, long stock, String address){
        boolean isTrue = false;
        try {
            isTrue = bookDao.modify(bookId, typeId, name, price, desc, pic, publish, author, stock, address);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isTrue;
    }

    // 获取有多少本书
    public int getCount(){
        int count = 0;
        try {
            count = bookDao.getCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // 查询某一页的书籍
    public List<Book> getBookByPage(long page,long count){
        TypeDao typeDao = new TypeDao();

        List<Book> books = null;
        try {
            books = bookDao.getBookByPage(page,count);
            // 查询完以后，这里列表中的 book 对象的 type 属性 还是默认值 null ，因为数据库中没有存储该book的type,仅存储了 typeId
            for (Book book : books) {
                long typeId = book.getTypeId();
                book.setType(typeDao.getTypeById(typeId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // 通过 name 查书籍信息
    public Book getBookByName(String name){
        Book book = null;
        try {
            book = bookDao.getBookByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    // 修改 book 信息
    public boolean modify(Book book) {
        boolean modify = false;
        try {
            modify = bookDao.modify(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modify;
    }
}
