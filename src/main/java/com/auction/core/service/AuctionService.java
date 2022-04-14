package com.auction.core.service;

import com.auction.core.dto.AuctionDto;
import com.auction.core.dto.MultipartBodyDto;
import com.auction.core.enums.AuctionStatus;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface AuctionService {
    AuctionDto create(AuctionDto dto);

    List<AuctionDto> findByStatus(AuctionStatus status);

    LocalDateTime getAuctionEndTime(String auctionId);

    File generateCSV(String auctionId) throws IOException;

    void saveImages(String auctionId, String productId, MultipartBodyDto multipart);

    File getImage(String auctionId, String productId, String imageId);

    AuctionDto findById(String auctionId);
}
