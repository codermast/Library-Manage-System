package com.bite.mybook.dao;

import com.bite.mybook.bean.User;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDao {
    // JDBC -> DBUtils;
    QueryRunner runner = new QueryRunner();


    /*
     *   获取用户对象
     * */
    public User getUser(String name, String pwd) throws SQLException {
        // 第一步：调用 DBHelper 获取连接对象
        Connection connection = DBHelper.getConnection();

        // 第二步：准备好需要执行的SQL语句
        String sql = "select * from user where name=? and pwd=? and state=1";

        // 第三步：调用查询方法，将返回的查询结果进行封装
        User user = runner.query(connection, sql, new BeanHandler<User>(User.class), name, pwd);

        // 第四步：关闭连接
        connection.close();

        // 第五步：返回 user
        return user;
    }

    /*
     *   修改密码
     * */
    public int modifyPwd(long id, String newpwd) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "update user set pwd=? where id=? and state=1";

        // line 为修改的行数
        int line = runner.update(connection, sql,newpwd, id);

        // 关闭连接
        connection.close();

        // 此时即修改成功
        return line;
    }
}
