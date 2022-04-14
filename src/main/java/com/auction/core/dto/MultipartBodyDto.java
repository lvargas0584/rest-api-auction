package com.auction.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public class MultipartBodyDto {

    @JsonIgnore
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream file;
}
