/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
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
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyConfig;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Constraint;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import utils.exceptions.UtilException;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/*
 * This test class is used to test various methods from the EDC Util class
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath*:application.yml")
@SpringBootTest(classes = {utils.JsonUtil.class, utils.EdcUtil.class, utils.FileUtil.class, DtrConfig.class})
@EnableAutoConfiguration
@ActiveProfiles("test")
class EdcUtilTest {
    @Autowired
    private DtrConfig dtrConfig;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private EdcUtil edcUtil;
    LinkedHashMap<String, Object> logicConstraint;
    LinkedHashMap<String, Object> constraint1;
    LinkedHashMap<String, Object> constraint2;
    LinkedHashMap<String, Object> operator;
    LinkedList<LinkedHashMap<String, Object>> constraints;
    LinkedHashMap<String, Object> action;
    LinkedList<LinkedHashMap<String, Object>> permissions;
    LinkedList<LinkedHashMap<String, Object>> prohibitions;
    LinkedList<LinkedHashMap<String, Object>> obligations;
    LinkedHashMap<String, Object> policy;
    LinkedList<LinkedHashMap<String, Object>> policies;
    LinkedHashMap<String, Object> credential;
    LinkedHashMap<String, Object> multipleCredential;
    @BeforeEach
    void setUp() {
        logicConstraint = new LinkedHashMap<>();
        constraint1 = new LinkedHashMap<>();
        constraint2 = new LinkedHashMap<>();
        operator = new LinkedHashMap<>();
        constraints = new LinkedList<>();
        policy = new LinkedHashMap<>();
        credential = new LinkedHashMap<>();
        action = new LinkedHashMap<>();
        permissions = new LinkedList<>();
        prohibitions = new LinkedList<>();
        obligations = new LinkedList<>();
        policies = new LinkedList<>();
        multipleCredential=  new LinkedHashMap<>();
        operator.put("@id", "odrl:eq");
        constraint1.put("odrl:leftOperand", "cx-policy:Membership");
        constraint1.put("odrl:operator", operator);
        constraint1.put("odrl:rightOperand", "active");

        constraint2.put("odrl:leftOperand", "cx-policy:FrameworkAgreement");
        constraint2.put("odrl:operator", operator);
        constraint2.put("odrl:rightOperand", "circulareconomy:1.0");
        constraints.add(constraint1);
        constraints.add(constraint2);
        logicConstraint.put("odrl:and", constraints);

        action.put("odrl:action", "USE");
        action.put("odrl:constraint", logicConstraint);

        permissions.add(action);
        policy.put("odrl:permission", permissions);
        policy.put("odrl:prohibition", prohibitions);
        policy.put("odrl:obligation", obligations);
        credential.put("policy", policy);
        policies.add(policy);
        policies.add(policy);
        multipleCredential.put("policy",policies);

    }
    /*
     * This test case parses a correct constraint in to the desired object format.
     */
    @Test
    void parseCorrectConstraint() {
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(constraint1, true));
        Constraint constraintResponse = edcUtil.parseConstraint(constraint1);
        LogUtil.printTest("[RESPONSE]: "+ jsonUtil.toJson(constraintResponse, true));
        assertNotNull(constraintResponse);
    }
    /*
     * This test case parses a logic complex constraint with the parse constraint logic
     */
    @Test
    void parseLogicConstraint() {
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(logicConstraint, true));
        Constraint constraintResponse = edcUtil.parseConstraint(logicConstraint);
        LogUtil.printTest("[RESPONSE]: "+ jsonUtil.toJson(constraintResponse, true));
        assertNull(constraintResponse);
    }
    /*
     * This test case parses a policy testing the configuration
     */
    @Test
    void isPolicyActionsValid() {
        // Bind policy to class
        Set mappedPolicy = jsonUtil.bind(this.policy, new TypeReference<>(){});
        if(mappedPolicy == null){
            throw new UtilException(EdcUtilTest.class, "[TEST EXCEPTION]: Failed to parse policy to type reference!");
        }
        // Get policy configuration
        PolicyConfig policyConfigs = this.dtrConfig.getPolicyCheck();
        if(policyConfigs == null){
            throw new UtilException(EdcUtilTest.class, "[TEST EXCEPTION]: Policy configuration not found!");
        }
        // Call the edcUtil method
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(mappedPolicy, true));
        Boolean constraintResponse = edcUtil.isPolicyActionsValid(mappedPolicy, policyConfigs);
        LogUtil.printTest("[RESPONSE]: "+ jsonUtil.toJson(constraintResponse, true));
        assertNull(constraintResponse);
    }
}