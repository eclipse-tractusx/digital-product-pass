import { CX_REGISTRY_URL } from "@/services/service.const";
import axios from "axios";


export default class AasServices {

  getAasShellId(assetIds, requestHeaders) {
    return new Promise((resolve) => {

      axios.get(`${CX_REGISTRY_URL}/registry/lookup/shells?assetIds=${assetIds}`,
        {
          headers: requestHeaders
        }
      )
        .then((response) => {
          console.log("PassportView (AAS Shell Id):", response.data);
          resolve(response.data);
        })
        .catch((e) => {
          console.error("getAasShellId -> " + e);
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
          console.error("getShellDescriptor -> " + e);
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
          console.error("getSubmodelDescriptor ->" + e);
          resolve("rejected");
        });
    });
  }
}
