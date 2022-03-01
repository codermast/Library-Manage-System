package com.bite.mybook.dao;

import com.bite.mybook.bean.Book;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookDao {
    QueryRunner runner = new QueryRunner();

    // 根据类型查书籍
    public List<Book> getBookByTypeId(long typeId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from book where typeId=?";

        List<Book> books = runner.query(connection, sql, new BeanListHandler<Book>(Book.class), typeId);

        connection.close();

        return books;
    }
}
