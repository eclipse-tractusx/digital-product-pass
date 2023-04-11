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

import java.nio.file.Paths;
import java.util.*;

public class DataModel extends HashMap<String, Object> {
    protected String dataModelName;
    protected String dataModelDir;
    protected String dataModelPath;
    public DataModel(String dataModelName,String dataModelDir) {
        this.dataModelName = dataModelName;
        this.dataModelDir = dataModelDir;
        this.dataModelPath = this.buildDataModelPath();
        super.put("name", this.dataModelName);
        super.put("data", new HashMap<String, Object>());
    }

    @Override
    public Object put(String key, Object value){
        Map<String, Object> tmpDataModel = (Map<String, Object>) super.get("data");
        tmpDataModel.put(key, value);
        super.put("data", tmpDataModel);
        return tmpDataModel;
    }

    public Object get(String key){
        Map<String, Object> tmpDataModel = (Map<String, Object>) super.get("data");
        return tmpDataModel.get(key);
    }

    public Map<String, Object> getData(){
        return (Map<String, Object>) super.get("data");
    }
    public String buildDataModelPath(){
        return Paths.get(this.dataModelDir,this.dataModelName + ".json").toAbsolutePath().toString();
    }

    public void save(){
        // Save not implemented
    }
}
