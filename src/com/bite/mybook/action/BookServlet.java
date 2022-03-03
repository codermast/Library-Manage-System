package com.bite.mybook.action;

import com.alibaba.fastjson.JSON;
import com.bite.mybook.bean.Book;
import com.bite.mybook.biz.BookBiz;
import com.bite.mybook.util.DateHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/*
 *   1. query 查询
 *   2.
 * */

@WebServlet("/book")
public class BookServlet extends HttpServlet {
    // Book类型的业务操作对象
    BookBiz bookBiz = new BookBiz();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * /book?type=add 添加图书
     * /book?type=modifypre&id=xx 修改前准备
     * /book?type=modify        修改
     * /book?type=remove&id=xx    删除
     * /book?type=query&pageIndex=1 :分页查询(request:转发)
     * /book?type=details&id=xx   展示书籍详细信息
     * /book?type=doajax&name=xx  :使用ajax查询图书名对应的图书信息
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
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

        String type = req.getParameter("type");
        switch (type) {
            case "add":
                try {
                    add(req, resp, out);
                } catch (FileUploadException e) {
                    e.printStackTrace();
                    resp.sendError(500, "文件上传失败");
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.sendError(500, e.getMessage());
                }
                break;
            case "modifypre":
                //book?type=modifypre&id=xx
                long bookId = Long.parseLong(req.getParameter("id"));
                Book book = bookBiz.getBookByBookId(bookId);
                req.setAttribute("book", book);
                req.getRequestDispatcher("book_modify.jsp").forward(req, resp);
                break;
            case "modify":
                try {
                    modify(req, resp, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "remove":
                //1.获取删除的bookid
                long removeId = Long.parseLong(req.getParameter("id"));

                //2. 调用biz删除方法
                try {
                    boolean count = bookBiz.remove(removeId);
                    if (count) {
                        out.println("<script>alert('图书删除成功'); location.href='book?type=query&pageIndex=1';</script>");
                    } else {
                        out.println("<script>alert('图书删除失败'); location.href='book?type=query&pageIndex=1';</script>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<script>alert('" + e.getMessage() + "'); location.href='book?type=query&pageIndex=1';</script>");
                }
                //3. 提示+跳转=>out (查询的servlet)
                break;
            case "query":
                query(req, resp, out);
                break;
            case "details":
                details(req, resp, out);
                break;
            case "doajax":
                String name = req.getParameter("name");
                Book book2 = bookBiz.getBookByName(name);
                if (book2 == null) {
                    out.print("{}");//null json 对象
                } else {
                    out.print(JSON.toJSONString(book2));
                }
                break;
            default:
                resp.sendError(404);
        }
    }

    /**
     * 修改图书信息的方法
     *
     * @param req
     * @param resp
     * @param out
     */
    private void modify(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws Exception {
        //1.构建一个磁盘工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //1.1 设置大小
        factory.setSizeThreshold(1024 * 9);
        //1.2 临时仓库
        File file = new File("c:\\temp");
        if (!file.exists()) {
            file.mkdir();//创建文件夹
        }
        factory.setRepository(file);

        //2.文件上传+表单数据
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        //3.将请求解析成一个个FileItem(文件+表单元素)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //4.遍历FileItem
        Book book = new Book();
        for (FileItem item : fileItems) {
            if (item.isFormField()) {
                //4.1 元素名称和用户填写的值  name: 文城
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
                switch (name) {
                    case "id":
                        book.setId(Long.parseLong(value));
                        break;
                    case "pic":
                        book.setPic(value);
                        break;
                    case "typeId":
                        book.setTypeId(Long.parseLong(value));
                        break;
                    case "name":
                        book.setName(value);
                        break;
                    case "price":
                        book.setPrice(Double.parseDouble(value));
                        break;
                    case "desc":
                        book.setDesc(value);
                        break;
                    case "publish":
                        book.setPublish(value);
                        break;
                    case "author":
                        book.setAuthor(value);
                        break;
                    case "stock":
                        book.setStock(Long.parseLong(value));
                        break;
                    case "address":
                        book.setAddress(value);
                        break;
                }

            } else {
                //4.2 文件: 图片的文件名  文城.png,用户不选择图片时，fileName的数据为""
                String fileName = item.getName();
                //避免文件替换：当前的系统时间.png
                if (fileName.trim().length() > 0) {
                    //4.2.1 获取后缀名 .png
                    String filterName = fileName.substring(fileName.lastIndexOf("."));
                    //4.2.2 修改文件名  20211117112512234.png
                    fileName = DateHelper.getImageName() + filterName;
                    //保存到哪里
                    //虚拟路径: Images/cover/1-1.png
                    //文件的读写:实际路径 D://xx  --> 虚拟路径: Images/cover对应的实际路径
                    String path = req.getServletContext().getRealPath("/Images/cover");
                    //d:/xxx/xx/ 20211117112512234.png
                    String filePath = path + "/" + fileName;
                    //数据库表中的路径 ：Images/cover/101-1.png：相对项目的根目录的位置
                    String dbPath = "Images/cover/" + fileName;
                    book.setPic(dbPath);

                    //4.3 保存文件
                    item.write(new File(filePath));
                }
            }
        }

        //5.将信息保存到数据库
        boolean count = bookBiz.modify(book);
        if (count) {
            out.println("<script>alert('修改书籍成功');location.href='book?type=query&pageIndex=1';</script>");
        } else {
            out.println("<script>alert('修改书籍失败');location.href='book?type=query&pageIndex=1';</script>");
        }
    }

    /**
     * 查看图书详情
     *
     * @param req
     * @param resp
     * @param out
     */
    private void details(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws ServletException, IOException {
        //1.获取图书的编号
        long bookId = Long.parseLong(req.getParameter("id"));
        //2.根据编号获取图书对象
        Book book = bookBiz.getBookByBookId(bookId);
        //3.将对象保存到req
        req.setAttribute("book", book);
        //4.转发到 jsp页面
        req.getRequestDispatcher("book_details.jsp").forward(req, resp);


    }

    /**
     * 查询
     * book?type=query&pageIndex=1
     * 页数: biz
     * 当前页码:pageIndex = 1
     * 存:request,转发
     *
     * @param req
     * @param resp
     * @param out
     */
    private void query(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws ServletException, IOException {
        //1.获取信息(页数，页码,信息)

        // 一页展示多少个行数
        int pageSize = 10;

        int pageCount = 0;
        // 页数：一共有多少页
        if (bookBiz.getCount() % pageSize == 0) {
            pageCount = bookBiz.getCount() / pageSize;
        } else {
            pageCount = bookBiz.getCount() / pageSize + 1;
        }

        int pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageIndex > pageCount) {
            pageIndex = pageCount;
        }
        List<Book> books = bookBiz.getBookByPage(pageIndex, pageSize);

        //2.将books存到 req 中
        req.setAttribute("pageCount", pageCount);
        req.setAttribute("books", books);

        //3. 转发到jsp页面
        req.getRequestDispatcher("book_list.jsp?pageIndex=" + pageIndex).forward(req, resp);
    }

    /**
     * 添加书籍
     * 1.enctype="multipart/form-data":和以前不同
     * 获取表单元素  req.getParameter("name") : error
     * 2.文件上传 ：图片文件从浏览器端保存到服务器端 (第三方 FileUpload+io)
     * 3.路径
     * 图片:
     * D:\图书管理系统-JavaWeb\01-数据库脚本\cover\文城.png  :实际路径
     * http://localhost:8888/mybook_explored_war/Images/cover/文城.png:虚拟路径(服务器)
     *
     * @param req
     * @param resp
     */
    private void add(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws Exception {
        //1.构建一个磁盘工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //1.1 设置大小
        factory.setSizeThreshold(1024 * 9);
        //1.2 临时仓库
        File file = new File("c:\\temp");
        if (!file.exists()) {
            file.mkdir();//创建文件夹
        }
        factory.setRepository(file);

        //2.文件上传+表单数据
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        //3.将请求解析成一个个FileItem(文件+表单元素)
        List<FileItem> fileItems = fileUpload.parseRequest(req);

        //4.遍历FileItem
        Book book = new Book();
        for (FileItem item : fileItems) {
            if (item.isFormField()) {
                //4.1 元素名称和用户填写的值  name: 文城
                String name = item.getFieldName();
                String value = item.getString("utf-8");//防止乱码
                switch (name) {
                    case "typeId":
                        book.setTypeId(Long.parseLong(value));
                        break;
                    case "name":
                        book.setName(value);
                        break;
                    case "price":
                        book.setPrice(Double.parseDouble(value));
                        break;
                    case "desc":
                        book.setDesc(value);
                        break;
                    case "publish":
                        book.setPublish(value);
                        break;
                    case "author":
                        book.setAuthor(value);
                        break;
                    case "stock":
                        book.setStock(Long.parseLong(value));
                        break;
                    case "address":
                        book.setAddress(value);
                        break;
                }

            } else {
                //4.2 文件: 图片的文件名  文城.png
                String fileName = item.getName();
                //避免文件替换：当前的系统时间.png
                //4.2.1 获取后缀名 .png
                String filterName = fileName.substring(fileName.lastIndexOf("."));
                //4.2.2 修改文件名  20211117112512234.png
                fileName = DateHelper.getImageName() + filterName;
                //保存到哪里
                //虚拟路径: Images/cover/1-1.png
                //文件的读写:实际路径 D://xx  --> 虚拟路径: Images/cover对应的实际路径
                String path = req.getServletContext().getRealPath("/Images/cover");
                //d:/xxx/xx/ 20211117112512234.png
                String filePath = path + "/" + fileName;
                //数据库表中的路径 ：Images/cover/101-1.png：相对项目的根目录的位置
                String dbPath = "Images/cover/" + fileName;
                book.setPic(dbPath);

                //4.3 保存文件
                item.write(new File(filePath));
            }

        }

        //5.将信息保存到数据库
        boolean count = bookBiz.add(book);
        if (count) {
            out.println("<script>alert('添加书籍成功');location.href='book?type=query&pageIndex=1';</script>");
        } else {
            out.println("<script>alert('添加书籍失败');location.href='book_add.jsp';</script>");
        }
    }
}
