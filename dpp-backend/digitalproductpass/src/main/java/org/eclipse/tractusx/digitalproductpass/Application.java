/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
 *
 * Copyright (c) 2022, 2024 BMW AG
 * Copyright (c) 2022, 2024 Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
 *
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.digitalproductpass;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import utils.FileUtil;
import utils.JsonUtil;

import java.io.File;


/**
 * This class is the main class of the Application, where the main method is to start it with Spring boot.
 **/
@SpringBootApplication(scanBasePackages={
        "utils", "org.eclipse.tractusx.digitalproductpass"})
@EnableAsync
@SecurityScheme(
        description = "Access token generated in the Product Passport Consumer Frontend, against the Catena-X IAM Service",
        name = "BearerAuthentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class Application {
    @Autowired
    BuildProperties buildProperties;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    Environment env;
	public static void main(String[] args) {

        SpringApplication application =
                new SpringApplication(Application.class);
        application.run(args);

	}

    @Bean
    public OpenAPI openApiConfig() throws Exception {
        return new OpenAPI().info(getApiInfo()).components(this.loadSchemas());
    }

    public Components loadSchemas() throws Exception {
        String content = fileUtil.getResourceFileAsString(fileUtil.getResourceContent(this.getClass(), "schemas/dpp-cdc.jsonld"));
        if(content == null){
            throw new Exception("It was not possible to load the dpp schema file");
        }
        return new Components().addSchemas(
                "CertifiedDataCredential", new JsonSchema().example(jsonUtil.parseJson(content))
        );
    }

    public Info getApiInfo(){
        Info info = new Info();
        info.title(buildProperties.getName());
        info.description("Open API documentation for the "+buildProperties.getName());
        info.version(buildProperties.getVersion());
        info.license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"));
        return info;
    }
}
