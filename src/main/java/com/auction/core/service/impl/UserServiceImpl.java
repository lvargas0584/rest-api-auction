package com.auction.core.service.impl;

import com.auction.core.dto.AdminUserDto;
import com.auction.core.dto.User;
import com.auction.core.dto.UserDto;
import com.auction.core.entity.*;
import com.auction.core.service.UserService;
import com.auction.core.util.Util;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;

@Singleton
public class UserServiceImpl implements UserService {

    @ConfigProperty(name = "whatsapp.send.message.url")
    private String whatsappSendMessageUrl;

    @Override
    public void addUser(UserDto userDto) {
        Optional<UserEntity> byAuctionId = UserEntity.findByAuctionId(userDto.getAuctionId());

        if (byAuctionId.isPresent()) {
            UserEntity byId = UserEntity.findById(byAuctionId.get().id);
            byId.getUsers().add(userDto.getUser());
            byId.update();
        } else {
            UserEntity entity = new UserEntity();
            entity.setAuctionId(userDto.getAuctionId());
            entity.setUsers(Arrays.asList(userDto.getUser()));
            entity.persist();
        }
    }

    @Override
    public Boolean inBlankList(String phone) {
        return BlackListEntity.find("phone = ?1", phone).firstResultOptional().isPresent();
    }

    @Override
    public void addBlankList(String phone) {
        BlackListEntity blackListEntity = new BlackListEntity();
        blackListEntity.setPhone(phone);
        blackListEntity.persist();
    }

    @Override
    public Boolean loginAdmin(AdminUserDto adminUserDto) {
        Optional<UserAdminEntity> panacheMongoEntityBase = UserAdminEntity.find("user = ?1", adminUserDto.getUser()).singleResultOptional();
        UserAdminEntity userAdminEntity = panacheMongoEntityBase.orElseThrow(() -> new NotFoundException());

        if (Util.compareHash(adminUserDto.getPass(), userAdminEntity.getPassword()))
            return Util.compareHash(adminUserDto.getPass(), userAdminEntity.getPassword());
        else
            throw new NotAuthorizedException("for user " + adminUserDto.getUser());
    }

    @Override
    public AdminUserDto createAdmin(AdminUserDto adminUserDto) {
        UserAdminEntity userAdminEntity = new UserAdminEntity();
        userAdminEntity.setUser(adminUserDto.getUser());
        userAdminEntity.setPassword(Util.generateHashMD5(adminUserDto.getPass()));
        userAdminEntity.setRoles(Arrays.asList("ADMIN"));
        userAdminEntity.persist();
        return adminUserDto;
    }

    @Override
    public void addBid(UserDto userDto) {
        UserEntity entity = UserEntity.findByAuctionId(userDto.getAuctionId()).orElseThrow(() -> new NotFoundException("auction"));
        entity.getUsers().stream()
                .filter(s -> s.getPhone().equalsIgnoreCase(userDto.getUser().getPhone())).findFirst()
                .orElseThrow(() -> new NotFoundException("phone"))
                .setBid(Integer.valueOf(userDto.getUser().getBid()));
        entity.update();
    }

    @Override
    public String getMessageAuctionWinner(String idAuction, String idProduct, String phone) throws UnsupportedEncodingException {

        String message = AuctionConfigEntity.find("parentName = ?1", "WHATSAPP_MESSAGE_TEMPLATE")
                .singleResultOptional()
                .map(s -> ((AuctionConfigEntity) s).getValue1())
                .orElseThrow(() -> new NotFoundException("WHATSAPP_MESSAGE_TEMPLATE"));


        String productName = AuctionEntity.findByIdOptional(new ObjectId(idAuction))
                .map(s -> ((AuctionEntity) s).getProducts())
                .orElseThrow(() -> new NotFoundException("Auction"))
                .stream().filter(p -> p.getId().equalsIgnoreCase(idProduct))
                .map(product -> product.getName())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product"));

        User user = UserEntity.findByAuctionId(idAuction)
                .map(s -> s.getUsers())
                .orElseThrow(() -> new NotFoundException())
                .stream().filter(u -> u.getPhone().equalsIgnoreCase(phone))
                .findFirst()
                .orElseThrow(() -> new NotFoundException());


        message = MessageFormat.format(message, user.getAlias(), productName, "s/."+user.getBid());
        message = URLEncoder.encode(message, StandardCharsets.UTF_8.name());
        return MessageFormat.format(whatsappSendMessageUrl, "51"+phone, message);
    }
}
