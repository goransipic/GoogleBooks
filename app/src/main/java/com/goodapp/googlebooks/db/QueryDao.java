package com.goodapp.googlebooks.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.goodapp.googlebooks.vo.RecentQuery;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by gsipic on 15/01/2018.
 */

@Dao
public interface QueryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecentQuery query);


    @Query("SELECT * FROM recentquery ORDER BY `query` DESC LIMIT 1")
    Single<List<RecentQuery>> getRecentQuery();

}
