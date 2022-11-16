package net.catenax.ce.materialpass.listeners;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AppListener {

    @EventListener(ApplicationReadyEvent.class)
    public void onStartUp() {
        System.out.print("\n\n" +
                "************************************************\n" +
                "Catena-X Consumer Backend\n" +
                "Copyright (c) 2022: CGI Deutschland B.V. & Co. KG\n" +
                "Copyright (c) 2022: Contributors to the CatenaX (ng) GitHub Organisation.\n" +
                "\n\n-------------> [ SERVER STARTED ] <-------------\n" +
                "Listening to requests...\n\n"
        );

    }
}
