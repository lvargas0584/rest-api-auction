package com.auction.core.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class User {
    @NotNull
    private String phone;
    private String alias;
    private Integer bid;
    private String facebookToken;
    private String facebookUserId;
    private String email;
    private String name;
    private String birthday;
}
