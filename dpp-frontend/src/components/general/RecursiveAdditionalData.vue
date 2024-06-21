<template>
  <div class="additional-data">
    <!-- eslint-disable-next-line vue/no-v-for-template-key -->
    <template v-for="attribute in jsonData.children" :key="attribute">
      <template v-if="attribute.type.dataType !== 'object'">
        <DialogComponent class="field-dialog">
          <Field
            :info="attribute.description ? attribute.description : null"
            :icon="callIconFinder(attribute.label)"
            :label="attribute.label"
            :value="processValue(attribute)"
            :unit="attribute.type.typeUnit ? attribute.type.typeUnit : ''"
          />
          <template v-slot:text>
            {{ attribute.description }}
          </template>
        </DialogComponent>
      </template>
      <template v-else>
        <DialogComponent class="field-dialog">
          <div class="column">
            <div class="container-label">{{ attribute.label }}</div>
          </div>
          <template v-slot:text>
            {{ attribute.description }}
          </template>
        </DialogComponent>
        <template v-if="attribute.children">
          <recursive-additional-data :jsonData="attribute" />
        </template>
      </template>
    </template>
  </div>
</template>

<script>
import passportUtil from "@/utils/passportUtil.js";
import Field from "../passport/Field.vue";
import DialogComponent from "../general/Dialog.vue";
export default {
  name: "RecursiveAdditionalData",
  components: {
    Field,
    DialogComponent,
  },
  props: {
    jsonData: {
      type: [Object, Array],
    },
  },

  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
    processValue(attribute) {
      if (attribute.type.dataType == "array" && Array.isArray(attribute.data)) {
        return attribute.data.join(", ");
      } else {
        return attribute.data;
      }
    },
  },
};
</script>

