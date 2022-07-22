/*
 *  Copyright (c) 2020, 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *       Fraunhofer Institute for Software and Systems Engineering - added dependencies
 *
 */

plugins {
    `java-library`
    id("application")
}

val rsApi: String by project

dependencies {
    api(project(":spi"))
    implementation(project(":common:util"))
    implementation("javax.servlet:javax.servlet-api:4.0.0")
    implementation("org.keycloak:keycloak-core:17.0.0")
	implementation("org.keycloak:keycloak-adapter-core:17.0.0")
	implementation("org.keycloak:keycloak-adapter-spi:17.0.0")
	implementation("org.keycloak:keycloak-admin-client:17.0.0")
	implementation("org.keycloak:keycloak-common:17.0.0")
	compileOnly("org.keycloak:keycloak-server-spi:17.0.0")
	implementation("org.keycloak:keycloak-authz-client:17.0.0")
	implementation ("org.keycloak.bom:keycloak-adapter-bom:16.1.1")
	implementation("org.apache.poi:poi-ooxml:5.2.0")
//	implementation("org.apache.logging.log4j:log4j-to-slf4j:2.17.1")
    implementation("javax.ws.rs:javax.ws.rs-api:2.1")
    
    api("jakarta.ws.rs:jakarta.ws.rs-api:${rsApi}")
}
