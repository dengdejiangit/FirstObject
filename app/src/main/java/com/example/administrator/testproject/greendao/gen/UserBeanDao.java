package com.example.administrator.testproject.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.administrator.testproject.bean.UserBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_BEAN".
*/
public class UserBeanDao extends AbstractDao<UserBean, Integer> {

    public static final String TABLENAME = "USER_BEAN";

    /**
     * Properties of entity UserBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, int.class, "userId", true, "USER_ID");
    }


    public UserBeanDao(DaoConfig config) {
        super(config);
    }
    
    public UserBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_BEAN\" (" + //
                "\"USER_ID\" INTEGER PRIMARY KEY NOT NULL );"); // 0: userId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getUserId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getUserId());
    }

    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.getInt(offset + 0);
    }    

    @Override
    public UserBean readEntity(Cursor cursor, int offset) {
        UserBean entity = new UserBean( //
            cursor.getInt(offset + 0) // userId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserBean entity, int offset) {
        entity.setUserId(cursor.getInt(offset + 0));
     }
    
    @Override
    protected final Integer updateKeyAfterInsert(UserBean entity, long rowId) {
        return entity.getUserId();
    }
    
    @Override
    public Integer getKey(UserBean entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserBean entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
