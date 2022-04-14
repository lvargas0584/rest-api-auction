package com.auction.core.entity;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.io.Serializable;

@MongoEntity(collection = "black_list")
public class BlackListEntity extends PanacheMongoEntity implements Serializable {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
