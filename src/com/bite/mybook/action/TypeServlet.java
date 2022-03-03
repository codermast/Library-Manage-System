package com.bite.mybook.action;

import com.bite.mybook.bean.Type;
import com.bite.mybook.biz.TypeBiz;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/type")
public class TypeServlet extends HttpServlet {
    // type类型的业务操作对象
    TypeBiz typeBiz = new TypeBiz();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求的字符集编码
        req.setCharacterEncoding("utf-8");
        // 设置响应的字符集编码
        resp.setContentType("text/html;charset=utf-8");

        PrintWriter out = resp.getWriter();

        //验证用户是否登录
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            out.println("<script>alert('请登录');parent.window.location.href='login.html';</script>");
            return;
        }

        ServletContext application = req.getServletContext();

        String type = req.getParameter("type");

        switch (type) {
            // 修改之前的操作
            case "modifypre":
                modifypre(req, resp, out, application);
                break;

            // 修改类型
            case "modify":
                modify(req,resp,out,application);
                break;

            // 删除类型
            case "remove":
                remove(req,resp,out,application);
                break;

            // 增加类型
            case "add":
                add(req,resp,out,application);
                break;
        }
    }

    // type 的修改前操作
    private void modifypre(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws ServletException, IOException {
        // 1.获取要修改对象的 id
        long id  = Long.parseLong(req.getParameter("id"));
        // 2.通过 id 获取到要修改的对象
        Type typeById = typeBiz.getTypeById(id);
        // 3.将要修改的对象添加到req对象中
        req.setAttribute("type",typeById);

        // 将请求转发到 修改页面
        req.getRequestDispatcher("type_modify.jsp").forward(req,resp);
    }

    // type 的修改操作
    private void modify(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) {
        long typdId = Long.parseLong(req.getParameter("typeId"));

        String typeName = req.getParameter("typeName");

        long parentType = Long.parseLong(req.getParameter("parentType"));

        boolean modify = typeBiz.modify(typdId, typeName, parentType);

        if (modify){
            List<Type> types = typeBiz.getAll();
            application.setAttribute("types",types);
            out.println("<script>alert('修改成功！');location.href='type_list.jsp';</script>");
        }else {
            out.println("<script>alert('修改失败！');location.href='type_list.jsp';</script>");
        }
    }

    // type 的添加
    private void add(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) {
        String typeName = req.getParameter("typeName");
        long parentTypeId = Long.parseLong(req.getParameter("parentType"));

        boolean add = false;
        try {
            add = typeBiz.add(typeName, parentTypeId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (add){
            List<Type> types = (List<Type>) application.getAttribute("types");
            Type typeByName = typeBiz.getTypeByName(typeName);
            types.add(typeByName);
            application.setAttribute("types",types);
            out.println("<script>alert('添加成功！');location.href='type_list.jsp';</script>");
            return;
        }else {
            out.println("<script>alert('添加失败！');location.href='type_add.jsp';</script>");
        }
    }

    // type 的删除操作
    private void remove(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) {
        long typeId = Long.parseLong(req.getParameter("id"));

        try {
            boolean remove = typeBiz.remove(typeId);
            if (remove){
                List<Type> types = (List<Type>) application.getAttribute("types");
                int index = 0;
                for (Type type1 : types) {
                    if (type1.getId() == typeId){
                        types.remove(index);
                    }
                    index++;
                }
                application.setAttribute("types",types);
                out.println("<script>alert('删除成功！');location.href='type_list.jsp';</script>");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
