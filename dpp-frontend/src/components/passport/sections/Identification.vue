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

<template>
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="
            callHasContent(
              propsData.localIdentifier ||
                propsData.manufacturerId ||
                propsData.localIdentifiers ||
                propsData.dataMatrixCode ||
                propsData.batch ||
                propsData.codes
            )
          "
        >
          <template v-if="propsData.manufacturerId">
            <Field
              :icon="callIconFinder('additionalCode')"
              :value="propsData.manufacturerId"
              :label="$t('sections.identification.manufacturerId')"
            />
          </template>
          <template v-for="attr in propsData.localIdentifiers" :key="attr">
            <Field
              :icon="callIconFinder('additionalCode')"
              :value="attr.value"
              :label="attr.key"
            />
          </template>
          <template v-if="propsData.dataMatrixCode">
            <Field
              :icon="callIconFinder('additionalCode')"
              :value="propsData.dataMatrixCode"
              :label="$t('sections.identification.dataMatrixCode')"
            />
          </template>
          <template v-if="propsData.batch">
            <template v-for="attr in propsData.batch" :key="attr">
              <Field
                :icon="callIconFinder('batch')"
                :value="attr.value"
                :label="callToSentenceCase(attr.key)"
              />
            </template>
          </template>
          <template v-if="propsData.codes">
            <template v-for="attr in propsData.codes" :key="attr">
              <DialogComponent class="field-dialog">
                <Field
                  info
                  :icon="callIconFinder('codes')"
                  :value="attr.value"
                  :label="callToSentenceCase(attr.key)"
                />
                <template v-slot:title>
                  {{ $t("sections.identification.description") }}
                </template>
                <template v-slot:text>
                  {{ attr.description }}
                </template>
              </DialogComponent>
            </template>
          </template>
        </v-col>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="
            callHasContent(
              propsData.localIdentifier ||
                propsData.chemistry ||
                propsData.idDmc ||
                propsData.category ||
                propsData.type ||
                propsData.classification
            )
          "
        >
          <template v-if="propsData.type">
            <Field
              :icon="callIconFinder('type')"
              :value="propsData.type.manufacturerPartId"
              :label="propsData.type.nameAtManufacturer"
            />
          </template>
          <template v-if="propsData.classification">
            <template v-for="attr in propsData.classification" :key="attr">
              <DialogComponent class="field-dialog">
                <Field
                  info
                  :icon="callIconFinder('classification')"
                  :value="attr.classificationID"
                  :label="$t('sections.identification.classification')"
                  :subText="attr.classificationStandard"
                />
                <template v-slot:title>
                  {{ $t("sections.identification.description") }}
                </template>
                <template v-slot:text>
                  {{ attr.classificationDescription }}
                </template>
              </DialogComponent>
            </template>
          </template>
          <template v-if="propsData.localIdentifier">
            <Field
              :icon="callIconFinder('localIdentifiers')"
              :value="propsData.localIdentifier.value"
              :label="propsData.localIdentifier.key"
            />
          </template>
          <template v-if="propsData.chemistry">
            <Field
              :icon="callIconFinder('chemistry')"
              :value="propsData.chemistry"
              :label="$t('sections.identification.chemistry')"
            />
          </template>
          <template v-if="propsData.idDmc">
            <Field
              :icon="callIconFinder('idDmc')"
              :value="propsData.idDmc"
              :label="$t('sections.identification.idDmc')"
            />
          </template>
          <template v-if="propsData.category">
            <Field
              :icon="callIconFinder('category')"
              :value="propsData.category"
              :label="$t('sections.identification.category')"
            />
          </template>
        </v-col>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="
            callHasContent(
              propsData.additionalCode ||
                propsData.dataCarrier ||
                propsData.identification ||
                propsData.serial
            )
          "
        >
          <template v-if="propsData.serial">
            <template v-for="attr in propsData.serial" :key="attr">
              <Field
                :icon="callIconFinder('serial')"
                :value="attr.value"
                :label="callToSentenceCase(attr.key)"
              />
            </template>
          </template>
          <template v-if="propsData.additionalCode">
            <template v-for="attr in propsData.additionalCode" :key="attr">
              <Field
                :icon="callIconFinder('additionalCode')"
                :value="attr.value"
                :label="attr.key"
              />
            </template>
          </template>
          <template v-if="propsData.dataCarrier">
            <Field
              :icon="callIconFinder('dataCarrier')"
              :value="propsData.dataCarrier.carrierType"
              :label="propsData.dataCarrier.carrierLayout"
            />
          </template>
          <template v-if="propsData.identification">
            <template v-if="propsData.identification.batch">
              <template
                v-for="attr in propsData.identification.batch"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('batch')"
                  :value="attr.value"
                  :label="callToSentenceCase(attr.key)"
                />
              </template>
            </template>
            <template v-if="propsData.identification.codes">
              <template
                v-for="attr in propsData.identification.codes"
                :key="attr"
              >
                <template v-if="attr.description">
                  <DialogComponent class="field-dialog">
                    <Field
                      :icon="callIconFinder('codes')"
                      :value="attr.value"
                      :label="attr.key"
                      info
                    />
                    <template v-slot:title>
                      {{ $t("sections.identification.description") }}
                    </template>
                    <template v-slot:text>
                      {{ attr.description }}
                    </template>
                  </DialogComponent>
                </template>
                <template v-else>
                  <Field
                    :icon="callIconFinder('codes')"
                    :value="attr.value"
                    :label="attr.key"
                  />
                </template>
              </template>
            </template>
            <template v-if="propsData.identification.type">
              <Field
                :icon="callIconFinder('type')"
                :value="propsData.identification.type.manufacturerPartId"
                :label="propsData.identification.type.nameAtManufacturer"
              />
            </template>
            <template v-if="propsData.identification.classification">
              <template
                v-for="attr in propsData.identification.classification"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('classification')"
                  :value="attr.classificationID"
                  :label="attr.classificationStandard"
                />
              </template>
            </template>
            <template v-if="propsData.identification.serial">
              <template
                v-for="attr in propsData.identification.serial"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('serial')"
                  :value="attr.value"
                  :label="callToSentenceCase(attr.key)"
                />
              </template>
            </template>
          </template>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>
<script>
import Field from "../Field.vue";
import DialogComponent from "../../general/Dialog.vue";
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "IdentificationComponent",
  components: {
    Field,
    DialogComponent,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      propsData: this.$props.data.aspect.identification,
    };
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
    callToSentenceCase(text) {
      return passportUtil.toSentenceCase(text);
    },
    callHasContent(data) {
      return passportUtil.hasContent(data);
    },
  },
};
</script>
