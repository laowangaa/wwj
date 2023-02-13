import {get} from '@/utils/http'
import {formatRoutes} from '@/utils/routerFormat'
export default function initAdminMenu(router, store) {
    if (store.state.adminMenus.length > 0) {
      return
    }
    
    get('/menu/get/' + store.state.rid).then(resp => {
        var fmtRoutes = formatRoutes(resp.data)
        router.addRoutes(fmtRoutes)
        store.commit('initAdminMenu', fmtRoutes)
    })
  }