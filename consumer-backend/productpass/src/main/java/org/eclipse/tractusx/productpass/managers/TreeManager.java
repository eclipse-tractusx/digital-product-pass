/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
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

package org.eclipse.tractusx.productpass.managers;

import org.eclipse.tractusx.productpass.config.IrsConfig;
import org.eclipse.tractusx.productpass.exceptions.ManagerException;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin3;
import org.eclipse.tractusx.productpass.models.manager.Node;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.DateTimeUtil;
import utils.FileUtil;
import utils.JsonUtil;

import java.util.Map;
@Component
public class TreeManager {
    private FileUtil fileUtil;
    private JsonUtil jsonUtil;

    private ProcessManager processManager;

    private IrsConfig irsConfig;

    @Autowired
    public TreeManager(FileUtil fileUtil, JsonUtil jsonUtil, ProcessManager processManager, IrsConfig irsConfig) {
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.processManager = processManager;
        this.irsConfig = irsConfig;
    }

    public String newTreeFile(String processId, DigitalTwin3 digitalTwin){
        try {
            String path = processManager.getProcessFilePath(processId, this.irsConfig.getTree().getFileName());
            TreeDataModel treeDataModel = new TreeDataModel(
                    Map.of(
                            digitalTwin.getIdentification(),
                            new Node(
                                    digitalTwin
                            )
                    )
            );
            return jsonUtil.toJsonFile(
                    path,
                    treeDataModel.dataModel,
                    this.irsConfig.getTree().getIndent()
            ); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create the tree data model file");
        }
    }






}
