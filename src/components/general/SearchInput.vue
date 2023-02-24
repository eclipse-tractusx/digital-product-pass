<template>
  <v-form class="form" @submit.prevent>
    <div class="input-form">
      <input
        v-model="typedCode"
        class="input"
        type="text"
        placeholder="Type ID"
      />
    </div>
    <v-btn
      rounded="pill"
      color="#0F71CB"
      size="small"
      class="submit-btn"
      @click="onClick"
      type="submit"
    >
      Search
      <v-icon class="icon" start md icon="mdi-arrow-right"></v-icon>
    </v-btn>
  </v-form>
</template>

<script>
export default {
  data() {
    return {
      typedCode: "",
    };
  },
  methods: {
    onClick() {
      this.$router.push({
        path: `/${this.typedCode}`,
      });
    },
  },
};
</script>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.input-form {
  padding: 17px;
  background: linear-gradient(to right, #f8b500, #f88000);
  border-radius: 35px;
}
.input {
  width: 560px;
  border-radius: 20px;
  color: #444;
  padding: 18px;
  padding-left: 60px;
  font-size: 20px;
  outline: none;
  background-image: url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCI+PGRlZnM+PHN0eWxlPi5jbHMtMXtmaWxsOiNhMGEwYTA7fS5jbHMtMntmaWxsOiNhMGEwYTA7fTwvc3R5bGU+PC9kZWZzPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTE1LjUgMTRoLS43OWwtLjI4LS4yN0MxNS40MSAxMi41OSAxNiAxMS4xMSAxNiA5LjUgMTYgNS45MSAxMy4wOSAzIDkuNSAzUzMgNS45MSAzIDkuNSA1LjkxIDE2IDkuNSAxNmMxLjYxIDAgMy4wOS0uNTkgNC4yMy0xLjU3bC4yNy4yOHYuNzlsNSA0Ljk5TDIwLjQ5IDE5bC00Ljk5LTV6bS02IDBDNy4wMSAxNCA1IDExLjk5IDUgOS41UzcuMDEgNSA5LjUgNSAxNCA3LjAxIDE0IDkuNSAxMS45OSAxNCA5LjUgMTR6Ii8+PC9zdmc+Cg==);
  background-repeat: no-repeat;
  background-position: 16px center;
  background-size: 32px 32px;
  border: 1px solid lightgray;
  border-radius: 25px;
  background-color: white;
}

.submit-btn {
  margin-top: 30px;
  height: 56px;
  width: 185px;
  font-size: 16px;
  font-weight: 500;
  color: #fff;
  background: none;
  border: none;
  cursor: pointer;
}

.icon {
  padding-left: 20px;
}

@media (max-width: 856px) {
  .input {
    position: relative;
    width: 50vw;
  }
}

@media (max-width: 375px) {
  .input {
    position: relative;
    width: 80vw;
  }
}
</style>
