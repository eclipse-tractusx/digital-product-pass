<template>
  <div className="section-content">{{ children }}</div>
</template>

<script>
export default {
  name: "SectionContent",
  props: {
    children: String,
  },
  components: {},
};
</script>

<style scoped>
.section-content {
  width: 100%;
  border: solid 1px #b3cb2d;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
  height: 400px;
}
</style>
