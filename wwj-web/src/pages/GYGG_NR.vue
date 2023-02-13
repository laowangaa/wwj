<template>
  <div id="app" v-html="html">
     {{gygg_guid}}
  </div>
</template>

<script type="text/javascript">

export default {
  name: "app",
  data() {
    return {
      html:""
    }
  },
  props:{
    gygg_guid:String
  },
  methods:{
    loadGYGGNR:function (guid){
       this.$get("/tBpxm/getGGNRbyGUID", {
        guid: guid
      })
        .then((res) =>{
            if(res.data.data.data===""||res.data.data.data===null){
            this.html="<div align='center'><h1>暂无公告</h1></div>";
          }else{ 
          this.html=res.data.data.data;
        }console.log(res);
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
  mounted(){
    console.log(this.gygg_guid);
  },watch:{
      gygg_guid: {
      　　　　handler(newValue, oldValue) {
                  this.loadGYGGNR(newValue);
      　　　　},
      　　　　deep: true
      }
  }
}
</script>

<style scoped>
</style>
