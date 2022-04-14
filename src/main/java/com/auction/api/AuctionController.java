package com.auction.api;

import com.auction.core.dto.AuctionDto;
import com.auction.core.dto.MultipartBodyDto;
import com.auction.core.enums.AuctionStatus;
import com.auction.core.mapper.AuctionMapper;
import com.auction.core.service.AuctionService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Path("/auction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AuctionController {

    @Inject
    AuctionService auctionService;
    @Inject
    AuctionMapper mapper;

    @POST
    public Response create(@Valid AuctionDto dto) {
        AuctionDto auctionDto = auctionService.create(dto);
        return Response.created(URI.create(String.format("/auction/%s", auctionDto.getId())))
                .entity(auctionDto)
                .build();
    }


    @GET
    public List<AuctionDto> get(@QueryParam("status") AuctionStatus status) {
        return auctionService.findByStatus(status);
    }

    @GET
    @Path("/{auctionId}")
    public Response getById(@PathParam("auctionId") String auctionId) {
        return Response.accepted(auctionService.findById(auctionId)).build();
    }


    @GET
    @Path("/end/{auctionId}")
    public Response getAuctionEndTime(@PathParam("auctionId") String auctionId) {
        return Response.accepted(auctionService.getAuctionEndTime(auctionId)).build();
    }


    @GET
    @Path("/download/{auctionId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileWithGet(@PathParam("auctionId") String auctionId) throws IOException {
        Response.ResponseBuilder response = Response.ok((Object) auctionService.generateCSV(auctionId));
        response.header("Content-Disposition", "attachment;filename=" + auctionId);
        return response.build();
    }

    @POST
    @Path("/{auctionId}/product/{productId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveImages(@PathParam("auctionId") String auctionId, @PathParam("productId") String productId, @MultipartForm MultipartBodyDto multipartInput) {

        auctionService.saveImages(auctionId, productId, multipartInput);
        return Response.accepted().build();
    }

    @GET
    @Path("/{auctionId}/product/{productId}/{imageId}")
    @Produces("image/png")
    public Response getImage(@PathParam("auctionId") String auctionId, @PathParam("productId") String productId, @PathParam("imageId") String imageId) {
        return Response.accepted(auctionService.getImage(auctionId, productId, imageId)).build();
    }

}