package com.bite.mybook.biz;

import com.bite.mybook.bean.Book;
import com.bite.mybook.bean.Type;
import com.bite.mybook.dao.TypeDao;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TypeBiz {
    // 构建 TypeDao 对象
    TypeDao typeDao = new TypeDao();

    // 通过typeid查询一个 type 类型
    public Type getTypeById(long typeId) {
        Type typeById = null;
        try {
            typeById = typeDao.getTypeById(typeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return typeById;
    }

    // 通过typename查询一个 type 类型
    public Type getTypeByName(String typeName){
        Type typeByName = null;
        try {
            typeByName = typeDao.getTypeByName(typeName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeByName;
    }

    // 查询所有 type 类型
    public List<Type> getAll(){
        List<Type> list = null;
        try {
            list = typeDao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 修改一个类型
    public boolean modify(long id,String newname,long newparentId){
        int line = 0;

        try {
            line = typeDao.modify(id,newname,newparentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return line == 1;
    }

    // 添加一个 type 类型
    public boolean add(String name,long parentId) throws Exception {
        try {
            Type typeByName = typeDao.getTypeByName(name);
            if (typeByName!= null){
                throw new Exception("类型已经存在无法添加");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int line = 0;
        try {
            line = typeDao.add(name, parentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return line == 1;
    }

    // 添加多个类型
    public boolean addAll(List<Type> list){
        int count = 0;
        try {
            count = typeDao.addAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count == list.size();
    }

    // 删除一个类型
    // 三种情况：1.类型不存在 2.类型存在但是有实际的子信息 3.类型存在并且可以删除
    public boolean remove(long id) throws Exception {
        BookBiz bookBiz = new BookBiz();

        List<Book> bookByTypeId = bookBiz.getBookByTypeId(id);

        if (bookByTypeId.size() != 0){
            // 即有对应类型的子信息，则不能删除
            throw new Exception("请先清空对应类型的书籍，然后再进行删除！");
        }

        int line = 0;
        try {
            line = typeDao.remove(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return line == 1;
    }
}
