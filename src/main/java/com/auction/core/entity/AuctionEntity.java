package com.auction.core.entity;

import com.auction.core.dto.Item;
import com.auction.core.dto.Product;
import com.auction.core.enums.AuctionStatus;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@MongoEntity(collection = "auction")
public class AuctionEntity extends PanacheMongoEntity implements Serializable {
    @NotBlank
    private String promotionalName;
    private String transmissionUrl;
    private Integer defaultTime;
    private Boolean autoFinish;
    private LocalDateTime endTime;
    private List<Product> products;
    private List<Item> bidderButtons;
    private AuctionStatus status;

    public static List<PanacheMongoEntityBase> findByStatus(AuctionStatus status) {
        return find("status = ?1", status).list();
    }

    public String getPromotionalName() {
        return promotionalName;
    }

    public void setPromotionalName(String promotionalName) {
        this.promotionalName = promotionalName;
    }

    public String getTransmissionUrl() {
        return transmissionUrl;
    }

    public void setTransmissionUrl(String transmissionUrl) {
        this.transmissionUrl = transmissionUrl;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Item> getBidderButtons() {
        return bidderButtons;
    }

    public void setBidderButtons(List<Item> bidderButtons) {
        this.bidderButtons = bidderButtons;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public Integer getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(Integer defaultTime) {
        this.defaultTime = defaultTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getAutoFinish() {
        return autoFinish;
    }

    public void setAutoFinish(Boolean autoFinish) {
        this.autoFinish = autoFinish;
    }
}
