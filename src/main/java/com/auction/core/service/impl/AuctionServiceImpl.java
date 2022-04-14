package com.auction.core.service.impl;

import com.auction.core.dto.AuctionDto;
import com.auction.core.dto.MultipartBodyDto;
import com.auction.core.entity.AuctionEntity;
import com.auction.core.entity.UserEntity;
import com.auction.core.enums.AuctionStatus;
import com.auction.core.mapper.AuctionMapper;
import com.auction.core.service.AuctionService;
import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class AuctionServiceImpl implements AuctionService {

    private static final Logger log = LoggerFactory.getLogger(AuctionServiceImpl.class);

    @Inject
    AuctionMapper mapper;

    private final String imagesEndPoint = "/auction/{0}/product/{1}/{2}";
    private final String imagePath = "/tmp/auction/{0}/product/{1}/{2}";

    @Override
    public AuctionDto create(AuctionDto dto) {
        AuctionEntity entity = mapper.toEntity(dto);
        entity.persistOrUpdate();
        return mapper.toDto(entity);
    }

    @Override
    public List<AuctionDto> findByStatus(AuctionStatus status) {
        if (status != null)
            return AuctionEntity.findByStatus(status).stream().map(s -> mapper.toDto((AuctionEntity) s)).collect(Collectors.toList());
        else
            return AuctionEntity.findAll().stream().map(s -> mapper.toDto((AuctionEntity) s)).collect(Collectors.toList());
    }

    @Override
    public LocalDateTime getAuctionEndTime(String auctionId) {
        Optional<AuctionEntity> byIdOptional = AuctionEntity.findByIdOptional(new ObjectId(auctionId));
        if (byIdOptional.isPresent()) {
            byIdOptional.get().setEndTime(LocalDateTime.now(ZoneId.of("America/Lima")).plusMinutes(byIdOptional.get().getDefaultTime()));
            byIdOptional.get().update();
            return byIdOptional.get().getEndTime();
        } else return null;
    }

    @Override
    public File generateCSV(String auctionId) throws IOException {

        Optional<UserEntity> byAuctionId = UserEntity.findByAuctionId(auctionId);


        List<String[]> dataLines = new ArrayList<>();

        dataLines.add(new String[]
                {"John", "Doe", "38", "Comment Data\nAnother line of comment data"});
        dataLines.add(new String[]
                {"Jane", "Doe, Jr.", "19", "She said \"I'm being quoted\""});

        generate(auctionId, dataLines);

        return new File("/tmp" + File.separator + auctionId);
    }

    @Override
    public void saveImages(String auctionId, String productId, MultipartBodyDto multipart) {

        try {
            UUID idImage = UUID.randomUUID();
            String imageUuidPath = MessageFormat.format(imagePath, auctionId, productId, idImage);
            String imagesUuiEndPoint = MessageFormat.format(imagesEndPoint, auctionId, productId, idImage);

            log.info(imageUuidPath);
            File tmp = new File(imageUuidPath);
            FileUtils.copyInputStreamToFile(multipart.file, tmp);

            AuctionEntity auctionEntity = AuctionEntity.findById(new ObjectId(auctionId));
            auctionEntity.getProducts().stream().filter(p -> p.getId().equalsIgnoreCase(productId)).findFirst().get().getImages().add(imagesUuiEndPoint);
            auctionEntity.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File getImage(String auctionId, String productId, String imageId) {

        File file = new File(MessageFormat.format(imagePath, auctionId, productId, imageId));
        if (file.exists()) {
            return file;
        } else {
            throw new NotFoundException();
        }

    }

    @Override
    public AuctionDto findById(String auctionId) {
        Optional<AuctionEntity> byIdOptional = AuctionEntity.findByIdOptional(new ObjectId(auctionId));
        AuctionEntity entity = byIdOptional.orElseThrow(() -> new NotFoundException());
        return mapper.toDto(entity);
    }


    /*private AuctionDto buildAuction(AuctionEntity s) {

        AuctionDto auctionDto = mapper.toDto(s);
        auctionDto.getProducts().forEach(p -> {
            List<String> listImages = new ArrayList<>();
            String commonPath = MessageFormat.format(imagesPath, auctionDto.getId(), p.getId());
            System.out.println(commonPath);
            log.info(commonPath);
            Stream.of(new File(commonPath).listFiles())
                    .filter(file -> !file.isDirectory())
                    .map(File::getName)
                    .forEach(image -> listImages.add(commonPath + image));
            p.setImages(listImages);
        });
        return auctionDto;
    }*/


    public void generate(String auctionId, List<String[]> dataLines) throws IOException {

        File csvOutputFile = new File("/tmp" + File.separator + auctionId);
        csvOutputFile.createNewFile();
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }


    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

}
