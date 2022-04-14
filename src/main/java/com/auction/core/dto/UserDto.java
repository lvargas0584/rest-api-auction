package com.auction.core.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    @NotNull
    private String auctionId;
    private User user;
}
