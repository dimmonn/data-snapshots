<template>
  <div id="app">
    <div id="nav">
      <router-link v-if="!authenticated" to="/register">Register</router-link>
      <router-link v-if="authenticated" to="/secure">Secure</router-link>
      <router-link v-if="authenticated" to="/login" v-on:click.native="logout()" replace>Logout</router-link>
      <router-link v-else to="/login">Login</router-link>
    </div>
    <router-view @authenticated="setAuthenticated"/>
  </div>
</template>

<script>
  export default {
    name: 'App',
    data() {
      return {
        authenticated: false
      }
    },
    methods: {
      setAuthenticated(status) {
        this.authenticated = status;
      },
      logout() {
        this.authenticated = false;
      }
    }
  }
</script>

<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }

  #nav {
    padding: 30px;
  }

  #nav a {
    font-weight: bold;
    color: #2c3e50;
    padding-right: 10px;
  }

  #nav a:not(:first-child) {
    border-left: 1px solid #2c3e50;
    padding-left: 10px;
  }

</style>
