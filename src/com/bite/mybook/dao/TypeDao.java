package com.bite.mybook.dao;

import com.bite.mybook.bean.Type;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TypeDao {
    // 构建 Queryrunner 对象
    QueryRunner runner = new QueryRunner();


    // 修改一个类型
    public int modify(long id,String newname,long newparentId) throws SQLException {
        Connection connection = DBHelper.getConnection();
        String sql = "update type set name=?,parentId=? where id = ?";

        int line = runner.update(connection, sql, newname, newparentId, id);
        connection.close();

        return line;
    }

        // 添加一个类型
    // 这里的 主键 id 数据库内是自增的，所以不需要用户提供
    public int add(String name,long parentId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "insert into type (name,parentId) values (?,?)";

        int line = runner.update(connection, sql, name, parentId);

        connection.close();

        return line;
    }

    // 添加多个类型
    public int addAll(List<Type> list) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "insert into type (name,parentId) values (?,?)";
        // 一共添加多少个类型
        int count = 0;
        for (Type type : list) {
            int line = runner.update(connection, sql, type.getName(), type.getParentId());
            if (line == 1){
                count += 1;
            }else {
                throw new SQLException("添加失败");
            }
        }
        connection.close();

        return count;
    }
    // 通过typeid查询一个 type 类型
    public Type getTypeById(long typeId) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from type where id=?";

        Type type = runner.query(connection, sql, new BeanHandler<Type>(Type.class),typeId);

        connection.close();

        return type;
    }

    // 通过typename查询一个 type 类型
    public Type getTypeByName(String typeName) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from type where name=?";

        Type type = runner.query(connection, sql, new BeanHandler<Type>(Type.class),typeName);

        connection.close();

        return type;
    }

    // 查询所有 type 类型
    public List<Type> getAll() throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "select * from type";

        List<Type> list = runner.query(connection, sql, new BeanListHandler<Type>(Type.class));
        connection.close();

        return list;
    }

    // 删除一个类型
    public int remove(long id) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String sql = "delete from type where id = ?";
        int line = runner.update(connection, sql, id);
        connection.close();
        return line;
    }
}
