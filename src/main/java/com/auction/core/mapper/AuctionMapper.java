package com.auction.core.mapper;

import com.auction.core.dto.AuctionDto;
import com.auction.core.entity.AuctionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface AuctionMapper {
    AuctionDto toDto(AuctionEntity entity);

    AuctionEntity toEntity(AuctionDto dto);
}
