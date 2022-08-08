<template>
  <div className="section-header">
    <span className="section-title">{{ title }}</span>
  </div>
</template>

<script>
export default {
  name: "SectionHeader",
  props: {
    title: String,
  },
  components: {},
};
</script>

<style scoped>
.section-header {
  display: flex;
  border: solid 1px #b3cb2d;
  border-radius: 4px 4px 0 0;
  background-color: #fff;
}
.section-title {
  font-size: 28px;
  color: #b3cb2d;
  margin: 30px 30px 30px 40px;
  font-weight: bold;
}
</style>
