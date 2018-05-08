package com.example.fzeih.telefonbuch;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface DaoAccess{

    @Insert
    void insertOnlySingleFriend(Friend friend);

    @Query ("SELECT * FROM friend WHERE friendName = :friendName")
    Friend fetchOneFriendbyFriendName(String friendName);
}
