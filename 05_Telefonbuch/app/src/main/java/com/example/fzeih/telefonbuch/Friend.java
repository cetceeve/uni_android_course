package com.example.fzeih.telefonbuch;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Friend {

    @PrimaryKey(autoGenerate = true)
    private Integer friendId;

    private String friendName;

    private int phoneNumber;

    public Friend(){}

    @NonNull
    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(@NonNull Integer friendId) {
        this.friendId = friendId;
    }

    @NonNull
    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(@NonNull String friendName) {
        this.friendName = friendName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
