<template v-if="cellChemistry">
  <SectionHeader title="2. Cell chemistry" @click="toggle = !toggle" />
  <!-- Composition of Electrolyte -->
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Composition of Electrolyte</span>
      </div>
      <div v-if="cellChemistry.electrolyteComposition" class="list-container">
        <ul>
          <span class="list-label"></span>
          <li
            v-for="electrolytes in cellChemistry.electrolyteComposition"
            :key="electrolytes"
            data-cy="electrolyte-composition"
          >
            <span>
              {{ electrolytes.materialName }}
            </span>
            -
            <span> {{ electrolytes.matierialPercentageMassFraction }}% </span>
            -
            <span> {{ electrolytes.matierialWeight }}kg </span>
          </li>
        </ul>
      </div>
    </div>

    <!-- Composition of Anode -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Composition of Anode</span>
      </div>
      <div v-if="cellChemistry.anodeActiveMaterials" class="list-container">
        <ul>
          <span class="list-label"></span>
          <li
            v-for="electrolytes in cellChemistry.anodeActiveMaterials"
            :key="electrolytes"
          >
            <span>
              {{ electrolytes.materialName }}
            </span>
            -
            <span> {{ electrolytes.matierialPercentageMassFraction }}% </span>
            -
            <span> {{ electrolytes.matierialWeight }}kg </span>
          </li>
        </ul>
      </div>
    </div>
    <!-- Composition of Anode other -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Composition of other Anode materials</span>
      </div>
      <div v-if="cellChemistry.anodeCompositionOther" class="list-container">
        <ul>
          <span class="list-label"></span>
          <li
            v-for="electrolytes in cellChemistry.anodeCompositionOther"
            :key="electrolytes"
          >
            <span>
              {{ electrolytes.materialName }}
            </span>
            -
            <span> {{ electrolytes.matierialPercentageMassFraction }}% </span>
            -
            <span> {{ electrolytes.matierialWeight }}kg </span>
          </li>
        </ul>
      </div>
    </div>
    <!-- Composition of Cathode -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Composition of Cathode</span>
      </div>
      <div v-if="cellChemistry.cathodeActiveMaterials" class="list-container">
        <ul>
          <span class="list-label"></span>
          <li
            v-for="electrolytes in cellChemistry.cathodeActiveMaterials"
            :key="electrolytes"
          >
            <span>
              {{ electrolytes.materialName }}
            </span>
            -
            <span> {{ electrolytes.matierialPercentageMassFraction }}% </span>
            -
            <span> {{ electrolytes.matierialWeight }}kg </span>
          </li>
        </ul>
      </div>
    </div>
    <!-- Composition of Cathode other -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Composition of other Cathode materials</span>
      </div>
      <div v-if="cellChemistry.cathodeCompositionOther" class="list-container">
        <ul>
          <span class="list-label"></span>
          <li
            v-for="electrolytes in cellChemistry.cathodeCompositionOther"
            :key="electrolytes"
          >
            <span>
              {{ electrolytes.materialName }}
            </span>
            -
            <span> {{ electrolytes.matierialPercentageMassFraction }}% </span>
            -
            <span> {{ electrolytes.matierialWeight }}kg </span>
          </li>
        </ul>
      </div>
    </div>
    <!-- Recyclate content active materials -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Recyclate content active materials</span>
      </div>
      <div
        v-if="cellChemistry.recyclateContentActiveMaterials"
        class="list-container"
      >
        <ul>
          <span class="list-label"></span>
          <li
            v-for="electrolytes in cellChemistry.recyclateContentActiveMaterials"
            :key="electrolytes"
          >
            <span>
              {{ electrolytes.materialName }}
            </span>
            -
            <span> {{ electrolytes.matierialPercentageMassFraction }}% </span>
            -
            <span> {{ electrolytes.matierialWeight }}kg </span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";

export default {
  name: "CellChemistry",
  components: {
    SectionHeader,
  },
  props: {
    sectionTitle: {
      type: String,
      default: "",
    },
    cellChemistry: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
    };
  },
};
</script>

<style scoped>
.section-content {
  width: 100%;
  border: solid 1px #b3cb2d;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
  margin-bottom: 50px;
}

.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}

.field-container {
  display: flex;
  flex-direction: column;
  width: 33%;
}

.sub-title {
  font-weight: bold;
  font-size: 20px;
  color: #c6cca3;
}

.sub-title-container {
  padding: 22px 40px 0 40px;
  width: 100%;
}

.list-container {
  width: 33%;
  padding: 0 0 22px 40px;
}

.list-label {
  padding: 22px 0 10px 0;
  font-size: 12px;
  color: #777777;
}

ul {
  display: flex;
  flex-direction: column;
  padding: 0;
}

li {
  margin-left: 20px;
  font-weight: bold;
}

.hidden {
  display: none;
}

@media (max-width: 750px) {
  .section-content {
    border: none;
  }

  .section-content {
    margin-bottom: 0;
  }

  .field-container {
    width: 100%;
  }

  .list-container {
    width: 100%;
    padding-left: 50px;
  }

  .section-content {
    border: none;
    margin-bottom: 0;
  }

  .sub-title-container {
    padding: 22px 40px 0 30px;
  }
}
</style>
