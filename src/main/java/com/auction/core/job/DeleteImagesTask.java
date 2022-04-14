package com.auction.core.job;

import com.auction.core.entity.AuctionEntity;
import com.auction.core.enums.AuctionStatus;
import com.auction.core.mapper.AuctionMapper;
import io.quarkus.scheduler.Scheduled;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

@ApplicationScoped
public class DeleteImagesTask {

    private static final Logger log = LoggerFactory.getLogger(DeleteImagesTask.class);

    private final String imagePath = "/tmp/auction/{0}/";

    @Inject
    AuctionMapper mapper;

    //@Scheduled(cron = "0 0 1 1 * ?")
    @Scheduled(every = "10s")
    void deleteImagesAuctionArchived() {
        AuctionEntity.findByStatus(AuctionStatus.FINISHED).forEach(a -> {
            try {
                String format = MessageFormat.format(imagePath, ((AuctionEntity) a).id.toString());
                FileUtils.deleteDirectory(new File(format));
                ((AuctionEntity) a).setStatus(AuctionStatus.ARCHIVED);
                ((AuctionEntity) a).update();

                log.info("JOB EXEC DELETE " + format);
            } catch (IOException e) {
                log.error("error delete images", e);
                e.printStackTrace();
            }
        });
    }
}
