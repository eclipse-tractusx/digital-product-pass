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

import org.eclipse.tractusx.digitalproductpass.core.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.core.exceptions.ManagerException;
import org.eclipse.tractusx.digitalproductpass.core.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.core.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.digitalproductpass.core.models.dtregistry.SubModel;
import org.eclipse.tractusx.digitalproductpass.core.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.core.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.verification.config.CDCConfig;
import org.eclipse.tractusx.digitalproductpass.verification.config.VerificationConfig;
import org.eclipse.tractusx.digitalproductpass.verification.models.CertifiedDataCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.DateTimeUtil;
import utils.FileUtil;
import utils.HttpUtil;
import utils.JsonUtil;

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
    public String setVerificationStatus(String processId, SubModel subModel) {
        try {

            Boolean vc = this.isVerifiableCredential(subModel);

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
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }


    public Boolean isVerifiableCredential(SubModel subModel){

        CDCConfig cdcConfig = verificationConfig.getCertifiedDataCredential();
        List<CDCConfig.SemanticKey> keys = cdcConfig.getSemanticIds();

        // Check against keys the credential!

        return true;

    }
}
