package org.eclipse.tractusx.digitalproductpass.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class consists exclusively to define the attributes and methods needed for the Simple API configuration.
 **/
@Configuration
@ConfigurationProperties(prefix="configuration.simple-api")
public class SingleApiConfig {

    /** ATTRIBUTES **/
    private Integer maxRetries;

    /** GETTERS AND SETTERS **/
    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }
}
