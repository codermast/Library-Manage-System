package com.bite.mybook.dao;

import com.bite.mybook.bean.Book;
import com.bite.mybook.biz.BookBiz;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookDao {
    QueryRunner runner = new QueryRunner();

    // 查询同一类型的所有书籍
    public List<Book> getBookByTypeId(long typeId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from book where typeId=?";

        List<Book> books = runner.query(connection, sql, new BeanListHandler<>(Book.class), typeId);

        connection.close();

        return books;
    }

    // 根据书籍id获取book对象
    public Book getBookByBookId(long bookId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from book where id=?";

        Book book = runner.query(connection, sql, new BeanHandler<>(Book.class), bookId);

        connection.close();

        return book;
    }

    // 添加一个书籍
    public boolean add(long typeId, String name, double price, String desc, String pic, String publish, String author, long stock, String address) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "insert into book (typeId,`name`,price,`desc`,pic,publish,author,stock,address) values (?,?,?,?,?,?,?,?,?)";

        Book book = runner.insert(connection, sql, new BeanHandler<>(Book.class), typeId, name, price, desc, pic, publish, author, stock, address);

        connection.close();

        return true;
    }

    // 删除一个书籍
    public boolean remove(long bookId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "delete from book where id=?";

        int line = runner.update(connection, sql, bookId);

        connection.close();

        return line == 1;
    }

    // 修改一个书籍
    public boolean modify(long bookId,long typeId, String name, double price, String desc, String pic, String publish, String author, long stock, String address) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "update book set typeId=?, `name`=?, price=?, `desc`=?, pic=?, publish=?, author=?, stock=?, address=? where id=?";

        int line = runner.update(connection,sql,typeId, name, price, desc, pic, publish, author, stock, address,bookId);

        connection.close();

        return line == 1;
    }
    // 获取有多少本书
    public int getCount() throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select count(*) from book";

        long line = runner.query(connection, sql, new ScalarHandler<>());
        connection.close();
        return (int) line;
    }

    // 查询某一页的书籍
    public List<Book> getBookByPage(long page,long count) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from book limit ?,?";

        long isTruePageNum = 0;

        if (page > 1){
            isTruePageNum = (page - 1) * count;
        }

        List<Book> books = runner.query(connection, sql, new BeanListHandler<>(Book.class), isTruePageNum, count);

        connection.close();
        return books;
    }

    // 通过 name 查书籍信息
    public Book getBookByName(String name) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from book where `name`=?";

        Book book = runner.query(connection, sql, new BeanHandler<>(Book.class), name);

        connection.close();
        return book;
    }

    // 修改 book 信息
    public boolean modify(Book book) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "update book set typeId=?,`name`=?,price=?,`desc`=?,pic=?,publish=?,author=?,stock=?,address=? where id=?";

        int line = runner.update(connection, sql, book.getTypeId(), book.getName(), book.getPrice(), book.getDesc(), book.getPic(), book.getPublish(), book.getAuthor(), book.getStock(), book.getAddress(),book.getId());

        connection.close();

        return line == 1;
    }
}
