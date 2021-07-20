package com.android.gpspro;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey(autoGenerate = true) // 기본키 + 시퀀스
    private long id;
    private String name;
    private String email;
    private String profileURL;

    @Ignore
    public Contact() { }

    @Ignore
    public Contact(long id, String name, String email, String profileURL) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileURL = profileURL;
    }

    public Contact(String name, String email, String profileURL) {
        this.name = name;
        this.email = email;
        this.profileURL = profileURL;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

/**
 *  @Ignore // 엔티티에 지속하고 싶지 않은 필드나 생성자가 있는 경우 @Ignore를 사용한다.
 */
