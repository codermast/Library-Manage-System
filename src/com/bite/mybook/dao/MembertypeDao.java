package com.bite.mybook.dao;

import com.bite.mybook.bean.Membertype;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MembertypeDao {

    QueryRunner runner = new QueryRunner();

    // 通过 typeid 查询 membertype
    public Membertype getByTypeId(long typeId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from membertype where id=?";

        Membertype membertype = runner.query(connection, sql, new BeanHandler<>(Membertype.class), typeId);

        connection.close();

        return membertype;
    }

    // 查询所有 membertype
    public List<Membertype> getAll() throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from membertype";

        List<Membertype> membertypes = runner.query(connection, sql, new BeanListHandler<>(Membertype.class));

        connection.close();

        return membertypes;
    }
}
