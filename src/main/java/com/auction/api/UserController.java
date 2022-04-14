package com.auction.api;

import com.auction.core.dto.AdminUserDto;
import com.auction.core.dto.UserDto;
import com.auction.core.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;

@Path("/auction/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserController {

    @Inject
    UserService userService;

    @POST
    public Response create(UserDto userDto) {
        userService.addUser(userDto);
        return Response.accepted().build();
    }

    @POST
    @Path("/bid")
    public Response saveBid(@Valid UserDto userDto) {
        userService.addBid(userDto);
        return Response.accepted().build();
    }

    @GET
    @Path("/blacklist/{phone}")
    public Response inBlackList(@PathParam("phone") String phone) {
        return Response.accepted(userService.inBlankList(phone)).build();
    }

    @POST
    @Path("/blacklist/{phone}")
    public Response addBlackList(@PathParam("phone") String phone) {
        userService.addBlankList(phone);
        return Response.accepted().build();
    }

    @POST
    @Path("/admin/login")
    public Response loginAdmin(@Valid AdminUserDto adminUserDto) {
        return Response.accepted(userService.loginAdmin(adminUserDto)).build();
    }

    @POST
    @Path("/admin")
    public Response createAdmin(@Valid AdminUserDto adminUserDto) {
        return Response.accepted(userService.createAdmin(adminUserDto)).build();
    }

    @GET
    @Path("/message/{idAuction}/{idProduct}/{phone}")
    public Response getMessage(@PathParam("idAuction") String idAuction, @PathParam("idProduct") String idProduct, @PathParam("phone") String phone) throws UnsupportedEncodingException {
        return Response.ok(userService.getMessageAuctionWinner(idAuction, idProduct, phone)).build();
    }

}

