<!--  -->
<template>
  <div>
    <div class="home_title">山东省土地利用动态监测系统</div>
    <div class="home_userinfoContainer">
      <el-dropdown @command="handleCommand" >
        <span class="el-dropdown-link home_userinfo">
          {{currentUserName}}
          <i class="el-icon-arrow-down el-icon--right home_userinfo"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="goIndex">返回主页</el-dropdown-item>
          <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import {post} from '@/utils/http'
export default {
  data() {
    return {
      currentUserName:''
    };
  },
  mounted:function getUser() {
    this.currentUserName = this.$store.state.username
  },
  methods: {
      handleCommand(command) {
        var _this = this;
        if (command == 'logout') {
          this.$confirm('注销登录吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(function () {
            post("/user/logout")
            _this.$store.commit('logout')
            _this.$router.replace({name:'login'});
            _this.$message.success('退出成功')
          }, function () {
            //取消
          })
        }else if(command == 'goIndex'){
          this.$router.push({name:'index'})
        }
      }
    }
};
</script>
<style>
  .el-header {
    background-color: #20a0ff;
    color: #333;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .home_title {
    color: #fff;
    font-size: 22px;
    display: inline;
  }

  .home_userinfo {
    color: #fff;
    cursor: pointer;
  }

  .home_userinfoContainer {
    text-align:right;
    right: 0px;
   
    display: inline;
    margin-left: 1080px;
  }
</style>