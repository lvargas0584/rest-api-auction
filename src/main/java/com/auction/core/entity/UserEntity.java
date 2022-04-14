package com.auction.core.entity;

import com.auction.core.dto.User;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@MongoEntity(collection = "user")
public class UserEntity extends PanacheMongoEntity implements Serializable {
    private String auctionId;
    private List<User> users;

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public static Optional<UserEntity> findByAuctionId(String auctionId){
     return find("auctionId = ?1", auctionId).singleResultOptional();
    }
}
