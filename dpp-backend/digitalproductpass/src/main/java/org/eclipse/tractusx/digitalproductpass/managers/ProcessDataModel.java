/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
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

package org.eclipse.tractusx.digitalproductpass.managers;

import org.eclipse.tractusx.digitalproductpass.exceptions.DataModelException;
import org.eclipse.tractusx.digitalproductpass.models.manager.Process;
import org.springframework.stereotype.Component;
import utils.DateTimeUtil;
import utils.ThreadUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * This class consists exclusively of methods to operate on the Process's Data Model.
 *
 * <p> The methods defined here are intended to do every needed operations in order to be able to add, start or get a Process, check if
 * a specific Process exists or to get and set the Process's state.
 *
 */
@Component
public class ProcessDataModel {

    /** ATTRIBUTES **/
    public Map<String, Process> dataModel;

    /** CONSTRUCTOR(S) **/
    public ProcessDataModel() {
        this.dataModel = new HashMap<>();
    }

    /** GETTERS AND SETTERS **/
    @SuppressWarnings("Unused")
    public Map<String, Process> getDataModel() {
        return dataModel;
    }
    @SuppressWarnings("Unused")
    public void setDataModel(Map<String, Process> dataModel) {
        this.dataModel = dataModel;
    }

    /** METHODS **/

    /**
     * Adds the given {@code Process} object into the Process Data Model.
     * <p>
     * @param   process
     *          the {@code Process} object to add.
     *
     * @return this {@code ProcessDataModel} object.
     *
     */
    public ProcessDataModel addProcess(Process process){
        this.dataModel.put(process.id, process);
        return this;
    }

    /**
     * Sets the state of the Process with the given process id with the given state value.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   state
     *          the {@code String} value of the new process's state (e.g: "FAILED", "NEGOTIATED", "COMPLETED").
     *
     * @return this {@code ProcessDataModel} object.
     *
     * @throws DataModelException
     *           if the process doesn't exist.
     *
     */
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

    /**
     * Gets the state of the Process with the given process id.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return this {@code String} state's value.
     *
     *
     */
    public String getState(String processId){
        return this.dataModel.get(processId).getState();
    }

    /**
     * Starts the Process with the given process id with the given thread level task.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   processRunnable
     *          the {@code Runnable} task to start in the process.
     *
     * @return this {@code ProcessDataModel} object.
     *
     * @throws DataModelException
     *           if unable to start the process.
     *
     */
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

    /**
     * Gets the Process with the given process id.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return the {@code Process} object found, if exists.
     *
     *
     */
    public Process getProcess(String processId){
        return this.dataModel.getOrDefault(processId, null);
    }

    /**
     * Checks if a Process with the given process id exists in the Process Data Model.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return true if the process exists, false otherwise.
     *
     *
     */
    public Boolean processExists(String processId){
        return this.dataModel.containsKey(processId);
    }

}
