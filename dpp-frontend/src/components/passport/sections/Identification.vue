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
              callCurrentData.localIdentifier ||
                callCurrentData.manufacturerId ||
                callCurrentData.localIdentifiers ||
                callCurrentData.dataMatrixCode ||
                callCurrentData.batch ||
                callCurrentData.codes
            )
          "
        >
          <template v-if="callCurrentData.manufacturerId">
            <Field
              :icon="callIconFinder('additionalCode')"
              :value="callCurrentData.manufacturerId"
              :label="$t('sections.identification.manufacturerId')"
            />
          </template>
          <template
            v-for="attr in callCurrentData.localIdentifiers"
            :key="attr"
          >
            <Field
              :icon="callIconFinder('additionalCode')"
              :value="attr.value"
              :label="attr.key"
            />
          </template>
          <template v-if="callCurrentData.dataMatrixCode">
            <Field
              :icon="callIconFinder('additionalCode')"
              :value="callCurrentData.dataMatrixCode"
              :label="$t('sections.identification.dataMatrixCode')"
            />
          </template>
          <template v-if="callCurrentData.batch">
            <template v-for="attr in callCurrentData.batch" :key="attr">
              <Field
                :icon="callIconFinder('batch')"
                :value="attr.value"
                :label="callToSentenceCase(attr.key)"
              />
            </template>
          </template>
          <template v-if="callCurrentData.codes">
            <template v-for="attr in callCurrentData.codes" :key="attr">
              <DialogComponent class="field-dialog">
                <Field
                  :info="attr.description ? attr.description : null"
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
              callCurrentData.localIdentifier ||
                callCurrentData.chemistry ||
                callCurrentData.idDmc ||
                callCurrentData.category ||
                callCurrentData.type ||
                callCurrentData.classification
            )
          "
        >
          <template v-if="callCurrentData.type">
            <Field
              :icon="callIconFinder('type')"
              :value="callCurrentData.type.manufacturerPartId"
              :label="callCurrentData.type.nameAtManufacturer"
            />
          </template>
          <template v-if="callCurrentData.classification">
            <template
              v-for="attr in callCurrentData.classification"
              :key="attr"
            >
              <DialogComponent class="field-dialog">
                <Field
                  :info="
                    attr.classificationDescription
                      ? attr.classificationDescription
                      : null
                  "
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
          <template v-if="callCurrentData.localIdentifier">
            <Field
              :icon="callIconFinder('localIdentifiers')"
              :value="callCurrentData.localIdentifier.value"
              :label="callCurrentData.localIdentifier.key"
            />
          </template>
          <template v-if="callCurrentData.chemistry">
            <Field
              :icon="callIconFinder('chemistry')"
              :value="callCurrentData.chemistry"
              :label="$t('sections.identification.chemistry')"
            />
          </template>
          <template v-if="callCurrentData.idDmc">
            <Field
              :icon="callIconFinder('idDmc')"
              :value="callCurrentData.idDmc"
              :label="$t('sections.identification.idDmc')"
            />
          </template>
          <template v-if="callCurrentData.category">
            <Field
              :icon="callIconFinder('category')"
              :value="callCurrentData.category"
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
              callCurrentData.additionalCode ||
                callCurrentData.dataCarrier ||
                callCurrentData.identification ||
                callCurrentData.serial
            )
          "
        >
          <template v-if="callCurrentData.serial">
            <template v-for="attr in callCurrentData.serial" :key="attr">
              <Field
                :icon="callIconFinder('serial')"
                :value="attr.value"
                :label="callToSentenceCase(attr.key)"
              />
            </template>
          </template>
          <template v-if="callCurrentData.additionalCode">
            <template
              v-for="attr in callCurrentData.additionalCode"
              :key="attr"
            >
              <Field
                :icon="callIconFinder('additionalCode')"
                :value="attr.value"
                :label="attr.key"
              />
            </template>
          </template>
          <template v-if="callCurrentData.dataCarrier">
            <Field
              :icon="callIconFinder('dataCarrier')"
              :value="callCurrentData.dataCarrier.carrierType"
              :label="callCurrentData.dataCarrier.carrierLayout"
            />
          </template>
          <template v-if="callCurrentData.identification">
            <template v-if="callCurrentData.identification.batch">
              <template
                v-for="attr in callCurrentData.identification.batch"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('batch')"
                  :value="attr.value"
                  :label="callToSentenceCase(attr.key)"
                />
              </template>
            </template>
            <template v-if="callCurrentData.identification.codes">
              <template
                v-for="attr in callCurrentData.identification.codes"
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
            <template v-if="callCurrentData.identification.type">
              <Field
                :icon="callIconFinder('type')"
                :value="callCurrentData.identification.type.manufacturerPartId"
                :label="callCurrentData.identification.type.nameAtManufacturer"
              />
            </template>
            <template v-if="callCurrentData.identification.classification">
              <template
                v-for="attr in callCurrentData.identification.classification"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('classification')"
                  :value="attr.classificationID"
                  :label="attr.classificationStandard"
                />
              </template>
            </template>
            <template v-if="callCurrentData.identification.serial">
              <template
                v-for="attr in callCurrentData.identification.serial"
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
      propsData: this.$props.data.aspect.identification || {},
      tppData: this.$props.data.aspect.generic?.identification || {},
    };
  },
  computed: {
    callCurrentData() {
      return passportUtil.currentData(this.tppData, this.propsData);
    },
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
