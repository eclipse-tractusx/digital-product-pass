/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig.ActionConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig.PolicyConfig;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Constraint;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.exceptions.UtilException;

import java.util.ArrayList;

import java.util.List;


/**
 * This class is responsible for building and handling with policies
 *
 * <p> The methods defined here are intended to check or manipulate EDC's related data.
 */
@Component
public class PolicyUtil {

    public PolicyUtil() {
    }

    /**
     * Build policies from configuration parameters
     * <p>
     *
     * @param policyConfigs {@code List<PolicyConfig>} the list of policy configurations
     * @return {@code List<Set>} the list of parsed policies built from the configuration parameters
     * @throws UtilException if error when parsing the contracts
     */
    public List<Set> buildPolicies(List<PolicyConfig> policyConfigs){
        try {
            // Initialize policy array
            List<Set> policies = new ArrayList<>(policyConfigs.size());
            policyConfigs.forEach(policyConfig -> policies.add(new Set(policyConfig))); //Build the policies based on the configuration
            return policies; // Return the clean policies
        } catch (Exception e) {
            throw new UtilException(PolicyUtil.class, e, "It was not possible to parse the policies from configuration!");
        }
    }




}
