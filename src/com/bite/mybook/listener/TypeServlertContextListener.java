package com.bite.mybook.listener;

import com.bite.mybook.bean.Type;
import com.bite.mybook.biz.TypeBiz;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class TypeServlertContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 1.获取 type 列表
        TypeBiz typeBiz = new TypeBiz();
        List<Type> types = typeBiz.getAll();
        // 2.获取application 对象
        ServletContext application = servletContextEvent.getServletContext();
        // 3.将 types 列表添加到 application 对象中
        application.setAttribute("types",types);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
