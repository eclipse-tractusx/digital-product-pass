<template>
  <v-container class="cards-container">
    <v-row>
      <v-col
        cols="12"
        md="3"
        v-for="(card, index) in cards"
        :key="index"
        style="padding: 6px !important"
      >
        <div class="card-container">
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

          <v-container class="pa-0" v-if="card.title === 'SUSTAINABILITY'">
            <v-row class="pa-0 ma-0">
              <ElementChart
                :data="card.value"
                style="margin: 24px 4px 2px 12px; padding: 0"
              />
              <v-divider vertical></v-divider>
              <v-col cols="4" class="ma-0 pa-0; co2-container">
                <span class="card-value" style="padding-bottom: 0">
                  {{ card.secondValue }} {{ card.secondValueUnits }}
                </span>
                <div class="card-label" style="padding-top: 0">
                  {{ card.secondLabel }}
                </div>
              </v-col>
            </v-row>
          </v-container>
          <div v-else>
            <div class="card-label">
              {{ card.label }}
            </div>
            <div class="card-value">{{ card.value }} {{ card.valueUnits }}</div>
            <v-divider></v-divider>
            <div v-if="card.title === 'HEALTH'" style="margin-bottom: 60px">
              <div class="charging-cycles-title">Charging Cycles</div>
              <BarChart :currentValue="currentValue" :maxValue="maxValue" />
            </div>
            <div v-else>
              <div class="card-second-value">
                {{ card.secondValue }} {{ card.secondValueUnits }}
              </div>
              <div class="card-second-label">
                {{ card.secondLabel }}
              </div>
            </div>
          </div>
          <span v-if="card.info" class="card-info-icon">
            <v-icon start md icon="mdi-information-outline"> </v-icon>
            <Tooltip>
              {{ card.info }}
            </Tooltip>
          </span>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Tooltip from "../general/Tooltip.vue";
import ElementChart from "../passport/ElementChart.vue";
import BarChart from "../passport/BarChart.vue";
export default {
  name: "CardsComponent",
  components: {
    Tooltip,
    ElementChart,
    BarChart,
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
          title: "SUSTAINABILITY",
          icon: "mdi-molecule-co2",
          secondLabel: "CO Total",
          secondValueUnits: "t",
          value:
            this.$props.data.data.passport.cellChemistry.cathodeActiveMaterials,
          secondValue: this.$props.data.data.passport.cO2FootprintTotal,
          info: "info",
        },
      ],
    };
  },
};
</script>
