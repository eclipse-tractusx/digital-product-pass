import { CX_REGISTRY_URL } from "@/services/service.const";
import axios from "axios";


export default class aasServices {

  constructor(){
        
  }
  getAasShellId(assetIds, requestHeaders) {
    return new Promise((resolve) => {
      let encodedAssetIds = encodeURIComponent(assetIds);
      axios.get(`${CX_REGISTRY_URL}/registry/lookup/shells?assetIds=${encodedAssetIds}`,
        {
          headers: requestHeaders
        }
      )
        .then((response) => {
          console.log("PassportView (AAS Shell Id):", response.data);
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e);
          resolve("rejected");
        });
    });
  }
  getShellDescriptor(aasShellId, requestHeaders) {
    return new Promise((resolve) => {
      axios
        .get(`${CX_REGISTRY_URL}/registry/registry/shell-descriptors/${aasShellId}`,
          {
            headers: requestHeaders
          }
        )
        .then((response) => {
          console.log("PassportView (Shell Descriptor Object):", response.data);
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e);
          resolve("rejected");
        });
    });
  }
  getSubmodelDescriptor(shellDescriptor, requestHeaders) {
    return new Promise((resolve) => {
      axios
        .get(
          `${CX_REGISTRY_URL}/registry/registry/shell-descriptors/${shellDescriptor.identification}/submodel-descriptors/${shellDescriptor.submodelDescriptors[0].identification}`,
          {
            headers: requestHeaders
          }
        )
        .then((response) => {
          console.log("PassportView (SubModel):", response.data);
          resolve(response.data);
        })
        .catch((e) => {
          this.errors.push(e);
          resolve("rejected");
        });
    });
  }
}
