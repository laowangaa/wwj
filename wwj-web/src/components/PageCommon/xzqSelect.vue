<template>
  <div>
   
      <tree-select style="width:180px;float:left;"
        :options="options"
        placeholder="请选择分类..."
        v-model="xzq"
      />
  
    <!-- <Card>
      {{ value }}
    </Card> -->
  </div>
</template>

<script>
  import { mapActions } from 'vuex'
  import TreeSelect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: 'xzqSelect',
    components: { TreeSelect },
    data() {
      return {
        xzq: 370000,
        options: []
      }
    },
    methods: {
      ...mapActions([
        'albumCategoryList'
      ]),

      queryCategoryList () {
         this.$get("/sysCanton/getXZQ",null).then((res) => {
  
          if (res.status == 200) {
             
            let result = []
            const children = this.getTree(res.data.data.array.child)
         
          
            let obj = {}
            obj.label = '山东省',
            obj.id = 370000,
            obj.children = children
            result.push(obj)
            this.options = result
          } else {
            this.$Notice.error({
              title: '错误',
              desc: res.msg
            })
          }
        }).catch(error => {
         /*  this.$Notice.error({
            title: '错误',
            desc: '网络连接错误'
          }) */
          console.log(error)
        })
      },

      getTree (tree) {
        
        let arr = [];
        if (tree != null) {
          tree.forEach(item => {
            let obj = {};
            obj.label = item.name;
            obj.id = item.no;
          
            if(item.child!=null) {
              obj.children = this.getTree(item.child);
            }
            arr.push(obj);
          });
        }
        return arr
      }
    },
   
 mounted () {
      this.queryCategoryList()
    }
  }
</script>

<style scoped>

</style>