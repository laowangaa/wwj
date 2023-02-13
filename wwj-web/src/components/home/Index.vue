<!--  -->
<template>
  <div>
    <el-container>
      <el-header >
        <Head></Head>
      </el-header>
      <el-container ref="homePage">
        <el-aside :width="sideWidth">
          <side></side>
        </el-aside>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Side from "@/components/home/Side";
import Head from "@/components/home/Head";
export default {
  data() {
    return {
      clientHeight: "",
      sideWidth: "200px"
    };
  },
  mounted: function() {
    // 获取浏览器可视区域高度
    this.clientHeight = `${document.documentElement.clientHeight}`;
    this.sideWidth = document.body.clientWidth * 0.15 + "px";
    //document.body.clientWidth;
    //console.log(self.clientHeight);
    window.onresize = function temp() {
      this.clientHeight = `${document.documentElement.clientHeight}`;
    };
  },
  watch: {
    // 如果 `clientHeight` 发生改变，这个函数就会运行
    clientHeight: function() {
      this.changeFixed(this.clientHeight);
    }
  },
  methods: {
    changeFixed(clientHeight) {
      //动态修改样式
      // console.log(clientHeight);
      // console.log(this.$refs.homePage.$el.style.height);
      this.$refs.homePage.$el.style.height = clientHeight - 80 + "px";
    }
  },
  components: {
    Head,
    Side
  }
};
</script>
<style>
</style>