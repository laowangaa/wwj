<template>
  <div>
    <div>
      <span style="float: left; margin-top: 7.5px">行政区：</span>
      <!-- <xzqh v-model="criteria.xzq"></xzqh> -->
      <tree-select
        style="width: 180px; float: left"
        :options="options"
        placeholder="请选择分类..."
        v-model="criteria.xzq"
      />
      <span>年份：</span>
      <el-select
        v-model="criteria.nf"
        multiple
        collapse-tags
        style="margin-left: 20px"
        placeholder="请选择"
        @change="change()"
      >
        <el-option
          v-for="item in date"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
    <el-button type="primary" @click="resetForm">重置</el-button>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column
          prop="date"
          align="center"
          label="山东省2019年各省辖市住宅用地供应情况表"
        >
          <el-table-column prop="date" align="right" label="单位：公顷">
            <el-table-column
              prop="name"
              align="center"
              
              label="住宅用地供应情况"
            >
            </el-table-column>

            <el-table-column prop="mj" label="省份" align="center">
            </el-table-column>
            <el-table-column prop="mj2" align="center" label="市区">
            </el-table-column>
            <el-table-column prop="tb" label="同比" align="center">
            </el-table-column>
            <el-table-column prop="zb" label="占比" align="center">
            </el-table-column>
            <el-table-column prop="tbzz" label="环比" align="center">
            </el-table-column>
          </el-table-column>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>


<script type="text/javascript">

export default {
  data() {
    return {
      date: [],
      tableData: [],
      options: [],
      //多选数组
      multipleSelection: [],

      //请求的URL
      url: "/tjfx/ndrw",

      //搜索条件
      criteria:{
        xzq: 370000,
        nf: ""
      },

      //下拉菜单选项
      select: "",

      //默认每页数据量
      pagesize: 10,

      //默认高亮行数据id
      highlightId: -1,

      //当前页码
      currentPage: 1,

      //查询的页码
      start: 1,

      //默认数据总数
      totalCount: 1000
    };
  },
  mounted() {
    this.queryCategoryList();
  },
 
  created() {
    var nowDate = new Date();
    var year = nowDate.getFullYear();
    var yearSelect = [];
    for (var i = 0; i <= 5; i++) {
      var y = year - i;
      yearSelect.push({ label: y, value: y });
    }
    this.date = yearSelect;
    this.loadData();
  },
  methods: {
 //方法区
            formatDate(row, column) {
                // 获取单元格数据
                let data = row[column.property]
                if(data == null) {
                    return null
                }
                let dt = new Date(data)
                 return dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate()
            },

    queryCategoryList() {
      this.$get("/sysCanton/getXZQ", null)
        .then(res => {
          if (res.status == 200) {
            let result = [];
            const children = this.getTree(res.data.data.array.child);
            let obj = {};
            (obj.label = "山东省"),
              (obj.id = 370000),
              (obj.children = children);
            result.push(obj);
            this.options = result;
          } else {
            this.$Notice.error({
              title: "错误",
              desc: res.msg
            });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    getTree(tree) {
      let arr = [];
      if (tree != null) {
        tree.forEach(item => {
          let obj = {};
          obj.label = item.name;
          obj.id = item.no;
          if (item.child != null) {
            obj.children = this.getTree(item.child);
          }
          arr.push(obj);
        });
      }
      return arr;
    },
    change() {
      this.$forceUpdate(); 
    }, //从服务器读取数据
    resetForm(){
      this.criteria.xzq=370000;
      this.criteria.nf='';
      
    },
    loadData: function() {
      var data = {
        xzq:this.criteria.xzq,
        nf:"2017"
      }
      console.log("in");
      this.$post(this.url,data)
      
        .then(res =>{
          console.log(res);
          //this.tableData = res.data.data.data.pageBpxmData;
          //this.totalCount = res.data.data.data.number;
        })
        .catch(error => {
          /*  this.$Notice.error({
            title: '错误',
            desc: '网络连接错误'
          }) */
          console.log(error);
        });
    }
  }
};
</script>

<style >

</style>
