import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import VueAxios from 'vue-axios';
import Toast from "vue-toastification";
import "vue-toastification/dist/index.css";
import {BCard} from "bootstrap-vue";
import { BFormInput } from 'bootstrap-vue'
import { ButtonPlugin } from 'bootstrap-vue'
import { BInputGroup } from 'bootstrap-vue'
import { BInputGroupPrepend } from 'bootstrap-vue'

import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";

Vue.use(ButtonPlugin);
Vue.component('b-form-input', BFormInput);
Vue.component('b-input-group', BInputGroup);
Vue.component('b-input-group-prepend', BInputGroupPrepend);

Vue.component('b-card', BCard);
Vue.component('b-card-text', BCard);
Vue.component('b-card-group', BCard);
Vue.component('b-link', BCard);
Vue.use(Toast);

Vue.use(VueAxios, axios);
new Vue({
  router,
  render: function (h) {
    return h(App)
  }
}).$mount('#app');