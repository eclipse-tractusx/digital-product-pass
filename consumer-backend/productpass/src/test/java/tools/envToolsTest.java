package tools;

import net.catenax.ce.productpass.exceptions.ControllerException;
import net.catenax.ce.productpass.http.controllers.api.ApiController;
import net.catenax.ce.productpass.models.negotiation.Offer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class envToolsTest {
    public envTools env;


    @BeforeEach
    void setUp() {
        this.env = new envTools();
        Assertions.assertNotNull(this.env);
    }

    @Test
    void getEnvironment() {
        String environment = this.env.getEnvironment();
        Assertions.assertNotNull(environment);
        logTools.printTest("["+this.getClass().getName()+".getEnvironment] " + "Current Environment [" + environment + "]");

    }

}
