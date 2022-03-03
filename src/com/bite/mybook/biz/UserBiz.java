package com.bite.mybook.biz;

import com.bite.mybook.bean.User;
import com.bite.mybook.dao.UserDao;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class UserBiz {
    // 构建 UserDao 对象
    UserDao userDao = new UserDao();

    // 获取查询的 user 对象
    public User getUser(String name,String pwd){
        User user = null;
        try {
            user = userDao.getUser(name, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // 修改密码
    public boolean modifyPwd(long id,String newpwd){
        boolean isModify = false;
        try {
            isModify = (userDao.modifyPwd(id,newpwd) == 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isModify;
    }

    /*
     *   获取用户对象
     * */
    public User getUserById(long id){
        User user = null;
        try {
            user = userDao.getUserById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
