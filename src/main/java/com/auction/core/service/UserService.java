package com.auction.core.service;

import com.auction.core.dto.AdminUserDto;
import com.auction.core.dto.UserDto;

import java.io.UnsupportedEncodingException;

public interface UserService {

    void addUser(UserDto userDto);

    Boolean inBlankList(String phone);

    void addBlankList(String phone);

    Boolean loginAdmin(AdminUserDto adminUserDto);

    AdminUserDto createAdmin(AdminUserDto adminUserDto);

    void addBid(UserDto userDto);

    String getMessageAuctionWinner(String idAuction, String idProduct, String phone) throws UnsupportedEncodingException;
}
