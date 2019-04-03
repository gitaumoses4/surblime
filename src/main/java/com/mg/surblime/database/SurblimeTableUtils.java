package com.mg.surblime.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by moses on 5/5/18.
 */

public class SurblimeTableUtils<T, ID, O extends OrmLiteSqliteOpenHelper> {

    private O o;
    private Dao<T, ID> dao;

    public SurblimeTableUtils(Context context, Class<O> classOfOpenHelper, Class<T> classOfT) {
        try {
            this.o = classOfOpenHelper.getConstructor(Context.class).newInstance(context);
            this.dao = o.getDao(classOfT);
        } catch (Exception e) {
            Log.e(SurblimeTableUtils.class.getSimpleName(), "Constructor", e);
        }
    }

    public T find(ID id) throws SQLException {
        return dao.queryForId(id);
    }

    public void deleteAll() throws SQLException {
        for (T t : getAll()) {
            dao.delete(t);
        }
    }

    public void create(T t) throws SQLException {
        dao.create(t);
    }

    public void delete(T t) throws SQLException {
        dao.delete(t);
    }

    public boolean exists(T t) throws SQLException {
        return dao.queryForSameId(t) != null;
    }

    public void update(T t) throws SQLException {
        dao.update(t);
    }

    public List<T> getAll() throws SQLException {
        return dao.queryForAll();
    }

    public List<T> getAll(String sortColumnName, boolean ascending) throws SQLException {
        return getQueryBuilder().orderBy(sortColumnName, ascending).query();
    }

    public List<T> getAll(String sortColumnName, boolean ascending, long limit) throws SQLException {
        return getQueryBuilder().orderBy(sortColumnName, ascending).limit(limit).query();
    }

    public List<T> getAll(long limit) throws SQLException {
        return getQueryBuilder().limit(limit).query();
    }

    public QueryBuilder<T, ID> getQueryBuilder() {
        return dao.queryBuilder();
    }

    public Dao<T, ID> getDao() {
        return dao;
    }
}
