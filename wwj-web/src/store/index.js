import Vue from 'vue'
import Vuex from 'vuex'
 
Vue.use(Vuex)
const key = 'user'
const store = new Vuex.Store({
  state : {
    username:window.localStorage.getItem('user' || '[]') == null ?'' : JSON.parse(window.localStorage.getItem('user' || '[]')).username,
    token:'',
    rid:-1,
    adminMenus:[]
  },
  mutations: {
    setUserName(state,username){
      state.username = username
    },
    setUserToken(state,token){
      state.token = token
    },
    setUserRole(state,role){
      state.rid = role
    },
    initAdminMenu(state,menu){
      state.adminMenus = menu
    },
    logout(state,user){
      state.username=null,
      state.token = '',
      state.rid = -1,
      state.adminMenus = []
      window.localStorage.removeItem('user')
    }
  }
})
 
export default store