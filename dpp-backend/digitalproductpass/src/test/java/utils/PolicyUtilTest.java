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
import org.bouncycastle.est.ESTAuth;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import utils.exceptions.UtilException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * This test class is used to test various methods from the Policy Util class
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {utils.JsonUtil.class, utils.PolicyUtil.class, utils.FileUtil.class, DtrConfig.class})
@ComponentScan(basePackages = { "org.eclipse.tractusx.digitalproductpass" })
@EnableConfigurationProperties
class PolicyUtilTest {

    @Autowired
    private DtrConfig dtrConfig;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private PolicyUtil policyUtil;

    /*
     * This test case parses a correct constraint in to the desired object format.
     */
    @Test
    void buildPolicies() {
        if(this.dtrConfig == null){
            throw new UtilException(PolicyUtilTest.class, "[TEST EXCEPTION]: Configuration not found!");
        }
        PolicyCheckConfig policyCheckConfig = this.dtrConfig.getPolicyCheck();
        if(policyCheckConfig == null){
            throw new UtilException(PolicyUtilTest.class, "[TEST EXCEPTION]: The policy configuration was not found!");
        }
        // Get all the policies from configuration
        List<PolicyCheckConfig.PolicyConfig> policies = policyCheckConfig.getPolicies();
        if(policies == null){
            throw new UtilException(PolicyUtilTest.class, "[TEST EXCEPTION]: The policies in configuration were not found!");
        }
        // Call the edcUtil method
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(policies, true));
        List<Set> builtPolicies = policyUtil.buildPolicies(policies);
        LogUtil.printTest("[RESPONSE]: "+ jsonUtil.toJson(builtPolicies, true));
        assertEquals(builtPolicies.size(), policies.size());
    }
}