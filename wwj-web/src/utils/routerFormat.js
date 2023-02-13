export function  formatRoutes(routes){
    let fmtRoutes = []
    routes.forEach(routes => {
      if (routes.child) {
        routes.child = formatRoutes(routes.child)
      }
  
      let fmtRoute = {
        path: routes.path,
        component: resolve => {
          require(['@/components/admin/' + routes.component + '.vue'], resolve)
        },
        name: routes.name,
        nameZh: routes.nameZh,
        iconCls: routes.icon,
        children: routes.child
      }
      fmtRoutes.push(fmtRoute)
    })
    return fmtRoutes
  }