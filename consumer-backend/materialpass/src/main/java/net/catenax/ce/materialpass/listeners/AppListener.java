package net.catenax.ce.materialpass.listeners;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import tools.logTools;

@Component
public class AppListener {

    @EventListener(ApplicationReadyEvent.class)
    public void onStartUp() {
        String serverStartUpMessage = "\n\n" +
                "************************************************\n" +
                "Catena-X Consumer Backend\n" +
                "Copyright (c) 2022: CGI Deutschland B.V. & Co. KG\n" +
                "Copyright (c) 2022: Contributors to the CatenaX (ng) GitHub Organisation.\n" +
                "Version: "+ System.getProperty("version")+ "\n\n" +
                "\n\n-------------> [ SERVER STARTED ] <-------------\n" +
                "Listening to requests...\n\n";

        System.out.print(serverStartUpMessage);
        logTools.printMessage("[ LOGGING STARTED ] <-----------------------------------------");
        logTools.printMessage("Creating log file...");



    }
    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor(); // Or use another one of your liking
    }

}
