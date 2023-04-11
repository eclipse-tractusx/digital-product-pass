/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package org.eclipse.tractusx.productpass.models.manager;

import utils.FileUtil;
import utils.JsonUtil;

import java.nio.file.Paths;

public abstract class Manager {
    protected String dataModelName;
    protected String dataDir;
    protected String tmpDir;

    public DataModel dataModel;
    protected String dataModelPath;

    public void setManager(String className){
        this.dataDir = FileUtil.createDataDir(className);
        this.tmpDir = FileUtil.createTmpDir(className);
        this.dataModelName = this.getDataModelName();
        this.dataModel = new DataModel(this.dataModelName, this.dataDir);
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public String getDataModelPath() {
        return dataModelPath;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }
    public String buildDataModelPath(){
        return Paths.get(this.dataDir,this.dataModelName + ".json").toAbsolutePath().toString();
    }
    public String getDataModelName(){
        return "dataModel";
    }

    public DataModel loadDataModel(){
        this.dataModelPath = this.buildDataModelPath();
        if(!FileUtil.pathExists(this.dataModelPath)){
            JsonUtil.toJsonFile(this.dataModelPath, this.dataModel, true);
        }
        return (DataModel) JsonUtil.fromJsonFile(this.dataModelPath);
    }
    public String saveDataModel(){
        this.dataModelPath = this.buildDataModelPath();
        return JsonUtil.toJsonFile(this.dataModelPath, this.dataModel, true);
    }


}
