package com.auction.core.dto;

import com.auction.core.enums.AuctionStatus;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuctionDto {
    private ObjectId id;
    @NotBlank
    private String promotionalName;
    private String transmissionUrl;
    @NotNull
    private Integer defaultTime;
    //@JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSZ",locale = "America/Lima")
    @JsonbDateFormat(locale = "America/Lima")
    private LocalDateTime endTime;
    private List<Product> products;
    @NotEmpty
    private List<Item> bidderButtons;
    private AuctionStatus status;
    private Boolean autoFinish;
}
