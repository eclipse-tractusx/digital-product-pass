package net.catenax.ce.materialpass.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tools.logTools;

@Component
public class AppListener {
    @Autowired
    BuildProperties buildProperties;
    @EventListener(ApplicationReadyEvent.class)
    public void onStartUp() {
        String serverStartUpMessage = "\n\n" +
                "************************************************\n" +
                buildProperties.getName()+"\n" +
                "Copyright (c) 2022-2023: CGI Deutschland B.V. & Co. KG\n" +
                "Copyright (c) 2022-2023: Contributors to the CatenaX (ng) GitHub Organisation.\n" +
                "Version: "+ buildProperties.getVersion() + "\n\n" +
                "\n\n-------------> [ SERVER STARTED ] <-------------\n" +
                "Listening to requests...\n\n";

        logTools.printMessage(serverStartUpMessage);
        logTools.printMessage("[ LOGGING STARTED ] <-----------------------------------------");
        logTools.printMessage("Creating log file...");
    }

}
