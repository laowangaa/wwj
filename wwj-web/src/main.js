// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import ElementUI from 'element-ui'
import { Message } from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'
import { get, post,  del } from './utils/http'
import store from './store'
import initAdminMenu from '@/utils/initMenu'
import echarts from 'echarts'

Vue.use(ElementUI)
Vue.use(Message)
Vue.use(axios)
Vue.use(echarts)
Vue.prototype.$echarts = echarts
Vue.config.productionTip = false
Vue.prototype.$http = axios
Vue.prototype.$message = Message
Vue.http = axios

//  封装好的axios
// Vue.get = get
Vue.prototype.$get = get
Vue.prototype.$post = post

Vue.prototype.$del = del


router.beforeEach((to, from, next) => {
  if (store.state.token && (to.name == 'adminDashboard')) {
    axios.get('/user/getAuth').then(resp => {
      initAdminMenu(router, store)
    })
  }
  if (to.meta.requireAuth) {//如果目标路由需要认证
    axios.get('/user/getAuth').then(result => { //向后端请求当前用户的状态
      if (result.data == 1) { //如果后端返回1，代表当前用户已登录授权
        next()
      } else {
        alert("您还未登录")
        next({
          path: '/login'
        })
      }
    })
  } else {
    next()
  }
}
)
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
