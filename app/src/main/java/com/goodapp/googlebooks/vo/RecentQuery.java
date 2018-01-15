package com.goodapp.googlebooks.vo;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Created by gsipic on 15/01/2018.
 */

@Entity(primaryKeys = "query")
public class RecentQuery {
    @NonNull
    public String query;

    public RecentQuery(@NonNull String query) {
        this.query = query;
    }
}
