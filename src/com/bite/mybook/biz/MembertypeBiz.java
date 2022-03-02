package com.bite.mybook.biz;

import com.bite.mybook.bean.Membertype;
import com.bite.mybook.dao.MembertypeDao;

import java.sql.SQLException;
import java.util.List;

public class MembertypeBiz {
    // 构建 MembertypeDao 对象
    MembertypeDao membertypeDao = new MembertypeDao();

    // 通过 typeid 查询 membertype
    public Membertype getByTypeId(long typeId) {
        Membertype membertype = null;
        try {
            membertype = membertypeDao.getByTypeId(typeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membertype;
    }

    // 查询所有 membertype
    public List<Membertype> getAll(){
        List<Membertype> membertypes = null;
        try {
            membertypes = membertypeDao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membertypes;
    }

}
