<template>
  <div v-if="attributesList" class="sub-section-container">
    <div class="sub-title-container">
      <span class="sub-title">{{ label }}</span>
    </div>
    <div class="list-container">
      <ul>
        <span class="list-label"></span>
        <li v-for="attribute in attributesList" :key="attribute">
          <span>
            {{ attribute.materialName }}
          </span>
          <span v-if="attribute.matierialPercentageMassFraction">
            - {{ attribute.matierialPercentageMassFraction }}%
          </span>
          <span v-if="attribute.matierialWeight">
            - {{ attribute.matierialWeight }}kg
          </span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "AttributeField",
  props: {
    attributesList: { type: [String, Number], default: "" },
    label: { type: [String, Number], default: "" },
  },
};
</script>

<style scoped>
ul {
  display: flex;
  flex-direction: column;
  padding: 0;
}

li {
  margin-left: 20px;
  font-weight: bold;
}

.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
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

@media (max-width: 750px) {
  .list-container {
    width: 100%;
    padding-left: 50px;
  }

  .sub-title-container {
    padding: 22px 40px 0 30px;
  }
}
</style>
