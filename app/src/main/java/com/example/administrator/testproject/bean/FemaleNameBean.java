package com.example.administrator.testproject.bean;


import java.util.List;

/**
 * Create  by  User:WS  Data:2019/7/15
 */

public class FemaleNameBean {
    /**
     * code : 200
     * msg : 成功!
     * data : [{"femalename":"﻿花花世界丶小风流"},{"femalename":"回忆似风不可追"},{"femalename":"向阳花.永不低头"},{"femalename":"纠结需要的心痛゛"},{"femalename":"九片棱角的灬回忆"},{"femalename":"斑马线上的妖精"},{"femalename":"各种高潮各种飘"},{"femalename":"舊城舊事就回憶ず"},{"femalename":"有打有闹有情调づ"},{"femalename":"如果你還不明白。"},{"femalename":"泼墨挥笔画中仙"},{"femalename":"迷途在北极的鱼"},{"femalename":"猴子死了谁偷桃丶"},{"femalename":"今夕何处觅良人"},{"femalename":"只想陪在你身边"},{"femalename":"十里长亭花落尽"},{"femalename":"丶秋雨萌萌入凉意"},{"femalename":"海岛陌安逝暮梦"},{"femalename":"上帝是一种信仰"},{"femalename":"我的男人请尊重"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * femalename : ﻿花花世界丶小风流
         */

        private String femalename;

        public String getFemalename() {
            return femalename;
        }

        public void setFemalename(String femalename) {
            this.femalename = femalename;
        }
    }
}
