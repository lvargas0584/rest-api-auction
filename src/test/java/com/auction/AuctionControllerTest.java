package com.auction;

import com.auction.core.entity.AuctionConfigEntity;
import com.auction.core.entity.AuctionEntity;
import com.auction.core.service.UserService;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@QuarkusTest
public class AuctionControllerTest {

    @Inject
    UserService userService;


    @Test
    public void testHelloEndpoint() throws UnsupportedEncodingException {

        //System.out.println(userService.getMessageAuctionWinner(idAuction, "","51987816150"));

        /*AuctionEntity entity = AuctionEntity.findById(new ObjectId("5ef1077aecfaa272038a9406"));
        entity.getProducts().forEach(s -> {
            s.setImages(new ArrayList<>());
        });
        entity.update();*/
        AuctionConfigEntity byId = AuctionConfigEntity.findById(new ObjectId("5ef641445da1d062f5c3dddd"));
        byId.setValue1("Felicitaciones {0} ganaste la subasta del producto {1} por el monto {2}");
        byId.update();
        /*configEntity = new AuctionConfigEntity();
        configEntity.setName("WHATSAPP_MESSAGE_TEMPLATE");
        configEntity.setValue1("message");
        configEntity.setStatus(1);
        configEntity.persist();

        configEntity = new AuctionConfigEntity();
        configEntity.setValue1("Felicitaciones {0} ganaste la subasta del producto {1} por el monto {2}. Las cuentas para el deposito son las siguientes {3}");
        configEntity.setParentName("WHATSAPP_MESSAGE_TEMPLATE");
        configEntity.setStatus(1);
        configEntity.persist();*/

    }

}