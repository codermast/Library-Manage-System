package com.bite.mybook.dao;

import com.bite.mybook.bean.Record;
import com.bite.mybook.biz.MemberBiz;
import com.bite.mybook.biz.RecordBiz;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RecordDao {
    QueryRunner runner = new QueryRunner();
    MemberBiz memberBiz = new MemberBiz();

    // 查询全部借阅记录
    public List<Record> getAll() throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from record";

        List<Record> records = runner.query(connection, sql, new BeanListHandler<>(Record.class));

        connection.close();

        return records;
    }

    // 查询已归还记录
    public List<Record> getReturned() throws SQLException {
        Connection connection = DBHelper.getConnection();
        String sql = "select * from record where isBack=1";
        List<Record> records = runner.query(connection, sql, new BeanListHandler<>(Record.class));
        connection.close();
        return records;
    }

    // 查询未归还记录
    public List<Record> getNotReturn() throws SQLException {
        Connection connection = DBHelper.getConnection();
        String sql = "select * from record where isBack=0";
        List<Record> records = runner.query(connection, sql, new BeanListHandler<>(Record.class));
        connection.close();
        return records;
    }

    // 增加用户借阅记录
    public boolean add(long memberId, long bookId, double deposit, long userId) throws SQLException {
        Connection connection = DBHelper.getConnection();
        String sql = "insert into record (memberId, bookId, rentDate, backDate, deposit, userId) values (?,?,CURRENT_DATE,null,?,?)";

        int line = runner.update(connection, sql, memberId,bookId, deposit, userId);

        connection.close();
        return line == 1;
    }

    // 通过用户 id 查询借阅记录
    public List<Record> getRecordById(long id) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from record where memberId=?";

        List<Record> records = runner.query(connection, sql, new BeanListHandler<>(Record.class), id);

        connection.close();

        return records;
    }

    // 归还书籍
    public boolean backBooks(long mid, long id) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "update record set backDate=CURRENT_DATE,isBack=1 where memberId=? and id=?";
        runner.update(connection,sql,mid,id);

        String sql2 = "select * from record where memberId=? and bookId=?";
        List<Record> records = runner.query(connection, sql2, new BeanListHandler<>(Record.class), mid, id);
        boolean isBackAllTrue = true;
        for (Record record : records) {
            // 押金
            double deposit = record.getDeposit();
            // 退还用户押金
            boolean b = memberBiz.memberChangeByMid(record.getMemberId(), deposit);
            isBackAllTrue = b && isBackAllTrue;
        }
        connection.close();
        return isBackAllTrue;
    }


    public static void main(String[] args) {
        RecordDao recordDao = new RecordDao();
        List<Record> all;
        try {
            all = recordDao.getReturned();
            System.out.println(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
