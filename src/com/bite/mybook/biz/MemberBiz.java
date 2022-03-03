package com.bite.mybook.biz;

import com.bite.mybook.bean.Member;
import com.bite.mybook.bean.Membertype;
import com.bite.mybook.dao.MemberDao;
import com.bite.mybook.util.DBHelper;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class MemberBiz {
    // memberDao 对象构建
    MemberDao memberDao = new MemberDao();

    // 会员注册
    public boolean register(Member member){
        boolean register = false;
        try {
            long time = System.currentTimeMillis();
            Date date = new Date(time);
            member.setRegdate(date);
            register = memberDao.register(member);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return register;
    }

    // 会员注销
    public boolean remove(long memberId){
        boolean cancellation = false;
        try {
            cancellation = memberDao.remove(memberId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cancellation;
    }

    // 会员修改
    public boolean modify(Member member){
        boolean modify = false;
        try {
            modify = memberDao.modify(member);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modify;
    }

    // 会员查询：1.通过id 2.通过类别 3.通过名称
    public List<Member> getAll() {
        MembertypeBiz membertypeBiz = new MembertypeBiz();
        List<Member> members = null;
        try {
            members = memberDao.getAll();

            for (Member member : members) {
                long typeId = member.getTypeId();
                Membertype membertype = membertypeBiz.getByTypeId(typeId);
                member.setType(membertype);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public Member getById(long memberId){
        Member member = null;
        try {
            member = memberDao.getById(memberId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    public List<Member> getByTypeId(long typeId){
        List<Member> byTypeId = null;
        try {
            byTypeId = memberDao.getByTypeId(typeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return byTypeId;
    }

    public Member getByName(String name){
        Member byName = null;
        try {
            byName = memberDao.getByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return byName;
    }

    // 会员充值
    public boolean memberRecharge(String idNumber,double amount){
        boolean isTrue = false;
        try {
            isTrue = memberDao.memberRecharge(idNumber, amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isTrue;
    }

    // 通过mId修改余额

    /*
     *   amount > 0 : 借书，付押金
     *   amount < 0 : 还书，退押金
     * */
    public boolean memberChangeByMid(long mid,double amount){
        boolean b = false;
        try {
            b = memberDao.memberChangeByMid(mid, amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    // 通过身份证号获取会员信息
    public Member getMemberByIdNumber(String idNumber){
        Member member = null;
        try {
            member = memberDao.getMemberByIdNumber(idNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    // 通过 id 获取 idNumber
    public String getIdNumById(long id){
        Member memberById = null;
        try {
            memberById = memberDao.getMemberById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberById.getIdNumber();
    }

    // 通过 idNumber 获取 id
    public long getIdByIdNum(String idNumber){
        long idByIdNum = 0;
        try {
            idByIdNum = memberDao.getIdByIdNum(idNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idByIdNum;
    }
}
