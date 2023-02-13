<template>
  <div>
   <!--  <span style="float:left;margin-top:7.5px">行政区：</span>
    <xzqh v-model="criteria.xzq"></xzqh> 
    <tree-select
      style="width:180px;float:left;"
      :options="options"
      placeholder="请选择分类..."
      v-model="criteria.xzq"
    />
   <el-select v-model="value1" multiple placeholder="请选择">
    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
    </el-select>
    <span>年份：</span>
    <el-select
      v-model="criteria.nf"
      multiple
      collapse-tags
      style="margin-left: 20px;"
      placeholder="请选择"
      @change="change()"
    >
      <el-option v-for="item in date" :key="item.value" :label="item.label" :value="item.value"></el-option>
    </el-select>
    <span>项目名称：</span>
    <el-input v-model="criteria.xmmc" placeholder="请输入项目名称" style="width:155px"></el-input>
    <el-button type="primary" @click="loadData">查询</el-button>
    <el-button type="primary" @click="resetForm">重置</el-button> -->
    <div>
      <el-dialog title="出让公告" :visible.sync="dialogTableVisible">
        <GYGGNR :gygg_guid="GYGG_GUID"></GYGGNR>
      </el-dialog>
      <el-table
        ref="testTable"
        :data="tableData"
        style="width:100%"
        border
        :default-sort="{prop: 'id', order: 'ascending'}"
        @selection-change="handleSelectionChange"
        @row-click="handleclick"
        :row-class-name="tableRowClassName"
      > 
        <el-table-column prop="ZD_BH" label="宗地编号" width="350"></el-table-column>
        <el-table-column prop="MJ" label="面积" width="350" ></el-table-column>
        <el-table-column prop="TD_YT" label="土地用途" width="350"></el-table-column>
   
        <el-table-column label="操作" width="150"> 
          <template slot-scope="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.$index, scope.row)">查看出让公告</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div align="center">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pagesize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalCount"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import GYGGNR from "../../../pages/GYGG_NR"
export default {
  data() {
    return {
      date: [],
      tableData: [],
      options: [],
      //多选数组
      multipleSelection: [],
      dialogTableVisible: false,
      //请求的URL
      url: "/tBpxm/getGYGGZDData",
      GYGG_GUID:'',

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
  components:{
    GYGGNR
  },
  mounted() {
   // this.queryCategoryList();
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

   

  
    change() {
      this.$forceUpdate();
    }, //从服务器读取数据
  
    loadData: function() {
      console.log("in");
      this.$get(this.url, {
        pageNum: this.currentPage,
        pageSize: this.pagesize
      })
        .then((res) =>{
          console.log(res.data.data.data.pageGYGGZDData);
          this.tableData = res.data.data.data.pageGYGGZDData;
          this.totalCount = res.data.data.data.number;
        })
        .catch(error => {
          /*  this.$Notice.error({
            title: '错误',
            desc: '网络连接错误'
          }) */
          console.log(error);
        });
    },

    //多选响应
    handleSelectionChange: function(val) {
      this.multipleSelection = val;
    },

    //点击行响应
    handleclick: function(row, event, column) {
      this.highlightId = row.id;
    },

    //编辑
    handleEdit: function(index, row) {
      this.dialogTableVisible = true;
      this.GYGG_GUID=row.GYGG_GUID;
      /* this.$prompt("请输入新名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      })
        .then(({ value }) => {
          if (value == "" || value == null) return;
          this.$http
            .post(
              "newstu/update",
              { id: row.id, name: value },
              { emulateJSON: true }
            )
            .then(
              function(res) {
                this.loadData(this.criteria, this.currentPage, this.pagesize);
              },
              function() {
                console.log("failed");
              }
            );
        })
        .catch(() => {}); */
    },

    //单行删除
    handleDelete: function(index, row) {
      var array = [];
      array.push(row.id);
      this.$http
        .post("newstu/delete", { array: array }, { emulateJSON: true })
        .then(
          function(res) {
            this.loadData(this.criteria, this.currentPage, this.pagesize);
          },
          function() {
            console.log("failed");
          }
        );
    },

    //搜索
    search: function() {
      this.loadData(this.criteria, this.currentPage, this.pagesize);
    },

    //添加
    add: function() {
      this.$prompt("请输入名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
      })
        .then(({ value }) => {
          if (value == "" || value == null) return;
          this.$http
            .post("newstu/add", { name: value }, { emulateJSON: true })
            .then(
              function(res) {
                this.loadData(this.criteria, this.currentPage, this.pagesize);
              },
              function() {
                console.log("failed");
              }
            );
        })
        .catch(() => {});
    },

    //多项删除
    deletenames: function() {
      if (this.multipleSelection.length == 0) return;
      var array = [];
      this.multipleSelection.forEach(item => {
        array.push(item.id);
      });
      this.$post("newstu/delete", { array: array }, { emulateJSON: true }).then(
        function(res) {
          this.loadData(this.criteria, this.currentPage, this.pagesize);
        },
        function() {
          console.log("failed");
        }
      );
    },

    //改变当前点击的行的class，高亮当前行
    tableRowClassName: function(row, index) {
      if (row.id == this.highlightId) {
        return "info-row";
      }
    },

    //每页显示数据量变更
    handleSizeChange: function(val) {
      this.pagesize = val;
      this.loadData(this.criteria, this.currentPage, this.pagesize);
    },

    //页码变更
    handleCurrentChange: function(val) {
      this.currentPage = val;
      this.loadData(this.criteria, this.currentPage, this.pagesize);
    }
  }
}; //loadData(this.criteria, this.currentPage, this.pagesize);
</script>