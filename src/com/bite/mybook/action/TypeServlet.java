package com.bite.mybook.action;

import com.bite.mybook.bean.Type;
import com.bite.mybook.biz.TypeBiz;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/type")
public class TypeServlet extends HttpServlet {
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
        TypeBiz typeBiz = new TypeBiz();
        ServletContext application = req.getServletContext();

        String type = req.getParameter("type");

        switch (type){
            case "modifypre":
                break;
            // 删除类型
            case "remove":
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
                break;
            // 增加类型
            case "add":
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
                break;
        }
    }
}
