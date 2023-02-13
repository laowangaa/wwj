<style scoped>
h2 {
  text-align:center;
  margin-top: 30px;
  margin-right: 490px;
  padding: 30px;
  font-size: 18px;
}
#chart_bing {
  width: 40%;
  height: 320px; margin-top: 75px;
  float: left;
  margin-left: 10px;
}
#chart_zhu {
  width: 48%;
  margin-top: 75px;
  height: 320px;
  float: right;
}
</style>
<template>
  <div>
    <el-col :span="2">
      <span class="filter_title">起始时间 :</span>
    </el-col>
    <el-col :span="6" class="itemBox">
      <el-date-picker
        v-model="useDate"
        type="daterange"
        unlink-panels
        value-format="yyyy-MM-dd"
        range-separator="至"
        start-placeholder="起始日期"
        end-placeholder="结束日期"
        size="small"
        @change="selectDatarange"
        :picker-options="pickerOptions"
      ></el-date-picker>
    </el-col>
    <h2>vue中插入Echarts示例</h2>
    <div id="chart_bing" v-loading="pageLoading"></div>
    <div id="chart_zhu" v-loading="pageLoading"></div>
  </div>
</template>
<script>
//import echarts from 'echarts'
export default {
  data() {
    var day2 = new Date();
    day2.setTime(day2.getTime());
    var s2 = day2.getFullYear()+"-" + (day2.getMonth()+1) + "-" + day2.getDate();
    return { 
      pageLoading:true,
      useDate: ['2015-01-01',s2],
      bingData:[],
      xzq:[],
      zmj:[],
      xhmj:[],
      czl:[]
    };
  },
  mounted() {
    this.drawBing();
    this.drawZhu();
  },
  created(){
    this.selectDatarange();
  },
  methods: {
    selectDatarange() {
      var param = {
        date0:this.useDate[0],
        date1:this.useDate[1]
      }
     
       this.$post("/xzrw/Bing", param)
        .then(res => {
            let data = res.data.data.data;
       
          let xzqname = ["商服用地", "工矿仓储用地", "住宅用地", "其他用地"];
          for(let i = 0;i<data.length;i++){
            let qwe = {};
            qwe.name=xzqname[i];
            qwe.value=data[i].MJ;
            this.bingData.push(qwe);
          }
          this.drawBing();
        }).catch(error => {
          /*  this.$Notice.error({
            title: '错误',
            desc: '网络连接错误'
          }) */
          console.log(error);
        });
       this.$post("/xzrw/Zhu", param)
        .then(req => {
           let data1 = req.data.data.data;
           for(let i = 0;i<data1.length;i++){
             this.xhmj.push(data1[i].XHMJ);
             this.zmj.push(data1[i].ZMJ);
             this.xzq.push(data1[i].NAME);
             this.czl.push(data1[i].CZL);
           }
            
           this.drawZhu();
           this.pageLoading=false;
        }).catch(error => {
          /*  this.$Notice.error({
            title: '错误',
            desc: '网络连接错误'
          }) */
          console.log(error);
        });
      //选择日期范围后的回调
     /*  this.filterData.issueBeginDate = this.useDate[0];
      this.filterData.issueEndDate = this.useDate[1]; */
    },
    drawBing() {
      let this_ = this;
      let myChart = this.$echarts.init(
        document.getElementById("chart_bing")
      );
      let option = {
        title: {
          text: "闲置处置结构",
          left: "center"
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
          orient: "vertical",
          left: "left",
          data: ["商服用地", "工况仓储用地", "住宅用地", "其他用地"]
        },
        series: [
          {
            name: "访问来源",
            type: "pie",
            radius: "55%",
            left: ["50%", "60%"],
            data: this.bingData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)"
              }
            }
          }
        ]
      };
      myChart.setOption(option);
      //建议加上以下这一行代码，不加的效果图如下（当浏览器窗口缩小的时候）。超过了div的界限（红色边框）
      window.addEventListener("resize", function() {
        myChart.resize();
      });
    },
    drawZhu() {
      let this_ = this;
      let myChart = this.$echarts.init(
        document.getElementById("chart_zhu")
      );
      let option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            crossStyle: {
              color: "#999"
            }
          }
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ["line", "bar"] },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        legend: {
          data: ["总面积", "销号面积", "处置率"]
        },
        xAxis: [
          {
            type: "category",
            data: this.xzq,
            axisPointer: {
              type: "shadow"
            }
          }
        ],
        yAxis: [
          {
            type: "value",
            name: "面积",
          /*   min: 0,
            max: 250,
            interval: 50, */
            axisLabel: {
              formatter: "{value}"
            }
          },
          {
            type: "value",
            name: "处置率",
           /*  min: 0,
            max: 25,
            interval: 5, */
            axisLabel: {
              formatter: "{value} %"
            }
          }
        ],
        series: [
          {
            name: "总面积",
            type: "bar",
            data: this.zmj
          },
          {
            name: "销号面积",
            type: "bar",
            data: this.xhmj
          },
          {
            name: "处置率",
            type: "line",
            yAxisIndex: 1,
            data: this.czl
          }
        ]
      };

      myChart.setOption(option);
      //建议加上以下这一行代码，不加的效果图如下（当浏览器窗口缩小的时候）。超过了div的界限（红色边框）
      window.addEventListener("resize", function() {
        myChart.resize();
      });
    }
  },
  watch: {}
 
};
</script>

