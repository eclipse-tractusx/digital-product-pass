<template>
  <div className="section-content">{{ children }}</div>
</template>

<script>
export default {
  name: 'SectionContent',
  components: {},
  props: {
    children: String,
  },
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
