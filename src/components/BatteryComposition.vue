<template>
  <SectionHeader title="2. Battery composition"></SectionHeader>
  <!-- Composition of Electrolyte -->
  <div class="section-content">
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Composition of Electrolyte</span>
      </div>
      <div class="list-container">
        <ul>
          <span class="list-label"></span>
          <li
              v-for="electrolytes in batteryComposition.electrolyteComposition"
              :key="electrolytes"
          >
            {{ electrolytes }}
          </li>
        </ul>
      </div>
    </div>
    <!-- Composition of Anode -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title"> Composition of Anode</span>
      </div>
      <Field
          label="Natural Graphite content"
          v-bind:unit="
          batteryComposition.anodeContent.naturalGraphiteContent.unit
        "
          v-bind:value="
          batteryComposition.anodeContent.naturalGraphiteContent.value
        "
      />
      <div class="list-container">
        <ul>
          <span class="list-label">Recyclate Content Ni</span>
          <li
              v-for="electrolytes in batteryComposition.anodeContent
              .anodeComposition"
              :key="electrolytes"
          >
            {{ electrolytes }}
          </li>
        </ul>
      </div>
    </div>
    <!-- Composition of Cathode -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title"> Composition of Cathode</span>
      </div>
      <Field
          label="Li content"
          v-bind:unit="batteryComposition.cathodeComposition.liContent.unit"
          v-bind:value="batteryComposition.cathodeComposition.liContent.value"
      />
      <Field
          label="Ni content"
          v-bind:unit="batteryComposition.cathodeComposition.niContent.unit"
          v-bind:value="batteryComposition.cathodeComposition.niContent.value"
      />
      <Field
          label="Co content"
          v-bind:unit="batteryComposition.cathodeComposition.coContent.unit"
          v-bind:value="batteryComposition.cathodeComposition.coContent.value"
      />
      <div class="list-container">
        <ul>
          <span class="list-label">Recyclate Content Ni</span>
          <li
              v-for="electrolytes in batteryComposition.cathodeComposition
              .otherCathodeComposition"
              :key="electrolytes"
          >
            {{ electrolytes }}
          </li>
        </ul>
      </div>
    </div>
    <!-- TODO: Part Numbers for Components TABLE
 -->
    <!-- Critical Raw Materials -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Critical Raw Materials</span>
      </div>
      <div class="list-container">
        <ul>
          <span class="list-label">List of CRM</span>
          <li
              v-for="electrolytes in batteryComposition.crm"
              :key="electrolytes"
          >
            {{ electrolytes }}
          </li>
        </ul>
      </div>
    </div>
    <!-- Recycled content in active materials/battery model
 -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title"
        >Recycled content in active materials/battery model</span
        >
      </div>
      <Field
          label="Recyclate Content Ni"
          v-bind:unit="batteryComposition.niRecyclateContent.unit"
          v-bind:value="batteryComposition.niRecyclateContent.value"
      />
      <Field
          label="Recyclate Content Li"
          v-bind:unit="batteryComposition.liRecyclateContent.unit"
          v-bind:value="batteryComposition.liRecyclateContent.value"
      />
      <Field
          label="Recyclate Content Co"
          v-bind:unit="batteryComposition.coRecyclateContent.unit"
          v-bind:value="batteryComposition.coRecyclateContent.value"
      />
      <Field
          label="Recyclate Content Pb"
          v-bind:unit="batteryComposition.pbRecyclateContent.unit"
          v-bind:value="batteryComposition.pbRecyclateContent.value"
      />
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import Field from "./Field.vue";

export default {
  name: "BatteryComposition",
  props: {
    sectionTitle: String,
    batteryComposition: {},
  },
  components: {
    Field,
    SectionHeader
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
  min-height: 120px;
}

.sub-title {
  font-weight: bold;
  font-size: 20px;
  color: #c6cca3;
}

.sub-title-container {
  padding: 40px 40px 20px 40px;
  width: 100%;
}

.list-container {
  width: 33%;
  padding-left: 40px;
  padding-bottom: 40px;
}

.list-label {
  padding: 30px 0 10px 0;
  font-size: 12px;
  color: #777777;
}

ul {
  display: flex;
  flex-direction: column;
}

li {
  margin-left: 20px;
  font-weight: bold;
}
</style>
