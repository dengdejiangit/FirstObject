package com.example.administrator.testproject.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.testproject.greendao.gen.DaoMaster;
import com.example.administrator.testproject.greendao.gen.UserBeanDao;

import org.greenrobot.greendao.database.Database;

public class MyOpenHelper extends DaoMaster.DevOpenHelper {
    MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("updateDb", "old:"+oldVersion+" new:"+newVersion);
//        原理：首先创建临时表（数据格式和原表一模一样）。
//        把当前表的数据插入到临时表中去。
//        删除掉原表，创建新表。
//        把临时表数据插入到新表中去，然后删除临时表。
        MigrationHelper.migrate(db, UserBeanDao.class);
    }
}
