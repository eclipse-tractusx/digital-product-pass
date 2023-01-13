package net.catenax.ce.productpass.http.controllers.api;

import net.catenax.ce.productpass.exceptions.ControllerException;
import net.catenax.ce.productpass.models.negotiation.Offer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.*;

import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest
class ApiControllerTest {
    @Autowired
    ApiController controller;


    String assetId;
    String idType;
    Integer index;

    @BeforeEach
    void setUp() {
        assetId = "X123456789012X12345678901234566";
        idType = "Battery_ID_DMC_Code";
        index = 0;
    }
    @Test
    void getContractOfferByAssetId() {
        try {
            String date = dateTimeTools.getDateTimeFormatted(null);
            Offer offer = controller.getContractOfferByAssetId(this.assetId);
            Assertions.assertNotNull(offer);
            logTools.printTest("["+this.getClass().getName()+".getContractOfferByAssetId] " + "Offer found: " + offer.getId() + " for assetId: ["+assetId+"]" );
        } catch (ControllerException e) {
            fail("It was not possible to retrieve contract offer");
        }
    }
}
