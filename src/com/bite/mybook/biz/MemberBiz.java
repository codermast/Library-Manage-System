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
}
