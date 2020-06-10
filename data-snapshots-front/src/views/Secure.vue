<template>
  <div class="container">
    <h1>Secured area</h1>
    <div class="base-demo" style="width: 900px">
      <vue-table-dynamic :params="params"></vue-table-dynamic>
      <br/>
    </div>

    <div class="large-12 medium-12 small-12 cell">
      <label>File
        <input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/>
      </label>
      <button :disabled="!upload" v-on:click="submitFile()">Submit</button>
    </div>

    <div class="removable">
      <b-input-group>
        <b-form-input v-model="number" min="0.00"></b-form-input>
        <b-input-group-prepend>
          <b-button variant="outline-info" @click="removeItem">Remove By ID</b-button>
        </b-input-group-prepend>
      </b-input-group>
    </div>
  </div>
</template>

<script>
  import VueTableDynamic from 'vue-table-dynamic';
  import App from "../App";

  export default {
    created() {
      App.data().authenticated=true;
      console.log('Secure component has been created!');
      this.mounted();
    },
    components: {
      VueTableDynamic
    },
    methods: {
      mounted() {
        this.getMeta();
      },
      getMeta: function () {
        this.axios.get("http://localhost:8083/files/entries", {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: 'Bearer ' + localStorage.getItem('jwt'),
            'Access-Control-Allow-Origin': '*'
          }
        })
        .then(response => {
          let tmp = response.data;
          tmp.forEach(elem => {
                let item = [elem.id, elem.name, elem.description, elem.timestamp];
                this.params.data.push(item)
              }
          )
        })
        .catch(error => {
          if (typeof error.data !== 'undefined' && error.data.message.localeCompare(
              "Access Denied")) {
            this.$router.push('/login');
          } else {
            this.$toast.error("unexpected error happened");
          }

        });
      },
      handleFileUpload() {
        this.upload = true;
        this.file = this.$refs.file.files[0];
        if (!this.file || this.file.type !== 'text/plain') {
          this.$toast.error("file type is " + this.file.type + " , expected one is text/plain", {
            timeout: 4000
          });
          this.upload = false;
        }
      },
      removeItem() {
        this.axios.delete("http://localhost:8083/files/entries/" + this.number, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: 'Bearer ' + localStorage.getItem('jwt'),
            'Access-Control-Allow-Origin': '*'
          }
        })
        .then(response => {

          this.params.data = [
            ['Id', 'Name', 'Description', 'Timestamp']
          ];
          let tmp = response.data;
          tmp.forEach(elem => {
                let item = [elem.id, elem.name, elem.description, elem.timestamp];
                this.params.data.push(item)
              }
          )
        })
        .catch(error => {
          if (typeof error.response !== 'undefined' && String(
              error.response.data.message).valueOf() ===
              "Access Denied") {
            this.$router.push('/login');
          } else if (typeof typeof error.response !== 'undefined') {
            console.log("dd  " + error.response.data.message);
            this.$toast.error(error.response.data.message, {
              timeout: 4000
            });
          }
        });

      },
      submitFile() {
        let formData = new FormData();
        let reader = new FileReader();
        reader.readAsText(this.file, "UTF-8");
        reader.onload = evt => {
          this.text = evt.target.result;
        };
        reader.onerror = evt => {
          console.error(evt);
        };
        formData.append('file', this.file);
        this.axios.post('http://localhost:8083/files/upload-file',
            formData,
            {
              headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: 'Bearer ' + localStorage.getItem('jwt'),
                'Access-Control-Allow-Origin': '*'
              },

            }
        ).then(result => {
          console.log('SUCCESS!!');
          console.log(result.data);
          this.params.data = [
            ['Id', 'Name', 'Description', 'Timestamp']
          ];
          let tmp = result.data;
          tmp.forEach(elem => {
                let item = [elem.id, elem.name, elem.description, elem.timestamp];
                this.params.data.push(item)
              }
          );
        })
        .catch(error => {
          console.log('FAILURE!!');
          console.log(error);
          this.file = '';
        });
      }
    },
    data() {
      return {
        number: 0,
        params: {
          data: [
            ['Id', 'Name', 'Description', 'Timestamp']
          ]
        },
        file: '',
        upload: false,
        text: ""
      }
    },

  }
</script>
<style>
  .base-demo {
    width: 70%;
    margin: 0 auto;
  }

  .removable {
    width: 20%;
    margin: 0 auto;
  }
</style>