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

import org.eclipse.tractusx.productpass.exceptions.DataModelException;
import org.eclipse.tractusx.productpass.exceptions.ManagerException;
import org.eclipse.tractusx.productpass.models.manager.Process;
import org.springframework.stereotype.Component;
import utils.DateTimeUtil;
import utils.LogUtil;
import utils.ThreadUtil;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProcessDataModel {

    public Map<String, Process> dataModel;

    public ProcessDataModel() {
        this.dataModel = new HashMap<>();
    }
    public ProcessDataModel addProcess(Process process){
        this.dataModel.put(process.id, process);
        return this;
    }
    public ProcessDataModel setState(String processId, String state){
        Process process = this.dataModel.getOrDefault(processId, null);
        if(process == null){
            throw new DataModelException(this.getClass().getName(), "The process does not exists!");
        }
        process.state = state;
        process.updated = DateTimeUtil.getTimestamp();
        this.dataModel.put(processId, process);
        return this;
    }
    public String getState(String processId){
        return this.dataModel.get(processId).getState();
    }
    public ProcessDataModel startProcess(String processId, Runnable processRunnable){
        try {
            Process process = this.dataModel.getOrDefault(processId, null);
            if (process == null) {
                throw new DataModelException(this.getClass().getName(), "The process does not exists!");
            }
            process.state = "RUNNING";
            process.thread = ThreadUtil.runThread(processRunnable, processId);
            process.updated = DateTimeUtil.getTimestamp();
            this.dataModel.put(processId, process);
            return this;
        }catch (Exception e){
            throw new DataModelException(this.getClass().getName(), e, "It was not possible to start the process");
        }
    }

    public Process getProcess(String processId){
        return this.dataModel.getOrDefault(processId, null);
    }

    public Boolean processExists(String processId){
        return this.dataModel.containsKey(processId);
    }
    public Map<String, Process> getDataModel() {
        return dataModel;
    }

    public void setDataModel(Map<String, Process> dataModel) {
        this.dataModel = dataModel;
    }
}
