<!-- 
  Tractus-X - Digital Product Passport Application 
 
  Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation

  See the NOTICE file(s) distributed with this work for additional
  information regarding copyright ownership.
 
  This program and the accompanying materials are made available under the
  terms of the Apache License, Version 2.0 which is available at
  https://www.apache.org/licenses/LICENSE-2.0.
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  either express or implied. See the
  License for the specific language govern in permissions and limitations
  under the License.
 
  SPDX-License-Identifier: Apache-2.0
-->

# Policy Configuration Guide

## Introduction

Aiming to assure data sovereignty and to protect companies from accepting contracts negotiations with policies
which are not compliant to their company contract exchange guidelines, a solution for indicating which policies
should be chosen from the contract catalog incoming from the EDC is provided.


When auto-negotiation is enabled for the assets or done for the digital twin registry assets, the policies must be specified.

| Asset                       | Auto-Negotiation | Manual Negotiation | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
|-----------------------------|------------------|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Digital Twin Registry       | X                |                    | Just auto-negotiation is enabled, where the contract agreements get stored for optimizing the data retrieval process. The allowed policies can be configured in the helm chart value files.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| Aspect Model (Data Service) | X                | X                  | Negotiation is always automatic, however the policies can be "manually selected" in the frontend if disabled. If no policy is send in the "agree" response the policy will be took from configuration. Just the policies provided in the configuration will be chosen, and will be allowed to negotiate. A filter from the catalog will be done for the contracts with valid policies. Allowing the user in the manual selection to chose the policy he wants. I case for the manual selection the user wants to be able to see all the policies the `policyConfig.enabled` from the `passport` configuration needs to be set to false. More information [here](#manual-negotiation) |


> [!IMPORTANT]
> 
> The Digital Product Passport application does not have any logic to check if the policy is valid in the network or if the partner retrieving information using SSI has the authorized policy permissions for accessing and using the asset. This logic is already covered by the `Eclipse DataSpace Connector (EDC)` which is consuming the information from the network in name of the digital product pass application.  

## Table of contents

<!-- TOC -->
* [Policy Configuration Guide](#policy-configuration-guide)
  * [Introduction](#introduction)
  * [Table of contents](#table-of-contents)
* [DPP Backend Configuration](#dpp-backend-configuration)
  * [Helm Chart Details](#helm-chart-details)
  * [Parameters](#parameters)
    * [Main Parameters](#main-parameters)
    * [Policy Set Configuration](#policy-set-configuration)
      * [Action Configuration](#action-configuration)
        * [Logical Constraint Configuration](#logical-constraint-configuration)
        * [Constraint Configuration](#constraint-configuration)
    * [Comparison Modes](#comparison-modes)
* [DPP Frontend Configuration](#dpp-frontend-configuration)
  * [Policy Selection Options](#policy-selection-options)
    * [Auto-Negotiation](#auto-negotiation)
    * [Manual Negotiation](#manual-negotiation)
    * [Agree Contract](#agree-contract)
      * [Policy Interpretation](#policy-interpretation)
      * [Decline Contract](#decline-contract)
* [Examples](#examples)
  * [Logical Constraints](#logical-constraints)
  * [Multiple Policies](#multiple-policies)
  * [Single Constraint](#single-constraint)
<!-- TOC -->

# DPP Backend Configuration

## Helm Chart Details

In the helm charts configuration`/charts/digital-product-pass/values.yaml` two different policy configurations keys are in place:

| Key Path                                  | Description                                                                                                                                                                                                      |
|-------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `backend.passport.policyCheck`            | In this configuration the policies are going to be checked against the aspect models "assets" avaialable at the catalog. Its function is to act as a filter for the incoming policies.                           |
| `backend.digitalTwinRegistry.policyCheck` | In this configuration the policies configured are checked against the digital twin registry policies. It purpose is to select policies from the catalog in order to comply with the companies policy guidelines. |

## Parameters

### Main Parameters

In order to configure the policies and do the checks the configuration in both keys are identical:

| Key                      | Type                  | Description                                                                                                                                                         |
|--------------------------|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `policyCheck.enabled`    | **Boolean**           | Indicates if the policy check is enabled or disabled. In case it is disabled the first policy available will chosen.                                                |
| `policyCheck.strictMode` | **Boolean**           | Defines if the policy check will be done strictly using hashes or by default comparing each the constraint against the configuration. More details in section "Vali |
| `policyCheck.policies`   | **Array of Policies** | Contains the list of allowed policies to be chosen from the edc catalog. Its structure is approximated to the semantic of the policies.                             |

### Policy Set Configuration

For each policy in `policyCheck.policies` a specific configuration needs to be specified:

| Key           | Type                 | Description                                                                                                                    |
|---------------|----------------------|--------------------------------------------------------------------------------------------------------------------------------|
| `permission`  | **Array of Actions** | Describes the permissions "constraints" and actions allowed at the incoming policies. Access and usage policies configuration. |
| `prohibition` | **Array of Actions** | Describes the prohibitions "constraints" and actions allowed at the incoming policies.                                         |
| `obligation`  | **Array of Actions** | Describes the obligations "constraints" and actions allowed at the incoming policies.                                          |

> [!CAUTION]
> 
> Currently just the `permission` constraint is standardized in Catena-X. Check the following guide to learn and understand how the policies should be configured in Catena-X: [catenax-eV/cx-odrl-profile](https://github.com/catenax-eV/cx-odrl-profile/blob/main/profile.md)

#### Action Configuration

For each action in `policies.permission` or `policies.prohibition` or `policies.obligation` a specific configuration needs to be specified:

| Key                 | Type                     | Description                                                                                        | Optional |
|---------------------|--------------------------|----------------------------------------------------------------------------------------------------|----------|
| `action`            | **String**               | Specified the specific action identifier. Example: `USE`.                                          |          |
| `logicalConstraint` | **String**               | Specifies the logical constraint to be used in case more than one "constraint" is specified.       | X        |
| `constraints`       | **Array of Constraints** | Specified the list of constraints allowed to be negotiated in the digital product pass applicaiton |          |

##### Logical Constraint Configuration

In the `logicalConstraint` at the moment three different keys are supported:

| Key                         | Number of Policies Allowed | Description                                                                                                                                               |
|-----------------------------|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `odrl:and`                  | *No Limit*                 | If the policy incoming has a logical constraint from the AND logic door it will be checked against this property                                          |
| `odrl:or`                   | *No Limit*                 | If the policy incoming has a logical constraint from the OR logic door it will be checked against this property                                           |
| `null` or key not specified | *1*                        | If the key is not specified or is null then just **one** constraint is allowed, if more constraints are mentioned **just the first one will be selected** |

##### Constraint Configuration

Each and every individual constraint in `constraints` is configured in the following way

| Key            | Type                      | Description                                                                                              |
|----------------|---------------------------|----------------------------------------------------------------------------------------------------------|
| `leftOperand`  | **String**                | The left operand indicated which key is being specified for the right operand                            |
| `operator`     | **String**                | The operator stands in between the left and right operands, its the logic that specifies the constraints |
| `rightOperand` | **String** or **Integer** | The right operand indicates the value from the leftOperand key.                                          |

### Comparison Modes

Two available modes of comparison are available for the key `strictMode`.

| Key              | `strictMode` value | Description                                                                                                                                                                                                                                                                                                                                                                                                                         |
|------------------|--------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Strict Mode**  | `true`             | Comparison using hashes. The two policies are parsed into the same "object" class in java so they adapt to the same structure, once it is done they will be hashed and compared. In this case the *order* from arrays of constraints and actions are relevand and it must be *identical* to the other policy. Because of the properties of hashes, since if one byte changes it would affect the result, creating a different hash. |
| **Default Mode** | `false`            | Comparison from each constraint and action against eachother. The two policies will be compared constraint by constraint using a permisive mode, where strings are compared ignoring if they are `lowerCase` or `upperCase`, and also the **order from the constraints is not relevant**.                                                                                                                                           |

# DPP Frontend Configuration

In the frontend the possibility to manually chose the policy is configured in this parameter `frontend.negotiation.autoSign`.
If the parameter is set to `false` the backend will automatically select the first valid policy available in the catalog and matching the policy configurations.

## Policy Selection Options

For viewing and understanding the latest information regarding the policy selection flow see th [User Manual](../user/UserManual.md).

In this documentation some points and resources will be indicated:

### Auto-Negotiation

If Auto sign setting is enabled, the first valid contract policy checked against the configuration is always chosen as shown in below screenshot.
The passport is shown to the user as shown in a [Passport Page](../user/UserManual.md#passport-page).

![Loading Product Pass](../user/media/loadingPass.png)

### Manual Negotiation

This feature requires user action. If disabled, the contract policy must be choosen by the user from the popup menu during the loading process.

![Before Contract Policy Selection](../user/media/beforePolicySelection.png)

The sign feature basically signs the contract policy before the contract negotiation is done. The right contract policy must be selected by the user, otherwise the contract negotiation is aborted and user is returned back to the [Main Menu](../user/UserManual.md#main-menu). 

![Agree Contract policy](../user/media/agreePolicy.png)

### Agree Contract

In the **Choose a policy** dialog, there might be more than one policy listed. User needs to select the appropriate one and click on the **Agree** button.

The policy shown to the user, is written in Open Digital Rights Language (ODRL), which is translated into a more visual interpretation. For more information consult [Policy Interpretation](#policy-interpretation)

#### Policy Interpretation
This section defines how the policy can be interpreted to make it more understandable. The contract policy contains two different constraints separated by **OR**, **AND** or **None** logical constraints against the defined asset Id in a target:

_Example of policy content_:
A user or company can have access to this policy either they have **Membership** equals to **active** or **FrameworkAgreemen.sustainability** equals to **active**.

In case the user want to see more details from the policy, it can be done by clicking in "More Details" which will display a JSON tree, with the complete policy and contract available for deep inspection.

> **_NOTE:_**
*The contract policy is always checked against the defined asset Id in a target field*

![View Contract Policy](../user/media/viewPolicy.png)

The user accepts the right contract policy, and click on the **Agree** button which resumes the remaining negotiation and data transfer steps. In the end, the passport data is retrieved and displays to the user [Passport Page](../user/UserManual.md#passport-page).

![After Contract Policy Selection](../user/media/afterPolicySelection.png)

#### Decline Contract
If a user is not permitted to accept a particular policy from his company, the contract policy can be declined in this case. The user will be redirected to the [Main Menu](../user/UserManual.md#main-menu).

![Decline Contract Policy](../user/media/declinePolicy.png)

# Examples

## Logical Constraints

In case the and operator is used here is the way how it would be configured. For this policy in `JSON` format:

```json
{
   "@context": {
      "odrl": "http://www.w3.org/ns/odrl/2/",
      "cx-policy": "https://w3id.org/catenax/policy/"
   },
   "@type": "PolicyDefinitionRequest",
   "@id": "cx-policy",
   "profile": "cx-policy:profile2405",
   "policy": {
      "@type": "Policy",
      "odrl:permission": [
         {
            "odrl:action": "USE",
            "odrl:constraint": {
               "@type": "LogicalConstraint",
               "odrl:and": [
                  {
                     "@type": "Constraint",
                     "odrl:leftOperand": "cx-policy:Membership",
                     "odrl:operator": {
                        "@id": "odrl:eq"
                     },
                     "odrl:rightOperand": "active"
                  },
                  {
                     "@type": "Constraint",
                     "odrl:leftOperand": "cx-policy:FrameworkAgreement",
                     "odrl:operator": {
                        "@id": "odrl:eq"
                     },
                     "odrl:rightOperand": "circulareconomy:1.0"
                  },
                  {
                     "@type": "Constraint",
                     "odrl:leftOperand": "cx-policy:UsagePurpose",
                     "odrl:operator": {
                        "@id": "odrl:eq"
                     },
                     "odrl:rightOperand": "cx.circular.dpp:1"
                  }
               ]
            }
         }
      ],
      "odrl:obligation": [],
      "odrl:prohibition": []
   }
}
```

The complete configuration in the `passport` section would be the following in `YAML` format with reduced complexity:

```yaml
policyCheck:
  enabled: true
  strictMode: false # -- Can be enabled if order from constraints is important
  policies:
    - permission:
        - action: "USE"
          logicalConstraint: "odrl:and"
          constraints:
            - leftOperand: "cx-policy:Membership"
              operator: "odrl:eq"
              rightOperand: "active"
            - leftOperand: "cx-policy:FrameworkAgreement"
              operator: "odrl:eq"
              rightOperand: "circulareconomy:1.0"
            - leftOperand: "cx-policy:UsagePurpose"
              operator: "odrl:eq"
              rightOperand: "cx.circular.dpp:1"
      prohibition: [ ]
      obligation: [ ]
```

## Multiple Policies

In case of multiple allowed policies the configuration will look like this:

>[!TIP]
> 
> The `prohibition` and `obligation` constraints if empty can be omitted!

```yaml
policyCheck:
  enabled: true
  strictMode: false # -- Can be enabled if order from constraints is important
  policies:
    # -- All three constraints must be there if the "odrl:and" constraint
    - permission:
        - action: "USE"
          logicalConstraint: "odrl:and"
          constraints:
            - leftOperand: "cx-policy:Membership"
              operator: "odrl:eq"
              rightOperand: "active"
            - leftOperand: "cx-policy:FrameworkAgreement"
              operator: "odrl:eq"
              rightOperand: "circulareconomy:1.0"
            - leftOperand: "cx-policy:UsagePurpose"
              operator: "odrl:eq"
              rightOperand: "cx.circular.dpp:1"
      prohibition: [ ]
      obligation: [ ]
    # -- Just one constraint is mentioned and without logical constraint
    - permission:
        - action: "USE"
          constraints:
            - leftOperand: "cx-policy:Membership"
              operator: "odrl:eq"
              rightOperand: "active"
      prohibition: [ ]
      obligation: [ ]
    # -- Example with "odrl:or" logical constraint
    - permission:
        - action: "USE"
          logicalConstraint: "odrl:or"
          constraints:
            - leftOperand: "cx-policy:FrameworkAgreement"
              operator: "odrl:eq"
              rightOperand: "circulareconomy:1.0"
            - leftOperand: "cx-policy:FrameworkAgreement"
              operator: "odrl:eq"
              rightOperand: "pcf:1.0"
      prohibition: [ ]
      obligation: [ ]
```

## Single Constraint

In case the policy is simple and has one constraint, no `logicalConstraint` shall be added.

```yaml
policyCheck:
  enabled: true
  strictMode: false # -- Can be enabled if order from constraints is important
  policies:
    # -- Just one constraint is mentioned and without logical constraint
    - permission:
        - action: "USE"
          constraints:
            - leftOperand: "cx-policy:Membership"
              operator: "odrl:eq"
              rightOperand: "active"
      prohibition: [ ]
      obligation: [ ]
```

In case another constraint is added just the first one will be considered:
```yaml
policyCheck:
  enabled: true
  strictMode: false # -- Can be enabled if order from constraints is important
  policies:
    # -- Just one constraint is mentioned and without logical constraint
    - permission:
        - action: "USE"
          # -- No logical constraint mentioned here
          constraints:
            # -- Just this constraint will be selected
            - leftOperand: "cx-policy:Membership"
              operator: "odrl:eq"
              rightOperand: "active"
            # -- This constraint will be ignored
            - leftOperand: "cx-policy:UsagePurpose"
              operator: "odrl:neq"
              rightOperand: "cx.core.qualityNotifications:1"
      prohibition: [ ]
      obligation: [ ]

```
The reason behind this is that the policies without logical constraints look like this:

```json
{
   "@context": {
      "odrl": "http://www.w3.org/ns/odrl/2/",
      "cx-policy": "https://w3id.org/catenax/policy/"
   },
   "@type": "PolicyDefinitionRequest",
   "@id": "cx-policy",
   "profile": "cx-policy:profile2405",
   "policy": {
      "@type": "Policy",
      "odrl:permission": [
         {
            "odrl:action": "USE",
            "odrl:constraint": {
               "odrl:leftOperand": "cx-poliy:Membership",
               "odrl:operator": {
                  "@id": "odrl:eq"
               },
               "odrl:rightOperand": "active"
            }
         }
      ],
      "odrl:obligation": [],
      "odrl:prohibition": []
   }
}
```

If you want to have multiple single constraints do it like this:

```yaml
policyCheck:
  enabled: true
  strictMode: false # -- Can be enabled if order from constraints is important
  policies:
    # -- Just one constraint is mentioned and without logical constraint
    - permission:
        - action: "USE"
          # -- No logical constraint mentioned here
          constraints:
            # -- Just this constraint will be selected
            - leftOperand: "cx-policy:Membership"
              operator: "odrl:eq"
              rightOperand: "active"
      # -- Just one constraint is mentioned and without logical constraint
    - permission:
        - action: "USE"
          # -- No logical constraint mentioned here
          constraints:
            # -- This constraint will be considered now
            - leftOperand: "cx-policy:UsagePurpose"
              operator: "odrl:neq"
              rightOperand: "cx.core.qualityNotifications:1"

```
