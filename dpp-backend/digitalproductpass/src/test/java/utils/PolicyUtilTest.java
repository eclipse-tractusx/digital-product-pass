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
package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.bouncycastle.est.ESTAuth;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.config.PolicyCheckConfig;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import utils.exceptions.UtilException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * This test class is used to test various methods from the Policy Util class
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {utils.JsonUtil.class, utils.PolicyUtil.class, utils.FileUtil.class, org.eclipse.tractusx.digitalproductpass.config.DtrConfig.class, org.eclipse.tractusx.digitalproductpass.config.PassportConfig.class})
@EnableConfigurationProperties
class PolicyUtilTest {

    @Autowired
    DtrConfig dtrConfig;
    @Autowired
    PassportConfig passportConfig;

    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    PolicyUtil policyUtil;

    LinkedHashMap<String, Object> logicalConstraint;
    LinkedHashMap<String, Object> logicalConstraintDtr;
    LinkedHashMap<String, Object> constraint1;
    LinkedHashMap<String, Object> constraint2;
    LinkedHashMap<String, Object> constraint3;

    LinkedHashMap<String, Object> constraint4;
    LinkedHashMap<String, Object> operator;
    LinkedList<LinkedHashMap<String, Object>> constraints;
    LinkedList<LinkedHashMap<String, Object>> configuredConstraints;
    LinkedHashMap<String, Object> action;
    LinkedHashMap<String, Object> actionDtr;
    LinkedList<LinkedHashMap<String, Object>> permissions;

    LinkedHashMap<String, Object> singlePermission;
    LinkedList<LinkedHashMap<String, Object>> permissionsDtr;
    LinkedList<LinkedHashMap<String, Object>> prohibitions;
    LinkedList<LinkedHashMap<String, Object>> obligations;
    LinkedHashMap<String, Object> policy;
    LinkedHashMap<String, Object> uniquePolicy;
    LinkedHashMap<String, Object> policyDtr;
    LinkedList<LinkedHashMap<String, Object>> policies;
    LinkedList<LinkedHashMap<String, Object>> policiesDtr;
    LinkedHashMap<String, Object> credential;
    LinkedHashMap<String, Object> uniqueCredential;
    LinkedHashMap<String, Object> credentialDtr;
    LinkedHashMap<String, Object> multipleCredential;
    LinkedHashMap<String, Object> multipleCredentialDtr;
    PolicyCheckConfig policyCheckConfig;
    PolicyCheckConfig policyCheckConfigPassport;
    Set mappedPolicy;
    Set mappedPolicyDtr;

    List<PolicyCheckConfig.PolicyConfig> policiesConfig;
    @BeforeEach
    void setUp() {
        logicalConstraintDtr = new LinkedHashMap<>();
        logicalConstraint = new LinkedHashMap<>();
        constraint1 = new LinkedHashMap<>();
        constraint2 = new LinkedHashMap<>();
        constraint3 = new LinkedHashMap<>();
        constraint4 = new LinkedHashMap<>();
        operator = new LinkedHashMap<>();
        constraints = new LinkedList<>();
        configuredConstraints = new LinkedList<>();
        policy = new LinkedHashMap<>();
        uniquePolicy = new LinkedHashMap<>();
        policyDtr = new LinkedHashMap<>();
        credential = new LinkedHashMap<>();
        uniqueCredential = new LinkedHashMap<>();
        credentialDtr = new LinkedHashMap<>();
        action = new LinkedHashMap<>();
        actionDtr = new LinkedHashMap<>();
        permissions = new LinkedList<>();
        singlePermission = new LinkedHashMap<>();
        permissionsDtr = new LinkedList<>();
        prohibitions = new LinkedList<>();
        obligations = new LinkedList<>();
        policies = new LinkedList<>();
        policiesDtr = new LinkedList<>();
        multipleCredential = new LinkedHashMap<>();
        multipleCredentialDtr = new LinkedHashMap<>();
        operator.put("@id", "odrl:eq");
        constraint1.put("odrl:leftOperand", "cx-policy:Membership");
        constraint1.put("odrl:operator", operator);
        constraint1.put("odrl:rightOperand", "active");

        constraint2.put("odrl:leftOperand", "cx-policy:FrameworkAgreement");
        constraint2.put("odrl:operator", operator);
        constraint2.put("odrl:rightOperand", "circulareconomy:1.0");

        LinkedHashMap<String, Object> constraint5 = new LinkedHashMap<>();
        LinkedList<LinkedHashMap<String, Object>> correctContraints = new LinkedList<>();
        LinkedHashMap<String, Object> correctAction = new LinkedHashMap<>();
        LinkedHashMap<String, Object> correctLogicalConstraint = new LinkedHashMap<>();
        constraint5.put("odrl:leftOperand", "cx-policy:UsagePurpose");
        constraint5.put("odrl:operator", operator);
        constraint5.put("odrl:rightOperand", "cx.circular.dpp:1");
        constraints.add(constraint1);
        constraints.add(constraint2);
        correctContraints.add(constraint1);
        correctContraints.add(constraint2);
        correctContraints.add(constraint5);
        operator.put("@id", "odrl:eq");
        constraint3.put("odrl:leftOperand", "cx-policy:Membership");
        constraint3.put("odrl:operator", operator);
        constraint3.put("odrl:rightOperand", "active");

        constraint4.put("odrl:leftOperand", "cx-policy:UsagePurpose");
        constraint4.put("odrl:operator", operator);
        constraint4.put("odrl:rightOperand", "cx.core.digitalTwinRegistry:1");
        configuredConstraints.add(constraint3);
        configuredConstraints.add(constraint4);
        logicalConstraintDtr.put("odrl:and", configuredConstraints);
        logicalConstraint.put("odrl:and", constraints);

        correctLogicalConstraint.put("odrl:and", correctContraints);

        action.put("odrl:action", new LinkedHashMap<>(){{put("odrl:type","USE");}});
        action.put("odrl:constraint", logicalConstraint);

        correctAction.put("odrl:action", new LinkedHashMap<>(){{put("odrl:type","USE");}});
        correctAction.put("odrl:constraint", correctLogicalConstraint);

        actionDtr.put("odrl:action", new LinkedHashMap<>(){{put("odrl:type","USE");}});
        actionDtr.put("odrl:constraint", logicalConstraintDtr);

        singlePermission = correctAction;
        permissions.add(action);
        uniquePolicy.put("odrl:permission", singlePermission);
        uniquePolicy.put("odrl:prohibition", prohibitions);
        uniquePolicy.put("odrl:obligation", obligations);
        uniqueCredential.put("policy", uniquePolicy);

        policy.put("odrl:permission", permissions);
        policy.put("odrl:prohibition", prohibitions);
        policy.put("odrl:obligation", obligations);
        credential.put("policy", policy);

        permissionsDtr.add(actionDtr);
        policyDtr.put("odrl:permission", permissionsDtr);
        policyDtr.put("odrl:prohibition", prohibitions);
        policyDtr.put("odrl:obligation", obligations);
        credentialDtr.put("policy", policyDtr);
        policiesDtr.add(policyDtr);
        policiesDtr.add(policyDtr);
        multipleCredentialDtr.put("policy",policiesDtr);

        policies.add(policy);
        policies.add(policy);
        multipleCredential.put("policy",policies);
        // Bind policy to class
        mappedPolicyDtr = jsonUtil.bind(this.policyDtr, new TypeReference<>(){});
        if(mappedPolicyDtr == null){
            throw new UtilException(PolicyUtilTest.class, "[TEST EXCEPTION]: Failed to parse policy dtr to type reference!");
        }
        mappedPolicy = jsonUtil.bind(this.policy, new TypeReference<>(){});
        if(mappedPolicy == null){
            throw new UtilException(PolicyUtilTest.class, "[TEST EXCEPTION]: Failed to parse policy to type reference!");
        }

        if(this.dtrConfig == null){
            throw new UtilException(PolicyUtilTest.class, "[TEST EXCEPTION]: Configuration not found!");
        }
        policyCheckConfigPassport = this.passportConfig.getPolicyCheck();
        policyCheckConfig = this.dtrConfig.getPolicyCheck();
        if(policyCheckConfig == null){
            throw new UtilException(PolicyUtilTest.class, "[TEST EXCEPTION]: The policy configuration was not found!");
        }
        // Get all the policies from configuration
        policiesConfig = policyCheckConfig.getPolicies();
        if(policiesConfig == null){
            throw new UtilException(PolicyUtilTest.class, "[TEST EXCEPTION]: The policies in configuration were not found!");
        }

    }
    /*
     * Checks if all the constraints are the same and valid
     */
    @Test
    void defaultValidPolicyCheck() {
        try {
            // Call the edcUtil method
            List<Set> builtPolicies = policyUtil.buildPolicies(policiesConfig);
            LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(mappedPolicyDtr, true));
            LogUtil.printTest("[CONFIGURATION POLICIES]: " + jsonUtil.toJson(builtPolicies, true));
            Boolean result = policyUtil.defaultPolicyCheck(mappedPolicyDtr, builtPolicies);
            LogUtil.printTest("[RESPONSE]: " + jsonUtil.toJson(result, true));
            assertTrue(result);
        }catch(Exception e){
            throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
        }
    }
    /*
     * Checks if all the constraints are the same and invalid
     */
    @Test
    void defaultInvalidPolicyCheck() {
        try {
            List<Set> builtPolicies = policyUtil.buildPolicies(policiesConfig);
            LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(mappedPolicy, true));
            LogUtil.printTest("[CONFIGURATION POLICIES]: " + jsonUtil.toJson(builtPolicies, true));
            Boolean result = policyUtil.defaultPolicyCheck(mappedPolicy, builtPolicies);
            LogUtil.printTest("[RESPONSE]: " + jsonUtil.toJson(result, true));
            assertFalse(result);
        }catch(Exception e){
            throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
        }
    }
    /*
     * This test case parses a correct constraint in to the desired object format.
     */
    @Test
    void buildPolicies() {
        try{
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(policiesConfig, true));
        List<Set> builtPolicies = policyUtil.buildPolicies(policiesConfig);
        LogUtil.printTest("[RESPONSE]: "+ jsonUtil.toJson(builtPolicies, true));
        assertEquals(builtPolicies.size(), policiesConfig.size());
    }catch(Exception e){
        throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
    }
    }
    /*
     * This test case check is the policy is valid using hashes
     */
    @Test
    void strictValidPolicyCheck() {
        try{
        List<Set> builtPolicies = policyUtil.buildPolicies(policiesConfig);
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(mappedPolicyDtr, true));
        LogUtil.printTest("[CONFIGURATION POLICIES]: " + jsonUtil.toJson(builtPolicies, true));
        Boolean result = policyUtil.strictPolicyCheck(mappedPolicyDtr, builtPolicies);
        LogUtil.printTest("[RESPONSE]: "+ jsonUtil.toJson(result, true));
        assertTrue(result);
    }catch(Exception e){
        throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
    }
    }
    /*
     * This test case check is the policy is invalid using hashes
     */
    @Test
    void strictInvalidPolicyCheck() {
        try{
        List<Set> builtPolicies = policyUtil.buildPolicies(policiesConfig);
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(mappedPolicyDtr, true));
        LogUtil.printTest("[CONFIGURATION POLICIES]: " + jsonUtil.toJson(builtPolicies, true));
        Boolean result = policyUtil.strictPolicyCheck(mappedPolicy, builtPolicies);
        LogUtil.printTest("[RESPONSE]: "+ jsonUtil.toJson(result, true));
        assertFalse(result);
    }catch(Exception e){
        throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
    }
    }
    /*
     * This test case checks if a policy that is invalid is marked as invalid
     */
    @Test
    void isInvalidPolicyValid() {
        try{
        // Generate the policies from configuration
        List<Set> builtPolicies = this.policyUtil.buildPolicies(policiesConfig);
        LogUtil.printTest("[CONFIGURATION POLICIES]: " + jsonUtil.toJson(builtPolicies, true));
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(mappedPolicy, true));
        Boolean isValid = this.policyUtil.isPolicyValid(mappedPolicy, builtPolicies, false);
        LogUtil.printTest("[RESPONSE]: " + jsonUtil.toJson(isValid, true));
        assertFalse(isValid);
    }catch(Exception e){
        throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
    }
    }
    /*
     * This test case checks if a policy that is valid is marked as valid
     */
    @Test
    void isValidPolicyValid() {
        try{
        List<Set> builtPolicies = this.policyUtil.buildPolicies(policiesConfig);
        LogUtil.printTest("[CONFIGURATION POLICIES]: " + jsonUtil.toJson(builtPolicies, true));
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(mappedPolicyDtr, true));
        Boolean isValid = this.policyUtil.isPolicyValid(mappedPolicyDtr, builtPolicies,false);
        LogUtil.printTest("[RESPONSE]: " + jsonUtil.toJson(isValid, true));
        assertTrue(isValid);
        }catch(Exception e){
            throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
        }
    }

    /**
     * Evaluate if the policy in configuration is valid
     **/
    @Test
    void isPolicyValid(){
        try{
        // Generate the policies from configuration
        List<Set> builtPolicies = this.policyUtil.buildPolicies(policiesConfig);
        // Get the hashCodes from the different policies
        List<String> hashes = builtPolicies.stream().map(p -> CrypUtil.sha256(this.jsonUtil.toJson(p, false))).toList();
        LogUtil.printTest("[ALL VALID POLICY HASHES]: " + jsonUtil.toJson(hashes, true));
        String policyHash = CrypUtil.sha256(this.jsonUtil.toJson(this.policyDtr, false));
        LogUtil.printTest("[POLICY HASH]: " + jsonUtil.toJson(policyHash, true));
        assertTrue(hashes.contains(policyHash));
        }catch(Exception e){
            throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
        }
    }

    /**
     * This test case gets policies by constraint
     * **/
    @Test
    void getPolicyByConstraints() {
        try{
        LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(policiesDtr, true));
        LogUtil.printTest("[POLICY CONFIGURATION]: " + jsonUtil.toJson(policyCheckConfig, true));
        Set validPolicy = this.policyUtil.getPolicyByConstraints(policiesDtr, policyCheckConfig);
        LogUtil.printTest("[RESPONSE]: " + jsonUtil.toJson(validPolicy, true));
        assertNotNull(validPolicy);
        }catch(Exception e){
            throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
        }
    }
    /**
     * This test case gets policies by constraint when invalid
     * **/
    @Test
    void getPolicyInvalidByConstraints() {
        try{
            LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(policies, true));
            LogUtil.printTest("[POLICY CONFIGURATION]: " + jsonUtil.toJson(policyCheckConfig, true));
            Set validPolicy = this.policyUtil.getPolicyByConstraints(policies, policyCheckConfig);
            LogUtil.printTest("[RESPONSE]: " + jsonUtil.toJson(validPolicy, true));
            assertNull(validPolicy);
        }catch(Exception e){
            throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
        }
    }

    /**
     * Parse unique policy instead of array
     * **/
    @Test
    void parseUniquePolicy(){
        try{
            LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(uniquePolicy, true));
            LogUtil.printTest("[POLICY CONFIGURATION]: " + jsonUtil.toJson(policyCheckConfigPassport, true));
            Set validPolicy = this.policyUtil.getPolicyByConstraints(uniquePolicy, policyCheckConfigPassport);
            LogUtil.printTest("[RESPONSE]: " + jsonUtil.toJson(validPolicy, true));
            assertNotNull(validPolicy);
        }catch(Exception e){
            throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
        }
    }

    /**
     * Parse unique policy instead of array
     * **/
    @Test
    void getUniquePolicies(){
        try{
            LogUtil.printTest("[INPUT]: " + jsonUtil.toJson(uniquePolicy, true));
            LogUtil.printTest("[POLICY CONFIGURATION]: " + jsonUtil.toJson(policyCheckConfigPassport, true));
            List<Set> validPolicies = this.policyUtil.getValidPoliciesByConstraints(uniquePolicy, policyCheckConfigPassport);
            LogUtil.printTest("[RESPONSE]: " + jsonUtil.toJson(validPolicies, true));
            assertTrue(validPolicies.size() > 0);
        }catch(Exception e){
            throw new UtilException(PolicyUtilTest.class, e,"It was not possible to test the default policy check!");
        }
    }

}