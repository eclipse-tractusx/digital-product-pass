<template>
  <v-container>
    <v-row>
      <v-col v-for="(card, index) in cards" :key="index" class="card-container">
        <span class="card-title">{{ card.title }} </span>
        <span>
          <v-icon
            :class="[card.title === 'HEALTH' ? 'card-icon-green' : '']"
            class="card-icon"
            start
            md
            :icon="card.icon"
            size="x-large"
          ></v-icon>
        </span>

        <v-container v-if="card.title === 'SUSTAINABILITY'">
          <v-row>
            <v-col class="materials-container">
              <v-row>
                <div
                  class="material-container"
                  v-for="(material, index) in card.value"
                  :key="index"
                >
                  <span class="material-label">{{
                    material.materialName
                  }}</span>
                  <span class="material-value">
                    {{ material.materialPercentageMassFraction }}%
                  </span>
                </div>
              </v-row>
            </v-col>

            <v-divider vertical></v-divider>

            <v-col class="co2-container">
              <span class="card-value">
                {{ card.secondValue }} {{ card.secondValueUnits }}
              </span>
              <div class="card-label">{{ card.secondLabel }}</div>
            </v-col>
          </v-row>
        </v-container>
        <div v-else>
          <div class="card-label">{{ card.label }}</div>
          <div class="card-value">{{ card.value }} {{ card.valueUnits }}</div>
          <v-divider></v-divider>
          <div v-if="card.title === 'HEALTH'" style="margin-bottom: 60px">
            <div class="charging-cycles-title">Charging Cycles</div>
            <div class="charging-cycles">
              <div class="bar-chart" />
              <div
                class="bar-chart"
                :style="`width: ${barChart(
                  currentValue,
                  maxValue
                )}; background: #0f71cb`"
              />
              <div class="chart-value current">
                {{ isNumeric(currentValue) ? currentValue : "No data" }}
              </div>
              <div class="chart-value max">
                {{ isNumeric(maxValue) ? maxValue : "No data" }}
              </div>
            </div>
          </div>
          <div v-else>
            <div class="card-second-value">
              {{ card.secondValue }} {{ card.secondValueUnits }}
            </div>
            <div class="card-second-label">{{ card.secondLabel }}</div>
          </div>
        </div>
        <span v-if="card.info" class="card-info-icon">
          <v-icon start md icon="mdi-information-outline"> </v-icon>
          <Tooltip>
            {{ card.info }}
          </Tooltip>
        </span>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Tooltip from "../general/Tooltip.vue";
export default {
  name: "CardsComponent",
  components: {
    Tooltip,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      currentValue:
        this.$props.data.data.passport.batteryCycleLife
          .cycleLifeTestDepthOfDischarge,
      maxValue:
        this.$props.data.data.passport.batteryCycleLife.expectedLifetime,
      cards: [
        {
          title: "GENERAL",
          label: "Type",
          secondLabel: "Model",
          icon: "mdi-pound",
          value:
            this.$props.data.data.passport.batteryIdentification.batteryType,
          secondValue:
            this.$props.data.data.passport.batteryIdentification.batteryModel,
        },
        {
          title: "PERFORMANCE",
          label: "Rated Capacity",
          secondLabel: "Original Power",
          icon: "mdi-image-size-select-small",
          value:
            this.$props.data.data.passport.electrochemicalProperties
              .ratedCapacity,
          valueUnits: "kW",
          secondValueUnits: "Ah",
          secondValue:
            this.$props.data.data.passport.electrochemicalProperties
              .batteryPower.originalPowerCapability,
          info: "info",
        },
        {
          title: "HEALTH",
          label: "State of Health (SoH)",
          secondLabel: "Charging Cycles",
          icon: "mdi-battery-plus",
          value: this.$props.data.data.passport.stateOfBattery.stateOfHealth,
          valueUnits: "%",
          secondValue:
            this.$props.data.data.passport.batteryIdentification.batteryModel,
          info: "info",
        },
        {
          /////////////////tut/////
          title: "SUSTAINABILITY",
          icon: "mdi-molecule-co2",
          secondLabel: "CO2 Total",
          secondValueUnits: "t",
          value:
            this.$props.data.data.passport.cellChemistry.cathodeActiveMaterials,
          secondValue: this.$props.data.data.passport.cO2FootprintTotal,
          info: "info",
        },
      ],
    };
  },
  methods: {
    barChart(currentValue, maxValue) {
      if (!currentValue || !maxValue) return 0;
      if (this.isNumeric(!currentValue) || this.isNumeric(!maxValue)) return 0;

      const bar = (currentValue * 100) / maxValue;

      return bar + "%";
    },
    isNumeric(n) {
      return !isNaN(parseFloat(n)) && isFinite(n);
    },
  },
};
</script>
