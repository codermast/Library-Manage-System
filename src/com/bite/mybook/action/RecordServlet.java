package com.bite.mybook.action;

import com.alibaba.fastjson.JSON;
import com.bite.mybook.bean.*;
import com.bite.mybook.biz.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/record")
public class RecordServlet extends HttpServlet {

    // 构建 recordBiz 对象
    RecordBiz recordBiz = new RecordBiz();

    MemberBiz memberBiz = new MemberBiz();
    MembertypeBiz membertypeBiz = new MembertypeBiz();
    BookBiz bookBiz = new BookBiz();
    UserBiz userBiz = new UserBiz();

    /*
     *
     * /record?type=add&mid=1&ids=5_4_9_10 图书借阅
     * /record?type=queryback&idn=xx:根据会员的身份证号查询会员信息及借阅信息
     * /record?type=back&mid=1&ids_5_4 图书归还
     * /record?type=keep&id=x 书籍续借
     * /record?type=doajax&typeId=x&keyword=xx ajax查询*/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();

        //验证用户是否登录
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            out.println("<script>alert('请登录');parent.window.location.href='login.html';</script>");
            return;
        }

        User user = (User) session.getAttribute("user");


        ServletContext application = req.getServletContext();

        String type = req.getParameter("type");
        switch (type) {
            // 借阅书籍
            case "add":
                add(req, resp, out, application, user);
                break;
            // do-ajax 借阅记录
            case "doajax":
                doajax(req, resp, out, application);
                break;
            // 归还
            case "back":
                back(req, resp, out, application);
                break;
            //  查询借阅记录
            case "queryback":
                queryback(req, resp, out, application);
                break;
            default:
                resp.sendError(404);
        }
    }

    private void add(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application, User user) {
        long mid = Long.parseLong(req.getParameter("mid"));
        // 格式为 3_4_5
        // String ids = req.getParameter("ids");
        String[] split = req.getParameter("ids").split("_");
        long[] arr = new long[split.length];

        for (int i = 0; i < split.length; i++) {
            arr[i] = Long.parseLong(split[i]);
        }

        long userId = user.getId();

        boolean add = recordBiz.add(mid, arr, userId);

        if (add) {
            List<Record> records = recordBiz.getRecordById(mid);
            req.setAttribute("records", records);
            out.println("<script>alert('图书借阅成功');location.href='main.jsp';</script>");
        } else {
            out.println("<script>alert('图书借阅失败');location.href='main.jsp';</script>");
        }
    }

    private void doajax(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws IOException {
        String typeId = req.getParameter("typeId");
        List<Record> records = new LinkedList<>();

        switch (typeId) {
            // 全部
            case "0":
                records = recordBiz.getAll();
                break;
            // 已归还
            case "1":
                records = recordBiz.getReturned();
                break;
            // 未归还
            case "2":
                records = recordBiz.getNotReturn();
                break;
            default:
                resp.sendError(404);
        }

        for (Record record : records) {
            Member member = memberBiz.getById(record.getMemberId());
            Book book = bookBiz.getBookByBookId(record.getBookId());
            User user = userBiz.getUserById(record.getUserId());

            record.setMember(member);
            record.setBook(book);
            record.setUser(user);
        }

        String json = JSON.toJSONString(records);
        out.print(json);
    }

    private void back(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) {
        long mid = Long.parseLong(req.getParameter("mid"));
        String[] split = req.getParameter("ids").split("_");
        boolean backAll = recordBiz.backBooks(mid, split);
        if (backAll) {
            out.println("<script>alert('图书归还成功');location.href='main.jsp';</script>");
        } else {
            out.println("<script>alert('图书归还失败');location.href='main.jsp';</script>");
        }
    }

    // 查询用户的借阅记录
    private void queryback(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws ServletException, IOException {
        String idNubmer = req.getParameter("idn");
        List<Record> records = recordBiz.getRecordByIdNumber(idNubmer);

        Member member = memberBiz.getMemberByIdNumber(idNubmer);
        Membertype membertype = membertypeBiz.getByTypeId(member.getTypeId());
        member.setType(membertype);


        req.setAttribute("member", member);
        req.setAttribute("records", records);

        req.getRequestDispatcher("return_list.jsp").forward(req, resp);
    }
}
