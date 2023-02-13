<!--  -->
<template>
  <div>
    <!-- <el-row :gutter="20">
      <el-col :span="4">
        <el-collapse v-model="activeName" accordion @change="handleChange">
          <el-collapse-item title="管理员" name="1"></el-collapse-item>
          <el-collapse-item title="内容管理者" name="2"></el-collapse-item>
          <el-collapse-item title="访客" name="3"></el-collapse-item>
        </el-collapse>
      </el-col>
      <el-col :span="16"> -->
      <!--   <div>
          <el-tree
            :data="data"
            show-checkbox
            ref="tree"
            node-key="id"
            :props="defaultProps"
            :default-checked-keys="roleMenuId"
            :default-expand-all="true"
            @check="changeRole"
          ></el-tree>
        </div>
      </el-col>
    </el-row> -->
  </div>
</template>

<script>
import { get, post } from "@/utils/http";
export default {
  data() {
    return {
      data: [],
      roleMenuId: [],
      roleMenu: [],
      parentIdList: [],
      activeName: "1",
      defaultProps: {
        children: "child",
        label: "name_zh"
      }
    };
  },
  mounted: function() {
    get("/menu/all").then(res => {
        this.data = res.noParent;
        this.initRoleMenuId(this.activeName);
        this.parentIdList = res.parent;
    });
  },
  methods: {
    handleChange(activeNames) {
      this.initRoleMenuId(activeNames);
    },
    initRoleMenuId(id) {
      var _this = this;
      //根据角色id获取当前角色菜单
      get("/menu/" + id).then(res => {
        _this.roleMenu = res;
         _this.roleMenuId = res.filter(function(val) {
          return _this.parentIdList.indexOf(val) === -1;
        });
        this.$refs.tree.setCheckedKeys(_this.roleMenuId)
      });
    }
  }
};
</script>
<style>
</style>