package com.bite.mybook.action;

import com.bite.mybook.bean.User;
import com.bite.mybook.biz.UserBiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/*
*   用户的三种请求
*       1. login    : 登录
*       2. exit     : 安全退出
*       3. modefyPwd:修改密码
* */

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    // 构建 UserBiz 对象
    UserBiz userBiz = new UserBiz();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 设置请求的字符集编码
        req.setCharacterEncoding("utf-8");
        // 设置响应的字符集编码
        resp.setContentType("text/html;charset=utf-8");

        HttpSession session = req.getSession();

        String method = req.getParameter("type");

        PrintWriter out = resp.getWriter();

        switch (method){
            case "login":
                String code = (String) session.getAttribute("code");
                String usercode = req.getParameter("valcode");

                // 如果验证码错误，即不用继续执行
                if (!code.equalsIgnoreCase(usercode)){
                    out.println("<script>alert('验证码输入错误！');location.href='login.html';</script>");
                    return;
                }

                String name = req.getParameter("name");
                String pwd = req.getParameter("pwd");

                // 获取用户的业务对象
                User user = userBiz.getUser(name, pwd);

                if (user == null){
                    // user == null 即为没有获取到user对象，即用户名或者密码不正确
                    out.println("<script>alert('用户名或者密码不正确！');location.href='login.html';</script>");
                }else {
                    // 此时获取到了user对象，即登陆成功
                    session.setAttribute("user",user);

                    out.println("<script>alert('登陆成功！');location.href='index.jsp';</script>");
                }
                break;
            case "exit":
                // 清除 session
                session.invalidate();
                // 跳转到 login.html
                out.println("<script>parent.window.location.href='login.html';</script>");
                break;
            case "modifyPwd":
                User userLogin = (User) session.getAttribute("user");

                String oldpwd = req.getParameter("pwd");    // 输入的原密码

                // 判断原密码是否正确
                if (!oldpwd.equals(userLogin.getPwd())){
                    out.println("<script>alert('原密码输入错误！');</script>");
                    return;
                }

                // 判断新密码两次是否相同
                String newpwd1 = req.getParameter("newpwd");
                String newpwd2 = req.getParameter("newpwd2");
                if (!newpwd1.equals(newpwd2)){
                    out.println("<script>alert('确认密码与新密码不一致！');</script>");
                    return ;
                }

                long id = userLogin.getId();

                // 此时可以进行密码的修改
                boolean b = userBiz.modifyPwd(id,newpwd1);
                if (b) {
                    out.println("<script>alert('密码修改成功，请重新登录！');parent.window.location.href='login.html';</script>");
                }else{
                    out.println("<script>alert('密码修改失败，请稍后重试！');parent.window.location.href='index.jsp';</script>");
                }
                break;
        }
    }
}
