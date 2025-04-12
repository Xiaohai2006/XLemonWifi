<template>
  <div class="search-box">
    <input
      type="text"
      :placeholder="msg"
      class="search-input"
      v-model="localValue"
      @focus="isFocused = true"
      @blur="isFocused = false"
      @keyup.enter="$emit('enter', localValue)"
    />
  </div>
</template>

<script>
export default {
  name: 'SearchBox',
  props: {
    msg: String,
    modelValue: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      isFocused: false,
      localValue: this.modelValue,
    };
  },
  watch: {
    modelValue(newVal) {
      console.log('SearchBox watch: modelValue →', newVal);
      this.localValue = newVal;
    },
    localValue(newVal) {
      console.log('SearchBox watch: localValue →', newVal);
      this.$emit('update:modelValue', newVal);
    },
  },
  mounted() {
    console.log('SearchBox mounted, initial modelValue:', this.modelValue);
    this.localValue = this.modelValue;
  },
};
</script>

<style scoped>
.search-box {
  display: flex;
  justify-content: center;
  align-items: center;
}

.search-input {
  width: 200px;
  height: 40px;
  padding: 0 15px;
  border: 1px solid #ccc;
  border-radius: 20px;
  outline: none;
  transition: all 0.3s ease;
  background-color: #fff;
}

.search-input:focus {
  width: 250px;
  border-color: #007bff;
  box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
}
</style>
