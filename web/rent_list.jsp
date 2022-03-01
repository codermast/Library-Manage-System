<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html >
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="keywords"  content = "图书 java jsp"/>
    <meta http-equiv="author" content="phenix"/>
    <link rel="stylesheet" type="text/css" href="./Style/skin.css" />
    <script src="Js/jquery-3.3.1.min.js"></script>
    <script>

        $(function(){
            $("#btnQuery").click(function(){
                //0.清理
                $("#tbRecord").find("tbody").html("");
                //1.获取id
                var typeId = $(":radio:checked").prop("value");
                //2.获取关键字
                var keyword = $("#keyword").val();
                //3.组成url
                var url = "record.let?type=doajax&typeId="+typeId+"&keyword="+keyword;
                //4.发送并获取数据
                $.get(url,function(data){
                     console.log(data);
                //5.数据加载到页面
                    if(data==="[]" ){
                        alert("没有信息展示");
                        return;
                    }

                    //1. data=>json对象
                    var records = JSON.parse(data);
                    for(var i=0;i<records.length;i++){
                       var record =  records[i];
                       //tr
                        var tr = $(" <tr align=\"center\" class=\"d\">");
                        var tdMName = $(" <td>"+record.memberName+"</td>");
                        var tdBName = $("<td>"+record.bookName+"</td>");
                        var tdRentDate = $("<td>"+record.rentDate+"</td>");
                        var tdBackDate = $("<td>"+ (record.backDate===undefined?"":record.backDate)+"</td>");
                        var tdDeposit = $("<td>"+record.deposit+"</td>");
                        //5-td
                        tr.append(tdMName);
                        tr.append(tdBName);
                        tr.append(tdRentDate);
                        tr.append(tdBackDate);
                        tr.append(tdDeposit);

                        //加入到表
                        $("#tbRecord").find("tbody").append(tr);
                    }

                });


            });

        });

    </script>

</head>
    <body>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <!-- 头部开始 -->
            <tr>
                <td width="17" valign="top" background="./Images/mail_left_bg.gif">
                    <img src="./Images/left_top_right.gif" width="17" height="29" />
                </td>
                <td valign="top" background="./Images/content_bg.gif">
                    
                </td>
                <td width="16" valign="top" background="./Images/mail_right_bg.gif"><img src="./Images/nav_right_bg.gif" width="16" height="29" /></td>
            </tr>
            <!-- 中间部分开始 -->
            <tr>
                <!--第一行左边框-->
                <td valign="middle" background="./Images/mail_left_bg.gif">&nbsp;</td>
                <!--第一行中间内容-->
                <td valign="top" bgcolor="#F7F8F9">
                    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                        <!-- 空白行-->
                        <tr><td colspan="2" valign="top">&nbsp;</td><td>&nbsp;</td><td valign="top">&nbsp;</td></tr>
                        <tr>
                            <td colspan="4">
                                <table>
                                    <tr>
                                        <td width="100" align="center"><img src="./Images/mime.gif" /></td>
                                        <td valign="bottom"><h3 style="letter-spacing:1px;">常用功能 > 借阅历史记录 </h3></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!-- 一条线 -->
                        <tr>
                            <td height="40" colspan="4">
                                <table width="100%" height="1" border="0" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">
                                    <tr><td></td></tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td width="2%">&nbsp;</td>
                            <td width="96%">
                                <fieldset>
                                    <legend>查询条件</legend>
                                    <table width="100%" class="cont"  >
                                        <tr>
                                            <td colspan="8" align="center">
                                                <input type="radio" name="query" value="0" checked/>全部 &nbsp;&nbsp;
                                                <input type="radio" name="query" value="1"/>已归还 &nbsp;&nbsp;
                                                <input type="radio" name="query" value="2"/>未归还 &nbsp;&nbsp;
                                                <input type="radio" name="query" value="3"/>最近一周需归还 &nbsp;&nbsp;
                                                请输入关键字:&nbsp;&nbsp;<input class="text" type="text" id="keyword" name="keyword"/> 
                                                <input type="button" id="btnQuery" value="搜索" style="width: 80px;"/></td>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                            <td width="2%">&nbsp;</td>
                        </tr>
                      
                        <!--空行-->
                        <tr>
                            <td height="40" colspan="3">
                            </td>
                        </tr>
                        <!-- 产品列表开始 -->
                        <tr>
                            <td width="2%">&nbsp;</td>
                            <td width="96%">
                                <table width="100%">
                                    <tr>
                                        <td colspan="2">
                                            <form action="" method="">
                                                <table width="100%"  class="cont tr_color" id="tbRecord">
                                                    <thead>
                                                        <tr>
                                                            <th>会员名称</th>
                                                            <th>书籍名称</th>
                                                            <th>借阅时间</th>
                                                            <th>归还时间</th>
                                                            <th>押金(元)</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                    </tbody>
                                                </table>
                                            </form>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td width="2%">&nbsp;</td>
                        </tr>
                        <!-- 产品列表结束 -->
                        <tr>
                            <td height="40" colspan="4">
                                <table width="100%" height="1" border="0" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">
                                    <tr><td></td></tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td width="2%">&nbsp;</td>
                            <td width="51%" class="left_txt">
                                <img src="./Images/icon_mail.gif" width="16" height="11"> 客户服务邮箱：2087924818@qq.com<br />
                            </td>
                            <td>&nbsp;</td><td>&nbsp;</td>
                        </tr>
                    </table>
                </td>
                <td background="./Images/mail_right_bg.gif">&nbsp;</td>
            </tr>
            <!-- 底部部分 -->
            <tr>
                <td valign="bottom" background="./Images/mail_left_bg.gif">
                    <img src="./Images/buttom_left.gif" width="17" height="17" />
                </td>
                <td background="./Images/buttom_bgs.gif">
                    <img src="./Images/buttom_bgs.gif" width="17" height="17">
                </td>
                <td valign="bottom" background="./Images/mail_right_bg.gif">
                    <img src="./Images/buttom_right.gif" width="16" height="17" />
                </td>           
            </tr>
        </table>
    </body>
</html>