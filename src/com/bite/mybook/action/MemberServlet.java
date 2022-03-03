package com.bite.mybook.action;

import com.alibaba.fastjson.JSON;
import com.bite.mybook.bean.Member;
import com.bite.mybook.bean.Membertype;
import com.bite.mybook.biz.MemberBiz;
import com.bite.mybook.biz.MembertypeBiz;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/member")
public class MemberServlet extends HttpServlet {

    MemberBiz memberBiz = new MemberBiz();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        PrintWriter out = resp.getWriter();
        ServletContext application = req.getServletContext();

        String method = req.getParameter("type");

        switch (method){
            case "query":
                query(req,resp,out,application);
                break;
            case "addpre":
                addpre(req,resp,out,application);
                break;
            case "add":
                add(req,resp,out,application);
                break;
            case "modifypre":
                modifypre(req,resp,out,application);
                break;
            case "modify":
                modify(req,resp,out,application);
                break;
            case "remove":
                remove(req,resp,out,application);
                break;
            case "modifyrecharge":
                modifyrecharge(req,resp,out,application);
                break;
            case "doajax":
                doajax(req,resp,out,application);
                break;
        }
    }

    private void doajax(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) {
        String idNum = req.getParameter("idn");
        MembertypeBiz membertypeBiz = new MembertypeBiz();
        Member memberByIdNumber = memberBiz.getMemberByIdNumber(idNum);
        memberByIdNumber.setType(membertypeBiz.getByTypeId(memberByIdNumber.getTypeId()));
        String memberJsonStr = JSON.toJSONString(memberByIdNumber);
        out.print(memberJsonStr);
    }

    private void modifyrecharge(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws ServletException, IOException {
        String idNumber = req.getParameter("idNumber");
        double amount = Double.parseDouble(req.getParameter("amount"));

        boolean b = memberBiz.memberRecharge(idNumber, amount);

        if (b){
            List<Member> members = memberBiz.getAll();
            req.setAttribute("memberList",members);
            req.getRequestDispatcher("mem_list.jsp").forward(req, resp);
        }else {
            out.println("<script>alert('充值失败！');</script>");
        }

    }

    private void remove(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws ServletException, IOException {
        long memberId = Long.parseLong(req.getParameter("id"));

        boolean remove = memberBiz.remove(memberId);

        if (remove){
            List<Member> members = memberBiz.getAll();
            req.setAttribute("memberList",members);
            req.getRequestDispatcher("mem_list.jsp").forward(req, resp);
        }else {
            out.println("<script>alert('删除失败！');</script>");
        }
    }

    private void modifypre(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws ServletException, IOException {
        // 准备好 member
        long id = Long.parseLong(req.getParameter("id"));
        Member member = memberBiz.getById(id);
        req.setAttribute("member",member);

        // 转发到 jsp 页面
        req.getRequestDispatcher("mem_modify.jsp").forward(req, resp);
    }

    private void modify(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws ServletException, IOException {
        // 用户 id
        long id = Long.parseLong(req.getParameter("id"));
        // 用户名
        String name = req.getParameter("name");
        // 密码
        String pwd = req.getParameter("pwd");
        // 确认密码
        String pwd2 = req.getParameter("pwd2");
        // 会员类型
        long membertypeid = Long.parseLong(req.getParameter("memberType"));
        // 充值金额
        double balance = Double.parseDouble(req.getParameter("balance"));
        // 电话
        String tel = req.getParameter("tel");
        // 身份证号
        String idNumber = req.getParameter("idNumber");

        Member member = new Member();
        member.setId(id);
        member.setName(name);
        member.setPwd(pwd);
        member.setTypeId(membertypeid);
        member.setBalance(balance);
        member.setTel(tel);
        member.setIdNumber(idNumber);

        boolean modify = memberBiz.modify(member);

        if (modify){
            List<Member> members = memberBiz.getAll();
            req.setAttribute("memberList",members);
            req.getRequestDispatcher("mem_list.jsp").forward(req, resp);
        }else {
            out.println("<script>alert('修改失败！');</script>");
        }
    }

    private void addpre(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws ServletException, IOException {
        // 准备好所有的会员列表供用户下拉框选择
        MembertypeBiz membertypeBiz = new MembertypeBiz();

        List<Membertype> membertypes = membertypeBiz.getAll();
        req.setAttribute("memberTypes",membertypes);

        // 转发到 jsp 页面
        req.getRequestDispatcher("mem_add.jsp").forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws ServletException, IOException {
        // 用户名
        String name = req.getParameter("name");
        // 密码
        String pwd = req.getParameter("pwd");
        // 确认密码
        String pwd2 = req.getParameter("pwd2");
        // 会员类型
        long membertypeid = Long.parseLong(req.getParameter("memberType"));
        // 充值金额
        double balance = Double.parseDouble(req.getParameter("balance"));
        // 电话
        String tel = req.getParameter("tel");
        // 身份证号
        String idNumber = req.getParameter("idNumber");

        // 确认密码和密码不一致
        if (!pwd.equals(pwd2)){
            out.println("<script>alert('密码和确认密码不一致！');location.href='mem_list.jsp';</script>");
        }

        Member member = new Member();

        member.setName(name);
        member.setPwd(pwd);
        member.setTypeId(membertypeid);
        member.setBalance(balance);
        member.setTel(tel);
        member.setIdNumber(idNumber);

        boolean register = memberBiz.register(member);
        if (register){
            List<Member> members = memberBiz.getAll();
            req.setAttribute("memberList",members);
            req.getRequestDispatcher("mem_list.jsp").forward(req, resp);
        }else {
            out.println("<script>alert('注册失败！');</script>");
        }
    }

    private void query(HttpServletRequest req, HttpServletResponse resp, PrintWriter out, ServletContext application) throws ServletException, IOException {
        List<Member> members = memberBiz.getAll();

        req.setAttribute("memberList",members);

        // 转发到 jsp 页面
        req.getRequestDispatcher("mem_list.jsp").forward(req, resp);
    }
}
