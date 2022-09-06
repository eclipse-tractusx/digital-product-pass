<template>
  <div className="section-header" @click="toggle = !toggle">
    <span className="section-title">{{  title  }}</span>
    <span className="toggle-container">
      <img :src="Toggle" alt="toggle" class="toggle-btn" :class="[toggle ? 'rotate' : '']" />
    </span>
  </div>
</template>

<script>
import Toggle from '../assets/toggle.svg';

export default {
  name: 'SectionHeader',
  components: {
    Toggle
  },
  props: {
    title: String,
  },
  setup() {
    return {
      Toggle,
    };
  },
  data() {
    return {
      toggle: false,
    };
  }
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
  width: 90%
}

.toggle-container {
  display: flex;
}

.toggle-btn {
  width: 70px;
  cursor: pointer;
}

.rotate {
  transform: rotate(180deg)
}

@media (max-width: 750px) {
  .section-header {
    border-radius: 0;
  }

  .section-title {
    font-size: 20px;
    margin: 22px 0px 22px 30px;
  }

  .section-header {
    border: none;
    border-top: solid 1px #b3cb2d;
    border-bottom: solid 1px #b3cb2d;
  }
}
</style>
