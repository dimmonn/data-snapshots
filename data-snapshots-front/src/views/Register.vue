<template>
  <div class="Register">
    <!-- Material form register -->
    <form>
      <p class="h4 text-center mb-4">Sign up</p>
      <div class="grey-text">
        <mdb-input label="Your name" icon="user" type="text" v-model="input.username"/>
        <mdb-input label="Your password" icon="envelope" type="password" v-model="input.password"/>
        <mdb-input label="Confirm your password" icon="exclamation-triangle" type="password"
                   v-model="input.password_confirm"/>
      </div>
      <div class="text-center">
        <mdb-btn color="primary" v-on:click="register">Register</mdb-btn>
      </div>
    </form>
    <!-- Material form register -->
  </div>
</template>
<script>
  import {mdbInput, mdbBtn} from 'mdbvue';

  export default {
    name: 'Register',
    components: {
      mdbInput,
      mdbBtn
    },
    data() {
      return {
        message: '',
        input: {
          username: "",
          password: "",
          password_confirm: ""
        }
      }
    },
    methods: {
      register() {
        window.rr = this;

        if (this.input.password !== this.input.password_confirm) {
          this.$toast.error("passwords do not match", {
            timeout: 4000
          });

        } else if (this.input.username !== "" && this.input.password !== "") {
          this.axios.post('http://localhost:8083/v1/users/sign-up', {
            username: this.input.username,
            password: this.input.password
          })
          .then(response => {
            this.$router.push('/login');
          })
          .catch((error) => {
            this.$toast.error("user or password is already registered", {
              timeout: 4000
            });
          });
        } else {
          this.$toast.error("Username and or password must not be empty", {
            timeout: 4000
          });
        }
      }
    }

  }
</script>

<style>

  .Register {
    position: fixed;
    top: 20%;
    left: 50%;
    margin-top: -50px;
    margin-left: -100px;
  }

</style>