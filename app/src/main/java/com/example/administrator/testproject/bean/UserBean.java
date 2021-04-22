package com.example.administrator.testproject.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/*
 * Create  by  User:Administrator  Data:2019/4/30  WS
 *
 */

@Entity
public class UserBean {
    @Id
    int userId;

    @Generated(hash = 1117567381)
    public UserBean(int userId) {
        this.userId = userId;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
}
