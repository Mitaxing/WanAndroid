package com.mita.wanandroid.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserCollection {

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "collectId")
    private int collectId;

    @Generated(hash = 1300103967)
    public UserCollection(Long id, int collectId) {
        this.id = id;
        this.collectId = collectId;
    }

    @Generated(hash = 309844656)
    public UserCollection() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCollectId() {
        return collectId;
    }

    public void setCollectId(int collectId) {
        this.collectId = collectId;
    }
}
