<template>
  <div id="login">
    <h1>Login</h1>
    <div class="form-inputs">
      <label for="username">Username</label>
      <input type="text" id="username" name="username" v-model="input.username"
             placeholder="Username"/>
    </div>
    <div class="form-inputs">
      <label for="password">Password</label>
      <input type="password" id="password" name="password" v-model="input.password"
             placeholder="Password"/>
    </div>
    <div>
      <button type="button" v-on:click="login()">Login</button>
    </div>
    <p class="ref">
      <router-link to="/register">Not Registered?</router-link>
    </p>
  </div>
</template>

<script>
  export default {
    name: 'Login',

    data() {
      return {
        message: '',
        input: {
          username: "",
          password: ""
        }
      }
    },
    methods: {

      login() {
        window.rr = this;

        if (this.input.username != "" && this.input.password != "") {
          this.axios.post('http://auth:8080/login',

              {
                username: this.input.username,
                password: this.input.password
              }, {
                headers: {
                  'Access-Control-Allow-Origin': '*'
                }
              })
          .then(response => {
            console.log(response);
            window.tt = response;
            localStorage.setItem('jwt', response.headers.authorization);

            if (localStorage.getItem('jwt') != null) {
              this.$emit("authenticated", true);
              this.$router.replace({name: "Secure"});
              window.rr.message = response.status;
            }
          })
          .catch((error) => {
            this.$toast.error("user or password is wrong", {
              timeout: 4000
            });
          });

        } else {
          console.log("Username and or password must not be empty");
          this.$toast.error("Username and or password must not be empty", {
            timeout: 4000
          });
        }
      }
    }
  }
</script>

<style>

  #login .form-inputs {
    padding-bottom: 10px;
  }

  #login .form-inputs label {
    padding-right: 10px;
  }

</style>