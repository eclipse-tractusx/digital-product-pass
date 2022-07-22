<template>
  <SectionHeader title="2. Battery composition"> </SectionHeader>
  <!-- Composition of Electrolyte -->
  <div class="section-content">
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Composition of Electrolyte</span>
      </div>
      <div class="list-container">
        <ul>
          <span class="list-label"></span>
          <li v-for="electrolytes in batteryComposition.electrolyteComposition">
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
      <div class="field-container">
        <span class="field-label">Natural Graphite content</span>
        <span class="field-value"
          >{{ batteryComposition.anodeContent.naturalGraphiteContent.value
          }}{{
            batteryComposition.anodeContent.naturalGraphiteContent.unit
          }}</span
        >
      </div>
      <div class="list-container">
        <ul>
          <span class="list-label">Recyclate Content Ni</span>
          <li
            v-for="electrolytes in batteryComposition.anodeContent
              .anodeComposition"
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
      <div class="field-container">
        <span class="field-label">Li content</span>
        <span class="field-value"
          >{{ batteryComposition.cathodeComposition.liContent.value
          }}{{ batteryComposition.cathodeComposition.liContent.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Ni content</span>
        <span class="field-value"
          >{{ batteryComposition.cathodeComposition.niContent.value
          }}{{ batteryComposition.cathodeComposition.niContent.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Co content</span>
        <span class="field-value"
          >{{ batteryComposition.cathodeComposition.coContent.value
          }}{{ batteryComposition.cathodeComposition.coContent.unit }}</span
        >
      </div>
      <div class="list-container">
        <ul>
          <span class="list-label">Recyclate Content Ni</span>
          <li
            v-for="electrolytes in batteryComposition.cathodeComposition
              .otherCathodeComposition"
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
          <li v-for="electrolytes in batteryComposition.crm">
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
      <div class="field-container">
        <span class="field-label">Recyclate Content Ni</span>
        <span class="field-value"
          >{{ batteryComposition.niRecyclateContent.value
          }}{{ batteryComposition.niRecyclateContent.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Recyclate Content Li</span>
        <span class="field-value"
          >{{ batteryComposition.liRecyclateContent.value
          }}{{ batteryComposition.liRecyclateContent.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Recyclate Content Co</span>
        <span class="field-value"
          >{{ batteryComposition.coRecyclateContent.value
          }}{{ batteryComposition.coRecyclateContent.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Recyclate Content Pb</span>
        <span class="field-value"
          >{{ batteryComposition.pbRecyclateContent.value
          }}{{ batteryComposition.pbRecyclateContent.unit }}</span
        >
      </div>
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import SectionContent from "./SectionContent.vue";

export default {
  name: "BatteryComposition",
  props: {
    sectionTitle: String,
    batteryComposition: {},
  },
  components: {
    SectionHeader,
    SectionContent,
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
.field-label {
  padding: 30px 0px 10px 40px;
  font-size: 12px;
  color: #777777;
}

.field-value {
  padding-left: 40px;
  font-size: 14px;
  line-height: 20px;
  font-weight: bold;
}
.longer {
  padding-bottom: 50px;
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
  padding: 30px 0px 10px 0px;
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
