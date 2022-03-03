package com.bite.mybook.dao;

import com.bite.mybook.bean.Member;
import com.bite.mybook.biz.MemberBiz;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MemberDao {
    QueryRunner runner = new QueryRunner();

    // 会员注册
    public boolean register(Member member) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "insert into member (name,pwd,typeId,balance,regdate,tel,idNumber) values (?,?,?,?,?,?,?)";

        int line = runner.update(connection,
                sql,
                member.getName(),
                member.getPwd(),
                member.getTypeId(),
                member.getBalance(),
                member.getRegdate(),
                member.getTel(),
                member.getIdNumber());
        connection.close();
        return line == 1;
    }

    // 会员注销
    public boolean remove(long memberId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "delete from member where id=?";

        int line = runner.update(connection, sql, memberId);

        connection.close();

        return line == 1;
    }

    // 会员修改
    public boolean modify(Member member) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "update `member` set `name`=?,pwd=?,typeId=?,balance=?,regdate=?,tel=?,idNumber=? where id=?";

        int line = runner.update(connection,
                sql,
                member.getName(),
                member.getPwd(),
                member.getTypeId(),
                member.getBalance(),
                member.getRegdate(),
                member.getTel(),
                member.getIdNumber(),
                member.getId());

        connection.close();

        return line == 1;
    }

    // 会员查询：1.查询所有 2.通过id 3.通过类别 4.通过名称
    public List<Member> getAll() throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from member";

        List<Member> members = runner.query(connection, sql, new BeanListHandler<>(Member.class));
        connection.close();
        return members;
    }

    public Member getById(long memberId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from member where id=?";

        Member retMember = runner.query(connection, sql, new BeanHandler<>(Member.class), memberId);
        connection.close();

        return retMember;
    }

    // 通过类型获取对象
    public List<Member> getByTypeId(long typeId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from member where typeId=?";

        List<Member> members = runner.query(connection, sql, new BeanListHandler<>(Member.class), typeId);

        connection.close();

        return members;
    }


    // 通过 名称 获取对象
    public Member getByName(String name) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from member where name=?";

        Member member = runner.query(connection, sql, new BeanHandler<>(Member.class), name);
        connection.close();
        return member;
    }

    // 会员充值

    // 通过身份证号修改余额
    public boolean memberRecharge(String idNumber,double amount) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String updateSql = "update member set balance=balance+? where idNumber=?";
        int line = runner.update(connection, updateSql, amount, idNumber);

        connection.close();

        return line > 0;
    }

    // 通过mId修改余额

    /*
    *   amount > 0 : 借书，付押金
    *   amount < 0 : 还书，退押金
    * */
    public boolean memberChangeByMid(long mid,double amount) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "update member set balance=balance+? where id=?";

        int update = runner.update(connection, sql, amount, mid);

        connection.close();
        return update == 1;
    }

    // 通过身份证号获取会员信息
    public Member getMemberByIdNumber(String idNumber) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from member where idNumber=?";

        Member member = runner.query(connection, sql, new BeanHandler<>(Member.class), idNumber);
        connection.close();
        return member;
    }

    // 通过 id 号获取会员信息
    public Member getMemberById(long id) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from member where id=?";

        Member member = runner.query(connection, sql, new BeanHandler<>(Member.class), id);
        connection.close();
        return member;
    }


    public long getIdByIdNum(String idNumber) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from member where idNumber=?";

        Member member = runner.query(connection, sql, new BeanHandler<>(Member.class), idNumber);
        connection.close();
        return member.getId();
    }


    public static void main(String[] args) {
        MemberDao memberDao = new MemberDao();

        try {
            memberDao.memberChangeByMid(2,100);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
