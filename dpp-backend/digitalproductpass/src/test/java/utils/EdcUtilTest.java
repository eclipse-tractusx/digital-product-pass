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

import org.eclipse.tractusx.digitalproductpass.models.negotiation.Constraint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * This test class is used to test various methods from the EDC Util class
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {utils.JsonUtil.class, utils.EdcUtil.class, utils.FileUtil.class})
class EdcUtilTest {

    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private EdcUtil edcUtil;
    LinkedHashMap<String, Object> logicConstraint;
    LinkedHashMap<String, Object> constraint1;
    LinkedHashMap<String, Object> constraint2;
    LinkedHashMap<String, Object> operator;
    LinkedList<LinkedHashMap<String, Object>> constraints;
    @BeforeEach
    void setUp() {
        logicConstraint = new LinkedHashMap<>();
        constraint1 = new LinkedHashMap<>();
        constraint2 = new LinkedHashMap<>();
        operator = new LinkedHashMap<>();
        constraints = new LinkedList<>();
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
}