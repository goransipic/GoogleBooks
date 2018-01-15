package com.goodapp.googlebooks.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;

import com.goodapp.googlebooks.vo.RecentQuery;

/**
 * Created by gsipic on 15/01/2018.
 */
@Database(entities = {RecentQuery.class}, version = 1)
public abstract class BooksDb extends RoomDatabase {

    abstract public QueryDao queryDao();

}
