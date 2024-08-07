/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.digitalproductpass.verification.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.eclipse.tractusx.digitalproductpass.core.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.core.exceptions.ManagerException;
import org.eclipse.tractusx.digitalproductpass.core.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.core.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.digitalproductpass.core.models.dtregistry.SubModel;
import org.eclipse.tractusx.digitalproductpass.core.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.core.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.verification.config.CDCConfig;
import org.eclipse.tractusx.digitalproductpass.verification.config.VerificationConfig;
import org.eclipse.tractusx.digitalproductpass.verification.models.Proof;
import org.eclipse.tractusx.digitalproductpass.verification.models.VerificationInfo;
import org.eclipse.tractusx.digitalproductpass.verification.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * This class manages the verification process and the status from the verification of digital twins and data retrieved
 *
 */

@Component
public class VerificationManager {

    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private ProcessManager processManager;
    @Autowired
    private WalletService walletService;
    @Autowired
    private ProcessConfig processConfig;
    @Autowired
    private VerificationConfig verificationConfig;


    /**
     * Sets the verification process status.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   subModel
     *          the {@code SubModel} digital twin with the information about the verification
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String setVerificationStatus(String processId, SubModel subModel, String bpn) {
        try {

            Boolean vc = this.isVerifiableCredential(subModel);
            if(vc == null){
                throw new ManagerException(this.getClass().getName(), "No configuration available for the verification process!");
            }
            if(vc){
                LogUtil.printMessage("[DPP VERIFICATION ADD-ON] [PROCESS "+processId+"] Verifiable Credential Aspect Found in subModel ["+ subModel.getIdentification()+"]!");
            }
            String path = processManager.getProcessFilePath(processId, processManager.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }
            History history = new History(
                    subModel.getIdentification(),
                    vc?"VERIFIABLE":"UNVERIFIABLE",
                    DateTimeUtil.getTimestamp()
            );
            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            if (statusFile == null){
                throw new ManagerException(this.getClass().getName(), "Status file does not exists for process id ["+processId+"]!");
            }
            statusFile.setStatus(history.getStatus());
            statusFile.setModified(DateTimeUtil.getTimestamp());
            statusFile.setHistory( vc?"verifiable-aspect-found":"plain-aspect-found", history);
            statusFile.setVerification(VerificationInfo.builder().vc(vc).owner(bpn).build());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
    /**
     * Sets the verification process status.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String setVerificationStarted(String processId) {
        try {

            String path = processManager.getProcessFilePath(processId, processManager.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            LogUtil.printMessage("[DPP VERIFICATION ADD-ON] [PROCESS "+ processId+"] Verification Started for Data Aspect Received from EDC!");
            History history = new History(
                    processId,
                    "VERIFYING",
                    DateTimeUtil.getTimestamp()
            );
            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            if (statusFile == null){
                throw new ManagerException(this.getClass().getName(), "Status file does not exists for process id ["+processId+"]!");
            }
            statusFile.setStatus(history.getStatus());
            statusFile.setModified(DateTimeUtil.getTimestamp());
            statusFile.setHistory("verification-started", history);
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
    /**
     * Sets the verification process status.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   verificationInfo
     *          the {@code VerificationInfo} verification information form the aspect
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String setVerificationInfo(String processId, VerificationInfo verificationInfo) {
        try {

            String path = processManager.getProcessFilePath(processId, processManager.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            if(verificationInfo.verified){
                LogUtil.printMessage("[DPP VERIFICATION ADD-ON] [PROCESS "+ processId+"] [VERIFIED] The Data Aspect was verified Successfully!");
            }else{
                LogUtil.printError("[DPP VERIFICATION ADD-ON] [PROCESS "+ processId+"] [VERIFICATION FAILED] The Data Aspect was not able to be verified!");
            }
            History history = new History(
                    processId,
                    verificationInfo.verified?"VERIFIED":"UNVERIFIED",
                    DateTimeUtil.getTimestamp()
            );
            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            if (statusFile == null){
                throw new ManagerException(this.getClass().getName(), "Status file does not exists for process id ["+processId+"]!");
            }
            statusFile.setStatus(history.getStatus());
            statusFile.setModified(DateTimeUtil.getTimestamp());
            statusFile.setHistory(verificationInfo.verified?"verification-completed":"verification-failed", history);
            statusFile.setVerification(verificationInfo);
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
    public Boolean isVerifiableCredential(SubModel subModel){
        CDCConfig cdcConfig = verificationConfig.getCertifiedDataCredential();
        if(cdcConfig == null){
            throw new ManagerException(this.getClass().getName()+".isVerifiableCredential", "No Certified Data Credential configuration available!");
        }
        List<CDCConfig.SemanticKey> keys = cdcConfig.getSemanticIdKeys();

        if(keys == null){
            return null;
        }

        SubModel.SemanticId semanticId = subModel.getSemanticId();

        List<SubModel.SemanticId.Key> semanticIds = semanticId.getKeys();

        return keys.stream().parallel().allMatch(semanticKey -> this.isKeyInKeys(semanticKey, semanticIds));
    }

    public Boolean isKeyInKeys(CDCConfig.SemanticKey key, List<SubModel.SemanticId.Key> keys){
        return keys.stream().parallel().anyMatch(semanticKey -> key.getKey().equals(semanticKey.getType()) && key.getValue().equals(semanticKey.getValue()));
    }

    public VerificationInfo setupVerification(VerificationInfo verificationInfo, JsonNode passport){

        if(!verificationInfo.vc){
            return verificationInfo;
        }

        if(passport.has("proof")){
            verificationInfo.setProof(jsonUtil.bind(passport.get("proof"), new TypeReference<>() {}));
        }

        if(passport.has("issuer")){
            String issuer = passport.get("issuer").asText();
            verificationInfo.setWallet(issuer);
            verificationInfo.setIssuer(this.extractBPNFromIssuer(issuer));
        }
        if(passport.has("validFrom")){
            verificationInfo.setIssuedAt(passport.get("validFrom").asText());
        } else if(passport.has("issuanceDate")) {
            verificationInfo.setIssuedAt(passport.get("issuanceDate").asText());
        }

        if(passport.has("validUntil")){
            verificationInfo.setExpiresAt(passport.get("validUntil").asText());
        } else if(passport.has("expirationDate")) {
            verificationInfo.setExpiresAt(passport.get("expirationDate").asText());
        }
        return verificationInfo;
    }

    public String extractBPNFromIssuer(String issuer){
        String bpn = CatenaXUtil.getBPN(issuer);
        if(bpn == null){
            return issuer;
        }
        return bpn;
    }

    public VerificationInfo buildVerification(JsonNode verifiableCredential, VerificationInfo verificationInfo){
        JsonNode response = null;
        boolean verified = false;
        String message = "An unexpected error occurred while verifying!";
        try{
            response =  walletService.verifyCredential(verifiableCredential);
        } catch (Exception e) {
            verificationInfo.setVerified(false);
            verificationInfo.setError("An error occurred while verifying, the credential signature was not able to be verified!");
            return verificationInfo;
        }
        if (response == null) {
            verificationInfo.setVerified(false);
            verificationInfo.setError("An error occurred while verifying, the wallet service returned null!");
            return verificationInfo;
        }

        if (response.has("verified")) {
            verified = response.get("verified").asBoolean();
        }

        if (response.has("message"))
            message = response.get("message").asText();

        if(!verified){
            LogUtil.printError("[DPP VERIFICATION ADD-ON] [VERIFICATION FAILED] " + message);
            verificationInfo.setError(message);
        }

        verificationInfo.setVerified(verified);
        return verificationInfo;
    }
}
