import Vue from 'vue';
import Keycloak from 'keycloak-js'
import { INIT_OPTIONS } from "@/services/service.const"
/// -------------------------------  ///

// export default {
    
// }




///  --------------------------------  ///

//const _keycloak = new Keycloak(INIT_OPTIONS)

// const service = {
//     install(Vue) {
//         Vue.$keycloak = _keycloak
//     }
// }
// const object1 = {};
// service.install = Vue => {
//     Vue.$keycloak = _keycloak
//     Object.defineProperties(_keycloak,  {
//         $keyclock: {
//           get (){
//             return _keycloak
//           }
//         }
//       });
// }

// console.log(service)
// console.log(_keycloak)


//Vue.use(service);

// export default service

// export default {
//     install: (app, options) => {
//       // inject a globally available $translate() method
//       return app.config.globalProperties.$keycloak = keycloak
//     }
//   }


export default {
    install: (Vue, options) => {
        //app.$keycloak = keycloak
        // Vue.config.globalProperties.$keycloak = () => {
        //     return _keycloak
            
        // }
    Vue.$keycloak = _keycloak
    Object.defineProperties(_keycloak,  {
        $keyclock: {
          get (){
            return _keycloak
          }
        }
      });
      console.log(keycloak)
        

    }
};

// const Plugin = {
//     install: Vue => {
//         Vue.$keycloak = keycloak;
//     },
// };
// Plugin.install = Vue => {
//     Vue.$keycloak = keycloak;
//     Object.defineProperties(Vue.prototype, {
//         $keycloak: {
//             get() {
//                 return keycloak;
//             },
//         },
//     });
// };


//Vue.use(Plugin);

// export default Plugin;