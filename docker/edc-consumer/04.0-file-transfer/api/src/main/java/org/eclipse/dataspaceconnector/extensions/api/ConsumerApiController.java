/*
 *  Copyright (c) 2021 Fraunhofer Institute for Software and Systems Engineering
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Fraunhofer Institute for Software and Systems Engineering - initial API and implementation
 *
 */

package org.eclipse.dataspaceconnector.extensions.api;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.dataspaceconnector.spi.contract.negotiation.ConsumerContractNegotiationManager;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.negotiation.ContractOfferRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.offer.ContractOffer;
import org.eclipse.dataspaceconnector.spi.types.domain.material.battery.BatteryPassport;
import org.eclipse.dataspaceconnector.spi.types.domain.material.battery.ProviderMetadata;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/")
public class ConsumerApiController {

    private final Monitor monitor;
    private final TransferProcessManager processManager;
    private static final String REGISTRY_PATH = "/samples/04.0-file-transfer/registry";
    private static final String DATA_PATH = "/samples/04.0-file-transfer/data";
    private static final String METADATA_PATH = REGISTRY_PATH + "/metadata";

    public ConsumerApiController(Monitor monitor, TransferProcessManager processManager) {
        this.monitor = monitor;
        this.processManager = processManager;
    }

    @POST
    @Path("file/{filename}")
    public Response initiateTransfer(@PathParam("filename") String filename, @QueryParam("connectorAddress") String connectorAddress,
                                     @QueryParam("destination") String destinationPath, @QueryParam("contractId") String contractId) {

        monitor.info(format("Received request for file %s against provider %s", filename, connectorAddress));

        Objects.requireNonNull(filename, "filename");
        Objects.requireNonNull(connectorAddress, "connectorAddress");

        var dataRequest = DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(connectorAddress) //the address of the provider connector
                .protocol("ids-multipart")
                .connectorId("consumer")
                .assetId(filename)
                .dataDestination(DataAddress.Builder.newInstance()
                        .keyName("keyName")
                        .type("File") //the provider uses this to select the correct DataFlowController
                        .property("path", destinationPath) //where we want the file to be stored
                        .build())
                .managedResources(false) //we do not need any provisioning
                .contractId(contractId)
                .build();

        var result = processManager.initiateConsumerRequest(dataRequest);

        return result.failed() ? Response.status(400).build() : Response.ok(result.getContent()).build();
    }

    @GET
    @Path("provider/metadata/{selectedProvider}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProviderAccessInformation(@PathParam("selectedProvider") String selectedProvider,
    		                                     @QueryParam("role") String role) {

        String result = "";
        if (selectedProvider == null && role == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        String currentDir = System.getProperty("user.dir") + METADATA_PATH;
        String filename = currentDir + "/" + selectedProvider + "_" + role + ".json";
        ProviderMetadata metadata = new ProviderMetadata();
        result = readFile(filename);
        //getJsonData(metadata, filename);
        if (result == "")
        	return Response.status(Response.Status.NO_CONTENT).build();
        else
            return Response.ok(result).header("Access-Control-Allow-Origin", "*").allow("OPTIONS").build();
    }

    @GET
    @Path("/")
    public Response helloWorld() {

        return Response.ok("Hello World..!!").build();

    }

    @GET
    @Path("/login")
    public Response isUserAuthenticated() {

        return Response.ok("User has been authenticated successfully..!!").build();

    }

    @GET
    @Path("passport/display/{filename}")
    public Response printProductPassport(@PathParam("filename") String filename) {

    	 String result = "";
    	if (filename == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        String filePath = System.getProperty("user.dir") + DATA_PATH + "/" + filename;
        result = readFile(filePath);
        return Response.ok(result).build();

    }

    private String getJsonData(Object object, String filename) {

    	String result = "";
    	ObjectMapper objectMapper = new ObjectMapper();
    	File file = new File(filename);
    	try {
    		if (object instanceof ProviderMetadata) {
    			ProviderMetadata metadata = objectMapper.readValue(file, ProviderMetadata.class);
    			ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
                result = objectWriter.writeValueAsString(metadata);
//                System.out.println("Id: " + metadata.getId()  + "_Provider: " + metadata.getProvider() + "_Role: " +metadata.getRole());
//                List<Battery> batteries = metadata.getBatteries();
//        		for (Battery battery : batteries) {
//        			System.out.println(battery.getId() +"_"+ battery.getName() + "_" + battery.getType() );
//        		}
    			}
    		else if (object instanceof BatteryPassport) {
    			BatteryPassport batteryPass = objectMapper.readValue(file, BatteryPassport.class);
    			ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
                result = objectWriter.writeValueAsString(batteryPass);
    		}

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went wrong..!!");
        }

    	return result;
    }


    private String readFile(String filename) {

    	String result = "";
    	try
    	 {
    		var list = Files.readAllLines(Paths.get(filename));
    		result = String.join("", list);
    		System.out.println(result);
    	 }
    	 catch(Exception ex)
    	 {
    	     ex.printStackTrace();
    	 }
    	return result;
    }
}
