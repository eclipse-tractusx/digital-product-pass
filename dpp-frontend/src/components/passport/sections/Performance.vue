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
        <template v-if="propsData.rated">
          <v-col sm="12" md="3" class="pa-0 ma-0">
            <div class="element-chart-label">
              {{ $t("sections.performance.rated") }}
            </div>
            <template v-if="propsData.rated.roundTripEfficiency">
              <template
                v-if="propsData.rated.roundTripEfficiency.depthOfDischarge"
              >
                <Field
                  :icon="callIconFinder('depthOfDischarge')"
                  :label="$t('sections.performance.depthOfDischarge')"
                  :value="propsData.rated.roundTripEfficiency.depthOfDischarge"
                  unit="%"
                />
              </template>
              <template v-if="propsData.rated.roundTripEfficiency.temperature">
                <Field
                  :icon="callIconFinder('temperature')"
                  :label="$t('sections.performance.temperature')"
                  :value="propsData.rated.roundTripEfficiency.temperature"
                  unit="°C"
                />
              </template>
              <template
                v-if="propsData.rated.roundTripEfficiency['50PercentLife']"
              >
                <Field
                  :icon="callIconFinder('50PercentLife')"
                  :label="$t('sections.performance.50PercentLife')"
                  :value="propsData.rated.roundTripEfficiency['50PercentLife']"
                />
              </template>
              <template v-if="propsData.rated.roundTripEfficiency.initial">
                <Field
                  :icon="callIconFinder('roundTripEfficiency')"
                  :label="$t('sections.performance.initial')"
                  :value="propsData.rated.roundTripEfficiency.initial"
                />
              </template>
            </template>
            <template v-if="propsData.rated.selfDischargingRate">
              <Field
                :icon="callIconFinder('selfDischargingRate')"
                :label="$t('sections.performance.selfDischargingRate')"
                :value="propsData.rated.selfDischargingRate"
              />
            </template>
            <template v-if="propsData.rated.performanceDocument">
              <template
                v-for="attr in propsData.rated.performanceDocument"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder(attr.header)"
                  :label="attr.header"
                  :value="attr.content"
                />
              </template>
            </template>
            <template v-if="propsData.rated.testReport">
              <template v-for="attr in propsData.rated.testReport" :key="attr">
                <Field
                  :icon="callIconFinder(attr.header)"
                  :label="attr.header"
                  :value="attr.content"
                />
              </template>
            </template>
            <template v-if="propsData.rated.temperature">
              <Field
                :icon="callIconFinder('temperature')"
                :label="$t('sections.performance.lowerTemperature')"
                :value="propsData.rated.temperature.lower"
                unit="°C"
              />
              <Field
                :icon="callIconFinder('temperature')"
                :label="$t('sections.performance.upperTemperature')"
                :value="propsData.rated.temperature.upper"
                unit="°C"
              />
            </template>
          </v-col>
        </template>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <template v-if="propsData.rated.lifetime">
            <div class="element-chart-label">
              {{ $t("sections.performance.lifetime") }}
            </div>
            <template
              v-for="attr in propsData.rated.lifetime.report"
              :key="attr"
            >
              <Field
                :icon="callIconFinder(attr.header)"
                :label="attr.header"
                :value="attr.content"
              />
            </template>
            <template v-if="propsData.rated.lifetime.cycleLifeTesting">
              <div class="element-chart-label">
                {{ $t("sections.performance.cycleLifeTesting") }}
              </div>
              <template
                v-if="propsData.rated.lifetime.cycleLifeTesting.temperature"
              >
                <Field
                  :icon="callIconFinder('cycleLifeTesting')"
                  :label="$t('sections.performance.temperature')"
                  :value="propsData.rated.lifetime.cycleLifeTesting.temperature"
                  unit="°C"
                />
              </template>
              <template
                v-if="
                  propsData.rated.lifetime.cycleLifeTesting.depthOfDischarge
                "
              >
                <Field
                  :icon="callIconFinder('depthOfDischarge')"
                  :label="$t('sections.performance.depthOfDischarge')"
                  :value="
                    propsData.rated.lifetime.cycleLifeTesting.depthOfDischarge
                  "
                  unit="°C"
                />
              </template>
              <template
                v-if="
                  propsData.rated.lifetime.cycleLifeTesting.appliedDischargeRate
                "
              >
                <Field
                  :icon="callIconFinder('appliedDischargeRate')"
                  :label="$t('sections.performance.appliedDischargeRate')"
                  :value="
                    propsData.rated.lifetime.cycleLifeTesting
                      .appliedDischargeRate
                  "
                />
              </template>
              <template v-if="propsData.rated.lifetime.cycleLifeTesting.cycles">
                <Field
                  :icon="callIconFinder('cycles')"
                  :label="$t('sections.performance.cycles')"
                  :value="propsData.rated.lifetime.cycleLifeTesting.cycles"
                />
              </template>
            </template>
            <template v-if="propsData.rated.lifetime.expectedYears">
              <Field
                :icon="callIconFinder('expectedYears')"
                :label="$t('sections.performance.expectedYears')"
                :value="propsData.rated.lifetime.expectedYears"
              />
            </template>
          </template>
        </v-col>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <template v-if="propsData.rated.power">
            <div class="element-chart-label">
              {{ $t("sections.performance.power") }}
            </div>
            <template v-if="propsData.rated.power.at20SoC">
              <Field
                :icon="callIconFinder('at20SoC')"
                :label="$t('sections.performance.at20SoC')"
                :value="propsData.rated.power.at20SoC"
                unit="W"
              />
            </template>
            <template v-if="propsData.rated.power.temperature">
              <Field
                :icon="callIconFinder('temperature')"
                :label="$t('sections.performance.temperature')"
                :value="propsData.rated.power.temperature"
                unit="°C"
              />
            </template>
            <template v-if="propsData.rated.power.value">
              <Field
                :icon="callIconFinder('power')"
                :label="$t('sections.performance.power')"
                :value="propsData.rated.power.value"
                unit="W"
              />
            </template>
            <template v-if="propsData.rated.power.at80SoC">
              <Field
                :icon="callIconFinder('at80SoC')"
                :label="$t('sections.performance.at80SoC')"
                :value="propsData.rated.power.at80SoC"
                unit="W"
              />
            </template>
          </template>
          <template v-if="propsData.rated.resistance">
            <template v-if="propsData.rated.resistance.temperature">
              <Field
                :icon="callIconFinder('temperature')"
                :label="$t('sections.performance.temperature')"
                :value="propsData.rated.resistance.temperature"
                unit="°C"
              />
            </template>
            <template v-if="propsData.rated.resistance.cell">
              <Field
                :icon="callIconFinder('cell')"
                :label="$t('sections.performance.cell')"
                :value="propsData.rated.resistance.cell"
                unit="Ω"
              />
            </template>
            <template v-if="propsData.rated.resistance.pack">
              <Field
                :icon="callIconFinder('pack')"
                :label="$t('sections.performance.pack')"
                :value="propsData.rated.resistance.pack"
                unit="Ω"
              />
            </template>
            <template v-if="propsData.rated.resistance.module">
              <Field
                :icon="callIconFinder('module')"
                :label="$t('sections.performance.module')"
                :value="propsData.rated.resistance.module"
                unit="Ω"
              />
            </template>
          </template>
          <template v-if="propsData.rated.voltage">
            <template v-if="propsData.rated.voltage.temperature">
              <Field
                :icon="callIconFinder('temperature')"
                :label="$t('sections.performance.temperature')"
                :value="propsData.rated.voltage.temperature"
                unit="°C"
              />
            </template>
            <div class="element-chart-label">
              {{ $t("sections.performance.voltage") }}
            </div>
            <template v-if="propsData.rated.voltage.min">
              <Field
                :icon="callIconFinder('min')"
                :label="$t('sections.performance.voltageMin')"
                :value="propsData.rated.voltage.min"
                unit="V"
              />
            </template>
            <template v-if="propsData.rated.voltage.nominal">
              <Field
                :icon="callIconFinder('nominal')"
                :label="$t('sections.performance.voltageNominal')"
                :value="propsData.rated.voltage.nominal"
                unit="V"
              />
            </template>
            <template v-if="propsData.rated.voltage.max">
              <Field
                :icon="callIconFinder('max')"
                :label="$t('sections.performance.voltageMax')"
                :value="propsData.rated.voltage.max"
                unit="V"
              />
            </template>
          </template>
          <template v-if="propsData.rated.energy">
            <div class="element-chart-label">
              {{ $t("sections.performance.energy") }}
            </div>
            <template v-if="propsData.rated.energy.temperature">
              <Field
                :icon="callIconFinder('temperature')"
                :label="$t('sections.performance.temperature')"
                :value="propsData.rated.energy.temperature"
                unit="°C"
              />
            </template>
            <template v-if="propsData.rated.energy.value">
              <Field
                :icon="callIconFinder('energy')"
                :label="$t('sections.performance.energy')"
                :value="propsData.rated.energy.value"
                unit="kWh"
              />
            </template>
          </template>
          <template v-if="propsData.rated.capacity">
            <div class="element-chart-label">
              {{ $t("sections.performance.capacity") }}
            </div>
            <template v-if="propsData.rated.capacity.temperature">
              <Field
                :icon="callIconFinder('temperature')"
                :label="$t('sections.performance.temperature')"
                :value="propsData.rated.capacity.temperature"
                unit="°C"
              />
            </template>
            <template v-if="propsData.rated.capacity.value">
              <Field
                :icon="callIconFinder('capacity')"
                :label="$t('sections.performance.capacity')"
                :value="propsData.rated.capacity.value"
              />
            </template>
            <template v-if="propsData.rated.capacity.thresholdExhaustion">
              <Field
                :icon="callIconFinder('thresholdExhaustion')"
                :label="$t('sections.performance.thresholdExhaustion')"
                :value="propsData.rated.capacity.thresholdExhaustion"
              />
            </template>
          </template>
        </v-col>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <template v-if="propsData.dynamic">
            <div class="element-chart-label">
              {{ $t("sections.performance.dynamic") }}
            </div>
            <template v-if="propsData.dynamic.selfDischargingRate">
              <Field
                :icon="callIconFinder('selfDischargingRate')"
                :label="$t('sections.performance.selfDischargingRate')"
                :value="propsData.dynamic.selfDischargingRate"
              />
            </template>
            <template v-if="propsData.dynamic.roundTripEfficiency">
              <div class="element-chart-label">
                {{ $t("sections.performance.roundTripEfficiency") }}
              </div>
              <template v-if="propsData.dynamic.roundTripEfficiency.remaining">
                <Field
                  :icon="callIconFinder('remaining')"
                  :label="$t('sections.performance.remaining')"
                  :value="propsData.dynamic.roundTripEfficiency.remaining.value"
                  :subText="
                    callFormattedDate(
                      propsData.dynamic.roundTripEfficiency.remaining.time
                    )
                  "
                />
              </template>
              <template v-if="propsData.dynamic.roundTripEfficiency.fade">
                <Field
                  :icon="callIconFinder('remaining')"
                  :label="$t('sections.performance.remaining')"
                  :value="propsData.dynamic.roundTripEfficiency.fade.value"
                  :subText="
                    callFormattedDate(
                      propsData.dynamic.roundTripEfficiency.fade.time
                    )
                  "
                />
              </template>
            </template>
            <template v-if="propsData.dynamic.operatingEnvironment">
              <div class="element-chart-label">
                {{ $t("sections.performance.operatingEnvironment") }}
              </div>
              <template
                v-for="attr in propsData.dynamic.operatingEnvironment"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder(attr.header)"
                  :label="attr.header"
                  :value="attr.content"
                />
              </template>
            </template>
            <template v-if="propsData.dynamic.stateOfCharge">
              <Field
                :icon="callIconFinder('stateOfCharge')"
                :label="$t('sections.performance.stateOfCharge')"
                :value="propsData.dynamic.stateOfCharge.value"
                unit="%"
                :subText="
                  callFormattedDate(propsData.dynamic.stateOfCharge.time)
                "
              />
            </template>
            <template v-if="propsData.dynamic.performanceDocument">
              <div class="element-chart-label">
                {{ $t("sections.performance.performanceDocument") }}
              </div>
              <template
                v-for="attr in propsData.dynamic.performanceDocument"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder(attr.header)"
                  :label="attr.header"
                  :value="attr.content"
                />
              </template>
            </template>
            <template v-if="propsData.dynamic.fullCycles">
              <Field
                :icon="callIconFinder('fullCycles')"
                :label="$t('sections.performance.fullCycles')"
                :value="propsData.dynamic.fullCycles.value"
                :subText="callFormattedDate(propsData.dynamic.fullCycles.time)"
              />
            </template>
            <template v-if="propsData.dynamic.power">
              <div class="element-chart-label">
                {{ $t("sections.performance.power") }}
              </div>
              <template v-if="propsData.dynamic.power.remaining">
                <Field
                  :icon="callIconFinder('remaining')"
                  :label="$t('sections.performance.remaining')"
                  :value="propsData.dynamic.power.remaining.value"
                  unit="W"
                  :subText="
                    callFormattedDate(propsData.dynamic.power.remaining.time)
                  "
                />
              </template>
              <template v-if="propsData.dynamic.power.fade">
                <Field
                  :icon="callIconFinder('fade')"
                  :label="$t('sections.performance.fade')"
                  :value="propsData.dynamic.power.fade.value"
                  unit="W"
                  :subText="
                    callFormattedDate(propsData.dynamic.power.fade.time)
                  "
                />
              </template>
            </template>
            <template v-if="propsData.dynamic.negativeEvents">
              <div class="element-chart-label">
                {{ $t("sections.performance.negativeEvents") }}
              </div>
              <template
                v-for="attr in propsData.dynamic.negativeEvents"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder(attr.header)"
                  :label="attr.header"
                  :value="attr.content"
                />
              </template>
            </template>
            <template v-if="propsData.dynamic.resistance">
              <div class="element-chart-label">
                {{ $t("sections.performance.resistance") }}
              </div>
              <template v-if="propsData.dynamic.resistance.increase">
                <template v-if="propsData.dynamic.resistance.increase.cell">
                  <Field
                    :icon="callIconFinder('increaseCell')"
                    :label="$t('sections.performance.increaseCell')"
                    :value="propsData.dynamic.resistance.increase.cell.value"
                    unit="Ω"
                    :subText="
                      callFormattedDate(
                        propsData.dynamic.resistance.increase.cell.time
                      )
                    "
                  />
                </template>
                <template v-if="propsData.dynamic.resistance.increase.pack">
                  <Field
                    :icon="callIconFinder('increasePack')"
                    :label="$t('sections.performance.increasePack')"
                    :value="propsData.dynamic.resistance.increase.pack.value"
                    unit="Ω"
                    :subText="
                      callFormattedDate(
                        propsData.dynamic.resistance.increase.pack.time
                      )
                    "
                  />
                </template>
                <template v-if="propsData.dynamic.resistance.increase.module">
                  <Field
                    :icon="callIconFinder('increaseModule')"
                    :label="$t('sections.performance.increaseModule')"
                    :value="propsData.dynamic.resistance.increase.module.value"
                    unit="Ω"
                    :subText="
                      callFormattedDate(
                        propsData.dynamic.resistance.increase.module.time
                      )
                    "
                  />
                </template>
              </template>
              <template v-if="propsData.dynamic.resistance.remaining">
                <template v-if="propsData.dynamic.resistance.remaining.cell">
                  <Field
                    :icon="callIconFinder('remainingCell')"
                    :label="$t('sections.performance.remainingCell')"
                    :value="propsData.dynamic.resistance.remaining.cell.value"
                    unit="Ω"
                    :subText="
                      callFormattedDate(
                        propsData.dynamic.resistance.remaining.cell.time
                      )
                    "
                  />
                </template>
                <template v-if="propsData.dynamic.resistance.remaining.pack">
                  <Field
                    :icon="callIconFinder('remainingPack')"
                    :label="$t('sections.performance.remainingPack')"
                    :value="propsData.dynamic.resistance.remaining.pack.value"
                    unit="Ω"
                    :subText="
                      callFormattedDate(
                        propsData.dynamic.resistance.remaining.pack.time
                      )
                    "
                  />
                </template>
                <template v-if="propsData.dynamic.resistance.remaining.module">
                  <Field
                    :icon="callIconFinder('remainingModule')"
                    :label="$t('sections.performance.remainingModule')"
                    :value="propsData.dynamic.resistance.remaining.module.value"
                    unit="Ω"
                    :subText="
                      callFormattedDate(
                        propsData.dynamic.resistance.remaining.module.time
                      )
                    "
                  />
                </template>
              </template>
            </template>
            <template v-if="propsData.dynamic.capacity">
              <div class="element-chart-label">
                {{ $t("sections.performance.capacity") }}
              </div>
              <template v-if="propsData.dynamic.capacity.fade">
                <Field
                  :icon="callIconFinder('capacityFade')"
                  :label="$t('sections.performance.capacityFade')"
                  :value="propsData.dynamic.capacity.fade.value"
                  :subText="
                    callFormattedDate(propsData.dynamic.capacity.fade.time)
                  "
                />
              </template>
              <template v-if="propsData.dynamic.capacity.throughput">
                <Field
                  :icon="callIconFinder('throughput')"
                  :label="$t('sections.performance.throughput')"
                  :value="propsData.dynamic.capacity.throughput.value"
                  :subText="
                    callFormattedDate(
                      propsData.dynamic.capacity.throughput.time
                    )
                  "
                />
              </template>
              <template v-if="propsData.dynamic.capacity.capacity">
                <Field
                  :icon="callIconFinder('capacity')"
                  :label="$t('sections.performance.capacity')"
                  :value="propsData.dynamic.capacity.capacity.value"
                  :subText="
                    callFormattedDate(propsData.dynamic.capacity.capacity.time)
                  "
                />
              </template>
            </template>
            <template v-if="propsData.dynamic.energy">
              <div class="element-chart-label">
                {{ $t("sections.performance.energy") }}
              </div>
              <template v-if="propsData.dynamic.energy.remaining">
                <Field
                  :icon="callIconFinder('remainingEnergy')"
                  :label="$t('sections.performance.remainingEnergy')"
                  :value="propsData.dynamic.energy.remaining.value"
                  unit="kWh"
                  :subText="
                    callFormattedDate(propsData.dynamic.energy.remaining.time)
                  "
                />
              </template>
              <template v-if="propsData.dynamic.energy.soce">
                <Field
                  :icon="callIconFinder('soce')"
                  :label="$t('sections.performance.soce')"
                  :value="propsData.dynamic.energy.soce.value"
                  :subText="
                    callFormattedDate(propsData.dynamic.energy.soce.time)
                  "
                />
              </template>
              <template v-if="propsData.dynamic.energy.throughput">
                <Field
                  :icon="callIconFinder('throughput')"
                  :label="$t('sections.performance.throughput')"
                  :value="propsData.dynamic.energy.throughput.value"
                  unit="kWh"
                  :subText="
                    callFormattedDate(propsData.dynamic.energy.throughput.time)
                  "
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
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "PerformanceComponent",
  components: {
    Field,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      propsData: this.$props.data.aspect.performance,
    };
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
    callFormattedDate(timestamp) {
      return passportUtil.formattedDate(timestamp);
    },
  },
};
</script>
